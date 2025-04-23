package socket.handler;

import com.google.gson.Gson;
import dao.KhachHang_DAO;
import dto.KhachHangDTO;
import model.Request;
import model.Response;

import java.io.IOException;
import java.util.List;

public class KhachHangHandler implements RequestHandler {
    private final KhachHang_DAO khachHangDao;
    private final Gson gson;

    public KhachHangHandler() {
        this.khachHangDao = new KhachHang_DAO();
        this.gson = new Gson();
    }

    @Override
    public Response<?> handle(Request<?> request) throws IOException {
        String action = request.getAction();
        switch (action) {
            case "GET_ALL_KHACH_HANG" -> {
                List<KhachHangDTO> ds = khachHangDao.getAllKhachHangDTOs();
                return new Response<>(true, ds);
            }
            case "THEM_KHACH_HANG" -> {
                KhachHangDTO dto = gson.fromJson(
                        gson.toJson(request.getData()), KhachHangDTO.class
                );
                if (dto == null || dto.getMaKH() == null || dto.getMaKH().isEmpty()) {
                    return new Response<>(false, "Mã khách hàng không hợp lệ");
                }
                boolean success = khachHangDao.create(dto);
                return new Response<>(success, success ? "Thêm khách hàng thành công" : "Thêm khách hàng thất bại");
            }
<<<<<<< HEAD
            case "SUA_KHACH_HANG" -> {
                KhachHangDTO dto = gson.fromJson(
                        gson.toJson(request.getData()), KhachHangDTO.class
                );
                if (dto == null || dto.getMaKH() == null || dto.getMaKH().isEmpty()) {
                    return new Response<>(false, "Mã khách hàng không hợp lệ");
                }
                boolean success = khachHangDao.update(dto);
                return new Response<>(success, success ? "Cập nhật khách hàng thành công" : "Cập nhật khách hàng thất bại");
=======

            case "SUA_KHACH_HANG" -> {
                KhachHangDTO dto = gson.fromJson(gson.toJson(request.getData()), KhachHangDTO.class);
                if (dto == null || dto.getMaKH() == null || dto.getMaKH().isEmpty()) {
                    System.out.println("Invalid KhachHangDTO: null or empty maKH");
                    return new Response<>(false, "Mã khách hàng không hợp lệ");
                }
                boolean success = khachHangDao.update(dto);
                String message = success ? "Sửa khách hàngthành công" : "Sửa khách hàng thất bại";
                System.out.println("SUA_KHACH_HANG result: " + message);
                return new Response<>(success, message);
>>>>>>> main
            }
            case "TIM_KHACH_HANG_THEO_TEN" -> {
                String hoTen = gson.fromJson(gson.toJson(request.getData()), String.class);
                if (hoTen == null || hoTen.isEmpty()) {
                    return new Response<>(false, "Họ tên không hợp lệ");
                }
                List<KhachHangDTO> ds = khachHangDao.findByHoTen(hoTen);
                return new Response<>(true, ds);
            }
            case "TIM_KHACH_HANG_NANG_CAO" -> {
<<<<<<< HEAD
                // Tìm kiếm nâng cao: [hoTen, email, soDienThoai]
                String[] criteria = gson.fromJson(gson.toJson(request.getData()), String[].class);
                if (criteria == null || criteria.length != 3) {
                    return new Response<>(false, "Tiêu chí tìm kiếm không hợp lệ");
                }
                String hoTen = criteria[0].isEmpty() ? null : criteria[0];
                String email = criteria[1].isEmpty() ? null : criteria[1];
                String sdt = criteria[2].isEmpty() ? null : criteria[2];

                List<KhachHangDTO> ds = khachHangDao.findByMultipleCriteria(hoTen, email, sdt);
=======
                // Tìm kiếm nâng cao theo keyword
                String keyword = gson.fromJson(gson.toJson(request.getData()), String.class);
                if (keyword == null || keyword.trim().isEmpty()) {
                    return new Response<>(false, "Từ khóa tìm kiếm không hợp lệ");
                }

                List<KhachHangDTO> ds = khachHangDao.findByKeyword(keyword.trim());
>>>>>>> main
                return new Response<>(true, ds);
            }
        }
        return new Response<>(false, "Hành động không được hỗ trợ");
    }
}
