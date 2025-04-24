package socket.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dao.PhieuDatPhong_DAO;
import dao.Phong_DAO;
import dto.PhieuDatPhongDTO;
import dto.PhongDTO;
import jakarta.persistence.EntityManager;
import model.Phong;
import model.Request;
import model.Response;
import utils.HibernateUtil;
import utils.custom_element.LocalDateAdapter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PhieuDatPhongHandler implements RequestHandler {
    private final PhieuDatPhong_DAO phieuDatPhongDao;
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();


    public PhieuDatPhongHandler() {
        this.phieuDatPhongDao = new PhieuDatPhong_DAO();
//        this.gson = new Gson();
    }

    @Override
    public Response<?> handle(Request<?> request) throws IOException {
        String action = request.getAction();
        switch (action) {
            case "CREATE_PHIEU_DAT_PHONG" -> {
                PhieuDatPhongDTO dto = gson.fromJson(
                        gson.toJson(request.getData()), PhieuDatPhongDTO.class
                );
                System.out.println("Nhận yêu cầu CREATE_PHIEU_DAT_PHONG với dữ liệu: " + gson.toJson(dto));
                if (dto == null || dto.getMaPDP() == null || dto.getMaPDP().isEmpty()) {
                    System.out.println("Lỗi: Mã phiếu đặt phòng không hợp lệ");
                    return new Response<>(false, "Mã phiếu đặt phòng không hợp lệ");
                }
                boolean success = phieuDatPhongDao.create3(dto);
                System.out.println("Kết quả thêm phiếu đặt phòng: " + success);
                return new Response<>(success, success);
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
                    System.out.println("Bắt đầu xử lý yêu cầu GET_ALL_PHIEU_DAT_PHONG...");
                    PhieuDatPhong_DAO phieuDatPhongDao = new PhieuDatPhong_DAO();
                    List<PhieuDatPhongDTO> phieuDatPhongList = phieuDatPhongDao.getAllPhieuDatPhongDTOs();
                    if (phieuDatPhongList == null) {
                        System.out.println("Lỗi: Không lấy được danh sách phiếu đặt phòng (danh sách null)");
                        return new Response<>(false, "Không lấy được danh sách phiếu đặt phòng (danh sách null)");
                    }
                    System.out.println("Số lượng phiếu đặt phòng trả về: " + phieuDatPhongList.size());
                    return new Response<>(true, phieuDatPhongList);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    String errorMessage = "Lỗi hệ thống khi lấy danh sách phiếu đặt phòng: " + ex.getMessage();
                    System.err.println(errorMessage);
                    return new Response<>(false, errorMessage);
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
//            case "GET_PHIEU_DAT_PHONG_DA_DAT_IN_LINE" -> {
//                try {
//                    System.out.println("Bắt đầu xử lý yêu cầu GET_PHIEU_DAT_PHONG_DA_DAT...");
//
//                    // Bước 1: Lấy danh sách tất cả phiếu đặt phòng
//                    System.out.println("Bước 1: Lấy danh sách phiếu đặt phòng...");
//                    PhieuDatPhong_DAO phieuDatPhongDao = new PhieuDatPhong_DAO();
//                    List<PhieuDatPhongDTO> allPhieuDatPhong = phieuDatPhongDao.getAllPhieuDatPhongDTOs();
//                    if (allPhieuDatPhong == null || allPhieuDatPhong.isEmpty()) {
//                        System.out.println("Không có phiếu đặt phòng nào trong hệ thống.");
//                        return new Response<>(true, "Không có phiếu đặt phòng nào trong hệ thống.");
//                    }
//                    System.out.println("Số lượng phiếu đặt phòng: " + allPhieuDatPhong.size());
//
//                    // Bước 2: Lấy danh sách tất cả phòng
//                    System.out.println("Bước 2: Lấy danh sách phòng...");
//                    Phong_DAO phongDao = new Phong_DAO();
//                    List<PhongDTO> phongList = phongDao.getAllPhongDTOs();
//                    if (phongList == null || phongList.isEmpty()) {
//                        System.out.println("Không có phòng nào trong hệ thống.");
//                        return new Response<>(true, "Không có phòng nào trong hệ thống.");
//                    }
//                    System.out.println("Số lượng phòng: " + phongList.size());
//
//                    // Bước 3: Tạo map trạng thái phòng
//                    System.out.println("Bước 3: Tạo map trạng thái phòng...");
//                    Map<String, Integer> phongTinhTrangMap = phongList.stream()
//                            .collect(Collectors.toMap(
//                                    PhongDTO::getMaPhong,
//                                    PhongDTO::getTinhTrang,
//                                    (existing, replacement) -> existing
//                            ));
//
//                    // Bước 4: Lọc phiếu đặt phòng
//                    System.out.println("Bước 4: Lọc phiếu đặt phòng...");
//                    List<PhieuDatPhongDTO> filteredPhieuDatPhong = allPhieuDatPhong.stream()
//                            .filter(pdp -> pdp.getDsMaPhong() != null && !pdp.getDsMaPhong().isEmpty())
//                            .filter(pdp -> pdp.getDsMaPhong().stream()
//                                    .anyMatch(maPhong -> {
//                                        Integer tinhTrang = phongTinhTrangMap.get(maPhong);
//                                        return tinhTrang != null && tinhTrang == 1;
//                                    }))
//                            .collect(Collectors.toList());
//
//                    System.out.println("Số lượng phiếu đặt phòng sau lọc: " + filteredPhieuDatPhong.size());
//                    if (filteredPhieuDatPhong.isEmpty()) {
//                        return new Response<>(true, "Không có phiếu đặt phòng nào với phòng đã đặt");
//                    }
//                    return new Response<>(true, filteredPhieuDatPhong);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    String errorMessage = "Lỗi hệ thống khi lấy dữ liệu phiếu đặt phòng: " + e.getMessage();
//                    System.err.println(errorMessage);
//                    return new Response<>(false, errorMessage);
//                }
//            }

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
            case "HUY_DAT_PHONG" -> {
                try {
                    System.out.println("Bắt đầu xử lý yêu cầu HUY_DAT_PHONG...");

                    // Lấy dữ liệu từ request: maPDP và maPhong
                    JsonObject requestData = gson.fromJson(gson.toJson(request.getData()), JsonObject.class);
                    String maPDP = requestData.get("maPDP").getAsString();
                    String maPhong = requestData.get("maPhong").getAsString();

                    if (maPDP == null || maPDP.isEmpty() || maPhong == null || maPhong.isEmpty()) {
                        System.out.println("Lỗi: Mã phiếu đặt phòng hoặc mã phòng không hợp lệ");
                        return new Response<>(false, "Mã phiếu đặt phòng hoặc mã phòng không hợp lệ");
                    }
                    System.out.println("Xử lý hủy đặt phòng cho maPDP = " + maPDP + ", maPhong = " + maPhong);

                    // Bước 1: Kiểm tra sự tồn tại của phiếu đặt phòng
                    System.out.println("Bước 1: Kiểm tra sự tồn tại của phiếu đặt phòng " + maPDP + "...");
                    PhieuDatPhong_DAO phieuDatPhongDao = new PhieuDatPhong_DAO();
                    PhieuDatPhongDTO pdpDTO = phieuDatPhongDao.read(maPDP);
                    if (pdpDTO == null) {
                        System.out.println("Lỗi: Phiếu đặt phòng " + maPDP + " không tồn tại");
                        return new Response<>(false, "Phiếu đặt phòng " + maPDP + " không tồn tại");
                    }
                    System.out.println("Phiếu đặt phòng " + maPDP + " tồn tại");

                    // Bước 2: Kiểm tra sự tồn tại của phòng được chọn
                    System.out.println("Bước 2: Kiểm tra sự tồn tại của phòng " + maPhong + "...");
                    Phong_DAO phongDao = new Phong_DAO();
                    PhongDTO phongDTO = phongDao.read(maPhong);
                    if (phongDTO == null) {
                        System.out.println("Lỗi: Phòng " + maPhong + " không tồn tại");
                        return new Response<>(false, "Phòng " + maPhong + " không tồn tại");
                    }
                    System.out.println("Trạng thái hiện tại của phòng " + maPhong + ": " + phongDTO.getTinhTrang());
                    if (phongDTO.getTinhTrang() != 1) {
                        System.out.println("Lỗi: Phòng " + maPhong + " không ở trạng thái Đã đặt (tinh_trang = " + phongDTO.getTinhTrang() + ")");
                        return new Response<>(false, "Phòng " + maPhong + " không ở trạng thái Đã đặt");
                    }

                    // Bước 3: Lấy danh sách tất cả các phòng liên kết với maPDP
                    System.out.println("Bước 3: Lấy danh sách tất cả các phòng liên kết với maPDP = " + maPDP + "...");
                    List<String> dsMaPhong = pdpDTO.getDsMaPhong();
                    if (dsMaPhong == null || dsMaPhong.isEmpty()) {
                        System.out.println("Lỗi: Không tìm thấy phòng nào liên kết với maPDP = " + maPDP);
                        return new Response<>(false, "Không tìm thấy phòng nào liên kết với phiếu đặt phòng " + maPDP);
                    }
                    System.out.println("Danh sách mã phòng liên kết: " + dsMaPhong);
                    // Kiểm tra xem maPhong có trong danh sách liên kết không
                    if (!dsMaPhong.contains(maPhong)) {
                        System.out.println("Lỗi: Phòng " + maPhong + " không liên kết với phiếu đặt phòng " + maPDP);
                        return new Response<>(false, "Phòng " + maPhong + " không liên kết với phiếu đặt phòng " + maPDP);
                    }

                    // Bước 4: Cập nhật trạng thái tất cả các phòng liên kết từ 1 (Đã đặt) sang 0 (Còn trống)
                    System.out.println("Bước 4: Cập nhật trạng thái tất cả các phòng liên kết...");
                    for (String mp : dsMaPhong) {
                        System.out.println("Cập nhật trạng thái phòng " + mp + "...");
                        PhongDTO pDTO = phongDao.read(mp);
                        if (pDTO == null) {
                            System.out.println("Lỗi: Phòng " + mp + " không tồn tại");
                            return new Response<>(false, "Phòng " + mp + " không tồn tại");
                        }
                        if (pDTO.getTinhTrang() != 1) {
                            System.out.println("Cảnh báo: Phòng " + mp + " không ở trạng thái Đã đặt (tinh_trang = " + pDTO.getTinhTrang() + "), bỏ qua cập nhật trạng thái");
                            continue;
                        }
                        pDTO.setTinhTrang(0); // Đổi trạng thái sang Còn trống
                        boolean updatedPhong = phongDao.update(pDTO);
                        if (!updatedPhong) {
                            System.out.println("Lỗi: Cập nhật trạng thái phòng " + mp + " thất bại");
                            return new Response<>(false, "Cập nhật trạng thái phòng " + mp + " thất bại");
                        }
                        System.out.println("Cập nhật trạng thái phòng " + mp + " thành công: tinh_trang = 0");
                    }

                    // Bước 5: Xóa tất cả liên kết trong chi_tiet_dat_phong liên quan đến maPDP
                    System.out.println("Bước 5: Xóa tất cả liên kết trong chi_tiet_dat_phong cho maPDP = " + maPDP + "...");
                    EntityManager em = HibernateUtil.getEntityManager();
                    try {
                        em.getTransaction().begin();
                        int deletedRows = em.createNativeQuery("DELETE FROM chi_tiet_dat_phong WHERE ma_pdp = :maPdp")
                                .setParameter("maPdp", maPDP)
                                .executeUpdate();
                        em.getTransaction().commit();
                        System.out.println("Xóa liên kết thành công: " + deletedRows + " hàng bị xóa");
                        if (deletedRows == 0) {
                            System.out.println("Cảnh báo: Không có liên kết nào trong chi_tiet_dat_phong cho maPDP = " + maPDP);
                        }
                    } catch (Exception ex) {
                        if (em.getTransaction().isActive()) {
                            em.getTransaction().rollback();
                        }
                        System.out.println("Lỗi khi xóa liên kết trong chi_tiet_dat_phong: " + ex.getMessage());
                        return new Response<>(false, "Lỗi khi xóa liên kết trong chi_tiet_dat_phong: " + ex.getMessage());
                    } finally {
                        em.close();
                    }

                    // Bước 6: Xóa phiếu đặt phòng
                    System.out.println("Bước 6: Xóa phiếu đặt phòng " + maPDP + "...");
                    boolean deletedPDP = phieuDatPhongDao.delete(maPDP);
                    if (!deletedPDP) {
                        System.out.println("Lỗi: Xóa phiếu đặt phòng " + maPDP + " thất bại");
                        return new Response<>(false, "Xóa phiếu đặt phòng " + maPDP + " thất bại");
                    }
                    System.out.println("Xóa phiếu đặt phòng " + maPDP + " thành công");

                    System.out.println("Hủy đặt phòng thành công cho maPDP = " + maPDP);
                    return new Response<>(true, "Hủy đặt phòng thành công");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    String errorMessage = "Lỗi hệ thống khi hủy đặt phòng: " + ex.getMessage();
                    System.err.println(errorMessage);
                    return new Response<>(false, errorMessage);
                }
            }
            case "GET_PHIEU_DAT_PHONG_DANG_SU_DUNG" -> {
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

                    // Bước 4: Lọc phiếu đặt phòng chỉ với phòng đang sử dụng (tình trạng = 2)
                    System.out.println("Bước 4: Lọc phiếu đặt phòng với phòng đang sử dụng...");
                    List<PhieuDatPhongDTO> filteredPhieuDatPhong = allPhieuDatPhong.stream()
                            .filter(pdp -> pdp.getDsMaPhong() != null && !pdp.getDsMaPhong().isEmpty())
                            .filter(pdp -> pdp.getDsMaPhong().stream()
                                    .anyMatch(maPhong -> {
                                        Integer tinhTrang = phongTinhTrangMap.get(maPhong);
                                        return tinhTrang != null && tinhTrang == 2; // Chỉ lấy những phòng có tình trạng đang sử dụng (2)
                                    }))
                            .collect(Collectors.toList());

                    System.out.println("Số lượng phiếu đặt phòng sau lọc: " + filteredPhieuDatPhong.size());
                    if (filteredPhieuDatPhong.isEmpty()) {
                        return new Response<>(true, "Không có phiếu đặt phòng nào với phòng đang sử dụng");
                    }
                    return new Response<>(true, filteredPhieuDatPhong);

                } catch (Exception e) {
                    e.printStackTrace();
                    String errorMessage = "Lỗi hệ thống khi lấy dữ liệu phiếu đặt phòng: " + e.getMessage();
                    System.err.println(errorMessage);
                    return new Response<>(false, errorMessage);
                }
            }
            case "NHAN_PHONG" -> {
                try {
                    JsonObject requestData = gson.fromJson(gson.toJson(request.getData()), JsonObject.class);
                    String maPDP = requestData.get("maPDP").getAsString();
                    String maPhong = requestData.get("maPhong").getAsString();

                    if (maPDP == null || maPDP.isEmpty() || maPhong == null || maPhong.isEmpty()) {
                        return new Response<>(false, "Mã phiếu đặt phòng hoặc mã phòng không hợp lệ");
                    }

                    PhieuDatPhong_DAO phieuDatPhongDao = new PhieuDatPhong_DAO();
                    PhieuDatPhongDTO pdpDTO = phieuDatPhongDao.read(maPDP);
                    if (pdpDTO == null || !pdpDTO.getDsMaPhong().contains(maPhong)) {
                        return new Response<>(false, "Phiếu đặt phòng hoặc phòng không hợp lệ");
                    }

                    Phong_DAO phongDao = new Phong_DAO();
                    PhongDTO phongDTO = phongDao.read(maPhong);
                    if (phongDTO == null || phongDTO.getTinhTrang() != 1) {
                        return new Response<>(false, "Phòng không tồn tại hoặc không ở trạng thái Đã đặt");
                    }

                    phongDTO.setTinhTrang(2); // Change to Đang sử dụng
                    boolean updated = phongDao.update(phongDTO);
                    if (!updated) {
                        return new Response<>(false, "Cập nhật trạng thái phòng thất bại");
                    }

                    return new Response<>(true, "Nhận phòng thành công");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return new Response<>(false, "Lỗi hệ thống khi nhận phòng: " + ex.getMessage());
                }
            }
            case "TRA_PHONG" -> {
                try {
                    JsonObject requestData = gson.fromJson(gson.toJson(request.getData()), JsonObject.class);
                    String maPDP = requestData.get("maPDP").getAsString();
                    String maPhong = requestData.get("maPhong").getAsString();

                    if (maPDP == null || maPDP.isEmpty() || maPhong == null || maPhong.isEmpty()) {
                        return new Response<>(false, "Mã phiếu đặt phòng hoặc mã phòng không hợp lệ");
                    }

                    PhieuDatPhong_DAO phieuDatPhongDao = new PhieuDatPhong_DAO();
                    PhieuDatPhongDTO pdpDTO = phieuDatPhongDao.read(maPDP);
                    if (pdpDTO == null || !pdpDTO.getDsMaPhong().contains(maPhong)) {
                        return new Response<>(false, "Phiếu đặt phòng hoặc phòng không hợp lệ");
                    }

                    Phong_DAO phongDao = new Phong_DAO();
                    PhongDTO phongDTO = phongDao.read(maPhong);
                    if (phongDTO == null || phongDTO.getTinhTrang() != 2) {
                        return new Response<>(false, "Phòng không tồn tại hoặc không ở trạng thái Đang sử dụng");
                    }

                    // Update room status to 0 (Còn trống)
                    phongDTO.setTinhTrang(0);
                    boolean updatedPhong = phongDao.update(phongDTO);
                    if (!updatedPhong) {
                        return new Response<>(false, "Cập nhật trạng thái phòng thất bại");
                    }

                    // Delete booking details from chi_tiet_dat_phong
                    EntityManager em = HibernateUtil.getEntityManager();
                    try {
                        em.getTransaction().begin();
                        int deletedRows = em.createNativeQuery("DELETE FROM chi_tiet_dat_phong WHERE ma_pdp = :maPdp")
                                .setParameter("maPdp", maPDP)
                                .executeUpdate();
                        em.getTransaction().commit();
                        if (deletedRows == 0) {
                            System.out.println("Cảnh báo: Không có liên kết nào trong chi_tiet_dat_phong cho maPDP = " + maPDP);
                        }
                    } catch (Exception ex) {
                        if (em.getTransaction().isActive()) {
                            em.getTransaction().rollback();
                        }
                        return new Response<>(false, "Lỗi khi xóa liên kết trong chi_tiet_dat_phong: " + ex.getMessage());
                    } finally {
                        em.close();
                    }

                    // Delete the booking
                    boolean deletedPDP = phieuDatPhongDao.delete(maPDP);
                    if (!deletedPDP) {
                        return new Response<>(false, "Xóa phiếu đặt phòng thất bại");
                    }

                    return new Response<>(true, "Trả phòng thành công");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return new Response<>(false, "Lỗi hệ thống khi trả phòng: " + ex.getMessage());
                }
            }
        }
        return null;
    }
}
