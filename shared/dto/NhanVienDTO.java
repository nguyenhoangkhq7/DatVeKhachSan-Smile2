package dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NhanVienDTO {
    @EqualsAndHashCode.Include
    private String maNhanVien;
    private String hoTen;
    private String email;
    private String sdt;

}

