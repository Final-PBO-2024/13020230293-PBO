package com.absensi.form; // Pastikan package ini sesuai dengan struktur Anda

import com.absensi.dao.TeacherDAO;
import com.absensi.main.AllForms;
import com.absensi.main.Form;
import com.absensi.main.FormManager;
import com.absensi.model.Teacher;
import com.absensi.model.User;
import com.absensi.service.ServiceTeacher;
import com.absensi.tablemodel.TabModTeacher; 
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

public class FormTeacherRestore extends Form {
    private final ServiceTeacher serviceTeacher = new TeacherDAO();
    private final TabModTeacher tblModelTeacherRestore = new TabModTeacher();
    private JTable tblDataTeacherRestore = new JTable(); // Variabel tabel untuk form ini
    private JTextField txtSearchTeacherRestore;
    private final User loggedInUser;

    public FormTeacherRestore() {
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

        add(createTitlePanel("Teacher Data Restore"), "growx");
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

   // ... (bagian lain dari FormTeacherRestore.java tetap sama)

private JPanel createButtonPanel() {
    JPanel panel = new JPanel(new MigLayout("wrap", "[][]push[]", "")); // 2 Tombol di kiri, push, Search di kanan

   
    JButton btnRestore = new JButton("Restore");
    btnRestore.putClientProperty(FlatClientProperties.STYLE,
            "background:@accentColor;foreground:rgb(255,255,255);");
    btnRestore.setIcon(new FlatSVGIcon("com/absensi/icon/recovery.svg", 0.03f));
    btnRestore.setIconTextGap(3);
    btnRestore.addActionListener(e -> restoreSelectedData());

    
    JButton btnDeletePerm = new JButton("Delete");
   
    btnDeletePerm.putClientProperty(FlatClientProperties.STYLE, "background:rgb(220,53,69);"
            + "foreground:rgb(255,255,255);");
    btnDeletePerm.setIcon(new FlatSVGIcon("com/absensi/icon/bin.svg", 0.03f));
    btnDeletePerm.setIconTextGap(3);
    btnDeletePerm.addActionListener(e -> permanentDeleteSelectedData()); 

    // TextField Search
    txtSearchTeacherRestore = new JTextField();
    txtSearchTeacherRestore.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search Deleted Teacher...");
    txtSearchTeacherRestore.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
    txtSearchTeacherRestore.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
            new FlatSVGIcon("raven/modal/demo/icons/search.svg", 0.4f)); 

  
    txtSearchTeacherRestore.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            String keyword = txtSearchTeacherRestore.getText().trim();
            if (keyword.isEmpty()) {
                loadData(); // Memuat semua data jika kolom pencarian kosong
            } else {
                searchData(keyword); // Melakukan pencarian berdasarkan keyword
            }
        }
    });
   
    panel.add(btnRestore, "hmin 30, wmin 100, sg btnAction");
    panel.add(btnDeletePerm, "hmin 30, wmin 100, sg btnAction");
     panel.add(txtSearchTeacherRestore, "hmin 30, width 300, gapx 8");

    return panel;
}

