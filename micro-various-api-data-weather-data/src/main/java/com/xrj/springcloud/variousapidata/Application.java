package com.xrj.springcloud.variousapidata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Application {

	public static void main(String[] args) {
		System.out.println("hello spring boot");
		SpringApplication.run(Application.class, args);
	}
}
