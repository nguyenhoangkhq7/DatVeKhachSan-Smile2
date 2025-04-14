package socket.handler;

import model.Request;
import model.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HandlerManager {

    private static final Map<String, RequestHandler> handlers = new HashMap<>();

    static {
        // Đăng ký handler ứng với action
        handlers.put("DANG_NHAP", new TaiKhoanHandler());
        handlers.put("GET_ALL_NHAN_VIEN", new NhanVienHandler());
    }

    public static Response<?> handle(Request<?> request) throws IOException {
        String action = request.getAction();
        RequestHandler handler = handlers.get(action);


        if (handler != null) {
            return handler.handle(request);
        } else {
            return new Response<>(false, null);
        }
    }
}

