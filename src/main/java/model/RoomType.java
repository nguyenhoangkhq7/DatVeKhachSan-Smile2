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
@Table(name = "LoaiPhong")
public class RoomType {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "MaLoai", length = 50, nullable = false)
    private String maLoai;

    @Column(name = "TenLoai", length = 100, nullable = false)
    private String tenLoai;

    @Column(name = "MoTa", length = 255)
    private String moTa;

    @OneToMany(mappedBy = "loaiPhong")
    private Set<Room> danhSachPhong;
}
