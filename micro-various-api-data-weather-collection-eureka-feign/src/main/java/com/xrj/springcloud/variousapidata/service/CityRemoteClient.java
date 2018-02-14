package com.xrj.springcloud.variousapidata.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.xrj.springcloud.variousapidata.common.ServerResponse;
import com.xrj.springcloud.variousapidata.vo.City;

@FeignClient(name="micro-various-api-data-weather-city-eureka-feign")  //调用的远程服务应用名称
public interface CityRemoteClient {
	
	@GetMapping("/api-weather-data/city-list")
	ServerResponse<List<City>> getCities();

}
