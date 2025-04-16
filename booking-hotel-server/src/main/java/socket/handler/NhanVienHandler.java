package socket.handler;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dao.NhanVien_DAO;
import dto.NhanVienDTO;
import model.Request;
import model.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class NhanVienHandler implements RequestHandler {
    private final NhanVien_DAO nhanVienDao;
    private final Gson gson;

    public NhanVienHandler() {
        this.nhanVienDao = new NhanVien_DAO();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
            @Override
            public void write(JsonWriter out, LocalDate value) throws IOException {
                out.value(value.toString());
            }

            @Override
            public LocalDate read(JsonReader jsonReader) throws IOException {
                return LocalDate.parse(jsonReader.nextString());
            }
        });
        this.gson = builder.create();
    }

    @Override
    public Response<?> handle(Request<?> request) throws IOException {
        String action = request.getAction();
        System.out.println("Handling action: " + action);
        System.out.println("Request data: " + gson.toJson(request.getData()));

        try {
            switch (action) {
                case "GET_ALL_NHAN_VIEN" -> {
                    List<NhanVienDTO> ds = nhanVienDao.getAllNhanVienDTOs();
                    return new Response<>(true, ds);
                }
                case "THEM_NHAN_VIEN" -> {
                    // Tránh ép kiểu trực tiếp, sử dụng Gson để deserialize an toàn
                    NhanVienDTO dto = gson.fromJson(gson.toJson(request.getData()), NhanVienDTO.class);
                    if (dto == null || dto.getMaNhanVien() == null || dto.getMaNhanVien().isEmpty()) {
                        System.out.println("Invalid NhanVienDTO: null or empty maNhanVien");
                        return new Response<>(false, "Mã nhân viên không hợp lệ");
                    }
                    boolean success = nhanVienDao.create(dto);
                    String message = success ? "Thêm nhân viên thành công" : "Thêm nhân viên thất bại";
                    System.out.println("THEM_NHAN_VIEN result: " + message);
                    return new Response<>(success, message);
                }
                case "CAP_NHAT_NHAN_VIEN" -> {
                    NhanVienDTO dto = gson.fromJson(gson.toJson(request.getData()), NhanVienDTO.class);
                    if (dto == null || dto.getMaNhanVien() == null || dto.getMaNhanVien().isEmpty()) {
                        System.out.println("Invalid NhanVienDTO: null or empty maNhanVien");
                        return new Response<>(false, "Mã nhân viên không hợp lệ");
                    }
                    boolean success = nhanVienDao.update(dto);
                    String message = success ? "Sửa nhân viên thành công" : "Sửa nhân viên thất bại";
                    System.out.println("CAP_NHAT_NHAN_VIEN result: " + message);
                    return new Response<>(success, message);
                }
                case "XOA_NHAN_VIEN" -> {
                    String maNhanVien = gson.fromJson(gson.toJson(request.getData()), String.class);
                    if (maNhanVien == null || maNhanVien.isEmpty()) {
                        System.out.println("Invalid maNhanVien: null or empty");
                        return new Response<>(false, "Mã nhân viên không hợp lệ");
                    }
                    boolean success = nhanVienDao.delete(maNhanVien);
                    String message = success ? "Xóa nhân viên thành công" : "Xóa nhân viên thất bại";
                    System.out.println("XOA_NHAN_VIEN result: " + message);
                    return new Response<>(success, message);
                }
                case "TIM_NHAN_VIEN_THEO_TEN" -> {
                    String hoTen = gson.fromJson(gson.toJson(request.getData()), String.class);
                    if (hoTen == null || hoTen.isEmpty()) {
                        System.out.println("Invalid hoTen: null or empty");
                        return new Response<>(false, "Tên nhân viên không hợp lệ");
                    }
                    List<NhanVienDTO> ds = nhanVienDao.findByHoTen(hoTen);
                    System.out.println("TIM_NHAN_VIEN_THEO_TEN found: " + ds.size() + " results");
                    return new Response<>(true, ds);
                }
                case "TIM_NHAN_VIEN_NANG_CAO" -> {
                    String[] criteria = gson.fromJson(gson.toJson(request.getData()), String[].class);
                    if (criteria == null || criteria.length != 3) {
                        System.out.println("Invalid criteria: null or incorrect length");
                        return new Response<>(false, "Tiêu chí tìm kiếm không hợp lệ");
                    }
                    String hoTen = criteria[0].isEmpty() ? null : criteria[0];
                    String email = criteria[1].isEmpty() ? null : criteria[1];
                    String sdt = criteria[2].isEmpty() ? null : criteria[2];
                    List<NhanVienDTO> ds = nhanVienDao.findByMultipleCriteria(hoTen, email, sdt);
                    System.out.println("TIM_NHAN_VIEN_NANG_CAO found: " + ds.size() + " results");
                    return new Response<>(true, ds);
                }
                default -> {
                    System.out.println("Unsupported action: " + action);
                    return new Response<>(false, "Hành động không được hỗ trợ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error handling " + action + ": " + e.getMessage());
            return new Response<>(false, "Lỗi hệ thống: " + e.getMessage());
        }
    }
}