package utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.beginObject();

        out.name("date").value(value.toLocalDate().toString()); // Ghi date dưới dạng chuỗi

        out.name("time");
        out.beginObject();
        out.name("hour").value(value.getHour());
        out.name("minute").value(value.getMinute());
        out.name("second").value(value.getSecond());
        out.name("nano").value(value.getNano());
        out.endObject();

        out.endObject();
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        LocalDate date = null;
        int hour = 0, minute = 0, second = 0, nano = 0;

        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "date":
                    String dateStr = in.nextString(); // Đọc date dưới dạng chuỗi
                    date = LocalDate.parse(dateStr); // Parse chuỗi thành LocalDate
                    break;
                case "time":
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "hour":
                                hour = in.nextInt();
                                break;
                            case "minute":
                                minute = in.nextInt();
                                break;
                            case "second":
                                second = in.nextInt();
                                break;
                            case "nano":
                                nano = in.nextInt();
                                break;
                        }
                    }
                    in.endObject();
                    break;
            }
        }
        in.endObject();

        return date != null ? LocalDateTime.of(date, LocalTime.of(hour, minute, second, nano)) : null;
    }
}