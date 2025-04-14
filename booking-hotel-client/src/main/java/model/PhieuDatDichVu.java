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
public class PhieuDatDichVu {
    @EqualsAndHashCode.Include
    private String maPDDV;
    private LocalDateTime ngayDatDichVu;
    private int soLuongDichVu;
    private String moTa;
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private HoaDon hoaDon;
    private Set<DichVu> dsDichVu;
}
