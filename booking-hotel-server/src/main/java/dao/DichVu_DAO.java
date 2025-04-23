package dao;


import dto.DichVuDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import mapper.GenericMapper;
import mapper.impl.DichVuMapperImpl;
import model.DichVu;
import utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DichVu_DAO extends GenericDAO<DichVu> {

    private final GenericMapper<DichVu, DichVuDTO> mapper;

    public DichVu_DAO() {
        super(DichVu.class);
        this.mapper = new DichVuMapperImpl();
    }

    // CRUD
    public boolean create(DichVuDTO dto) {
        if (dto == null || dto.getMaDV() == null || dto.getMaDV().isEmpty()) {
            return false;
        }
        return super.create(mapper.toEntity(dto));
    }

    public boolean update(DichVuDTO dto) {
        if (dto == null || dto.getMaDV() == null || dto.getMaDV().isEmpty()) {
            return false;
        }
        return super.update(mapper.toEntity(dto));
    }

    public boolean delete(DichVuDTO dto) {
        if (dto == null || dto.getMaDV() == null || dto.getMaDV().isEmpty()) {
            return false;
        }
        return super.delete(mapper.toEntity(dto).getMaDV());
    }

    public DichVuDTO read(String maDV) {
        if (maDV == null || maDV.isEmpty()) {
            return null;
        }
        return mapper.toDTO(super.read(maDV));
    }

    public List<DichVuDTO> findAllDTOs() {
        return super.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<DichVuDTO> findByTenDV(String tenDV) {
        if (tenDV == null || tenDV.isEmpty()) return new ArrayList<>();
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<DichVu> cq = cb.createQuery(DichVu.class);
            Root<DichVu> root = cq.from(DichVu.class);
            cq.select(root).where(cb.like(cb.lower(root.get("tenDV")), "%" + tenDV.toLowerCase() + "%"));
            return em.createQuery(cq).getResultList().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    public List<DichVuDTO> findByMultipleCriteria(String tenDV, Double minGia, Double maxGia, String donViTinh) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<DichVu> cq = cb.createQuery(DichVu.class);
            Root<DichVu> root = cq.from(DichVu.class);
            List<Predicate> predicates = new ArrayList<>();

            if (tenDV != null && !tenDV.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("tenDV")), "%" + tenDV.toLowerCase() + "%"));
            }

            if (minGia != null && maxGia != null) {
                predicates.add(cb.between(root.get("donGia"), minGia, maxGia));
            } else if (minGia != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("donGia"), minGia));
            } else if (maxGia != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("donGia"), maxGia));
            }

            if (donViTinh != null && !donViTinh.isEmpty()) {
                predicates.add(cb.equal(root.get("donViTinh"), donViTinh));
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