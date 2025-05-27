package com.absensi.form;

import com.absensi.dao.KelasDAO;
import com.absensi.form.input.FormInputKelas;
import com.absensi.main.AllForms;
import com.absensi.main.Form; 
import com.absensi.main.FormManager;
import com.absensi.model.Kelas;
import com.absensi.model.User;
import com.absensi.service.ServiceKelas;
import com.absensi.tablemodel.TabModKelas;
import com.absensi.util.AlertUtils;
import com.absensi.util.MessageModal;
import com.absensi.util.TabelUtils;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color; 
import java.awt.Dimension; 
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.Option;

public class FormKelas extends Form {
    private final ServiceKelas serviceKelas = new KelasDAO();
    private final TabModKelas tblModelKelas = new TabModKelas();
    private JTable tblDataKelas = new JTable();
    private JTextField txtSearchKelas;
    private final User loggedInUser;

    public FormKelas() {
        this.loggedInUser = FormManager.getLoggedInUser();
        if (this.loggedInUser == null) {
            JOptionPane.showMessageDialog(null, "User not logged in. Please login first.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx, wrap", "[fill]", "[][][][fill, grow]"));
        putClientProperty(FlatClientProperties.STYLE, "arc:10");

        add(setInfoPanel(), "growx");
        add(createSeparatorLine(), "growx");
        add(setButtonPanel(), "growx");
        add(setTableDataPanel(), "growx, growy");

        loadDataToTable();
    }

    private JPanel setInfoPanel() {
        JPanel panel = new JPanel(new MigLayout("fill"));
        JLabel lbTitle = new JLabel("Class Data");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18");
        panel.add(lbTitle);
        return panel;
    }

