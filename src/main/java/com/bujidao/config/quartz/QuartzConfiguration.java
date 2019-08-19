package com.bujidao.config.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.bujidao.service.ProductSellDailyService;

@Configuration
public class QuartzConfiguration {
	@Autowired
	private ProductSellDailyService productSellDailyService;
	@Autowired
	private MethodInvokingJobDetailFactoryBean jobDetailFactory;
	@Autowired
	private CronTriggerFactoryBean productSellDailyTriggerFactory;
	
	/**
	 * 创建jobDetailFactory并返回
	 */
	@Bean(name="jobDetailFactory")
	public MethodInvokingJobDetailFactoryBean createJobDetailFactory(){
		//这个工厂主要是用来制作jobDetail,即制作一个任务
		MethodInvokingJobDetailFactoryBean bean=new MethodInvokingJobDetailFactoryBean();
		//设置上jobDetail的名字
		bean.setName("product_sell_daily_job");
		//设置jobDeatail的分组
		bean.setGroup("job_product_sell_daily_group");
		//对于相同的jobDetail当指定多个Trigger,很可能第一个job完成之前，第二个job就开始了
		//指定concurrent设为false,多个job不会并发运行，第二个job不会在第一个job完成之前开始
		bean.setConcurrent(false);
		//指定运行任务的类
		bean.setTargetObject(productSellDailyService);
		//指定运行类的方法
		bean.setTargetMethod("dailyCalculate");
		return bean;
	}
	
	/**
	 * 创建CronTrigger并返回
	 */
	@Bean(name="productSellDailyTriggerFactory")
	public CronTriggerFactoryBean createProductSellDailyTrigger(){
		CronTriggerFactoryBean bean=new CronTriggerFactoryBean();
		bean.setBeanName("product_sell_daily_trigger");
		//和jobDetail不一样也没关系，因为这是两个不同的类
		bean.setGroup("job_product_sell_daily_group");
		bean.setJobDetail(jobDetailFactory.getObject());
		//设置cron表达式,先用每3s打印一次测一下
//		bean.setCronExpression("0/3 * * * * ?");
		bean.setCronExpression("0 5 0 * * ? *");
		return bean;
	}

	/**
	 * 创建调度工厂并返回
	 */
	@Bean(name="schedulerFactory")
	public SchedulerFactoryBean createSchedulerFactory(){
		SchedulerFactoryBean bean=new SchedulerFactoryBean();
		bean.setTriggers(productSellDailyTriggerFactory.getObject());
		return bean;
	}
}
