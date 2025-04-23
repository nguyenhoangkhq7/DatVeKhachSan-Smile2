package dto;

import java.time.LocalDateTime;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HoaDonDTO {
    @EqualsAndHashCode.Include
    private String maHD;
    private String maKH;
    private String maNV;
    private String maPDP;
    private LocalDateTime ngayLapHD;
    private LocalDateTime ngayNhanPhong;
    private LocalDateTime ngayTraPhong;
    private int soPhongDat;

}