    private JSeparator createSeparatorLine() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }

    private JPanel setButtonPanel() {
        JPanel panel = new JPanel(new MigLayout("wrap", "[][][]push[]"));

        JButton btnAdd = new JButton("Add");
        btnAdd.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255);");
        btnAdd.setIcon(new FlatSVGIcon("com/absensi/icon/add_white.svg", 0.8f));
        btnAdd.setIconTextGap(5);
        btnAdd.addActionListener(e -> openFormInputAdd());

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setIcon(new FlatSVGIcon("com/absensi/icon/edit.svg", 0.4f));
        btnUpdate.setIconTextGap(5);
        btnUpdate.addActionListener(e -> openFormInputUpdate());

        JButton btnDelete = new JButton("Delete");
        btnDelete.setIcon(new FlatSVGIcon("com/absensi/icon/delete.svg", 0.4f));
        btnDelete.setIconTextGap(5);
        btnDelete.addActionListener(e -> deleteDataKelas());

        txtSearchKelas = new JTextField();
        txtSearchKelas.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search Class or Teacher...");
        txtSearchKelas.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtSearchKelas.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("raven/modal/demo/icons/search.svg", 0.4f));
        txtSearchKelas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchDataInTable();
            }
        });

        panel.add(btnAdd, "hmin 30, wmin 90, sg btnAction");
        panel.add(btnUpdate, "hmin 30, wmin 90, sg btnAction");
        panel.add(btnDelete, "hmin 30, wmin 90, sg btnAction");
        panel.add(txtSearchKelas, "hmin 30, width 250::, gapx 8");
        return panel;
    }

    private JPanel setTableDataPanel() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0", "[fill]", "[fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE, 
            "arc:10;" +
            "[light]background:rgb(255,255,255);" +
            "[dark]background:$Table.background;");

        tblDataKelas.setModel(tblModelKelas);
        tblDataKelas.setFillsViewportHeight(true); 
        setTableProperties();
        hideColumnIdKelas();

        JScrollPane scrollPane = new JScrollPane(tblDataKelas);
        scrollPane.getViewport().setBackground(UIManager.getColor("Table.background")); 
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, "grow");
        return panel;
    }

    private void setTableProperties() {
        TabelUtils.setColumnWidths(tblDataKelas, new int[]{0}, new int[]{50}); 
        TabelUtils.setHeaderAlignment(tblDataKelas, new int[]{0}, new int[]{JLabel.CENTER}, JLabel.LEFT);
        TabelUtils.setColumnAlignment(tblDataKelas, new int[]{0}, JLabel.CENTER);

        tblDataKelas.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS); 

        tblDataKelas.getTableHeader().putClientProperty(FlatClientProperties.STYLE,
                "height:30;hoverBackground:null;pressedBackground:null;"
                + "separatorColor:$Separator.borderColor;" 
                + "[dark]background:lighten($TableHeader.background,3%);" 
                + "[light]background:darken($TableHeader.background,3%)"  
        );
        
        tblDataKelas.setShowGrid(false); 
        tblDataKelas.setIntercellSpacing(new Dimension(0, 1)); 

        tblDataKelas.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight:30;"
                + "showHorizontalLines:true;"  
                + "showVerticalLines:false;"   
                + "gridColor:$Separator.borderColor;" 
                + "selectionBackground:$TableHeader.hoverBackground;"
                + "selectionInactiveBackground:$TableHeader.hoverBackground;"
                + "selectionForeground:$Table.foreground;"
        );
        tblDataKelas.putClientProperty("Table.showCellFocusIndicator", false);
        // tblDataKelas.putClientProperty("Table.striped", true); // Aktifkan jika ingin striping, matikan showHorizontalLines
    }
    
    private void hideColumnIdKelas() {
        if (tblDataKelas.getColumnCount() > 1) { 
            tblDataKelas.getColumnModel().getColumn(1).setMinWidth(0);
            tblDataKelas.getColumnModel().getColumn(1).setMaxWidth(0);
            tblDataKelas.getColumnModel().getColumn(1).setWidth(0);
        }
    }

    private void loadDataToTable() {
        List<Kelas> list = serviceKelas.getData();
        tblModelKelas.setData(list);
    }

    public void refreshTable() {
        loadDataToTable();
    }

    private void searchDataInTable() {
        String keyword = txtSearchKelas.getText();
        List<Kelas> list = serviceKelas.searchData(keyword);
        tblModelKelas.setData(list);
    }

    private void openFormInputAdd() {
        Option option = ModalDialog.createOption();
        ModalDialog.showModal(SwingUtilities.getWindowAncestor(this), 
                              new SimpleModalBorder(new FormInputKelas(null, this), "Add Class Data"), 
                              option, "form input");
    }

    private void openFormInputUpdate() {
        int selectedRow = tblDataKelas.getSelectedRow();
        if (selectedRow != -1) {
            Kelas modelToUpdate = tblModelKelas.getData(tblDataKelas.convertRowIndexToModel(selectedRow));
            Option option = ModalDialog.createOption();
            ModalDialog.showModal(SwingUtilities.getWindowAncestor(this), 
                                  new SimpleModalBorder(new FormInputKelas(modelToUpdate, this), "Update Class Data"), 
                                  option, "form update");
        } else {
            Toast.show(SwingUtilities.getWindowAncestor(this), Toast.Type.INFO, "Please select data to update", AlertUtils.getOptionAlert());
        }
    }

    private void deleteDataKelas() {
        int selectedRow = tblDataKelas.getSelectedRow();
        if (selectedRow != -1) {
            Kelas modelToDelete = tblModelKelas.getData(tblDataKelas.convertRowIndexToModel(selectedRow));
            
            if (serviceKelas.isClassReferencedInStudents(modelToDelete.getIdClass())) {
                 String infoMessage = "<html>Class <b>" + modelToDelete.getClassName() + "</b> cannot be deleted because it is still referenced by active students.<br>" +
                                     "Please remove or reassign students from this class first.</html>";
                ModalDialog.showModal(SwingUtilities.getWindowAncestor(this), new MessageModal(
                        MessageModal.Type.WARNING,
                        infoMessage,
                        "Deletion Prevented",
                        SimpleModalBorder.CANCEL_OPTION, 
                        (controller, action) -> { }
                ));
                return; 
            }

            String message = "Are you sure you want to delete class: " + modelToDelete.getClassName() + "?";
            String title = "Confirm Delete";

            ModalDialog.showModal(SwingUtilities.getWindowAncestor(this), new MessageModal(
                    MessageModal.Type.INFO, 
                    message,
                    title,
                    SimpleModalBorder.YES_NO_OPTION,
                    (controller, action) -> {
                        if (action == SimpleModalBorder.YES_OPTION) {
                            Kelas forDeletion = new Kelas();
                            forDeletion.setIdClass(modelToDelete.getIdClass());
                            if (loggedInUser != null) {
                                forDeletion.setDeleteBy(loggedInUser.getIdUser());
                            } else {
                                Toast.show(SwingUtilities.getWindowAncestor(FormKelas.this), Toast.Type.ERROR, "User not logged in. Cannot delete data.", AlertUtils.getOptionAlert());
                                return;
                            }

                            serviceKelas.deleteData(forDeletion); 
                            Toast.show(SwingUtilities.getWindowAncestor(FormKelas.this), Toast.Type.SUCCESS, "Class data deleted successfully!", AlertUtils.getOptionAlert());
                            refreshTable(); 

                            // Mencoba merefresh FormKelasRestore
                            Form targetRestoreForm = AllForms.getForm(FormKelasRestore.class);
                            if (targetRestoreForm instanceof FormKelasRestore) {
                                ((FormKelasRestore) targetRestoreForm).refreshTable();
                            }
                        }
                    }
            ));
        } else {
            Toast.show(SwingUtilities.getWindowAncestor(this), Toast.Type.INFO, "Please select data to delete", AlertUtils.getOptionAlert());
        }
    }
}