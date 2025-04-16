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
    private static String host;
    private static int port;
    private static final int MAX_RETRIES = 3;

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
        SocketManager.host = host;
        SocketManager.port = port;
        reconnect();
    }

    private static void reconnect() throws IOException {
        close();
        System.out.println("Attempting to connect to " + host + ":" + port);
        socket = new Socket(host, port);
        socket.setSoTimeout(30000);
        socket.setKeepAlive(true);
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Connected to server at " + host + ":" + port);
    }

    public static <T> void send(Object request) throws IOException {
        if (socket == null || socket.isClosed() || !socket.isConnected()) {
            reconnect();
        }
        String json = gson.toJson(request);
        System.out.println("Sending request: " + json);
        out.println(json);
        out.flush();
    }

    public static <T> T receive(Type responseType) throws IOException {
        int retries = MAX_RETRIES;
        while (retries > 0) {
            try {
                if (socket == null || socket.isClosed() || !socket.isConnected()) {
                    reconnect();
                }
                String json = in.readLine();
                if (json == null) {
                    throw new IOException("Server closed connection unexpectedly");
                }
                System.out.println("Received response: " + json);
                return gson.fromJson(json, responseType);
            } catch (IOException e) {
                retries--;
                System.out.println("Receive attempt " + (MAX_RETRIES - retries) + " failed: " + e.getMessage());
                if (retries == 0) {
                    throw new IOException("Failed to receive data after " + MAX_RETRIES + " retries: " + e.getMessage(), e);
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