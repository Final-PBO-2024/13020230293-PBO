package com.absensi.form;

import com.absensi.dao.StudentDAO;
import com.absensi.main.AllForms;
import com.absensi.main.Form;
import com.absensi.main.FormManager;
import com.absensi.model.Student;
import com.absensi.model.User;
import com.absensi.service.ServiceStudent;
import com.absensi.tablemodel.TabModStudent; 
import com.absensi.util.AlertUtils;
import com.absensi.util.MessageModal;
import com.absensi.util.TabelUtils;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.component.SimpleModalBorder;

public class FormStudentRestore extends Form {
    private final ServiceStudent serviceStudent = new StudentDAO();
    private final TabModStudent tblModelStudentRestore = new TabModStudent();
    private JTable tblDataStudentRestore = new JTable();
    private JTextField txtSearchStudentRestore;
    private final User loggedInUser;

    public FormStudentRestore() {
        this.loggedInUser = FormManager.getLoggedInUser();
        if (this.loggedInUser == null) {
            JOptionPane.showMessageDialog(this, "User not logged in. Please login first.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx, wrap", "[fill]", "[][][][fill, grow]"));
        putClientProperty(FlatClientProperties.STYLE, "arc:10");

        add(createTitlePanel("Restore Student Data"), "growx");
        add(createSeparator(), "growx");
        add(createButtonPanel(), "growx");
        add(createTablePanel(), "growx, growy");
        loadData();
    }

    private JPanel createTitlePanel(String title) {
        JPanel panel = new JPanel(new MigLayout("fill"));
        JLabel lbTitle = new JLabel(title);
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18");
        panel.add(lbTitle);
        return panel;
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new MigLayout("wrap", "[]push[]"));

        JButton btnRestore = new JButton("Restore");
        btnRestore.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;"
                + "foreground:rgb(255,255,255);"); 
        btnRestore.setIcon(new FlatSVGIcon("com/absensi/icon/recovery.svg", 0.03f));
        btnRestore.setIconTextGap(3);
        btnRestore.addActionListener(e -> restoreSelectedData());

        txtSearchStudentRestore = new JTextField();
        txtSearchStudentRestore.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search Deleted Student...");
        txtSearchStudentRestore.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtSearchStudentRestore.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("raven/modal/demo/icons/search.svg", 0.4f));
        txtSearchStudentRestore.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String keyword = txtSearchStudentRestore.getText().trim();
                if (keyword.isEmpty()) {
                    loadData();
                } else {
                    searchData(keyword);
                }
            }
        });

        panel.add(btnRestore, "hmin 30, wmin 100, sg btnAction");
        panel.add(txtSearchStudentRestore, "hmin 30, width 250::, gapx 8");
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0", "[fill]", "[fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;[light]background:rgb(255,255,255);[dark]background:$Table.background;");

        tblDataStudentRestore.setModel(tblModelStudentRestore);
        tblDataStudentRestore.setFillsViewportHeight(true); 
        setTableProperties(); 
        hideIdColumn();

        JScrollPane scrollPane = new JScrollPane(tblDataStudentRestore);
        scrollPane.getViewport().setBackground(UIManager.getColor("Table.background")); // Sesuaikan background viewport
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, "grow");
        return panel;
    }


    private void setTableProperties() {
        TabelUtils.setColumnWidths(tblDataStudentRestore, new int[]{0}, new int[]{50});
        TabelUtils.setHeaderAlignment(tblDataStudentRestore, new int[]{0}, new int[]{JLabel.CENTER}, JLabel.LEFT);
        TabelUtils.setColumnAlignment(tblDataStudentRestore, new int[]{0}, JLabel.CENTER);

        tblDataStudentRestore.getTableHeader().putClientProperty(FlatClientProperties.STYLE,
                "height:30;hoverBackground:null;pressedBackground:null;"
                + "separatorColor:$tableHeader.background");

        tblDataStudentRestore.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight:30;showHorizontalLines:true;intercellSpacing:0,1;" // intercellSpacing (horizontal, vertical)
                + "cellFocusColor:$TableHeader.hoverBackground;"
                + "selectionBackground:$TableHeader.hoverBackground;"
                + "selectionInactiveBackground:$TableHeader.hoverBackground;"
                + "selectionForeground:$Table.foreground;");
        
    }

    private void hideIdColumn() {
        if (tblDataStudentRestore.getColumnCount() > 1) { // Kolom ID Siswa (indeks 1 di model)
            tblDataStudentRestore.getColumnModel().getColumn(1).setMinWidth(0);
            tblDataStudentRestore.getColumnModel().getColumn(1).setMaxWidth(0);
            tblDataStudentRestore.getColumnModel().getColumn(1).setWidth(0);
        }
    }

    private void loadData() {
        List<Student> list = serviceStudent.getDataIsDelete();
        tblModelStudentRestore.setData(list);
    }

    public void refreshTable() {
        loadData();
    }

    private void searchData(String keyword) {
        List<Student> list = serviceStudent.searchDataIsDelete(keyword);
        tblModelStudentRestore.setData(list);
    }

    private void restoreSelectedData() {
        int selectedRow = tblDataStudentRestore.getSelectedRow();
        if (selectedRow != -1) {
            Student studentToRestore = tblModelStudentRestore.getData(tblDataStudentRestore.convertRowIndexToModel(selectedRow));
            String message = "Are you sure you want to restore student: " + studentToRestore.getStudentName() + "?";
            
            ModalDialog.showModal(SwingUtilities.getWindowAncestor(this), new MessageModal(
                    MessageModal.Type.INFO, 
                    message, 
                    "Confirm Restore", 
                    SimpleModalBorder.YES_NO_OPTION,
                    (controller, action) -> {
                        if (action == SimpleModalBorder.YES_OPTION) {
                            Student forRestore = new Student();
                            forRestore.setIdStudent(studentToRestore.getIdStudent());
                            serviceStudent.restoreData(forRestore);
                            Toast.show(SwingUtilities.getWindowAncestor(FormStudentRestore.this), Toast.Type.SUCCESS, "Student data restored successfully!", AlertUtils.getOptionAlert());
                            refreshTable(); 
                            refreshMainStudentForm(); 
                        }
                    }));
        } else {
            Toast.show(SwingUtilities.getWindowAncestor(this), Toast.Type.INFO, "Please select data to restore", AlertUtils.getOptionAlert());
        }
    }

    private void refreshMainStudentForm() {
        Form mainForm = AllForms.getForm(FormStudent.class);
        if (mainForm instanceof FormStudent) {
            ((FormStudent) mainForm).refreshTable();
        }
    }
}