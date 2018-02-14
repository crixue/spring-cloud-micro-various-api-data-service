package com.xrj.springcloud.variousapidata.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xrj.springcloud.variousapidata.common.RedisService;
import com.xrj.springcloud.variousapidata.vo.City;
import com.xrj.springcloud.variousapidata.vo.WeatherResponse;

/**
 * 获取天气数据的定时任务
 * @author crixus
 *
 */
@Configuration
@Component
public class WeatherDataScheduleTask {
	private static final Logger logger =  LoggerFactory.getLogger(WeatherDataScheduleTask.class);
	private static final int RESPONSE_SUCCESS = 200;
	private static final String WEATHER_URL_PREFIX = "http://wthrcdn.etouch.cn/weather_mini?";
	private static final Long EXPIRE_MINUTES = 60L;
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private RedisService redisService;

	public void syncWeatherData() {
		try {
			logger.info("[WeatherDataScheduleTask] 定时获取各个城市天气数据 - [start]");
			//TODO 获取citylist数据
			List<City> cityList = new ArrayList<City>();
			City cityTemp = new City();
			cityTemp.setCityId("101280101");
			cityList.add(cityTemp);
			
			for(City city: cityList) {
				String cityId = city.getCityId();
				syncWeatherDataAtScheduledTime(cityId);
				logger.debug("[WeatherDataScheduleTask] 正在获取 cityId 为 {} 的数据。", cityId);
			}
			logger.info("[WeatherDataScheduleTask] 定时获取各个城市天气数据 - [end]");
		} catch (Exception e) {
			logger.error("WeatherDataScheduleTask invoke syncWeatherData throw a exception: ", e);
		}
	}
	
	
	private void syncWeatherDataAtScheduledTime(String CityId) throws JsonParseException, JsonMappingException, IOException {
		String url = WEATHER_URL_PREFIX + "citykey=" + CityId;
		WeatherResponse weatherResponse = getWeatherResponse(url);
		redisService.set(url, weatherResponse, EXPIRE_MINUTES, TimeUnit.MINUTES);
	}
	
	/**
	 * 根据url获取响应请求
	 * @param url
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private WeatherResponse getWeatherResponse(String url) throws JsonParseException, JsonMappingException, IOException {
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
		if(responseEntity.getStatusCodeValue() != RESPONSE_SUCCESS){
			logger.error("访问api接口失败，请尝试重新访问");
			throw new RuntimeException("访问api接口失败，请尝试重新访问");
		}
		
		WeatherResponse weatherResponse = mapper.readValue(responseEntity.getBody(), WeatherResponse.class);
		return weatherResponse;
	}
	
}
