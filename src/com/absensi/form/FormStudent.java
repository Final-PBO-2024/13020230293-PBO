package com.absensi.form;

import com.absensi.dao.StudentDAO;
import com.absensi.form.input.FormInputStudent;
import com.absensi.main.AllForms;
import com.absensi.main.Form;
import com.absensi.main.FormManager;
import com.absensi.model.Student;
import com.absensi.model.User;
import com.absensi.service.ServiceStudent;
import com.absensi.tablemodel.TabModStudent;
import com.absensi.util.AlertUtils;
import static com.absensi.util.AlertUtils.getOptionAlert;
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
import raven.modal.option.Option;

public class FormStudent extends Form {
    private final ServiceStudent servis = new StudentDAO();
    private final TabModStudent tblModel = new TabModStudent();
    private JTable tblData = new JTable();
    private JTextField txtSearch;
    private int idStudent;
    private final User loggedInUser;

    public FormStudent() {
        this.loggedInUser = FormManager.getLoggedInUser();
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx, wrap", "[fill]", "[][][][fill, grow]"));
        add(setInfo());
        add(createSeparator());
        add(setButton());
        add(setTableData());
    }

    private JPanel setInfo() {
        JPanel panel = new JPanel(new MigLayout("fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        JLabel lbTitle = new JLabel("Student Data");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18");
        panel.add(lbTitle);
        return panel;
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }

    private JPanel setButton() {
        JPanel panel = new JPanel(new MigLayout("wrap", "[][][]push[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        JButton btnAdd = new JButton("Add");
        btnAdd.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255);");
        btnAdd.setIcon(new FlatSVGIcon("com/absensi/icon/add_white.svg", 0.8f));
        btnAdd.setIconTextGap(5);
        btnAdd.addActionListener(e -> insertData());

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setIcon(new FlatSVGIcon("com/absensi/icon/edit.svg", 0.4f));
        btnUpdate.setIconTextGap(5);
        btnUpdate.addActionListener(e -> updateData());

        JButton btnDelete = new JButton("Delete");
        btnDelete.setIcon(new FlatSVGIcon("com/absensi/icon/delete.svg", 0.4f));
        btnDelete.setIconTextGap(5);
        btnDelete.addActionListener(e -> deleteData());

        txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search NIS, Name, Class...");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("raven/modal/demo/icons/search.svg", 0.4f));
        
        txtSearch.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) {
                loadData(); // Memuat semua data jika kolom pencarian kosong
            } else {
               searchData();
            }
        }
    });
        panel.add(btnAdd, "hmin 30, wmin 90, sg btn");
        panel.add(btnUpdate, "hmin 30, wmin 50");
        panel.add(btnDelete, "hmin 30, wmin 50");
        panel.add(txtSearch, "hmin 30, width 300, gapx 8");
        return panel;
    }

    private JPanel setTableData() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0", "fill", "fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;[light]background:rgb(255,255,255);[dark]background:tint($Panel.background,5%)");

        loadData();
        setTableProperties();
        hideColumnId();

        JScrollPane scrollPane = new JScrollPane(tblData);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane);
        return panel;
    }

    private void setTableProperties() {
        TabelUtils.setColumnWidths(tblData, new int[]{0}, new int[]{50});
        TabelUtils.setHeaderAlignment(tblData, new int[]{0}, new int[]{JLabel.CENTER}, JLabel.LEFT);
        TabelUtils.setColumnAlignment(tblData, new int[]{0}, JLabel.CENTER);

        tblData.getTableHeader().putClientProperty(FlatClientProperties.STYLE,
                "height:30;hoverBackground:null;pressedBackground:null;"
                        + "separatorColor:$tableHeader.background");

        tblData.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight:30;showHorizontalLines:true;intercellSpacing:0,1;"
                        + "cellFocusColor:$TableHeader.hoverBackground;"
                        + "selectionBackground:$TableHeader.hoverBackground;"
                        + "selectionInactiveBackground:$TableHeader.hoverBackground;"
                        + "selectionForeground:$Table.foreground;");
    }

    private void loadData() {
        List<Student> list = servis.getData();
        tblModel.setData(list);
        tblData.setModel(tblModel);
    }

    private void hideColumnId() {
        tblData.getColumnModel().getColumn(1).setMinWidth(0); // Assuming ID is the second column
        tblData.getColumnModel().getColumn(1).setMaxWidth(0);
        tblData.getColumnModel().getColumn(1).setWidth(0);
    }

    private void searchData() {
        String keyword = txtSearch.getText();
        List<Student> list = servis.searchData(keyword);
        tblModel.setData(list);
        tblData.setModel(tblModel);
    }

    public void refreshTable() {
        loadData();
    }

    private void insertData() {
        Option option = ModalDialog.createOption();
        ModalDialog.showModal(this, new SimpleModalBorder(new FormInputStudent(null, this), "Add Student"), option, "form input");
    }

    private void updateData() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            Student model = tblModel.getData(row);
            Option option = ModalDialog.createOption();
            ModalDialog.showModal(this, new SimpleModalBorder(new FormInputStudent(model, this), "Update Student"), option, "form update");
        } else {
            Toast.show(this, Toast.Type.INFO, "Tolong pilih data yang mau di update", getOptionAlert());
        }
    }

    private void deleteData() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            Student model = tblModel.getData(tblData.convertRowIndexToModel(row)); // Use convertRowIndexToModel
            final int idStudentYangAkanDihapus = model.getIdStudent();

            String message = "Apakah kamu yakin menghapus data ini?";
            String title = "Konfirmasi";

            ModalDialog.showModal(this, new MessageModal(
                    MessageModal.Type.INFO,
                    message,
                    title,
                    SimpleModalBorder.YES_NO_OPTION,
                    (controller, action) -> {
                        if (action == SimpleModalBorder.YES_OPTION) {
                            Student modelStudent = new Student();
                            modelStudent.setDeleteBy(loggedInUser.getIdUser());
                            modelStudent.setIdStudent(idStudentYangAkanDihapus);

                            servis.deleteData(modelStudent);
                            Toast.show(this, Toast.Type.SUCCESS, "Data berhasil dihapus!", getOptionAlert());
                            loadData(); // Refresh the table

                            // Refresh FormStudentRestore if it exists
                            FormStudentRestore restoreForm = null;
                            Object potentialRestoreForm = AllForms.getForm(FormStudentRestore.class);
                            if (potentialRestoreForm instanceof FormStudentRestore) {
                                restoreForm = (FormStudentRestore) potentialRestoreForm;
                            }
                            if (restoreForm != null) {
                                restoreForm.refreshTable();
                            }
                        }
                    }));
        } else {
            Toast.show(this, Toast.Type.INFO, "Tolong pilih data yang mau dihapus", getOptionAlert());
        }
    }
}