package dto;

import java.time.LocalDate;
import java.util.List;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PhieuDatPhongDTO {
    @EqualsAndHashCode.Include
    private String maPDP;
    private String maKH;
    private String maNV;
    private List<String> dsMaPhong;
    private LocalDate ngayDatPhong;
    private LocalDate ngayNhanPhongDuKien;
    private LocalDate ngayTraPhongDuKien;
    private String maHD;
}
