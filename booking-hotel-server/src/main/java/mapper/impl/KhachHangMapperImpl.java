package mapper.impl;


import dto.KhachHangDTO;
import mapper.GenericMapper;
import model.KhachHang;

public class KhachHangMapperImpl implements GenericMapper<KhachHang, KhachHangDTO> {

    @Override
    public KhachHangDTO toDTO(KhachHang entity) {
        if (entity == null) return null;
        return  new KhachHangDTO(
                entity.getMaKH(),
                entity.getHoTen(),
                entity.getSoDienThoai(),
                entity.getSoCCCD(),
                entity.getEmail()
<<<<<<< HEAD
=======

>>>>>>> main
        );
    }

   @Override
    public KhachHang toEntity(KhachHangDTO dto){
        if (dto == null) return null;
        KhachHang khachHang = new KhachHang();
        khachHang.setMaKH(dto.getMaKH());
        khachHang.setHoTen(dto.getHoTen());
        khachHang.setSoDienThoai(dto.getSoDienThoai());
        khachHang.setSoCCCD(dto.getSoCCCD());
        khachHang.setEmail(dto.getEmail());
        return khachHang;
    }
}


