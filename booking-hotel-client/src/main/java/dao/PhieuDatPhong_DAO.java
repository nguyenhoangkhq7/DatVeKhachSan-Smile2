package dao;

import connectDB.ConnectDB;
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatPhong;
import entity.Phong;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class PhieuDatPhong_DAO {
    private ArrayList<PhieuDatPhong> dsPhieuDatPhong;
    private ArrayList<PhieuDatPhong> dsPhieuDatPhongDangCho;
    private ArrayList<PhieuDatPhong> dsPhieuDatPhongDeDoi;
    private ArrayList<PhieuDatPhong> dsPhieuDatPhongDaNhan;
    public PhieuDatPhong_DAO() {
        dsPhieuDatPhong = new ArrayList<>();
        dsPhieuDatPhongDangCho = new ArrayList<>();
        dsPhieuDatPhongDeDoi = new ArrayList<>();
        dsPhieuDatPhongDaNhan = new ArrayList<>();
    }

    public ArrayList<PhieuDatPhong> getDSPhieuDatPhongDaNhan() {
        try{
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "select pdp.*, kh.maKH, kh.hoTen AS hoTenKH, nv.maNV, nv.Hoten AS hoTenNV from PhieuDatPhong pdp join KhachHang kh on pdp.maKH = kh.maKH join NhanVien nv on pdp.maNV = nv.maNV where pdp.tinhTrangPDP = 1 order by pdp.ngayDat DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                String maPDP = rs.getString("maPDP");
                String maPhong = rs.getString("maPhong");
                Phong p = new Phong(maPhong);
                String maNV = rs.getString("maNV");
                String hoTenNV = rs.getString("hoTenNV");
                NhanVien nv = new NhanVien(maNV, hoTenNV);
                String maKH = rs.getString("maKH");
                String hoTenKH = rs.getString("hoTenKH");
                KhachHang kh = new KhachHang(maKH, hoTenKH);
                Date ngayDen = rs.getTimestamp("ngayDen");
                Date ngayDi = rs.getTimestamp("ngayDi");
                Date ngayDat = rs.getDate("ngayDat");
                int tinhTrangPDP = rs.getInt("tinhTrangPDP");
                PhieuDatPhong phieuDatPhong= new PhieuDatPhong(maPDP, p, nv, kh, ngayDi, ngayDen, ngayDat, tinhTrangPDP);
                dsPhieuDatPhongDaNhan.add(phieuDatPhong);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return dsPhieuDatPhongDaNhan;
    }

    public ArrayList<PhieuDatPhong> getDSPhieuDatPhongDeDoi() {
        try{
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "select pdp.*, kh.maKH, kh.hoTen AS hoTenKH, nv.maNV, nv.Hoten AS hoTenNV from PhieuDatPhong pdp join KhachHang kh on pdp.maKH = kh.maKH join NhanVien nv on pdp.maNV = nv.maNV where pdp.tinhTrangPDP = 0 or pdp.tinhTrangPDP = 1 order by pdp.ngayDat DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                String maPDP = rs.getString("maPDP");
                String maPhong = rs.getString("maPhong");
                Phong p = new Phong(maPhong);
                String maNV = rs.getString("maNV");
                String hoTenNV = rs.getString("hoTenNV");
                NhanVien nv = new NhanVien(maNV, hoTenNV);
                String maKH = rs.getString("maKH");
                String hoTenKH = rs.getString("hoTenKH");
                KhachHang kh = new KhachHang(maKH, hoTenKH);
                Date ngayDen = rs.getTimestamp("ngayDen");
                Date ngayDi = rs.getTimestamp("ngayDi");
                Date ngayDat = rs.getDate("ngayDat");
                int tinhTrangPDP = rs.getInt("tinhTrangPDP");
                PhieuDatPhong phieuDatPhong= new PhieuDatPhong(maPDP, p, nv, kh, ngayDi, ngayDen, ngayDat, tinhTrangPDP);
                dsPhieuDatPhongDeDoi.add(phieuDatPhong);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return dsPhieuDatPhongDeDoi;
    }
    public PhieuDatPhong getPDPTheoMa(String maPDP) {
        String sql = "SELECT * FROM PhieuDatPhong WHERE maPDP = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPDP);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Phong p = new Phong_DAO().searchPhong(rs.getString("maPhong"));
                    NhanVien nv = new NhanVien_DAO().timKiemTheoMa(rs.getString("maNV"));
                    KhachHang kh = new KhachHang_DAO().timKiemTheoMaKH(rs.getString("maKH"));
                    Date ngayDen = rs.getTimestamp("ngayDen");
                    Date ngayDi = rs.getTimestamp("ngayDi");
                    Date ngayDat = rs.getDate("ngayDat");
                    int tinhTrangPDP = rs.getInt("tinhTrangPDP");

                    return new PhieuDatPhong(maPDP, p, nv, kh, ngayDi, ngayDen, ngayDat, tinhTrangPDP);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<PhieuDatPhong> getDSPhieuDatPhongDangCho() {
        try{
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "select pdp.*, kh.maKH, kh.hoTen AS hoTenKH, nv.maNV, nv.Hoten AS hoTenNV from PhieuDatPhong pdp join KhachHang kh on pdp.maKH = kh.maKH join NhanVien nv on pdp.maNV = nv.maNV where pdp.tinhTrangPDP = 0  order by pdp.ngayDat DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                String maPDP = rs.getString("maPDP");
                String maPhong = rs.getString("maPhong");
                Phong p = new Phong(maPhong);
                String maNV = rs.getString("maNV");
                String hoTenNV = rs.getString("hoTenNV");
                NhanVien nv = new NhanVien(maNV, hoTenNV);
                String maKH = rs.getString("maKH");
                String hoTenKH = rs.getString("hoTenKH");
                KhachHang kh = new KhachHang(maKH, hoTenKH);
                Date ngayDen = rs.getTimestamp("ngayDen");
                Date ngayDi = rs.getTimestamp("ngayDi");
                Date ngayDat = rs.getDate("ngayDat");
                int tinhTrangPDP = rs.getInt("tinhTrangPDP");
                PhieuDatPhong phieuDatPhong= new PhieuDatPhong(maPDP, p, nv, kh, ngayDi, ngayDen, ngayDat, tinhTrangPDP);
                dsPhieuDatPhongDangCho.add(phieuDatPhong);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return dsPhieuDatPhongDangCho;
    }

    public ArrayList<PhieuDatPhong> getDSPhieuDatPhong() {
        try{
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "select pdp.*, kh.maKH, kh.hoTen AS hoTenKH, nv.maNV, nv.Hoten AS hoTenNV from PhieuDatPhong pdp join KhachHang kh on pdp.maKH = kh.maKH join NhanVien nv on pdp.maNV = nv.maNV order by pdp.ngayDat DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                String maPDP = rs.getString("maPDP");
                String maPhong = rs.getString("maPhong");
                Phong p = new Phong(maPhong);
                String maNV = rs.getString("maNV");
                String hoTenNV = rs.getString("hoTenNV");
                NhanVien nv = new NhanVien(maNV, hoTenNV);
                String maKH = rs.getString("maKH");
                String hoTenKH = rs.getString("hoTenKH");
                KhachHang kh = new KhachHang(maKH, hoTenKH);
                Date ngayDen = rs.getTimestamp("ngayDen");
                Date ngayDi = rs.getTimestamp("ngayDi");
                Date ngayDat = rs.getDate("ngayDat");
                int tinhTrangPDP = rs.getInt("tinhTrangPDP");
                PhieuDatPhong phieuDatPhong= new PhieuDatPhong(maPDP, p, nv, kh, ngayDi, ngayDen, ngayDat, tinhTrangPDP);
                dsPhieuDatPhong.add(phieuDatPhong);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return dsPhieuDatPhong;
    }

    public ArrayList<String> getSoPhongDaDat(Date ngayDen, Date ngayDi) {
        ArrayList<String> dsSoPhong = new ArrayList<>();

        Connection con = ConnectDB.getInstance().getConnection();
        String sql = "SELECT maPhong FROM PhieuDatPhong WHERE ((ngayDen <= ? AND ngayDi >= ?) OR (ngayDen <= ? AND ngayDi >= ?) OR (ngayDen >= ? AND ngayDi <= ?) OR (ngayDen <= ? AND ngayDi >= ?)) AND tinhTrangPDP <> 2";

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setTimestamp(1, new Timestamp(ngayDi.getTime()));
            stmt.setTimestamp(2, new Timestamp(ngayDen.getTime()));
            stmt.setTimestamp(3, new Timestamp(ngayDi.getTime()));
            stmt.setTimestamp(4, new Timestamp(ngayDen.getTime()));
            stmt.setTimestamp(5, new Timestamp(ngayDen.getTime()));
            stmt.setTimestamp(6, new Timestamp(ngayDi.getTime()));
            stmt.setTimestamp(7, new Timestamp(ngayDen.getTime()));
            stmt.setTimestamp(8, new Timestamp(ngayDi.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dsSoPhong.add(rs.getString("maPhong"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsSoPhong;
    }


    public boolean huyDatPhong(String maPDP) {
            try {
                Connection con = ConnectDB.getInstance().getConnection();
                String sql = "UPDATE PhieuDatPhong SET tinhTrangPDP = 2 WHERE maPDP = ?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, maPDP);
                int rowsUpdated = stmt.executeUpdate();
                return rowsUpdated > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

    }

    public boolean nhanPhong(String maPDP) {
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "UPDATE PhieuDatPhong SET tinhTrangPDP = 1 WHERE maPDP = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maPDP);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean traPhong(String maPDP) {
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "UPDATE PhieuDatPhong SET tinhTrangPDP = 3 WHERE maPDP = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maPDP);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean datPhong(PhieuDatPhong phieuDatPhong) {
        try {
            Connection con = ConnectDB.getInstance().getConnection();

            String checkSql = "SELECT diaChi, SDT, email FROM KhachHang WHERE maKH = ?";
            PreparedStatement checkStatement = con.prepareStatement(checkSql);
            checkStatement.setString(1, phieuDatPhong.getKhachHang().getMaKH());
            ResultSet rs = checkStatement.executeQuery();

            if (rs.next()) {
                boolean update = false;
                if (!rs.getString("diaChi").equals(phieuDatPhong.getKhachHang().getDiaChi())) {
                    update = true;
                }
                if (!rs.getString("SDT").equals(phieuDatPhong.getKhachHang().getSdt())) {
                    update = true;
                }
                if (!rs.getString("email").equals(phieuDatPhong.getKhachHang().getEmail())) {
                    update = true;
                }

                if (update) {
                    String updateSql = "UPDATE KhachHang SET diaChi = ?, SDT = ?, email = ? WHERE maKH = ?";
                    PreparedStatement updateStatement = con.prepareStatement(updateSql);
                    updateStatement.setString(1, phieuDatPhong.getKhachHang().getDiaChi());
                    updateStatement.setString(2, phieuDatPhong.getKhachHang().getSdt());
                    updateStatement.setString(3, phieuDatPhong.getKhachHang().getEmail());
                    updateStatement.setString(4, phieuDatPhong.getKhachHang().getMaKH());
                    updateStatement.executeUpdate();
                }
            } else {
                // Tao moi
                String insertKhachHangSql = "INSERT INTO KhachHang (maKH, hoTen, diaChi, SDT, soCCCD, email, trangThai, ngaySinh) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = con.prepareStatement(insertKhachHangSql);
                insertStatement.setString(1, phieuDatPhong.getKhachHang().getMaKH());
                insertStatement.setString(2, phieuDatPhong.getKhachHang().getHoTen());
                insertStatement.setString(3, phieuDatPhong.getKhachHang().getDiaChi());
                insertStatement.setString(4, phieuDatPhong.getKhachHang().getSdt());
                insertStatement.setString(5, phieuDatPhong.getKhachHang().getcCCD());
                insertStatement.setString(6, phieuDatPhong.getKhachHang().getEmail());
                insertStatement.setInt(7, 1);
                insertStatement.setDate(8, new java.sql.Date(phieuDatPhong.getKhachHang().getNgaySinh().getTime()));

                insertStatement.executeUpdate();
            }
            String sql = "INSERT INTO PhieuDatPhong (maPDP, maPhong, maNV, maKH, ngayDat, ngayDen, ngayDi, tinhTrangPDP) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            preparedStatement.setString(1, phieuDatPhong.getMaPDP());
            preparedStatement.setString(2, phieuDatPhong.getPhong().getMaPhong());
            preparedStatement.setString(3, phieuDatPhong.getNhanVien().getMaNV());
            preparedStatement.setString(4, phieuDatPhong.getKhachHang().getMaKH());
            preparedStatement.setDate(5, new java.sql.Date(phieuDatPhong.getNgayDat().getTime()));
            preparedStatement.setTimestamp(6, new Timestamp(phieuDatPhong.getNgayDen().getTime()));
            preparedStatement.setTimestamp(7, new Timestamp(phieuDatPhong.getNgayDi().getTime()));
            preparedStatement.setInt(8, 0);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public boolean doesPDPIdExist(String maPDP){
        for (PhieuDatPhong pdp: dsPhieuDatPhong){
            if (pdp.getMaPDP().equals(maPDP)){
                return true;
            }
        }
        return false;
    }

}
