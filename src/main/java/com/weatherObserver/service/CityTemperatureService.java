package com.weatherObserver.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weatherObserver.entity.AvailableTimeToCity;
import com.weatherObserver.entity.CityWeather;
import com.weatherObserver.entity.User;
import com.weatherObserver.entity.WeatherCondition;
import com.weatherObserver.model.dto.CityDTO;
import com.weatherObserver.repository.AvailableTimeToCityDAO;
import com.weatherObserver.repository.CityWeatherDAO;
import com.weatherObserver.repository.UserDAO;
import com.weatherObserver.repository.WeatherConditionDAO;

@Service
public class CityTemperatureService {

	private UserDAO userDAO;
	private CityWeatherDAO cityWeatherDAO;
	private AccuweatherService accuweatherService;
	private AvailableTimeToCityDAO availableTimeToCityDAO;
	private WeatherConditionDAO weatherConditionDAO;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	public CityTemperatureService(UserDAO userDAO, CityWeatherDAO cityWeatherDAO,
			AccuweatherService accuweatherService, AvailableTimeToCityDAO availableTimeToCityDAO, WeatherConditionDAO weatherConditionDAO) {
		this.userDAO = userDAO;
		this.cityWeatherDAO = cityWeatherDAO;
		this.accuweatherService = accuweatherService;
		this.availableTimeToCityDAO = availableTimeToCityDAO;
		this.weatherConditionDAO = weatherConditionDAO;
	}

	public List<CityDTO> listCities(String email) {
		User user = userDAO.findByEmail(email);
		if (user == null)
			throw new NoSuchElementException("User not found");
		List<AvailableTimeToCity> citiesAndPeriodToObserve = verifyCitiesInSlectedPeriod(user);
		List<CityWeather> cityWeather = searchAvailableCityWeather(citiesAndPeriodToObserve);
		List<CityDTO> cityWeatherDTO = cityWeather.stream().map(city -> modelMapper.map(city, CityDTO.class))
				.collect(Collectors.toList());
		return cityWeatherDTO;
	}

	private List<CityWeather>  searchAvailableCityWeather(List<AvailableTimeToCity> citiesToObserve) {
		List<CityWeather> cityWeather = new ArrayList<>();
		for (AvailableTimeToCity periodToCity : citiesToObserve) {
			CityWeather city = cityWeatherDAO.findByName(periodToCity.getCityName());
			List<WeatherCondition> weatherConditions = removeWeatherConditionsInDiffertenPeriod(city.getWeatherConditions(), periodToCity);
			city.setWeatherConditions(weatherConditions);
			cityWeather.add(city);
		}
		return cityWeather;
	}

	private List<WeatherCondition> removeWeatherConditionsInDiffertenPeriod(List<WeatherCondition> weatherConditions, AvailableTimeToCity periodToCity) {
		LocalDateTime end = periodToCity .getEnd();
		LocalDateTime start = periodToCity .getStart();
		List<WeatherCondition> weatherConditionsToReturn = new ArrayList<>();
		for (WeatherCondition weatherCondition : weatherConditions) {
			LocalDateTime localObservationDateTime = weatherCondition.getLocalObservationDateTime();
			if(localObservationDateTime.isBefore(end) && localObservationDateTime.isAfter(start)) {
				weatherConditionsToReturn.add(weatherCondition);
			}
		}
		return weatherConditionsToReturn;
	}

	private List<AvailableTimeToCity> verifyCitiesInSlectedPeriod(User user) {
		List<AvailableTimeToCity> citiesToObserve = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
		List<AvailableTimeToCity> currentTimeToCities = user.getAvailableTimeToCities();
		for (AvailableTimeToCity availableTimeToCity : currentTimeToCities) {
			LocalDateTime end = availableTimeToCity .getEnd();
			LocalDateTime start = availableTimeToCity .getStart();
			if(now.isBefore(end) && now.isAfter(start))
				citiesToObserve.add(availableTimeToCity);
		}
		return citiesToObserve;
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
		weatherConditionDAO.save(weatherContidion);
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
			user = User.builder().email(email).availableTimeToCities(new ArrayList<>()).build();
		}
		user.addAvailableTimeToCity(availableTimeToCity);
		availableTimeToCityDAO.save(availableTimeToCity);
		return user;
	}

}
