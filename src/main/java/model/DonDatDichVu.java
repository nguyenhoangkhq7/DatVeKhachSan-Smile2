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
@Table(name = "don_dat_dich_vu")
public class DonDatDichVu {

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
    private DonDatPhong donDatPhong;

    @ToString.Exclude
    private NhanVien nhanVien;

    @ToString.Exclude
    private KhachHang khachHang;




}
