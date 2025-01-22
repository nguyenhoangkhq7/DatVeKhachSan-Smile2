package model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "phong")
public class Phong {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "ma_phong", length = 50, nullable = false, unique = true)
    private String maPhong;

    @Column(name = "ten_phong", length = 100, nullable = false)
    private String tenPhong;

    @Column(name = "gia_phong", nullable = false)
    private double giaPhong;

    @Column(name = "tinh_trang", nullable = false)
    private int tinhTrang;

    @Column(name = "mo_ta", length = 250)
    private String moTa;

    @Column(name = "so_nguoi", nullable = false)
    private int soNguoi;


    // các thuộc tính tham chiếu
    @ToString.Exclude
    @ManyToOne()
    @JoinColumn(name = "ma_loai_phong")
    private LoaiPhong loaiPhong;

    // có cái bảng phụ là Phong_PGG
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
        name = "phong_pgg",
        joinColumns = @JoinColumn(name = "ma_phong"),
        inverseJoinColumns = @JoinColumn(name = "ma_PGG")
    )
    private Set<PhieuGiamGia> dsPhieuGiamGia;

}
