//package model;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@NoArgsConstructor
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//@Getter
//@Setter
//@ToString
//@Table(name = "dich_vu")
//public class DichVu {
//
//    @Id
//    @EqualsAndHashCode.Include
//    @Column(name = "ma_dv", length = 50, nullable = false, unique = true)
//    private String maDV;
//
//    @Column(name = "ten_dv", length = 100, nullable = false)
//    private String tenDV;
//
//    @Column(name = "don_gia", nullable = false)
//    private double donGia;
//
//    @Column(name = "don_vi_tinh", nullable = false)
//    private String donViTinh;
//
//    @Column(name = "mo_ta", length = 250)
//    private String moTa;
//
//}

package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@ToString
@Table(name = "dich_vu")
public class DichVu {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "ma_dv", length = 50, nullable = false, unique = true)
    private String maDV;

    @Column(name = "ten_dv", length = 100, nullable = false)
    private String tenDV;

    @Column(name = "don_gia", nullable = false)
    private double donGia;

    @Column(name = "don_vi_tinh", nullable = false)
    private String donViTinh;

    @Column(name = "mo_ta", length = 250)
    private String moTa;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "ma_pddv") // Tham chiếu đến PhieuDatDichVu
    private PhieuDatDichVu phieuDatDichVu; // Thêm thuộc tính này để tham chiếu đến PhieuDatDichVu
}