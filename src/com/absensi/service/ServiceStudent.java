package com.absensi.service;

import com.absensi.model.Student;
import java.util.List;

public interface ServiceStudent {
    void insertData(Student model);
    void updateData(Student model);
    void deleteData(Student model); 
    void restoreData(Student model); 

    List<Student> getData();
    List<Student> getDataIsDelete();
    List<Student> searchData(String keyword);
    List<Student> searchDataIsDelete(String keyword);

    boolean validasiNIS(Student model); 
}