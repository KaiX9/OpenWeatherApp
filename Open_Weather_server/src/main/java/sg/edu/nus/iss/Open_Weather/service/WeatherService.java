package sg.edu.nus.iss.Open_Weather.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import sg.edu.nus.iss.Open_Weather.model.Weather;
import sg.edu.nus.iss.Open_Weather.repository.WeatherRepository;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepo;

    @Value("${open.weather.key}")
    private String openWeatherKey;

    public Optional<Weather> getWeather(String city) {

        Optional<Weather> weather = this.weatherRepo.get(city);
        if (weather.isEmpty()) {
            String weatherUrl = UriComponentsBuilder
            .fromUriString("https://api.openweathermap.org/data/2.5/weather")
            .queryParam("q", city.replaceAll(" ", "+"))
            .queryParam("appid", openWeatherKey)
            .queryParam("units", "metric")
            .toUriString();

            RestTemplate template = new RestTemplate();
            System.out.println("get from api");
            try {
            ResponseEntity<String> r = template.getForEntity(weatherUrl, String.class);
            Weather w = Weather.createFromJson(r.getBody());
            this.weatherRepo.save(w);
            return Optional.of(w);
            } catch (HttpClientErrorException e) {
            String responseBody = e.getResponseBodyAsString();
            Weather w_error = Weather.createErrorFromJson(responseBody);
            return Optional.of(w_error);
            }
        }
        
        return weather;
    }
}
