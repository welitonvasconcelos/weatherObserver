package com.weatherObserver.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
@Entity
@Data
@Table(name = "cityWeather")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityWeather implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String key;
	private String name;
	
	@OneToMany
	private List<WeatherCondition> weatherConditions;
	
	
	public void addWeatherCondition(WeatherCondition weatherCondition) {
		weatherConditions.add(weatherCondition);
	}
}
