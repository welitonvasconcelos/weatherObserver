package com.weatherObserver.model.dto;

import java.util.List;

import com.weatherObserver.entity.WeatherCondition;

import lombok.Data;

@Data
public class CityDTO {

	public String name;
	public List<WeatherCondition> weatherConditions;

}
