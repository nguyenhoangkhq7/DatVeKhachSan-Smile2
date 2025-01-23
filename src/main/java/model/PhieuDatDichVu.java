
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
@Table(name = "phieu_dat_dich_vu")
public class PhieuDatDichVu {

    @Id
    @Column(name = "ma_pddv", nullable = false, length = 50)
    private String maPDDV;

    @Column(name = "ngay_dat_dich_vu", nullable = false)
    private LocalDateTime ngayDatDichVu;

    @Column(name = "so_luong_dich_vu", nullable = false)
    private int soLuongDichVu;

    @Column(name = "mo_ta")
    private String moTa;



    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ma_nhan_vien", nullable = false)
    private NhanVien nhanVien;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ma_kh")
    private KhachHang khachHang;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ma_hd")
    private HoaDon hoaDon; // Thêm thuộc tính này để tham chiếu đến HoaDon

    @ToString.Exclude
    @OneToMany(mappedBy = "phieuDatDichVu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DichVu> dsDichVu; // Mối quan hệ với DichVu
}
