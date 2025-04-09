package dao;

import connectDB.ConnectDB;
import entity.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class PhieuDoiPhong_DAO {
    private ArrayList<PhieuDoiPhong> pdp;
    public PhieuDoiPhong_DAO() {
        pdp = new ArrayList<>();
//        public ArrayList<PhieuDoiPhong> getListPDP() {
//            try {
//                Connection con = ConnectDB.getInstance().getConnection();
//                String sql = "";
//                Statement st = con.createStatement();
//                ResultSet rs = st.executeQuery(sql);
//                while (rs.next()) {
//
//                }
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
}