// ... (sisa kode FormTeacherRestore.java Anda)



    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0", "[fill]", "[fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;[light]background:rgb(255,255,255);[dark]background:$Table.background;");

        tblDataTeacherRestore.setModel(tblModelTeacherRestore);
        tblDataTeacherRestore.setFillsViewportHeight(true);
        setTableProperties(); // Memanggil metode styling tabel yang Anda berikan
        hideIdColumn();

        JScrollPane scrollPane = new JScrollPane(tblDataTeacherRestore);
        scrollPane.getViewport().setBackground(UIManager.getColor("Table.background"));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, "grow");
        return panel;
    }

    private void setTableProperties() {
        TabelUtils.setColumnWidths(tblDataTeacherRestore, new int[]{0}, new int[]{50});
        TabelUtils.setHeaderAlignment(tblDataTeacherRestore, new int[]{0}, new int[]{JLabel.CENTER}, JLabel.LEFT);
        TabelUtils.setColumnAlignment(tblDataTeacherRestore, new int[]{0}, JLabel.CENTER);

        tblDataTeacherRestore.getTableHeader().putClientProperty(FlatClientProperties.STYLE,
                "height:30;hoverBackground:null;pressedBackground:null;"
                + "separatorColor:$tableHeader.background");

        tblDataTeacherRestore.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight:30;showHorizontalLines:true;intercellSpacing:0,1;" // intercellSpacing (horizontal, vertical)
                + "cellFocusColor:$TableHeader.hoverBackground;"
                + "selectionBackground:$TableHeader.hoverBackground;"
                + "selectionInactiveBackground:$TableHeader.hoverBackground;"
                + "selectionForeground:$Table.foreground;");
        
  
    }

    private void hideIdColumn() {
        // Kolom untuk Teacher: "No"(0), "ID Teacher"(1-hidden), "Teacher Name"(2), "NIP"(3), dst.
        if (tblDataTeacherRestore.getColumnCount() > 1) { 
            tblDataTeacherRestore.getColumnModel().getColumn(1).setMinWidth(0);
            tblDataTeacherRestore.getColumnModel().getColumn(1).setMaxWidth(0);
            tblDataTeacherRestore.getColumnModel().getColumn(1).setWidth(0);
        }
    }

    private void loadData() {
        List<Teacher> list = serviceTeacher.getDataIsDelete();
        tblModelTeacherRestore.setData(list);
    }

    public void refreshTable() {
        loadData();
    }

    private void searchData(String keyword) {
        List<Teacher> list = serviceTeacher.searchDataIsDelete(keyword);
        tblModelTeacherRestore.setData(list);
    }

    private void restoreSelectedData() {
        int selectedRow = tblDataTeacherRestore.getSelectedRow();
        if (selectedRow != -1) {
            Teacher teacherToRestore = tblModelTeacherRestore.getData(tblDataTeacherRestore.convertRowIndexToModel(selectedRow));
            String message = "Are you sure you want to restore teacher: " + teacherToRestore.getTeacherName() + "?";
            ModalDialog.showModal(SwingUtilities.getWindowAncestor(this), new MessageModal(
                    MessageModal.Type.INFO, message, "Confirm Restore", SimpleModalBorder.YES_NO_OPTION,
                    (controller, action) -> {
                        if (action == SimpleModalBorder.YES_OPTION) {
                            Teacher forRestore = new Teacher(); 
                            forRestore.setIdTeacher(teacherToRestore.getIdTeacher());

                            serviceTeacher.restoreData(forRestore);
                            Toast.show(SwingUtilities.getWindowAncestor(FormTeacherRestore.this), Toast.Type.SUCCESS, "Teacher data restored successfully!", AlertUtils.getOptionAlert());
                            refreshTable(); 
                            refreshMainTeacherForm(); 
                        }
                    }));
        } else {
            Toast.show(SwingUtilities.getWindowAncestor(this), Toast.Type.INFO, "Please select data to restore", AlertUtils.getOptionAlert());
        }
    }

    private void permanentDeleteSelectedData() {
        int selectedRow = tblDataTeacherRestore.getSelectedRow();
        if (selectedRow != -1) {
            Teacher teacherToDelete = tblModelTeacherRestore.getData(tblDataTeacherRestore.convertRowIndexToModel(selectedRow));
            
            String warningMessage = "<html><b>WARNING!</b><br>" +
                                    "You are about to permanently delete teacher: <b>" + teacherToDelete.getTeacherName() + "</b>.<br>" +
                                    "This action CANNOT be undone. Are you absolutely sure?</html>";
            
            ModalDialog.showModal(SwingUtilities.getWindowAncestor(this), new MessageModal(
                    MessageModal.Type.WARNING, 
                    warningMessage, 
                    "Confirm Permanent Deletion", 
                    SimpleModalBorder.YES_NO_OPTION, 
                    (controller, action) -> {
                        if (action == SimpleModalBorder.YES_OPTION) {
                            int confirmSecond = JOptionPane.showConfirmDialog(
                                SwingUtilities.getWindowAncestor(FormTeacherRestore.this),
                                "Please confirm one more time: Permanently delete " + teacherToDelete.getTeacherName() + "?",
                                "Final Confirmation",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE
                            );

                            if (confirmSecond == JOptionPane.YES_OPTION) {
                                try {
                                    Teacher forPermanentDelete = new Teacher();
                                    forPermanentDelete.setIdTeacher(teacherToDelete.getIdTeacher());
                                    
                                    serviceTeacher.permanentDeleteData(forPermanentDelete);
                                    Toast.show(SwingUtilities.getWindowAncestor(FormTeacherRestore.this), Toast.Type.SUCCESS, "Teacher data permanently deleted!", AlertUtils.getOptionAlert());
                                    refreshTable(); 
                                } catch (Exception ex) {
                                    // Tangani jika DAO melempar exception (misal karena foreign key constraint)
                                    String errorMessage = ex.getMessage();
                                    if (errorMessage != null && errorMessage.toLowerCase().contains("foreign key constraint")) {
                                        errorMessage = "Cannot delete this teacher permanently as they are still referenced in other data (e.g., as a homeroom teacher).";
                                    } else if (errorMessage == null) {
                                        errorMessage = "An unexpected error occurred during permanent deletion.";
                                    }
                                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(FormTeacherRestore.this),
                                        errorMessage,
                                        "Deletion Error", JOptionPane.ERROR_MESSAGE);
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }));
        } else {
            Toast.show(SwingUtilities.getWindowAncestor(this), Toast.Type.INFO, "Please select data to permanently delete", AlertUtils.getOptionAlert());
        }
    }   

    private void refreshMainTeacherForm() {
        Form mainForm = AllForms.getForm(FormTeacher.class); // Ganti dengan kelas FormTeacher utama Anda
        if (mainForm instanceof FormTeacher) {
            ((FormTeacher) mainForm).refreshTable();
        }
    }
}