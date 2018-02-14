package com.xrj.microapidataclientfeign.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.xrj.microapidataclientfeign.common.ServerResponse;
import com.xrj.microapidataclientfeign.vo.City;

@FeignClient(name="micro-various-api-data-weather-city")
public interface CityClient {

	@GetMapping(value="/api-weather-data/city-list")
	ServerResponse<List<City>> getCityList();
	
}
