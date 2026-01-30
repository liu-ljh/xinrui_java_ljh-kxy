package org.xinrui.schedule;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xinrui.core.tool.utils.DateUtil;
import org.xinrui.dto.EmrExClinicalDto;
import org.xinrui.dto.EmrExClinicalItemDto;
import org.xinrui.entity.EmrExClinical;
import org.xinrui.entity.EmrExClinicalItem;
import org.xinrui.mapper.EmrExClinicalItemMapper;
import org.xinrui.mapper.EmrExClinicalMapper;
import org.xinrui.pacs.mapper.PacsExamReportMapper;
import org.xinrui.service.IPacsInfectionService;
import org.xinrui.util.EmrExClinicalItemUtil;
import org.xinrui.util.EmrExClinicalUtil;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 *
 * 白云二院传染病上报（检查部分）定时上传任务
 * 同步频率：医院信息系统保存数据后的当日24点前
 * 触发时机：检查报告结果后当天
 * @author Administrator
 */
@Slf4j
@Component
public class PacsInfectionSchedule {

	@Autowired
	private EmrExClinicalMapper emrExClinicalMapper;

	@Autowired
	private EmrExClinicalItemMapper emrExClinicalItemMapper;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private IPacsInfectionService iPacsInfectionService;

	// 分页大小配置
	public static final int PAGE_SIZE = 50;

	// Redis键配置
	public static final String REDIS_INFECTION_REPORT_DATA = "baiyun2yuan-pacs-web:INFECTION_REPORT_DATA";
	public static final String REDIS_INFECTION_ITEM_DATA = "baiyun2yuan-pacs-web:INFECTION_ITEM_DATA";


	/**
	 * 每晚23点59分上传当天的检查报告项目数据
	 */
	@Scheduled(cron = "0 59 23 * * ?")
	@Scheduled(cron = "0 59 23 * * ?")
	public void uploadEmrExClinicalData() {
		log.info("【uploadEmrExClinicalData】任务开始，准备上传检查报告数据");
		//设置查询日期参数
		Date today = new Date();
		Date startTime = DateUtil.getStartOfDate(today);
		Date endTime = DateUtil.getEndOfDate(today);
		Map<String, Date> queryParams = new HashMap<>();
		queryParams.put("startTime", startTime);
		queryParams.put("endTime", endTime);
		// 设置分页查询参数
		int currentPage = 1;
		Page<EmrExClinical> page = new Page<>(currentPage, PAGE_SIZE, false); // 不查询总记录数以提升性能
		String dateStr = DateUtil.format(today, "yyyyMMdd");
		String uploadReportRedisKey = REDIS_INFECTION_REPORT_DATA + ":" + dateStr;
		try {
			// 上传检查报告数据
			while (true) {
				log.debug("【uploadEmrExClinicalData】正在查询第 {} 页数据", currentPage);
				// 数据库查询报告数据
				List<EmrExClinical> pageData = emrExClinicalMapper.selectEmrExClinical(page, queryParams);
				if (pageData == null || pageData.isEmpty()) {
					log.info("【uploadEmrExClinicalData】第 {} 页无数据，查询结束", currentPage);
					break;
				}
				log.info("【uploadEmrExClinicalData】第 {} 页查询到 {} 条数据，开始处理", currentPage, pageData.size());
				// 构建检查报告数据
				for (EmrExClinical emrExClinicalDatum : pageData) {
					String oid = String.valueOf(emrExClinicalDatum.getOid());
					try {
						EmrExClinicalDto reportDatum = buildEmrExClinicalDto(emrExClinicalDatum);
						String reportKeyVal = reportDatum.getId();
						// 校验ID是否有效，避免Redis key为null或"null"
						if (reportKeyVal == null || "null".equals(reportKeyVal)) {
							log.warn("【uploadEmrExClinicalData】数据ID无效，跳过上传。OID：{}", oid);
							continue;
						}
						if (stringRedisTemplate.opsForSet().isMember(uploadReportRedisKey, reportKeyVal)) {
							log.debug("【uploadEmrExClinicalData】数据已存在，跳过上传。ID：{}", reportKeyVal);
							continue;
						}
						// 上传检查报告数据
						boolean uploadSuccess = iPacsInfectionService.postEmrExClinical(reportDatum);
						if (uploadSuccess) {
							stringRedisTemplate.opsForSet().add(uploadReportRedisKey, reportKeyVal);
							log.debug("【uploadEmrExClinicalData】数据上传成功。ID：{}", reportKeyVal);
						} else {
							log.warn("【uploadEmrExClinicalData】接口返回上传失败。ID：{}", reportKeyVal);
						}
					} catch (Exception e) {
						// 捕获单条数据处理异常，避免中断整个批次
						log.error("【uploadEmrExClinicalData】处理单条数据异常，OID：{}，错误信息：{}", oid, e.getMessage(), e);
					}
				}
				// 如果当前页数据量小于分页大小，说明已经是最后一页了
				if (pageData.size() < PAGE_SIZE) {
					log.info("【uploadEmrExClinicalData】第 {} 页数据量不足一页，判定为最后一页", currentPage);
					break;
				}
				// 准备查询下一页
				page.setCurrent(++currentPage);
			}
		} catch (Exception e) {
			log.error("【uploadEmrExClinicalData】任务执行发生严重异常：{}", e.getMessage(), e);
		} finally {
			log.info("【uploadEmrExClinicalData】任务执行结束");
		}
	}


