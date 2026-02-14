package org.xinrui.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 传染病上报项目配置类
 * @author Administrator
 */
@Data
@Component
@ConfigurationProperties(prefix = "pacs-idiss")
public class PacsIdissConfig {
	private String ip;
	private String port;

	public String getUrl() {
		return "http://" + ip + ":" + port;
	}
}

