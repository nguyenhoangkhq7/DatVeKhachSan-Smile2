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
        handlers.put("GET_ALL_DICH_VU", new DichVuHandler());
        handlers.put("GET_PHIEU_DAT_DICH_VU_BY_MA_PDP", new DichVuHandler());
        handlers.put("GET_ALL_PHONG_DAT_DICH_VU", new PhieuDatDichVuHandler());
        handlers.put("GET_PHONG_DA_DAT", new PhieuDatPhongHandler());
        handlers.put("GET_PHIEU_DAT_PHONG_DA_DAT", new PhieuDatPhongHandler());
        handlers.put("GET_PHIEU_DAT_PHONG_DA_DAT_IN_LINE", new PhieuDatPhongHandler());
        handlers.put("HUY_DAT_PHONG", new PhieuDatPhongHandler());
        handlers.put("GET_PHIEU_DAT_PHONG_DANG_SU_DUNG", new PhieuDatPhongHandler());
        handlers.put("NHAN_PHONG", new PhieuDatPhongHandler());
        handlers.put("TRA_PHONG", new PhieuDatPhongHandler());
        handlers.put("XOA_DICH_VU", new DichVuHandler());
        handlers.put("GET_TEN_LOAI_PHONG_BY_MA_PHONG", new PhongHandler());
        handlers.put("GET_SO_PHONG_BY_LOAI_PHONG", new PhongHandler());
        handlers.put("GET_TEN_LOAI_BY_MA_LOAI", new LoaiPhongHandler());
        handlers.put("TIM_KHACH_HANG_THEO_TEN", new KhachHangHandler());
        handlers.put("TIM_KHACH_HANG_THEO_CCCD", new KhachHangHandler());
        handlers.put("SUA_PHONG", new PhongHandler());
        handlers.put("READ_PHONG", new PhongHandler());
        handlers.put("THEM_KHACH_HANG", new KhachHangHandler());
        handlers.put("THEM_KHACH_HANG_BOOLEAN", new KhachHangHandler());
        handlers.put("SUA_PHONG_BOOLEAN", new PhongHandler());
        handlers.put("CREATE_HOA_DON", new HoaDonHandler());
        handlers.put("CHECK_PHIEU_DAT_PHONG_EXISTS", new PhieuDatPhongHandler());
        handlers.put("FIND_PHIEU_BY_MULTIPLE_CRITERIA", new PhieuDatPhongHandler());
        handlers.put("GET_ALL_PHONG_DAT_DICHVU", new DichVuHandler());
        handlers.put("GET_ALL_LOAIPHONG", new LoaiPhongHandler());
        handlers.put("THEM_PHONG", new PhongHandler());
        handlers.put("KIEM_TRA_MA_PHONG", new PhongHandler());
        handlers.put("XOA_PHONG", new PhongHandler());
        handlers.put("THONG_KE_DOANH_THU_PHONG", new HoaDonHandler());
        handlers.put("THONG_KE_DOANH_THU_DICH_VU", new HoaDonHandler());
        handlers.put("READ_HOA_DON", new HoaDonHandler());
        handlers.put("GET_ALL_HOA_DON", new HoaDonHandler());
        handlers.put("FIND_BY_CRITERIA", new HoaDonHandler());
        handlers.put("FIND_BY_MA_KH", new PhieuDatDichVuHandler());
        handlers.put("GET_ALL_PHIEU_DAT_DICH_VU", new PhieuDatDichVuHandler());
        handlers.put("GET_ALL_PHIEU_DAT_PHONGG", new PhieuDatPhongHandler());
        handlers.put("CREATE_PHIEU_DAT_DICH_VU_1", new PhieuDatDichVuHandler());
        handlers.put("GET_ALL_PHIEU_DAT_DICH_VU_1", new PhieuDatDichVuHandler());


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

