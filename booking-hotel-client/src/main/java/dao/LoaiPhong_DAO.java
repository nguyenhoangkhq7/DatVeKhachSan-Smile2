package dao;

import connectDB.ConnectDB;
import entity.LoaiPhong;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LoaiPhong_DAO {
    private ArrayList<LoaiPhong> dsLoaiPhong;

    public LoaiPhong_DAO() {
        dsLoaiPhong = new ArrayList<>();
    }

    public ArrayList<LoaiPhong> getDSLoaiPhong() {
        try{
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "select * from LoaiPhong";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                String tenLoaiPhong = rs.getString("tenLoai");
                String moTa = rs.getString("moTa");
                LoaiPhong loaiPhong = new LoaiPhong(tenLoaiPhong, moTa);
                dsLoaiPhong.add(loaiPhong);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return dsLoaiPhong;
    }
}
