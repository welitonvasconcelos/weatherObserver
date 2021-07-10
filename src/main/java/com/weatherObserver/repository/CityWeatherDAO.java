package com.weatherObserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weatherObserver.entity.CityWeather;

@Repository
public interface CityWeatherDAO extends JpaRepository<CityWeather, Long>{
	public CityWeather findByName(String name);
}
