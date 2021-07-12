package com.weatherObserver.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.weatherObserver.entity.CityWeather;
import com.weatherObserver.entity.Temperature;
import com.weatherObserver.entity.WeatherCondition;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CityWeatherDAOTest {

	@Autowired
	private CityWeatherDAO cityWeatherDAO;

	@Autowired
	private TestEntityManager entityManager;

	private String cityName;
	private String keyCity;
	private LocalDateTime now;
	private WeatherCondition weatherCondition;

	@BeforeEach
	public void beforeEach() {
		cityName = "City";
		keyCity = "1234";
		now = LocalDateTime.now();
		Temperature temperature = Temperature.builder().metricValue(28.0).metricUnit("C").build();
		weatherCondition = WeatherCondition.builder().temperature(temperature).localObservationDateTime(now).build();
		List<WeatherCondition> weatherConditions = new ArrayList<WeatherCondition>();
		weatherConditions.add(weatherCondition);
		CityWeather city = CityWeather.builder().key(keyCity).name(cityName).weatherConditions(weatherConditions)
				.build();
		entityManager.persist(city);
	}

	@Test
	public void shouldFindCityWeather() {
		CityWeather city = cityWeatherDAO.findByName(cityName);
		assertNotNull(city);
		assertEquals(cityName, city.getName());
		assertEquals(keyCity, city.getKey());
		assertEquals(weatherCondition.getLocalObservationDateTime(),
				city.getWeatherConditions().get(0).getLocalObservationDateTime());
	}

	@Test
	public void shouldNotFindCityWeather() {
		CityWeather city = cityWeatherDAO.findByName("testCity");
		assertNull(city);
	}

}
