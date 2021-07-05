package com.weatherObserver.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemperatureDTO {
	
	@JsonProperty("Metric")
	private MetricDTO metric;
}
