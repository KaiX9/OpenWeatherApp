package sg.edu.nus.iss.Open_Weather.model;

import java.io.Serializable;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Condition implements Serializable {
    
    private String description;
    private String icon;

    public Condition() {

    }

    public Condition(String description, String icon) {
        this.description = description;
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Condition [description=" + description + ", icon=" + icon + "]";
    }

    public static Condition createFromJson(JsonObject jsonObj) {
        Condition c = new Condition();
        c.setDescription("%s - %s".formatted(jsonObj.getString("main"), jsonObj.getString("description")));
        c.setIcon("https://openweathermap.org/img/wn/%s@2x.png".formatted(jsonObj.getString("icon")));
        return c;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("description", getDescription())
                .add("icon", getIcon())
                .build();
    }
    
}
