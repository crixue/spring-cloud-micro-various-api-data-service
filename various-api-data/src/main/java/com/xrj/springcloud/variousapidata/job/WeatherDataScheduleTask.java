package com.xrj.springcloud.variousapidata.job;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.xrj.springcloud.variousapidata.service.CityDataService;
import com.xrj.springcloud.variousapidata.service.WeatherDataService;
import com.xrj.springcloud.variousapidata.vo.City;

/**
 * 获取天气数据的定时任务
 * @author crixus
 *
 */
@Configuration
@Component
public class WeatherDataScheduleTask {
	private static final Logger logger =  LoggerFactory.getLogger(WeatherDataScheduleTask.class);
	
	@Autowired
	private CityDataService cityDataService;
	@Autowired
	private WeatherDataService weatherDataService;

	public void syncWeatherData() {
		try {
			logger.info("[WeatherDataScheduleTask] 获取各个城市天气数据 - [start]");
			List<City> cityList = cityDataService.listCity();
			
			for(City city: cityList) {
				String cityId = city.getCityId();
				weatherDataService.syncWeatherDataAtScheduledTime(cityId);
				logger.debug("[WeatherDataScheduleTask] 正在获取 cityId 为 {} 的数据。", cityId);
			}
			logger.info("[WeatherDataScheduleTask] 获取各个城市天气数据 - [end]");
		} catch (Exception e) {
			logger.error("WeatherDataScheduleTask invoke syncWeatherData throw a exception: ", e);
		}
		
		
	}
	
}
