package com.absensi.service;

import com.absensi.model.User;

public interface ServiceUser {
    boolean isUserExists();
    boolean validateUsername (User model);
    void insertData (User model);
    
    User processLogin(User model);
}
