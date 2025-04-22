
package dao;


import dto.PhieuDatPhongDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import mapper.GenericMapper;
import mapper.impl.PhieuDatPhongMapperImpl;
import model.PhieuDatPhong;
import utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PhieuDatPhong_DAO extends GenericDAO<PhieuDatPhong> {
    private final GenericMapper<PhieuDatPhong, PhieuDatPhongDTO> mapper;

    public PhieuDatPhong_DAO() {
        super(PhieuDatPhong.class);
        this.mapper = new PhieuDatPhongMapperImpl();
    }

    // CREATE
    public boolean create(PhieuDatPhongDTO dto) {
        if (dto == null || dto.getMaPDP() == null || dto.getMaPDP().isEmpty()) {
            return false;
        }
        return super.create(mapper.toEntity(dto));
    }

    // READ by ID
    public PhieuDatPhongDTO read(String maPDP) {
        if (maPDP == null || maPDP.isEmpty()) return null;
        return mapper.toDTO(super.read(maPDP));
    }

    // UPDATE
    public boolean update(PhieuDatPhongDTO dto) {
        if (dto == null || dto.getMaPDP() == null || dto.getMaPDP().isEmpty()) {
            return false;
        }
        return super.update(mapper.toEntity(dto));
    }

    // DELETE
    public boolean delete(String maPDP) {
        if (maPDP == null || maPDP.isEmpty()) return false;
        return super.delete(maPDP);
    }

    // Get all
    public List<PhieuDatPhongDTO> getAllPhieuDatPhongDTOs() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<PhieuDatPhong> cq = cb.createQuery(PhieuDatPhong.class);
            Root<PhieuDatPhong> root = cq.from(PhieuDatPhong.class);
            root.fetch("dsPhong", JoinType.LEFT); // Eager fetch dsPhong

            cq.select(root);

            List<PhieuDatPhong> resultList = em.createQuery(cq).getResultList();
            return resultList.stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());

        } finally {
            em.close();
        }
    }

    // Find by maKH
    public List<PhieuDatPhongDTO> findByMaKH(String maKH) {
        if (maKH == null || maKH.isEmpty()) return new ArrayList<>();
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<PhieuDatPhong> cq = cb.createQuery(PhieuDatPhong.class);
            Root<PhieuDatPhong> root = cq.from(PhieuDatPhong.class);
            cq.select(root).where(cb.equal(root.get("khachHang").get("maKH"), maKH));
            return em.createQuery(cq).getResultList().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    // Find by multiple criteria
    public List<PhieuDatPhongDTO> findByMultipleCriteria(String maKH, String maNV, Integer soPhongDat) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<PhieuDatPhong> cq = cb.createQuery(PhieuDatPhong.class);
            Root<PhieuDatPhong> root = cq.from(PhieuDatPhong.class);
            List<Predicate> predicates = new ArrayList<>();

            if (maKH != null && !maKH.isEmpty()) {
                predicates.add(cb.equal(root.get("khachHang").get("maKH"), maKH));
            }

            if (maNV != null && !maNV.isEmpty()) {
                predicates.add(cb.equal(root.get("nhanVien").get("maNhanVien"), maNV));
            }

            if (soPhongDat != null) {
                predicates.add(cb.equal(cb.size(root.get("dsPhong")), soPhongDat));
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
