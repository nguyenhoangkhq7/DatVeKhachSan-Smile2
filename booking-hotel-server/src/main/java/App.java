/*
    *QLKS_smile2  day creative: 1/18/2025
    version: 2023.2  IntelliJ IDEA
    author: Nguyễn Hoàng Khang  */
    /*
       *class description:
            write description right here   
     */


import data.DataGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class App {
    public static void main(String[] args)
    {
        EntityManager em = Persistence.createEntityManagerFactory("mariadb-pu")
                .createEntityManager();

        // bỏ comment dòng này và chạy lần đầu để generate data mẫu, sau đó comment cho các lần tiếp theo
//      new DataGenerator().generateAndPersistSampleData();
    }
}
