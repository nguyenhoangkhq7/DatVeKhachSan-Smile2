package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tai_khoan")
public class TaiKhoan {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "ten_dn", length = 50, nullable = false)
    private String tenDN;

    @Column(name = "mat_khau", nullable = false)
    private String matKhau;
}



