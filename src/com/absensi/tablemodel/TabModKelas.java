package com.absensi.tablemodel;

import com.absensi.model.Kelas;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TabModKelas extends AbstractTableModel {

    private List<Kelas> list = new ArrayList<>();
    private final String[] columnNames = {"No", "ID Kelas", "Class Name", "Teacher"};

    public void setData(List<Kelas> list) {
        this.list.clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }

    public Kelas getData(int rowIndex) {
        return list.get(rowIndex);
    }

    public void addRow(Kelas kelas) {
        list.add(kelas);
        fireTableRowsInserted(list.size() - 1, list.size() - 1);
    }

    public void updateRow(int rowIndex, Kelas kelas) {
        list.set(rowIndex, kelas);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void deleteRow(int rowIndex) {
        list.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
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
        Kelas model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1); 
            case 1:
                return model.getIdClass(); 
            case 2:
                return model.getClassName();
            case 3:
                return (model.getTeacher() != null) ? model.getTeacher().getTeacherName() : "-";
            default:
                return null;
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