package dao;

import connectDB.ConnectDB;
import entity.KhachHang;
import entity.NhanVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class NhanVien_DAO {
    private ArrayList<NhanVien> dsnv;
    public NhanVien_DAO (){
        dsnv = new ArrayList<NhanVien>();
    }

    public ArrayList<NhanVien> getDSNhanVien() {
        ArrayList<NhanVien> dsnv = new ArrayList<>();
        try {
            // Câu truy vấn chỉ lấy khách hàng có trạng thái khác 0
            String sql = "SELECT * FROM NhanVien WHERE TrangThai = 1";
            // Sử dụng PreparedStatement để tăng tính an toàn và hiệu suất
            Connection conn = ConnectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Lấy thông tin từ ResultSet
                String maNV = rs.getString("maNV");
                String hoTen = rs.getString("hoTen");
                String chucVu = rs.getString("chucVu");
                String SDT = rs.getString("SDT");
                String diaChi = rs.getString("diaChi");
                String email = rs.getString("email");
                Date ngaySinh = rs.getDate("ngaySinh");
                Date ngayVaoLam = rs.getDate("ngayVaoLam");
                double luongCoBan = rs.getDouble("luongCoBan");
                double heSoLuong = rs.getDouble("heSoLuong");
                int trangThai = rs.getInt("trangThai");
                // Khởi tạo đối tượng KhachHang và thêm vào danh sách
                NhanVien nv= new NhanVien(maNV, hoTen, chucVu,SDT, diaChi, email,ngaySinh, ngayVaoLam, luongCoBan, heSoLuong, trangThai);
                dsnv.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In lỗi nếu có
        }
        return dsnv; // Trả về danh sách khách hàng
    }

    public boolean themNhanVien(NhanVien nv) throws SQLException {
        int kq = 0;
        try {
            String query = "INSERT INTO NhanVien (maNV, hoTen, chucVu, ngaySinh, ngayVaoLam, SDT, diaChi, email, luongCoBan, heSoLuong, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ConnectDB.getInstance().connect();
            Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nv.getMaNV());
            stmt.setString(2, nv.getHoTen());
            stmt.setString(3, nv.getChucVu());
            stmt.setDate(4, new java.sql.Date(nv.getNgaySinh().getTime()));
            stmt.setDate(5, new java.sql.Date(nv.getNgayVaoLam().getTime()));
            stmt.setString(6, nv.getSoDT());
            stmt.setString(7, nv.getDiaChi());
            stmt.setString(8, nv.getEmail());
            stmt.setDouble(9, nv.getLuongCoBan());
            stmt.setDouble(10, nv.getHeSoLuong());
            stmt.setInt(11, 1); // Đặt giá trị mặc định cho trangThai là 1
            kq = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kq > 0;
    }
    public boolean xoaNV(String maNV) throws SQLException {
        String query = "UPDATE NhanVien SET trangThai = 0 WHERE maNV = ?";
        try {
            ConnectDB.getInstance().connect();
            Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, maNV);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean suaNhanVien(NhanVien nv) throws SQLException {
        int kq = 0;
        try {
            String query = "UPDATE NhanVien SET hoTen=?, chucVu=?, ngaySinh=?, ngayVaoLam=?, SDT=?, diaChi=?, email=?, luongCoBan=?, heSoLuong=? WHERE maNV=?";
            ConnectDB.getInstance().connect();
            Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nv.getHoTen());
            stmt.setString(2, nv.getChucVu());
            stmt.setDate(3, new java.sql.Date(nv.getNgaySinh().getTime()));
            stmt.setDate(4, new java.sql.Date(nv.getNgayVaoLam().getTime()));
            stmt.setString(5, nv.getSoDT());
            stmt.setString(6, nv.getDiaChi());
            stmt.setString(7, nv.getEmail());
            stmt.setDouble(8, nv.getLuongCoBan());
            stmt.setDouble(9, nv.getHeSoLuong());
            stmt.setString(10, nv.getMaNV());
            kq = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kq > 0;
    }

    public NhanVien timKiemTheoMa(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE maNV = ?";
        try {
            ConnectDB.getInstance().connect();
            Connection conn = ConnectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maNV);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hoTen = rs.getString("hoTen");
                String chucVu = rs.getString("chucVu");
                String SDT = rs.getString("SDT");
                String diaChi = rs.getString("diaChi");
                String email = rs.getString("email");
                Date ngaySinh = rs.getDate("ngaySinh");
                Date ngayVaoLam = rs.getDate("ngayVaoLam");
                double luongCoBan = rs.getDouble("luongCoBan");
                double heSoLuong = rs.getDouble("heSoLuong");
                int trangThai = rs.getInt("trangThai");

                return new NhanVien(maNV, hoTen, chucVu, SDT, diaChi, email, ngaySinh, ngayVaoLam, luongCoBan, heSoLuong, trangThai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean kiemTraTrungEmail(String email) {
        try {
            String sql = "SELECT COUNT(*) FROM NhanVien WHERE email = ?";
            ConnectDB.getInstance().connect();
            Connection conn = ConnectDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu số lượng > 0 thì email đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu không tìm thấy hoặc có lỗi
    }

    public NhanVien GetNV(int i) {
        if (i >= 0 && i < dsnv.size()) {
            return dsnv.get(i);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new NhanVien_DAO().timKiemTheoMa("17-MN90"));
    }
}
