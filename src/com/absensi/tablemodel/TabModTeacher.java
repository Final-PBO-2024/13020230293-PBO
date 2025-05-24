package com.absensi.tablemodel;

import com.absensi.model.Teacher;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TabModTeacher extends AbstractTableModel{
    
    private final List <Teacher> list = new ArrayList<>();
    
    public Teacher getData (int index){
        return list.get(index);
    }
    
    public void clear(){
        list.clear();
        fireTableCellUpdated(0, 0);
    }
    
    public void setData (List<Teacher>list){
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }

    private final String[] columNames = {"No","ID","Teacher Name","NIP","Gender","Birth Date","Address","Phone "};
    
    
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Teacher model = list.get(rowIndex);
        if (columnIndex==0) {
            return (rowIndex + 1);  
        }else{
            switch (columnIndex) {
                case 1:
                    return model.getIdTeacher();
                case 2:
                    return model.getTeacherName();
                case 3:
                    return model.getNip();
                case 4:
                    return model.getGender();
                case 5:
                    return model.getBirthDate();
                case 6:
                    return model.getAddress();
                case 7:
                    return model.getPhone();
                default:
                   return null;
            }
        }
        
 
}

    @Override
    public String getColumnName(int column) {
        return columNames [column];
        
    }
}
