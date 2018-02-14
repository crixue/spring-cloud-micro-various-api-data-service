package com.xrj.springcloud.variousapidata.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xrj.springcloud.variousapidata.common.ServerResponse;
import com.xrj.springcloud.variousapidata.service.CityDataService;
import com.xrj.springcloud.variousapidata.vo.City;

@RestController
@RequestMapping("/api-weather-data")
public class WeatherDataController {
	private static final Logger logger = LoggerFactory.getLogger(WeatherDataController.class);
	
	@Autowired
	private CityDataService cityDataService;
	
	@GetMapping("/city-list")
	public ServerResponse<List<City>> listCity() {
		ServerResponse<List<City>> response = null;
		
		try {
			response = cityDataService.getCitiesResponse();
		} catch (Exception e) {
			logger.error("获取城市数据异常");
		}
		return response;
	}
	

}
