package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.Service;
import java.util.List;

public class Service_DAO {

    private EntityManager em;

    public Service_DAO(EntityManager em) {
        this.em = em;
    }

    // Phương thức lưu dịch vụ mới vào cơ sở dữ liệu
    public boolean save(Service service) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(service);
            tr.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tr.isActive()) {
                tr.rollback();
            }
        }
        return false;
    }

    // Phương thức tìm dịch vụ theo mã dịch vụ
    public Service findByServiceCode(String serviceCode) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        try {
            return em.find(Service.class, serviceCode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Phương thức cập nhật thông tin dịch vụ
    public boolean update(Service service) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.merge(service);
            tr.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tr.isActive()) {
                tr.rollback();
            }
        }
        return false;
    }

    // Phương thức xóa dịch vụ theo mã dịch vụ
    public boolean delete(String serviceCode) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            Service service = em.find(Service.class, serviceCode);
            if (service != null) {
                em.remove(service);
                tr.commit();
                return true;
            } else {
                System.out.println("Service not found with code: " + serviceCode);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tr.isActive()) {
                tr.rollback();
            }
        }
        return false;
    }

    // Phương thức tìm tất cả các dịch vụ
    public List<Service> findAll() {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        try {
            return em.createQuery("SELECT s FROM Service s", Service.class).getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
