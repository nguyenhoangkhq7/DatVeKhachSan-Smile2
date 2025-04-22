package dao;

import dto.LoaiPhongDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import mapper.GenericMapper;
import mapper.impl.LoaiPhongMapperImpl;
import model.LoaiPhong;
import utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LoaiPhong_DAO extends GenericDAO<LoaiPhong> {
    private final GenericMapper<LoaiPhong, LoaiPhongDTO> mapper;

    public LoaiPhong_DAO() {
        super(LoaiPhong.class);
        this.mapper = new LoaiPhongMapperImpl();
    }

    // Tạo loại phòng
    public boolean create(LoaiPhongDTO loaiPhongDTO) {
        if (loaiPhongDTO == null || loaiPhongDTO.getMaLoai() == null || loaiPhongDTO.getMaLoai().isEmpty()) {
            return false;
        }
        return super.create(mapper.toEntity(loaiPhongDTO));
    }

    // Xóa loại phòng theo mã
    public boolean delete(String maLoaiPhong) {
        if (maLoaiPhong == null || maLoaiPhong.isEmpty()) return false;
        return super.delete(maLoaiPhong);
    }

    // Cập nhật loại phòng
    public boolean update(LoaiPhongDTO loaiPhongDTO) {
        if (loaiPhongDTO == null || loaiPhongDTO.getMaLoai() == null || loaiPhongDTO.getMaLoai().isEmpty()) {
            return false;
        }
        return super.update(mapper.toEntity(loaiPhongDTO));
    }

    // Đọc loại phòng theo mã
    public LoaiPhongDTO read(String maLoaiPhong) {
        if (maLoaiPhong == null || maLoaiPhong.isEmpty()) return null;
        return mapper.toDTO(super.read(maLoaiPhong));
    }

    // Lấy tất cả loại phòng
    public List<LoaiPhongDTO> getAllLoaiPhongDTOs() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<LoaiPhong> cq = cb.createQuery(LoaiPhong.class);
            Root<LoaiPhong> root = cq.from(LoaiPhong.class);
            cq.select(root);
            List<LoaiPhong> result = em.createQuery(cq).getResultList();
            return result.stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    // Tìm loại phòng theo tên gần đúng
    public List<LoaiPhongDTO> findByTenLoaiPhong(String tenLoai) {
        if (tenLoai == null || tenLoai.isEmpty()) return new ArrayList<>();
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<LoaiPhong> cq = cb.createQuery(LoaiPhong.class);
            Root<LoaiPhong> root = cq.from(LoaiPhong.class);
            Predicate tenLike = cb.like(cb.lower(root.get("tenLoai")), "%" + tenLoai.toLowerCase() + "%");
            cq.select(root).where(tenLike);
            return em.createQuery(cq).getResultList()
                    .stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }
}