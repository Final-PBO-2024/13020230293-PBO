package com.absensi.dao;

import com.absensi.config.Koneksi;
import com.absensi.model.Kelas;
import com.absensi.model.Teacher;
import com.absensi.service.ServiceKelas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KelasDAO implements ServiceKelas {

    private final Connection conn;

    public KelasDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public void insertData(Kelas model) {
        PreparedStatement st = null;
        try {
            String sql = "INSERT INTO classes (class_name, id_teacher, insert_by) VALUES (?, ?, ?)";
            st = conn.prepareStatement(sql);

            st.setString(1, model.getClassName());
            st.setInt(2, model.getTeacher().getIdTeacher());
            st.setInt(3, model.getInsertBy());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void updateData(Kelas model) {
        PreparedStatement st = null;
        try {
            String sql = "UPDATE classes SET class_name=?, id_teacher=?, update_by=?, update_at=NOW() WHERE id_class=?";
            st = conn.prepareStatement(sql);

            st.setString(1, model.getClassName());
            st.setInt(2, model.getTeacher().getIdTeacher());
            st.setInt(3, model.getUpdateBy());
            st.setInt(4, model.getIdClass());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void deleteData(Kelas model) {
        PreparedStatement st = null;
        try {
            String sql = "UPDATE classes SET delete_by=?, delete_at=NOW(), is_delete=TRUE WHERE id_class=?";
            st = conn.prepareStatement(sql);

            st.setInt(1, model.getDeleteBy());
            st.setInt(2, model.getIdClass());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void restoreData(Kelas model) {
        PreparedStatement st = null;
        try {
            String sql = "UPDATE classes SET delete_by=NULL, delete_at=NULL, is_delete=FALSE WHERE id_class=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, model.getIdClass());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<Kelas> executeQueryWithJoin(String sql, String keyword, boolean isDeleted) {
        List<Kelas> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(sql);
            if (keyword != null && !keyword.isEmpty()) {
                String likeKeyword = "%" + keyword + "%";
                st.setString(1, likeKeyword); 
                st.setString(2, likeKeyword); 
            }
            rs = st.executeQuery();
            while (rs.next()) {
                Kelas kls = new Kelas();
                kls.setIdClass(rs.getInt("c.id_class"));
                kls.setClassName(rs.getString("c.class_name"));
                Teacher teacher = new Teacher();
                teacher.setIdTeacher(rs.getInt("c.id_teacher"));
                teacher.setTeacherName(rs.getString("t.teacher_name"));
                kls.setTeacher(teacher);
                list.add(kls);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public List<Kelas> getData() {
        String sql = "SELECT c.id_class, c.class_name, c.id_teacher, t.teacher_name " +
                     "FROM classes c " +
                     "JOIN teachers t ON c.id_teacher = t.id_teacher " +
                     "WHERE c.is_delete=FALSE ORDER BY c.class_name ASC";
        return executeQueryWithJoin(sql, null, false);
    }

    @Override
    public List<Kelas> getDataIsDelete() {
        String sql = "SELECT c.id_class, c.class_name, c.id_teacher, t.teacher_name " +
                     "FROM classes c " +
                     "JOIN teachers t ON c.id_teacher = t.id_teacher " +
                     "WHERE c.is_delete=TRUE ORDER BY c.class_name ASC";
        return executeQueryWithJoin(sql, null, true);
    }

    @Override
    public List<Kelas> searchData(String keyword) {
        String sql = "SELECT c.id_class, c.class_name, c.id_teacher, t.teacher_name " +
                     "FROM classes c " +
                     "JOIN teachers t ON c.id_teacher = t.id_teacher " +
                     "WHERE (c.class_name LIKE ? OR t.teacher_name LIKE ?) AND c.is_delete=FALSE " +
                     "ORDER BY c.class_name ASC";
        return executeQueryWithJoin(sql, keyword, false);
    }

    @Override
    public List<Kelas> searchDataIsDelete(String keyword) {
        String sql = "SELECT c.id_class, c.class_name, c.id_teacher, t.teacher_name " +
                     "FROM classes c " +
                     "JOIN teachers t ON c.id_teacher = t.id_teacher " +
                     "WHERE (c.class_name LIKE ? OR t.teacher_name LIKE ?) AND c.is_delete=TRUE " +
                     "ORDER BY c.class_name ASC";
        return executeQueryWithJoin(sql, keyword, true);
    }

    @Override
    public boolean validasiNamaKelas(Kelas model) {
        // Selalu kembalikan true untuk memperbolehkan nama kelas yang sama
        return true; 
    }
    
    @Override
    public boolean isClassReferencedInStudents(int idClass) {
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean referenced = false;
        try {
            String sql = "SELECT COUNT(*) FROM students WHERE id_class = ? AND is_delete = FALSE";
            st = conn.prepareStatement(sql);
            st.setInt(1, idClass);
            rs = st.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                referenced = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return referenced;
    }

    public List<Teacher> getAllTeachers() {
        List<Teacher> list = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT id_teacher, teacher_name FROM teachers WHERE is_delete=FALSE ORDER BY teacher_name ASC";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                Teacher teacher = new Teacher();
                teacher.setIdTeacher(rs.getInt("id_teacher"));
                teacher.setTeacherName(rs.getString("teacher_name"));
                list.add(teacher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}