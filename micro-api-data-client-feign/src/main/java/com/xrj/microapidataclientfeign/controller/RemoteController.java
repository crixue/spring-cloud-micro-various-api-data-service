package com.xrj.microapidataclientfeign.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xrj.microapidataclientfeign.common.ServerResponse;
import com.xrj.microapidataclientfeign.service.CityClient;
import com.xrj.microapidataclientfeign.vo.City;

@RestController
@RequestMapping("/micro-api-data-client-feign")
public class RemoteController {
	private final static Logger logger = LoggerFactory.getLogger(RemoteController.class);

	@Autowired
	private CityClient cityClient;
	
	@GetMapping("/list-cities")
	public ServerResponse<List<City>> getCityList() {
		ServerResponse<List<City>> response = cityClient.getCityList();
		return response;
	}
	
	
}
