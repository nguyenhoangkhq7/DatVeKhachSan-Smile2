/*
    *QLKS_smile2  day creative: 1/18/2025
    version: 2023.2  IntelliJ IDEA
    author: Nguyễn Hoàng Khang  */
    /*
       *class description:
            write description right here   
     */


import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class App {
    public static void main(String[] args)
    {
        EntityManager em = Persistence.createEntityManagerFactory("mariadb-pu")
                .createEntityManager();
    }
}
