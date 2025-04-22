package utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        out.beginObject();

        out.name("date");
        out.beginObject();
        out.name("year").value(value.getYear());
        out.name("month").value(value.getMonthValue());
        out.name("day").value(value.getDayOfMonth());
        out.endObject();

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
        int year = 0, month = 0, day = 0, hour = 0, minute = 0, second = 0, nano = 0;

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "date":
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "year":
                                year = in.nextInt();
                                break;
                            case "month":
                                month = in.nextInt();
                                break;
                            case "day":
                                day = in.nextInt();
                                break;
                        }
                    }
                    in.endObject();
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
        return LocalDateTime.of(year, month, day, hour, minute, second, nano);
    }
}
