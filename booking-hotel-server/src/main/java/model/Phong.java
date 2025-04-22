
package model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "phong")
public class Phong {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "ma_phong", length = 50, nullable = false, unique = true)
    private String maPhong;

    @Column(name = "ten_phong", length = 100, nullable = false)
    private String tenPhong;

    @Column(name = "gia_phong", nullable = false)
    private double giaPhong;

    @Column(name = "tinh_trang", nullable = false)
    private int tinhTrang;
    //Có 5 trạng thái phòng:
    //    0: Còn trống
    //    1: Đã đặt
    //    2: Đang sử dụng
    //    3: Đang dọn dẹp
    //    4: Đang bảo trì
    //    5: Tạm khóa

    @Column(name = "mo_ta", length = 250)
    private String moTa;

    @Column(name = "so_nguoi", nullable = false)
    private int soNguoi;

    // các thuộc tính tham chiếu
    @ToString.Exclude
//    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ma_loai_phong")
//    @ManyToOne(fetch = FetchType.EAGER)
    @ManyToOne(fetch = FetchType.LAZY)
    private LoaiPhong loaiPhong;

    // có cái bảng phụ là Phong_PGG
    @ToString.Exclude
//    @ManyToMany
//    @JoinTable(
//            name = "phong_pgg",
//            joinColumns = @JoinColumn(name = "ma_phong"),
//            inverseJoinColumns = @JoinColumn(name = "ma_PGG")
//    )
//    private Set<PhieuGiamGia> dsPhieuGiamGia;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "phong_pgg",
            joinColumns = @JoinColumn(name = "ma_phong"),
            inverseJoinColumns = @JoinColumn(name = "ma_PGG")
    )
    private Set<PhieuGiamGia> dsPhieuGiamGia;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "ma_ddp") // Thêm mối quan hệ với PhieuDatPhong
    private PhieuDatPhong phieuDatPhong; // Thêm thuộc tính này để liên kết với PhieuDatPhong
}
