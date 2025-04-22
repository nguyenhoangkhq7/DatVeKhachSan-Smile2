package socket.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.PhieuDatDichVu_DAO;
import dto.PhieuDatDichVuDTO;
import model.Request;
import model.Response;
import utils.LocalDateAdapter;
import utils.LocalDateTimeAdapter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class PhieuDatDichVuHandler implements RequestHandler {
    private final PhieuDatDichVu_DAO phieuDatDichVuDao;
    private final Gson gson;

    public PhieuDatDichVuHandler() {
        this.phieuDatDichVuDao = new PhieuDatDichVu_DAO();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .serializeNulls()
                .create();
    }

    @Override
    public Response<?> handle(Request<?> request) throws IOException {
        String action = request.getAction();
        switch (action) {
            case "CREATE_PHIEU_DAT_DICH_VU" -> {
                PhieuDatDichVuDTO phieuDatDichVuDTO = gson.fromJson(gson.toJson(request.getData()), PhieuDatDichVuDTO.class);
                if (phieuDatDichVuDTO == null || phieuDatDichVuDTO.getMaPDDV() == null || phieuDatDichVuDTO.getMaPDDV().isEmpty()) {
                    return new Response<>(false, "Mã phiếu đặt dịch vụ không hợp lệ");
                }
                boolean success = phieuDatDichVuDao.create1(phieuDatDichVuDTO);
                return new Response<>(success, success ? "Thêm phiếu đặt dịch vụ thành công" : "Thêm phiếu đặt dịch vụ thất bại");
            }
            case "READ_PHIEU_DAT_DICH_VU" -> {
                String maPDDV = gson.fromJson(gson.toJson(request.getData()), String.class);
                if (maPDDV == null || maPDDV.isEmpty()) {
                    return new Response<>(false, "Mã phiếu đặt dịch vụ không hợp lệ");
                }
                PhieuDatDichVuDTO phieuDatDichVuDTO = phieuDatDichVuDao.read(maPDDV);
                return new Response<>(true, phieuDatDichVuDTO != null ? phieuDatDichVuDTO : "Phiếu đặt dịch vụ không tồn tại");
            }
            case "GET_ALL_PHIEU_DAT_DICH_VU" -> {
                try {
                    List<PhieuDatDichVuDTO> ds = phieuDatDichVuDao.getAllPhieuDatDichVuDTOs();
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
                List<PhieuDatDichVuDTO> phieuDatDichVuList = phieuDatDichVuDao.findByMaKH(maKH);
                return new Response<>(true, phieuDatDichVuList);
            }
            case "FIND_BY_CRITERIA" -> {
                String[] criteria = gson.fromJson(gson.toJson(request.getData()), String[].class);
                if (criteria == null || criteria.length != 3) {
                    return new Response<>(false, "Tiêu chí tìm kiếm không hợp lệ");
                }
                String maKH = criteria[0].isEmpty() ? null : criteria[0];
                String maNV = criteria[1].isEmpty() ? null : criteria[1];
                Integer soLuongDichVu = null;
                try {
                    soLuongDichVu = criteria[2].isEmpty() ? null : Integer.parseInt(criteria[2]);
                } catch (NumberFormatException e) {
                    return new Response<>(false, "Số lượng dịch vụ không hợp lệ");
                }
                List<PhieuDatDichVuDTO> phieuDatDichVuList = phieuDatDichVuDao.findByMultipleCriteria(maKH, maNV, soLuongDichVu);
                return new Response<>(true, phieuDatDichVuList);
            }
            default -> {
                return new Response<>(false, "Hành động không được hỗ trợ");
            }
        }
    }
}