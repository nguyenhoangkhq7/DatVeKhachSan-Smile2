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
        handlers.put("THEM_NHAN_VIEN", new NhanVienHandler());
        handlers.put("CAP_NHAT_NHAN_VIEN", new NhanVienHandler());
        handlers.put("XOA_NHAN_VIEN", new NhanVienHandler());
        handlers.put("TIM_NHAN_VIEN_THEO_TEN", new NhanVienHandler());
        handlers.put("TIM_NHAN_VIEN_NANG_CAO", new NhanVienHandler());
        handlers.put("THEM_DICH_VU", new DichVuHandler());
        handlers.put("SUA_DICH_VU", new DichVuHandler());
        handlers.put("TIM_DICH_VU_THEO_TEN", new DichVuHandler());
        handlers.put("TIM_DICH_VU_NANG_CAO", new DichVuHandler());


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

