package com.xrj.microapidataclientfeign;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MicroApiDataClientFeignApplicationTests {
	
	@Value("${AppKey}")
	private String appKey;

	@Test
	public void contextLoads() {
		assertEquals("c82188d7c74b88c0adec20ce0f9162be", appKey);
	}

}
