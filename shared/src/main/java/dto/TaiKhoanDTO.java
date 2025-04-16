package dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TaiKhoanDTO {
    @EqualsAndHashCode.Include
    private String tenDN;
    private String matKhau;
}
