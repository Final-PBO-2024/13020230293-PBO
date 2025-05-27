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

public class TeacherDAO implements ServiceTeacher {

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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(st);
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(st);
        }
    }

    @Override
    public void deleteData(Teacher model) { // Ini adalah SOFT DELETE
        PreparedStatement st = null;
        try {
            String sql = "UPDATE teachers SET delete_by=?, delete_at = NOW(), is_delete=TRUE WHERE id_teacher=?";
            st = conn.prepareStatement(sql);

            st.setInt(1, model.getDeleteBy());
            st.setInt(2, model.getIdTeacher());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(st);
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(st);
        }
    }

    @Override
    public void permanentDeleteData(Teacher model) {
        PreparedStatement st = null;
        try {
            // PERHATIAN: Query ini akan menghapus data secara permanen!
            String sql = "DELETE FROM teachers WHERE id_teacher=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, model.getIdTeacher());
            st.executeUpdate();
        } catch (SQLException e) {
            // Penting untuk menangani ForeignKeyConstraintViolationException jika guru
            // masih direferensikan di tabel lain (misalnya tabel 'classes')
            // dan tidak ada ON DELETE CASCADE/SET NULL di database.
            if (e.getSQLState().startsWith("23")) { // Kode SQLState untuk integrity constraint violation
                // Anda bisa melempar custom exception atau menampilkan pesan error yang lebih baik
                System.err.println("Data cannot be deleted permanently. It is still referenced in other tables: " + e.getMessage());
                // Contoh: throw new DataIntegrityViolationException("Guru ini masih menjadi wali kelas.", e);
                // Untuk sekarang, kita hanya print stack trace
                e.printStackTrace(); 
                // Atau, beri tahu pengguna melalui throw exception yang ditangkap di Form
                // throw new RuntimeException("Data guru ini tidak bisa dihapus permanen karena masih menjadi wali kelas.", e);
            } else {
                e.printStackTrace();
            }
        } finally {
            closeStatement(st);
        }
    }
    // -----------------------------------------**


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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closeStatement(st);
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
                     "WHERE (id_teacher LIKE ? OR " +
                     "teacher_name LIKE ? OR " +
                     "nip LIKE ? OR " +
                     // "birth_date LIKE ? OR " + // Pencarian berdasarkan tanggal bisa rumit dengan LIKE
                     "address LIKE ? OR " +
                     "phone LIKE ?) AND is_delete=FALSE";
        try {
            st = conn.prepareStatement(sql);
            String likeKeyword = "%" + keyword + "%";
            st.setString(1, likeKeyword); // Untuk id_teacher, ini mungkin tidak ideal jika id integer
            st.setString(2, likeKeyword);
            st.setString(3, likeKeyword);
            // st.setString(4, likeKeyword); // Untuk birth_date
            st.setString(4, likeKeyword); // Indeks disesuaikan karena birth_date di-skip
            st.setString(5, likeKeyword); // Indeks disesuaikan

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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closeStatement(st);
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closeStatement(st);
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
                     "WHERE (id_teacher LIKE ? OR " + 
                     "teacher_name LIKE ? OR " +   
                     "nip LIKE ? OR " +            
                     // "birth_date LIKE ? OR " +   
                     "address LIKE ? OR " +        
                     "phone LIKE ?) " +            
                     "AND is_delete=TRUE";
        try {
            st = conn.prepareStatement(sql);
            String likeKeyword = "%" + keyword + "%";
            st.setString(1, likeKeyword);
            st.setString(2, likeKeyword);
            st.setString(3, likeKeyword);
            // st.setString(4, likeKeyword); // untuk birth_date
            st.setString(4, likeKeyword); // Indeks disesuaikan
            st.setString(5, likeKeyword); // Indeks disesuaikan

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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closeStatement(st);
        }
        return list;
    }

    @Override
    public boolean validasiNIP(Teacher model) {
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean valid = true; // Ubah ke true, anggap valid kecuali ditemukan NIP yang sama
        try {
            // Cek NIP yang sama untuk ID guru yang berbeda (berguna saat update)
            // atau untuk semua guru (saat insert, dimana model.getIdTeacher() akan 0)
            String sql = "SELECT nip FROM teachers WHERE nip = ? AND id_teacher != ?";
            st = conn.prepareStatement(sql);
            st.setString(1, model.getNip());
            st.setInt(2, model.getIdTeacher()); // Jika insert, idTeacher = 0, maka akan cek semua NIP
            rs = st.executeQuery();
            if (rs.next()) { // Jika ditemukan NIP yang sama
                valid = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            valid = false; // Anggap tidak valid jika ada error
        } finally {
            closeResultSet(rs);
            closeStatement(st);
        }
        return valid;
    }

    // Helper methods untuk menutup resources
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