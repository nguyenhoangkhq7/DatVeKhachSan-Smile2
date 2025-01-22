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
@Table(name = "loai_phong")
public class LoaiPhong {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "ma_loai", length = 50, nullable = false)
    private String maLoai;

    @Column(name = "ten_loai", length = 100, nullable = false)
    private String tenLoai;

    @Column(name = "mo_ta", length = 255)
    private String moTa;

}
