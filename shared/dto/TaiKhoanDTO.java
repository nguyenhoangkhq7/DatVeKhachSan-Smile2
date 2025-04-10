package dto;

import java.io.Serializable;

public class TaiKhoanDTO implements Serializable {
    private String tenDN;
    private String matKhau;

    public TaiKhoanDTO(String tenDN, String matKhau) {
        this.tenDN = tenDN;
        this.matKhau = matKhau;
    }

    public String getTenDN() {
        return tenDN;
    }

    public String getMatKhau() {
        return matKhau;
    }
}
