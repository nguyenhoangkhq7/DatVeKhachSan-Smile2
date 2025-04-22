package model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "phieu_dat_phong")
public class PhieuDatPhong {

    @Id
    @Column(name = "ma_pdp", nullable = false, length = 50)
    private String maPDP;

    @Column(name = "ngay_dat_phong", nullable = false)
    private LocalDate ngayDatPhong;

    @Column(name = "ngay_nhan_phong_du_kien", nullable = false)
    private LocalDate ngayNhanPhongDuKien;

    @Column(name = "ngay_tra_phong_du_kien", nullable = false)
    private LocalDate ngayTraPhongDuKien;

    // Quan hệ với Khách hàng
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_kh")
    private KhachHang khachHang;

    // Quan hệ với Nhân viên
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nv")
    private NhanVien nhanVien;

    // Quan hệ với Phòng (many-to-many)
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "chi_tiet_dat_phong",
            joinColumns = @JoinColumn(name = "ma_pdp"),
            inverseJoinColumns = @JoinColumn(name = "ma_phong")
    )
    private Set<Phong> phongs;


    // Quan hệ với Hóa đơn
    @ToString.Exclude
    @OneToOne(mappedBy = "phieuDatPhong", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private HoaDon hoaDon;

    // Quan hệ với Phiếu đặt dịch vụ
    @ToString.Exclude
    @OneToMany(mappedBy = "phieuDatPhong", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhieuDatDichVu> phieuDatDichVus;
}