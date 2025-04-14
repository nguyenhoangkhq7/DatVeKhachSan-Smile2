package socket.handler;

import com.google.gson.Gson;
import dao.GenericDAO;
import dto.TaiKhoanDTO;
import model.NhanVien;
import model.TaiKhoan;
import model.Request;
import model.Response;

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
                    int chucVu = result.get(0).getChucVu();
                    String role = (chucVu == 1) ? "ADMIN" : "NHANVIEN";
                    return new Response<>(true, role);
                } else {
                    return new Response<>(false, null);
                }
            }

            // các case khác có thể thêm vào đây
        }

        return new Response<>(false, null); // mặc định nếu không khớp action nào
    }

}


