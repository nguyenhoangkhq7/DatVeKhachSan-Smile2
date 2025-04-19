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

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    // Quan hệ với Nhân viên
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nhan_vien", nullable = false)
    private NhanVien nhanVien;

    // Quan hệ với Khách hàng
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_kh")
    private KhachHang khachHang;

    // Quan hệ với Hóa đơn
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_hd")
    private HoaDon hoaDon;

    // Quan hệ với Phiếu đặt phòng
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_pdp", nullable = false)
    private PhieuDatPhong phieuDatPhong;

    // Quan hệ với Dịch vụ (many-to-many)
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "chi_tiet_dat_dich_vu",
            joinColumns = @JoinColumn(name = "ma_pddv"),
            inverseJoinColumns = @JoinColumn(name = "ma_dv")
    )
    private Set<DichVu> dichVus;
}