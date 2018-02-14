package com.xrj.springcloud.variousapidata.service;

import com.xrj.springcloud.variousapidata.common.ServerResponse;
import com.xrj.springcloud.variousapidata.vo.Weather;

public interface WeatherDataService {

	ServerResponse<Weather> getWeatherDataByCityName(String cityName) throws Exception;

	ServerResponse<Weather> getWeatherDataByCityId(String CityId) throws Exception;

}
