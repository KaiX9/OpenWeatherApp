package sg.edu.nus.iss.Open_Weather.model;

import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Weather implements Serializable {
    
    private String temperature;
    private String feels_like;
    private String minTemp;
    private String maxTemp;
    private String humidity;
    private String speed;
    private long weatherTimeStamp;
    private long sunriseTimeStamp;
    private long sunsetTimeStamp;
    private String cityName;
    private long timezone;
    private List<Condition> conditions = new ArrayList<Condition>();

    private String cod;
    private String message;
    
    public Weather() {

    }

    public Weather(String temperature, String feels_like, String minTemp, String maxTemp, String humidity, String speed,
            long weatherTimeStamp, long sunriseTimeStamp, long sunsetTimeStamp, String cityName, long timezone,
            List<Condition> conditions, String cod, String message) {
        this.temperature = temperature;
        this.feels_like = feels_like;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.humidity = humidity;
        this.speed = speed;
        this.weatherTimeStamp = weatherTimeStamp;
        this.sunriseTimeStamp = sunriseTimeStamp;
        this.sunsetTimeStamp = sunsetTimeStamp;
        this.cityName = cityName;
        this.timezone = timezone;
        this.conditions = conditions;
        this.cod = cod;
        this.message = message;
    }

    public String getCod() {
        return cod;
    }
    public void setCod(String cod) {
        this.cod = cod;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getTemperature() {
        return temperature;
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    public String getFeels_like() {
        return feels_like;
    }
    public void setFeels_like(String feels_like) {
        this.feels_like = feels_like;
    }
    public String getMinTemp() {
        return minTemp;
    }
    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }
    public String getMaxTemp() {
        return maxTemp;
    }
    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }
    public String getHumidity() {
        return humidity;
    }
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
    public String getSpeed() {
        return speed;
    }
    public void setSpeed(String speed) {
        this.speed = speed;
    }
    public long getWeatherTimeStamp() {
        return weatherTimeStamp;
    }
    public void setWeatherTimeStamp(long weatherTimeStamp) {
        this.weatherTimeStamp = weatherTimeStamp;
    }
    public long getSunriseTimeStamp() {
        return sunriseTimeStamp;
    }
    public void setSunriseTimeStamp(long sunriseTimeStamp) {
        this.sunriseTimeStamp = sunriseTimeStamp;
    }
    public long getSunsetTimeStamp() {
        return sunsetTimeStamp;
    }
    public void setSunsetTimeStamp(long sunsetTimeStamp) {
        this.sunsetTimeStamp = sunsetTimeStamp;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public long getTimezone() {
        return timezone;
    }
    public void setTimezone(long timezone) {
        this.timezone = timezone;
    }
    public List<Condition> getConditions() {
        return conditions;
    }
    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return "Weather [temperature=" + temperature + ", feels_like=" + feels_like + ", minTemp=" + minTemp
                + ", maxTemp=" + maxTemp + ", humidity=" + humidity + ", speed=" + speed + ", weatherTimeStamp="
                + weatherTimeStamp + ", sunriseTimeStamp=" + sunriseTimeStamp + ", sunsetTimeStamp=" + sunsetTimeStamp
                + ", cityName=" + cityName + ", timezone=" + timezone + ", conditions=" + conditions + ", cod=" + cod
                + ", message=" + message + "]";
    }

    public static Weather createFromJson(String json) {
        Weather w = new Weather();
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject jsonObj = reader.readObject();
        JsonObject mainObj = jsonObj.getJsonObject("main");
        w.setTemperature(mainObj.getJsonNumber("temp").toString());
        w.setFeels_like(mainObj.getJsonNumber("feels_like").toString());
        w.setMinTemp(mainObj.getJsonNumber("temp_min").toString());
        w.setMaxTemp(mainObj.getJsonNumber("temp_max").toString());
        w.setHumidity(mainObj.getJsonNumber("humidity").toString());
        JsonObject windObj = jsonObj.getJsonObject("wind");
        w.setSpeed(windObj.getJsonNumber("speed").toString());
        w.setWeatherTimeStamp(jsonObj.getJsonNumber("dt").longValue());
        JsonObject sysObj = jsonObj.getJsonObject("sys");
        w.setSunriseTimeStamp(sysObj.getJsonNumber("sunrise").longValue());
        w.setSunsetTimeStamp(sysObj.getJsonNumber("sunset").longValue());
        w.setCityName(jsonObj.getString("name"));
        w.setTimezone(jsonObj.getInt("timezone"));
        JsonArray weatherArray = jsonObj.getJsonArray("weather");
        w.setConditions(weatherArray.stream()
            .map(c -> (JsonObject)c)
            .map(c -> Condition.createFromJson(c))
            .toList());
        
        return w;
    }

    public static Weather createFromJsonInRedis(String json) {
        Weather w = new Weather();
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject jObj = reader.readObject();
        w.setCityName(jObj.getString("city"));
        w.setTemperature(jObj.getString("temperature"));
        w.setFeels_like(jObj.getString("feels_like"));
        w.setMinTemp(jObj.getString("minimum_temperature"));
        w.setMaxTemp(jObj.getString("maximum_temperature"));
        w.setHumidity(jObj.getString("humidity"));
        w.setSpeed(jObj.getString("wind_speed"));
        w.setWeatherTimeStamp(jObj.getInt("weather_timestamp"));
        w.setSunriseTimeStamp(jObj.getInt("sunrise"));
        w.setSunsetTimeStamp(jObj.getInt("sunset"));
        w.setTimezone(jObj.getInt("timezone"));
        JsonArray weatherArray = jObj.getJsonArray("weather");
        w.setConditions(weatherArray.stream()
            .map(c -> (JsonObject)c)
            .map(c -> Condition.createFromJsonInRedis(c))
            .toList());

        return w;
    }

    public static Weather createErrorFromJson(String json) {
        Weather w = new Weather();
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject jsonObj = reader.readObject();
        w.setCod(jsonObj.getString("cod"));
        w.setMessage(jsonObj.getString("message"));

        return w;
    }

    public JsonObject toErrorJSON() {
        return Json.createObjectBuilder()
                .add("status_code", getCod())
                .add("message", getMessage())
                .build();
    }

    public JsonObject toJSON() {
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (Condition c : conditions) {
            arrBuilder.add(c.toJSON());
        }

        return Json.createObjectBuilder()
            .add("city", getCityName())
            .add("temperature", getTemperature())
            .add("feels_like", getFeels_like())
            .add("minimum_temperature", getMinTemp())
            .add("maximum_temperature", getMaxTemp())
            .add("humidity", getHumidity())
            .add("wind_speed", getSpeed())
            .add("weather_timestamp", convertDate(getWeatherTimeStamp()))
            .add("sunrise", convertDate(getSunriseTimeStamp()))
            .add("sunset", convertDate(getSunsetTimeStamp()))
            .add("timezone", getTimezone())
            .add("weather", arrBuilder)
            .build();
    }

    public JsonObject toJSONIntoRedis() {
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (Condition c : conditions) {
            arrBuilder.add(c.toJSON());
        }

        return Json.createObjectBuilder()
            .add("city", getCityName())
            .add("temperature", getTemperature())
            .add("feels_like", getFeels_like())
            .add("minimum_temperature", getMinTemp())
            .add("maximum_temperature", getMaxTemp())
            .add("humidity", getHumidity())
            .add("wind_speed", getSpeed())
            .add("weather_timestamp", getWeatherTimeStamp())
            .add("sunrise", getSunriseTimeStamp())
            .add("sunset", getSunsetTimeStamp())
            .add("timezone", getTimezone())
            .add("weather", arrBuilder)
            .build();
    }

    public String convertDate(long timestamp) {

        int offsetInSeconds = 0;
        int offsetInMinutes = 0;
        int offsetInHours = 0;
        String timez = "";

        if (getTimezone() >= 0) {
            offsetInSeconds = (int) getTimezone();
            offsetInHours = offsetInSeconds / 3600;
            offsetInMinutes = (offsetInSeconds % 3600) / 60;
            timez = String.format("GMT+%02d:%02d", offsetInHours, offsetInMinutes);
        } else if (getTimezone() < 0) {
            offsetInSeconds = (int) getTimezone();
            offsetInHours = Math.abs(offsetInSeconds) / 3600;
            offsetInMinutes = (Math.abs(offsetInSeconds) % 3600) / 60;
            timez = String.format("GMT-%02d:%02d", offsetInHours, offsetInMinutes);
        }
        Date date = new Date(timestamp * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        dateFormat.setTimeZone(TimeZone.getTimeZone(timez));
        return dateFormat.format(date);
        
    }

}