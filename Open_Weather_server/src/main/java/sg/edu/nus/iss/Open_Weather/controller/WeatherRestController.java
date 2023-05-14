package sg.edu.nus.iss.Open_Weather.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.Open_Weather.model.Weather;
import sg.edu.nus.iss.Open_Weather.service.WeatherService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class WeatherRestController {

    @Autowired
    private WeatherService wSvc;

    @GetMapping(path="/weather")
    public ResponseEntity<String> getWeather(@RequestParam String city) {

        Optional<Weather> weather = this.wSvc.getWeather(city);
        if (weather.get().getCod() == null) {
            return ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(weather.get().toJSON().toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(weather.get().toErrorJSON().toString());
        }
    }
    
}
