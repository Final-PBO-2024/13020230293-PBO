package com.absensi.config;

import com.mysql.cj.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class Koneksi {
    private static Connection conn;
    
    public static Connection getConnection(){
        if(conn==null){
            try{
                String url = "jdbc:mysql://localhost:3306/myabsensidb";
                String user = "root";
                String pass = "";
                DriverManager.registerDriver(new Driver());
                conn = (Connection) DriverManager.getConnection(url, user,pass);
            }catch (SQLException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database not connected\nCheck MySQL Service");
            }
        }
        return conn;
    }
}
