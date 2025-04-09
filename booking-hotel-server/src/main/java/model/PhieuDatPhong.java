package model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    private LocalDate ngayDatPhong; // Sử dụng LocalDate

    @Column(name = "ngay_nhan_phong_du_kien", nullable = false)
    private LocalDate ngayNhanPhongDuKien; // Sử dụng LocalDate

    @Column(name = "ngay_tra_phong_du_kien", nullable = false)
    private LocalDate ngayTraPhongDuKien; // Sử dụng LocalDate

    // các thuộc tính tham chiếu
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ma_khach_hang")
    private KhachHang khachHang;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ma_nhan_vien")
    private NhanVien nhanVien;

    @ToString.Exclude
    @OneToMany(mappedBy = "phieuDatPhong") // Thêm mappedBy để chỉ định mối quan hệ
    private Set<Phong> dsPhong;

    @ToString.Exclude
    @OneToOne(cascade = CascadeType.PERSIST) // Thêm cascade
    @JoinColumn(name = "ma_hd")
    private HoaDon hoaDon; // Mối quan hệ với HoaDon
}