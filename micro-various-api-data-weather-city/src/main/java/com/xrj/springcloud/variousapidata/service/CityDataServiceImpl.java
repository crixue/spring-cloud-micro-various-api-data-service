package com.xrj.springcloud.variousapidata.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.xrj.springcloud.variousapidata.common.ServerResponse;
import com.xrj.springcloud.variousapidata.util.XmlBuilder;
import com.xrj.springcloud.variousapidata.vo.City;
import com.xrj.springcloud.variousapidata.vo.CityList;

@Service
public class CityDataServiceImpl implements CityDataService {

	private List<City> listCity() throws Exception {
		//读取xml文件
		Resource resource = new ClassPathResource("cityList.xml");
		BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), "utf-8"));
		
		StringBuffer sb = new StringBuffer("");
		String line = StringUtils.EMPTY;
		while((line =  br.readLine()) != null) {
			sb.append(line);
		}
		
		if(br != null) {
			br.close();
		}
		
		//将xml转换成对象
		Object obj = XmlBuilder.xmlStrToOject(CityList.class, sb.toString());
		CityList cityList = null;
		if(obj instanceof CityList) {
			cityList = (CityList)obj;
		}
		
		return cityList.getCityList();
	}
	
	@Override
	public ServerResponse<List<City>> getCitiesResponse() throws Exception {
		List<City> cityList = listCity();
		
		if(!cityList.isEmpty()) {
			return ServerResponse.createBySucessResReturnData(cityList);
		}
		return ServerResponse.createByErrorResReturnMsg("未能正确获得数据");
		
	}
	
}
