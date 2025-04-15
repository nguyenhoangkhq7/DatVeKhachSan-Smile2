package mapper.impl;

import dto.HoaDonDTO;
import mapper.GenericMapper;
import model.HoaDon;
import model.KhachHang;
import model.NhanVien;
import model.PhieuDatPhong;

public class HoaDonMapperImpl implements GenericMapper<HoaDon, HoaDonDTO> {

    @Override
    public HoaDonDTO toDTO(HoaDon entity) {
        if (entity == null) return null;

        return new HoaDonDTO(
                entity.getMaHD(),
                entity.getKhachHang() != null ? entity.getKhachHang().getMaKH() : null,
                entity.getNhanVien() != null ? entity.getNhanVien().getMaNhanVien() : null,
                entity.getPhieuDatPhong() != null ? entity.getPhieuDatPhong().getMaPDP() : null,
                entity.getNgayLapHD(),
                entity.getNgayNhanPhong(),
                entity.getNgayTraPhong(),
                entity.getSoPhongDat()
        );
    }

    @Override
    public HoaDon toEntity(HoaDonDTO dto) {
        if (dto == null) return null;

        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHD(dto.getMaHD());

        // Gán đối tượng KhachHang
        if (dto.getMaKH() != null) {
            KhachHang kh = new KhachHang();
            kh.setMaKH(dto.getMaKH());
            hoaDon.setKhachHang(kh);
        }

        // Gán đối tượng NhanVien
        if (dto.getMaNV() != null) {
            NhanVien nv = new NhanVien();
            nv.setMaNhanVien(dto.getMaNV());
            hoaDon.setNhanVien(nv);
        }

        // Gán đối tượng PhieuDatPhong
        if (dto.getMaPDP() != null) {
            PhieuDatPhong pdp = new PhieuDatPhong();
            pdp.setMaPDP(dto.getMaPDP());
            hoaDon.setPhieuDatPhong(pdp);
        }

        hoaDon.setNgayLapHD(dto.getNgayLapHD());
        hoaDon.setNgayNhanPhong(dto.getNgayNhanPhong());
        hoaDon.setNgayTraPhong(dto.getNgayTraPhong());
        hoaDon.setSoPhongDat(dto.getSoPhongDat());

        return hoaDon;
    }
}
