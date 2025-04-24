package dao;

import dto.HoaDonDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
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

    // CREATE
    public boolean create1(HoaDonDTO hoaDonDTO) {
        if (hoaDonDTO == null || hoaDonDTO.getMaHD() == null || hoaDonDTO.getMaHD().isEmpty()) {
            System.err.println("Mã hóa đơn không hợp lệ");
            return false;
        }
        if (hoaDonDTO.getMaKH() == null || hoaDonDTO.getMaKH().isEmpty()) {
            System.err.println("Mã khách hàng không hợp lệ");
            return false;
        }
        return super.create1(mapper.toEntity(hoaDonDTO));
    }


    // READ (find by ID)
    public HoaDonDTO read(String maHoaDon) {
        if (maHoaDon == null || maHoaDon.isEmpty()) return null;
        return mapper.toDTO(super.read(maHoaDon));
    }

    // Get all HoaDonDTOs
    public List<HoaDonDTO> getAllHoaDonDTOs() {
        return super.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // Find by mã khách hàng
    public List<HoaDonDTO> findByMaKH(String maKH) {
        if (maKH == null || maKH.isEmpty()) return new ArrayList<>();
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<HoaDon> cq = cb.createQuery(HoaDon.class);
            Root<HoaDon> root = cq.from(HoaDon.class);
            cq.select(root).where(cb.equal(root.get("khachHang").get("maKH"), maKH));
            return em.createQuery(cq).getResultList().stream()
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
                JOIN phieu_dat_phong pdp ON hd.ma_phieu_dat_phong = pdp.ma_pdp
                JOIN chi_tiet_dat_phong ct ON  pdp.ma_pdp = ct.ma_pdp
                JOIN phong p ON ct.ma_phong = p.ma_phong
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
                JOIN chi_tiet_dat_dich_vu ct ON pddv.ma_pddv = ct.ma_pddv
                JOIN dich_vu dv ON ct.ma_dv = dv.ma_dv
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

    // Tìm hóa đơn theo nhiều tiêu chí: mã KH, mã NV, số phòng đặt
    public List<HoaDonDTO> findByMultipleCriteria(String maKH, String maNV, Integer soPhongDat) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<HoaDon> cq = cb.createQuery(HoaDon.class);
            Root<HoaDon> root = cq.from(HoaDon.class);
            List<Predicate> predicates = new ArrayList<>();

            if (maKH != null && !maKH.isEmpty()) {
                predicates.add(cb.equal(root.get("khachHang").get("maKH"), maKH));
            }

            if (maNV != null && !maNV.isEmpty()) {
                predicates.add(cb.equal(root.get("nhanVien").get("maNhanVien"), maNV));
            }

            if (soPhongDat != null) {
                predicates.add(cb.equal(root.get("soPhongDat"), soPhongDat));
            }

            cq.select(root).where(predicates.toArray(new Predicate[0]));
            return em.createQuery(cq).getResultList().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
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
