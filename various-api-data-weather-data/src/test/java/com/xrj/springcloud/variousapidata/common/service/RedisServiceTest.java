package com.xrj.springcloud.variousapidata.common.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xrj.springcloud.variousapidata.common.RedisService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceTest {

	@Autowired
	private RedisService redisService;
	
	@Test
	public void testSet() {
		if(!redisService.exists("hello")) {
			redisService.set("hello", "spring boot");
			System.out.println("has set hello to redis");
		}
	}
	
	@Test
	public void testGet() {
		if(redisService.exists("hello")) {
			String value = redisService.get("hello");
			System.out.println("has get key of hello from redis : " + value);
		}
	}
	
}
