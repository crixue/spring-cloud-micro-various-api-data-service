package com.xrj.springcloud.variousapidata.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.xrj.springcloud.variousapidata.common.ServerResponse;
import com.xrj.springcloud.variousapidata.vo.Weather;

public interface WeatherDataService {

	ServerResponse<Weather> getWeatherDataByCityName(String cityName);

	ServerResponse<Weather> getWeatherDataByCityId(String CityId);


}
