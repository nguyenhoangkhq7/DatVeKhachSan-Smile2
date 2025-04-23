package socket.handler;

import com.google.gson.Gson;
import dao.HoaDon_DAO;
import dto.HoaDonDTO;
import model.Request;
import model.Response;

import java.io.IOException;
import java.util.List;

public class HoaDonHandler implements RequestHandler {
    private final HoaDon_DAO hoaDonDao;
    private final Gson gson;

    public HoaDonHandler() {
        this.hoaDonDao = new HoaDon_DAO();
        this.gson = new Gson();
    }

    @Override
    public Response<?> handle(Request<?> request) throws IOException {
        String action = request.getAction();
        System.out.println("Xử lý yêu cầu: " + action); // Debug
        try {
            switch (action) {
                case "GET_ALL_HOADON" -> {
                    List<HoaDonDTO> ds = hoaDonDao.getAllHoaDonDTOs();
                    if (ds.isEmpty()) {
                        return new Response<>(false, "Không có hóa đơn nào trong cơ sở dữ liệu");
                    }
                    return new Response<>(true, ds);
                }
                case "KIEM_TRA_MA_HOADON" -> {
                    String maHoaDon = gson.fromJson(gson.toJson(request.getData()), String.class);
                    if (maHoaDon == null || maHoaDon.trim().isEmpty()) {
                        return new Response<>(false, "Mã hóa đơn không hợp lệ");
                    }
                    boolean exists = hoaDonDao.existsByMaHoaDon(maHoaDon.trim());
                    return new Response<>(true, exists);
                }
                case "THONG_KE_DOANH_THU_PHONG" -> {
                    List<Object[]> ds = hoaDonDao.getDoanhThuPhongTheoNamThang();
                    System.out.println("Dữ liệu doanh thu phòng: " + ds); // Debug
                    if (ds.isEmpty()) {
                        return new Response<>(false, "Không có dữ liệu doanh thu phòng");
                    }
                    return new Response<>(true, ds);
                }
                case "THONG_KE_DOANH_THU_DICH_VU" -> {
                    List<Object[]> ds = hoaDonDao.getDoanhThuDichVuTheoNamThang();
                    System.out.println("Dữ liệu doanh thu dịch vụ: " + ds); // Debug
                    if (ds.isEmpty()) {
                        return new Response<>(false, "Không có dữ liệu doanh thu dịch vụ");
                    }
                    return new Response<>(true, ds);
                }
                default -> {
                    return new Response<>(false, "Hành động không được hỗ trợ: " + action);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi xử lý yêu cầu " + action + ": " + e.getMessage());
            e.printStackTrace();
            return new Response<>(false, "Lỗi server: " + e.getMessage());
        }
    }
}