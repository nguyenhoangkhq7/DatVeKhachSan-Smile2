package data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import model.*;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class DataGenerator {
    private final Faker faker = new Faker();
    private final EntityManager em = Persistence.createEntityManagerFactory("mariadb-pu").createEntityManager();
    private final EntityTransaction tr = em.getTransaction();

    private final String[] roomTypes = {"Phòng Đơn", "Phòng Đôi", "Phòng Gia Đình", "Phòng Deluxe"};
    private final Map<String, LoaiPhong> loaiPhongMap = new HashMap<>();

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
        nv.setTrangThai(faker.number().numberBetween(0, 2));
        nv.setTaiKhoan(tk);
        return nv;
    }

    private LoaiPhong generateLoaiPhongCoDinh(String tenLoai, int index) {
        LoaiPhong lp = new LoaiPhong();
        lp.setMaLoai("LP" + (1000 + index)); // e.g., LP1000, LP1001, ...
        lp.setTenLoai(tenLoai);
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

    private Phong generatePhong(LoaiPhong loaiPhong) {
        Phong phong = new Phong();
        phong.setMaPhong("PH" + faker.number().numberBetween(1000, 9999));
        phong.setTenPhong("Phòng " + faker.number().numberBetween(1, 100));
        phong.setGiaPhong(faker.number().randomDouble(2, 500000, 5000000));
        phong.setTinhTrang(faker.number().numberBetween(0, 2));
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
        dv.setDonGia(faker.number().randomDouble(2, 100000, 1000000));
        dv.setDonViTinh(faker.options().option("Lần", "Chiếc", "Hộp", "Bộ", "Giờ"));
        dv.setMoTa(faker.lorem().sentence(10));
        return dv;
    }

    // Sửa: Tạo ngày ngẫu nhiên trong khoảng 2023-2025
    private LocalDateTime generateRandomDateTime() {
        int year = faker.number().numberBetween(2023, 2025);
        int month = faker.number().numberBetween(1, 12);
        int day = faker.number().numberBetween(1, 28); // Giới hạn ngày để tránh lỗi tháng ngắn
        return LocalDateTime.of(year, month, day, faker.number().numberBetween(0, 23), faker.number().numberBetween(0, 59));
    }

    private PhieuDatDichVu generatePhieuDatDichVu(KhachHang kh, NhanVien nv, HoaDon hd) {
        PhieuDatDichVu pddv = new PhieuDatDichVu();
        pddv.setMaPDDV("PDDV" + faker.number().numberBetween(1000, 9999));
        pddv.setNgayDatDichVu(generateRandomDateTime()); // Sửa: Dùng ngày ngẫu nhiên
        pddv.setSoLuongDichVu(faker.number().numberBetween(1, 5));
        pddv.setMoTa(faker.lorem().sentence());
        pddv.setNhanVien(nv);
        pddv.setKhachHang(kh);
        pddv.setHoaDon(hd);

        Set<DichVu> dsDV = new HashSet<>();
        for (int i = 0; i < faker.number().numberBetween(1, 3); i++) {
            DichVu dv = generateDichVu();
            dv.setPhieuDatDichVu(pddv);
            dsDV.add(dv);
        }
        pddv.setDsDichVu(dsDV);
        return pddv;
    }

    private PhieuDatPhong generatePhieuDatPhong(KhachHang kh, NhanVien nv, LoaiPhong lp) {
        PhieuDatPhong pdp = new PhieuDatPhong();
        pdp.setMaPDP("PDP" + faker.number().numberBetween(1000, 9999));
        LocalDateTime baseDate = generateRandomDateTime(); // Ngày cơ sở
        pdp.setNgayDatPhong(baseDate.toLocalDate());
        pdp.setNgayNhanPhongDuKien(baseDate.plusDays(faker.number().numberBetween(1, 10)).toLocalDate());
        pdp.setNgayTraPhongDuKien(baseDate.plusDays(faker.number().numberBetween(11, 20)).toLocalDate());
        pdp.setKhachHang(kh);
        pdp.setNhanVien(nv);

        Set<Phong> dsPhong = new HashSet<>();
        for (int i = 0; i < faker.number().numberBetween(1, 3); i++) {
            Phong phong = generatePhong(lp);
            phong.setPhieuDatPhong(pdp);
            dsPhong.add(phong);
        }
        pdp.setDsPhong(dsPhong);

        return pdp;
    }

    private HoaDon generateHoaDon(KhachHang kh, PhieuDatPhong pdp, NhanVien nv) {
        HoaDon hd = new HoaDon();
        hd.setMaHD("HD" + faker.number().numberBetween(1000, 9999));
        LocalDateTime baseDate = generateRandomDateTime(); // Ngày cơ sở
        hd.setNgayLapHD(baseDate);
        hd.setNgayNhanPhong(baseDate.plusDays(faker.number().numberBetween(1, 10)));
        hd.setNgayTraPhong(baseDate.plusDays(faker.number().numberBetween(11, 20)));
        hd.setSoPhongDat(faker.number().numberBetween(1, 5));
        hd.setKhachHang(kh);
        hd.setPhieuDatPhong(pdp);
        hd.setNhanVien(nv);

        // Gán ngược từ PDP về HD
        pdp.setHoaDon(hd);

        // Tạo danh sách Phiếu Đặt Dịch Vụ và gán ngược về hóa đơn
        Set<PhieuDatDichVu> dsPDDV = new HashSet<>();
        for (int i = 0; i < faker.number().numberBetween(1, 3); i++) {
            dsPDDV.add(generatePhieuDatDichVu(kh, nv, hd));
        }
        hd.setDsPhieuDatDichVu(dsPDDV);

        return hd;
    }

    public void generateAndPersistSampleData() {
        // 1. Tạo sẵn 4 loại phòng duy nhất
        for (int i = 0; i < roomTypes.length; i++) {
            LoaiPhong lp = generateLoaiPhongCoDinh(roomTypes[i], i);
            loaiPhongMap.put(roomTypes[i], lp);
        }

        // Tăng số lượng bản ghi để có dữ liệu phong phú hơn
        for (int i = 0; i < 50; i++) { // Tăng từ 10 lên 50 để có nhiều dữ liệu hơn
            TaiKhoan tk = generateTaiKhoan();
            NhanVien nv = generateNhanVien(tk);
            KhachHang kh = generateKhachHang();

            // Random loại phòng trong danh sách đã tạo
            String randomType = faker.options().option(roomTypes);
            LoaiPhong lp = loaiPhongMap.get(randomType);

            PhieuDatPhong pdp = generatePhieuDatPhong(kh, nv, lp);
            HoaDon hd = generateHoaDon(kh, pdp, nv);

            try {
                tr.begin();
                // Chỉ persist 4 loại phòng duy nhất 1 lần ở đầu
                if (i == 0) {
                    for (LoaiPhong loai : loaiPhongMap.values()) {
                        em.persist(loai);
                    }
                }

                em.persist(tk);
                em.persist(nv);
                em.persist(kh);
                for (Phong p : pdp.getDsPhong()) em.persist(p);
                em.persist(pdp);
                for (PhieuDatDichVu pddv : hd.getDsPhieuDatDichVu()) {
                    for (DichVu dv : pddv.getDsDichVu()) em.persist(dv);
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