package mapper.impl;

import dto.NhanVienDTO;
import mapper.GenericMapper;
import model.NhanVien;

public class NhanVienMapperImpl implements GenericMapper<NhanVien, NhanVienDTO> {

//    @Override
//    public NhanVienDTO toDTO(NhanVien entity) {
//        return null;
//    }
//
//    @Override
//    public NhanVien toEntity(NhanVienDTO dto) {
//        return null;
//    }
@Override
public NhanVienDTO toDTO(NhanVien entity) {
    if (entity == null) return null;
    return new NhanVienDTO(
            entity.getMaNhanVien(),
            entity.getHoTen(),
            entity.getChucVu(),
            entity.getSDT(),
            entity.getDiaChi(),
            entity.getEmail(),
            entity.getNgaySinh(),
            entity.getNgayVaoLam(),
            entity.getLuongCoBan(),
            entity.getHeSoLuong()
    );
}

    @Override
    public NhanVien toEntity(NhanVienDTO dto) {
        if (dto == null) return null;
        NhanVien nhanVien = new NhanVien();
        nhanVien.setMaNhanVien(dto.getMaNhanVien());
        nhanVien.setHoTen(dto.getHoTen());
        nhanVien.setChucVu(dto.getChucVu());
        nhanVien.setSDT(dto.getSDT());
        nhanVien.setDiaChi(dto.getDiaChi());
        nhanVien.setEmail(dto.getEmail());
        nhanVien.setNgaySinh(dto.getNgaySinh());
        nhanVien.setNgayVaoLam(dto.getNgayVaoLam());
        return nhanVien;
    }
}
