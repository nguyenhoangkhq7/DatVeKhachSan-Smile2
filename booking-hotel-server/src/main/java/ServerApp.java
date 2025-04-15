import model.Request;
import model.Response;
import socket.handler.HandlerManager;
import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {

    private static final int PORT = 12345;
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server đang chạy trên cổng " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Đợi client kết nối
                System.out.println("Client đã kết nối: " + clientSocket.getInetAddress());

                new Thread(() -> handleClient(clientSocket)).start(); // Mỗi client xử lý riêng 1 luồng
                // Tạo một luồng để xử lý client
                // handleClient(clientSocket); // Nếu không sử dụng luồng, sẽ chờ client xử lý xong mới tiếp tục
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
            Gson gson = new Gson();

            // Đọc chuỗi JSON từ client
            String requestJson = reader.readLine();
            if (requestJson == null || requestJson.isEmpty()) {
                System.out.println("Yêu cầu rỗng hoặc không hợp lệ từ client.");
                return;
            }

            // Chuyển JSON thành đối tượng Request
            Request<?> request = gson.fromJson(requestJson, Request.class);

            // Gọi HandlerManager để xử lý và nhận về Response tương ứng
            Response<?> response = HandlerManager.handle(request);

            // Chuyển Response thành JSON và gửi về lại client
            String responseJson = gson.toJson(response);
            writer.write(responseJson);
            writer.newLine();
            writer.flush();

        } catch (IOException e) {
            System.err.println("Lỗi khi xử lý client: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

