package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.Promotion;
import java.util.List;

public class Promotion_DAO {

    private EntityManager em;

    public Promotion_DAO(EntityManager em) {
        this.em = em;
    }

    // Phương thức lưu khuyến mãi mới vào cơ sở dữ liệu
    public boolean save(Promotion promotion) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(promotion);
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

    // Phương thức tìm khuyến mãi theo mã khuyến mãi
    public Promotion findByPromotionCode(String promotionCode) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        try {
            return em.find(Promotion.class, promotionCode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Phương thức cập nhật thông tin khuyến mãi
    public boolean update(Promotion promotion) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.merge(promotion);
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

    // Phương thức xóa khuyến mãi theo mã khuyến mãi
    public boolean delete(String promotionCode) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            Promotion promotion = em.find(Promotion.class, promotionCode);
            if (promotion != null) {
                em.remove(promotion);
                tr.commit();
                return true;
            } else {
                System.out.println("Promotion not found with code: " + promotionCode);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tr.isActive()) {
                tr.rollback();
            }
        }
        return false;
    }

    // Phương thức tìm tất cả các khuyến mãi
    public List<Promotion> findAll() {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        try {
            return em.createQuery("SELECT p FROM Promotion p", Promotion.class).getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
