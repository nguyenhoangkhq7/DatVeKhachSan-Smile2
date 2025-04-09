package dao;


import connectDB.ConnectDB;
import entity.Phong;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.util.Date;

public class Phong_DAO {
    private ArrayList<Phong> dsPhong;
    public Phong_DAO() {
        dsPhong = new ArrayList<>();
    }
    public ArrayList<Phong> getDSPhong() {
        try{
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "select * from Phong";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                String maPhong = rs.getString("maPhong");
                String tenPhong = rs.getString("tenPhong");
                String loaiPhong = rs.getString("loaiPhong");
                double giaPhong = rs.getDouble("giaPhong");
                int trangThai = rs.getInt("tinhTrang");
                int soNguoi = rs.getInt("soNguoi");
                String moTa = rs.getString("moTa");
                Phong p= new Phong(maPhong, tenPhong, giaPhong, loaiPhong, trangThai, moTa, soNguoi);
                dsPhong.add(p);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return dsPhong;
    }

    public ArrayList<String> getSoPhongByLoaiPhong(String loaiPhong) {
        ArrayList<String> dsSoPhong = new ArrayList<>();
        String query = "SELECT maPhong FROM Phong WHERE loaiPhong = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, loaiPhong);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                dsSoPhong.add(rs.getString("maPhong"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dsSoPhong;
    }

    public String getTenPhongBySoPhong(String soPhong) {
        String query = "SELECT tenPhong FROM Phong WHERE maPhong = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, soPhong);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                return rs.getString("tenPhong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    public boolean createPhong(Phong p){
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement ps = null;
        int n = 0;
        try{
            ps = con.prepareStatement("insert into Phong values(?,?,?,?,?,?,?)");
            ps.setString(1, p.getMaPhong());
            ps.setString(2, p.getTenPhong());
            ps.setDouble(3, p.getGiaPhong());
            ps.setString(4, p.getLoaiPhong());
            ps.setString(5, p.getTrangThai());
            ps.setString(6, p.getMoTa());
            ps.setInt(7, p.getSoNguoi());
            n = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    public boolean updatePhong(Phong p) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement ps = null;
        int n = 0;
        try{
            ps = con.prepareStatement("update Phong set TenPhong = ?, GiaPhong = ?, LoaiPhong = ?, TrangThai = ?, MoTa = ?, SoNguoi = ? where MaPhong = ?");
            ps.setString(1, p.getTenPhong());
            ps.setDouble(2, p.getGiaPhong());
            ps.setString(3,p.getLoaiPhong());
            ps.setString(4, p.getTrangThai());
            ps.setString(5, p.getMoTa());
            ps.setInt(6, p.getSoNguoi());
            ps.setString(7, p.getMaPhong());
            n = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    public boolean deletePhong(String maPhong) throws SQLException {
        ConnectDB.getInstance().connect();
        PreparedStatement ps = null;
        int n = 0;
        try{
            ps = ConnectDB.getConnection().prepareStatement("delete from Phong where MaPhong = ?");
            ps.setString(1, maPhong);
            n = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }
//    public  Phong searchPhong(String maPhong) {
//        try {
//            ConnectDB.getInstance().connect();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        Phong phong = null;
//        try{
//            ps = ConnectDB.getConnection().prepareStatement("select * from Phong where MaPhong = ?");
//            ps.setString(1, maPhong);
//            rs = ps.executeQuery();
//            while (rs.next()){
//                String tenPhong = rs.getString(2);
//                double giaPhong = rs.getDouble(3);
//                String loaiPhong = rs.getString(4);
//                int trangThai = rs.getInt(5);
//                String moTa = rs.getString(6);
//                int soNguoi = rs.getInt(7);
//                phong = new Phong(maPhong, tenPhong, giaPhong, loaiPhong, trangThai, moTa, soNguoi);
//            }
//        } catch (SQLException e){
//            e.printStackTrace();
//        }
//        return phong;
//    }
    public Phong searchPhong(String maPhong) {
        Phong phong = null;
        String query = "SELECT * FROM Phong WHERE maPhong = ?";

        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, maPhong);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) { // Nếu có kết quả
                String tenPhong = rs.getString("tenPhong");
                double giaPhong = rs.getDouble("giaPhong");
                String loaiPhong = rs.getString("loaiPhong");
                int trangThai = rs.getInt("tinhTrang");
                String moTa = rs.getString("moTa");
                int soNguoi = rs.getInt("soNguoi");

                // Khởi tạo đối tượng Phong
                phong = new Phong(maPhong, tenPhong, giaPhong, loaiPhong, trangThai, moTa, soNguoi);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In lỗi nếu có
        }
        return phong; // Trả về đối tượng Phong hoặc null nếu không tìm thấy
    }

    public static void main(String[] args) {
        System.out.println(new Phong_DAO().searchPhong("A001"));
    }
}