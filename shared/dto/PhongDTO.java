package dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PhongDTO {
    @EqualsAndHashCode.Include
    private String maPhong;
    private String tenPhong;
    private double giaPhong;
    private int tinhTrang;
    private int soNguoi;
    private String moTa;
    private String maLoai;

}
