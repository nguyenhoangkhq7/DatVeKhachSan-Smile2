package model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "Invoice") // Tên bảng trong cơ sở dữ liệu
public class Invoice {

    @Id
    @Column(name = "MaHD", nullable = false, length = 50)
    private String maHD;

    @Column(name = "NgayLapHD", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date ngayLapHD;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "MaKhachHang", nullable = false)
    private Customer khachHang; // Mối quan hệ với KhachHang

    @Column(name = "NgayNhanPhong", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date ngayNhanPhong;

    @Column(name = "NgayTraPhong", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date ngayTraPhong;

    @Column(name = "SoPhongDat", nullable = false)
    private int soPhongDat;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "MaPDP", nullable = false)
    private Booking phieuDatPhong; // Mối quan hệ với PhieuDatPhong (Đơn Đặt Phòng)

    // Phương thức tính tổng tiền
//    public double tongTien() {
//
//    }
//
//    // Constructor đầy đủ
//    public Invoice(String maHD, Date ngayLapHD, Customer khachHang, Date ngayNhanPhong,
//                   Date ngayTraPhong, int soPhongDat, Booking phieuDatPhong) {
//        this.maHD = maHD;
//        this.ngayLapHD = ngayLapHD;
//        this.khachHang = khachHang;
//        this.ngayNhanPhong = ngayNhanPhong;
//        this.ngayTraPhong = ngayTraPhong;
//        this.soPhongDat = soPhongDat;
//        this.phieuDatPhong = phieuDatPhong;
//    }
}
