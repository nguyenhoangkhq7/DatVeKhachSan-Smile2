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
    //Có 5 trạng thái phòng:
    //    0: Còn trống
    //    1: Đã đặt
    //    2: Đang sử dụng
    //    3: Đang dọn dẹp
    //    4: Đang bảo trì
    //    5: Tạm khóa
    private String moTa;
    private int soNguoi;
    private LoaiPhong loaiPhong;
    private Set<PhieuGiamGia> dsPhieuGiamGia;
    private PhieuDatPhong phieuDatPhong;
}
