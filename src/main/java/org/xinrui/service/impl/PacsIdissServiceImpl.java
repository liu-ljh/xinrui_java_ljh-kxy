package org.xinrui.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.xinrui.config.PacsIdissConfig;
import org.xinrui.core.tool.utils.DateUtil;
import org.xinrui.core.tool.utils.Func;
import org.xinrui.dto.EmrExClinicalDto;
import org.xinrui.dto.EmrExClinicalItemDto;
import org.xinrui.dto.ResultDTO;
import org.xinrui.entity.EmrExClinical;
import org.xinrui.entity.EmrExClinicalItem;
import org.xinrui.mapper.EmrExClinicalItemMapper;
import org.xinrui.mapper.EmrExClinicalMapper;
import org.xinrui.service.IPacsIdissService;
import org.xinrui.util.CommonUtil;
import org.xinrui.util.EmrExClinicalItemUtil;
import org.xinrui.util.EmrExClinicalUtil;
import org.xinrui.util.ModalityMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 传染病上报服务实现类
 * @author Administrator
 */
@Slf4j
@AllArgsConstructor
@Service
public class PacsIdissServiceImpl implements IPacsIdissService {

	/** 分页大小设置 */
	private static final int PAGE_SIZE = 50;
	/** redis key */
	private static final String REDIS_INFECTION_REPORT_DATA = "pacs-idiss:INFECTION_REPORT_DATA";
	private static final String REDIS_INFECTION_ITEM_DATA = "pacs-idiss:INFECTION_ITEM_DATA";

	private final PacsIdissConfig pacsIdissConfig;
	private final StringRedisTemplate stringRedisTemplate;

	private final EmrExClinicalMapper emrExClinicalMapper;

	private final EmrExClinicalItemMapper emrExClinicalItemMapper;


	@Override
	public void uploadEmrExClinicalData() {
		// 设置查询日期参数
		Date today = new Date();
		Date startTime = DateUtil.getStartOfDate(today);
		Date endTime = DateUtil.getEndOfDate(today);
		Map<String, Date> queryParams = new HashMap<>();
		queryParams.put("startTime", startTime);
		queryParams.put("endTime", endTime);
		// 设置分页查询参数
		int currentPage = 1;
		Page<EmrExClinical> page = new Page<>(currentPage, PAGE_SIZE, false);
		String dateStr = DateUtil.format(today, "yyyyMMdd");
		String uploadReportRedisKey = REDIS_INFECTION_REPORT_DATA + ":" + dateStr;
		// 上传检查报告数据
		while (true) {
			// 数据库查询报告数据
			List<EmrExClinical> pageData = emrExClinicalMapper.selectEmrExClinical(page, queryParams);
			if (pageData == null || pageData.isEmpty()) {
				break;
			}
			// 构建检查报告数据
			for (EmrExClinical emrExClinicalDatum : pageData) {
				try {
					// 构建检查报告数据
					EmrExClinicalDto reportDatum = buildEmrExClinicalDto(emrExClinicalDatum);

					// 若数据已经上传过，则跳过
					String reportKeyVal = reportDatum.getId();
					if (stringRedisTemplate.opsForSet().isMember(uploadReportRedisKey, reportKeyVal)) {
						continue;
					}

					// 上传检查报告数据
					log.info("【uploadEmrExClinicalData】开始上传检查报告数据, reportDatum:{}", Func.toJson(reportDatum));
					boolean uploadSuccess = postEmrExClinical(reportDatum);

					// 上传成功则将key存到redis，避免重复上传
					if (uploadSuccess) {
						stringRedisTemplate.opsForSet().add(uploadReportRedisKey, reportKeyVal);
					}
				} catch (Exception e) {
					// 捕获单条数据处理异常，避免中断整个批次
					log.error("【uploadEmrExClinicalData】第{}/{}页，id为{}，数据上传异常：{}", currentPage, pageData.size(), emrExClinicalDatum.getOid(), e.getMessage());
				}
			}
			// 如果当前页数据量小于分页大小，说明已经是最后一页了
			if (pageData.size() < PAGE_SIZE) {
				break;
			}
			// 准备查询下一页
			page.setCurrent(++currentPage);
		}
	}

