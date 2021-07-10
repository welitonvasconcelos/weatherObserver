package com.weatherObserver.service;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.weatherObserver.entity.AvailableTimeToCity;
import com.weatherObserver.entity.User;
import com.weatherObserver.entity.WeatherCondition;
import com.weatherObserver.entity.CityWeather;
import com.weatherObserver.entity.Metric;
import com.weatherObserver.entity.Temperature;
import com.weatherObserver.repository.CityWeatherDAO;
import com.weatherObserver.repository.UserDAO;


@ExtendWith(MockitoExtension.class)
public class CityTemperatureServiceTest {

	private CityTemperatureService cityTemperatureService;

	@Mock
	private UserDAO userDAO;

	@Mock
	private CityWeatherDAO cityWeatherDAO;

	@Mock
	private AccuweatherService accuweatherService;

	@Captor
	private ArgumentCaptor<CityWeather> captor;

	@BeforeEach
	public void beforeEach() {
		this.cityTemperatureService = new CityTemperatureService(userDAO, cityWeatherDAO, accuweatherService);
	}

	@Test
	public void verifyAddInRepository() {
		String email = "user@user.com";
		String cityKey = "1234";
		String cityName = "City";
		WeatherCondition weatherCondition = createWeatherCondition(cityKey);
		Mockito.when(userDAO.findByEmail(email)).thenReturn(null);
		Mockito.when(cityWeatherDAO.findByName(cityName)).thenReturn(null);
		Mockito.when(accuweatherService.fetchCityKey(cityName)).thenReturn(cityKey);
		Mockito.when(accuweatherService.fetchWeatherConditions(cityKey)).thenReturn(weatherCondition);
		AvailableTimeToCity availableTimeToCity = AvailableTimeToCity.builder().cityName(cityName)
				.start(LocalDateTime.now()).end(LocalDateTime.now().plusDays(7)).build();
		User user = cityTemperatureService.addCity(email, availableTimeToCity);
		Mockito.verify(userDAO).save(user);
		Mockito.verify(cityWeatherDAO).save(captor.capture());
		CityWeather cityCapture = captor.getValue();
		assertEquals(user.getEmail(), email);
		assertEquals(cityCapture.getKey(), cityKey);
		assertEquals(cityCapture.getName(), cityName);
		assertEquals(cityCapture.getWeatherConditions().get(0), weatherCondition);
		assertFalse(cityCapture.getWeatherConditions().isEmpty());
	}

	@Test
	public void wheaterConditionsIsNotFound() {
		String email = "user@user.com";
		String cityKey = "1234";
		String cityName = "City";
		Mockito.when(userDAO.findByEmail(email)).thenReturn(null);
		Mockito.when(cityWeatherDAO.findByName(cityName)).thenReturn(null);
		Mockito.when(accuweatherService.fetchCityKey(cityName)).thenReturn(cityKey);
		Mockito.when(accuweatherService.fetchWeatherConditions(cityKey)).thenReturn(null);
		AvailableTimeToCity availableTimeToCity = AvailableTimeToCity.builder().cityName(cityName)
				.start(LocalDateTime.now()).end(LocalDateTime.now().plusDays(7)).build();
		assertThrows(NoSuchElementException.class, () -> cityTemperatureService.addCity(email, availableTimeToCity));
	}

	@Test
	public void keyCityIsNotFound() {
		String email = "user@user.com";
		String cityName = "City";
		Mockito.when(userDAO.findByEmail(email)).thenReturn(null);
		Mockito.when(cityWeatherDAO.findByName(cityName)).thenReturn(null);
		Mockito.when(accuweatherService.fetchCityKey(cityName)).thenReturn(null);
		AvailableTimeToCity availableTimeToCity = AvailableTimeToCity.builder().cityName(cityName)
				.start(LocalDateTime.now()).end(LocalDateTime.now().plusDays(7)).build();
		assertThrows(NoSuchElementException.class, () -> cityTemperatureService.addCity(email, availableTimeToCity));
	}

	private WeatherCondition createWeatherCondition(String cityKey) {
		Metric metric = Metric.builder().value(28.0).unit("C").build();
		Temperature temperature = Temperature.builder().metric(metric).build();

		return WeatherCondition.builder().cityKey(cityKey).temperature(temperature)
				.localObservationDateTime(LocalDateTime.now()).build();
	}

}
