package socket;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class SocketClient {

    private String host;
    private int port;
    private Gson gson = new Gson();

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public <T> T sendRequest(Object request, Class<T> responseType) throws IOException {
        try (
                Socket socket = new Socket(host, port);
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Gửi request
            String requestJson = gson.toJson(request);
            out.println(requestJson);

            // Nhận response
            String responseJson = in.readLine();
            return gson.fromJson(responseJson, responseType);
        }
    }
}

