package socket.handler;

import com.google.gson.Gson;
import dao.PhieuDatPhong_DAO;
import dao.Phong_DAO;
import dto.PhieuDatPhongDTO;
import dto.PhongDTO;
import model.Phong;
import model.Request;
import model.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
            case "GET_PHONG_DA_DAT" -> {
                try {
                    System.out.println("Bắt đầu xử lý yêu cầu GET_PHONG_DA_DAT...");

                    Phong_DAO phongDao = new Phong_DAO();
                    System.out.println("Bước 1: Gọi findPhongDaDat()...");
                    List<PhongDTO> filteredPhong = phongDao.findPhongDaDat();

                    System.out.println("Số lượng phòng đã đặt: " + filteredPhong.size());
                    if (filteredPhong.isEmpty()) {
                        return new Response<>(true, "Không có phòng nào ở trạng thái đã đặt");
                    }
                    return new Response<>(true, filteredPhong);
                } catch (Exception e) {
                    e.printStackTrace();
                    String errorMessage = "Lỗi hệ thống khi lấy dữ liệu phòng: " + e.getMessage();
                    System.err.println(errorMessage);
                    return new Response<>(false, errorMessage);
                }
            }
            case "GET_PHIEU_DAT_PHONG_DA_DAT_IN_LINE" -> {
                try {
                    System.out.println("Bắt đầu xử lý yêu cầu GET_PHIEU_DAT_PHONG_DA_DAT...");

                    // Bước 1: Lấy danh sách tất cả phiếu đặt phòng
                    System.out.println("Bước 1: Lấy danh sách phiếu đặt phòng...");
                    PhieuDatPhong_DAO phieuDatPhongDao = new PhieuDatPhong_DAO();
                    List<PhieuDatPhongDTO> allPhieuDatPhong = phieuDatPhongDao.getAllPhieuDatPhongDTOs();
                    if (allPhieuDatPhong == null || allPhieuDatPhong.isEmpty()) {
                        System.out.println("Không có phiếu đặt phòng nào trong hệ thống.");
                        return new Response<>(true, "Không có phiếu đặt phòng nào trong hệ thống.");
                    }
                    System.out.println("Số lượng phiếu đặt phòng: " + allPhieuDatPhong.size());

                    // Bước 2: Lấy danh sách tất cả phòng
                    System.out.println("Bước 2: Lấy danh sách phòng...");
                    Phong_DAO phongDao = new Phong_DAO();
                    List<PhongDTO> phongList = phongDao.getAllPhongDTOs();
                    if (phongList == null || phongList.isEmpty()) {
                        System.out.println("Không có phòng nào trong hệ thống.");
                        return new Response<>(true, "Không có phòng nào trong hệ thống.");
                    }
                    System.out.println("Số lượng phòng: " + phongList.size());

                    // Bước 3: Tạo map trạng thái phòng
                    System.out.println("Bước 3: Tạo map trạng thái phòng...");
                    Map<String, Integer> phongTinhTrangMap = phongList.stream()
                            .collect(Collectors.toMap(
                                    PhongDTO::getMaPhong,
                                    PhongDTO::getTinhTrang,
                                    (existing, replacement) -> existing
                            ));

                    // Bước 4: Lọc phiếu đặt phòng
                    System.out.println("Bước 4: Lọc phiếu đặt phòng...");
                    List<PhieuDatPhongDTO> filteredPhieuDatPhong = allPhieuDatPhong.stream()
                            .filter(pdp -> pdp.getDsMaPhong() != null && !pdp.getDsMaPhong().isEmpty())
                            .filter(pdp -> pdp.getDsMaPhong().stream()
                                    .anyMatch(maPhong -> {
                                        Integer tinhTrang = phongTinhTrangMap.get(maPhong);
                                        return tinhTrang != null && tinhTrang == 1;
                                    }))
                            .collect(Collectors.toList());

                    System.out.println("Số lượng phiếu đặt phòng sau lọc: " + filteredPhieuDatPhong.size());
                    if (filteredPhieuDatPhong.isEmpty()) {
                        return new Response<>(true, "Không có phiếu đặt phòng nào với phòng đã đặt");
                    }
                    return new Response<>(true, filteredPhieuDatPhong);
                } catch (Exception e) {
                    e.printStackTrace();
                    String errorMessage = "Lỗi hệ thống khi lấy dữ liệu phiếu đặt phòng: " + e.getMessage();
                    System.err.println(errorMessage);
                    return new Response<>(false, errorMessage);
                }
            }

            case "GET_PHIEU_DAT_PHONG_DA_DAT" -> {
                try {
                    System.out.println("Bắt đầu xử lý yêu cầu GET_PHIEU_DAT_PHONG_DA_DAT...");

                    // Bước 1: Lấy danh sách tất cả phiếu đặt phòng
                    System.out.println("Bước 1: Lấy danh sách phiếu đặt phòng...");
                    PhieuDatPhong_DAO phieuDatPhongDao = new PhieuDatPhong_DAO();
                    List<PhieuDatPhongDTO> allPhieuDatPhong = phieuDatPhongDao.getAllPhieuDatPhongDTOs();
                    if (allPhieuDatPhong == null || allPhieuDatPhong.isEmpty()) {
                        System.out.println("Không có phiếu đặt phòng nào trong hệ thống.");
                        return new Response<>(true, "Không có phiếu đặt phòng nào trong hệ thống.");
                    }
                    System.out.println("Số lượng phiếu đặt phòng: " + allPhieuDatPhong.size());

                    // Đếm số khách hàng duy nhất
                    long soKhachHangDuyNhat = allPhieuDatPhong.stream()
                            .map(PhieuDatPhongDTO::getMaKH)
                            .filter(Objects::nonNull)
                            .distinct()
                            .count();
                    System.out.println("Số khách hàng duy nhất: " + soKhachHangDuyNhat);

                    // Bước 2: Lấy danh sách tất cả phòng
                    System.out.println("Bước 2: Lấy danh sách phòng...");
                    Phong_DAO phongDao = new Phong_DAO();
                    List<PhongDTO> phongList = phongDao.getAllPhongDTOs();
                    if (phongList == null || phongList.isEmpty()) {
                        System.out.println("Không có phòng nào trong hệ thống.");
                        return new Response<>(true, "Không có phòng nào trong hệ thống.");
                    }
                    System.out.println("Số lượng phòng: " + phongList.size());

                    // Bước 3: Tạo map trạng thái phòng
                    System.out.println("Bước 3: Tạo map trạng thái phòng...");
                    Map<String, Integer> phongTinhTrangMap = phongList.stream()
                            .collect(Collectors.toMap(
                                    PhongDTO::getMaPhong,
                                    PhongDTO::getTinhTrang,
                                    (existing, replacement) -> existing
                            ));

                    // Bước 4: Lọc phiếu đặt phòng
                    System.out.println("Bước 4: Lọc phiếu đặt phòng...");
                    List<PhieuDatPhongDTO> filteredPhieuDatPhong = allPhieuDatPhong.stream()
                            .filter(pdp -> pdp.getDsMaPhong() != null && !pdp.getDsMaPhong().isEmpty())
                            .filter(pdp -> pdp.getDsMaPhong().stream()
                                    .anyMatch(maPhong -> {
                                        Integer tinhTrang = phongTinhTrangMap.get(maPhong);
                                        return tinhTrang != null && tinhTrang == 1;
                                    }))
                            .collect(Collectors.toList());

                    System.out.println("Số lượng phiếu đặt phòng sau lọc: " + filteredPhieuDatPhong.size());
                    if (filteredPhieuDatPhong.isEmpty()) {
                        return new Response<>(true, "Không có phiếu đặt phòng nào với phòng đã đặt");
                    }
                    return new Response<>(true, filteredPhieuDatPhong);
                } catch (Exception e) {
                    e.printStackTrace();
                    String errorMessage = "Lỗi hệ thống khi lấy dữ liệu phiếu đặt phòng: " + e.getMessage();
                    System.err.println(errorMessage);
                    return new Response<>(false, errorMessage);
                }
            }
        }
        return null;
    }
}
