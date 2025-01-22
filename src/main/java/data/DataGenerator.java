/*
    *QLKS_smile2  day creative: 1/18/2025
    version: 2023.2  IntelliJ IDEA
    author: Nguyễn Hoàng Khang  */
    /*
       *class description:
            write description right here   
     */


package data;

import model.*;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DataGenerator {
    private Faker faker = new Faker();

    private final Random rd = new Random();

    //tạo dữ liệu cho Customer
    private KhachHang CustomerData() {
        List<KhachHang> khachHangs = new ArrayList<>();
            KhachHang khachHang = new KhachHang();
            khachHang.setMaKH("KH" + faker.number().numberBetween(1000, 9999));
            khachHang.setHoTen(faker.name().fullName());
            khachHang.setSoDienThoai(generatePhoneNumber(faker));
            khachHang.setSoCCCD(faker.number().digits(12)); // Số CCCD 12 chữ số
            khachHang.setEmail(faker.internet().emailAddress());
            khachHangs.add(khachHang);
        return (KhachHang) khachHangs;
    }

    public NhanVien EmployeeData() {
        Faker faker = new Faker();
        NhanVien nhanVien = new NhanVien();
        nhanVien.setMaNhanVien("NV" + faker.number().numberBetween(1000, 9999));
        nhanVien.setHoTen(faker.name().fullName());
        nhanVien.setChucVu(faker.number().numberBetween(1, 5));
        nhanVien.setSDT(generatePhoneNumber(faker));
        nhanVien.setDiaChi(faker.address().fullAddress());
        nhanVien.setEmail(faker.internet().emailAddress());
//        employee.setNgaySinh(faker.date().birthday(20, 60));
        return nhanVien;
    }

    public static List<TaiKhoan> generateFakeAccounts(List<NhanVien> nhanViens) {
        Faker faker = new Faker();
        List<TaiKhoan> taiKhoans = new ArrayList<>();

        for (NhanVien nhanVien : nhanViens) {
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setTenDN(nhanVien.getMaNhanVien());
            taiKhoan.setMatKhau(faker.internet().password(8, 16));
            taiKhoan.setNhanVien(nhanVien);
            taiKhoans.add(taiKhoan);
        }

        return taiKhoans;
    }
    // Tạo dữ liệu giả lập cho bảng Promotion
    private PhieuGiamGia PromotionData() {
        PhieuGiamGia phieuGiamGia = new PhieuGiamGia();
        phieuGiamGia.setMaPGG("PGG" + faker.number().numberBetween(1000, 9999)); // Mã khuyến mãi
        phieuGiamGia.setMucGiamGia(faker.number().randomDouble(2, 5, 50)); // Mức giảm giá (5% đến 50%)

        // Ngày bắt đầu (hiện tại - 30 ngày)
        Calendar calStart = Calendar.getInstance();
        calStart.add(Calendar.DAY_OF_MONTH, -faker.number().numberBetween(1, 30)); // Trừ 1-30 ngày
        phieuGiamGia.setNgayBatDau(calStart.getTime());

        // Ngày kết thúc (hiện tại + 30 ngày)
        Calendar calEnd = Calendar.getInstance();
        calEnd.add(Calendar.DAY_OF_MONTH, faker.number().numberBetween(1, 30)); // Thêm 1-30 ngày
        phieuGiamGia.setNgayKetThuc(calEnd.getTime());

        phieuGiamGia.setDieuKienApDung(faker.lorem().sentence(10)); // Điều kiện áp dụng
        phieuGiamGia.setLuotSuDung(faker.number().numberBetween(1, 100)); // Số lượt sử dụng

        return phieuGiamGia;
    }

    // Tạo dữ liệu giả lập cho bảng RoomType
    private LoaiPhong RoomTypeData() {
        LoaiPhong loaiPhong = new LoaiPhong();
        loaiPhong.setMaLoai("LP" + faker.number().numberBetween(1000, 9999)); // Mã loại phòng
        loaiPhong.setTenLoai(faker.lorem().words(2).toString().replaceAll("[\\[\\],]", "")); // Tên loại phòng (2 từ ngẫu nhiên)
        loaiPhong.setMoTa(faker.lorem().sentence(10)); // Mô tả loại phòng (1 câu mô tả)

        loaiPhong.setDanhSachPhong(new HashSet<>()); // Danh sách phòng ban đầu để trống
        return loaiPhong;
    }

    // Tạo dữ liệu giả lập cho bảng Room
    private Phong RoomData(LoaiPhong loaiPhong, DonDatPhong donDatPhong, Set<PhieuGiamGia> phieuGiamGias) {
        Phong phong = new Phong();
        phong.setMaPhong("PH" + faker.number().numberBetween(1000, 9999)); // Mã phòng
        phong.setTenPhong("Phòng " + faker.number().numberBetween(1, 100)); // Tên phòng
        phong.setGiaPhong(faker.number().randomDouble(2, 500000, 5000000)); // Giá phòng từ 500k đến 5 triệu
        phong.setTinhTrang(faker.number().numberBetween(0, 2)); // 0: Trống, 1: Đã đặt, 2: Bảo trì
        phong.setMoTa(faker.lorem().sentence(10)); // Mô tả phòng
        phong.setSoNguoi(faker.number().numberBetween(1, 6)); // Số người từ 1 đến 6
        phong.setLoaiPhong(loaiPhong); // Loại phòng
        phong.setDonDatPhong(donDatPhong); // Booking liên kết
        phong.setKhuyenMais(phieuGiamGias); // Các khuyến mãi liên kết

        return phong;
    }


    // Tạo dữ liệu giả lập cho bảng Service
    private DichVu ServiceData() {
        DichVu dichVu = new DichVu();
        dichVu.setMaDV("DV" + faker.number().numberBetween(1000, 9999)); // Mã dịch vụ
        dichVu.setTenDV(faker.commerce().productName()); // Tên dịch vụ
        dichVu.setDonGia(faker.number().randomDouble(2, 100, 1000)); // Đơn giá ngẫu nhiên từ 100 đến 1000
        dichVu.setDonViTinh(faker.options().option("Lần", "Chiếc", "Hộp", "Bộ", "Giờ")); // Đơn vị tính ngẫu nhiên
        dichVu.setMoTa(faker.lorem().sentence(10)); // Mô tả dịch vụ (1 câu ngẫu nhiên)

        return dichVu;
    }
    private DonDatDichVu generateServiceOrder(List<KhachHang> khachHangs, List<DonDatPhong> donDatPhongs, List<NhanVien> nhanViens) {
        if (khachHangs.isEmpty() || donDatPhongs.isEmpty() || nhanViens.isEmpty()) {
            System.out.println("Một trong các danh sách khách hàng, đặt phòng hoặc nhân viên trống. Không thể tạo đơn hàng dịch vụ.");
            return null;
        }

        // số
        var customer = khachHangs.get(faker.number().numberBetween(0, khachHangs.size()));
        var booking = donDatPhongs.get(faker.number().numberBetween(0, donDatPhongs.size()));
        var employee = nhanViens.get(faker.number().numberBetween(0, nhanViens.size()));

        DonDatDichVu donDatDichVu = new DonDatDichVu();
        donDatDichVu.setMaPDDV("PDDV" + faker.number().numberBetween(1000, 9999));
        donDatDichVu.setNgayDatDichVu(generatePastDate(30));  // Giả sử generatePastDate là hàm đã được định nghĩa
        donDatDichVu.setSoLuongDichVu(faker.number().numberBetween(1, 5));
        donDatDichVu.setDonDatPhong(booking);
        donDatDichVu.setKhachHang(customer);
        donDatDichVu.setNhanVien(employee);
        donDatDichVu.setMoTa(faker.lorem().sentence());

        return donDatDichVu;
    }
    // Tạo dữ liệu cho Invoice
    private HoaDon generateInvoice(List<KhachHang> khachHangs, List<DonDatPhong> donDatPhongs) {
        if (khachHangs.isEmpty() || donDatPhongs.isEmpty()) {
            System.out.println("Danh sách khách hàng hoặc đặt phòng trống. Không thể tạo hóa đơn.");
            return null;  // Hoặc xử lý theo cách khác tùy nhu cầu
        }

        var customer = khachHangs.get(faker.number().numberBetween(0, khachHangs.size()));
        var booking = donDatPhongs.get(faker.number().numberBetween(0, donDatPhongs.size()));

        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHD("HD" + faker.number().numberBetween(1000, 9999));

        LocalDate invoiceDate = LocalDate.now().minus(faker.number().numberBetween(1, 30), ChronoUnit.DAYS);
        hoaDon.setNgayLapHD(java.sql.Date.valueOf(invoiceDate));

        hoaDon.setKhachHang(customer);

        LocalDate checkInDate = LocalDate.now().plus(faker.number().numberBetween(1, 10), ChronoUnit.DAYS);
        hoaDon.setNgayNhanPhong(java.sql.Date.valueOf(checkInDate));

        LocalDate checkOutDate = LocalDate.now().plus(faker.number().numberBetween(1, 20), ChronoUnit.DAYS);
        hoaDon.setNgayTraPhong(java.sql.Date.valueOf(checkOutDate));

        hoaDon.setSoPhongDat(faker.number().numberBetween(1, 5));
        hoaDon.setPhieuDatPhong(booking);

        return hoaDon;
    }
    private static Date generatePastDate(int daysBack) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -new Random().nextInt(daysBack + 1)); // Trừ đi một số ngày ngẫu nhiên
        return calendar.getTime();
    }


    private static String generatePhoneNumber(Faker faker) {
        int length = faker.number().numberBetween(10, 13); // Số chữ số từ 10-13
        return faker.number().digits(length);
    }

