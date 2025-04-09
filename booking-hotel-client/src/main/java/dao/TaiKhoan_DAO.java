package dao;

import connectDB.ConnectDB;
import entity.NhanVien;
import entity.TaiKhoan;

import java.sql.*;
import java.util.ArrayList;

public class TaiKhoan_DAO {
    private ArrayList<TaiKhoan> dsTK;
    private static NhanVien currentNhanVien;
    public TaiKhoan_DAO() {
        dsTK = new ArrayList<TaiKhoan>();
    }

    public String checkDangNhap(String tenDn, String matKhau) {
        Connection con = ConnectDB.getInstance().getConnection();
        String sql = "SELECT * FROM TaiKhoan tk JOIN NhanVien nv ON tk.maNV = nv.maNV WHERE tenDn = ? AND matKhau = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, tenDn);
            pstmt.setString(2, matKhau);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Lấy thông tin NhanVien từ kết quả truy vấn
                String maNV = rs.getString("maNV");
                String hoTen = rs.getString("hoTen");
                String chucVu = rs.getString("chucVu");
                Date ngaySinh = rs.getDate("ngaySinh");
                Date ngayVaoLam = rs.getDate("ngayVaoLam");
                String SDT = rs.getString("SDT");
                String diaChi = rs.getString("diaChi");
                String email = rs.getString("email");
                double luongCoBan = rs.getDouble("luongCoBan");
                double heSoLuong = rs.getDouble("heSoLuong");
                int trangThai = rs.getInt("trangThai");

                NhanVien nhanVien = new NhanVien(maNV, hoTen, chucVu, SDT, diaChi, email, ngaySinh, ngayVaoLam, luongCoBan, heSoLuong, trangThai);

                // Kiểm tra nếu tài khoản là admin
                currentNhanVien = nhanVien;
                if ("admin".equals(tenDn) && "1".equals(matKhau)) {
                    return "ADMIN"; // Trả về vai trò admin
                } else {
                    return "EMPLOYEE"; // Trả về vai trò nhân viên
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Nếu không tìm thấy tài khoản
    }
    public ArrayList<TaiKhoan> getDsTK() throws SQLException {
        Connection con = ConnectDB.getInstance().getConnection();
        String sql = "Select * from TaiKhoan";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()){
            String tenDN = rs.getString("tenDN");
            String matKhau = rs.getString("matKhau");
            NhanVien nhanVien = new NhanVien(rs.getString("maNV"));
            TaiKhoan taiKhoan = new TaiKhoan(tenDN, matKhau, nhanVien);
            dsTK.add(taiKhoan);

        }
        return dsTK;
    }
    //them mot tai khoan
    public boolean themTK(TaiKhoan tk) {
        if (dsTK.contains(tk)) {
            return false;
        }
        return dsTK.add(tk);
    }
    //xoa mot tai khoan
    public boolean xoaTK(int i) {
        if (i >= 0 && i < dsTK.size()) {
            dsTK.remove(i);
            return true;
        }
        return false;
    }
    public ArrayList<TaiKhoan> getDSTK() {
        return dsTK;
    }
    public TaiKhoan GetTK(int i) {
        if (i < 0 || i > dsTK.size()) {
            return null;
        }
        return dsTK.get(i);
    }
    public static NhanVien getCurrentNhanVien(){
        return currentNhanVien;
    }
    //tim kiem tai khoan
//    public TaiKhoan timKiem (String tenDN) {
//        TaiKhoan tk = new TaiKhoan(tenDN);
//        if (dsTK.contains(tk))
//            return dsTK.get(dsTK.indexOf(tk));
//        return null;
//    }
//    //cap nhat nhan vien
//    public boolean capNhatTaiKhoan (String tenDNOld, TaiKhoan tkNew) {
//        TaiKhoan tkOld = new TaiKhoan(tenDNOld);
//        if (dsTK.contains(tenDNOld)) {
//            tkOld = dsTK.get(dsTK.indexOf(tkOld));
//            tkOld.setMatKhau(tkNew.getMatKhau());
//            return true;
//        }
//        return false;
//    }
}
