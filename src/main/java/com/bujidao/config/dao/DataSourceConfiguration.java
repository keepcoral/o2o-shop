package com.bujidao.config.dao;

import java.beans.PropertyVetoException;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bujidao.util.DESUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;
/**
 * 配置dataSource到ioc容器里面
 * @author wyq
 *
 */
@Configuration
//配置mybatis的扫描路径，和bean中的MapperScannerConfigurer一样
@MapperScan("com.bujidao.dao")
public class DataSourceConfiguration {
	//通过@Value标签就能从application.properties中读取值
	@Value("${jdbc.driver}")
	private String jdbcDriver;
	@Value("${jdbc.url}")
	private String jdbcUrl;
	@Value("${jdbc.username}")
	private String jdbcUsername;
	@Value("${jdbc.password}")
	private String jdbcPassword;
	
	@Bean(name="dataSource")
	public ComboPooledDataSource createDateSource() throws PropertyVetoException{
		//生成dataSource实例
		ComboPooledDataSource dataSource=new ComboPooledDataSource();
		//跟配置文件一样设置以下信息
		//驱动
		dataSource.setDriverClass(jdbcDriver);
		//数据库连接url
		dataSource.setJdbcUrl(jdbcUrl);
		//设置用户名
		dataSource.setUser(DESUtil.getDecryptString(jdbcUsername));
		//设置密码
		dataSource.setPassword(DESUtil.getDecryptString(jdbcPassword));
		//配置c3p0连接池的私有属性
		//连接池的最大线程数
		dataSource.setMaxPoolSize(30);
		//连接池最小连接数
		dataSource.setMinPoolSize(10);
		//关闭连接后不会自动commit
		dataSource.setAutoCommitOnClose(false);
		//连接超时时间
		dataSource.setCheckoutTimeout(10000);
		//连接失败重试次数
		dataSource.setAcquireRetryAttempts(2);
		//最大空闲时间,60秒内未使用则连接被丢弃。若为0则永不丢弃。Default: 0
		dataSource.setMaxIdleTime(60);
		return dataSource;
	}
}
