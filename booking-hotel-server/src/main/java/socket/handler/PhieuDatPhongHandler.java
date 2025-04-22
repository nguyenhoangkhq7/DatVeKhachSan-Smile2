package socket.handler;

import com.google.gson.Gson;
import dao.PhieuDatPhong_DAO;
import dto.PhieuDatPhongDTO;
import model.Request;
import model.Response;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class PhieuDatPhongHandler implements RequestHandler {
    private final PhieuDatPhong_DAO phieuDatPhongDao;
    private final Gson gson;

    public PhieuDatPhongHandler() {
        this.phieuDatPhongDao = new PhieuDatPhong_DAO();
        this.gson = new Gson();
    }

    @Override
    public Response<?> handle(Request<?> request) throws IOException {
        String action = request.getAction();
        switch (action) {
            case "CREATE_PHIEU_DAT_PHONG" -> {
                PhieuDatPhongDTO pdpDTO = gson.fromJson(gson.toJson(request.getData()), PhieuDatPhongDTO.class);
                if (pdpDTO == null || pdpDTO.getMaPDP() == null || pdpDTO.getMaPDP().isEmpty()) {
                    return new Response<>(false, "Mã phiếu đặt phòng không hợp lệ");
                }
                boolean success = phieuDatPhongDao.create(pdpDTO);
                return new Response<>(success, success ? "Thêm phiếu đặt phòng thành công" : "Thêm phiếu đặt phòng thất bại");
            }
            case "READ_PHIEU_DAT_PHONG" -> {
                String maPhieu = gson.fromJson(gson.toJson(request.getData()), String.class);
                if (maPhieu == null || maPhieu.isEmpty()) {
                    return new Response<>(false, "Mã phiếu đặt phòng không hợp lệ");
                }
                PhieuDatPhongDTO pdpDTO = phieuDatPhongDao.read(maPhieu);
                if (pdpDTO == null) {
                    return new Response<>(false, "Phiếu đặt phòng không tồn tại");
                }
                return new Response<>(true, pdpDTO);
//                return new Response<>(true, pdpDTO != null ? pdpDTO : "Phiếu đặt phòng không tồn tại");
            }
            case "GET_ALL_PHIEU_DAT_PHONG" -> {
                try {
                    List<PhieuDatPhongDTO> list = phieuDatPhongDao.getAllPhieuDatPhongDTOs();
                    return new Response<>(true, list);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Response<>(false, "Lỗi hệ thống: " + e.getMessage());
                }
            }
            case "FIND_PHIEU_BY_MA_KH" -> {
                String maKH = gson.fromJson(gson.toJson(request.getData()), String.class);
                if (maKH == null || maKH.isEmpty()) {
                    return new Response<>(false, "Mã khách hàng không hợp lệ");
                }
                List<PhieuDatPhongDTO> ds = phieuDatPhongDao.findByMaKH(maKH);
                return new Response<>(true, ds);
            }
            case "FIND_PHIEU_BY_CRITERIA" -> {
                String[] criteria = gson.fromJson(gson.toJson(request.getData()), String[].class);
                if (criteria == null || criteria.length != 3) {
                    return new Response<>(false, "Tiêu chí tìm kiếm không hợp lệ");
                }
                String maKH = criteria[0].isEmpty() ? null : criteria[0];
                String maNV = criteria[1].isEmpty() ? null : criteria[1];
                Integer soPhong = criteria[2].isEmpty() ? null : Integer.parseInt(criteria[2]);
                List<PhieuDatPhongDTO> list = phieuDatPhongDao.findByMultipleCriteria(maKH, maNV, soPhong);
                return new Response<>(true, list);
            }
            default -> {
                return new Response<>(false, "Hành động không được hỗ trợ");
            }
        }
    }
}