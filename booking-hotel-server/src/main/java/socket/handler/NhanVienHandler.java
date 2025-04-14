package socket.handler;

import com.google.gson.Gson;
import dao.NhanVien_DAO;
import dto.NhanVienDTO;
import model.Request;
import model.Response;

import java.io.IOException;
import java.util.List;

public class NhanVienHandler implements RequestHandler{
    private final NhanVien_DAO nhanVienDao;
    private final Gson gson;

    public NhanVienHandler() {
        this.nhanVienDao = new NhanVien_DAO();
        this.gson = new Gson();
    }

    @Override
    public Response<?> handle(Request<?> request) throws IOException {
        String action = request.getAction();
        switch (action) {
            case "GET_ALL_NHAN_VIEN" -> {
                try {
                    List<NhanVienDTO> ds = nhanVienDao.getAllNhanVienDTOs();
                    return new Response<>(true, ds);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Response<>(false, "Lỗi hệ thống: " + e.getMessage());
                }
            }
            case "THEM_NHAN_VIEN" -> {
                NhanVienDTO dto = gson.fromJson(
                        gson.toJson(request.getData()), NhanVienDTO.class
                );
                if (dto == null || dto.getMaNhanVien() == null || dto.getMaNhanVien().isEmpty()) {
                    return new Response<>(false, "Mã nhân viên không hợp lệ");
                }
                boolean sucess = nhanVienDao.create(dto);
                return new Response<>(sucess, sucess ? "Thêm nhân viên thành công" : "Thêm nhân viên thất bại");
            }
            case "CAP_NHAT_NHAN_VIEN" -> {
                // Sửa thông tin nhân viên
                NhanVienDTO dto = gson.fromJson(gson.toJson(request.getData()), NhanVienDTO.class);
                if (dto == null || dto.getMaNhanVien() == null || dto.getMaNhanVien().isEmpty()) {
                    return new Response<>(false, "Mã nhân viên không hợp lệ");
                }
                boolean success = nhanVienDao.update(dto);
                return new Response<>(success, success ? "Sửa nhân viên thành công" : "Sửa nhân viên thất bại");
            }

            case "XOA_NHAN_VIEN" -> {
                // Xóa nhân viên theo mã
                String maNhanVien = gson.fromJson(gson.toJson(request.getData()), String.class);
                if (maNhanVien == null || maNhanVien.isEmpty()) {
                    return new Response<>(false, "Mã nhân viên không hợp lệ");
                }
                boolean success = nhanVienDao.delete(maNhanVien);
                return new Response<>(success, success ? "Xóa nhân viên thành công" : "Xóa nhân viên thất bại");
            }
            case "TIM_NHAN_VIEN_THEO_TEN" -> {
                // Tìm kiếm nhân viên theo tên gần đúng
                String hoTen = gson.fromJson(gson.toJson(request.getData()), String.class);
                if (hoTen == null || hoTen.isEmpty()) {
                    return new Response<>(false, "Tên nhân viên không hợp lệ");
                }
                List<NhanVienDTO> ds = nhanVienDao.findByHoTen(hoTen);
                return new Response<>(true, ds);
            }
            case "TIM_NHAN_VIEN_NANG_CAO" -> {
                // Tìm kiếm nhân viên theo nhiều tiêu chí
                String[] criteria = gson.fromJson(gson.toJson(request.getData()), String[].class);
                if (criteria == null || criteria.length != 3) {
                    return new Response<>(false, "Tiêu chí tìm kiếm không hợp lệ");
                }
                String hoTen = criteria[0].isEmpty() ? null : criteria[0];
                String email = criteria[1].isEmpty() ? null : criteria[1];
                String sdt = criteria[2].isEmpty() ? null : criteria[2];
                List<NhanVienDTO> ds = nhanVienDao.findByMultipleCriteria(hoTen, email, sdt);
                return new Response<>(true, ds);
            }
        }
        return new Response<>(false, "Hành động không được hỗ trợ");
    }
}
