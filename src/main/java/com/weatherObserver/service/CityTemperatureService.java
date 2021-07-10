package com.weatherObserver.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.weatherObserver.entity.AvailableTimeToCity;
import com.weatherObserver.entity.CityWeather;
import com.weatherObserver.entity.User;
import com.weatherObserver.entity.WeatherCondition;
import com.weatherObserver.model.dto.CityDTO;
import com.weatherObserver.repository.CityWeatherDAO;
import com.weatherObserver.repository.UserDAO;

@Service
public class CityTemperatureService {

	private UserDAO userDAO;
	private CityWeatherDAO cityWeatherDAO;
	private AccuweatherService accuweatherService;

	@Autowired
	public CityTemperatureService(UserDAO userDAO, CityWeatherDAO cityWeatherDAO,
			AccuweatherService accuweatherService) {
		this.userDAO = userDAO;
		this.cityWeatherDAO = cityWeatherDAO;
		this.accuweatherService = accuweatherService;
	}

	public List<CityDTO> listCities(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	public User addCity(String email, AvailableTimeToCity availableTimeToCity) {
		User user = searchUser(email, availableTimeToCity);
		CityWeather city = searchCity(availableTimeToCity);
		WeatherCondition weatherContidion = searchWeatherCondition(city);
		city.addWeatherCondition(weatherContidion);
		userDAO.save(user);
		cityWeatherDAO.save(city);
		return user;
	}

	private WeatherCondition searchWeatherCondition(CityWeather city) {
		WeatherCondition weatherContidion = accuweatherService.fetchWeatherConditions(city.getKey());
		if (weatherContidion == null)
			throw new NoSuchElementException("Weather Condition not found");
		return weatherContidion;
	}

	private CityWeather searchCity(AvailableTimeToCity availableTimeToCity) {
		String cityName = availableTimeToCity.getCityName();
		CityWeather city = cityWeatherDAO.findByName(cityName);
		if (city == null) {
			String keyCity = accuweatherService.fetchCityKey(availableTimeToCity.getCityName());
			if (keyCity == null)
				throw new NoSuchElementException("City not found");
			city = CityWeather.builder().key(keyCity).name(cityName).weatherConditions(new ArrayList<WeatherCondition>()).build();
		}
		return city;
	}

	private User searchUser(String email, AvailableTimeToCity availableTimeToCity) {
		User user = userDAO.findByEmail(email);
		if (user == null) {
			user = User.builder().email(email).availableTimeToCity(availableTimeToCity).build();
		}
		return user;
	}

}
