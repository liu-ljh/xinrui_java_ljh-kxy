package org.xinrui.config.database;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.xinrui.config.MyBatisAutoConfig;

import javax.sql.DataSource;

/**
 * frame数据库数据源配置
 * @author Administrator
 */
@Configuration
@MapperScan(basePackages = "org.xinrui.frame.mapper", sqlSessionTemplateRef = "frameDbSqlSessionTemplate")
public class FrameDataSourceConfig {

	@Autowired
	private MyBatisAutoConfig myBatisPlusConfig;

	@Autowired
	@Qualifier(DataSourceConfig.FRAME_DB_DATASOURCE_BEAN_NAME)
	private DataSource frameDbDataSource;

	@Bean("frameSqlSessionFactory")
	public SqlSessionFactory frameDbSqlSessionFactory() throws Exception {
		MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
		factoryBean.setDataSource(frameDbDataSource);

		MybatisConfiguration configuration = new MybatisConfiguration();
		configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
		factoryBean.setConfiguration(configuration);
		//指定xml路径.
		factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:org/xinrui/frame/mapper/*Mapper.xml"));
		factoryBean.setTypeAliasesPackage("org.xinrui.frame");
		factoryBean.setPlugins(new Interceptor[]{
			myBatisPlusConfig.mybatisPlusInterceptor()
		});

		GlobalConfig globalConfig = new GlobalConfig();
		globalConfig.setMetaObjectHandler(myBatisPlusConfig);
		factoryBean.setGlobalConfig(globalConfig);
		return factoryBean.getObject();
	}

	@Bean("frameDbSqlSessionTemplate")
	public SqlSessionTemplate frameDbSqlSessionTemplate() throws Exception {
		SqlSessionTemplate template = new SqlSessionTemplate(frameDbSqlSessionFactory());
		return template;
	}


	@Bean("frameJdbcTemplate")
	public JdbcTemplate frameJdbcTemplate() {
		return new JdbcTemplate(frameDbDataSource);
	}

	@Bean("frameDataSourceTransactionManager")
	public DataSourceTransactionManager dataSourceTransactionManager() {
		return new DataSourceTransactionManager(frameDbDataSource);
	}
}