//    public void generateAndPrintSampleData() {
//        EntityManager em = Persistence.createEntityManagerFactory("mariadb-pu")
//                .createEntityManager();
//
//        EntityTransaction tr = em.getTransaction();
//        for (int i = 0; i < 10; i++) {
//
//            Customer customer = CustomerData();
//            Employee employee = EmployeeData();
//            Account account = generateFakeAccounts(Collections.singletonList(employee)).get(0); // Tạo tài khoản cho nhân viên
//            Promotion promotion = PromotionData();
//            RoomType roomType = RoomTypeData();
//            Service service = ServiceData();
//            ServiceOrder serviceOrder = ServiceData();
//            Invoice invoice =  Invoice();
//
//            // Tạo phòng và gán với loại phòng, khuyến mãi, và booking (booking ở đây để null)
//            Room room = RoomData(roomType, null, new HashSet<>(Collections.singletonList(promotion))); // Phòng không có booking
//
//
//            tr.begin();
//
//            em.persist(customer);
//            em.persist(employee);
//            em.persist(account);
//            em.persist(promotion);
//            em.persist(roomType);
//            em.persist(service);
//            em.persist(room);
//            em.persist(serviceOrder);
//
//            em.persist(invoice);
//
//            tr.commit();
//        }
//    }

//    public static void main(String[] args) {
//        DataGenerator generator = new DataGenerator();
//        generator.generateAndPrintSampleData();
//    }

}
