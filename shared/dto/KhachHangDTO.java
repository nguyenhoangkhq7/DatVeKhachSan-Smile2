package dto;

public class KhachHangDTO {
    private String maKH;
    private String hoTen;
    private String soDienThoai;
    private String soCCCD;
    private String email;

    public KhachHangDTO(String maKH, String hoTen, String soDienThoai, String soCCCD, String email) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.soCCCD = soCCCD;
        this.email = email;
    }
}

