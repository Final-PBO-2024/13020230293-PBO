package com.absensi.form.input;

import com.absensi.dao.KelasDAO;
import com.absensi.dao.TeacherDAO;
import com.absensi.form.FormKelas;
import com.absensi.model.Kelas;
import com.absensi.model.Teacher;
import com.absensi.model.User;
import com.absensi.main.FormManager;
import com.absensi.service.ServiceKelas;
import com.absensi.service.ServiceTeacher;
import com.absensi.util.AlertUtils;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;

public class FormInputKelas extends JPanel {

    private final ServiceKelas serviceKelas = new KelasDAO();
    private final ServiceTeacher serviceTeacher = new TeacherDAO();
    private Kelas modelKelas; // Bisa null (untuk insert) atau berisi data (untuk update)
    private final FormKelas formKelasInstance;
    private final User loggedInUser;

    private JTextField txtClassName;
    private JComboBox<Teacher> cbxTeacher;

    public FormInputKelas(Kelas modelKelas, FormKelas formKelasInstance) {
        this.modelKelas = modelKelas;
        this.formKelasInstance = formKelasInstance;
        this.loggedInUser = FormManager.getLoggedInUser();
        init();

        if (this.modelKelas != null) { // Mode Update
            txtClassName.setText(this.modelKelas.getClassName());
            if (this.modelKelas.getTeacher() != null && this.modelKelas.getTeacher().getIdTeacher() != 0) {
                for (int i = 0; i < cbxTeacher.getItemCount(); i++) {
                    Teacher item = cbxTeacher.getItemAt(i);
                    if (item != null && item.getIdTeacher() == this.modelKelas.getTeacher().getIdTeacher()) {
                        cbxTeacher.setSelectedIndex(i);
                        break;
                    }
                }
            } else {
                 cbxTeacher.setSelectedIndex(-1); 
            }
        }
    }

    private void init() {
        setLayout(new BorderLayout());
        Color drawerBackground = UIManager.getColor("Drawer.background");
        if (drawerBackground != null) {
            setBackground(drawerBackground);
        } else {
            setBackground(UIManager.getColor("Panel.background")); 
        }
        setOpaque(true);
        putClientProperty(FlatClientProperties.STYLE, "border:20,20,20,20;");

        JPanel panelForm = new JPanel(new MigLayout("wrap 2, fillx, gap 10", "[grow 0,trail]15[fill]"));
        panelForm.setOpaque(false); 

        JLabel lbClassName = new JLabel("Class Name");
        txtClassName = new JTextField();
        txtClassName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter class name");

        JLabel lbTeacher = new JLabel("Homeroom Teacher");
        cbxTeacher = new JComboBox<>();
        loadTeachersToComboBox();

        panelForm.add(lbClassName);
        panelForm.add(txtClassName, "growx");
        panelForm.add(lbTeacher);
        panelForm.add(cbxTeacher, "growx");

        add(panelForm, BorderLayout.CENTER);

        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.setOpaque(false); 
        panelButton.putClientProperty(FlatClientProperties.STYLE, "border:10,0,0,0");

        JButton btnSave = new JButton("Save");
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255)");
        btnSave.addActionListener(e -> saveData());

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> closeModal());

        panelButton.add(btnSave);
        panelButton.add(btnCancel);

        add(panelButton, BorderLayout.SOUTH);
    }

    private void loadTeachersToComboBox() {
        List<Teacher> teachers = serviceTeacher.getData(); // Mengambil guru yang aktif
        DefaultComboBoxModel<Teacher> model = new DefaultComboBoxModel<>();
        for (Teacher teacher : teachers) {
            model.addElement(teacher);
        }
        cbxTeacher.setModel(model);
        if (this.modelKelas == null || this.modelKelas.getTeacher() == null || this.modelKelas.getTeacher().getIdTeacher() == 0) {
             cbxTeacher.setSelectedIndex(-1); 
        }
    }

    private boolean validateInput() {
        Window ownerWindow = SwingUtilities.getWindowAncestor(this);
        if (txtClassName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(ownerWindow, "Class Name cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtClassName.requestFocus();
            return false;
        }
        Object selectedItem = cbxTeacher.getSelectedItem();
        if (selectedItem == null || !(selectedItem instanceof Teacher) || ((Teacher) selectedItem).getIdTeacher() == 0) {
             if (selectedItem instanceof Teacher && ((Teacher) selectedItem).getIdTeacher() == 0) { // Jika ada placeholder ID 0
                JOptionPane.showMessageDialog(ownerWindow, "Homeroom Teacher must be selected!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                cbxTeacher.requestFocus();
                return false;
             } else if (selectedItem == null) {
                 JOptionPane.showMessageDialog(ownerWindow, "Homeroom Teacher must be selected!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                cbxTeacher.requestFocus();
                return false;
             }
        }
        return true;
    }

    private void saveData() {
        if (!validateInput()) {
            return;
        }

        Window ownerWindow = SwingUtilities.getWindowAncestor(this);
        if (ownerWindow == null) {
            ownerWindow = FormManager.getFrame();
        }

        String className = txtClassName.getText().trim();
        Teacher selectedTeacher = (Teacher) cbxTeacher.getSelectedItem();

        if (this.modelKelas == null) { // Mode Insert
            Kelas newKelasToSave = new Kelas(); 
            newKelasToSave.setClassName(className);
            newKelasToSave.setTeacher(selectedTeacher);
            if (loggedInUser != null) {
                newKelasToSave.setInsertBy(loggedInUser.getIdUser());
            } else {
                JOptionPane.showMessageDialog(ownerWindow, "User not logged in. Cannot save data.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        

            serviceKelas.insertData(newKelasToSave);
            Toast.show(ownerWindow, Toast.Type.SUCCESS, "Data class added successfully!", AlertUtils.getOptionAlert());
        } else { // Mode Update
            this.modelKelas.setClassName(className);
            this.modelKelas.setTeacher(selectedTeacher);
            if (loggedInUser != null) {
                this.modelKelas.setUpdateBy(loggedInUser.getIdUser());
            } else {
                JOptionPane.showMessageDialog(ownerWindow, "User not logged in. Cannot update data.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            serviceKelas.updateData(this.modelKelas);
            Toast.show(ownerWindow, Toast.Type.SUCCESS, "Data class updated successfully!", AlertUtils.getOptionAlert());
        }

        if (formKelasInstance != null) {
            formKelasInstance.refreshTable();
        }
        closeModal();
    }

    private void closeModal() {
        ModalDialog.closeModal("form input");
        ModalDialog.closeModal("form update");
    }
}