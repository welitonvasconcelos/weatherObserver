package com.weatherObserver.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherObserver.entity.WeatherCondition;
import com.weatherObserver.model.dto.WeatherConditionDTO;

@Service
public class AccuweatherService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private ModelMapper modelMapper;

	@Value("${weatherObserver.APIKEY}")
	private String apiKey;

	public WeatherCondition fetchWeatherConditions(String cityKey) {
		String url = "http://dataservice.accuweather.com/currentconditions/v1/" + cityKey + "?apikey=" + apiKey;
		restTemplate = new RestTemplate();
		WeatherConditionDTO[] weatherConditionDTO = restTemplate.getForObject(url, WeatherConditionDTO[].class);
		WeatherCondition weatherCondition = modelMapper.map(weatherConditionDTO[0], WeatherCondition.class);
		return weatherCondition;
	}

	public String fetchCityKey(String cityName){
		String url = "http://dataservice.accuweather.com/" + "locations/v1/cities/search?" + "apikey=" + apiKey + "&q="
				+ cityName;
		restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		JsonNode root = null;
		try {
			root = mapper.readTree(response.getBody());
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return root.get(0).path("Key").asText();
	}
}
