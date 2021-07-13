
# Weather Observer

Weather Observer is a demo system to register the current temperature observation for a selected city, between a start date and time and end date and time for an user identified by his email.

## Installation

Use the package manager [gradle](https://gradle.org/install/) to install foobar.

```bash
gradle bootRun
```

## Usage
You can see the api documentation in url /v2/api-docs

```java
@GetMapping("{email}")
@ResponseBody
@ApiOperation("Method to list all user cities")
public ResponseEntity<List<CityDTO>> listCities(@PathVariable("email") String email)

@PostMapping
@ResponseBody
@ApiOperation("Method to add a city to user observation")
public ResponseEntity<?> addCitiy(@RequestBody InputInserCityDTO city)

@PostMapping("{email}")
@ResponseBody
@ApiOperation("Method to update all city weathers conditions to user observation")
public ResponseEntity<?> updateWeathersConditions(@PathVariable("email") String email)

```

## Use
* Java 8
* Spring Boot
* H2 database
* lombok
* junit 5.2, Mockito
* Other utility libraries

## Other
* You need to create a api in [AccuWeather](https://developer.accuweather.com/) to configure your api license. After that, change the **APIKEY** in **application.properties**