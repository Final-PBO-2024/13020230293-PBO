package com.absensi.model;

import java.util.Date; 

public class Teacher {
    private int idTeacher;
    private String teacherName;
    private String nip;
    private String gender;
    private String birthDate; 
    private String address;
    private String phone;
    private int insertBy;
    private int updateBy;
    private int deleteBy;
    private Date insertAt;
    private Date updateAt;
    private Date deleteAt;
    private boolean isDelete;


    public Teacher() {
    }

    public int getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(int idTeacher) {
        this.idTeacher = idTeacher;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getInsertBy() {
        return insertBy;
    }

    public void setInsertBy(int insertBy) {
        this.insertBy = insertBy;
    }

    public int getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(int updateBy) {
        this.updateBy = updateBy;
    }

    public int getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(int deleteBy) {
        this.deleteBy = deleteBy;
    }

    public Date getInsertAt() {
        return insertAt;
    }

    public void setInsertAt(Date insertAt) {
        this.insertAt = insertAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Date getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(Date deleteAt) {
        this.deleteAt = deleteAt;
    }

    public boolean isIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

   
    @Override
    public String toString() {
        return teacherName != null ? teacherName : "-- Pilih Wali Kelas --";
    }
}