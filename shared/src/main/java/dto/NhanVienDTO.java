package dto;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NhanVienDTO {
    @EqualsAndHashCode.Include
    private String maNhanVien;
    private String hoTen;
    private int chucVu;
    @SerializedName("soDienThoai")
    private String SDT;
    private String diaChi;
    private String email;
    private LocalDate ngaySinh;
    private LocalDate ngayVaoLam;
    private double luongCoBan;
    private double heSoLuong;

}

