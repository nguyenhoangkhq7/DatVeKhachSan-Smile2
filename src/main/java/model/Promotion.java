package model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Table(name = "PhieuGiamGia")
public class Promotion {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "maPGG", length = 50, nullable = false)
    private String maPGG;

    @Column(name = "mucGiamGia", nullable = false)
    private double mucGiamGia;

    @Column(name = "ngayBatDau", nullable = false)
    private Date ngayBatDau;

    @Column(name = "ngayKetThuc", nullable = false)
    private Date ngayKetThuc;

    @Column(name = "dieuKienApDung", length = 255)
    private String dieuKienApDung;

    @Column(name = "luotSuDung", nullable = false)
    private int luotSuDung;

    // Mối quan hệ nhiều-nhiều giữa room với promotion
    @ManyToMany(mappedBy = "promotions")
    private Set<Room> rooms;
}
