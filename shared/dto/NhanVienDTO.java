package dto;

public class NhanVienDTO {
    private String maNhanVien;
    private String hoTen;
    private String email;
    private String sdt;

    public NhanVienDTO(String maNhanVien, String hoTen, String email, String sdt) {
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
        this.email = email;
        this.sdt = sdt;
    }
}

