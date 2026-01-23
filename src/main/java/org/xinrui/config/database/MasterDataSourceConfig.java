package org.xinrui.config.database;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.xinrui.config.MyBatisAutoConfig;

import javax.sql.DataSource;

/**
 * 类
 *
 * @author jiangxianliang
 * @since 2021/5/12
 */
@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = "org.xinrui.pacs.mapper", sqlSessionTemplateRef = "masterSqlSessionTemplate")
public class MasterDataSourceConfig {

	@Autowired
	@Qualifier(DataSourceConfig.MASTER_DATASOURCE_BEAN_NAME)
	private DataSource masterDataSource;

	@Autowired
	private MyBatisAutoConfig myBatisAutoConfig;

	@Primary
	@Bean("masterSqlSessionFactory")
	public SqlSessionFactory masterSqlSessionFactory() throws Exception {
		MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
		factoryBean.setDataSource(masterDataSource);

		MybatisConfiguration configuration = new MybatisConfiguration();
		configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
		factoryBean.setObjectWrapperFactory(new MybatisMapWrapperFactory());
		factoryBean.setConfiguration(configuration);
		//指定xml路径.
		factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:org/xinrui/pacs/mapper/*Mapper.xml"));
		factoryBean.setTypeAliasesPackage("org.xinrui.pacs.entity");
		factoryBean.setPlugins(new Interceptor[]{
			myBatisAutoConfig.mybatisPlusInterceptor()
		});

		GlobalConfig globalConfig = new GlobalConfig();
		globalConfig.setMetaObjectHandler(myBatisAutoConfig);
		factoryBean.setGlobalConfig(globalConfig);

		return factoryBean.getObject();
	}


	@Primary
	@Bean("masterSqlSessionTemplate")
	public SqlSessionTemplate masterSqlSessionTemplate() throws Exception {
		return new SqlSessionTemplate(masterSqlSessionFactory());
	}

	@Bean("jdbcTemplate")
	public JdbcTemplate jdbcTemplate() throws Exception {
		return new JdbcTemplate(masterDataSource);
	}

	@Primary
	@Bean("masterDataSourceTransactionManager")
	public DataSourceTransactionManager dataSourceTransactionManager() {
		return new DataSourceTransactionManager(masterDataSource);
	}

}
