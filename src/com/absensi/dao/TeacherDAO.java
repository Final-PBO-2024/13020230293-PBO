package com.absensi.dao;

import com.absensi.config.Koneksi;
import com.absensi.model.Teacher;
import com.absensi.service.ServiceTeacher;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO implements ServiceTeacher{

private final Connection conn;

public TeacherDAO() {
    conn = Koneksi.getConnection();
}

@Override
public void insertData(Teacher model) {
    PreparedStatement st = null;
    try {
        String sql = "INSERT INTO teachers (teacher_name, nip, gender, birth_date, address, phone, insert_by) VALUES (?, ?, ?, ?, ?, ?, ?)";
        st = conn.prepareStatement(sql);

        st.setString(1, model.getTeacherName());
        st.setString(2, model.getNip());
        st.setString(3, model.getGender());
        st.setString(4, model.getBirthDate());
        st.setString(5, model.getAddress());
        st.setString(6, model.getPhone());
        st.setInt(7, model.getInsertBy()); 

        st.executeUpdate();
        st.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    @Override
    public void updateData(Teacher model) { 
    PreparedStatement st = null;
    try {
        String sql = "UPDATE teachers SET teacher_name=?, nip=?, gender=?, birth_date=?, address=?, phone=?, update_by=?, update_at=NOW() WHERE id_teacher=?";
        st = conn.prepareStatement(sql);

         st.setString(1, model.getTeacherName());
        st.setString(2, model.getNip());
        st.setString(3, model.getGender());
        st.setString(4, model.getBirthDate());
        st.setString(5, model.getAddress());
        st.setString(6, model.getPhone());
        st.setInt(7, model.getUpdateBy()); 
        st.setInt(8, model.getIdTeacher());

        st.executeUpdate();
        st.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    @Override
    public void deleteData(Teacher model) {
        PreparedStatement st = null;
    try {
        String sql = "UPDATE teachers SET delete_by=?, delete_at = NOW(), is_delete=TRUE WHERE id_teacher=?";
        st = conn.prepareStatement(sql);

        st.setInt(1, model.getDeleteBy());
        st.setInt(2, model.getIdTeacher());

        st.executeUpdate();
        st.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }
    
    @Override
    public void restoreData(Teacher model) {
         PreparedStatement st = null;
    try {
        String sql = "UPDATE teachers SET delete_by=NULL, delete_at = NULL, is_delete=FALSE WHERE id_teacher=?";
        st = conn.prepareStatement(sql);
        
        st.setInt(1, model.getIdTeacher());
 
        st.executeUpdate();
        st.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    @Override
    public List<Teacher> getData() {
    List<Teacher> list = new ArrayList<>();
    PreparedStatement st = null;
    ResultSet rs = null;

    try {
        String sql = "SELECT id_teacher, teacher_name, nip, gender, birth_date, address, phone FROM teachers WHERE is_delete=FALSE";

        st = conn.prepareStatement(sql);
        rs = st.executeQuery();

        while (rs.next()) {
            Teacher modelTeacher = new Teacher();
            modelTeacher.setIdTeacher(rs.getInt("id_teacher"));
            modelTeacher.setTeacherName(rs.getString("teacher_name"));
            modelTeacher.setNip(rs.getString("nip"));
            modelTeacher.setGender(rs.getString("gender"));
            modelTeacher.setBirthDate(rs.getString("birth_date"));
            modelTeacher.setAddress(rs.getString("address"));
            modelTeacher.setPhone(rs.getString("phone"));

            list.add(modelTeacher);
        }

        rs.close();
        st.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}

    @Override
    public List<Teacher> searchData(String keyword) {
    List<Teacher> list = new ArrayList<>();
    PreparedStatement st = null;
    ResultSet rs = null;

    String sql = "SELECT id_teacher, teacher_name, nip, gender, birth_date, address, phone " +
                 "FROM teachers " +
                 "WHERE id_teacher LIKE ? OR " +
                 "teacher_name LIKE ? OR " +
                 "nip LIKE ? OR " +
                 "birth_date LIKE ? OR " +
                 "address LIKE ? OR " +
                 "phone LIKE ? " +
                 "AND is_delete=FALSE";

    try {
        st = conn.prepareStatement(sql);
        st.setString(1, "%" + keyword + "%");
        st.setString(2, "%" + keyword + "%");
        st.setString(3, "%" + keyword + "%");
        st.setString(4, "%" + keyword + "%");
        st.setString(5, "%" + keyword + "%");
        st.setString(6, "%" + keyword + "%");
        st.setString(7, "%" + keyword + "%");

        rs = st.executeQuery();

        while (rs.next()) {
            Teacher modelTeacher = new Teacher();
            modelTeacher.setIdTeacher(rs.getInt("id_teacher"));
            modelTeacher.setTeacherName(rs.getString("teacher_name"));
            modelTeacher.setNip(rs.getString("nip"));
            modelTeacher.setGender(rs.getString("gender"));
            modelTeacher.setBirthDate(rs.getString("birth_date"));
            modelTeacher.setAddress(rs.getString("address"));
            modelTeacher.setPhone(rs.getString("phone"));
            list.add(modelTeacher);
        }
        
        rs.close();
        st.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}
    

    @Override
    public List<Teacher> getDataIsDelete() {
       List<Teacher> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;

    try {
        String sql = "SELECT id_teacher, teacher_name, nip, gender, birth_date, address, phone FROM teachers WHERE is_delete=TRUE";

        st = conn.prepareStatement(sql);
        rs = st.executeQuery();

        while (rs.next()) {
            Teacher modelTeacher = new Teacher();
            modelTeacher.setIdTeacher(rs.getInt("id_teacher"));
            modelTeacher.setTeacherName(rs.getString("teacher_name"));
            modelTeacher.setNip(rs.getString("nip"));
            modelTeacher.setGender(rs.getString("gender"));
            modelTeacher.setBirthDate(rs.getString("birth_date"));
            modelTeacher.setAddress(rs.getString("address"));
            modelTeacher.setPhone(rs.getString("phone"));

            list.add(modelTeacher);
        }

        rs.close();
        st.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
    }

    @Override
    public List<Teacher> searchDataIsDelete(String keyword) {
        List<Teacher> list = new ArrayList<>();
    PreparedStatement st = null;
    ResultSet rs = null;

    String sql = "SELECT id_teacher, teacher_name, nip, gender, birth_date, address, phone " +
                 "FROM teachers " +
                 "WHERE id_teacher LIKE ? OR " +
                 "teacher_name LIKE ? OR " +
                 "nip LIKE ? OR " +
                 "birth_date LIKE ? OR " +
                 "address LIKE ? OR " +
                 "phone LIKE ? " +
                 "AND is_delete=TRUE";

    try {
        st = conn.prepareStatement(sql);
        st.setString(1, "%" + keyword + "%");
        st.setString(2, "%" + keyword + "%");
        st.setString(3, "%" + keyword + "%");
        st.setString(4, "%" + keyword + "%");
        st.setString(5, "%" + keyword + "%");
        st.setString(6, "%" + keyword + "%");
        st.setString(7, "%" + keyword + "%");

        rs = st.executeQuery();

        while (rs.next()) {
            Teacher modelTeacher = new Teacher();
            modelTeacher.setIdTeacher(rs.getInt("id_teacher"));
            modelTeacher.setTeacherName(rs.getString("teacher_name"));
            modelTeacher.setNip(rs.getString("nip"));
            modelTeacher.setGender(rs.getString("gender"));
            modelTeacher.setBirthDate(rs.getString("birth_date"));
            modelTeacher.setAddress(rs.getString("address"));
            modelTeacher.setPhone(rs.getString("phone"));
            list.add(modelTeacher);
        }
        
        rs.close();
        st.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
    }

    @Override
    public boolean validasiNIP(Teacher model) {
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean valid = false;
            try {
               String sql = "SELECT Teachername FROM Teachers WHERE Teachername LIKE BINARY ?";
               st = conn.prepareStatement(sql);
               st.setString(1, model.getNip());
               rs = st.executeQuery();
               valid = !rs.next();
           } catch (Exception e) {
               e.printStackTrace();
           }

           return valid;
    }


    
}

   
