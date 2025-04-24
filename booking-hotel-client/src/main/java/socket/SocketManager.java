package socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utils.custom_element.LocalDateAdapter;
import utils.custom_element.LocalDateTimeAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SocketManager {

    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
//    private static final Gson gson = new Gson();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
            .create();

    public static void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static <T> void send(Object request) {
        String json = gson.toJson(request);
        out.println(json);
    }

    public static <T> T receive(Class<T> responseType) throws IOException {
        String json = in.readLine();
        return gson.fromJson(json, responseType);
    }

    public static void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
    public static String receiveRaw() throws IOException {
        return in.readLine();
    }
    public static <T> T receiveType(Type responseType) throws IOException {
        String json = in.readLine();
        System.out.println("RECEIVED JSON: " + json);
        return gson.fromJson(json, responseType);
    }
    public static <T> T receive2(Type type) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String json = reader.readLine();
        System.out.println("Nhận dữ liệu: " + json);
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }
}