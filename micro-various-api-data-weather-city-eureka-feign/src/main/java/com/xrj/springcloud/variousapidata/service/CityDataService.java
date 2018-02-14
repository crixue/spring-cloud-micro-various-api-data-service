package com.xrj.springcloud.variousapidata.service;

import java.util.List;

import com.xrj.springcloud.variousapidata.common.ServerResponse;
import com.xrj.springcloud.variousapidata.vo.City;

public interface CityDataService {

	ServerResponse<List<City>> getCitiesResponse() throws Exception;

}
