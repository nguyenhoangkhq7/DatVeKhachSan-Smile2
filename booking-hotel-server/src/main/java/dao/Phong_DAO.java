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

    public List<PhongDTO> getAllPhongDTOs() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Phong> cq = cb.createQuery(Phong.class);
            Root<Phong> root = cq.from(Phong.class);
            root.fetch("loaiPhong"); // JOIN với bảng LoaiPhong

            cq.select(root);

            List<Phong> resultList = em.createQuery(cq).getResultList();
            return resultList.stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());

        } finally {
            em.close();
        }
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

    public List<PhongDTO> findByKeyword(String keyword) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Phong> cq = cb.createQuery(Phong.class);
            Root<Phong> root = cq.from(Phong.class);

            if (keyword != null) keyword = keyword.trim().toLowerCase();

            List<Predicate> predicates = new ArrayList<>();

            // Tìm theo chuỗi (String)
            predicates.add(cb.like(cb.lower(root.get("tenPhong")), "%" + keyword + "%"));
            predicates.add(cb.like(cb.lower(root.get("loaiPhong").get("tenLoai")), "%" + keyword + "%"));
            predicates.add(cb.like(cb.lower(root.get("moTa")), "%" + keyword + "%"));

            // Tìm theo số nếu có thể parse được
            try {
                double gia = Double.parseDouble(keyword);
                predicates.add(cb.equal(root.get("giaPhong"), gia));
            } catch (NumberFormatException ignored) {}

            try {
                int so = Integer.parseInt(keyword);
                predicates.add(cb.equal(root.get("soNguoi"), so));
                predicates.add(cb.equal(root.get("tinhTrang"), so));
            } catch (NumberFormatException ignored) {}

            // Ánh xạ keyword dạng chữ cho tình trạng
            if ("Còn trống".equalsIgnoreCase(keyword)) {
                predicates.add(cb.equal(root.get("tinhTrang"), 0));
            } else if ("Đang sử dụng".equalsIgnoreCase(keyword)) {
                predicates.add(cb.equal(root.get("tinhTrang"), 1));
            }

            cq.select(root).where(cb.or(predicates.toArray(new Predicate[0])));

            List<Phong> resultList = em.createQuery(cq).getResultList();
            return resultList.stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());

        } finally {
            em.close();
        }
    }


    // Kiểm tra mã phòng đã tồn tại hay chưa
    public boolean existsByMaPhong(String maPhong) {
        if (maPhong == null || maPhong.trim().isEmpty()) return false;
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Phong> root = cq.from(Phong.class);

            cq.select(cb.count(root)).where(cb.equal(root.get("maPhong"), maPhong.trim()));
            Long count = em.createQuery(cq).getSingleResult();
            return count != null && count > 0;
        } finally {
            em.close();
        }
    }


}
