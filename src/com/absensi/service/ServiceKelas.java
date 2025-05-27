package com.absensi.service;

import com.absensi.model.Kelas;
import java.util.List;

public interface ServiceKelas {
    void insertData(Kelas model);
    void updateData(Kelas model);
    void deleteData(Kelas model); 
    void restoreData(Kelas model); 

    List<Kelas> getData();
    List<Kelas> getDataIsDelete(); 
    List<Kelas> searchData(String keyword);
    List<Kelas> searchDataIsDelete(String keyword); 

    boolean validasiNamaKelas(Kelas model); 
    boolean isClassReferencedInStudents(int idClass);
}