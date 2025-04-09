package dao;

import connectDB.ConnectDB;
import entity.DichVu;
import entity.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class DichVu_DAO {

    private  Connection conn;
    private ArrayList<DichVu> dsdv;
    public DichVu_DAO() {
        dsdv = new ArrayList<DichVu>();
        this.conn = ConnectDB.getInstance().getConnection();
        dsdv = new ArrayList<>();
    }
    public ArrayList<DichVu> getDSDichVu() {
        ArrayList<DichVu> dsdv = new ArrayList<>();
        try {
            // Lấy kết nối đến cơ sở dữ liệu
            Connection con = ConnectDB.getInstance().getConnection();
            // Câu truy vấn chỉ lấy khách hàng có trạng thái khác 0
            String sql = "SELECT * FROM DichVu WHERE TrangThai = 1";
            // Sử dụng PreparedStatement để tăng tính an toàn và hiệu suất
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Lấy thông tin từ ResultSet
                String maDV = rs.getString("maDV");
                String tenDV = rs.getString("tenDV");
                String moTa = rs.getString("moTa");
                String dVT = rs.getString("donViTinh");
                int soLT = rs.getInt("soLuongTon");
                double giaDV = rs.getDouble("giaDV");
                int trangThai = rs.getInt("trangThai");
                // Khởi tạo đối tượng KhachHang và thêm vào danh sách
                DichVu dv = new DichVu(maDV, tenDV, giaDV, dVT, soLT, moTa, trangThai);
                dsdv.add(dv);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In lỗi nếu có
        }
        return dsdv;
    }
    public boolean themDichVu(DichVu dv) throws SQLException {

        int kq=0;
        try  {
            String query = "INSERT INTO DichVu (maDV, tenDV, moTa, giaDV,donViTinh,soLuongTon, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, dv.getMaDV());
            stmt.setString(2, dv.getTenDV());
            stmt.setString(3, dv.getMoTa());
            stmt.setDouble(4, dv.getGiaDV());
            stmt.setString(5, dv.getDonViTinh());
            stmt.setInt(6, dv.getSoLuongTon());
            stmt.setInt(7, 1);

            kq = stmt.executeUpdate();
        }catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return kq>0;
    }
    public boolean xoaDV(String maDV) throws SQLException {
        String query = "UPDATE DichVu SET trangThai = 0 WHERE maDV = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maDV);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean suaDichVu(DichVu dv) throws SQLException {
        int kq=0;

        try  {
            String query = "UPDATE DichVu SET tenDV=?, moTa=?, giaDV=? ,donViTinh=?, soLuongTon=? WHERE maDV = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, dv.getTenDV());
            stmt.setString(2, dv.getMoTa());
            stmt.setDouble(3, dv.getGiaDV());
            stmt.setString(4, dv.getDonViTinh());
            stmt.setInt(5, dv.getSoLuongTon());
            stmt.setString(6,dv.getMaDV());

            kq=stmt.executeUpdate();
        }catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return kq>0;
    }
    public boolean kiemTraTrungMaDichVu(String maDV) {
        try {
            String sql = "SELECT COUNT(*) FROM DichVu WHERE maDV = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maDV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu số lượng > 0 thì mã dịch vụ đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu không tìm thấy hoặc có lỗi
    }

}
