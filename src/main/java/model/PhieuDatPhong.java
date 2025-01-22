/*
    *QLKS_smile2  day creative: 1/19/2025
    version: 2023.2  IntelliJ IDEA
    author: Nguyễn Hoàng Khang  */
    /*
       *class description:
            write description right here   
     */


package model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

//getter setter tostring hashcode equal
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"maPDP"})
@NoArgsConstructor

@Entity
public class PhieuDatPhong {

    @Id
    @Column(name = "ma_ddp", nullable = false)
    private String maPDP;

    @Column(name = "ngay_dat_phong", nullable = false)
    private LocalDateTime ngayDatPhong;

    @Column(name = "ngay_nhan_phong_du_kien", nullable = false)
    private LocalDateTime ngayNhanPhongDuKien;

    @Column(name = "ngay_tra_phong_du_kien", nullable = false)
    private LocalDateTime ngayTraPhongDuKien;

    // các thuộc tính tham chiếu
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "ma_khach_hang")
    private KhachHang khachHang;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "ma_nhan_vien")
    private NhanVien nhanVien;

    @ToString.Exclude
    @OneToMany
    @JoinColumn(name = "ma_phong")
    private Set<Phong> dsPhong;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "ma_hd")
    private HoaDon hoaDon;
}
