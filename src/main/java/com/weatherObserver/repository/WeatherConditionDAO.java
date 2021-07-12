package com.weatherObserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weatherObserver.entity.WeatherCondition;

public interface WeatherConditionDAO extends JpaRepository<WeatherCondition, Long>{

}
