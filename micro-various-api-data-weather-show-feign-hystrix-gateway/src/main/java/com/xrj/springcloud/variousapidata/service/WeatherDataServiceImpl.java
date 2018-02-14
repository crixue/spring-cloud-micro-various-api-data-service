package com.xrj.springcloud.variousapidata.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xrj.springcloud.variousapidata.common.ServerResponse;
import com.xrj.springcloud.variousapidata.vo.Weather;

@Service
public class WeatherDataServiceImpl implements WeatherDataService {
	
	private static final Logger logger = LoggerFactory.getLogger(WeatherDataServiceImpl.class);
	
	@Autowired
	private DataClientZuul dataClientZuul;

	@Override
	public ServerResponse<Weather> getWeatherDataByCityName(String cityName) throws Exception{
		ServerResponse<Weather> response = dataClientZuul.getWeatherDataByCityName(cityName);
		return response;
	}


	@Override
	public ServerResponse<Weather> getWeatherDataByCityId(String cityId) throws Exception{
		ServerResponse<Weather> response = dataClientZuul.getWeatherDataByCityId(cityId);
		return response;
	}
	
	
}
