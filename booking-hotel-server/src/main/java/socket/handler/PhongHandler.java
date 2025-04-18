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
        }
        return new Response<>(false, "Hành động không được hỗ trợ");
    }
}
