package com.xrj.springcloud.variousapidata.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.xrj.springcloud.variousapidata.job.ScheduleTask;
import com.xrj.springcloud.variousapidata.job.WeatherDataScheduleTask;

@Configuration
public class QuartzConfig {
	
	/**
	 * 天气查询定时任务
	 * JobDetail表示一个具体的可执行的调度程序，Job是这个可执行程调度程序所要执行的内容，另外JobDetail还包含了这个任务调度的方案和策略。
	 * setConcurrent:是否并发执行 ,例如每5s执行一次任务，但是当前任务还没有执行完，就已经过了5s了，
	 * 		如果此处为true，则下一个任务会执行，如果此处为false，则下一个任务会等待上一个任务执行完后，再开始执行.
	 *  
	 * @param scheduledTask
	 * @return
	 */
	@Bean(name="weatherDataQuaryJobDetail")
	public MethodInvokingJobDetailFactoryBean weatherDataQuaryJobDetail(WeatherDataScheduleTask scheduledTask) {
		 MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean(); 
		 jobDetail.setConcurrent(false);
		 jobDetail.setName("schedule-quary-wearther-data");  //设置任务的名字  
		 jobDetail.setGroup("schedule-task");  //设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用  
		 jobDetail.setTargetObject(scheduledTask);  // 为需要执行的实体类对应的对象
		 jobDetail.setTargetMethod("syncWeatherData");  //ScheduledTask 执行的方法
		 return jobDetail;
		
	}
	
	/**
	 * Trigger代表一个调度参数的配置，什么时候去调。
	 *  setCronExpression的配置举例如下：
	 *  "0 0 12 * * ?" 每天中午12点触发
		"0 15 10 ? * *" 每天上午10:15触发
		"0 15 10 * * ?" 每天上午10:15触发
		"0 15 10 * * ? *" 每天上午10:15触发
		"0 15 10 * * ? 2005" 2005年的每天上午10:15触发
		"0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发
		"0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发
		"0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发
		"0 0-5 14 * * ?" 在每天下午2点到下午2:05期间的每1分钟触发
		"0 10,44 14 ? 3 WED" 每年三月的星期三的下午2:10和2:44触发
		"0 15 10 ? * MON-FRI" 周一至周五的上午10:15触发
		"0 15 10 15 * ?" 每月15日上午10:15触发
		"0 15 10 L * ?" 每月最后一日的上午10:15触发
		"0 15 10 ? * 6L" 每月的最后一个星期五上午10:15触发 
		"0 15 10 ? * 6L 2002-2005" 2002年至2005年的每月的最后一个星期五上午10:15触发
		"0 15 10 ? * 6#3" 每月的第三个星期五上午10:15触发
	 * @param jobDetail
	 * @return
	 */
	@Bean(name="weatherDataQuaryCronJobTrigger")
	public CronTriggerFactoryBean  weatherDataQuaryCronJobTrigger(@Qualifier("weatherDataQuaryJobDetail")MethodInvokingJobDetailFactoryBean jobDetail) {
		CronTriggerFactoryBean trigger = new CronTriggerFactoryBean(); 
		trigger.setJobDetail(jobDetail.getObject());
		trigger.setCronExpression("0 0/30 * * * ?");
		trigger.setName("weatherDataQuaryCronJobTrigger");
		return trigger;
		
	}
	
	
	/**
	 * Scheduler代表一个调度容器，一个调度容器中可以注册多个JobDetail和Trigger。
	 * 当Trigger与JobDetail组合，就可以被Scheduler容器调度了。
	 * @param cronJobTrigger
	 * @return
	 */
	@Bean
	public SchedulerFactoryBean schedulerFactory(@Qualifier("weatherDataQuaryCronJobTrigger")CronTriggerFactoryBean cronJobTrigger) {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setOverwriteExistingJobs(true);  //用于quartz集群,QuartzScheduler 启动时更新己存在的Job
		schedulerFactoryBean.setTriggers(cronJobTrigger.getObject());  //注册触发器 
		return schedulerFactoryBean;
		
	}
	

}
