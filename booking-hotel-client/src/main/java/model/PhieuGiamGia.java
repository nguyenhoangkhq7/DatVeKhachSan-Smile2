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
public class PhieuGiamGia {
    @EqualsAndHashCode.Include
    private String maPGG;
    private double mucGiamGia;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String dieuKienApDung;
    private int luotSuDung;
    private int moTa;
    private Set<Phong> phongs; // Mối quan hệ nhiều-nhiều
}
