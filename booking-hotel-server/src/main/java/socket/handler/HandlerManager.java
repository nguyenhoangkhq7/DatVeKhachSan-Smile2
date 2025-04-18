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
        handlers.put("GET_ALL_KHACH_HANG", new KhachHangHandler());
        handlers.put("SUA_KHACH_HANG", new KhachHangHandler());
        handlers.put("TIM_KHACH_HANG_NANG_CAO", new KhachHangHandler());
        handlers.put("GET_KHACH_HANG_BY_MA_KH", new KhachHangHandler());
        handlers.put("GET_ALL_PHONG", new PhongHandler());
        handlers.put("TIM_PHONG_NANG_CAO", new PhongHandler());
        handlers.put("CREATE_PHIEU_DAT_PHONG", new PhieuDatPhongHandler());
        handlers.put("READ_PHIEU_DAT_PHONG", new PhieuDatPhongHandler());
        handlers.put("GET_ALL_PHIEU_DAT_PHONG", new PhieuDatPhongHandler());
        handlers.put("FIND_PHIEU_BY_CRITERIA", new PhieuDatPhongHandler());
        handlers.put("FIND_PHIEU_BY_MA_KH", new PhieuDatPhongHandler());
        handlers.put("GET_PHONG_BY_MA_PHONG", new PhongHandler());



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

