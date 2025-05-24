package com.absensi.form;

import com.absensi.dao.TeacherDAO;
import com.absensi.main.Form;
import com.absensi.model.Teacher;
import com.absensi.service.ServiceTeacher;
import com.absensi.tablemodel.TabModTeacher;
import com.absensi.util.TabelUtils;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.PopupMenu;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

public class FormTeacher extends Form {
    private final ServiceTeacher servis = new TeacherDAO();
    private final TabModTeacher tblModel = new TabModTeacher();
    
    private JTable tblData =  new JTable();
    private JTextField txtSearch;
    
    public FormTeacher(){
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx, wrap", "[fill]", "[][][][fill, grow]"));
        add(setInfo());
        add(createSepator());
        add(setButton());
        add(setTableDate());
    }

    private JPanel setInfo() {
        JPanel panel = new JPanel(new MigLayout("fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        JLabel lbTitle = new JLabel("Teacher Data");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18");
        
        panel.add(lbTitle);
        return panel;
    }


    private JSeparator createSepator() {
       JSeparator separator = new JSeparator();
       separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
       return separator;
    }

    private JPanel setButton() {
       JPanel panel = new JPanel(new MigLayout("wrap","[][][]push[]"));
       panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
    
       JButton btnAdd = new JButton("Add");
       btnAdd.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;"
               + "foreground:rgb(255,255,255);");
       btnAdd.setIcon(new FlatSVGIcon("com/absensi/icon/add_white.svg",0.8f));
       btnAdd.setIconTextGap(5);
       btnAdd.addActionListener((e) -> {
       });
       
       JButton btnUpdate = new JButton("Update");
       btnUpdate.setIcon(new FlatSVGIcon("com/absensi/icon/edit.svg",0.4f));
       btnUpdate.setIconTextGap(5);
       btnUpdate.addActionListener((e) -> {
       });
       
       JButton btnDelete = new JButton("Delete");
       btnDelete.setIcon(new FlatSVGIcon("com/absensi/icon/delete.svg",0.4f));
       btnDelete.setIconTextGap(5);
       btnDelete.addActionListener((e) -> {
       });
       
       
       txtSearch = new JTextField();
       txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search..");
       txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
       txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("raven/modal/demo/icons/search.jvg", 0.4f));
       txtSearch.addKeyListener(new KeyAdapter() {
           @Override
           public void keyReleased(KeyEvent e) {
               
           }
           
       });
       panel.add(btnAdd,"hmin 30, wmin 90, sg btn");
       panel.add(btnUpdate,"hmin 30, wmin 50");
       panel.add(btnDelete,"hmin 30, wmin 50");
       panel.add(txtSearch,"hmin 30, width 300, gapx 8");
       return panel;
       
    }

    private JPanel setTableDate() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0","fill","fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "[light]background:rgb(255,255,255);"
                + "[dark]background:tint($panel.background,5%);");
    
        loadData();
        setTableProperties();
        hideColumnId();
        JScrollPane scrollpane = new JScrollPane(tblData);
        scrollpane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollpane);
        
    
        return panel;
    }

    private void setTableProperties() {
        TabelUtils.setColumnWidths(tblData, new int []{0}, new int[]{50});
        TabelUtils.setHeaderAlignment(tblData, new int[]{0},  new int[]{JLabel.CENTER}, JLabel.LEFT);
        TabelUtils.setColumnAlignment(tblData, new int[]{0},JLabel.CENTER);
        
        tblData.getTableHeader().putClientProperty(FlatClientProperties.STYLE, ""
                + "height:30;"
                + "hoverBackground:null;"
                + "pressedBackground:null;"
                + "separatorColor:$tableHeader.background");
        
        tblData.putClientProperty(FlatClientProperties.STYLE, ""
                + "rowHeight:30;"
                + "showHorizontalLines:true;"
                + "cellFocusColor:$TableHeader.hoverBackground;"
                + "selectionBackground:$TableHeader.hoverBackground;"
                + "selectionInactiveBackground:$TableHeader.hoverBackground;"
                + "selectionForeground:$TableHeader.hoverBackground;");
    }

    private void loadData() {
        List<Teacher> list = servis.getData();
        tblModel.setData(list);
        tblData.setModel(tblModel);
    }
    
    private void hideColumnId() {
        tblData.getColumnModel().getColumn(1).setMinWidth(0);
        tblData.getColumnModel().getColumn(1).setMaxWidth(0);
        tblData.getColumnModel().getColumn(1).setWidth(0);
    }
    
    

}
