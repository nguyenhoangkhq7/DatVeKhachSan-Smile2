package dao;

import com.google.gson.reflect.TypeToken;
import dto.PhieuDatPhongDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import mapper.GenericMapper;
import mapper.impl.PhieuDatPhongMapperImpl;
import model.PhieuDatPhong;
import model.Request;
import model.Response;
import org.hibernate.Hibernate;
import socket.SocketManager;
import utils.HibernateUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PhieuDatPhong_DAO extends GenericDAO<PhieuDatPhong> {
    public final GenericMapper<PhieuDatPhong, PhieuDatPhongDTO> mapper;

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
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<PhieuDatPhong> cq = cb.createQuery(PhieuDatPhong.class);
            Root<PhieuDatPhong> root = cq.from(PhieuDatPhong.class);

            // Fetch các liên kết cần thiết - EAGER loading
            root.fetch("phongs", JoinType.LEFT);
            root.fetch("khachHang", JoinType.LEFT);
            root.fetch("nhanVien", JoinType.LEFT);
            root.fetch("phieuDatDichVus", JoinType.LEFT);
            root.fetch("hoaDon", JoinType.LEFT);

            cq.select(root).where(cb.equal(root.get("maPDP"), maPDP));
            PhieuDatPhong entity = em.createQuery(cq).getSingleResult();
            Hibernate.initialize(entity);
            // Ép buộc khởi tạo collections trước khi đóng session
//            if (entity.getPhongs() != null) {
//                entity.getPhongs().size();
//            }


            return mapper.toDTO(entity);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi đọc PhieuDatPhong với maPDP=" + maPDP + ": " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
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

            // Fetch các liên kết cần thiết
            root.fetch("phongs", JoinType.LEFT);
            root.fetch("khachHang", JoinType.LEFT);
            root.fetch("nhanVien", JoinType.LEFT);
            root.fetch("phieuDatDichVus", JoinType.LEFT);

            cq.select(root).distinct(true);

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
                predicates.add(cb.equal(cb.size(root.get("phongs")), soPhongDat)); // Sửa dsPhong thành phongs
            }

            cq.select(root).where(predicates.toArray(new Predicate[0]));
            return em.createQuery(cq).getResultList().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }
    public boolean create3(PhieuDatPhongDTO dto) {
        if (dto == null || dto.getMaPDP() == null || dto.getMaPDP().isEmpty()) {
            System.out.println("Lỗi: Mã phiếu đặt phòng không hợp lệ");
            return false;
        }
        try {
            PhieuDatPhong entity = mapper.toEntity(dto); // Sửa toEntityDTO thành toEntity
            System.out.println("Entity trước khi lưu: " + entity);
            return super.create(entity); // Đảm bảo GenericDAO có phương thức create3
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm phiếu đặt phòng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean existsByMaPDP(String maPDP) {
        if (maPDP == null || maPDP.isEmpty()) {
            return false;
        }
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            em.createQuery("SELECT p FROM PhieuDatPhong p WHERE p.maPDP = :maPDP", PhieuDatPhong.class)
                    .setParameter("maPDP", maPDP)
                    .getSingleResult();
            return true; // Nếu tìm thấy, trả về true
        } catch (NoResultException e) {
            return false; // Nếu không tìm thấy, trả về false
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
}