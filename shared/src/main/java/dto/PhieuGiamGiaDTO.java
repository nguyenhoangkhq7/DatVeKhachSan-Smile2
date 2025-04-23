package dto;

import java.time.LocalDate;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PhieuGiamGiaDTO {
    @EqualsAndHashCode.Include
    private String maPGG;
    private double mucGiamGia;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String dieuKienApDung;
    private int luotSuDung;
    private String moTa;
}

