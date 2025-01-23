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
    private Faker faker = new Faker();

    // Generate fake KhachHang
    private KhachHang generateKhachHang() {
        KhachHang khachHang = new KhachHang();
        khachHang.setMaKH("KH" + faker.number().numberBetween(1000, 9999));
        khachHang.setHoTen(faker.name().fullName());
        khachHang.setSoDienThoai(generatePhoneNumber());
        khachHang.setSoCCCD(faker.number().digits(12));
        khachHang.setEmail(faker.internet().emailAddress());
        return khachHang;
    }

    // Generate fake HoaDon
    private HoaDon generateHoaDon(KhachHang khachHang, PhieuDatPhong phieuDatPhong, NhanVien nhanVien) {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHD("HD" + faker.number().numberBetween(1000, 9999));
        hoaDon.setNgayLapHD(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
        hoaDon.setNgayNhanPhong(LocalDateTime.now().plusDays(faker.number().numberBetween(1, 10)));
        hoaDon.setNgayTraPhong(LocalDateTime.now().plusDays(faker.number().numberBetween(11, 20)));
        hoaDon.setSoPhongDat(faker.number().numberBetween(1, 5));
        hoaDon.setKhachHang(khachHang);
        hoaDon.setPhieuDatPhong(phieuDatPhong);

        // dsPhieuDatDichVu
        Set<PhieuDatDichVu> dsPhieuDatDichVu = new HashSet<>();
        for (int i = 0; i < faker.number().numberBetween(1, 3); i++) {
            dsPhieuDatDichVu.add(generatePhieuDatDichVu(khachHang, nhanVien));
        }
        hoaDon.setDsPhieuDatDichVu(dsPhieuDatDichVu); // Set danh sách phiếu đặt dịch vụ
        hoaDon.setNhanVien(nhanVien); // Set nhân viên
        return hoaDon;
    }

    // Generate fake DichVu
    private DichVu generateDichVu() {
        DichVu dichVu = new DichVu();
        dichVu.setMaDV("DV" + faker.number().numberBetween(1000, 9999));
        dichVu.setTenDV(faker.commerce().productName());
        dichVu.setDonGia(faker.number().randomDouble(2, 100, 1000));
        dichVu.setDonViTinh(faker.options().option("Lần", "Chiếc", "Hộp", "Bộ", "Giờ"));
        dichVu.setMoTa(faker.lorem().sentence(10));
        return dichVu;
    }

    // Generate fake PhieuDatDichVu
    private PhieuDatDichVu generatePhieuDatDichVu(KhachHang khachHang, NhanVien nhanVien) {
        PhieuDatDichVu phieuDatDichVu = new PhieuDatDichVu();

        // Gán các giá trị cơ bản
        phieuDatDichVu.setMaPDDV("PDDV" + faker.number().numberBetween(1000, 9999));
        phieuDatDichVu.setNgayDatDichVu(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
        phieuDatDichVu.setSoLuongDichVu(faker.number().numberBetween(1, 5));
        phieuDatDichVu.setMoTa(faker.lorem().sentence());
        phieuDatDichVu.setNhanVien(nhanVien);
        phieuDatDichVu.setKhachHang(khachHang);

        // Tạo danh sách dịch vụ và liên kết với phiếu đặt dịch vụ
        Set<DichVu> dsDichVu = new HashSet<>();
        int soLuongDichVu = faker.number().numberBetween(1, 3); // Số lượng dịch vụ ngẫu nhiên
        for (int i = 0; i < soLuongDichVu; i++) {
            DichVu dichVu = generateDichVu(); // Tạo mới một dịch vụ
            dichVu.setPhieuDatDichVu(phieuDatDichVu); // Liên kết dịch vụ với phiếu đặt
            dsDichVu.add(dichVu); // Thêm vào danh sách
        }
        phieuDatDichVu.setDsDichVu(dsDichVu); // Gán danh sách dịch vụ vào phiếu đặt

        return phieuDatDichVu;
    }


    // Generate fake Phong
    private Phong generatePhong(LoaiPhong loaiPhong) {
        Phong phong = new Phong();
        phong.setMaPhong("PH" + faker.number().numberBetween(1000, 9999));
        phong.setTenPhong("Phòng " + faker.number().numberBetween(1, 100));
        phong.setGiaPhong(faker.number().randomDouble(2, 500000, 5000000));
        phong.setTinhTrang(faker.number().numberBetween(0, 2));
        phong.setMoTa(faker.lorem().sentence(10));
        phong.setSoNguoi(faker.number().numberBetween(1, 6));
        phong.setLoaiPhong(loaiPhong);

        // Tạo danh sách phiếu giảm giá ngẫu nhiên
        Set<PhieuGiamGia> dsPhieuGiamGia = new HashSet<>();
        for (int i = 0; i < faker.number().numberBetween(1, 3); i++) {
            dsPhieuGiamGia.add(generatePhieuGiamGia());
        }
        phong.setDsPhieuGiamGia(dsPhieuGiamGia); // Gán danh sách phiếu giảm giá vào phòng

        return phong;
    }

    // Generate fake PhieuDatPhong
    private PhieuDatPhong generatePhieuDatPhong(KhachHang khachHang, NhanVien nhanVien) {
        PhieuDatPhong phieuDatPhong = new PhieuDatPhong();
        phieuDatPhong.setMaPDP("PDP" + faker.number().numberBetween(1000, 9999));

        // Sử dụng LocalDate thay vì LocalDateTime
        phieuDatPhong.setNgayDatPhong(LocalDate.now().minusDays(faker.number().numberBetween(1, 30)));
        phieuDatPhong.setNgayNhanPhongDuKien(LocalDate.now().plusDays(faker.number().numberBetween(1, 10)));
        phieuDatPhong.setNgayTraPhongDuKien(LocalDate.now().plusDays(faker.number().numberBetween(11, 20)));

        phieuDatPhong.setKhachHang(khachHang);
        phieuDatPhong.setNhanVien(nhanVien);

        // dsPhong
        Set<Phong> dsPhong = new HashSet<>();
        dsPhong.add(generatePhong(generateLoaiPhong())); // Thêm một phòng vào danh sách
        phieuDatPhong.setDsPhong(dsPhong); // Set danh sách phòng

        // hoaDon
        HoaDon hoaDon = generateHoaDon(khachHang, phieuDatPhong, nhanVien);
        phieuDatPhong.setHoaDon(hoaDon); // Set hóa đơn
        return phieuDatPhong;
    }

    // Generate fake LoaiPhong
    private LoaiPhong generateLoaiPhong() {
        LoaiPhong loaiPhong = new LoaiPhong();
        String[] loaiPhongOptions = {"Phòng Đơn", "Phòng Đôi", "Phòng Gia Đình", "Phòng Deluxe"};
        String tenLoai = loaiPhongOptions[faker.number().numberBetween(0, loaiPhongOptions.length)];
        loaiPhong.setMaLoai("LP" + faker.number().numberBetween(1000, 9999));
        loaiPhong.setTenLoai(tenLoai);
        loaiPhong.setMoTa(faker.lorem().sentence(10));
        return loaiPhong;
    }

    // Generate fake NhanVien
    private NhanVien generateNhanVien(TaiKhoan taiKhoan) {
        NhanVien nhanVien = new NhanVien();
        nhanVien.setMaNhanVien("NV" + faker.number().numberBetween(1000, 9999));
        nhanVien.setHoTen(faker.name().fullName());
        nhanVien.setChucVu(faker.number().numberBetween(1, 5));
        nhanVien.setSDT(generatePhoneNumber());
        nhanVien.setDiaChi(faker.address().fullAddress());
        nhanVien.setEmail(faker.internet().emailAddress());
        nhanVien.setNgaySinh(faker.date().birthday(20, 60).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        nhanVien.setTaiKhoan(taiKhoan); // Set taiKhoan
        return nhanVien;
    }

    // Generate fake PhieuGiamGia
    private PhieuGiamGia generatePhieuGiamGia() {
        PhieuGiamGia phieuGiamGia = new PhieuGiamGia();
        phieuGiamGia.setMaPGG("PGG" + faker.number().numberBetween(1000, 9999));
        phieuGiamGia.setMucGiamGia(faker.number().randomDouble(2, 5, 50));

        // S ử dụng LocalDate thay vì Date
        phieuGiamGia.setNgayBatDau(LocalDate.now().minusDays(faker.number().numberBetween(1, 30)));
        phieuGiamGia.setNgayKetThuc(LocalDate.now().plusDays(faker.number().numberBetween(1, 30)));

        phieuGiamGia.setDieuKienApDung(faker.lorem().sentence(10));
        phieuGiamGia.setLuotSuDung(faker.number().numberBetween(1, 100));

        return phieuGiamGia;
    }

    // Generate fake TaiKhoan
    private TaiKhoan generateTaiKhoan() {
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setTenDN(faker.internet().username());
        taiKhoan.setMatKhau(faker.internet().password(8, 16));
        return taiKhoan;
    }

    // Generate a random phone number
    private String generatePhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }

    public void generateAndPrintSampleData() {
        EntityManager em = Persistence.createEntityManagerFactory("mariadb-pu").createEntityManager();
        EntityTransaction tr = em.getTransaction();

        for (int i = 0; i < 10; i++) {
            TaiKhoan taiKhoan = generateTaiKhoan();
            LoaiPhong loaiPhong = generateLoaiPhong();
            KhachHang khachHang = generateKhachHang();
            NhanVien nhanVien = generateNhanVien(taiKhoan);
            DichVu dichVu = generateDichVu();
            Phong phong = generatePhong(loaiPhong);
            PhieuGiamGia phieuGiamGia = generatePhieuGiamGia();
            PhieuDatPhong phieuDatPhong = generatePhieuDatPhong(khachHang, nhanVien);
            PhieuDatDichVu phieuDatDichVu = generatePhieuDatDichVu(khachHang, nhanVien);

            try {
                tr.begin();
//                em.persist(taiKhoan); // Lưu TaiKhoan
//                em.persist(khachHang); // Lưu KhachHang
//                  em.persist(phieuGiamGia);
//               em.persist(loaiPhong); // Lưu LoaiPhong
//               em.persist(dichVu);
//                em.persist(nhanVien); // Lưu NhanVien

//                em.persist(phong); // Lưu Phong
//               em.persist(phieuDatDichVu); // Lưu PhieuDatDichVu
//               em.persist(phieuDatPhong); // Lưu PhieuDatPhong
                tr.commit();
            } catch (Exception e) {
                if (tr.isActive()) {
                    tr.rollback();
                }
                e.printStackTrace(); // Ghi lại lỗi
            }

            // Sau khi đã lưu các thực thể, giờ bạn có thể tạo HoaDon
            HoaDon hoaDon = generateHoaDon(khachHang, phieuDatPhong, nhanVien);

            try {
                tr.begin();
              em.persist(hoaDon); // Lưu HoaDon
                tr.commit();
            } catch (Exception e) {
                if (tr.isActive()) {
                    tr.rollback();
                }
                e.printStackTrace(); // Ghi lại lỗi
            }
        }
    }

    public static void main(String[] args) {
        DataGenerator generator = new DataGenerator();
        generator.generateAndPrintSampleData();
    }
}