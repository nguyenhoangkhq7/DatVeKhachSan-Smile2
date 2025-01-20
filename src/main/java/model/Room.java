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
@ToString
@Table(name = "Phong")
public class Room {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "MaPhong", length = 50, nullable = false)
    private String maPhong;

    @Column(name = "TenPhong", length = 100, nullable = false)
    private String tenPhong;

    @Column(name = "GiaPhong", nullable = false)
    private double giaPhong;

    @Column(name = "TinhTrang", nullable = false)
    private int tinhTrang;

    @Column(name = "MoTa", length = 250)
    private String moTa;

    @Column(name = "SoNguoi", nullable = false)
    private int soNguoi;

    @ManyToOne
    @JoinColumn(name = "LoaiPhong", nullable = false)
    private RoomType loaiPhong;

    @ManyToOne
    @JoinColumn(name = "MaPDP", nullable = false)
    private Booking booking;

    // Mối quan hệ nhiều-nhiều giữa phòng và khuyến mãi
    @ManyToMany
    @JoinTable(
            name = "Detail_Room_Promotion",
            joinColumns = @JoinColumn(name = "MaPhong"),
            inverseJoinColumns = @JoinColumn(name = "MaPGG")
    )
    private Set<Promotion> promotions;
}
