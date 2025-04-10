package dto;

import java.time.LocalDateTime;
import java.util.List;

public class PhieuDatDichVuDTO {
    private String maPDDV;
    private LocalDateTime ngayDatDichVu;
    private int soLuongDichVu;
    private String moTa;
    private String maKH;
    private String maNV;
    private List<String> dsMaDV;

    public PhieuDatDichVuDTO(String maPDDV, LocalDateTime ngayDatDichVu, int soLuongDichVu, String moTa, String maKH, String maNV, List<String> dsMaDV) {
        this.maPDDV = maPDDV;
        this.ngayDatDichVu = ngayDatDichVu;
        this.soLuongDichVu = soLuongDichVu;
        this.moTa = moTa;
        this.maKH = maKH;
        this.maNV = maNV;
        this.dsMaDV = dsMaDV;
    }
}

