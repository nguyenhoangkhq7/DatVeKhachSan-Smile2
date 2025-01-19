package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "TaiKhoan")
public class Account {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "tenDN", length = 50, nullable = false)
    private String tenDN;

    @Column(name = "matKhau", nullable = false)
    private String matKhau;

    // Thiết lập quan hệ 1-1
    @OneToOne
    @JoinColumn(name = "maNhanVien", referencedColumnName = "maNhanVien", unique = true)
    private Employee nhanVien;
}