	@Override
	public void uploadEmrExClinicalItemData() {
		//设置查询日期参数
		Date today = new Date();
		Date startTime = DateUtil.getStartOfDate(today);
		Date endTime = DateUtil.getEndOfDate(today);
		//设置分页查询参数
		int currentPage = 1;
		// 不查询总记录数以提升性能
		Page<EmrExClinicalItem> page = new Page<>(currentPage, PAGE_SIZE, false);
		String dateStr = DateUtil.format(today, "yyyyMMdd");
		String uploadItemRedisKey = REDIS_INFECTION_ITEM_DATA + ":" + dateStr;
		// 上传检查报告项目数据
		while (true) {
			//数据库查询报告项目数据
			List<EmrExClinicalItem> pageData = emrExClinicalItemMapper.selectEmrExClinicalItem(page, startTime, endTime);
			if (pageData == null || pageData.isEmpty()) {
				break;
			}
			// 构建检查报告项目数据
			for (EmrExClinicalItem itemDatum : pageData) {
				try {
					EmrExClinicalItemDto itemDto = buildEmrExClinicalItemDto(itemDatum);

					//用redis查询是否已经上传过，上传过则跳过本次数据上传
					String itemKeyVal = itemDto.getId();
					if (stringRedisTemplate.opsForSet().isMember(uploadItemRedisKey, itemKeyVal)) {
						continue;
					}

					//上传检查报告项目数据
					log.info("【uploadEmrExClinicalItemData】开始上传检查报告项目数据, itemDto:{}", Func.toJson(itemDto));
					boolean uploadSuccess = postEmrExClinicalItem(itemDto);
					if (uploadSuccess) {
						stringRedisTemplate.opsForSet().add(uploadItemRedisKey, itemKeyVal);
					}
				} catch (Exception e) {
					// 捕获单条数据处理异常，避免中断整个批次
					log.error("【uploadEmrExClinicalItemData】第{}/{}页，id为{}，数据上传异常：{}", currentPage, pageData.size(), itemDatum.getOid(), e.getMessage());
				}
			}
			// 如果当前页数据量小于分页大小，说明已经是最后一页了
			if (pageData.size() < PAGE_SIZE) {
				break;
			}
			// 准备查询下一页
			page.setCurrent(++currentPage);
		}
	}

	/**
	 * 上传检查报告数据
	 * @param emrExClinicalDatum
	 * @return
	 */
	boolean postEmrExClinical(EmrExClinicalDto emrExClinicalDatum) {
		try {
			String apiUrl = pacsIdissConfig.getUrl() + "/hclient/emr/receive/emrExClinical";
			//测试使用testUrl
//			String apiUrl = "http://localhost:8081/hclient/emr/receive/emrExClinical";
			String result = CommonUtil.sendPostRequest(apiUrl, emrExClinicalDatum);
			ResultDTO resultDTO = CommonUtil.convertToResultDTO(result);

			// 处理响应
			if (resultDTO != null && resultDTO.getResult()){
				log.info("【uploadEmrExClinicalApi】上传成功：param:{}, result:{}",
					Func.toJson(emrExClinicalDatum),
					Func.toJson(resultDTO));
				return true;
			} else {
				log.error("【uploadEmrExClinicalItemApi】上传失败：param:{}, result:{}",
					Func.toJson(emrExClinicalDatum),
					Func.toJson(resultDTO));
				return false;
			}
		} catch (Exception e){
			log.error("【postEmrExClinicalItem】请求异常：param:{}, errMsg:{}",
					Func.toJson(emrExClinicalDatum), e);
			return false;
		}
	}

	/**
	 * 上传检查报告项目数据
	 * @param emrExClinicalItemDatum
	 * @return
	 */
	boolean postEmrExClinicalItem(EmrExClinicalItemDto emrExClinicalItemDatum) {
		try {
			String apiUrl = pacsIdissConfig.getUrl()+"/hclient/emr/receive/emrExClinicalItem";
			String result = CommonUtil.sendPostRequest(apiUrl, emrExClinicalItemDatum);
			ResultDTO resultDTO = CommonUtil.convertToResultDTO(result);

			// 处理响应
			if (resultDTO != null && resultDTO.getResult()){
				log.info("【uploadEmrExClinicalItemApi】上传成功：param:{}, result:{}",
					Func.toJson(emrExClinicalItemDatum),
					Func.toJson(resultDTO));
				return true;
			} else {
				log.error("【uploadEmrExClinicalItemApi】上传失败：param:{}, result:{}",
					Func.toJson(emrExClinicalItemDatum),
					Func.toJson(resultDTO));
				return false;
			}
		} catch (Exception e){
			log.error("【postEmrExClinicalItem】请求异常：param:{}, errMsg:{}",
					Func.toJson(emrExClinicalItemDatum), e);
			return false;
		}
	}

