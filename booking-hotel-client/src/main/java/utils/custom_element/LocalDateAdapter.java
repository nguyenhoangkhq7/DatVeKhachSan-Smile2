package utils.custom_element;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(date.format(formatter));
    }

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonObject dateObject = jsonObject.getAsJsonObject("date");

            int year = dateObject.get("year").getAsInt();
            int month = dateObject.get("month").getAsInt();
            int day = dateObject.get("day").getAsInt();

            return LocalDate.of(year, month, day);
        } catch (Exception e) {
            // Trường hợp JSON không chứa "date", xử lý như là object chứa luôn "year", "month", "day"
            JsonObject dateObject = json.getAsJsonObject();

            int year = dateObject.get("year").getAsInt();
            int month = dateObject.get("month").getAsInt();
            int day = dateObject.get("day").getAsInt();

            return LocalDate.of(year, month, day);
        }
    }


}

