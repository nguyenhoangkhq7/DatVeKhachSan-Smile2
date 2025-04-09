package model;

import java.time.LocalDateTime;
import java.util.Set;

public class PhieuDatDichVu {

    private String maPDDV;
    private LocalDateTime ngayDatDichVu;
    private int soLuongDichVu;
    private String moTa;

    private NhanVien nhanVien;
    private KhachHang khachHang;
    private HoaDon hoaDon;
    private Set<DichVu> dsDichVu;

    public PhieuDatDichVu() {
    }

    public String getMaPDDV() {
        return maPDDV;
    }

    public void setMaPDDV(String maPDDV) {
        this.maPDDV = maPDDV;
    }

    public LocalDateTime getNgayDatDichVu() {
        return ngayDatDichVu;
    }

    public void setNgayDatDichVu(LocalDateTime ngayDatDichVu) {
        this.ngayDatDichVu = ngayDatDichVu;
    }

    public int getSoLuongDichVu() {
        return soLuongDichVu;
    }

    public void setSoLuongDichVu(int soLuongDichVu) {
        this.soLuongDichVu = soLuongDichVu;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public Set<DichVu> getDsDichVu() {
        return dsDichVu;
    }

    public void setDsDichVu(Set<DichVu> dsDichVu) {
        this.dsDichVu = dsDichVu;
    }

    @Override
    public String toString() {
        return "PhieuDatDichVu{" +
                "maPDDV='" + maPDDV + '\'' +
                ", ngayDatDichVu=" + ngayDatDichVu +
                ", soLuongDichVu=" + soLuongDichVu +
                ", moTa='" + moTa + '\'' +
                ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNhanVien() : "null") +
                ", khachHang=" + (khachHang != null ? khachHang.getMaKH() : "null") +
                ", hoaDon=" + (hoaDon != null ? hoaDon.getMaHD() : "null") +
                ", dsDichVu=" + (dsDichVu != null ? dsDichVu.size() + " dịch vụ" : "null") +
                '}';
    }
}
