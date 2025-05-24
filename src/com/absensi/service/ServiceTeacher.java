
package com.absensi.service;

import com.absensi.model.Teacher;
import java.util.List;

public interface ServiceTeacher {
    
    void insertData (Teacher model);
    void updateData (Teacher model);
    void deleteData (Teacher model);
    void restoreData (Teacher model);
    
    List<Teacher> getData();
    List<Teacher> searchData(String keyword);
    
    List<Teacher> getDataIsDelete();
    List<Teacher> searchDataIsDelete(String keyword);
    
    boolean validasiNIP(Teacher model);
    
}
