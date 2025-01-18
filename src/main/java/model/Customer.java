package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Table(name = "KhachHang")
public class Customer {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "maKH", length = 50, nullable = false)
    private String maKH;

    @Column(name = "hoTen", length = 100, nullable = false)
    private String hoTen;

    @Column(name = "SDT", length = 15, nullable = false)
    private String SDT;

    @Column(name = "soCCCD", length = 12, unique = true, nullable = false)
    private String soCCCD;

    @Column(name = "email", length = 100, nullable = false)
    private String email;
}

