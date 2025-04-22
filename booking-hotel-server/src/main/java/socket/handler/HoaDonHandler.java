package socket.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.HoaDon_DAO;
import dto.HoaDonDTO;
import dto.NhanVienDTO;
import model.Request;
import model.Response;
import utils.LocalDateAdapter;
import utils.LocalDateTimeAdapter;
import utils.LocalDateTimeDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class HoaDonHandler implements RequestHandler {
    private final HoaDon_DAO hoaDonDao;
    private final Gson gson;

    public HoaDonHandler() {
        this.hoaDonDao = new HoaDon_DAO();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()) // ✅ GIỮ LẠI
                .serializeNulls()
                .create();
    }
    @Override
    public Response<?> handle(Request<?> request) throws IOException {
        String action = request.getAction();
        switch (action) {
            case "CREATE_HOA_DON" -> {
                HoaDonDTO hoaDonDTO = gson.fromJson(gson.toJson(request.getData()), HoaDonDTO.class);
                if (hoaDonDTO == null || hoaDonDTO.getMaHD() == null || hoaDonDTO.getMaHD().isEmpty()) {
                    return new Response<>(false, "Mã hóa đơn không hợp lệ");
                }
                boolean success = hoaDonDao.create1(hoaDonDTO);
                return new Response<>(success, success ? "Thêm hóa đơn thành công" : "Thêm hóa đơn thất bại");
            }
            case "READ_HOA_DON" -> {
                String maHD = gson.fromJson(gson.toJson(request.getData()), String.class);
                if (maHD == null || maHD.isEmpty()) {
                    return new Response<>(false, "Mã hóa đơn không hợp lệ");
                }
                HoaDonDTO hoaDonDTO = hoaDonDao.read(maHD);
                return new Response<>(true, hoaDonDTO != null ? hoaDonDTO : "Hóa đơn không tồn tại");
            }
            case "GET_ALL_HOA_DON" -> {
                try {
                    List<HoaDonDTO> ds = hoaDonDao.getAllHoaDonDTOs();
                    return new Response<>(true, ds);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Response<>(false, "Lỗi hệ thống: " + e.getMessage());
                }
            }
            case "FIND_BY_MA_KH" -> {
                String maKH = gson.fromJson(gson.toJson(request.getData()), String.class);
                if (maKH == null || maKH.isEmpty()) {
                    return new Response<>(false, "Mã khách hàng không hợp lệ");
                }
                List<HoaDonDTO> hoaDonList = hoaDonDao.findByMaKH(maKH);
                return new Response<>(true, hoaDonList);
            }
            case "FIND_BY_CRITERIA" -> {
                String[] criteria = gson.fromJson(gson.toJson(request.getData()), String[].class);
                if (criteria == null || criteria.length != 3) {
                    return new Response<>(false, "Tiêu chí tìm kiếm không hợp lệ");
                }
                String maKH = criteria[0].isEmpty() ? null : criteria[0];
                String maNV = criteria[1].isEmpty() ? null : criteria[1];
                Integer soPhongDat = criteria[2].isEmpty() ? null : Integer.parseInt(criteria[2]);
                List<HoaDonDTO> hoaDonList = hoaDonDao.findByMultipleCriteria(maKH, maNV, soPhongDat);
                return new Response<>(true, hoaDonList);
            }
            default -> {
                return new Response<>(false, "Hành động không được hỗ trợ");
            }
        }
    }
}
