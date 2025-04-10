package dto;


public class DichVuDTO {
    private String maDV;
    private String tenDV;
    private double donGia;
    private String donViTinh;
    private String moTa;

    public DichVuDTO(String maDV, String tenDV, double donGia, String donViTinh, String moTa) {
        this.maDV = maDV;
        this.tenDV = tenDV;
        this.donGia = donGia;
        this.donViTinh = donViTinh;
        this.moTa = moTa;
    }
}
