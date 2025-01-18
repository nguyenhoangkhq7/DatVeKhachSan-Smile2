package model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "NhanVien")
public class Employee {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "maNhanVien", length = 50, nullable = false)
    private String maNhanVien;

    @Column(name = "hoTen", length = 100, nullable = false)
    private String hoTen;

    @Column(name = "chucVu")
    private int chucVu;

    @Column(name = "SDT", length = 15, nullable = false)
    private String SDT;

    @Column(name = "diaChi", length = 255)
    private String diaChi;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "ngaySinh")
    private Date ngaySinh;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tenDN", referencedColumnName = "tenDN")
    private Account taiKhoan;
}

