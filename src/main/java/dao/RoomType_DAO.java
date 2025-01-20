package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.RoomType;
import java.util.List;

public class RoomType_DAO {

    private EntityManager em;

    public RoomType_DAO(EntityManager em) {
        this.em = em;
    }

    // Phương thức lưu loại phòng mới vào cơ sở dữ liệu
    public boolean save(RoomType roomType) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(roomType);
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

    // Phương thức tìm loại phòng theo mã loại phòng
    public RoomType findByRoomTypeCode(String roomTypeCode) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        try {
            return em.find(RoomType.class, roomTypeCode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Phương thức cập nhật thông tin loại phòng
    public boolean update(RoomType roomType) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.merge(roomType);
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

    // Phương thức xóa loại phòng theo mã loại phòng
    public boolean delete(String roomTypeCode) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            RoomType roomType = em.find(RoomType.class, roomTypeCode);
            if (roomType != null) {
                em.remove(roomType);
                tr.commit();
                return true;
            } else {
                System.out.println("RoomType not found with code: " + roomTypeCode);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tr.isActive()) {
                tr.rollback();
            }
        }
        return false;
    }

    // Phương thức tìm tất cả các loại phòng
    public List<RoomType> findAll() {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        try {
            return em.createQuery("SELECT rt FROM RoomType rt", RoomType.class).getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
