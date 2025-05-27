package com.absensi.tablemodel;

import com.absensi.model.Student;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TabModStudent extends AbstractTableModel {

    private List<Student> list = new ArrayList<>();
    private final String[] columnNames = {"No", "ID", "NIS", "Name", "Gender", "Birth Date", "Address", "Phone", "Class"};

    public void setData(List<Student> list) {
        this.list.clear();
        if (list != null) {
            this.list.addAll(list);
        }
        fireTableDataChanged();
    }

    public Student getData(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < list.size()) {
            return list.get(rowIndex);
        }
        return null; 
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student model = list.get(rowIndex);
        switch (columnIndex) {
            case 0: return (rowIndex + 1);
            case 1: return model.getIdStudent();
            case 2: return model.getNis();
            case 3: return model.getStudentName();
            case 4: return model.getGender();
            case 5: return model.getBirthDate(); 
            case 6: return model.getAddress();
            case 7: return model.getPhone();
            case 8: return (model.getKelas() != null) ? model.getKelas().getClassName() : "-";
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void clear() {
        list.clear();
        fireTableDataChanged();
    }
}