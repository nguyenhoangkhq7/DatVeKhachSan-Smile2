package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.Room;
import java.util.List;

public class Room_DAO {

    private EntityManager em;

    public Room_DAO(EntityManager em) {
        this.em = em;
    }

    // Phương thức lưu phòng mới vào cơ sở dữ liệu
    public boolean save(Room room) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(room);
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

    // Phương thức tìm phòng theo mã phòng
    public Room findByRoomCode(String roomCode) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        try {
            return em.find(Room.class, roomCode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Phương thức cập nhật thông tin phòng
    public boolean update(Room room) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.merge(room);
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

    // Phương thức xóa phòng theo mã phòng
    public boolean delete(String roomCode) {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            Room room = em.find(Room.class, roomCode);
            if (room != null) {
                em.remove(room);
                tr.commit();
                return true;
            } else {
                System.out.println("Room not found with room code: " + roomCode);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (tr.isActive()) {
                tr.rollback();
            }
        }
        return false;
    }

    // Phương thức tìm tất cả các phòng
    public List<Room> findAll() {
        if (em == null) {
            throw new IllegalStateException("EntityManager is not initialized!");
        }

        try {
            return em.createQuery("SELECT r FROM Room r", Room.class).getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
