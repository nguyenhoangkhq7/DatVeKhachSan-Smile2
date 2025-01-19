package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    @JoinColumn(name = "maLoai", nullable = false)
    private RoomType loaiPhong;
}
