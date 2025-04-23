package socket.handler;

import com.google.gson.Gson;
import dao.DichVu_DAO;
import dto.DichVuDTO;
import model.Request;
import model.Response;

import java.io.IOException;
import java.util.List;

public class DichVuHandler implements RequestHandler{
    private final DichVu_DAO dichVuDao;
    private final Gson gson;

    public DichVuHandler() {
        this.dichVuDao = new DichVu_DAO();
        this.gson = new Gson();
    }
    @Override
    public Response<?> handle(Request<?> request) throws IOException {
        String action = request.getAction();
        switch (action) {
            case "GET_ALL_DICH_VU" -> {
                List<DichVuDTO> ds = dichVuDao.findAllDTOs();
                return new Response<>(true, ds);
            }
            case "THEM_DICH_VU" -> {
                DichVuDTO dto = gson.fromJson(
                        gson.toJson(request.getData()), DichVuDTO.class
                );
                if (dto == null || dto.getMaDV() == null || dto.getMaDV().isEmpty()) {
                    return new Response<>(false, "Mã dịch vụ không hợp lệ");
                }
                boolean sucess = dichVuDao.create(dto);
                return new Response<>(sucess, sucess ? "Thêm dịch vụ thành công" : "Thêm dịch vụ thất bại");
            }

            case "SUA_DICH_VU" -> {
                DichVuDTO dto = gson.fromJson(
                        gson.toJson(request.getData()), DichVuDTO.class
                );
                if (dto == null || dto.getMaDV() == null || dto.getMaDV().isEmpty()) {
                    return new Response<>(false, "Mã dịch vụ không hợp lệ");
                }
                boolean sucess = dichVuDao.update(dto);
                return new Response<>(sucess, sucess ? "Cập nhật thành công" : "Cập nhật thất bại");
            }
            case "TIM_DICH_VU_THEO_TEN" -> {
                String tenDV = gson.fromJson(gson.toJson(request.getData()), String.class);
                if (tenDV == null || tenDV.isEmpty()) {
                    return new Response<>(false, "Tên dịch vụ không hợp lệ");
                }
                List<DichVuDTO> ds = dichVuDao.findByTenDV(tenDV);
                return new Response<>(true, ds);
            }
            case "TIM_DICH_VU_NANG_CAO" -> {
                // Tìm kiếm dịch vụ theo nhiều tiêu chí
                String[] criteria = gson.fromJson(gson.toJson(request.getData()), String[].class);
                if (criteria == null || criteria.length != 4) {
                    return new Response<>(false, "Tiêu chí tìm kiếm không hợp lệ");
                }
                String tenDV = criteria[0].isEmpty() ? null : criteria[0];
                Double minGia = criteria[1].isEmpty() ? null : Double.parseDouble(criteria[1]);
                Double maxGia = criteria[2].isEmpty() ? null : Double.parseDouble(criteria[2]);
                String donViTinh = criteria[3].isEmpty() ? null : criteria[3];
                List<DichVuDTO> ds = dichVuDao.findByMultipleCriteria(tenDV, minGia, maxGia, donViTinh);
                return new Response<>(true, ds);
            }
            case "XOA_DICH_VU" -> {
                System.out.println("Handling XOA_DICH_VU request for maDV: " + request.getData());
                try {
                    String maDV = gson.fromJson(gson.toJson(request.getData()), String.class);
                    if (maDV == null || maDV.isEmpty()) {
                        System.out.println("Invalid maDV: null or empty");
                        Response<String> response = new Response<>(false, "Mã dịch vụ không hợp lệ");
                        System.out.println("Returning response: " + gson.toJson(response));
                        return response;
                    }
                    DichVuDTO existingDV = dichVuDao.read(maDV);
                    if (existingDV == null) {
                        System.out.println("XOA_DICH_VU result: Dịch vụ không tồn tại với mã " + maDV);
                        Response<String> response = new Response<>(false, "Dịch vụ không tồn tại với mã " + maDV);
                        System.out.println("Returning response: " + gson.toJson(response));
                        return response;
                    }
                    boolean success = dichVuDao.delete(new DichVuDTO(maDV, "", 0, "", ""));
                    String message = success ? "Xóa dịch vụ thành công" : "Không thể xóa dịch vụ, có thể đang được sử dụng trong phiếu đặt dịch vụ";
                    System.out.println("XOA_DICH_VU result: " + message);
                    Response<String> response = new Response<>(success, message);
                    System.out.println("Returning response: " + gson.toJson(response));
                    return response;
                } catch (Exception e) {
                    System.out.println("XOA_DICH_VU error: " + e.getMessage());
                    e.printStackTrace();
                    Response<String> response = new Response<>(false, "Lỗi không xác định khi xóa dịch vụ: " + e.getMessage());
                    System.out.println("Returning error response: " + gson.toJson(response));
                    return response;
                }
            }
        }
        return new Response<>(false, "Hành động không được hỗ trợ");
    }
}
