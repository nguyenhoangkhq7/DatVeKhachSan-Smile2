package model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Embeddable
@Table(name = "KhachHang")
public class Customer {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "MaKH", length = 50, nullable = false)
    private String maKH;

    @Column(name = "HoTen", length = 100, nullable = false)
    private String hoTen;

    @Column(name = "SDT", length = 15, nullable = false)
    private String SDT;

    @Column(name = "SoCCCD", length = 12, unique = true, nullable = false)
    private String soCCCD;

    @Column(name = "Email", length = 100, nullable = false)
    private String email;
}

