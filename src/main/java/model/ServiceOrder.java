package model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "ServiceOrder") // Tên bảng trong cơ sở dữ liệu
public class ServiceOrder {

    @Id
    @Column(name = "MaPDDV", nullable = false, length = 50)
    private String maPDDV;

    @Column(name = "NgayDatDichVu", nullable = false)
    private Date ngayDatDichVu;

    @Column(name = "SoLuongDichVu", nullable = false)
    private int soLuongDichVu;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "MaDonDatPhong", nullable = false)
    private Booking donDatPhong; // Mối quan hệ với Booking (DonDatPhong)

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "MaKhachHang", nullable = false)
    private Customer khachHang; // Mối quan hệ với KhachHang

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "MaNhanVien", nullable = false)
    private Employee nhanVien; // Mối quan hệ với NhanVien

    @Column(name = "MoTa", length = 255)
    private String moTa;

}
