package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
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
    @Column(name = "maLoai", length = 50, nullable = false)
    private String maLoai;

    @Column(name = "tenLoai", length = 100, nullable = false)
    private String tenLoai;

    @Column(name = "moTa", length = 255)
    private String moTa;

    @OneToMany(mappedBy = "loaiPhong")
    private Set<Room> danhSachPhong;
}
