package com.absensi.dao;

import com.absensi.config.Koneksi;
import com.absensi.model.Kelas;
import com.absensi.model.Student;
import com.absensi.model.Teacher;
import com.absensi.service.ServiceStudent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO implements ServiceStudent {

    private final Connection conn;

    public StudentDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public void insertData(Student model) {
        PreparedStatement st = null;
        try {
            String sql = "INSERT INTO students (nis, student_name, gender, birth_date, address, phone, id_class, insert_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            st = conn.prepareStatement(sql);

            st.setString(1, model.getNis());
            st.setString(2, model.getStudentName());
            st.setString(3, model.getGender());
            st.setString(4, model.getBirthDate()); 
            st.setString(5, model.getAddress());
            st.setString(6, model.getPhone());
            st.setInt(7, model.getKelas().getIdClass());
            st.setInt(8, model.getInsertBy());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(st);
        }
    }

    @Override
    public void updateData(Student model) {
        PreparedStatement st = null;
        try {
            String sql = "UPDATE students SET nis=?, student_name=?, gender=?, birth_date=?, address=?, phone=?, id_class=?, update_by=?, update_at=NOW() WHERE id_student=?";
            st = conn.prepareStatement(sql);

            st.setString(1, model.getNis());
            st.setString(2, model.getStudentName());
            st.setString(3, model.getGender());
            st.setString(4, model.getBirthDate());
            st.setString(5, model.getAddress());
            st.setString(6, model.getPhone());
            st.setInt(7, model.getKelas().getIdClass());
            st.setInt(8, model.getUpdateBy());
            st.setInt(9, model.getIdStudent());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(st);
        }
    }

    @Override
    public void deleteData(Student model) {
        PreparedStatement st = null;
        try {
            String sql = "UPDATE students SET delete_by=?, delete_at=NOW(), is_delete=TRUE WHERE id_student=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, model.getDeleteBy());
            st.setInt(2, model.getIdStudent());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(st);
        }
    }

    @Override
    public void restoreData(Student model) {
        PreparedStatement st = null;
        try {
            String sql = "UPDATE students SET delete_by=NULL, delete_at=NULL, is_delete=FALSE WHERE id_student=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, model.getIdStudent());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(st);
        }
    }

    private List<Student> executeQueryWithJoin(String sql, String keyword) {
        List<Student> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql);
            if (keyword != null && !keyword.isEmpty()) {
                String likeKeyword = "%" + keyword + "%";
                st.setString(1, likeKeyword); 
                st.setString(2, likeKeyword); 
                st.setString(3, likeKeyword); 
                st.setString(4, likeKeyword); 
                st.setString(5, likeKeyword); 
            }
            rs = st.executeQuery();
            while (rs.next()) {
                Student std = new Student();
                std.setIdStudent(rs.getInt("s.id_student"));
                std.setNis(rs.getString("s.nis"));
                std.setStudentName(rs.getString("s.student_name"));
                std.setGender(rs.getString("s.gender"));
                std.setBirthDate(rs.getString("s.birth_date")); 
                std.setAddress(rs.getString("s.address"));
                std.setPhone(rs.getString("s.phone"));

                Kelas kls = new Kelas();
                kls.setIdClass(rs.getInt("s.id_class"));
                kls.setClassName(rs.getString("c.class_name"));
                std.setKelas(kls);
                list.add(std);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closeStatement(st);
        }
        return list;
    }

    @Override
    public List<Student> getData() {
        String sql = "SELECT s.id_student, s.nis, s.student_name, s.gender, s.birth_date, s.address, s.phone, s.id_class, c.class_name " +
                     "FROM students s " +
                     "JOIN classes c ON s.id_class = c.id_class " +
                     "WHERE s.is_delete=FALSE ORDER BY s.student_name ASC";
        return executeQueryWithJoin(sql, null);
    }

    @Override
    public List<Student> getDataIsDelete() {
        String sql = "SELECT s.id_student, s.nis, s.student_name, s.gender, s.birth_date, s.address, s.phone, s.id_class, c.class_name " +
                     "FROM students s " +
                     "JOIN classes c ON s.id_class = c.id_class " +
                     "WHERE s.is_delete=TRUE ORDER BY s.student_name ASC";
        return executeQueryWithJoin(sql, null);
    }

    @Override
    public List<Student> searchData(String keyword) {
        String sql = "SELECT s.id_student, s.nis, s.student_name, s.gender, s.birth_date, s.address, s.phone, s.id_class, c.class_name " +
                     "FROM students s " +
                     "JOIN classes c ON s.id_class = c.id_class " +
                     "WHERE (s.nis LIKE ? OR s.student_name LIKE ? OR s.address LIKE ? OR s.phone LIKE ? OR c.class_name LIKE ?) AND s.is_delete=FALSE " +
                     "ORDER BY s.student_name ASC";
        return executeQueryWithJoin(sql, keyword);
    }

    @Override
    public List<Student> searchDataIsDelete(String keyword) {
        String sql = "SELECT s.id_student, s.nis, s.student_name, s.gender, s.birth_date, s.address, s.phone, s.id_class, c.class_name " +
                     "FROM students s " +
                     "JOIN classes c ON s.id_class = c.id_class " +
                     "WHERE (s.nis LIKE ? OR s.student_name LIKE ? OR s.address LIKE ? OR s.phone LIKE ? OR c.class_name LIKE ?) AND s.is_delete=TRUE " +
                     "ORDER BY s.student_name ASC";
        return executeQueryWithJoin(sql, keyword);
    }

    @Override
    public boolean validasiNIS(Student model) {
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean valid = true;
        try {
            String sql = "SELECT nis FROM students WHERE nis = ? AND id_student != ? AND is_delete = FALSE";
            st = conn.prepareStatement(sql);
            st.setString(1, model.getNis());
            st.setInt(2, model.getIdStudent()); 
            rs = st.executeQuery();
            if (rs.next()) {
                valid = false; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            valid = false; 
        } finally {
            closeResultSet(rs);
            closeStatement(st);
        }
        return valid;
    }
    
    public List<Kelas> getAllActiveClasses() {
        List<Kelas> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT c.id_class, c.class_name, t.id_teacher, t.teacher_name " +
                         "FROM classes c " +
                         "JOIN teachers t ON c.id_teacher = t.id_teacher " +
                         "WHERE c.is_delete=FALSE ORDER BY c.class_name ASC";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                Kelas kls = new Kelas();
                kls.setIdClass(rs.getInt("c.id_class"));
                kls.setClassName(rs.getString("c.class_name"));
                
                Teacher teacher = new Teacher();
                teacher.setIdTeacher(rs.getInt("t.id_teacher"));
                teacher.setTeacherName(rs.getString("t.teacher_name"));
                kls.setTeacher(teacher);
                
                list.add(kls);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closeStatement(st);
        }
        return list;
    }
    
    // Helper methods to close resources
    private void closeStatement(PreparedStatement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}