	/**
	 * 每晚23点59分上传当天的检查报告项目数据
	 */
	@Scheduled(cron = "0 59 23 * * ?")
	public void uploadEmrExClinicalItemData() {
		log.info("【uploadEmrExClinicalItemData】任务开始，准备上传检查报告项目数据");
		//设置查询日期参数
		Date today = new Date();
		Date startTime = DateUtil.getStartOfDate(today);
		Date endTime = DateUtil.getEndOfDate(today);
		//设置分页查询参数
		int currentPage = 1;
		Page<EmrExClinicalItem> page = new Page<>(currentPage, PAGE_SIZE, false); // 不查询总记录数以提升性能
		String dateStr = DateUtil.format(today, "yyyyMMdd");
		String uploadItemRedisKey = REDIS_INFECTION_ITEM_DATA + ":" + dateStr;
		try {
			// 上传检查报告项目数据
			while (true) {
				log.debug("【uploadEmrExClinicalItemData】正在查询第 {} 页数据", currentPage);
				//数据库查询报告项目数据
				List<EmrExClinicalItem> pageData = emrExClinicalItemMapper.selectEmrExClinicalItem(page, startTime, endTime);
				if (pageData == null || pageData.isEmpty()) {
					log.info("【uploadEmrExClinicalItemData】第 {} 页无数据，查询结束", currentPage);
					break;
				}
				log.info("【uploadEmrExClinicalItemData】第 {} 页查询到 {} 条数据，开始处理", currentPage, pageData.size());
				try {
					// 构建检查报告项目数据
					for (EmrExClinicalItem itemDatum : pageData) {
						String oid = String.valueOf(itemDatum.getOid());
						try {
							EmrExClinicalItemDto itemDto = buildEmrExClinicalItemDto(itemDatum);
							String itemKeyVal = itemDto.getId();
							// 校验ID是否有效
							if (itemKeyVal == null || "null".equals(itemKeyVal)) {
								log.warn("【uploadEmrExClinicalItemData】数据ID无效，跳过上传。OID：{}", oid);
								continue;
							}
							//用redis查询是否已经上传过，上传过则跳过本次数据上传
							if (stringRedisTemplate.opsForSet().isMember(uploadItemRedisKey, itemKeyVal)) {
								log.debug("【uploadEmrExClinicalItemData】数据已存在，跳过上传。ID：{}", itemKeyVal);
								continue;
							}
							//上传检查报告项目数据
							boolean uploadSuccess = iPacsInfectionService.postEmrExClinicalItem(itemDto);
							if (uploadSuccess) {
								stringRedisTemplate.opsForSet().add(uploadItemRedisKey, itemKeyVal);
								log.debug("【uploadEmrExClinicalItemData】数据上传成功。ID：{}", itemKeyVal);
							} else {
								log.warn("【uploadEmrExClinicalItemData】接口返回上传失败。ID：{}", itemKeyVal);
							}
						} catch (Exception e) {
							// 捕获单条数据处理异常，避免中断整个批次
							log.error("【uploadEmrExClinicalItemData】处理单条数据异常，OID：{}，错误信息：{}", oid, e.getMessage(), e);
						}
					}
				} catch (Exception e) {
					log.error("【uploadEmrExClinicalItemData】第 {} 页数据处理发生异常：{}", currentPage, e.getMessage(), e);
				}
				// 如果当前页数据量小于分页大小，说明已经是最后一页了
				if (pageData.size() < PAGE_SIZE) {
					log.info("【uploadEmrExClinicalItemData】第 {} 页数据量不足一页，判定为最后一页", currentPage);
					break;
				}
				// 准备查询下一页
				page.setCurrent(++currentPage);
			}
		} catch (Exception e) {
			log.error("【uploadEmrExClinicalItemData】任务执行发生严重异常：{}", e.getMessage(), e);
		} finally {
			log.info("【uploadEmrExClinicalItemData】任务执行结束");
		}
	}

