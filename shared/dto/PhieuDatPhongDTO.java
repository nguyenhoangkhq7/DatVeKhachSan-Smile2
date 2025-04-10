package dto;

import java.time.LocalDate;
import java.util.List;

public class PhieuDatPhongDTO {
    private String maPDP;
    private String maKH;
    private String maNV;
    private List<String> dsMaPhong;
    private LocalDate ngayDatPhong;
    private LocalDate ngayNhanPhongDuKien;
    private LocalDate ngayTraPhongDuKien;

    public PhieuDatPhongDTO(String maPDP, String maKH, String maNV, List<String> dsMaPhong, LocalDate ngayDatPhong, LocalDate ngayNhanPhongDuKien, LocalDate ngayTraPhongDuKien) {
        this.maPDP = maPDP;
        this.maKH = maKH;
        this.maNV = maNV;
        this.dsMaPhong = dsMaPhong;
        this.ngayDatPhong = ngayDatPhong;
        this.ngayNhanPhongDuKien = ngayNhanPhongDuKien;
        this.ngayTraPhongDuKien = ngayTraPhongDuKien;
    }
}
