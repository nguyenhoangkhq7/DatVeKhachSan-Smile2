package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.Employee;

public class Employee_DAO {

    private EntityManager em;

    public Employee_DAO(EntityManager em) {
        this.em = em;
    }

    public boolean save(Employee employee) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(employee);
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

    public Employee findById(String maNhanVien) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        try {
            return em.find(Employee.class, maNhanVien);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean update(Employee employee) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.merge(employee);
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

    public boolean delete(String maNhanVien) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            Employee employee = em.find(Employee.class, maNhanVien);
            if (employee != null) {
                em.remove(employee);
                tr.commit();
                return true;
            } else {
                System.out.println("Employee not found with ID: " + maNhanVien);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tr.isActive()) {
                tr.rollback();
            }
        }
        return false;
    }
}