	/**
	 * 构建检查报告数据
	 * @return
	 */
	private static EmrExClinicalDto buildEmrExClinicalDto(EmrExClinical reportDatum) {
		if (reportDatum == null) {
			throw new IllegalArgumentException("构建检查报告数据失败：入参 reportDatum 不能为空");
		}
		EmrExClinicalDto emrExClinicalDto = new EmrExClinicalDto();
		emrExClinicalDto.setId(String.valueOf(reportDatum.getOid()));
		emrExClinicalDto.setPatientId(reportDatum.getPatientId() );
		emrExClinicalDto.setActivityTypeCode(reportDatum.getPatientSourceType() );
		emrExClinicalDto.setActivityTypeName(EmrExClinicalUtil.convertActivityType(reportDatum.getPatientSourceType()));
		emrExClinicalDto.setSerialNumber(reportDatum.getApplicationId() );
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
		emrExClinicalDto.setDeptCode(reportDatum.getApplicationDeptOid());
		emrExClinicalDto.setDeptName(EmrExClinicalUtil.convertDeptCode(reportDatum.getApplicationDeptOid()));
		emrExClinicalDto.setOperationTime(new Date());
		return emrExClinicalDto;
	}

	/**
	 * 构建检查报告项目数据
	 * @return
	 */
	private static EmrExClinicalItemDto buildEmrExClinicalItemDto(EmrExClinicalItem itemDatum) {
		if (itemDatum == null) {
			throw new IllegalArgumentException("构建检查报告项目数据失败：入参 itemDatum 不能为空");
		}
		EmrExClinicalItemDto emrExClinicalItemDto = new EmrExClinicalItemDto();
		emrExClinicalItemDto.setId(String.valueOf(itemDatum.getOid()));
		//处理报告id字段
		emrExClinicalItemDto.setExClinicalId(itemDatum.getReportId());
		//处理检查项目名称和检查项目代码
		String modality = itemDatum.getModality();
		emrExClinicalItemDto.setItemCode(EmrExClinicalItemUtil.getItemCode(modality));
		emrExClinicalItemDto.setItemName(EmrExClinicalItemUtil.getItemName(modality));
		//处理检查结果代码
		Integer positive  =itemDatum.getPositive();
		emrExClinicalItemDto.setExaminationResultCode(EmrExClinicalItemUtil.getExaminationResultCode(positive));
		//处理检查结果名称
		emrExClinicalItemDto.setExaminationResultName(EmrExClinicalItemUtil.getExaminationResultName(positive));
		//处理操作时间
		emrExClinicalItemDto.setOperationTime(new Date());
		return emrExClinicalItemDto;
	}

}
