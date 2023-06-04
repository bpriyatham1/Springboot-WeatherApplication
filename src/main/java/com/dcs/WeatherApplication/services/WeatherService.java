package com.dcs.WeatherApplication.services;


import com.alicp.jetcache.anno.Cached;
import com.dcs.WeatherApplication.dto.WeatherMapDTO;
import com.dcs.WeatherApplication.dto.WeatherMapTimeDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class WeatherService {
    private final String URI = "http://api.openweathermap.org/data/2.5/forecast";
    private final String API_ID = "e8280d9934db9c9f0202226a80a3bea3";

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Cached(expire = 10, timeUnit = TimeUnit.MINUTES)
    public ResponseEntity<?> weatherForecastAverage(String city, long inputDate) {
        List<WeatherMapTimeDTO> result = new ArrayList<WeatherMapTimeDTO>();
        try {
            WeatherMapDTO weatherMap = this.restTemplate.getForObject(this.url(city, inputDate), WeatherMapDTO.class);

            LocalDate date =
                    Instant.ofEpochSecond(inputDate).atZone(ZoneId.systemDefault()).toLocalDate();

            List<WeatherMapTimeDTO> collect = weatherMap.getList().stream()
                    .filter(x -> x.getDt().toLocalDate().equals(date)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(collect)) {
                result.add(collect.stream().findFirst().get());
            }
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(new Json(e.getResponseBodyAsString()), e.getStatusCode());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private String url(String city, long inputDate) {
        return String.format(URI.concat("?q=%s").concat("&dt=%s").concat("&appid=%s").concat("&units=metric"), city, inputDate, API_ID);
    }
}
