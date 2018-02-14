package com.xrj.variousapidata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MicroVariousApiDataEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroVariousApiDataEurekaServerApplication.class, args);
	}
}
