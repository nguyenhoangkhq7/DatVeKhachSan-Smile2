package model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class KhachHang {
    @EqualsAndHashCode.Include
    private String maKH;
    private String hoTen;
    private String soDienThoai;
    private String soCCCD;
    private String email;

}
