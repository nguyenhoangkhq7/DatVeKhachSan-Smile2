package dao;

import dto.PhongDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import mapper.GenericMapper;
import mapper.impl.PhongMapperImpl;
import model.Phong;
import utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Phong_DAO extends GenericDAO<Phong> {
    private final GenericMapper<Phong, PhongDTO> mapper;

    public Phong_DAO() {
        super(Phong.class);
        this.mapper = new PhongMapperImpl();
    }

    // Tạo phòng
    public boolean create(PhongDTO phongDTO) {
        if (phongDTO == null || phongDTO.getMaPhong() == null || phongDTO.getMaPhong().isEmpty()) {
            return false;
        }
        return super.create(mapper.toEntity(phongDTO));
    }

    // Xóa phòng theo mã
    public boolean delete(String maPhong) {
        if (maPhong == null || maPhong.isEmpty()) return false;
        return super.delete(maPhong);
    }

    // Cập nhật phòng
    public boolean update(PhongDTO phongDTO) {
        if (phongDTO == null || phongDTO.getMaPhong() == null || phongDTO.getMaPhong().isEmpty()) {
            return false;
        }
        return super.update(mapper.toEntity(phongDTO));
    }

    // Đọc phòng theo mã
    public PhongDTO read(String maPhong) {
        if (maPhong == null || maPhong.isEmpty()) return null;
        return mapper.toDTO(super.read(maPhong));
    }

    // Lấy tất cả phòng
    public List<PhongDTO> getAllPhongDTOs() {
        return super.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // Tìm kiếm phòng theo tên gần đúng
    public List<PhongDTO> findByTenPhong(String tenPhong) {
        if (tenPhong == null || tenPhong.isEmpty()) return new ArrayList<>();
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Phong> cq = cb.createQuery(Phong.class);
            Root<Phong> root = cq.from(Phong.class);
            cq.select(root).where(cb.like(cb.lower(root.get("tenPhong")), "%" + tenPhong.toLowerCase() + "%"));
            return em.createQuery(cq).getResultList().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    // Tìm kiếm phòng theo nhiều tiêu chí (tên, tình trạng, loại phòng)
    public List<PhongDTO> findByMultipleCriteria(String tenPhong, Integer tinhTrang, String maLoaiPhong) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Phong> cq = cb.createQuery(Phong.class);
            Root<Phong> root = cq.from(Phong.class);
            List<Predicate> predicates = new ArrayList<>();

            if (tenPhong != null && !tenPhong.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("tenPhong")), "%" + tenPhong.toLowerCase() + "%"));
            }

            if (tinhTrang != null) {
                predicates.add(cb.equal(root.get("tinhTrang"), tinhTrang));
            }

            if (maLoaiPhong != null && !maLoaiPhong.isEmpty()) {
                predicates.add(cb.equal(root.get("loaiPhong").get("maLoaiPhong"), maLoaiPhong));
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
