package model;

import java.time.LocalDate;
import java.util.Set;

public class PhieuGiamGia {

    private String maPGG;
    private double mucGiamGia;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String dieuKienApDung;
    private int luotSuDung;
    private int moTa;

    private Set<Phong> phongs; // Mối quan hệ nhiều-nhiều

    public PhieuGiamGia() {
    }

    public String getMaPGG() {
        return maPGG;
    }

    public void setMaPGG(String maPGG) {
        this.maPGG = maPGG;
    }

    public double getMucGiamGia() {
        return mucGiamGia;
    }

    public void setMucGiamGia(double mucGiamGia) {
        this.mucGiamGia = mucGiamGia;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getDieuKienApDung() {
        return dieuKienApDung;
    }

    public void setDieuKienApDung(String dieuKienApDung) {
        this.dieuKienApDung = dieuKienApDung;
    }

    public int getLuotSuDung() {
        return luotSuDung;
    }

    public void setLuotSuDung(int luotSuDung) {
        this.luotSuDung = luotSuDung;
    }

    public int getMoTa() {
        return moTa;
    }

    public void setMoTa(int moTa) {
        this.moTa = moTa;
    }

    public Set<Phong> getPhongs() {
        return phongs;
    }

    public void setPhongs(Set<Phong> phongs) {
        this.phongs = phongs;
    }

    @Override
    public String toString() {
        return "PhieuGiamGia{" +
                "maPGG='" + maPGG + '\'' +
                ", mucGiamGia=" + mucGiamGia +
                ", ngayBatDau=" + ngayBatDau +
                ", ngayKetThuc=" + ngayKetThuc +
                ", dieuKienApDung='" + dieuKienApDung + '\'' +
                ", luotSuDung=" + luotSuDung +
                ", moTa=" + moTa +
                ", soPhong=" + (phongs != null ? phongs.size() : 0) +
                '}';
    }
}
