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
@Table(name = "NhanVien")
@Embeddable
public class Employee {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "MaNhanVien", length = 50, nullable = false)
    private String maNhanVien;

    @Column(name = "HoTen", length = 100, nullable = false)
    private String hoTen;

    @Column(name = "ChucVu")
    private int chucVu;

    @Column(name = "SDT", length = 15, nullable = false)
    private String SDT;

    @Column(name = "DiaChi", length = 255)
    private String diaChi;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "NgaySinh")
    private LocalDate ngaySinh;

    // Thiết lập quan hệ 1-1
    @OneToOne
    @JoinColumn(name = "tenDN", nullable = false)
    @ToString.Exclude
    private Account taiKhoan;
}
