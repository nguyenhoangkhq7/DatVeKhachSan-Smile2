package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString

// mình đổi từ camelCase bên java qua snakecase bên sql nha
              // camelCase
            // snake_case
            // PascalCase
            // KHACH_HANG
@Table(name = "khach_hang")
public class KhachHang {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "ma_kh", length = 50, unique = true, nullable = false)
    private String maKH;

    @Column(name = "ho_ten", length = 100, nullable = false)
    private String hoTen;

    @Column(name = "so_dien_thoai", length = 15, nullable = false)
    private String soDienThoai;

    @Column(name = "so_cccd", length = 12, unique = true, nullable = false)
    private String soCCCD;

    @Column(name = "email", length = 100, nullable = false)
    private String email;
}

