package model;

public class DichVu {

    private String maDV;
    private String tenDV;
    private double donGia;
    private String donViTinh;
    private String moTa;
    private PhieuDatDichVu phieuDatDichVu;

    public DichVu() {
    }

    public DichVu(String maDV, String tenDV, double donGia, String donViTinh, String moTa) {
        this.maDV = maDV;
        this.tenDV = tenDV;
        this.donGia = donGia;
        this.donViTinh = donViTinh;
        this.moTa = moTa;
    }

    public String getMaDV() {
        return maDV;
    }

    public void setMaDV(String maDV) {
        this.maDV = maDV;
    }

    public String getTenDV() {
        return tenDV;
    }

    public void setTenDV(String tenDV) {
        this.tenDV = tenDV;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public PhieuDatDichVu getPhieuDatDichVu() {
        return phieuDatDichVu;
    }

    public void setPhieuDatDichVu(PhieuDatDichVu phieuDatDichVu) {
        this.phieuDatDichVu = phieuDatDichVu;
    }

    @Override
    public String toString() {
        return "DichVu{" +
                "maDV='" + maDV + '\'' +
                ", tenDV='" + tenDV + '\'' +
                ", donGia=" + donGia +
                ", donViTinh='" + donViTinh + '\'' +
                ", moTa='" + moTa + '\'' +
                '}';
    }
}
