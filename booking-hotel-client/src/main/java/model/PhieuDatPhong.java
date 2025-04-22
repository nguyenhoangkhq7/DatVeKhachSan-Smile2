package model;

import java.time.LocalDate;
import java.util.Set;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PhieuDatPhong {
    @EqualsAndHashCode.Include

    private String maPDP;
    private LocalDate ngayDatPhong;
    private LocalDate ngayNhanPhongDuKien;
    private LocalDate ngayTraPhongDuKien;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private Set<Phong> dsPhong;
    private HoaDon hoaDon;

}
