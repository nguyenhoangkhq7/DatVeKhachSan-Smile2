package socket.handler;

import com.google.gson.Gson;
import dao.GenericDAO;
import dto.TaiKhoanDTO;
import model.NhanVien;
import model.TaiKhoan;
import model.Request;
import model.Response;
import utils.session.SessionManager;

import java.util.List;

public class TaiKhoanHandler implements RequestHandler {

    @Override
    public Response<?> handle(Request<?> request) {
        Gson gson = new Gson();

        String action = request.getAction();
        switch (action) {
            case "DANG_NHAP" -> {
                TaiKhoanDTO dto = gson.fromJson(gson.toJson(request.getData()), TaiKhoanDTO.class);

                GenericDAO<NhanVien> nhanVienDAO = new GenericDAO<>(NhanVien.class);
                List<NhanVien> result = nhanVienDAO.findByCondition(nv -> {
                    TaiKhoan tk = nv.getTaiKhoan();
                    return tk != null &&
                            tk.getTenDN().equals(dto.getTenDN()) &&
                            tk.getMatKhau().equals(dto.getMatKhau());
                });

                if (!result.isEmpty()) {
                    NhanVien nhanVien = result.get(0);
                    SessionManager.setCurrentUser(nhanVien);
                    return new Response<>(true, nhanVien);
                } else {
                    return new Response<>(false, null);
                }
            }
        }

        return new Response<>(false, null); // mặc định nếu không khớp action nào
    }

}


