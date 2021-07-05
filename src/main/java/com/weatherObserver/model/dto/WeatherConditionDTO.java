package com.weatherObserver.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherConditionDTO {
	
	@JsonProperty("LocalObservationDateTime")
	private LocalDateTime localObservationDateTime;
	@JsonProperty("IsDayTime")
	private Boolean isDayTime;
	@JsonProperty("Temperature")
	private TemperatureDTO temperature;
}
