package com.weatherObserver.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricDTO {
	@JsonProperty("Value")
	private Double value;
	@JsonProperty("Unit")
	private String unit;
	@JsonProperty("UnitType")
	private Integer unitType;
}
