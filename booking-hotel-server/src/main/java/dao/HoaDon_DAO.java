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
    public boolean create(HoaDonDTO hoaDonDTO) {
        if (hoaDonDTO == null || hoaDonDTO.getMaHD() == null || hoaDonDTO.getMaHD().isEmpty()) {
            return false;
        }
        return super.create(mapper.toEntity(hoaDonDTO));
    }

//    // DELETE
//    public boolean delete(String maHoaDon) {
//        if (maHoaDon == null || maHoaDon.isEmpty()) return false;
//        return super.delete(maHoaDon);
//    }
//
//    // UPDATE
//    public boolean update(HoaDonDTO hoaDonDTO) {
//        if (hoaDonDTO == null || hoaDonDTO.getMaHD() == null || hoaDonDTO.getMaHD().isEmpty()) {
//            return false;
//        }
//        return super.update(mapper.toEntity(hoaDonDTO));
//    }

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
}
