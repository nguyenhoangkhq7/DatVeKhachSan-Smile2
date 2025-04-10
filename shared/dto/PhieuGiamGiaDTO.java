package dto;

import java.time.LocalDate;

public class PhieuGiamGiaDTO {
    private String maPGG;
    private double mucGiamGia;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String dieuKienApDung;
    private int luotSuDung;
    private String moTa;

    public PhieuGiamGiaDTO(String maPGG, double mucGiamGia, LocalDate ngayBatDau, LocalDate ngayKetThuc, String dieuKienApDung, int luotSuDung, String moTa) {
        this.maPGG = maPGG;
        this.mucGiamGia = mucGiamGia;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.dieuKienApDung = dieuKienApDung;
        this.luotSuDung = luotSuDung;
        this.moTa = moTa;
    }
}

