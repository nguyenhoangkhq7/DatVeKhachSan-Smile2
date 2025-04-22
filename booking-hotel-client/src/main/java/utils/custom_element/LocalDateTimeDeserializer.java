package utils.custom_element;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject dateObj = jsonObject.getAsJsonObject("date");
        JsonObject timeObj = jsonObject.getAsJsonObject("time");

        int year = dateObj.get("year").getAsInt();
        int month = dateObj.get("month").getAsInt();
        int day = dateObj.get("day").getAsInt();
        int hour = timeObj.get("hour").getAsInt();
        int minute = timeObj.get("minute").getAsInt();
        int second = timeObj.get("second").getAsInt();

        // Tạo LocalDateTime từ các thành phần year, month, day, hour, minute, second
        return LocalDateTime.of(year, month, day, hour, minute, second);
    }
}

