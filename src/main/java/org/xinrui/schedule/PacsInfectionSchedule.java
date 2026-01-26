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
import org.springframework.web.client.RestTemplate;
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


	private static final int PAGE_SIZE = 50;


	@Autowired
	private EmrExClinicalMapper emrExClinicalMapper;


	@Autowired
	private EmrExClinicalItemMapper emrExClinicalItemMapper;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private IPacsInfectionService iPacsInfectionService;

	public static final String REDIS_INFECTION_REPORT_DATA = "baiyun2yuan-pacs-web:INFECTION_REPORT_DATA";
	public static final String REDIS_INFECTION_ITEM_DATA = "baiyun2yuan-pacs-web:INFECTION_ITEM_DATA";


	/**
	 * 每晚23点59分上传当天的检查报告项目数据
	 */
	@Scheduled(cron = "0 59 23 * * ?")
	public void uploadEmrExClinicalData() {
		log.info("【uploadEmrExClinicalData】开始上传检查报告数据");

		 //设置查询日期参数
		Date today = new Date();
		Date startTime = DateUtil.getStartOfDate(today);
		Date endTime = DateUtil.getEndOfDate(today);

		Map<String, Date> queryParams = new HashMap<>();
		queryParams.put("startTime", startTime);
		queryParams.put("endTime", endTime);

		// 设置分页查询参数
		int currentPage = 1;
		Page<EmrExClinical> page = new Page<>(1, PAGE_SIZE, true);

		// 上传检查报告数据
		while (true) {
			// 数据库查询报告数据
			List<EmrExClinical> pageData = emrExClinicalMapper.selectEmrExClinical(page, queryParams);
			if (pageData == null || pageData.isEmpty()) {
				break;
			}
			try {
				// 构建检查报告数据
				for (EmrExClinical emrExClinicalDatum : pageData) {
					EmrExClinicalDto reportDatum = buildEmrExClinicalDto(emrExClinicalDatum);
					String uploadReportRedisKey = REDIS_INFECTION_REPORT_DATA + ":" + DateUtil.format(today, "yyyyMMdd");
					String reportKeyVal = reportDatum.getId();
					if (stringRedisTemplate.opsForSet().isMember(uploadReportRedisKey, reportKeyVal)) {
						continue;
					}
					// 上传检查报告数据
					boolean b = iPacsInfectionService.postEmrExClinical(reportDatum);
					if (b) {
						stringRedisTemplate.opsForSet().add(uploadReportRedisKey, reportKeyVal);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("【uploadEmrExClinicalData】第{}/{}页上传异常：{}",
					currentPage, page.getPages(), e.getMessage(), e);
			}
			// 如果当前页已经是最后一页了，就退出
			if (currentPage >= page.getPages()) {
				break;
			}
			// 准备查询下一页
			page.setCurrent(++currentPage);
		}
	}


	/**
	 * 每晚23点59分上传当天的检查报告项目数据
	 */
	@Scheduled(cron = "0 59 23 * * ?")
	public void uploadEmrExClinicalItemData() {
		//设置查询日期参数
		Date today= new Date();
		Date startTime = DateUtil.getStartOfDate(today);
		Date endTime = DateUtil.getEndOfDate(today);
		//设置分页查询参数
		int currentPage = 1;
		Page<EmrExClinicalItem> page = new Page<>(1,PAGE_SIZE,true);
		// 上传检查报告项目数据
		log.info("【uploadEmrExClinicalItemData】开始上传检查报告项目数据");
		while (true){
			//数据库查询报告项目数据
			List<EmrExClinicalItem> pageData = emrExClinicalItemMapper.selectEmrExClinicalItem(page,startTime,endTime);
			page.setCurrent(++currentPage);
			if(currentPage>page.getPages()||pageData==null||pageData.isEmpty()){
				break;
			}
			try{
				// 构建检查报告项目数据
				for(EmrExClinicalItem emrExClinicalItemDatum:pageData){
					//用redis查询是否已经上传过，上传过则跳过本次数据上传
					EmrExClinicalItemDto itemDatum = buildEmrExClinicalItemDto(emrExClinicalItemDatum);
					String uploadItemRedisKey =REDIS_INFECTION_ITEM_DATA+":"+DateUtil.format(today,"yyyyMMdd");
					String itemKeyVal = itemDatum.getId();
					if(stringRedisTemplate.opsForSet().isMember(uploadItemRedisKey,itemKeyVal)){
						continue;
					}
					//上传检查报告项目数据
					boolean b = iPacsInfectionService.postEmrExClinicalItem(itemDatum);
					if(b){
						stringRedisTemplate.opsForSet().add(uploadItemRedisKey,itemKeyVal);
					}
				}
			}catch (Exception e){
				log.error("【uploadEmrExClinicalItemDataApi】第{}/{}页上传异常：{}",
					currentPage,page.getPages(),e.getMessage());
			}
		}
	}

	/**
	 * 构建检查报告数据
	 * @return
	 */
	private EmrExClinicalDto buildEmrExClinicalDto(EmrExClinical reportDatum) {
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
	private EmrExClinicalItemDto buildEmrExClinicalItemDto(EmrExClinicalItem itemDatum) {
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
