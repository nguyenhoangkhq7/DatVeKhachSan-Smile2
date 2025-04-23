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
        handlers.put("GET_ALL_KHACH_HANG", new KhachHangHandler());
        handlers.put("SUA_KHACH_HANG", new KhachHangHandler());
        handlers.put("TIM_KHACH_HANG_NANG_CAO", new KhachHangHandler());
        handlers.put("GET_ALL_PHONG", new PhongHandler());
        handlers.put("TIM_PHONG_NANG_CAO", new PhongHandler());
        handlers.put("GET_ALL_LOAIPHONG", new LoaiPhongHandler());
        handlers.put("THEM_PHONG", new PhongHandler());
        handlers.put("SUA_PHONG", new PhongHandler());
        handlers.put("KIEM_TRA_MA_PHONG", new PhongHandler());
        handlers.put("XOA_PHONG", new PhongHandler());
        handlers.put("THONG_KE_DOANH_THU_PHONG", new HoaDonHandler());
        handlers.put("THONG_KE_DOANH_THU_DICH_VU", new HoaDonHandler());

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

