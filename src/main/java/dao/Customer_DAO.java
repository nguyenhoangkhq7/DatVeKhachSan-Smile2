package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.Customer;

public class Customer_DAO {
    private EntityManager em;
    public boolean save (Customer customer) {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(customer);
            tr.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
        }
        return false;
    }

    public boolean update (Customer customer) {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.merge(customer);
            tr.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
        }
        return false;
    }

    public boolean delete (String id) {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            Customer cus = em.find(Customer.class, id);
            em.remove(cus);
            tr.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
        }
        return false;
    }

}


