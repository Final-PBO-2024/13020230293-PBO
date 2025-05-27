package com.absensi.form;

import com.absensi.dao.KelasDAO;
import com.absensi.main.AllForms;
import com.absensi.main.Form; // Pastikan import Form superclass sudah benar
import com.absensi.main.FormManager;
import com.absensi.model.Kelas;
import com.absensi.model.User;
import com.absensi.service.ServiceKelas;
import com.absensi.tablemodel.TabModKelas; // Menggunakan TableModel yang sama
import com.absensi.util.AlertUtils;
import com.absensi.util.MessageModal;
import com.absensi.util.TabelUtils;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.component.SimpleModalBorder;

public class FormKelasRestore extends Form {
    private final ServiceKelas serviceKelas = new KelasDAO();
    private final TabModKelas tblModelKelasRestore = new TabModKelas();
    private JTable tblDataKelasRestore = new JTable();
    private JTextField txtSearchKelasRestore;
    private final User loggedInUser; // Meskipun mungkin tidak banyak digunakan di restore, baik untuk konsistensi

    public FormKelasRestore() {
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

        add(setInfoPanelRestore(), "growx");
        add(createSeparatorLineRestore(), "growx");
        add(setButtonPanelRestore(), "growx");
        add(setTableDataPanelRestore(), "growx, growy");

        loadDataToTableRestore();
    }

    private JPanel setInfoPanelRestore() {
        JPanel panel = new JPanel(new MigLayout("fill"));
        JLabel lbTitle = new JLabel("Restore Class Data");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18");
        panel.add(lbTitle);
        return panel;
    }

    private JSeparator createSeparatorLineRestore() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }

    private JPanel setButtonPanelRestore() {
        JPanel panel = new JPanel(new MigLayout("wrap", "[]push[]")); 

        JButton btnRestore = new JButton("Restore");
        // Anda bisa menggunakan warna tema atau warna spesifik untuk tombol restore
        btnRestore.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;"
                + "foreground:rgb(255,255,255);"); // Contoh: hijau
        btnRestore.setIcon(new FlatSVGIcon("com/absensi/icon/recovery.svg", 0.03f));
        btnRestore.setIconTextGap(3);
        btnRestore.addActionListener(e -> restoreDataKelas());

        txtSearchKelasRestore = new JTextField();
        txtSearchKelasRestore.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search Deleted Class...");
        txtSearchKelasRestore.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtSearchKelasRestore.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("raven/modal/demo/icons/search.svg", 0.4f));
        txtSearchKelasRestore.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchDataInTableRestore();
            }
        });

        panel.add(btnRestore, "hmin 30, wmin 100, sg btnAction");
        panel.add(txtSearchKelasRestore, "hmin 30, width 250::, gapx 8");
        return panel;
    }

    private JPanel setTableDataPanelRestore() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0", "[fill]", "[fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE, 
            "arc:10;" +
            "[light]background:rgb(255,255,255);" +
            "[dark]background:$Table.background;");

        tblDataKelasRestore.setModel(tblModelKelasRestore);
        tblDataKelasRestore.setFillsViewportHeight(true);
        setTablePropertiesRestore(); // Menggunakan metode styling tabel yang sama atau serupa
        hideColumnIdKelasRestore();

        JScrollPane scrollPane = new JScrollPane(tblDataKelasRestore);
        scrollPane.getViewport().setBackground(UIManager.getColor("Table.background"));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, "grow");
        return panel;
    }

    // Menggunakan styling tabel yang sama dengan FormKelas untuk konsistensi
    private void setTablePropertiesRestore() {
        TabelUtils.setColumnWidths(tblDataKelasRestore, new int[]{0}, new int[]{50}); 
        TabelUtils.setHeaderAlignment(tblDataKelasRestore, new int[]{0}, new int[]{JLabel.CENTER}, JLabel.LEFT);
        TabelUtils.setColumnAlignment(tblDataKelasRestore, new int[]{0}, JLabel.CENTER);

        tblDataKelasRestore.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS); 

        tblDataKelasRestore.getTableHeader().putClientProperty(FlatClientProperties.STYLE,
                "height:30;hoverBackground:null;pressedBackground:null;"
                + "separatorColor:$Separator.borderColor;" 
                + "[dark]background:lighten($TableHeader.background,3%);" 
                + "[light]background:darken($TableHeader.background,3%)"  
        );
        
        tblDataKelasRestore.setShowGrid(false); 
        tblDataKelasRestore.setIntercellSpacing(new Dimension(0, 1));

        tblDataKelasRestore.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight:30;"
                + "showHorizontalLines:true;"  
                + "showVerticalLines:false;"   
                + "gridColor:$Separator.borderColor;" 
                + "selectionBackground:$TableHeader.hoverBackground;"
                + "selectionInactiveBackground:$TableHeader.hoverBackground;"
                + "selectionForeground:$Table.foreground;"
        );
        tblDataKelasRestore.putClientProperty("Table.showCellFocusIndicator", false);
        // tblDataKelasRestore.putClientProperty("Table.striped", true); // Bisa diaktifkan juga di sini
    }
    
    private void hideColumnIdKelasRestore() {
         if (tblDataKelasRestore.getColumnCount() > 1) {
            tblDataKelasRestore.getColumnModel().getColumn(1).setMinWidth(0);
            tblDataKelasRestore.getColumnModel().getColumn(1).setMaxWidth(0);
            tblDataKelasRestore.getColumnModel().getColumn(1).setWidth(0);
        }
    }

    private void loadDataToTableRestore() {
        List<Kelas> list = serviceKelas.getDataIsDelete();
        tblModelKelasRestore.setData(list);
    }

    public void refreshTable() {
        loadDataToTableRestore();
    }

    private void searchDataInTableRestore() {
        String keyword = txtSearchKelasRestore.getText();
        List<Kelas> list = serviceKelas.searchDataIsDelete(keyword);
        tblModelKelasRestore.setData(list);
    }

    private void restoreDataKelas() {
        int selectedRow = tblDataKelasRestore.getSelectedRow();
        if (selectedRow != -1) {
            Kelas modelToRestore = tblModelKelasRestore.getData(tblDataKelasRestore.convertRowIndexToModel(selectedRow));

            String message = "Are you sure you want to restore class: " + modelToRestore.getClassName() + "?";
            String title = "Confirm Restore";

            ModalDialog.showModal(SwingUtilities.getWindowAncestor(this), new MessageModal(
                    MessageModal.Type.INFO,
                    message,
                    title,
                    SimpleModalBorder.YES_NO_OPTION,
                    (controller, action) -> {
                        if (action == SimpleModalBorder.YES_OPTION) {
                            serviceKelas.restoreData(modelToRestore); // Tidak perlu set user_id disini, DAO handle
                            Toast.show(SwingUtilities.getWindowAncestor(FormKelasRestore.this), Toast.Type.SUCCESS, "Class data restored successfully!", AlertUtils.getOptionAlert());
                            refreshTable(); // Refresh tabel restore ini

                            // Refresh FormKelas jika ada dan dikelola oleh AllForms
                            Form targetKelasForm = AllForms.getForm(FormKelas.class);
                            if (targetKelasForm instanceof FormKelas) {
                               ((FormKelas) targetKelasForm).refreshTable();
                            }
                        }
                    }
            ));
        } else {
            Toast.show(SwingUtilities.getWindowAncestor(this), Toast.Type.INFO, "Please select data to restore", AlertUtils.getOptionAlert());
        }
    }
}