package dto;

import java.time.LocalDateTime;

public class HoaDonDTO {
    private String maHD;
    private String maKH;
    private String maNV;
    private String maPDP;
    private LocalDateTime ngayLapHD;
    private LocalDateTime ngayNhanPhong;
    private LocalDateTime ngayTraPhong;
    private int soPhongDat;

    public HoaDonDTO(String maHD, String maKH, String maNV, String maPDP, LocalDateTime ngayLapHD, LocalDateTime ngayNhanPhong, LocalDateTime ngayTraPhong, int soPhongDat) {
        this.maHD = maHD;
        this.maKH = maKH;
        this.maNV = maNV;
        this.maPDP = maPDP;
        this.ngayLapHD = ngayLapHD;
        this.ngayNhanPhong = ngayNhanPhong;
        this.ngayTraPhong = ngayTraPhong;
        this.soPhongDat = soPhongDat;
    }
}
