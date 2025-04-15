package socket;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.time.LocalDate;

public class SocketManager {

    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static final Gson gson;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
            @Override
            public void write(JsonWriter out, LocalDate value) throws IOException {
                out.value(value.toString());
            }

            @Override
            public LocalDate read(JsonReader jsonReader) throws IOException {
                return LocalDate.parse(jsonReader.nextString());
            }
        });
        gson = builder.create();
    }

    public static Gson getGson() {
        return gson;
    }

    public static void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        socket.setSoTimeout(30000);
        socket.setKeepAlive(true);
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static <T> void send(Object request) {
        String json = gson.toJson(request);
        out.println(json);
    }

    public static <T> T receive(Type responseType) throws IOException {
        int retries = 3;
        while (retries > 0) {
            try {
                String json = in.readLine();
                if (json == null) {
                    throw new IOException("Server closed connection unexpectedly");
                }
                return gson.fromJson(json, responseType);
            } catch (IOException e) {
                retries--;
                if (retries == 0) {
                    throw new IOException("Failed to receive data after retries: " + e.getMessage(), e);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Interrupted during retry", ie);
                }
            }
        }
        throw new IOException("Unexpected error in receive");
    }

    public static void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Lỗi khi đóng socket: " + e.getMessage());
        } finally {
            in = null;
            out = null;
            socket = null;
        }
    }
}