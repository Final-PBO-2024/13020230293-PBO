package com.absensi.form;

import com.absensi.dao.TeacherDAO;
import com.absensi.form.input.FormInputTeacher;
import com.absensi.main.AllForms;
import com.absensi.main.Form;
import com.absensi.main.FormManager;
import com.absensi.model.Teacher;
import com.absensi.model.User;
import com.absensi.service.ServiceTeacher;
import com.absensi.tablemodel.TabModTeacher;
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

public class FormTeacher extends Form {
    private final ServiceTeacher servis = new TeacherDAO();
    private final TabModTeacher tblModel = new TabModTeacher();
    private JTable tblData = new JTable();
    private JTextField txtSearch;
    private int idTeacher;
    private final User loggedInUser;

    public FormTeacher() {
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
        JLabel lbTitle = new JLabel("Teacher Data");
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
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search..");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("raven/modal/demo/icons/search.svg", 0.4f));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchData();
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
        List<Teacher> list = servis.getData();
        tblModel.setData(list);
        tblData.setModel(tblModel);
    }

    private void hideColumnId() {
        tblData.getColumnModel().getColumn(1).setMinWidth(0);
        tblData.getColumnModel().getColumn(1).setMaxWidth(0);
        tblData.getColumnModel().getColumn(1).setWidth(0);
    }

    private void searchData() {
        String keyword = txtSearch.getText();
        List<Teacher> list = servis.searchData(keyword);
        tblModel.setData(list);
        tblData.setModel(tblModel);
    }

    public void refreshTable() {
        loadData();
    }

    private void insertData() {
        Option option = ModalDialog.createOption();
        ModalDialog.showModal(this, new SimpleModalBorder(new FormInputTeacher(null, this), "Add Teacher"), option, "form input");
    }

    private void updateData() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            Teacher model = tblModel.getData(row);
            Option option = ModalDialog.createOption();
            ModalDialog.showModal(this, new SimpleModalBorder(new FormInputTeacher(model, this), "Update Teacher"), option, "form update");
        } else {
            Toast.show(this, Toast.Type.INFO, "Tolong pilih data yang mau di update", getOptionAlert());
        }
    }

   private void deleteData() {
    int row = tblData.getSelectedRow();
    if (row != -1) {
        Teacher model = tblModel.getData(tblData.convertRowIndexToModel(row)); // Gunakan convertRowIndexToModel
        final int idGuruYangAkanDihapus = model.getIdTeacher();

        String message = "Apakah kamu yakin menghapus data ini?";
        String title = "Konfirmasi";

        ModalDialog.showModal(this, new MessageModal(
                MessageModal.Type.INFO,
                message,
                title,
                SimpleModalBorder.YES_NO_OPTION,
                (controller, action) -> {
                    if (action == SimpleModalBorder.YES_OPTION) {
                        Teacher modelTeacher = new Teacher();
                        modelTeacher.setDeleteBy(loggedInUser.getIdUser());
                        modelTeacher.setIdTeacher(idGuruYangAkanDihapus);

                        servis.deleteData(modelTeacher);
                        Toast.show(this, Toast.Type.SUCCESS, "Data berhasil dihapus!", getOptionAlert());
                        loadData(); // Me-refresh tabel FormTeacher (ini sudah ada)

                        // ===== TAMBAHKAN BLOK KODE INI UNTUK REFRESH FORM RESTORE =====
                        FormTeacherRestore restoreForm = null;
                        Object potentialRestoreForm = AllForms.getForm(FormTeacherRestore.class); // Ambil instance FormTeacherRestore
                             if (potentialRestoreForm instanceof FormTeacherRestore) {
                                restoreForm = (FormTeacherRestore) potentialRestoreForm;
                                 }

                            if (restoreForm != null) { // Cukup cek null saja dulu
                                 restoreForm.refreshTable(); // Panggil metode refreshTable di FormTeacherRestore
                                 }
                                }
                }));
    } else {
        Toast.show(this, Toast.Type.INFO, "Tolong pilih data yang mau dihapus", getOptionAlert());
    }
}
}