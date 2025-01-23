package model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Table(name = "phieu_giam_gia")
public class PhieuGiamGia {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "ma_PGG", length = 50, nullable = false)
    private String maPGG;

    @Column(name = "muc_giam_gia", nullable = false)
    private double mucGiamGia;

    @Column(name = "ngay_bat_dau", nullable = false)
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc", nullable = false)
    private LocalDate ngayKetThuc;

    @Column(name = "dieu_kien_ap_dung", length = 255)
    private String dieuKienApDung;

    @Column(name = "luot_su_dung", nullable = false)
    private int luotSuDung;

    @Column(name = "mo_ta", nullable = false)
    private int moTa;

    // Mối quan hệ nhiều-nhiều giữa room với promotion
    @ToString.Exclude
    @ManyToMany(mappedBy = "dsPhieuGiamGia")
    private Set<Phong> phongs;
}
