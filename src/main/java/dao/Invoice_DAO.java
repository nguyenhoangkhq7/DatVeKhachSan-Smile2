package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.Invoice;
import java.util.List;

public class Invoice_DAO {

    private EntityManager em;

    public Invoice_DAO(EntityManager em) {
        this.em = em;
    }

    // Phương thức lưu hóa đơn mới vào cơ sở dữ liệu
    public boolean save(Invoice invoice) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(invoice);
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

    // Phương thức tìm hóa đơn theo mã hóa đơn
    public Invoice findByInvoiceCode(String maHD) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        try {
            return em.find(Invoice.class, maHD);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Phương thức cập nhật thông tin hóa đơn
    public boolean update(Invoice invoice) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.merge(invoice);
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

    // Phương thức xóa hóa đơn theo mã hóa đơn
    public boolean delete(String maHD) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            Invoice invoice = em.find(Invoice.class, maHD);
            if (invoice != null) {
                em.remove(invoice);
                tr.commit();
                return true;
            } else {
                System.out.println("Invoice not found with invoice code: " + maHD);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tr.isActive()) {
                tr.rollback();
            }
        }
        return false;
    }

    // Phương thức tìm tất cả các hóa đơn
    public List<Invoice> findAll() {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        try {
            return em.createQuery("SELECT i FROM Invoice i", Invoice.class).getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
