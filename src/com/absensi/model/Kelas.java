package com.absensi.model;

import java.util.Date; 

public class Kelas {

    private int idClass;
    private String className;
    private Teacher teacher;
    private int insertBy;
    private int updateBy;
    private int deleteBy;
    private Date insertAt;
    private Date updateAt;
    private Date deleteAt;
    private boolean isDelete;

 
    public Kelas() {
       
     
        this.teacher = new Teacher();
    }

  

    public int getIdClass() {
        return idClass;
    }

    public void setIdClass(int idClass) {
        this.idClass = idClass;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
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
        return className; 
                         
                         
    }
}