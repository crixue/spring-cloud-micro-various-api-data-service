package com.xrj.springcloud.variousapidata.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xrj.springcloud.variousapidata.common.RedisService;
import com.xrj.springcloud.variousapidata.common.ServerResponse;
import com.xrj.springcloud.variousapidata.vo.Weather;
import com.xrj.springcloud.variousapidata.vo.WeatherResponse;

@Service
public class WeatherDataServiceImpl implements WeatherDataService {
	
	private static final Logger logger = LoggerFactory.getLogger(WeatherDataServiceImpl.class);
	private static final int RESPONSE_SUCCESS = 200;
	private static final String WEATHER_URL_PREFIX = "http://wthrcdn.etouch.cn/weather_mini?";
	private static final Long EXPIRE_MINUTES = 60L;
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private RedisService redisService;

	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public ServerResponse<Weather> getWeatherDataByCityName(String cityName) {
		String url = WEATHER_URL_PREFIX + "city=" + cityName;
		WeatherResponse weatherResponse = null;
		try {
			weatherResponse = getWeatherCacheData(url);
		} catch (IOException e) {
			logger.error("解析json数据异常，异常原因：{}", e);
		}
		return convertCommonServerResponse(weatherResponse);
	}


	@Override
	public ServerResponse<Weather> getWeatherDataByCityId(String CityId) {
		String url = WEATHER_URL_PREFIX + "citykey=" + CityId;
		WeatherResponse weatherResponse = null;
		try {
			weatherResponse = getWeatherCacheData(url);
		} catch (IOException e) {
			logger.error("解析json数据异常，异常原因：{}", e);
		}
		return convertCommonServerResponse(weatherResponse);
	}
	
	@Override
	public void syncWeatherDataAtScheduledTime(String CityId) throws JsonParseException, JsonMappingException, IOException {
		String url = WEATHER_URL_PREFIX + "citykey=" + CityId;
		WeatherResponse weatherResponse = getWeatherResponse(url);
		redisService.set(url, weatherResponse, EXPIRE_MINUTES, TimeUnit.MINUTES);
	}
	
	/**
	 * 从Redis中获取数据，如果没有则直接请求，并存储
	 * 作为一种强依赖与其他api数据，Redis缓存一方面提高及时响应的能力，另一方面能够见减少对第三方服务调用，提高并发能力
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	private WeatherResponse getWeatherCacheData(String url) throws IOException, JsonParseException, JsonMappingException {
		WeatherResponse weatherResponse;
		if(redisService.exists(url)) {
			weatherResponse = mapper.readValue(redisService.get(url), WeatherResponse.class);
		} else {
			weatherResponse = getWeatherResponse(url);
			redisService.set(url, weatherResponse, EXPIRE_MINUTES, TimeUnit.MINUTES);
		}
		return weatherResponse;
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
		
//		String weatherData = new String(responseEntity.getBody().getBytes(), "GBK");
		WeatherResponse weatherResponse = mapper.readValue(responseEntity.getBody(), WeatherResponse.class);
		return weatherResponse;
	}
	
	/**
	 * 将其他类型的响应统一转换成同一响应类型
	 * @param weatherResponse
	 * @return
	 */
	private ServerResponse<Weather> convertCommonServerResponse(WeatherResponse weatherResponse) {
		Weather weather = null;
		ServerResponse<Weather> serverResponse = null;
		if(weatherResponse == null) {
			logger.info("天气接口数据为空");
			serverResponse = ServerResponse.createByErrorResReturnMsg("获得接口数据为空");
		}
		
		weather = weatherResponse.getData();
		serverResponse = ServerResponse.createBySucessResReturnData(weather);
		return serverResponse;
	}
	
	
}
