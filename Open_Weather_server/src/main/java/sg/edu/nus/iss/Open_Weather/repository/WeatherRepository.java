package sg.edu.nus.iss.Open_Weather.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.Open_Weather.model.Weather;

@Repository
public class WeatherRepository {
    
    @Autowired @Qualifier("weather")
    private RedisTemplate<String, String> template;

    public void save(Weather weather) {
        this.template.opsForValue().set(weather.getCityName().toLowerCase(), 
            weather.toJSONIntoRedis().toString(), 90, TimeUnit.MINUTES);
    }

    public Optional<Weather> get(String cityName) {
        String json = template.opsForValue().get(cityName.toLowerCase());
        System.out.println("json: " + json);
        if ((null == json || json.trim().length() <= 0)) {
            return Optional.empty();
        }
        return Optional.of(Weather.createFromJsonInRedis(json));
    }
}
