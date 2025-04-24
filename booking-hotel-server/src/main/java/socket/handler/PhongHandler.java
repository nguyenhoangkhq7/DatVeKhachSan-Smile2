package socket.handler;

import com.google.gson.Gson;
import dao.Phong_DAO;
import dto.PhongDTO;
import model.Request;
import model.Response;

import java.io.IOException;
import java.util.List;

public class PhongHandler implements RequestHandler {
    private final Phong_DAO phongDao;
    private final Gson gson;

    public PhongHandler() {
        this.phongDao = new Phong_DAO();
        this.gson = new Gson();
    }

    @Override
    public Response<?> handle(Request<?> request) throws IOException {
        String action = request.getAction();
        switch (action) {
            case "GET_ALL_PHONG" -> {
                List<PhongDTO> ds = phongDao.getAllPhongDTOs();
                return new Response<>(true, ds);
            }
            case "THEM_PHONG" -> {
                PhongDTO dto = gson.fromJson(
                        gson.toJson(request.getData()), PhongDTO.class
                );
                if (dto == null || dto.getMaPhong() == null || dto.getMaPhong().isEmpty()) {
                    return new Response<>(false, "Mã phòng không hợp lệ");
                }
                boolean success = phongDao.create(dto);
                return new Response<>(success, success ? "Thêm phòng thành công" : "Thêm phòng thất bại");
            }
            case "SUA_PHONG" -> {
                PhongDTO dto = gson.fromJson(
                        gson.toJson(request.getData()), PhongDTO.class
                );
                if (dto == null || dto.getMaPhong() == null || dto.getMaPhong().isEmpty()) {
                    return new Response<>(false, "Mã phòng không hợp lệ");
                }
                boolean success = phongDao.update(dto);
                return new Response<>(success, success ? "Cập nhật phòng thành công" : "Cập nhật phòng thất bại");
            }
            case "TIM_PHONG_THEO_TEN" -> {
                String tenPhong = gson.fromJson(gson.toJson(request.getData()), String.class);
                if (tenPhong == null || tenPhong.isEmpty()) {
                    return new Response<>(false, "Tên phòng không hợp lệ");
                }
                List<PhongDTO> ds = phongDao.findByTenPhong(tenPhong);
                return new Response<>(true, ds);
            }
            case "TIM_PHONG_NANG_CAO" -> {
                // Tìm kiếm nâng cao phòng theo keyword
                String keyword = gson.fromJson(gson.toJson(request.getData()), String.class);
                if (keyword == null || keyword.trim().isEmpty()) {
                    return new Response<>(false, "Từ khóa tìm kiếm không hợp lệ");
                }
                List<PhongDTO> ds = phongDao.findByKeyword(keyword.trim());
                return new Response<>(true, ds);
            }
            case "GET_PHONG_BY_MA_PHONG" -> {
                String maPhong = gson.fromJson(gson.toJson(request.getData()), String.class);
                PhongDTO phong = phongDao.read(maPhong);
                if (phong == null) {
                    return new Response<>(false, "Không tìm thấy phòng với mã: " + maPhong);
                }
                return new Response<>(true, phong);
            }
            case "GET_TEN_LOAI_PHONG_BY_MA_PHONG" -> {
                String maPhong = gson.fromJson(gson.toJson(request.getData()), String.class);
                System.out.println("Nhận yêu cầu GET_TEN_LOAI_PHONG_BY_MA_PHONG với maPhong: " + maPhong);

                if (maPhong == null || maPhong.trim().isEmpty()) {
                    System.out.println("Lỗi: maPhong không hợp lệ (null hoặc rỗng)");
                    return new Response<>(false, "Mã phòng không hợp lệ: null hoặc rỗng");
                }

                try {
                    String tenLoai = phongDao.getTenLoaiPhongByMaPhong(maPhong);
                    if (tenLoai == null) {
                        System.out.println("Không tìm thấy tenLoai cho maPhong: " + maPhong);
                        // Kiểm tra thêm để xác định nguyên nhân
                        PhongDTO phong = phongDao.read(maPhong);
                        if (phong == null) {
                            System.out.println("Lỗi: maPhong " + maPhong + " không tồn tại trong bảng phong");
                            return new Response<>(false, "Không tìm thấy phòng với mã: " + maPhong);
                        } else if (phong.getMaLoai() == null) {
                            System.out.println("Lỗi: maPhong " + maPhong + " có ma_loai_phong null hoặc không hợp lệ");
                            return new Response<>(false, "Phòng " + maPhong + " không có loại phòng được liên kết");
                        } else {
                            System.out.println("Lỗi: ma_loai_phong của " + maPhong + " không khớp với bảng loai_phong");
                            return new Response<>(false, "Không tìm thấy loại phòng cho mã phòng: " + maPhong);
                        }
                    }

                    System.out.println("Trả về tenLoai: " + tenLoai + " cho maPhong: " + maPhong);
                    return new Response<>(true, tenLoai);
                } catch (Exception e) {
                    System.out.println("Lỗi khi xử lý GET_TEN_LOAI_PHONG_BY_MA_PHONG cho maPhong " + maPhong + ": " + e.getMessage());
                    e.printStackTrace();
                    return new Response<>(false, "Lỗi server khi lấy loại phòng cho mã phòng " + maPhong + ": " + e.getMessage());
                }
            }
            case "GET_SO_PHONG_BY_LOAI_PHONG" -> {
                String tenLoaiPhong = gson.fromJson(gson.toJson(request.getData()), String.class);
                if (tenLoaiPhong == null || tenLoaiPhong.isEmpty()) {
                    return new Response<>(false, "Tên loại phòng không hợp lệ");
                }
                List<String> dsSoPhong = phongDao.getSoPhongByLoaiPhong(tenLoaiPhong);
                return new Response<>(true, dsSoPhong);
            }
        }
        return new Response<>(false, "Hành động không được hỗ trợ");
    }
}