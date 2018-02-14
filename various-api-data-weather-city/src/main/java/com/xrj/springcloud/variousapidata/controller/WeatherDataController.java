package com.xrj.springcloud.variousapidata.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xrj.springcloud.variousapidata.common.ServerResponse;
import com.xrj.springcloud.variousapidata.service.CityDataService;

@RestController
@RequestMapping("/api-weather-data")
public class WeatherDataController {
	private static final Logger logger = LoggerFactory.getLogger(WeatherDataController.class);
	
	@Autowired
	private CityDataService cityDataService;
	
	@GetMapping("/city-list")
	public ServerResponse<?> listCity() {
		ServerResponse<?> response = null;
		
		try {
			response = cityDataService.getCitiesResponse();
		} catch (Exception e) {
			logger.error("");
		}
		return response;
	}
	

}
