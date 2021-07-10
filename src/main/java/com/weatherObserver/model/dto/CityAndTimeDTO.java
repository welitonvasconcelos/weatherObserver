package com.weatherObserver.model.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CityAndTimeDTO {

	private String city;
	private LocalDateTime start;
    private LocalDateTime end;
}
