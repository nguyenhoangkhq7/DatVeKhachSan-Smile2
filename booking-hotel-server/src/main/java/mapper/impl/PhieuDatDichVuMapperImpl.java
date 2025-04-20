package mapper.impl;

import dto.PhieuDatDichVuDTO;
import model.*;
import mapper.GenericMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PhieuDatDichVuMapperImpl implements GenericMapper<PhieuDatDichVu, PhieuDatDichVuDTO> {

    @Override
    public PhieuDatDichVuDTO toDTO(PhieuDatDichVu entity) {
        if (entity == null) return null;

        PhieuDatDichVuDTO dto = new PhieuDatDichVuDTO();
        dto.setMaPDDV(entity.getMaPDDV());
        dto.setNgayDatDichVu(entity.getNgayDatDichVu());
        dto.setSoLuongDichVu(entity.getSoLuongDichVu());
        dto.setMoTa(entity.getMoTa());

        if (entity.getKhachHang() != null)
            dto.setMaKH(entity.getKhachHang().getMaKH());

        if (entity.getNhanVien() != null)
            dto.setMaNV(entity.getNhanVien().getMaNhanVien());

        if (entity.getPhieuDatPhong() != null)
            dto.setMaPDP(entity.getPhieuDatPhong().getMaPDP());

        if (entity.getHoaDon() != null)
            dto.setMaHD(entity.getHoaDon().getMaHD());

        if (entity.getDichVus() != null && !entity.getDichVus().isEmpty()) {
            List<String> dsMaDV = entity.getDichVus().stream()
                    .map(DichVu::getMaDV)
                    .collect(Collectors.toList());
            dto.setDsMaDV(dsMaDV);
        }

        return dto;
    }

    @Override
    public PhieuDatDichVu toEntity(PhieuDatDichVuDTO dto) {
        if (dto == null) return null;

        PhieuDatDichVu entity = new PhieuDatDichVu();
        entity.setMaPDDV(dto.getMaPDDV());
        entity.setNgayDatDichVu(dto.getNgayDatDichVu());
        entity.setSoLuongDichVu(dto.getSoLuongDichVu());
        entity.setMoTa(dto.getMoTa());

        if (dto.getMaKH() != null) {
            KhachHang kh = new KhachHang();
            kh.setMaKH(dto.getMaKH());
            entity.setKhachHang(kh);
        }

        if (dto.getMaNV() != null) {
            NhanVien nv = new NhanVien();
            nv.setMaNhanVien(dto.getMaNV());
            entity.setNhanVien(nv);
        }

        if (dto.getMaPDP() != null) {
            PhieuDatPhong pdp = new PhieuDatPhong();
            pdp.setMaPDP(dto.getMaPDP());
            entity.setPhieuDatPhong(pdp);
        }

        if (dto.getMaHD() != null) {
            HoaDon hd = new HoaDon();
            hd.setMaHD(dto.getMaHD());
            entity.setHoaDon(hd);
        }

        if (dto.getDsMaDV() != null && !dto.getDsMaDV().isEmpty()) {
            Set<DichVu> dsDichVu = dto.getDsMaDV().stream().map(maDV -> {
                DichVu dv = new DichVu();
                dv.setMaDV(maDV);
                return dv;
            }).collect(Collectors.toSet());
            entity.setDichVus(dsDichVu);
        }

        return entity;
    }
}

