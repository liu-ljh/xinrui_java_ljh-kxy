package org.xinrui.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xinrui.service.IPacsIdissService;


/**
 *
 * 白云二院传染病上报（检查部分）定时上传任务
 * 同步频率：医院信息系统保存数据后的当日24点前
 * 触发时机：检查报告结果后当天
 * @author Administrator
 */
@Slf4j
@Component
public class PacsIdissSchedule {

	@Autowired
	private IPacsIdissService iPacsIdissService;

	/**
	 * 每晚23点59分上传当天的检查报告项目数据
	 */
	@Scheduled(cron = "0 59 23 * * ?")
	public void uploadEmrExClinicalData() {
		// 上传开始时间
		long startTime = System.currentTimeMillis();
		log.info("【uploadEmrExClinicalData】开始定时上传检查报告数据...");
		try {
			iPacsIdissService.uploadEmrExClinicalData();
		} catch (Exception e) {
			log.error("【uploadEmrExClinicalData】任务执行失败, errorMsg:{}", e);
		} finally {
			log.info("【uploadEmrExClinicalData】任务结束, 耗时{}毫秒", System.currentTimeMillis() - startTime);
		}
	}


	/**
	 * 每晚23点59分上传当天的检查报告项目数据
	 */
	@Scheduled(cron = "0 59 23 * * ?")
	public void uploadEmrExClinicalItemData() {
		// 上传开始时间
		long startTime = System.currentTimeMillis();
		log.info("【uploadEmrExClinicalItemData】开始定时上传检查报告项目数据...");
		try {
			iPacsIdissService.uploadEmrExClinicalItemData();
		} catch (Exception e) {
			log.error("【uploadEmrExClinicalItemData】任务执行失败, errorMsg:{}", e);
		} finally {
			log.info("【uploadEmrExClinicalData】任务结束, 耗时{}毫秒", System.currentTimeMillis() - startTime);
		}
	}


	/**
	 * 定时清理redis缓存数据
	 * 每天12点执行一次
	 */
	@Scheduled(cron = "0 0 12 * * ?")
	public void clearRedisScheduled() {
		iPacsIdissService.clearRedis();
	}

}
