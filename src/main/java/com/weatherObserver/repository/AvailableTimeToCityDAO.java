package com.weatherObserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weatherObserver.entity.AvailableTimeToCity;

@Repository
public interface AvailableTimeToCityDAO extends JpaRepository<AvailableTimeToCity, Long>{

}
