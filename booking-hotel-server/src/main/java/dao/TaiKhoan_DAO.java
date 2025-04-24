package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import model.TaiKhoan;
import utils.HibernateUtil;

public class TaiKhoan_DAO extends GenericDAO<TaiKhoan> {

    public TaiKhoan_DAO() {
        super(TaiKhoan.class);
    }

    public TaiKhoan findByUsernameAndPassword(String tenDN, String matKhau) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery("SELECT tk FROM TaiKhoan tk WHERE tk.tenDN = :tenDN AND tk.matKhau = :matKhau", TaiKhoan.class)
                    .setParameter("tenDN", tenDN)
                    .setParameter("matKhau", matKhau)
                    .getSingleResult();
        } catch (NoResultException e) {
            System.out.println("Không tìm thấy tài khoản với tenDN=" + tenDN);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }
}