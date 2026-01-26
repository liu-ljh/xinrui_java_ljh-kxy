package org.xinrui.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "baiyun2yuan")
public class BaiYun2YuanConfig {
	private String ip;
	private String port;

	public String getUrl() {
		return "http://" + ip + ":" + port;
	}
}

