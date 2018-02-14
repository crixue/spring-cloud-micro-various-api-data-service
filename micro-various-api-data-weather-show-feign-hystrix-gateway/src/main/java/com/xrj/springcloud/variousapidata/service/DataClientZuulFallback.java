package com.xrj.springcloud.variousapidata.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xrj.springcloud.variousapidata.common.ServerResponse;
import com.xrj.springcloud.variousapidata.vo.City;
import com.xrj.springcloud.variousapidata.vo.Weather;

/**
 * 断路器hystrix实现快速失败降级返回类
 * @author crixus
 *
 */
@Component
public class DataClientZuulFallback implements DataClientZuul {

	private static final Logger logger = LoggerFactory.getLogger(DataClientZuulFallback.class);
	
	@Override
	public ServerResponse<List<City>> getCityList() {
		List<City> cityList = new ArrayList<>();
		City city = new City();
		city.setCityId("101280601");
		city.setCityName("深圳");
		cityList.add(city);
		logger.warn("远程获取城市微服务不可用！已降级直fallback类处理。");
		return ServerResponse.createBySucessResReturnData(cityList);
	}

	@Override
	public ServerResponse<Weather> getWeatherDataByCityId(String cityId) {
		logger.warn("远程获取天气微服务不可用！已降级直fallback类处理。");
		return null;
	}

	@Override
	public ServerResponse<Weather> getWeatherDataByCityName(String cityName) {
		logger.warn("远程获取天气微服务不可用！已降级直fallback类处理。");
		return null;
	}

}
