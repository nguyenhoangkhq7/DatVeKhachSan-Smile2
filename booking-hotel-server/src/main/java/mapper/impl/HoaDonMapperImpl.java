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

        HoaDonDTO dto = new HoaDonDTO();
        dto.setMaHD(entity.getMaHD());
        dto.setNgayLapHD(entity.getNgayLapHD());
        dto.setNgayNhanPhong(entity.getNgayNhanPhong());
        dto.setNgayTraPhong(entity.getNgayTraPhong());
        dto.setSoPhongDat(entity.getSoPhongDat());

        if (entity.getKhachHang() != null) {
            dto.setMaKH(entity.getKhachHang().getMaKH());
        }
        if (entity.getNhanVien() != null) {
            dto.setMaNV(entity.getNhanVien().getMaNhanVien());
        }
        if (entity.getPhieuDatPhong() != null) {
            dto.setMaPDP(entity.getPhieuDatPhong().getMaPDP());
        }

        return dto;
    }

    @Override
    public HoaDon toEntity(HoaDonDTO dto) {
        if (dto == null) return null;

        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHD(dto.getMaHD());
        hoaDon.setNgayLapHD(dto.getNgayLapHD());
        hoaDon.setNgayNhanPhong(dto.getNgayNhanPhong());
        hoaDon.setNgayTraPhong(dto.getNgayTraPhong());
        hoaDon.setSoPhongDat(dto.getSoPhongDat());

        if (dto.getMaKH() != null) {
            KhachHang khachHang = new KhachHang();
            khachHang.setMaKH(dto.getMaKH());
            hoaDon.setKhachHang(khachHang);
        }
        if (dto.getMaNV() != null) {
            NhanVien nhanVien = new NhanVien();
            nhanVien.setMaNhanVien(dto.getMaNV());
            hoaDon.setNhanVien(nhanVien);
        }
        if (dto.getMaPDP() != null) {
            PhieuDatPhong phieuDatPhong = new PhieuDatPhong();
            phieuDatPhong.setMaPDP(dto.getMaPDP());
            hoaDon.setPhieuDatPhong(phieuDatPhong);
        }


        return hoaDon;
    }
}
