package data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import model.*;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

public class DataGenerator {
    private final Faker faker = new Faker();
    private final EntityManager em = Persistence.createEntityManagerFactory("mariadb-pu").createEntityManager();
    private final EntityTransaction tr = em.getTransaction();

    private String generatePhoneNumber() {
        return faker.phoneNumber().cellPhone();
    }

    private KhachHang generateKhachHang() {
        KhachHang kh = new KhachHang();
        kh.setMaKH("KH" + faker.number().numberBetween(1000, 9999));
        kh.setHoTen(faker.name().fullName());
        kh.setSoDienThoai(generatePhoneNumber());
        kh.setSoCCCD(faker.number().digits(12));
        kh.setEmail(faker.internet().emailAddress());
        return kh;
    }

    private TaiKhoan generateTaiKhoan() {
        TaiKhoan tk = new TaiKhoan();
        tk.setTenDN(faker.internet().username());
        tk.setMatKhau(faker.internet().password(8, 16));
        return tk;
    }

    private NhanVien generateNhanVien(TaiKhoan tk) {
        NhanVien nv = new NhanVien();
        nv.setMaNhanVien("NV" + faker.number().numberBetween(1000, 9999));
        nv.setHoTen(faker.name().fullName());
        nv.setChucVu(faker.number().numberBetween(1, 5));
        nv.setSDT(generatePhoneNumber());
        nv.setDiaChi(faker.address().fullAddress());
        nv.setEmail(faker.internet().emailAddress());
        nv.setNgaySinh(faker.date().birthday(20, 60).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        nv.setNgayVaoLam(LocalDate.now().minusDays(faker.number().numberBetween(30, 365)));
        nv.setLuongCoBan(faker.number().randomDouble(2, 5000000, 15000000));
        nv.setHeSoLuong(faker.number().randomDouble(2, 1, 3));
        nv.setTrangThai(faker.number().numberBetween(0, 2)); // ví dụ: 0 - nghỉ, 1 - đang làm, 2 - tạm nghỉ
        nv.setTaiKhoan(tk);
        return nv;
    }



    private LoaiPhong generateLoaiPhong() {
        LoaiPhong lp = new LoaiPhong();
        String[] types = {"Phòng Đơn", "Phòng Đôi", "Phòng Gia Đình", "Phòng Deluxe"};
        lp.setMaLoai("LP" + faker.number().numberBetween(1000, 9999));
        lp.setTenLoai(faker.options().option(types));
        lp.setMoTa(faker.lorem().sentence(10));
        return lp;
    }

    private PhieuGiamGia generatePhieuGiamGia() {
        PhieuGiamGia pgg = new PhieuGiamGia();
        pgg.setMaPGG("PGG" + faker.number().numberBetween(1000, 9999));
        pgg.setMucGiamGia(faker.number().randomDouble(2, 5, 50));
        pgg.setNgayBatDau(LocalDate.now().minusDays(faker.number().numberBetween(1, 30)));
        pgg.setNgayKetThuc(LocalDate.now().plusDays(faker.number().numberBetween(1, 30)));
        pgg.setDieuKienApDung(faker.lorem().sentence(10));
        pgg.setLuotSuDung(faker.number().numberBetween(1, 100));
        return pgg;
    }

//    private Phong generatePhong(LoaiPhong loaiPhong) {
//        Phong phong = new Phong();
//        phong.setMaPhong("PH" + faker.number().numberBetween(1000, 9999));
//        phong.setTenPhong("Phòng " + faker.number().numberBetween(1, 100));
//        phong.setGiaPhong(faker.number().randomDouble(2, 500000, 5000000));
//        phong.setTinhTrang(faker.number().numberBetween(0, 2));
//        phong.setMoTa(faker.lorem().sentence(10));
//        phong.setSoNguoi(faker.number().numberBetween(1, 6));
//        phong.setLoaiPhong(loaiPhong);
//
//        Set<PhieuGiamGia> dsPGG = new HashSet<>();
//        for (int i = 0; i < faker.number().numberBetween(1, 3); i++) {
//            dsPGG.add(generatePhieuGiamGia());
//        }
//        phong.setDsPhieuGiamGia(dsPGG);
//
//        return phong;
//    }

    private Phong generatePhong(LoaiPhong loaiPhong, PhieuDatPhong phieuDatPhong) {
        Phong phong = new Phong();
        phong.setMaPhong("PH" + faker.number().numberBetween(1000, 9999));
        phong.setTenPhong("Phòng " + faker.number().numberBetween(1, 100));
        phong.setGiaPhong(faker.number().randomDouble(2, 500000, 5000000));
        phong.setTinhTrang(faker.number().numberBetween(0, 2));
        phong.setMoTa(faker.lorem().sentence(10));
        phong.setSoNguoi(faker.number().numberBetween(1, 6));
        phong.setLoaiPhong(loaiPhong);
        phong.setPhieuDatPhong(phieuDatPhong); // Thiết lập quan hệ với phiếu đặt phòng

        // Tạo các phiếu giảm giá nếu cần
        Set<PhieuGiamGia> dsPGG = new HashSet<>();
        for (int i = 0; i < faker.number().numberBetween(1, 3); i++) {
            dsPGG.add(generatePhieuGiamGia());
        }
        phong.setDsPhieuGiamGia(dsPGG);

        return phong;
    }

    private DichVu generateDichVu() {
        DichVu dv = new DichVu();
        dv.setMaDV("DV" + faker.number().numberBetween(1000, 9999));
        dv.setTenDV(faker.commerce().productName());
        dv.setDonGia(faker.number().randomDouble(2, 100, 1000));
        dv.setDonViTinh(faker.options().option("Lần", "Chiếc", "Hộp", "Bộ", "Giờ"));
        dv.setMoTa(faker.lorem().sentence(10));
        return dv;
    }

    private PhieuDatDichVu generatePhieuDatDichVu(KhachHang kh, NhanVien nv) {
        PhieuDatDichVu pddv = new PhieuDatDichVu();
        pddv.setMaPDDV("PDDV" + faker.number().numberBetween(1000, 9999));
        pddv.setNgayDatDichVu(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
        pddv.setSoLuongDichVu(faker.number().numberBetween(1, 5));
        pddv.setMoTa(faker.lorem().sentence());
        pddv.setNhanVien(nv);
        pddv.setKhachHang(kh);

        Set<DichVu> dsDV = new HashSet<>();
        for (int i = 0; i < faker.number().numberBetween(1, 3); i++) {
            DichVu dv = generateDichVu();
            dv.setPhieuDatDichVu(pddv);
            dsDV.add(dv);
        }
        pddv.setDsDichVu(dsDV);
        return pddv;
    }

//    private PhieuDatPhong generatePhieuDatPhong(KhachHang kh, NhanVien nv, LoaiPhong lp) {
//        PhieuDatPhong pdp = new PhieuDatPhong();
//        pdp.setMaPDP("PDP" + faker.number().numberBetween(1000, 9999));
//        pdp.setNgayDatPhong(LocalDate.now().minusDays(faker.number().numberBetween(1, 30)));
//        pdp.setNgayNhanPhongDuKien(LocalDate.now().plusDays(faker.number().numberBetween(1, 10)));
//        pdp.setNgayTraPhongDuKien(LocalDate.now().plusDays(faker.number().numberBetween(11, 20)));
//        pdp.setKhachHang(kh);
//        pdp.setNhanVien(nv);
//
//        Set<Phong> dsPhong = new HashSet<>();
//        dsPhong.add(generatePhong(lp));
//        pdp.setDsPhong(dsPhong);
//        return pdp;
//    }
    private PhieuDatPhong generatePhieuDatPhong(KhachHang kh, NhanVien nv, LoaiPhong lp) {
        PhieuDatPhong pdp = new PhieuDatPhong();
        pdp.setMaPDP("PDP" + faker.number().numberBetween(1000, 9999));
        pdp.setNgayDatPhong(LocalDate.now().minusDays(faker.number().numberBetween(1, 30)));
        pdp.setNgayNhanPhongDuKien(LocalDate.now().plusDays(faker.number().numberBetween(1, 10)));
        pdp.setNgayTraPhongDuKien(LocalDate.now().plusDays(faker.number().numberBetween(11, 20)));
        pdp.setKhachHang(kh);
        pdp.setNhanVien(nv);

        // Tạo danh sách phòng và thiết lập quan hệ hai chiều
        Set<Phong> dsPhong = new HashSet<>();
        int soPhong = faker.number().numberBetween(1, 4); // Mỗi phiếu đặt 1-3 phòng

        for (int i = 0; i < soPhong; i++) {
            Phong phong = generatePhong(lp, pdp);
            dsPhong.add(phong);
        }

        pdp.setDsPhong(dsPhong);
        return pdp;
    }

    private HoaDon generateHoaDon(KhachHang kh, PhieuDatPhong pdp, NhanVien nv) {
        HoaDon hd = new HoaDon();
        hd.setMaHD("HD" + faker.number().numberBetween(1000, 9999));
        hd.setNgayLapHD(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
        hd.setNgayNhanPhong(LocalDateTime.now().plusDays(faker.number().numberBetween(1, 10)));
        hd.setNgayTraPhong(LocalDateTime.now().plusDays(faker.number().numberBetween(11, 20)));
        hd.setSoPhongDat(faker.number().numberBetween(1, 5));
        hd.setKhachHang(kh);
        hd.setPhieuDatPhong(pdp);
        hd.setNhanVien(nv);

        Set<PhieuDatDichVu> dsPDDV = new HashSet<>();
        for (int i = 0; i < faker.number().numberBetween(1, 3); i++) {
            dsPDDV.add(generatePhieuDatDichVu(kh, nv));
        }
        hd.setDsPhieuDatDichVu(dsPDDV);

        return hd;
    }

//    public void generateAndPersistSampleData() {
//        for (int i = 0; i < 10; i++) {
//            TaiKhoan tk = generateTaiKhoan();
//            NhanVien nv = generateNhanVien(tk);
//            KhachHang kh = generateKhachHang();
//            LoaiPhong lp = generateLoaiPhong();
//            PhieuDatPhong pdp = generatePhieuDatPhong(kh, nv, lp);
//            HoaDon hd = generateHoaDon(kh, pdp, nv);
//
//            try {
//                tr.begin();
//                em.persist(tk);
//                em.persist(nv);
//                em.persist(kh);
//                em.persist(lp);
//                for (Phong p : pdp.getDsPhong()) em.persist(p);
//                em.persist(pdp);
//                for (PhieuDatDichVu pddv : hd.getDsPhieuDatDichVu()) {
//                    for (DichVu dv : pddv.getDsDichVu()) em.persist(dv);
//                    em.persist(pddv);
//                }
//                em.persist(hd);
//                tr.commit();
//            } catch (Exception e) {
//                if (tr.isActive()) tr.rollback();
//                e.printStackTrace();
//            }
//        }
//    }
public void generateAndPersistSampleData() {
    for (int i = 0; i < 10; i++) {
        try {
            tr.begin();

            // Tạo các entity độc lập trước
            TaiKhoan tk = generateTaiKhoan();
            em.persist(tk);

            NhanVien nv = generateNhanVien(tk);
            em.persist(nv);

            KhachHang kh = generateKhachHang();
            em.persist(kh);

            LoaiPhong lp = generateLoaiPhong();
            em.persist(lp);

            // Tạo phiếu đặt phòng và các phòng liên quan
            PhieuDatPhong pdp = generatePhieuDatPhong(kh, nv, lp);
            em.persist(pdp);

            // Persist các phòng (đã được persist tự động do cascade)

            // Tạo hóa đơn và các dịch vụ liên quan
            HoaDon hd = generateHoaDon(kh, pdp, nv);
            em.persist(hd);

            tr.commit();
        } catch (Exception e) {
            if (tr.isActive()) tr.rollback();
            e.printStackTrace();
        }
    }
}

    public static void main(String[] args) {
        new DataGenerator().generateAndPersistSampleData();
    }
}
