package model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "don_dat_dich_vu")
public class PhieuDatDichVu {

    @Id
    @Column(name = "ma_phieu_dat_dv", nullable = false, length = 50)
    private String maPDDV;

    @Column(name = "ngay_dat_dv", nullable = false)
    private LocalDateTime ngayDatDichVu;

    @Column(name = "so_luong_dv", nullable = false)
    private int soLuongDichVu;


    @Column(name = "mo_ta", length = 255)
    private String moTa;

    // các thuộc tính tham chiếu

    @ToString.Exclude
//    @ManyToOne
//    @JoinColumn(name = "ma_nhan_vien")
//    private NhanVien nhanVien;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ma_nhan_vien")
    private NhanVien nhanVien;

    @ToString.Exclude
//    @ManyToOne // owner relationship
//    @JoinColumn(name = "ma_khach_hang")
//    private KhachHang khachHang;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ma_khach_hang")
    private KhachHang khachHang;

    @ToString.Exclude
    @OneToMany
    @JoinColumn(name = "ma_phieu_dat_dv")
    private Set<DichVu> dsDichVu;



}
//package model;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//import java.util.Set;
//
//@Getter
//@Setter
//@ToString
//@NoArgsConstructor
//@Entity
//@Table(name = "phieu_dat_dich_vu")
//public class PhieuDatDichVu {
//
//    @Id
//    @Column(name = "ma_pddv", nullable = false, length = 50)
//    private String maPDDV;
//
//    @Column(name = "ngay_dat_dich_vu", nullable = false)
//    private LocalDateTime ngayDatDichVu;
//
//    @Column(name = "so_luong_dich_vu", nullable = false)
//    private int soLuongDichVu;
//
//    @Column(name = "mo_ta")
//    private String moTa;
//
//
//
//    @ToString.Exclude
//    @ManyToOne
//    @JoinColumn(name = "ma_nhan_vien", nullable = false)
//    private NhanVien nhanVien;
//
//    @ToString.Exclude
//    @ManyToOne
//    @JoinColumn(name = "ma_khach_hang", nullable = false)
//    private KhachHang khachHang;
//    @ToString.Exclude
//    @ManyToOne
//    @JoinColumn(name = "ma_hd", nullable = false)
//    private HoaDon hoaDon; // Thêm thuộc tính này để tham chiếu đến HoaDon
//
//    @ToString.Exclude
//    @OneToMany(mappedBy = "ma_phieu_dat_dv", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Set<DichVu> dsDichVu; // Mối quan hệ với DichVu
//}
