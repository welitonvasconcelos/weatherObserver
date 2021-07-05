package com.weatherObserver.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityInformationDTO {
	
	@JsonProperty("Key")
	private String Key;//43346
	@JsonProperty("LocalizedName")
	private String LocalizedName;
	@JsonProperty("EnglishName")
	private String EnglishName;
}
