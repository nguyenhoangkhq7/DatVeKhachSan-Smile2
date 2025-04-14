package mapper.impl;

import dto.DichVuDTO;
import mapper.GenericMapper;
import model.DichVu;

public class DichVuMapperImpl implements GenericMapper<DichVu, DichVuDTO> {

    @Override
    public DichVuDTO toDTO(DichVu entity) {
        if (entity == null) return null;
        return new DichVuDTO(
                entity.getMaDV(),
                entity.getTenDV(),
                entity.getDonGia(),
                entity.getDonViTinh(),
                entity.getMoTa()
        );
    }

    @Override
    public DichVu toEntity(DichVuDTO dto) {
        if (dto == null) return null;
        DichVu dichVu = new DichVu();
        dichVu.setMaDV(dto.getMaDV());
        dichVu.setTenDV(dto.getTenDV());
        dichVu.setDonGia(dto.getDonGia());
        dichVu.setDonViTinh(dto.getDonViTinh());
        dichVu.setMoTa(dto.getMoTa());
        return dichVu;
    }
}
