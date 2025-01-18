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
public class Account{

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "tenDN", length = 50, nullable = false)
    private String tenDN;

    @Column(name = "matKhau", nullable = false)
    private String matKhau;

    @OneToOne(mappedBy = "taiKhoan")
    private Employee nhanVien;
}


