package socket.handler;

import dao.GenericCRUD_DAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Request;
import model.Response;
import model.TaiKhoan;
import utils.HibernateUtil;

public class TaiKhoanHandler implements RequestHandler {

    @Override
    public Response<?> handle(Request<?> request) {
        try {
            EntityManager em = HibernateUtil.getEntityManager(); // Sử dụng HibernateUtil để lấy EntityManager
            GenericCRUD_DAO<TaiKhoan> taiKhoanDAO = new GenericCRUD_DAO<>(em, TaiKhoan.class);

            // Lấy thông tin từ request
            TaiKhoan requestData = (TaiKhoan) request.getData();

            // Tạo truy vấn để kiểm tra tài khoản
            TypedQuery<TaiKhoan> query = em.createQuery(
                    "SELECT t FROM TaiKhoan t WHERE t.tenDN = :tenDN AND t.matKhau = :matKhau", TaiKhoan.class);
            query.setParameter("tenDN", requestData.getTenDN());
            query.setParameter("matKhau", requestData.getMatKhau());

            TaiKhoan taiKhoan = query.getResultStream().findFirst().orElse(null);

            if (taiKhoan != null) {
                return new Response<>(true, taiKhoan);
            } else {
                return new Response<>(false, "Sai tên đăng nhập hoặc mật khẩu");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, "Lỗi server");
        }
    }
}
