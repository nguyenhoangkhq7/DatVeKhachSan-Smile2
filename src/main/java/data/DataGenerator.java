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
import model.Account;
import model.Customer;
import model.Employee;
import net.datafaker.Faker;
import org.hibernate.usertype.CompositeUserType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private static String generatePhoneNumber(Faker faker) {
        int length = faker.number().numberBetween(10, 13); // Số chữ số từ 10-13
        return faker.number().digits(length);
    }

    public void generateAndPrintSampleData() {
        EntityManager em = Persistence.createEntityManagerFactory("mariadb-pu")
                .createEntityManager();

        EntityTransaction tr = em.getTransaction();
        for (int i = 0; i <  10; i++) {
            Customer customer = CustomerData();
            Employee employee = EmployeeData();
//            Account account = (Account) generateFakeAccounts();

            tr.begin();
                em.persist(customer.getClass());
                em.persist(employee.getClass());
                em.persist(customer);
                em.persist(employee);
            tr.commit();

        }
    }
    public static void main(String[] args) {
        DataGenerator generator = new DataGenerator();
        generator.generateAndPrintSampleData();
    }

}
