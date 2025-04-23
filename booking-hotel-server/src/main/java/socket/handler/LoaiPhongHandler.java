package socket.handler;

import com.google.gson.Gson;
import dao.LoaiPhong_DAO;
import dto.LoaiPhongDTO;
import model.Request;
import model.Response;

import java.io.IOException;
import java.util.List;

public class LoaiPhongHandler implements RequestHandler {
    private final LoaiPhong_DAO loaiPhongDao;
    private final Gson gson;

    public LoaiPhongHandler() {
        this.loaiPhongDao = new LoaiPhong_DAO();
        this.gson = new Gson();
    }

    @Override
    public Response<?> handle(Request<?> request) throws IOException {
        String action = request.getAction();
        switch (action) {
            case "GET_ALL_LOAIPHONG" -> {
                List<LoaiPhongDTO> ds = loaiPhongDao.getAllLoaiPhongDTOs();
                return new Response<>(true, ds);
            }
            case "THEM_LOAIPHONG" -> {
                LoaiPhongDTO dto = gson.fromJson(
                        gson.toJson(request.getData()), LoaiPhongDTO.class
                );
                if (dto == null || dto.getMaLoai() == null || dto.getMaLoai().isEmpty()) {
                    return new Response<>(false, "Mã loại phòng không hợp lệ");
                }
                boolean success = loaiPhongDao.create(dto);
                return new Response<>(success, success ? "Thêm loại phòng thành công" : "Thêm loại phòng thất bại");
            }
            case "SUA_LOAIPHONG" -> {
                LoaiPhongDTO dto = gson.fromJson(
                        gson.toJson(request.getData()), LoaiPhongDTO.class
                );
                if (dto == null || dto.getMaLoai() == null || dto.getMaLoai().isEmpty()) {
                    return new Response<>(false, "Mã loại phòng không hợp lệ");
                }
                boolean success = loaiPhongDao.update(dto);
                return new Response<>(success, success ? "Cập nhật loại phòng thành công" : "Cập nhật loại phòng thất bại");
            }
            case "XOA_LOAIPHONG" -> {
                String maLoai = gson.fromJson(gson.toJson(request.getData()), String.class);
                if (maLoai == null || maLoai.isEmpty()) {
                    return new Response<>(false, "Mã loại phòng không hợp lệ");
                }
                boolean success = loaiPhongDao.delete(maLoai);
                return new Response<>(success, success ? "Xóa loại phòng thành công" : "Xóa loại phòng thất bại");
            }
            case "TIM_LOAIPHONG_THEO_TEN" -> {
                String tenLoai = gson.fromJson(gson.toJson(request.getData()), String.class);
                if (tenLoai == null || tenLoai.isEmpty()) {
                    return new Response<>(false, "Tên loại phòng không hợp lệ");
                }
                List<LoaiPhongDTO> ds = loaiPhongDao.findByTenLoaiPhong(tenLoai);
                return new Response<>(true, ds);
            }
        }

        return new Response<>(false, "Hành động không được hỗ trợ");
    }
}
