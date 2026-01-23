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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.xinrui.config.MyBatisAutoConfig;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "org.pacsdcom.**.mapper", sqlSessionTemplateRef = "pacsdcomSqlSessionTemplate")
public class PacsdcomDataSourceConfig {

	@Autowired
	private MyBatisAutoConfig myBatisAutoConfig;

	@Autowired
	@Qualifier(DataSourceConfig.PACSCDCOM_DB_DATASOURCE_BEAN_NAME)
	private DataSource pacsdcomDataSource;

	@Bean("pacsdcomSqlSessionFactory")
	public SqlSessionFactory pacsdcomdbSqlSessionFactory() throws Exception {
		MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
		factoryBean.setDataSource(pacsdcomDataSource);

		MybatisConfiguration configuration = new MybatisConfiguration();
		configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
		factoryBean.setConfiguration(configuration);
		//指定xml路径.
		factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:org/pacsdcom/mapper/*Mapper.xml"));
		factoryBean.setTypeAliasesPackage("org.pacsdcom.entity");
		factoryBean.setPlugins(new Interceptor[]{
			myBatisAutoConfig.mybatisPlusInterceptor()
		});

		GlobalConfig globalConfig = new GlobalConfig();
		globalConfig.setMetaObjectHandler(myBatisAutoConfig);
		factoryBean.setGlobalConfig(globalConfig);
		return factoryBean.getObject();
	}


	@Bean("pacsdcomSqlSessionTemplate")
	public SqlSessionTemplate pacsdcomdbSqlSessionTemplate() throws Exception {
		return new SqlSessionTemplate(pacsdcomdbSqlSessionFactory());
	}

	@Bean("pacsdcomDataSourceTransactionManager")
	public DataSourceTransactionManager pacsDataSourceTransactionManager() {
		return new DataSourceTransactionManager(pacsdcomDataSource);
	}
}
