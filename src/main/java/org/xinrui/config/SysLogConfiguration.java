package org.xinrui.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xinrui.core.launch.props.SysProperties;
import org.xinrui.core.launch.server.ServerInfo;
import org.xinrui.core.log.event.ApiLogListener;
import org.xinrui.core.log.event.ErrorLogListener;
import org.xinrui.core.log.event.UsualLogListener;
import org.xinrui.core.log.feign.ILogClient;

/**
 * 日志工具自动配置
 *
 * @author yzhangm
 */
@Configuration
@AllArgsConstructor
public class SysLogConfiguration {

	private final ILogClient logClient;
	private final ServerInfo serverInfo;
	private final SysProperties SysProperties;

	@Bean(name = "apiLogListener")
	public ApiLogListener apiLogListener() {
		return new ApiLogListener(logClient, serverInfo, SysProperties);
	}

	@Bean(name = "errorEventListener")
	public ErrorLogListener errorEventListener() {
		return new ErrorLogListener(logClient, serverInfo, SysProperties);
	}

	@Bean(name = "usualEventListener")
	public UsualLogListener usualEventListener() {
		return new UsualLogListener(logClient, serverInfo, SysProperties);
	}

}
