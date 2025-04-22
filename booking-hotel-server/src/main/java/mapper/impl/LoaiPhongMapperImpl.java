
package mapper.impl;

import dto.LoaiPhongDTO;
import mapper.GenericMapper;
import model.LoaiPhong;

public class LoaiPhongMapperImpl implements GenericMapper<LoaiPhong, LoaiPhongDTO> {

    @Override
    public LoaiPhongDTO toDTO(LoaiPhong entity) {
        if (entity == null) return null;

        return new LoaiPhongDTO(
                entity.getMaLoai(),
                entity.getTenLoai(),
                entity.getMoTa()
        );
    }

    @Override
    public LoaiPhong toEntity(LoaiPhongDTO dto) {
        if (dto == null) return null;

        LoaiPhong entity = new LoaiPhong();
        entity.setMaLoai(dto.getMaLoai());
        entity.setTenLoai(dto.getTenLoai());
        entity.setMoTa(dto.getMoTa());

        return entity;
    }
}