	/**
	 * 构建检查报告数据
	 * @param reportDatum
	 * @return
	 */
	private static EmrExClinicalDto buildEmrExClinicalDto(EmrExClinical reportDatum) {
		EmrExClinicalDto emrExClinicalDto = new EmrExClinicalDto();
		emrExClinicalDto.setId(String.valueOf(reportDatum.getOid()));
		emrExClinicalDto.setPatientId(reportDatum.getPatientId() );
		emrExClinicalDto.setActivityTypeCode(convertPatientSourceType(reportDatum.getPatientSourceType()));
		emrExClinicalDto.setActivityTypeName(EmrExClinicalUtil.convertActivityType(reportDatum.getPatientSourceType()));
		String serialNumber = reportDatum.getOutpatientNo() != null ? reportDatum.getOutpatientNo() : reportDatum.getAdmissionNo();
		emrExClinicalDto.setSerialNumber(serialNumber);
		emrExClinicalDto.setPatientName(reportDatum.getName() );
		emrExClinicalDto.setIdCardTypeCode(EmrExClinicalUtil.idCardTypeCodeJudge(reportDatum.getPersonId()));
		emrExClinicalDto.setIdCardTypeName(EmrExClinicalUtil.idCardTypeNameJudge(reportDatum.getPersonId()));
		emrExClinicalDto.setIdCard(reportDatum.getPersonId());

		emrExClinicalDto.setExaminationTypeCode(EmrExClinicalUtil.convertExaminationTypeCode(reportDatum.getModality()));
		emrExClinicalDto.setExaminationTypeName(EmrExClinicalUtil.convertExaminationTypeName(reportDatum.getModality()));
		emrExClinicalDto.setExaminationReportNo(reportDatum.getReportId() );
		emrExClinicalDto.setExaminationReportDate(reportDatum.getReportDate());
		emrExClinicalDto.setExaminationReportId(String.valueOf(reportDatum.getReader()));
		emrExClinicalDto.setOrgCode(reportDatum.getTenantOid());
		emrExClinicalDto.setOrgName(EmrExClinicalUtil.convertOrgCode(reportDatum.getTenantOid()));
		emrExClinicalDto.setDeptName(reportDatum.getApplicationDeptName());
		emrExClinicalDto.setDeptCode(EmrExClinicalUtil.getDeptNameByCode(reportDatum.getApplicationDeptName()));
		emrExClinicalDto.setOperationTime(reportDatum.getReportDate());
		return emrExClinicalDto;
	}

	/**
	 * 转换患者来源类型
	 * 	pacs：1门诊 2住院 3体检 4急诊 9其他
	 * 转换为 传染病上报字典-就诊记录类型代码：
	 * 1 门诊 2 急诊 3 留观入观 4 留观出观 5 入院 6 住院 7 首次病程 8 日常病程 9 出院
	 * @param patientSourceType
	 * @return
	 */
	private static String convertPatientSourceType(String patientSourceType) {
		if (Func.isEmpty(patientSourceType)) {
			return "10";
		}

		switch (patientSourceType) {
			// 门诊
			case "1":
				return "1";
			// 住院
			case "2":
				return "6";
			// 急诊
			case "4":
				return "2";
			case "3":
			case "9":
				return "10";
			default:
				return "10";
		}
	}

	/**
	 * 构建检查报告项目数据
	 * @param itemDatum
	 * @return
	 */
	private static EmrExClinicalItemDto buildEmrExClinicalItemDto(EmrExClinicalItem itemDatum) {
		EmrExClinicalItemDto emrExClinicalItemDto = new EmrExClinicalItemDto();
		emrExClinicalItemDto.setId(String.valueOf(itemDatum.getOid()));
		//处理报告id字段
		emrExClinicalItemDto.setExClinicalId(itemDatum.getReportId());
		//处理检查项目名称和检查项目代码
		String modality = itemDatum.getModality();
		emrExClinicalItemDto.setItemCode(ModalityMapping.getItemCode(modality));
		emrExClinicalItemDto.setItemName(ModalityMapping.getItemName(modality));
		//处理检查结果代码
		Integer positive = itemDatum.getPositive();
		emrExClinicalItemDto.setExaminationResultCode(EmrExClinicalItemUtil.getExaminationResultCode(positive));
		//处理检查结果名称
		emrExClinicalItemDto.setExaminationResultName(EmrExClinicalItemUtil.getExaminationResultName(positive));
		//处理操作时间
		emrExClinicalItemDto.setOperationTime(itemDatum.getReportDate());
		return emrExClinicalItemDto;
	}


	@Override
	public void clearRedis() {
		// 清理16天前的缓存数据
		Date now = new Date();
		String uploadExClinicalItemRedisKey = REDIS_INFECTION_ITEM_DATA + ":" + DateUtil.format(DateUtil.minusDays(now,16), "yyyyMMdd");
		String uploadExClinicalRedisKey = REDIS_INFECTION_REPORT_DATA + ":" + DateUtil.format(DateUtil.minusDays(now,16), "yyyyMMdd");
		stringRedisTemplate.delete(uploadExClinicalRedisKey);
		stringRedisTemplate.delete(uploadExClinicalItemRedisKey);
	}

}
