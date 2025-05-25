package com.absensi.form;

import com.absensi.dao.TeacherDAO;
import com.absensi.main.AllForms;
import com.absensi.main.Form;
import com.absensi.main.FormManager;
import com.absensi.model.Teacher;
import com.absensi.service.ServiceTeacher;
import com.absensi.tablemodel.TabModTeacher;
import static com.absensi.util.AlertUtils.getOptionAlert;
import com.absensi.util.MessageModal;
import com.absensi.util.TabelUtils;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
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
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.component.SimpleModalBorder;

public class FormTeacherRestore extends Form {

    private final ServiceTeacher servis = new TeacherDAO();
    private final TabModTeacher tblModel = new TabModTeacher();
    private JTable tblData = new JTable();
    private JTextField txtSearch;
    private JButton btnRestore;

    public FormTeacherRestore() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx, wrap", "[fill]", "[][][][fill, grow]"));
        add(setInfoPanel());
        add(createSeparator());
        add(setActionsPanel());
        add(setTableData());

        loadDeletedData();
        setTableProperties();
        hideColumnId();
    }

    private JPanel setInfoPanel() {
        JPanel panel = new JPanel(new MigLayout("fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;[light]background:$Panel.background;[dark]background:$Panel.background");

        JLabel lbTitle = new JLabel("Teacher Data Restore");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18");
        panel.add(lbTitle);
        return panel;
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }

    private JPanel setActionsPanel() {
        JPanel panel = new JPanel(new MigLayout("fillx, ins 0", "[][]push[]", "[fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;[light]background:$Panel.background;[dark]background:$Panel.background");

        btnRestore = new JButton("Restore");
        btnRestore.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255);");
        btnRestore.setIcon(new FlatSVGIcon("com/absensi/icon/restore.svg", 0.8f));
        btnRestore.setIconTextGap(5);
        btnRestore.addActionListener(e -> restoreSelectedData());

        txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search ...");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("raven/modal/demo/icons/search.svg", 0.4f));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchDeletedData();
            }
        });

        panel.add(btnRestore, "hmin 30, wmin 90, sg btn");
        panel.add(txtSearch, "hmin 30, width 300, gapx 8");
        return panel;
    }

    private JPanel setTableData() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0", "fill", "fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;[light]background:rgb(255,255,255);[dark]background:tint($Panel.background,5%)");

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
                "height:30;hoverBackground:null;pressedBackground:null;" +
                        "separatorColor:$tableHeader.background");

        tblData.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight:30;showHorizontalLines:true;intercellSpacing:0,1;" +
                        "cellFocusColor:$TableHeader.hoverBackground;" +
                        "selectionBackground:$TableHeader.hoverBackground;" +
                        "selectionInactiveBackground:$TableHeader.hoverBackground;" +
                        "selectionForeground:$Table.foreground;");
    }

    private void hideColumnId() {
        tblData.getColumnModel().getColumn(1).setMinWidth(0);
        tblData.getColumnModel().getColumn(1).setMaxWidth(0);
        tblData.getColumnModel().getColumn(1).setWidth(0);
    }

    private void loadDeletedData() {
        List<Teacher> list = servis.getDataIsDelete();
        tblModel.setData(list);
        tblData.setModel(tblModel);
    }

    private void searchDeletedData() {
        String keyword = txtSearch.getText();
        List<Teacher> list = servis.searchDataIsDelete(keyword);
        tblModel.setData(list);
    }

    private void restoreSelectedData() {
        int selectedRow = tblData.getSelectedRow();
        if (selectedRow != -1) {
            Teacher teacherToRestore = tblModel.getData(tblData.convertRowIndexToModel(selectedRow));

            ModalDialog.showModal(this, new MessageModal(
                    MessageModal.Type.INFO,
                    "Apakah Anda yakin ingin merestore data guru: " + teacherToRestore.getTeacherName() + "?",
                    "Konfirmasi Restore Data",
                    SimpleModalBorder.YES_NO_OPTION,
                    (controller, action) -> {
                        if (action == SimpleModalBorder.YES_OPTION) {
                            servis.restoreData(teacherToRestore);
                            Toast.show(FormTeacherRestore.this, Toast.Type.SUCCESS, "Data guru berhasil direstore!", getOptionAlert());
                            loadDeletedData();

                            FormTeacher mainTeacherForm = null;
                            Object potentialForm = AllForms.getForm(FormTeacher.class);
                            if (potentialForm instanceof FormTeacher) {
                                mainTeacherForm = (FormTeacher) potentialForm;
                            }

                            if (mainTeacherForm != null) {
                                mainTeacherForm.refreshTable();
                            }
                        }
                    }));
        } else {
            Toast.show(this, Toast.Type.WARNING, "Silakan pilih data guru yang ingin direstore terlebih dahulu.", getOptionAlert());
        }
    }

    public void refreshTable() {
        loadDeletedData();
    }
}