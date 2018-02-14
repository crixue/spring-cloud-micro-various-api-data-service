package com.xrj.springcloud.variousapidata.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.xrj.springcloud.variousapidata.common.ServerResponse;
import com.xrj.springcloud.variousapidata.vo.City;
import com.xrj.springcloud.variousapidata.vo.Weather;

/**
 * 远程调用网关api
 * @author crixus
 *
 */
@FeignClient(name="micro-various-api-data-client-zuul", fallback=DataClientZuulFallback.class)
public interface DataClientZuul {
	
	@GetMapping("/city/api-weather-data/city-list")
	ServerResponse<List<City>> getCityList();
	
	@GetMapping("/weather-data/api-weather-data/cityId/{cityId}")
	ServerResponse<Weather> getWeatherDataByCityId(@PathVariable("cityId")String cityId);

	@GetMapping("/weather-data/api-weather-data/cityName/{cityName}")
	ServerResponse<Weather> getWeatherDataByCityName(@PathVariable("cityName")String cityName);
}
