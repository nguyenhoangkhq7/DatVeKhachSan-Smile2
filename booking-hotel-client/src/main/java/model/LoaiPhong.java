package model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LoaiPhong {
    @EqualsAndHashCode.Include
    private String maLoai;
    private String tenLoai;
    private String moTa;

}
