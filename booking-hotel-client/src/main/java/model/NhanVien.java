package model;

import java.time.LocalDate;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NhanVien {
    @EqualsAndHashCode.Include
    private String maNhanVien;
    private String hoTen;
    private int chucVu;
    private String SDT;
    private String diaChi;
    private String email;
    private LocalDate ngaySinh;
    private TaiKhoan taiKhoan;

}
