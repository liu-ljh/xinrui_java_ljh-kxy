package org.xinrui.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xinrui.service.IPacsInfectionService;
import org.xinrui.service.impl.PacsInfectionServiceImpl;

@Component
@Slf4j
public class RedisCleanScheduledJob {
	@Autowired
	private IPacsInfectionService iPacsInfectionService;

	/**
	 * 定时清理redis缓存数据
	 * 每天12点执行一次
	 */
	@Scheduled(cron = "0 0 12 * * ?")
	public void clearRedisScheduled() {
		iPacsInfectionService.clearRedis();
	}
}
