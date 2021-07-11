package com.weatherObserver.model.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class InputInserCityDTO {

	public String email;
	public String cityName;
	public LocalDateTime start;
	public LocalDateTime end;
}
