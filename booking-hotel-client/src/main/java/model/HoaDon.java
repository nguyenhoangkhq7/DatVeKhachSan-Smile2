package model;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HoaDon {
    @EqualsAndHashCode.Include
    private String maHD;
    private LocalDateTime ngayLapHD;
    private LocalDateTime ngayNhanPhong;
    private LocalDateTime ngayTraPhong;
    private int soPhongDat;
    private KhachHang khachHang;
    private PhieuDatPhong phieuDatPhong;
    private Set<PhieuDatDichVu> dsPhieuDatDichVu;
    private NhanVien nhanVien;
}
