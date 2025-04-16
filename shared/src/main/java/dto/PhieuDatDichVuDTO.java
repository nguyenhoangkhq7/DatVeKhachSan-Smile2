package dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PhieuDatDichVuDTO {
    @EqualsAndHashCode.Include
    private String maPDDV;
    private LocalDateTime ngayDatDichVu;
    private int soLuongDichVu;
    private String moTa;
    private String maKH;
    private String maNV;
    private List<String> dsMaDV;

}

