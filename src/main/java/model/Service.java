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
    @Column(name = "maDV", length = 50, nullable = false, unique = true)
    private String maDV;

    @Column(name = "tenDV", length = 100, nullable = false)
    private String tenDV;

    @Column(name = "donGia", nullable = false)
    private double donGia;

    @Column(name = "donViTinh", nullable = false)
    private String donViTinh;

    @Column(name = "moTa", length = 250)
    private String moTa;

    /*@OneToMany(mappedBy = "dichVu")
    @ToString.Exclude
    private List<ChiTietHoaDon_DichVu> chiTietHoaDonDichVu;*/
}
