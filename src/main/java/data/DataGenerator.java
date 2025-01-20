/*
    *QLKS_smile2  day creative: 1/18/2025
    version: 2023.2  IntelliJ IDEA
    author: Nguyễn Hoàng Khang  */
    /*
       *class description:
            write description right here   
     */


package data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import model.*;
import net.datafaker.Faker;
import org.hibernate.usertype.CompositeUserType;

import java.util.*;

public class DataGenerator {
    private Faker faker = new Faker();

    private final Random rd = new Random();

    //tạo dữ liệu cho Customer
    private Customer CustomerData() {
        List<Customer> customers = new ArrayList<>();
            Customer customer = new Customer();
            customer.setMaKH("KH" + faker.number().numberBetween(1000, 9999));
            customer.setHoTen(faker.name().fullName());
            customer.setSDT(generatePhoneNumber(faker));
            customer.setSoCCCD(faker.number().digits(12)); // Số CCCD 12 chữ số
            customer.setEmail(faker.internet().emailAddress());
            customers.add(customer);
        return (Customer) customers;
    }

    public Employee EmployeeData() {
        Faker faker = new Faker();
        Employee employee = new Employee();
        employee.setMaNhanVien("NV" + faker.number().numberBetween(1000, 9999));
        employee.setHoTen(faker.name().fullName());
        employee.setChucVu(faker.number().numberBetween(1, 5));
        employee.setSDT(generatePhoneNumber(faker));
        employee.setDiaChi(faker.address().fullAddress());
        employee.setEmail(faker.internet().emailAddress());
//        employee.setNgaySinh(faker.date().birthday(20, 60));
        return employee;
    }

    public static List<Account> generateFakeAccounts(List<Employee> employees) {
        Faker faker = new Faker();
        List<Account> accounts = new ArrayList<>();

        for (Employee employee : employees) {
            Account account = new Account();
            account.setTenDN(employee.getMaNhanVien());
            account.setMatKhau(faker.internet().password(8, 16));
            account.setNhanVien(employee);
            accounts.add(account);
        }

        return accounts;
    }
    // Tạo dữ liệu giả lập cho bảng Promotion
    private Promotion PromotionData() {
        Promotion promotion = new Promotion();
        promotion.setMaPGG("PGG" + faker.number().numberBetween(1000, 9999)); // Mã khuyến mãi
        promotion.setMucGiamGia(faker.number().randomDouble(2, 5, 50)); // Mức giảm giá (5% đến 50%)

        // Ngày bắt đầu (hiện tại - 30 ngày)
        Calendar calStart = Calendar.getInstance();
        calStart.add(Calendar.DAY_OF_MONTH, -faker.number().numberBetween(1, 30)); // Trừ 1-30 ngày
        promotion.setNgayBatDau(calStart.getTime());

        // Ngày kết thúc (hiện tại + 30 ngày)
        Calendar calEnd = Calendar.getInstance();
        calEnd.add(Calendar.DAY_OF_MONTH, faker.number().numberBetween(1, 30)); // Thêm 1-30 ngày
        promotion.setNgayKetThuc(calEnd.getTime());

        promotion.setDieuKienApDung(faker.lorem().sentence(10)); // Điều kiện áp dụng
        promotion.setLuotSuDung(faker.number().numberBetween(1, 100)); // Số lượt sử dụng

        return promotion;
    }

    // Tạo dữ liệu giả lập cho bảng RoomType
    private RoomType RoomTypeData() {
        RoomType roomType = new RoomType();
        roomType.setMaLoai("LP" + faker.number().numberBetween(1000, 9999)); // Mã loại phòng
        roomType.setTenLoai(faker.lorem().words(2).toString().replaceAll("[\\[\\],]", "")); // Tên loại phòng (2 từ ngẫu nhiên)
        roomType.setMoTa(faker.lorem().sentence(10)); // Mô tả loại phòng (1 câu mô tả)

        roomType.setDanhSachPhong(new HashSet<>()); // Danh sách phòng ban đầu để trống
        return roomType;
    }

    // Tạo dữ liệu giả lập cho bảng Room
    private Room RoomData(RoomType loaiPhong, Booking booking, Set<Promotion> promotions) {
        Room room = new Room();
        room.setMaPhong("PH" + faker.number().numberBetween(1000, 9999)); // Mã phòng
        room.setTenPhong("Phòng " + faker.number().numberBetween(1, 100)); // Tên phòng
        room.setGiaPhong(faker.number().randomDouble(2, 500000, 5000000)); // Giá phòng từ 500k đến 5 triệu
        room.setTinhTrang(faker.number().numberBetween(0, 2)); // 0: Trống, 1: Đã đặt, 2: Bảo trì
        room.setMoTa(faker.lorem().sentence(10)); // Mô tả phòng
        room.setSoNguoi(faker.number().numberBetween(1, 6)); // Số người từ 1 đến 6
        room.setLoaiPhong(loaiPhong); // Loại phòng
        room.setBooking(booking); // Booking liên kết
        room.setPromotions(promotions); // Các khuyến mãi liên kết

        return room;
    }


    // Tạo dữ liệu giả lập cho bảng Service
    private Service ServiceData() {
        Service service = new Service();
        service.setMaDV("DV" + faker.number().numberBetween(1000, 9999)); // Mã dịch vụ
        service.setTenDV(faker.commerce().productName()); // Tên dịch vụ
        service.setDonGia(faker.number().randomDouble(2, 100, 1000)); // Đơn giá ngẫu nhiên từ 100 đến 1000
        service.setDonViTinh(faker.options().option("Lần", "Chiếc", "Hộp", "Bộ", "Giờ")); // Đơn vị tính ngẫu nhiên
        service.setMoTa(faker.lorem().sentence(10)); // Mô tả dịch vụ (1 câu ngẫu nhiên)

        return service;
    }

    private static String generatePhoneNumber(Faker faker) {
        int length = faker.number().numberBetween(10, 13); // Số chữ số từ 10-13
        return faker.number().digits(length);
    }

    public void generateAndPrintSampleData() {
        EntityManager em = Persistence.createEntityManagerFactory("mariadb-pu")
                .createEntityManager();

        EntityTransaction tr = em.getTransaction();
        for (int i = 0; i < 10; i++) {

            Customer customer = CustomerData();
            Employee employee = EmployeeData();
            Account account = generateFakeAccounts(Collections.singletonList(employee)).get(0); // Tạo tài khoản cho nhân viên
            Promotion promotion = PromotionData();
            RoomType roomType = RoomTypeData();
            Service service = ServiceData();

            // Tạo phòng và gán với loại phòng, khuyến mãi, và booking (booking ở đây để null)
            Room room = RoomData(roomType, null, new HashSet<>(Collections.singletonList(promotion))); // Phòng không có booking


            tr.begin();

            em.persist(customer);
            em.persist(employee);
            em.persist(account);
            em.persist(promotion);
            em.persist(roomType);
            em.persist(service);
            em.persist(room);

            tr.commit();
        }
    }

    public static void main(String[] args) {
        DataGenerator generator = new DataGenerator();
        generator.generateAndPrintSampleData();
    }

}
