package org.xinrui.config.database;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 类
 *
 * @author jiangxianliang
 * @since 2021/5/12
 */
@Configuration
public class DataSourceConfig {
	/**
	 * PSI 库连接配置前缀 (与 yml 中的连接信息前缀一致 , 下同)
	 */
	private static final String MASTER_DATASOURCE_PREFIX = "spring.datasource.druid.master";
	/**
	 * PSI 库连接Bean名字
	 */
	public static final String MASTER_DATASOURCE_BEAN_NAME = "master";

	/**
	 * pacs图像数据库连接配置前缀
	 */
	private static final String PACSDCOM_DB_DATASOURCE_PREFIX = "spring.datasource.druid.pacsdcom";
	/**
	 * pacs图像数据库连接Bean名字
	 */
	public static final String PACSCDCOM_DB_DATASOURCE_BEAN_NAME = "pacsdcom";

	/**
	 * 新篩 frame 库中间库连接配置前缀
	 */
	private static final String FRAME_DB_DATASOURCE_PREFIX = "spring.datasource.druid.frame";
	/**
	 * 新篩 frame 库中间库连接Bean名字
	 */
	public static final String FRAME_DB_DATASOURCE_BEAN_NAME = "frame";


	@Primary
	@Bean(name = MASTER_DATASOURCE_BEAN_NAME)
	@ConfigurationProperties(prefix = MASTER_DATASOURCE_PREFIX)
	public DruidDataSource masterDataSource() {
		return new DruidDataSource();
	}


	@Bean(name = PACSCDCOM_DB_DATASOURCE_BEAN_NAME)
	@ConfigurationProperties(prefix = PACSDCOM_DB_DATASOURCE_PREFIX)
	public DruidDataSource pacsdcomDataSource() {
		return new DruidDataSource();
	}

	@Bean(name = FRAME_DB_DATASOURCE_BEAN_NAME)
	@ConfigurationProperties(prefix = FRAME_DB_DATASOURCE_PREFIX)
	public DruidDataSource frameDataSource() {
		return new DruidDataSource();
	}
}
