package dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DichVuDTO {
    @EqualsAndHashCode.Include
    private String maDV;
    private String tenDV;
    private double donGia;
    private String donViTinh;
    private String moTa;
}
