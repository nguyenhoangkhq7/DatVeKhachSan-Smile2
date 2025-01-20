package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@ToString
@Table(name = "DichVu")
public class Service {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "MaDV", length = 50, nullable = false, unique = true)
    private String maDV;

    @Column(name = "TenDV", length = 100, nullable = false)
    private String tenDV;

    @Column(name = "DonGia", nullable = false)
    private double donGia;

    @Column(name = "DonViTinh", nullable = false)
    private String donViTinh;

    @Column(name = "MoTa", length = 250)
    private String moTa;

    /*@OneToMany(mappedBy = "DichVu")
    @ToString.Exclude
    private List<ChiTietHoaDon_DichVu> chiTietHoaDonDichVu;*/
}
