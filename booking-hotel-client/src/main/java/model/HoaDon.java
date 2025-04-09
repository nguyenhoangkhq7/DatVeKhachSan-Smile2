package model;

import java.time.LocalDateTime;
import java.util.Set;

public class HoaDon {

    private String maHD;
    private LocalDateTime ngayLapHD;
    private LocalDateTime ngayNhanPhong;
    private LocalDateTime ngayTraPhong;
    private int soPhongDat;

    private KhachHang khachHang;
    private PhieuDatPhong phieuDatPhong;
    private Set<PhieuDatDichVu> dsPhieuDatDichVu;
    private NhanVien nhanVien;

    public HoaDon() {
    }

    // Getters và Setters
    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public LocalDateTime getNgayLapHD() {
        return ngayLapHD;
    }

    public void setNgayLapHD(LocalDateTime ngayLapHD) {
        this.ngayLapHD = ngayLapHD;
    }

    public LocalDateTime getNgayNhanPhong() {
        return ngayNhanPhong;
    }

    public void setNgayNhanPhong(LocalDateTime ngayNhanPhong) {
        this.ngayNhanPhong = ngayNhanPhong;
    }

    public LocalDateTime getNgayTraPhong() {
        return ngayTraPhong;
    }

    public void setNgayTraPhong(LocalDateTime ngayTraPhong) {
        this.ngayTraPhong = ngayTraPhong;
    }

    public int getSoPhongDat() {
        return soPhongDat;
    }

    public void setSoPhongDat(int soPhongDat) {
        this.soPhongDat = soPhongDat;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public PhieuDatPhong getPhieuDatPhong() {
        return phieuDatPhong;
    }

    public void setPhieuDatPhong(PhieuDatPhong phieuDatPhong) {
        this.phieuDatPhong = phieuDatPhong;
    }

    public Set<PhieuDatDichVu> getDsPhieuDatDichVu() {
        return dsPhieuDatDichVu;
    }

    public void setDsPhieuDatDichVu(Set<PhieuDatDichVu> dsPhieuDatDichVu) {
        this.dsPhieuDatDichVu = dsPhieuDatDichVu;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHD='" + maHD + '\'' +
                ", ngayLapHD=" + ngayLapHD +
                ", ngayNhanPhong=" + ngayNhanPhong +
                ", ngayTraPhong=" + ngayTraPhong +
                ", soPhongDat=" + soPhongDat +
                '}';
    }
}
