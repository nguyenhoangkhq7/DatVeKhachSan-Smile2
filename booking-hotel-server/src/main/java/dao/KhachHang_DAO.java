package dao;

import dto.KhachHangDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import mapper.GenericMapper;
import mapper.impl.KhachHangMapperImpl;
import model.KhachHang;
import utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KhachHang_DAO extends GenericDAO<KhachHang> {
    private final GenericMapper<KhachHang, KhachHangDTO> mapper;

    public KhachHang_DAO() {
        super(KhachHang.class);
        this.mapper = new KhachHangMapperImpl();
    }

    // Tạo mới khách hàng
    public boolean create(KhachHangDTO khachHangDTO) {
        if (khachHangDTO == null || khachHangDTO.getMaKH() == null || khachHangDTO.getMaKH().isEmpty()) {
            return false;
        }
        return super.create(mapper.toEntity(khachHangDTO));
    }

    // Xóa khách hàng theo mã
    public boolean delete(String maKH) {
        if (maKH == null || maKH.isEmpty()) return false;
        return super.delete(maKH);
    }

    // Cập nhật thông tin khách hàng
    public boolean update(KhachHangDTO khachHangDTO) {
        if (khachHangDTO == null || khachHangDTO.getMaKH() == null || khachHangDTO.getMaKH().isEmpty()) {
            return false;
        }
        return super.update(mapper.toEntity(khachHangDTO));
    }

    // Đọc thông tin khách hàng theo mã
    public KhachHangDTO read(String maKH) {
        if (maKH == null || maKH.isEmpty()) return null;
        return mapper.toDTO(super.read(maKH));
    }

    // Lấy tất cả khách hàng
    public List<KhachHangDTO> getAllKhachHangDTOs() {
        return super.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // Tìm kiếm khách hàng theo tên gần đúng
    public List<KhachHangDTO> findByHoTen(String hoTen) {
        if (hoTen == null || hoTen.isEmpty()) return new ArrayList<>();
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<KhachHang> cq = cb.createQuery(KhachHang.class);
            Root<KhachHang> root = cq.from(KhachHang.class);
            cq.select(root).where(cb.like(cb.lower(root.get("hoTen")), "%" + hoTen.toLowerCase() + "%"));
            return em.createQuery(cq).getResultList().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    // Tìm kiếm khách hàng theo CCCD
    public List<KhachHangDTO> findBySoCCCD(String soCCCD) {
        if (soCCCD == null || soCCCD.isEmpty()) return new ArrayList<>();
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<KhachHang> cq = cb.createQuery(KhachHang.class);
            Root<KhachHang> root = cq.from(KhachHang.class);
            cq.select(root).where(cb.equal(root.get("soCCCD"), soCCCD));
            return em.createQuery(cq).getResultList().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    public List<KhachHangDTO> findByKeyword(String keyword) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<KhachHang> cq = cb.createQuery(KhachHang.class);
            Root<KhachHang> root = cq.from(KhachHang.class);

            if (keyword != null) keyword = keyword.trim().toLowerCase();

            Predicate nameLike = cb.like(cb.lower(root.get("hoTen")), "%" + keyword + "%");
            Predicate emailLike = cb.like(cb.lower(root.get("email")), "%" + keyword + "%");
            Predicate phoneLike = cb.like(cb.lower(root.get("soDienThoai")), "%" + keyword + "%");

            cq.select(root).where(cb.or(nameLike, emailLike, phoneLike));

            return em.createQuery(cq).getResultList().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }
}