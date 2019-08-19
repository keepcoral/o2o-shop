package com.bujidao.config.dao;

import java.io.IOException;

import javax.sql.DataSource;

import org.junit.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.bujidao.util.PathUtil;

@Configuration
public class SessionFactoryConfiguration {

	//配置mybatis-config.xml配置文件路径
	private static String mybatisConfigFile;
	@Value("${mybatis_config_file}")
	public void setMybatisConfigFile(String mybatisConfigFile) {
		SessionFactoryConfiguration.mybatisConfigFile = mybatisConfigFile;
	}

	//mybatis mapper文件所在路径
	private static String mapperPath;
	@Value("${mapper_path}")
	public void setMapperPath(String mapperPath) {
		SessionFactoryConfiguration.mapperPath = mapperPath;
	}
	
	//实体类所在的package,并使用别名
	@Value("${type_alias_package}")
	private String typeAliasesPackage;
	
	//因为要用到dataSource，我们在已经配置了@Bean了
	@Autowired
	private DataSource dataSource;
	
	/**
	 * 创建sqlSessionFactoryBean实例并设置configuration设置mapper映射路径
	 * @throws IOException 
	 */
	@Bean(name="sqlSessionFactory")
	public SqlSessionFactoryBean createSqlSessionFactoryBean() throws IOException{
		SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
		//设置mybatis configuration 扫描路径
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisConfigFile));
		//设置mapper扫描路径
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver=
					new PathMatchingResourcePatternResolver();
		String packageSearchPath=ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX+mapperPath;
		sqlSessionFactoryBean.setMapperLocations
			(pathMatchingResourcePatternResolver.getResources(packageSearchPath));
		//设置dataSource
		sqlSessionFactoryBean.setDataSource(dataSource);
		//扫描entity包 使用别名
		sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
		return sqlSessionFactoryBean;
	}
	
}
