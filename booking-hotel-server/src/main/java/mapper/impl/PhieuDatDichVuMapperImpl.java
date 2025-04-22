package mapper.impl;




import dto.PhieuDatDichVuDTO;
import mapper.GenericMapper;
import model.DichVu;
import model.KhachHang;
import model.NhanVien;
import model.PhieuDatDichVu;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PhieuDatDichVuMapperImpl implements GenericMapper<PhieuDatDichVu, PhieuDatDichVuDTO> {

    @Override
    public PhieuDatDichVuDTO toDTO(PhieuDatDichVu entity) {
        if (entity == null) return null;

        // Chuyển Set<DichVu> thành List<String> bằng cách lấy maDV
        List<String> dsMaDV = entity.getDsDichVu() != null
                ? entity.getDsDichVu().stream()
                .map(DichVu::getMaDV)
                .collect(Collectors.toList())
                : null;

        return new PhieuDatDichVuDTO(
                entity.getMaPDDV(),
                entity.getNgayDatDichVu(),
                entity.getSoLuongDichVu(),
                entity.getMoTa(),
                entity.getKhachHang() != null ? entity.getKhachHang().getMaKH() : null,
                entity.getNhanVien() != null ? entity.getNhanVien().getMaNhanVien() : null,
                dsMaDV
        );
    }

    @Override
    public PhieuDatDichVu toEntity(PhieuDatDichVuDTO dto) {
        if (dto == null) return null;

        PhieuDatDichVu phieuDatDichVu = new PhieuDatDichVu();
        phieuDatDichVu.setMaPDDV(dto.getMaPDDV());

        // Gán đối tượng KhachHang (chỉ gán maKH)
        if (dto.getMaKH() != null) {
            KhachHang kh = new KhachHang();
            kh.setMaKH(dto.getMaKH());
            phieuDatDichVu.setKhachHang(kh);
        }

        // Gán đối tượng NhanVien
        if (dto.getMaNV() != null) {
            NhanVien nv = new NhanVien();
            nv.setMaNhanVien(dto.getMaNV());
            phieuDatDichVu.setNhanVien(nv);
        }

        // Chuyển List<String> thành Set<DichVu> bằng cách tạo đối tượng DichVu với maDV
        Set<DichVu> dsMaDV = dto.getDsMaDV() != null
                ? dto.getDsMaDV().stream()
                .map(maDV -> {
                    DichVu dv = new DichVu();
                    dv.setMaDV(maDV);
                    return dv;
                })
                .collect(Collectors.toSet())
                : null;

        phieuDatDichVu.setNgayDatDichVu(dto.getNgayDatDichVu());
        phieuDatDichVu.setSoLuongDichVu(dto.getSoLuongDichVu());
        phieuDatDichVu.setMoTa(dto.getMoTa());
        phieuDatDichVu.setDsDichVu(dsMaDV);

        return phieuDatDichVu;
    }
}
