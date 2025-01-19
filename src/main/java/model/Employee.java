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
@Embeddable
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

    // Thiết lập quan hệ 1-1
    @OneToOne(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Account taiKhoan;
}
