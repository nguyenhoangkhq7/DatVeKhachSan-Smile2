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
                // Tìm nâng cao: [tenPhong, tinhTrang (as String), maLoaiPhong]
                String[] criteria = gson.fromJson(gson.toJson(request.getData()), String[].class);
                if (criteria == null || criteria.length != 3) {
                    return new Response<>(false, "Tiêu chí tìm kiếm không hợp lệ");
                }

                String tenPhong = criteria[0].isEmpty() ? null : criteria[0];
                Integer tinhTrang = null;
                if (!criteria[1].isEmpty()) {
                    try {
                        tinhTrang = Integer.parseInt(criteria[1]);
                    } catch (NumberFormatException e) {
                        return new Response<>(false, "Tình trạng không hợp lệ");
                    }
                }
                String maLoaiPhong = criteria[2].isEmpty() ? null : criteria[2];

                List<PhongDTO> ds = phongDao.findByMultipleCriteria(tenPhong, tinhTrang, maLoaiPhong);
                return new Response<>(true, ds);
            }
        }
        return new Response<>(false, "Hành động không được hỗ trợ");
    }
}
