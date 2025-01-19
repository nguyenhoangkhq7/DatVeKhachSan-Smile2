/*
    *QLKS_smile2  day creative: 1/19/2025
    version: 2023.2  IntelliJ IDEA
    author: Nguyễn Hoàng Khang  */
    /*
       *class description:
            write description right here   
     */


package model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

//getter setter tostring hashcode equal
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"maPDP"})
@NoArgsConstructor

@Entity
public class Booking {

    @Id
    @Column(name = "MaPDP", nullable = false)
    private String maPDP;

    @Column(name = "NgayDatPhong", nullable = false)
    private LocalDateTime ngayDatPhong;

    @Column(name = "NgayNhanPhongDuKien", nullable = false)
    private LocalDateTime ngayNhanPhongDuKien;

    @Column(name = "NgayTraPhongDuKien", nullable = false)
    private LocalDateTime ngayTraPhongDuKien;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "maKH", nullable = false)
    private Customer khachHang;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "maNhanVien", nullable = false)
    private Employee nhanVien;

//  Mối quan hệ giữa booking với room
//  (1 nhiều nhưng bên phía room không có tham chiếu ngược lại đơn đặt phòng)
    @ToString.Exclude
    @OneToMany(mappedBy = "loaiPhong", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Room> dsPhong;
}
