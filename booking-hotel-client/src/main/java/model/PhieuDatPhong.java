package model;

import java.time.LocalDate;
import java.util.Set;

public class PhieuDatPhong {

    private String maPDP;
    private LocalDate ngayDatPhong;
    private LocalDate ngayNhanPhongDuKien;
    private LocalDate ngayTraPhongDuKien;

    private KhachHang khachHang;
    private NhanVien nhanVien;
    private Set<Phong> dsPhong;
    private HoaDon hoaDon;

    public PhieuDatPhong() {
    }

    public String getMaPDP() {
        return maPDP;
    }

    public void setMaPDP(String maPDP) {
        this.maPDP = maPDP;
    }

    public LocalDate getNgayDatPhong() {
        return ngayDatPhong;
    }

    public void setNgayDatPhong(LocalDate ngayDatPhong) {
        this.ngayDatPhong = ngayDatPhong;
    }

    public LocalDate getNgayNhanPhongDuKien() {
        return ngayNhanPhongDuKien;
    }

    public void setNgayNhanPhongDuKien(LocalDate ngayNhanPhongDuKien) {
        this.ngayNhanPhongDuKien = ngayNhanPhongDuKien;
    }

    public LocalDate getNgayTraPhongDuKien() {
        return ngayTraPhongDuKien;
    }

    public void setNgayTraPhongDuKien(LocalDate ngayTraPhongDuKien) {
        this.ngayTraPhongDuKien = ngayTraPhongDuKien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public Set<Phong> getDsPhong() {
        return dsPhong;
    }

    public void setDsPhong(Set<Phong> dsPhong) {
        this.dsPhong = dsPhong;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    @Override
    public String toString() {
        return "PhieuDatPhong{" +
                "maPDP='" + maPDP + '\'' +
                ", ngayDatPhong=" + ngayDatPhong +
                ", ngayNhanPhongDuKien=" + ngayNhanPhongDuKien +
                ", ngayTraPhongDuKien=" + ngayTraPhongDuKien +
                ", khachHang=" + (khachHang != null ? khachHang.getMaKH() : "null") +
                ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNhanVien() : "null") +
                ", dsPhong=" + (dsPhong != null ? dsPhong.size() + " phòng" : "null") +
                ", hoaDon=" + (hoaDon != null ? hoaDon.getMaHD() : "null") +
                '}';
    }
}
