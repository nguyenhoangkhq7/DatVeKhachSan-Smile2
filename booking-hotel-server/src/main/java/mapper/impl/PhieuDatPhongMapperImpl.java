package mapper.impl;

import dto.PhieuDatPhongDTO;
import jakarta.persistence.EntityManager;
import mapper.GenericMapper;
import model.HoaDon;
import model.KhachHang;
import model.NhanVien;
import model.PhieuDatPhong;
import model.Phong;
import utils.HibernateUtil;

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
                entity.getPhongs() != null
                        ? entity.getPhongs().stream()
                        .filter(phong -> phong != null && phong.getMaPhong() != null)
                        .map(Phong::getMaPhong)
                        .collect(Collectors.toList())
                        : Collections.emptyList(),
                entity.getNgayDatPhong(),
                entity.getNgayNhanPhongDuKien(),
                entity.getNgayTraPhongDuKien(),
                entity.getHoaDon() != null ? entity.getHoaDon().getMaHD() : null // Ánh xạ maHD
        );
    }

    public PhieuDatPhong toEntity(PhieuDatPhongDTO dto) {
        if (dto == null) return null;

        PhieuDatPhong phieu = new PhieuDatPhong();
        phieu.setMaPDP(dto.getMaPDP());

        // Tải KhachHang từ cơ sở dữ liệu
        if (dto.getMaKH() != null) {
            EntityManager em = HibernateUtil.getEntityManager();
            try {
                KhachHang khachHang = em.find(KhachHang.class, dto.getMaKH());
                if (khachHang == null) {
                    throw new IllegalArgumentException("Khách hàng với mã " + dto.getMaKH() + " không tồn tại");
                }
                phieu.setKhachHang(khachHang);
            } finally {
                em.close();
            }
        }

        // Tải NhanVien từ cơ sở dữ liệu
        if (dto.getMaNV() != null) {
            EntityManager em = HibernateUtil.getEntityManager();
            try {
                NhanVien nhanVien = em.find(NhanVien.class, dto.getMaNV());
                if (nhanVien == null) {
                    throw new IllegalArgumentException("Nhân viên với mã " + dto.getMaNV() + " không tồn tại");
                }
                phieu.setNhanVien(nhanVien);
            } finally {
                em.close();
            }
        }

        // Tải Phong từ cơ sở dữ liệu
        if (dto.getDsMaPhong() != null) {
            EntityManager em = HibernateUtil.getEntityManager();
            try {
                Set<Phong> phongs = dto.getDsMaPhong().stream()
                        .filter(ma -> ma != null && !ma.isEmpty())
                        .map(ma -> {
                            Phong p = em.find(Phong.class, ma);
                            if (p == null) {
                                throw new IllegalArgumentException("Phòng với mã " + ma + " không tồn tại");
                            }
                            return p;
                        })
                        .collect(Collectors.toSet());
                phieu.setPhongs(phongs);
            } finally {
                em.close();
            }
        }

        phieu.setNgayDatPhong(dto.getNgayDatPhong());
        phieu.setNgayNhanPhongDuKien(dto.getNgayNhanPhongDuKien());
        phieu.setNgayTraPhongDuKien(dto.getNgayTraPhongDuKien());

        // Không gán HoaDon trực tiếp, vì quan hệ là ngược
        // HoaDon sẽ được tạo riêng sau khi PhieuDatPhong được lưu

        return phieu;
    }

//    @Override
//    public PhieuDatPhong toEntity(PhieuDatPhongDTO dto) {
//        if (dto == null) return null;
//
//        PhieuDatPhong phieu = new PhieuDatPhong();
//        phieu.setMaPDP(dto.getMaPDP());
//
//        if (dto.getMaKH() != null) {
//            KhachHang khachHang = new KhachHang();
//            khachHang.setMaKH(dto.getMaKH());
//            phieu.setKhachHang(khachHang);
//        }
//
//        if (dto.getMaNV() != null) {
//            NhanVien nhanVien = new NhanVien();
//            nhanVien.setMaNhanVien(dto.getMaNV());
//            phieu.setNhanVien(nhanVien);
//        }
//
//        if (dto.getDsMaPhong() != null) {
//            Set<Phong> phongs = dto.getDsMaPhong().stream()
//                    .filter(ma -> ma != null && !ma.isEmpty())
//                    .map(ma -> {
//                        Phong p = new Phong();
//                        p.setMaPhong(ma);
//                        return p;
//                    })
//                    .collect(Collectors.toSet());
//            phieu.setPhongs(phongs); // Sửa setDsPhong thành setPhongs
//        }
//
//        phieu.setNgayDatPhong(dto.getNgayDatPhong());
//        phieu.setNgayNhanPhongDuKien(dto.getNgayNhanPhongDuKien());
//        phieu.setNgayTraPhongDuKien(dto.getNgayTraPhongDuKien());
//
//        if (dto.getMaHD() != null) {
//            HoaDon hoaDon = new HoaDon();
//            hoaDon.setMaHD(dto.getMaHD());
//            phieu.setHoaDon(hoaDon); // Ánh xạ maHD
//        }
//
//        return phieu;
//    }
}