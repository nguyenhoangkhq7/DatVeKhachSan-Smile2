package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
