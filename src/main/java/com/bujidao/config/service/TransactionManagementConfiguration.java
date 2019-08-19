package com.bujidao.config.service;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

/**
 * 对应spring-service.xml中的
 * <tx:annotation-driven transaction-manager="transactionManager"/>
 *	继承TransactionManagementConfigurer可以开启annotation-driven
 * @author wyq
 *
 */
@Configuration
@EnableTransactionManagement 
// 开启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
public class TransactionManagementConfiguration implements TransactionManagementConfigurer{

	//注入DataSourceConfiguration里面的DataSource,通过createDateSource()方法注入
	@Autowired
	private DataSource dataSource;
	
	/**
	 * 我们需要注入dataSource去创建DataSourceTransactionManager
	 * annotation-driven就需要DataSourceTransactionManager
	 * 关于事务管理，需要返回PlatformTransactionManager的实现
	 */
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

}
