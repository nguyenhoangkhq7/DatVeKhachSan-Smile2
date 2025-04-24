package socket.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.GenericDAO;
import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import dto.LoginResponse;
import dto.TaiKhoanDTO;
import model.NhanVien;
import model.TaiKhoan;
import model.Request;
import model.Response;
import socket.SocketManager;
import utils.session.SessionManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class TaiKhoanHandler implements RequestHandler {

    @Override
    public Response<?> handle(Request<?> request) throws IOException {
        Gson gson = new Gson();

        String action = request.getAction();
        switch (action) {
//            case "DANG_NHAP" -> {
//                TaiKhoanDTO dto = gson.fromJson(gson.toJson(request.getData()), TaiKhoanDTO.class);
//
//                GenericDAO<NhanVien> nhanVienDAO = new GenericDAO<>(NhanVien.class);
//                List<NhanVien> result = nhanVienDAO.findByCondition(nv -> {
//                    TaiKhoan tk = nv.getTaiKhoan();
//                    return tk != null &&
//                            tk.getTenDN().equals(dto.getTenDN()) &&
//                            tk.getMatKhau().equals(dto.getMatKhau());
//                });
//
//                if (!result.isEmpty()) {
//                    NhanVien nhanVien = result.get(0);
//                    SessionManager.setCurrentUser(nhanVien);
//                    return new Response<>(true, nhanVien);
//                } else {
//                    return new Response<>(false, null);
//                }
//            }
            case "DANG_NHAP" -> {
                // Chuyển đổi request sang kiểu Request<TaiKhoanDTO>
                Type requestType = new TypeToken<Request<TaiKhoanDTO>>() {}.getType();
                Request<TaiKhoanDTO> typedRequest = gson.fromJson(gson.toJson(request), requestType);

                if (typedRequest == null || typedRequest.getData() == null) {
                    System.out.println("Không nhận được dữ liệu đăng nhập!");
                    return new Response<>(false, "Không nhận được dữ liệu");
                }

                TaiKhoanDTO taiKhoanDTO = typedRequest.getData();
                System.out.println("Nhận yêu cầu DANG_NHAP: tenDN=" + taiKhoanDTO.getTenDN());

                TaiKhoan_DAO taiKhoanDao = new TaiKhoan_DAO();
                TaiKhoan taiKhoan = taiKhoanDao.findByUsernameAndPassword(taiKhoanDTO.getTenDN(), taiKhoanDTO.getMatKhau());

                if (taiKhoan == null) {
                    System.out.println("Đăng nhập thất bại: Tài khoản không tồn tại!");
                    return new Response<>(false, "Tài khoản không tồn tại");
                }

                // Lấy tenDN từ TaiKhoan để tìm NhanVien
                String tenDN = taiKhoan.getTenDN();

                // Tìm nhân viên dựa trên tenDN (thông qua quan hệ taiKhoan)
                NhanVien_DAO nhanVienDao = new NhanVien_DAO();
                NhanVien nhanVien = nhanVienDao.findByMaTK(tenDN);
                if (nhanVien == null) {
                    System.out.println("Đăng nhập thất bại: Không tìm thấy nhân viên liên kết với tài khoản tenDN=" + tenDN);
                    return new Response<>(false, "Không tìm thấy nhân viên liên kết với tài khoản");
                }

                System.out.println("Thông tin nhân viên: maNV=" + nhanVien.getMaNhanVien() + ", hoTen=" + nhanVien.getHoTen() + ", chucVu=" + nhanVien.getChucVu());

                // Tạo LoginResponse
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setMaNV(nhanVien.getMaNhanVien());
                loginResponse.setTenNV(nhanVien.getHoTen());
                loginResponse.setChucVu(nhanVien.getChucVu());

                return new Response<>(true, loginResponse);
            }
            default -> {
                return new Response<>(false, "Hành động không được hỗ trợ");
            }
        }
//        return new Response<>(false, null);
    }

}


