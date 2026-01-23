package org.xinrui;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.xinrui.core.launch.SysApplication;

/**
 *
 * 启动器
 *
 * @author jxl
 */
@EnableScheduling
@SpringBootApplication(exclude = {
	DataSourceAutoConfiguration.class,
	HibernateJpaAutoConfiguration.class,
	DataSourceTransactionManagerAutoConfiguration.class,
	JpaRepositoriesAutoConfiguration.class
},scanBasePackages =  {"org.xinrui","org.guangzhou.link"})
public class Application {

	public static void main(String[] args) {
		SysApplication.run("xinrui-baiyun2yuan", Application.class, args);
	}

}

