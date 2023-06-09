package com.dcs.weather;

import com.dcs.weather.model.Weather;
import com.dcs.weather.repository.WeatherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class WeatherApiRestControllerTest {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final ObjectMapper om = new ObjectMapper();
    @Autowired
    WeatherRepository weatherRepository;
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        weatherRepository.deleteAll();
        om.setDateFormat(simpleDateFormat);
    }

    @org.junit.Test
    @WithMockUser(username = "admin", password = "admin123", roles = "ADMIN")
    public void testWeatherEndpointWithPOST() throws Exception {
        Weather expectedRecord = getTestData().get("chicago");
        Weather actualRecord = om.readValue(mockMvc.perform(post("/weather")
                .contentType("application/json").content(om.writeValueAsString(getTestData().get("chicago")))).andDo(print()).andExpect(jsonPath("$.id", greaterThan(0))).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), Weather.class);

        Assert.assertTrue(new ReflectionEquals(expectedRecord, "id").matches(actualRecord));
        assertEquals(true, weatherRepository.findById(actualRecord.getId()).isPresent());
    }

    private Map<String, Weather> getTestData() throws ParseException {
        Map<String, Weather> data = new LinkedHashMap<>();

        Weather chicago = new Weather(simpleDateFormat.parse("2019-06-11"), 41.8818f, -87.6231f, "Chicago", "Illinois", Arrays.asList(24.0, 21.5, 24.0, 19.5, 25.5, 25.5, 24.0, 25.0, 23.0, 22.0, 18.0, 18.0, 23.5, 23.0, 23.0, 25.5, 21.0, 20.5, 20.0, 18.5, 20.5, 21.0, 25.0, 20.5));
        data.put("chicago", chicago);

        Weather oakland = new Weather(simpleDateFormat.parse("2019-06-12"), 37.8043f, -122.2711f, "Oakland", "California", Arrays.asList(24.0, 36.0, 28.5, 29.0, 32.0, 36.0, 28.5, 34.5, 30.5, 31.5, 29.5, 27.0, 30.5, 23.5, 29.0, 22.0, 28.5, 32.5, 24.5, 28.5, 22.5, 35.0, 26.5, 32.5));
        data.put("oakland", oakland);

        Weather london = new Weather(simpleDateFormat.parse("2019-03-12"), 51.5098f, -0.1180f, "London", "N/A", Arrays.asList(11.0, 11.0, 5.5, 7.0, 5.0, 5.5, 6.0, 9.5, 11.5, 5.0, 6.0, 8.0, 9.5, 5.0, 9.0, 9.5, 12.0, 6.0, 9.5, 8.5, 8.0, 8.0, 9.0, 6.5));
        data.put("london", london);

        Weather moscow1 = new Weather(simpleDateFormat.parse("2019-03-12"), 55.7512f, 37.6184f, "Moscow", "N/A", Arrays.asList(-2.0, -4.5, 1.0, -6.0, 1.0, 1.5, -9.0, -2.5, -3.0, -0.5, -13.5, -9.0, -11.5, -5.5, -5.5, -3.5, -14.0, -9.5, 1.5, -15.0, -6.5, -7.0, -13.5, -14.5));
        data.put("moscow1", moscow1);

        Weather moscow2 = new Weather(simpleDateFormat.parse("2019-03-12"), 55.7512f, 37.6184f, "Moscow", "N/A", Arrays.asList(-2.0, -4.5, 1.0, -6.0, 1.0, 1.5, -9.0, -2.5, -3.0, -0.5, -13.5, -9.0, -11.5, -5.5, -5.5, -3.5, -14.0, -9.5, 1.5, -15.0, -6.5, -7.0, -13.5, -14.5));
        data.put("moscow2", moscow2);

        return data;
    }
}
