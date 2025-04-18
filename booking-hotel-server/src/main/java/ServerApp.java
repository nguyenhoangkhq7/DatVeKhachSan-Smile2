import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Request;
import model.Response;
import socket.handler.HandlerManager;
import utils.custom_element.LocalDateAdapter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;

public class ServerApp {

    private static final int PORT = 12345;
//    private static final Gson gson = new Gson();
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server đang chạy trên cổng " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client đã kết nối: " + clientSocket.getInetAddress());

                new Thread(() -> handleClient(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            String requestJson;
            while ((requestJson = reader.readLine()) != null) {
                if (requestJson.isEmpty()) continue;

                Request<?> request = gson.fromJson(requestJson, Request.class);
                Response<?> response = HandlerManager.handle(request);

                String responseJson = gson.toJson(response);
                writer.write(responseJson);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi xử lý client: " + e.getMessage());
        }
    }
}
