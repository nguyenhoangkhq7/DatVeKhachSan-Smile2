package mapper.impl;

import dto.PhieuDatPhongDTO;
import mapper.GenericMapper;
import model.KhachHang;
import model.NhanVien;
import model.PhieuDatPhong;
import model.Phong;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PhieuDatPhongMapperImpl implements GenericMapper<PhieuDatPhong, PhieuDatPhongDTO> {

    @Override
    public PhieuDatPhongDTO toDTO(PhieuDatPhong entity) {
        if (entity == null) return null;
        return new PhieuDatPhongDTO(
                entity.getMaPDP(),
                entity.getKhachHang() != null ? entity.getKhachHang().getMaKH() : null,
                entity.getNhanVien() != null ? entity.getNhanVien().getMaNhanVien() : null,
                entity.getDsPhong() != null
                        ? entity.getDsPhong().stream()
                        .filter(phong -> phong != null && phong.getMaPhong() != null)
                        .map(Phong::getMaPhong)
                        .collect(Collectors.toList())
                        : Collections.emptyList(),
                entity.getNgayDatPhong(),
                entity.getNgayNhanPhongDuKien(),
                entity.getNgayTraPhongDuKien()
        );
    }

    @Override
    public PhieuDatPhong toEntity(PhieuDatPhongDTO dto) {
        if (dto == null) return null;

        PhieuDatPhong phieu = new PhieuDatPhong();
        phieu.setMaPDP(dto.getMaPDP());

        if (dto.getMaKH() != null) {
            KhachHang khachHang = new KhachHang();
            khachHang.setMaKH(dto.getMaKH());
            phieu.setKhachHang(khachHang);
        }

        if (dto.getMaNV() != null) {
            NhanVien nhanVien = new NhanVien();
            nhanVien.setMaNhanVien(dto.getMaNV());
            phieu.setNhanVien(nhanVien);
        }

        if (dto.getDsMaPhong() != null) {
            Set<Phong> dsPhong = dto.getDsMaPhong().stream()
                    .filter(ma -> ma != null && !ma.isEmpty())
                    .map(ma -> {
                        Phong p = new Phong();
                        p.setMaPhong(ma);
                        return p;
                    })
                    .collect(Collectors.toSet());
            phieu.setDsPhong(dsPhong);
        }

        phieu.setNgayDatPhong(dto.getNgayDatPhong());
        phieu.setNgayNhanPhongDuKien(dto.getNgayNhanPhongDuKien());
        phieu.setNgayTraPhongDuKien(dto.getNgayTraPhongDuKien());

        return phieu;
    }
}