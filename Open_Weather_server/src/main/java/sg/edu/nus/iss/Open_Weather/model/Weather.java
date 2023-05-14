package sg.edu.nus.iss.Open_Weather.model;

import java.io.Serializable;
import java.io.StringReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private List<Condition> conditions = new ArrayList<Condition>();

    private String cod;
    private String message;
    
    public Weather() {

    }

    public Weather(String temperature, String feels_like, String minTemp, String maxTemp, String humidity, String speed,
            long weatherTimeStamp, long sunriseTimeStamp, long sunsetTimeStamp, String cityName,
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
                + ", cityName=" + cityName + ", conditions=" + conditions + ", cod=" + cod + ", message=" + message
                + "]";
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
        JsonArray weatherArray = jsonObj.getJsonArray("weather");
        w.setConditions(weatherArray.stream()
            .map(c -> (JsonObject)c)
            .map(c -> Condition.createFromJson(c))
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
            .add("weather", arrBuilder)
            .build();
    }

    public String convertDate(long timestamp) {
        Date date = new Date(timestamp * 1000);
        DateFormat dateFormat = DateFormat.getDateTimeInstance
            (DateFormat.LONG, DateFormat.LONG);
        return dateFormat.format(date);
    }

}