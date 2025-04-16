package socket;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class SocketManager {

    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static final Gson gson = new Gson();

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
}