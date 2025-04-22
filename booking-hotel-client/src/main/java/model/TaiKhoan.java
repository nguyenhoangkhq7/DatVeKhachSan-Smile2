package model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TaiKhoan {
    @EqualsAndHashCode.Include
    private String tenDN;
    private String matKhau;
}
