package mapper.impl;

import dto.PhongDTO;
import mapper.GenericMapper;
import model.Phong;

public class PhongMapperImpl implements GenericMapper<Phong, PhongDTO> {

    @Override
    public PhongDTO toDTO(Phong entity) {
        if (entity == null) return null;

        return new PhongDTO(
                entity.getMaPhong(),
                entity.getTenPhong(),
                entity.getGiaPhong(),
                entity.getTinhTrang(),
                entity.getSoNguoi(),
                entity.getMoTa(),
                entity.getLoaiPhong() != null ? entity.getLoaiPhong().getMaLoai() : null

        );
    }

    @Override
    public Phong toEntity(PhongDTO dto) {
        if (dto == null) return null;

        Phong phong = new Phong();
        phong.setMaPhong(dto.getMaPhong());
        phong.setTenPhong(dto.getTenPhong());
        phong.setGiaPhong(dto.getGiaPhong());
        phong.setTinhTrang(dto.getTinhTrang());
        phong.setMoTa(dto.getMoTa());
        phong.setSoNguoi(dto.getSoNguoi());

        // Nếu bạn có LoaiPhongDAO thì có thể fetch từ CSDL
        if (dto.getMaLoai() != null) {
            model.LoaiPhong loaiPhong = new model.LoaiPhong();
            loaiPhong.setMaLoai(dto.getMaLoai());
            phong.setLoaiPhong(loaiPhong);
        }

        // Có thể set thêm dsPhieuGiamGia và phieuDatPhong nếu cần

        return phong;
    }
}
