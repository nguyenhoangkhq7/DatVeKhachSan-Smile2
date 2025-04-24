package data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import model.*;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DataGenerator {
    private final Faker faker = new Faker();
    private final EntityManager em = Persistence.createEntityManagerFactory("mariadb-pu").createEntityManager();
    private final EntityTransaction tr = em.getTransaction();
    private final LocalDate START_DATE = LocalDate.of(2022, 1, 1);
    private final LocalDate END_DATE = LocalDate.of(2025, 12, 31);

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
        // Generate birthday for ages 20–60
        nv.setNgaySinh(faker.date().birthday(20, 60).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        // Random hire date between 2022 and 2025
        nv.setNgayVaoLam(convertToLocalDate(faker.date().between(
                Date.from(START_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(END_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant())
        )));
        nv.setLuongCoBan(faker.number().randomDouble(2, 5000000, 15000000));
        nv.setHeSoLuong(faker.number().randomDouble(2, 1, 3));
        nv.setTrangThai(faker.number().numberBetween(0, 2));
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
        // Random start date between 2022 and 2025
        LocalDate startDate = convertToLocalDate(faker.date().between(
                Date.from(START_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(END_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant())
        ));
        pgg.setNgayBatDau(startDate);
        // End date is 1–90 days after start date
        pgg.setNgayKetThuc(startDate.plusDays(faker.number().numberBetween(1, 90)));
        pgg.setDieuKienApDung(faker.lorem().sentence(10));
        pgg.setLuotSuDung(faker.number().numberBetween(1, 100));
        return pgg;
    }

    private Phong generatePhong(LoaiPhong loaiPhong) {
        Phong phong = new Phong();
        phong.setMaPhong("PH" + faker.number().numberBetween(1000, 9999));
        phong.setTenPhong("Phòng " + faker.number().numberBetween(1, 100));
        phong.setGiaPhong(faker.number().randomDouble(2, 500000, 5000000));
        phong.setTinhTrang(faker.number().numberBetween(0, 6));
        phong.setMoTa(faker.lorem().sentence(10));
        phong.setSoNguoi(faker.number().numberBetween(1, 6));
        phong.setLoaiPhong(loaiPhong);

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
        dv.setDonGia(faker.number().randomDouble(2, 100000, 2000000));
        dv.setDonViTinh(faker.options().option("Lần", "Chiếc", "Hộp", "Bộ", "Giờ"));
        dv.setMoTa(faker.lorem().sentence(10));
        return dv;
    }

    private PhieuDatDichVu generatePhieuDatDichVu(KhachHang kh, NhanVien nv, PhieuDatPhong pdp, HoaDon hd) {
        PhieuDatDichVu pddv = new PhieuDatDichVu();
        pddv.setMaPDDV("PDDV" + UUID.randomUUID().toString().substring(0, 8));
        // Random service booking date between 2022 and 2025
        pddv.setNgayDatDichVu(convertToLocalDateTime(faker.date().between(
                Date.from(START_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(END_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant())
        )));
        pddv.setSoLuongDichVu(faker.number().numberBetween(1, 5));
        pddv.setMoTa(faker.lorem().sentence());
        pddv.setNhanVien(nv);
        pddv.setKhachHang(kh);
        pddv.setPhieuDatPhong(pdp);
        pddv.setHoaDon(hd);

        Set<DichVu> dsDV = new HashSet<>();
        for (int i = 0; i < faker.number().numberBetween(1, 3); i++) {
            DichVu dv = generateDichVu();
            em.persist(dv);
            dsDV.add(dv);
        }
        pddv.setDichVus(dsDV);

        pdp.getPhieuDatDichVus().add(pddv);

        return pddv;
    }

    private PhieuDatPhong generatePhieuDatPhong(KhachHang kh, NhanVien nv, LoaiPhong lp) {
        PhieuDatPhong pdp = new PhieuDatPhong();
        pdp.setMaPDP("PDP" + UUID.randomUUID().toString().substring(0, 8));
        // Random booking date between 2022 and 2025
        LocalDate bookingDate = convertToLocalDate(faker.date().between(
                Date.from(START_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(END_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant())
        ));
        pdp.setNgayDatPhong(bookingDate);
        // Check-in date is 1–10 days after booking
        LocalDate checkInDate = bookingDate.plusDays(faker.number().numberBetween(1, 10));
        pdp.setNgayNhanPhongDuKien(checkInDate);
        // Check-out date is 1–10 days after check-in
        pdp.setNgayTraPhongDuKien(checkInDate.plusDays(faker.number().numberBetween(1, 10)));
        pdp.setKhachHang(kh);
        pdp.setNhanVien(nv);
        pdp.setPhieuDatDichVus(new HashSet<>());

        Set<Phong> dsPhong = new HashSet<>();
        int soPhong = faker.number().numberBetween(1, 4);
        for (int i = 0; i < soPhong; i++) {
            Phong phong = generatePhong(lp);
            em.persist(phong);
            dsPhong.add(phong);
        }
        pdp.setPhongs(dsPhong);

        return pdp;
    }

    private HoaDon generateHoaDon(KhachHang kh, PhieuDatPhong pdp, NhanVien nv) {
        HoaDon hd = new HoaDon();
        hd.setMaHD("HD" + UUID.randomUUID().toString().substring(0, 8));
        // Random invoice date between 2022 and 2025
        LocalDateTime invoiceDate = convertToLocalDateTime(faker.date().between(
                Date.from(START_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(END_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant())
        ));
        hd.setNgayLapHD(invoiceDate);
        // Check-in and check-out aligned with PhieuDatPhong
        hd.setNgayNhanPhong(invoiceDate.plusDays(faker.number().numberBetween(1, 10)));
        hd.setNgayTraPhong(hd.getNgayNhanPhong().plusDays(faker.number().numberBetween(1, 10)));
        hd.setSoPhongDat(pdp.getPhongs().size());
        hd.setKhachHang(kh);
        hd.setPhieuDatPhong(pdp);
        hd.setNhanVien(nv);

        pdp.setHoaDon(hd);

        Set<PhieuDatDichVu> dsPDDV = new HashSet<>();
        int soPDDV = faker.number().numberBetween(1, 3);
        for (int i = 0; i < soPDDV; i++) {
            PhieuDatDichVu pddv = generatePhieuDatDichVu(kh, nv, pdp, hd);
            dsPDDV.add(pddv);
        }
        hd.setDsPhieuDatDichVu(dsPDDV);

        return hd;
    }

    // Helper methods to convert Date to LocalDate and LocalDateTime
    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public void generateAndPersistSampleData() {
        for (int i = 0; i < 10; i++) {
            try {
                tr.begin();

                TaiKhoan tk = generateTaiKhoan();
                em.persist(tk);

                NhanVien nv = generateNhanVien(tk);
                em.persist(nv);

                KhachHang kh = generateKhachHang();
                em.persist(kh);

                LoaiPhong lp = generateLoaiPhong();
                em.persist(lp);

                PhieuDatPhong pdp = generatePhieuDatPhong(kh, nv, lp);
                em.persist(pdp);

                HoaDon hd = generateHoaDon(kh, pdp, nv);
                for (PhieuDatDichVu pddv : hd.getDsPhieuDatDichVu()) {
                    em.persist(pddv);
                }
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