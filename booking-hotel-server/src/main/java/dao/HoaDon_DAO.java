package dao;

import dto.HoaDonDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import mapper.GenericMapper;
import mapper.impl.HoaDonMapperImpl;
import model.HoaDon;
import utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HoaDon_DAO extends GenericDAO<HoaDon> {
    private final GenericMapper<HoaDon, HoaDonDTO> mapper;

    public HoaDon_DAO() {
        super(HoaDon.class);
        this.mapper = new HoaDonMapperImpl();
    }

    public List<HoaDonDTO> getAllHoaDonDTOs() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<HoaDon> cq = cb.createQuery(HoaDon.class);
            Root<HoaDon> root = cq.from(HoaDon.class);
            cq.select(root);

            List<HoaDon> resultList = em.createQuery(cq).getResultList();
            return resultList.stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    public List<Object[]> getDoanhThuPhongTheoNamThang() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            String sql = """
                SELECT
                    YEAR(hd.ngay_lap_hd) AS nam,
                    MONTH(hd.ngay_lap_hd) AS thang,
                    SUM(p.gia_phong * hd.so_luong_dat) AS doanh_thu_phong
                FROM hoa_don hd
                JOIN phieudatphong pdp ON hd.ma_phieu_dat_phong = pdp.ma_ddp
                JOIN phong p ON pdp.ma_ddp = p.ma_ddp
                GROUP BY YEAR(hd.ngay_lap_hd), MONTH(hd.ngay_lap_hd)
                ORDER BY nam, thang
            """;
            List<Object[]> result = em.createNativeQuery(sql).getResultList();
            System.out.println("Doanh thu phòng: " + result); // Debug
            return result;
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy doanh thu phòng: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Trả về danh sách rỗng nếu có lỗi
        } finally {
            em.close();
        }
    }

    public List<Object[]> getDoanhThuDichVuTheoNamThang() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            String sql = """
                SELECT
                    YEAR(pddv.ngay_dat_dich_vu) AS nam,
                    MONTH(pddv.ngay_dat_dich_vu) AS thang,
                    SUM(dv.don_gia * pddv.so_luong_dich_vu) AS doanh_thu_dich_vu
                FROM phieu_dat_dich_vu pddv
                JOIN dich_vu dv ON pddv.ma_pddv = dv.ma_pddv
                GROUP BY YEAR(pddv.ngay_dat_dich_vu), MONTH(pddv.ngay_dat_dich_vu)
                ORDER BY nam, thang
            """;
            List<Object[]> result = em.createNativeQuery(sql).getResultList();
            System.out.println("Doanh thu dịch vụ: " + result); // Debug
            return result;
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy doanh thu dịch vụ: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Trả về danh sách rỗng nếu có lỗi
        } finally {
            em.close();
        }
    }

    public boolean existsByMaHoaDon(String maHoaDon) {
        if (maHoaDon == null || maHoaDon.trim().isEmpty()) return false;
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<HoaDon> root = cq.from(HoaDon.class);
            cq.select(cb.count(root)).where(cb.equal(root.get("maHoaDon"), maHoaDon.trim()));
            Long count = em.createQuery(cq).getSingleResult();
            return count != null && count > 0;
        } finally {
            em.close();
        }
    }
}