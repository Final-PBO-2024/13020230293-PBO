package com.absensi.dao;

import com.absensi.config.Koneksi;
import com.absensi.model.User;
import com.absensi.service.ServiceUser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO implements ServiceUser{

private final Connection conn;

public UserDAO() {
    conn = Koneksi.getConnection();
}

@Override
public boolean isUserExists() {
    PreparedStatement st = null;
    ResultSet rs = null;
    try {
        String sql = "SELECT COUNT(*) FROM users WHERE is_delete = FALSE";
        st = conn.prepareStatement(sql);
        rs = st.executeQuery();
        if(rs.next()){
            return rs.getInt(1) > 0;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return false;
}

@Override
public boolean validateUsername(User model) {
    PreparedStatement st = null;
    ResultSet rs = null;
    boolean valid = false;
     try {
        String sql = "SELECT username FROM users WHERE username LIKE BINARY ?";
        st = conn.prepareStatement(sql);
        st.setString(1, model.getUsername());
        rs = st.executeQuery();
        valid = !rs.next();
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return valid;
}

@Override
public void insertData(User model) {
    PreparedStatement st = null;
    try {
        String sql = "INSERT INTO users (name, email, username, password, role, photo, insert_by) VALUES (?,?,?,?,?,?,?)";
        st = conn.prepareStatement(sql);
        
        st.setString(1,model.getName());
        st.setString(2,model.getEmail());
        st.setString(3,model.getUsername());
        st.setString(4,BCrypt.hashpw(model.getPassword(), BCrypt.gensalt()));
        st.setString(5,model.getRole());
        st.setString(6,model.getPhoto());
        
        if(model.getInsertBy() == null ||model.getInsertBy() == 0){
            st.setNull(7, java.sql.Types.INTEGER);
        }else{
            st.setString(7,model.getName());
        }
        
        st.executeUpdate();
        st.close();
        
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    @Override
    public User processLogin(User model) {
        PreparedStatement st = null;
        ResultSet rs = null;
        User modelUser = null;
        String sql = "SELECT id_user, name, email, username,password, role, photo FROM users WHERE username=?";
        try {
            st = conn.prepareStatement(sql);
            st.setString(1, model.getUsername());
            rs = st.executeQuery();
            
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(model.getPassword(), hashedPassword)) {
                    modelUser = new User();
                    modelUser.setIdUser(rs.getInt("id_user"));
                    modelUser.setName(rs.getString("name"));
                    modelUser.setEmail(rs.getString("email"));
                    modelUser.setUsername(rs.getString("username"));
                    modelUser.setRole(rs.getString("role"));
                    modelUser.setPhoto(rs.getString("photo"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelUser;
    }
}
