package com.xrj.springcloud.variousapidata.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * ScheduleTask 作为 quartz的定时任务对象被jobDetail定时执行
 * @Configuration @Component 必须要有
 * 而 @EnableScheduling 是spring特有的schedule执行必须要有的注释
 * @author crixus
 *
 */
@Configuration
@Component
//@EnableScheduling
public class ScheduleTask {

	 private static final Logger logger =  LoggerFactory.getLogger(ScheduleTask.class);
	 
	 public void sayHello() {
		 logger.info("hello spring boot!");
	 }
	 
	 
	 public void sayHi() {
		 logger.info("hi world!");
	 }
}
