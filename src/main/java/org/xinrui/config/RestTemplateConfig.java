package org.xinrui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置类
 * 用于配置和提供RestTemplate Bean实例
 * RestTemplate是Spring框架提供的用于执行HTTP请求的同步客户端工具
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 创建并配置RestTemplate Bean
     * 该方法将RestTemplate实例注册为Spring Bean，使其可以在整个应用程序中被注入和使用
     *
     * @return RestTemplate实例
     */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
