package com.weatherObserver.service;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.weatherObserver.api.WeatherObserverApplication;
import com.weatherObserver.entity.AvailableTimeToCity;
import com.weatherObserver.entity.CityWeather;
import com.weatherObserver.entity.Temperature;
import com.weatherObserver.entity.User;
import com.weatherObserver.entity.WeatherCondition;
import com.weatherObserver.model.dto.CityDTO;
import com.weatherObserver.repository.AvailableTimeToCityDAO;
import com.weatherObserver.repository.CityWeatherDAO;
import com.weatherObserver.repository.UserDAO;
import com.weatherObserver.repository.WeatherConditionDAO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WeatherObserverApplication.class)
@ExtendWith(MockitoExtension.class)
public class CityTemperatureServiceTest {

	private CityTemperatureService cityTemperatureService;

	@Mock
	private UserDAO userDAO;

	@Mock
	private CityWeatherDAO cityWeatherDAO;

	@Mock
	private AccuweatherService accuweatherService;

	@Mock
	private AvailableTimeToCityDAO availableTimeToCityDAO;

	@Mock
	private WeatherConditionDAO weatherConditionDAO;

	@Captor
	private ArgumentCaptor<CityWeather> captor;

	private String email;

	private String cityKey;

	private String cityName;

	private WeatherCondition weatherCondition;

	@BeforeEach
	public void beforeEach() {
		this.cityTemperatureService = new CityTemperatureService(userDAO, cityWeatherDAO, accuweatherService,
				availableTimeToCityDAO, weatherConditionDAO);
		email = "user@user.com";
		cityKey = "1234";
		cityName = "City";
	}
	@Test
	public void userNotFoundToListCities() {
		Mockito.when(userDAO.findByEmail(email)).thenReturn(null);
		assertThrows(NoSuchElementException.class, () -> cityTemperatureService.listCities(email));
	}
//	@Test
//	public void verifyNonSelectionWeatherConditions() {
//		LocalDateTime now = LocalDateTime.now().minusDays(7);
//		AvailableTimeToCity availableTimeToCity = AvailableTimeToCity.builder().id(1l).cityName(cityName)
//				.start(LocalDateTime.now().minusHours(1l)).end(LocalDateTime.now().plusDays(7)).build();
//		List<AvailableTimeToCity> availableTimeToCities = new ArrayList<AvailableTimeToCity>();
//		availableTimeToCities.add(availableTimeToCity);
//		User user1 = Mockito.mock(User.class);
//		Mockito.when(user1.getAvailableTimeToCities()).thenReturn(availableTimeToCities);
//		WeatherCondition weatherCondition = createWeatherCondition(cityKey, 28.0, "C", now);
//		List<WeatherCondition> weatherConditions = new ArrayList<WeatherCondition>();
//		weatherConditions.add(weatherCondition);
//		Mockito.when(userDAO.findByEmail(email)).thenReturn(user1);
//		CityWeather cityWeather = Mockito.mock(CityWeather.class);
//		Mockito.when(cityWeather.getWeatherConditions()).thenReturn(weatherConditions);
//		Mockito.when(cityWeatherDAO.findByName(cityName)).thenReturn(cityWeather);
//		List<CityDTO> listCities = cityTemperatureService.listCities(email);
//		assertTrue(listCities.isEmpty());
//	}
	
	@Test
	public void verifySelectionWeatherConditions() {
		LocalDateTime now = LocalDateTime.now().plusHours(1l);
		List<AvailableTimeToCity> availableTimeToCities = new ArrayList<AvailableTimeToCity>();
		availableTimeToCities.add(AvailableTimeToCity.builder().id(1l).cityName(cityName)
				.start(LocalDateTime.now()).end(LocalDateTime.now().plusDays(7)).build());
		User user = Mockito.mock(User.class);
		Mockito.when(user.getAvailableTimeToCities()).thenReturn(availableTimeToCities);
		WeatherCondition weatherCondition = createWeatherCondition(cityKey, 28.0, "C", now);
		List<WeatherCondition> weatherConditions = new ArrayList<WeatherCondition>();
		weatherConditions.add(weatherCondition);
		Mockito.when(userDAO.findByEmail(email)).thenReturn(user);
		CityWeather cityWeather = Mockito.mock(CityWeather.class);
		Mockito.when(cityWeather.getWeatherConditions()).thenReturn(weatherConditions);
		Mockito.when(cityWeatherDAO.findByName(cityName)).thenReturn(cityWeather);
		List<CityDTO> listCities = cityTemperatureService.listCities(email);
		assertEquals(weatherCondition, listCities.get(0).getWeatherConditions().get(0));
	}

	@Test
	public void verifyAddInRepository() {
		weatherCondition = createWeatherCondition(cityKey, 28.0, "C", LocalDateTime.now());
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
		LocalDateTime now = LocalDateTime.now();
		Mockito.when(userDAO.findByEmail(email)).thenReturn(null);
		Mockito.when(cityWeatherDAO.findByName(cityName)).thenReturn(null);
		Mockito.when(accuweatherService.fetchCityKey(cityName)).thenReturn(null);
		AvailableTimeToCity availableTimeToCity = AvailableTimeToCity.builder().cityName(cityName).start(now)
				.end(now.plusDays(7)).build();
		assertThrows(NoSuchElementException.class, () -> cityTemperatureService.addCity(email, availableTimeToCity));
	}

	private WeatherCondition createWeatherCondition(String cityKey,Double metricValue, String metricUnit, LocalDateTime localDateTime) {
		Temperature temperature = Temperature.builder().id(1l).metricValue(metricValue).metricUnit(metricUnit).build();
		return WeatherCondition.builder().id(1l).temperature(temperature)
				.localObservationDateTime(localDateTime).build();
	}

}
