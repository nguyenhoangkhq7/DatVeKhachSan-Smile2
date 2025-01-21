/*
    *DatVeKhachSan-Smile2  day creative: 1/21/2025
    version: 2023.2  IntelliJ IDEA
    author: Nguyễn Hoàng Khang  */
    /*
       *class description:
            write description right here   
     */


package dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;

public class GenericCRUD_DAO<T> {

    private EntityManager em;
    private Class<T> entityType;

    public GenericCRUD_DAO(EntityManager em, Class<T> entityType) {
        this.em = em;
        this.entityType = entityType;
    }

    public List<T> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityType);
        cq.from(entityType);
        return em.createQuery(cq).getResultList();
    }


    public boolean create (T entity) {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(entity);
            tr.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
        }
        return false;
    }

    public T read () {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            T entity = em.find(entityType, 1);
            tr.commit();
            return entity;
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
        }
        return null;
    }

    public boolean update (T entity) {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.merge(entity);
            tr.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
        }
        return false;
    }

    public boolean delete (Object id) {
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
            tr.rollback();
        }
        return false;
    }

}

