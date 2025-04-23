package model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "nhan_vien")
@Embeddable
public class NhanVien {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "ma_nv", length = 50, unique = true, nullable = false)
    private String maNhanVien;

    @Column(name = "ho_ten", length = 100, nullable = false)
    private String hoTen;

    @Column(name = "chuc_vu")
    private int chucVu;

    @Column(name = "so_dien_thoai", length = 15, nullable = false)
    private String SDT;

    @Column(name = "dia_chi", length = 255)
    private String diaChi;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Column(name = "ngay_vao_lam")
    private LocalDate ngayVaoLam;

    @Column(name = "luong_co_ban")
    private double luongCoBan;

    @Column(name = "he_so_luong")
    private double heSoLuong;

    @Column(name = "trang_thai")
    private int trangThai;

    // các thuộc tính tham chiếu
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ma_tk") // mặc định lấy khóa chính của TaiKhoan làm tên cột
    private TaiKhoan taiKhoan;
}
