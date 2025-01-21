package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.ServiceOrder;
import java.util.List;

public class ServiceOrder_DAO {

    private EntityManager em;

    public ServiceOrder_DAO(EntityManager em) {
        this.em = em;
    }

    // Phương thức lưu đơn đặt dịch vụ mới vào cơ sở dữ liệu
    public boolean save(ServiceOrder serviceOrder) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(serviceOrder);
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

    // Phương thức tìm đơn đặt dịch vụ theo mã đơn đặt dịch vụ
    public ServiceOrder findByServiceOrderCode(String maPDDV) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        try {
            return em.find(ServiceOrder.class, maPDDV);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Phương thức cập nhật thông tin đơn đặt dịch vụ
    public boolean update(ServiceOrder serviceOrder) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.merge(serviceOrder);
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

    // Phương thức xóa đơn đặt dịch vụ theo mã đơn đặt dịch vụ
    public boolean delete(String maPDDV) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            ServiceOrder serviceOrder = em.find(ServiceOrder.class, maPDDV);
            if (serviceOrder != null) {
                em.remove(serviceOrder);
                tr.commit();
                return true;
            } else {
                System.out.println("ServiceOrder not found with service order code: " + maPDDV);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tr.isActive()) {
                tr.rollback();
            }
        }
        return false;
    }

    // Phương thức tìm tất cả các đơn đặt dịch vụ
    public List<ServiceOrder> findAll() {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        try {
            return em.createQuery("SELECT s FROM ServiceOrder s", ServiceOrder.class).getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
