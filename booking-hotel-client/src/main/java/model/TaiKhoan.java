package model;

public class TaiKhoan {

    private String tenDN;
    private String matKhau;

    public TaiKhoan() {
    }

    public String getTenDN() {
        return tenDN;
    }

    public void setTenDN(String tenDN) {
        this.tenDN = tenDN;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "tenDN='" + tenDN + '\'' +
                ", matKhau='******'" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaiKhoan)) return false;

        TaiKhoan taiKhoan = (TaiKhoan) o;

        return tenDN != null ? tenDN.equals(taiKhoan.tenDN) : taiKhoan.tenDN == null;
    }

    @Override
    public int hashCode() {
        return tenDN != null ? tenDN.hashCode() : 0;
    }
}
