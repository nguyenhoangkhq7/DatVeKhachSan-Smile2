package socket.handler;

import com.google.gson.Gson;
import dao.LoaiPhong_DAO;
import dto.LoaiPhongDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import model.Request;
import model.Response;
import utils.HibernateUtil;

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
            case "GET_TEN_LOAI_BY_MA_LOAI" -> {
                String maLoai = gson.fromJson(gson.toJson(request.getData()), String.class);
                System.out.println("Nhận yêu cầu GET_TEN_LOAI_BY_MA_LOAI với maLoai: " + maLoai);

                if (maLoai == null || maLoai.trim().isEmpty()) {
                    System.out.println("Lỗi: maLoai không hợp lệ (null hoặc rỗng)");
                    return new Response<>(false, "Mã loại phòng không hợp lệ: null hoặc rỗng");
                }

                try {
                    EntityManager em = HibernateUtil.getEntityManager();
                    try {
                        String jpql = "SELECT lp.tenLoai FROM LoaiPhong lp WHERE lp.maLoai = :maLoai";
                        String tenLoai = em.createQuery(jpql, String.class)
                                .setParameter("maLoai", maLoai)
                                .getSingleResult();

                        if (tenLoai == null) {
                            System.out.println("Không tìm thấy tenLoai cho maLoai: " + maLoai);
                            return new Response<>(false, "Không tìm thấy loại phòng với mã: " + maLoai);
                        }

                        System.out.println("Trả về tenLoai: " + tenLoai + " cho maLoai: " + maLoai);
                        return new Response<>(true, tenLoai);
                    } catch (NoResultException e) {
                        System.out.println("Không tìm thấy loại phòng cho maLoai: " + maLoai);
                        return new Response<>(false, "Không tìm thấy loại phòng với mã: " + maLoai);
                    } finally {
                        em.close();
                    }
                } catch (Exception e) {
                    System.out.println("Lỗi khi xử lý GET_TEN_LOAI_BY_MA_LOAI cho maLoai " + maLoai + ": " + e.getMessage());
                    e.printStackTrace();
                    return new Response<>(false, "Lỗi server khi lấy tên loại phòng: " + e.getMessage());
                }
            }
        }

        return new Response<>(false, "Hành động không được hỗ trợ");
    }
}
