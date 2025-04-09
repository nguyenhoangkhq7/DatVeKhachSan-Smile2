package model;

import java.util.Set;

public class Phong {

    private String maPhong;
    private String tenPhong;
    private double giaPhong;
    private int tinhTrang;
    private String moTa;
    private int soNguoi;

    private LoaiPhong loaiPhong;
    private Set<PhieuGiamGia> dsPhieuGiamGia;
    private PhieuDatPhong phieuDatPhong;

    public Phong() {
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public double getGiaPhong() {
        return giaPhong;
    }

    public void setGiaPhong(double giaPhong) {
        this.giaPhong = giaPhong;
    }

    public int getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getSoNguoi() {
        return soNguoi;
    }

    public void setSoNguoi(int soNguoi) {
        this.soNguoi = soNguoi;
    }

    public LoaiPhong getLoaiPhong() {
        return loaiPhong;
    }

    public void setLoaiPhong(LoaiPhong loaiPhong) {
        this.loaiPhong = loaiPhong;
    }

    public Set<PhieuGiamGia> getDsPhieuGiamGia() {
        return dsPhieuGiamGia;
    }

    public void setDsPhieuGiamGia(Set<PhieuGiamGia> dsPhieuGiamGia) {
        this.dsPhieuGiamGia = dsPhieuGiamGia;
    }

    public PhieuDatPhong getPhieuDatPhong() {
        return phieuDatPhong;
    }

    public void setPhieuDatPhong(PhieuDatPhong phieuDatPhong) {
        this.phieuDatPhong = phieuDatPhong;
    }

    @Override
    public String toString() {
        return "Phong{" +
                "maPhong='" + maPhong + '\'' +
                ", tenPhong='" + tenPhong + '\'' +
                ", giaPhong=" + giaPhong +
                ", tinhTrang=" + tinhTrang +
                ", moTa='" + moTa + '\'' +
                ", soNguoi=" + soNguoi +
                '}';
    }
}
