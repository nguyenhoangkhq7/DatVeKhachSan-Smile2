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
@Table(name = "hoa_don") // Tên bảng trong cơ sở dữ liệu
public class HoaDon {

    @Id
    @Column(name = "ma_hd", nullable = false, length = 50)
    private String maHD;

    @Column(name = "ngay_lap_hd", nullable = false)
    private LocalDateTime ngayLapHD;

    @Column(name = "ngay_nhan_phong", nullable = false)
    private LocalDateTime ngayNhanPhong;

    @Column(name = "ngay_tra_phong", nullable = false)
    private LocalDateTime ngayTraPhong;

    @Column(name = "so_luong_dat", nullable = false)
    private int soPhongDat;

    // các thuộc tính tham chiếu
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST) // Thêm cascade
    @JoinColumn(name = "ma_khach_hang")
    private KhachHang khachHang; // Mối quan hệ với KhachHang

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "ma_phieu_dat_phong")
    private PhieuDatPhong phieuDatPhong; // Mối quan hệ với PhieuDatPhong (Đơn Đặt Phòng)

    @ToString.Exclude
    @OneToMany
    @JoinColumn(name = "ma_phieu_dat_dv")
    private Set<PhieuDatDichVu> dsPhieuDatDichVu; // Mối quan hệ với PhieuDatPhong (Đơn Đặt Phòng)

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST) // Thêm cascade
    @JoinColumn(name = "ma_nhan_vien")
    private NhanVien nhanVien; // Mối quan hệ với NhanVien
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
//@Table(name = "hoa_don") // Tên bảng trong cơ sở dữ liệu
//public class HoaDon {
//
//    @Id
//    @Column(name = "ma_hd", nullable = false, length = 50)
//    private String maHD;
//
//    @Column(name = "ngay_lap_hd", nullable = false)
//    private LocalDateTime ngayLapHD;
//
//    @Column(name = "ngay_nhan_phong", nullable = false)
//    private LocalDateTime ngayNhanPhong;
//
//    @Column(name = "ngay_tra_phong", nullable = false)
//    private LocalDateTime ngayTraPhong;
//
//    @Column(name = "so_luong_dat", nullable = false)
//    private int soPhongDat;
//
//    // các thuộc tính tham chiếu
//    @ToString.Exclude
//    @ManyToOne(cascade = CascadeType.ALL) // Thêm cascade
//    @JoinColumn(name = "ma_khach_hang", nullable = false)
//    private KhachHang khachHang; // Mối quan hệ với KhachHang
//
//    @ToString.Exclude
//    @ManyToOne(cascade = CascadeType.ALL) // Thêm cascade
//    @JoinColumn(name = "ma_phieu_dat_phong", nullable = false)
//    private PhieuDatPhong phieuDatPhong; // Mối quan hệ với PhieuDatPhong (Đơn Đặt Phòng)
//
//    @ToString.Exclude
//    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Sử dụng mappedBy
//    private Set<PhieuDatDichVu> dsPhieuDatDichVu; // Mối quan hệ với PhieuDatDichVu
//
//    @ToString.Exclude
//    @ManyToOne(cascade = CascadeType.ALL) // Thêm cascade
//    @JoinColumn(name = "ma_nhan_vien", nullable = false)
//    private NhanVien nhanVien; // Mối quan hệ với NhanVien
//}