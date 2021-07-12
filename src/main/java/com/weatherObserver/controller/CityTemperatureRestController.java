package com.weatherObserver.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.weatherObserver.entity.AvailableTimeToCity;
import com.weatherObserver.model.dto.CityDTO;
import com.weatherObserver.model.dto.InputInserCityDTO;
import com.weatherObserver.service.CityTemperatureService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/cityTemperature")
public class CityTemperatureRestController {
	
	@Autowired
	private CityTemperatureService cityTemperatureService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping("{email}")
	@ResponseBody
	@ApiOperation("Method to list all user cities")
	public ResponseEntity<List<CityDTO>> listCities(@PathVariable("email") String email){
		return ResponseEntity.ok(cityTemperatureService.listCities(email));
	}
	
	@PostMapping
	@ResponseBody
	@ApiOperation("Method to add a city to user observation")
	public ResponseEntity<?> addCitiy(@RequestBody InputInserCityDTO city){
		AvailableTimeToCity availableTimeToCity = modelMapper.map(city, AvailableTimeToCity.class);
		return  ResponseEntity.status(HttpStatus.CREATED).body(cityTemperatureService.addCity(city.getEmail(), availableTimeToCity));
	}
	@PostMapping("{email}")
	@ResponseBody
	@ApiOperation("Method to update all city weathers conditions to user observation")
	public ResponseEntity<?> updateWeathersConditions(@PathVariable("email") String email){
		return ResponseEntity.status(HttpStatus.OK).body(cityTemperatureService.updateCityWeathers(email));
	}
}
