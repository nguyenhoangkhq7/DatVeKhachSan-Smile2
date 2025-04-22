package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import model.HoaDon;
import model.KhachHang;
import model.NhanVien;
import model.PhieuDatPhong;
import utils.HibernateUtil; // bạn cần import đúng đường dẫn tới HibernateUtil

import java.util.List;
import java.util.function.Predicate;


public class GenericDAO<T> {

    private final Class<T> entityType;

    public GenericDAO(Class<T> entityType) {
        this.entityType = entityType;
    }

    public List<T> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityType);
            cq.from(entityType);
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }
    public boolean create1(T entity) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            if (entity instanceof HoaDon hoaDon) {
                // Xử lý KhachHang
                if (hoaDon.getKhachHang() != null && hoaDon.getKhachHang().getMaKH() != null) {
                    KhachHang existingKhachHang = em.find(KhachHang.class, hoaDon.getKhachHang().getMaKH());
                    if (existingKhachHang == null) {
                        throw new IllegalArgumentException("Khách hàng với mã " + hoaDon.getKhachHang().getMaKH() + " không tồn tại. Vui lòng tạo khách hàng trước.");
                    }
                    hoaDon.setKhachHang(existingKhachHang);
                } else {
                    throw new IllegalArgumentException("Mã khách hàng không hợp lệ");
                }

                // Xử lý NhanVien
                if (hoaDon.getNhanVien() != null && hoaDon.getNhanVien().getMaNhanVien() != null) {
                    NhanVien existingNhanVien = em.find(NhanVien.class, hoaDon.getNhanVien().getMaNhanVien());
                    if (existingNhanVien == null) {
                        throw new IllegalArgumentException("Nhân viên với mã " + hoaDon.getNhanVien().getMaNhanVien() + " không tồn tại. Vui lòng tạo nhân viên trước.");
                    }
                    hoaDon.setNhanVien(existingNhanVien);
                }

                // Xử lý PhieuDatPhong
                if (hoaDon.getPhieuDatPhong() != null && hoaDon.getPhieuDatPhong().getMaPDP() != null) {
                    PhieuDatPhong existingPhieuDatPhong = em.find(PhieuDatPhong.class, hoaDon.getPhieuDatPhong().getMaPDP());
                    if (existingPhieuDatPhong == null) {
                        throw new IllegalArgumentException("Phiếu đặt phòng với mã " + hoaDon.getPhieuDatPhong().getMaPDP() + " không tồn tại. Vui lòng tạo phiếu đặt phòng trước.");
                    }
                    hoaDon.setPhieuDatPhong(existingPhieuDatPhong);
                }
            }
            em.persist(entity);
            tr.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tr.isActive()) tr.rollback();
        } finally {
            em.close();
        }
        return false;
    }

    public boolean create(T entity) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(entity);
            tr.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tr.isActive()) tr.rollback();
        } finally {
            em.close();
        }
        return false;
    }

    public T read(Object id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.find(entityType, id);
        } finally {
            em.close();
        }
    }

    public boolean update(T entity) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.merge(entity);
            tr.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tr.isActive()) tr.rollback();
        } finally {
            em.close();
        }
        return false;
    }

    public boolean delete(Object id) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            T entity = em.find(entityType, id);
            if (entity != null) {
                em.remove(entity);
                tr.commit();
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tr.isActive()) tr.rollback();
        } finally {
            em.close();
        }
        return false;
    }

    public List<T> findByCondition(Predicate<T> predicate) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityType);
            Root<T> root = cq.from(entityType);
            cq.select(root);
            List<T> list = em.createQuery(cq).getResultList();
            return list.stream().filter(predicate).toList();
        } finally {
            em.close();
        }
    }

}