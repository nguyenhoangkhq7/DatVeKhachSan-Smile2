package dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LoaiPhongDTO {
    @EqualsAndHashCode.Include
    private String maLoai;
    private String tenLoai;
    private String moTa;

}

