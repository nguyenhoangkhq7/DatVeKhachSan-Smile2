package model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
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
    private KhachHang khachHang; // Mối quan hệ với KhachHang

    @ToString.Exclude
    private DonDatPhong phieuDatPhong; // Mối quan hệ với PhieuDatPhong (Đơn Đặt Phòng)

    @ToString.Exclude
    private Set<DonDatDichVu> dsDonDatDichVu; // Mối quan hệ với PhieuDatPhong (Đơn Đặt Phòng)
}
