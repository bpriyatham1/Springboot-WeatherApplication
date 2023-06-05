package com.dcs.WeatherApplication.controller;

import com.dcs.WeatherApplication.services.WeatherService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("weather")
public class WeatherApiController {

    @Autowired
    private WeatherService weatherService;

    @ApiOperation("Return a JSON object with the weather input of city provided")
    @GetMapping(value = "/fetchTemperature", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> weatherForecast(@ApiParam("Provide City name in String Format") @RequestParam(required = true) String city,
                                             @ApiParam("provide Input Date in Epoch Format") @RequestParam(required = true) long inputDate) {
        return weatherService.weatherForecast(city, inputDate);
    }

}
