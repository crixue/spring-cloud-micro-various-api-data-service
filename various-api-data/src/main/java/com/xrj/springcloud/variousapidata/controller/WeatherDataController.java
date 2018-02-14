package com.xrj.springcloud.variousapidata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xrj.springcloud.variousapidata.common.ServerResponse;
import com.xrj.springcloud.variousapidata.service.WeatherDataService;
import com.xrj.springcloud.variousapidata.vo.Weather;

@RestController
@RequestMapping("/api-weather-data")
public class WeatherDataController {
	
	@Autowired
	private WeatherDataService weatherDataService;
	
	@GetMapping("/cityId/{cityId}")
	public ServerResponse<Weather> getWeatherDataByCityId(@PathVariable("cityId")String cityId) {
		return weatherDataService.getWeatherDataByCityId(cityId);
	}
	
	@GetMapping("/cityName/{cityName}")
	public ServerResponse<Weather> getWeatherDataByCityName(@PathVariable("cityName")String cityName) {
		return weatherDataService.getWeatherDataByCityName(cityName);
	}
	

}
