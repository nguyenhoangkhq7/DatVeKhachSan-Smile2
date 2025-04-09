package socket;

import model.Request;
import model.Response;
import socket.handler.HandlerManager;
import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    private static final int PORT = 12345;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server đang chạy trên cổng " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Đợi client kết nối
                System.out.println("Client đã kết nối: " + clientSocket.getInetAddress());

                new Thread(() -> handleClient(clientSocket)).start(); // Mỗi client xử lý riêng 1 luồng
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        ) {
            String requestJson = reader.readLine();
            Request<?> request = gson.fromJson(requestJson, Request.class);

            // xử lý dữ liêu
            Response<?> response = HandlerManager.handle(request); // Chuyển xử lý đến Handler tương ứng

            String responseJson = gson.toJson(response);
            writer.write(responseJson);
            writer.newLine();
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

