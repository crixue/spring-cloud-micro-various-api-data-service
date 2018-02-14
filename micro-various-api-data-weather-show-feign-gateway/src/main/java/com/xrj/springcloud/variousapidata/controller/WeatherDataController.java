package com.xrj.springcloud.variousapidata.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.xrj.springcloud.variousapidata.common.ServerResponse;
import com.xrj.springcloud.variousapidata.service.DataClientZuul;
import com.xrj.springcloud.variousapidata.service.WeatherDataService;
import com.xrj.springcloud.variousapidata.vo.City;
import com.xrj.springcloud.variousapidata.vo.Weather;

@RestController
@RequestMapping("/report")
public class WeatherDataController {
	private final static Logger logger = LoggerFactory.getLogger(WeatherDataController.class);
	@Autowired
	private WeatherDataService weatherDataService;
	
	@Autowired
	private DataClientZuul dataClientZuul;
	
	@GetMapping("/cityId/{cityId}")
	public ModelAndView getReportByCityId(@PathVariable("cityId") String cityId, Model model) throws Exception {
		// 获取城市ID列表
		List<City> cityList = null;
		Weather weather = null;
		
		try {
			
			ServerResponse<Weather> weatherRes = weatherDataService.getWeatherDataByCityId(cityId);
			if(weatherRes.isSuccess()) {
				weather = weatherRes.getData();
			}
			
			// 由城市数据API微服务提供数据
			ServerResponse<List<City>> response = dataClientZuul.getCityList();
			if(response.isSuccess()) {
				cityList = response.getData();
			} 

			
		} catch (Exception e) {
			logger.error("Exception!", e);
		}
		
		model.addAttribute("title", "小薛的天气预报");
		model.addAttribute("cityId", cityId);
		model.addAttribute("cityList", cityList);
		model.addAttribute("report", weather);
		return new ModelAndView("weather/report", "reportModel", model);
	}
	

}
