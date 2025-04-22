package dao;

import dto.PhieuDatDichVuDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import mapper.GenericMapper;
import mapper.impl.PhieuDatDichVuMapperImpl;
import model.PhieuDatDichVu;
import utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PhieuDatDichVu_DAO extends GenericDAO<PhieuDatDichVu> {
    private final GenericMapper<PhieuDatDichVu, PhieuDatDichVuDTO> mapper;

    public PhieuDatDichVu_DAO() {
        super(PhieuDatDichVu.class);
        this.mapper = new PhieuDatDichVuMapperImpl();
    }

    // CREATE
    public boolean create1(PhieuDatDichVuDTO phieuDatDichVuDTO) {
        if (phieuDatDichVuDTO == null || phieuDatDichVuDTO.getMaPDDV() == null || phieuDatDichVuDTO.getMaPDDV().isEmpty()) {
            System.err.println("Mã phiếu đặt dịch vụ không hợp lệ");
            return false;
        }
        if (phieuDatDichVuDTO.getMaKH() == null || phieuDatDichVuDTO.getMaKH().isEmpty()) {
            System.err.println("Mã khách hàng không hợp lệ");
            return false;
        }
        return super.create1(mapper.toEntity(phieuDatDichVuDTO));
    }

    // READ (find by ID)
    public PhieuDatDichVuDTO read(String maPDDV) {
        if (maPDDV == null || maPDDV.isEmpty()) return null;
        return mapper.toDTO(super.read(maPDDV));
    }

    // Get all PhieuDatDichVuDTOs
    public List<PhieuDatDichVuDTO> getAllPhieuDatDichVuDTOs() {
        return super.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // Find by mã khách hàng
    public List<PhieuDatDichVuDTO> findByMaKH(String maKH) {
        if (maKH == null || maKH.isEmpty()) return new ArrayList<>();
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<PhieuDatDichVu> cq = cb.createQuery(PhieuDatDichVu.class);
            Root<PhieuDatDichVu> root = cq.from(PhieuDatDichVu.class);
            cq.select(root).where(cb.equal(root.get("khachHang").get("maKH"), maKH));
            return em.createQuery(cq).getResultList().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    // Tìm phiếu đặt dịch vụ theo nhiều tiêu chí: mã KH, mã NV, số lượng dịch vụ
    public List<PhieuDatDichVuDTO> findByMultipleCriteria(String maKH, String maNV, Integer soLuongDichVu) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<PhieuDatDichVu> cq = cb.createQuery(PhieuDatDichVu.class);
            Root<PhieuDatDichVu> root = cq.from(PhieuDatDichVu.class);
            List<Predicate> predicates = new ArrayList<>();

            if (maKH != null && !maKH.isEmpty()) {
                predicates.add(cb.equal(root.get("khachHang").get("maKH"), maKH));
            }

            if (maNV != null && !maNV.isEmpty()) {
                predicates.add(cb.equal(root.get("nhanVien").get("maNhanVien"), maNV));
            }

            if (soLuongDichVu != null) {
                predicates.add(cb.equal(root.get("soLuongDichVu"), soLuongDichVu));
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