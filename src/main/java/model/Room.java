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
@Embeddable
public class Room {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "maPhong", length = 50, nullable = false)
    private String maPhong;

    @Column(name = "tenPhong", length = 100, nullable = false)
    private String tenPhong;

    @Column(name = "giaPhong", nullable = false)
    private double giaPhong;

    @Column(name = "tinhTrang", nullable = false)
    private int tinhTrang;

    @Column(name = "moTa", length = 250)
    private String moTa;

    @Column(name = "soNguoi", nullable = false)
    private int soNguoi;

    @ManyToOne
    @JoinColumn(name = "loaiPhong", nullable = false)
    private RoomType loaiPhong;

    // Mối quan hệ nhiều-nhiều giữa room với promotion
    @ManyToMany
    @JoinTable(
            name = "Detail_Room_Promotion",
            joinColumns = @JoinColumn(name = "maPhong"),
            inverseJoinColumns = @JoinColumn(name = "maPGG")
    )
    private Set<Promotion> promotions;
}
