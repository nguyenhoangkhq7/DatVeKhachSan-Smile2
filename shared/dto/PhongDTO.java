package dto;

public class PhongDTO {
    private String maPhong;
    private String tenPhong;
    private double giaPhong;
    private int tinhTrang;
    private int soNguoi;
    private String moTa;
    private String maLoai;

    public PhongDTO(String maPhong, String tenPhong, double giaPhong, int tinhTrang, int soNguoi, String moTa, String maLoai) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.giaPhong = giaPhong;
        this.tinhTrang = tinhTrang;
        this.soNguoi = soNguoi;
        this.moTa = moTa;
        this.maLoai = maLoai;
    }
}
