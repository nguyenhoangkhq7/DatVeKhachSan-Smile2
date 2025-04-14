package model;

import java.util.Set;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Phong {
    @EqualsAndHashCode.Include
    private String maPhong;
    private String tenPhong;
    private double giaPhong;
    private int tinhTrang;
    private String moTa;
    private int soNguoi;
    private LoaiPhong loaiPhong;
    private Set<PhieuGiamGia> dsPhieuGiamGia;
    private PhieuDatPhong phieuDatPhong;
}
