package com.absensi.form.input;

import com.absensi.dao.StudentDAO;
import com.absensi.model.Student;
import com.absensi.model.Kelas;
import com.absensi.model.User;
import com.absensi.form.FormStudent;
import com.absensi.main.FormManager;
import com.absensi.service.ServiceStudent;
import com.absensi.util.AlertUtils; // Anda mungkin menggunakan JOptionPane sebagai gantinya
import com.formdev.flatlaf.FlatClientProperties;
// HAPUS: import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Window;
import java.text.SimpleDateFormat; // Masih digunakan untuk format ke String
import java.time.LocalDate;        // Untuk Raven DatePicker
import java.time.ZoneId;         // Untuk konversi LocalDate ke Date jika diperlukan
import java.time.format.DateTimeFormatter; // Untuk parsing dan formatting LocalDate
import java.util.Date;             // Untuk konversi dari LocalDate jika perlu
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DatePicker; // IMPORT RAVEN DATEPICKER
import raven.modal.ModalDialog;
import raven.modal.Toast;

public class FormInputStudent extends JPanel {

    private final ServiceStudent serviceStudent = new StudentDAO();
    private final StudentDAO studentDaoReference = (StudentDAO) serviceStudent;

    private Student modelStudent;
    private final FormStudent formStudentInstance;
    private final User loggedInUser;

    private JTextField txtNis;
    private JTextField txtStudentName;
    private JComboBox<String> cbxGender;
    private DatePicker datePickerBirthDate; // Menggunakan Raven DatePicker
    private JFormattedTextField dateEditorBirthDate; // Editor untuk DatePicker
    private JTextArea txtAddress;
    private JTextField txtPhone;
    private JComboBox<Kelas> cbxClass;

    // Formatter untuk LocalDate (digunakan oleh Raven DatePicker)
    private final DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public FormInputStudent(Student modelStudent, FormStudent formStudentInstance) {
        this.modelStudent = modelStudent;
        this.formStudentInstance = formStudentInstance;
        this.loggedInUser = FormManager.getLoggedInUser();
        init();

        if (this.modelStudent != null) { // Mode UPDATE
            txtNis.setText(this.modelStudent.getNis());
            txtStudentName.setText(this.modelStudent.getStudentName());
            cbxGender.setSelectedItem(this.modelStudent.getGender());

            String birthDateStr = this.modelStudent.getBirthDate();
            if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
                try {
                    LocalDate birthDate = LocalDate.parse(birthDateStr, localDateFormatter);
                    datePickerBirthDate.setSelectedDate(birthDate);
                } catch (Exception e) { // Tangkap Exception yang lebih umum dari parsing LocalDate
                    System.err.println("Error parsing birth_date from model: '" + birthDateStr + "' - " + e.getMessage());
                    JOptionPane.showMessageDialog(this, "Invalid birth date format in data: " + birthDateStr + ".\nPlease use yyyy-MM-dd.", "Date Format Error", JOptionPane.WARNING_MESSAGE);
                    datePickerBirthDate.clearSelectedDate();
                }
            } else {
                datePickerBirthDate.clearSelectedDate();
            }

            txtAddress.setText(this.modelStudent.getAddress());
            txtPhone.setText(this.modelStudent.getPhone());

            if (this.modelStudent.getKelas() != null && this.modelStudent.getKelas().getIdClass() != 0) {
                for (int i = 0; i < cbxClass.getItemCount(); i++) {
                    Kelas item = cbxClass.getItemAt(i);
                    if (item != null && item.getIdClass() == this.modelStudent.getKelas().getIdClass()) {
                        cbxClass.setSelectedIndex(i);
                        break;
                    }
                }
            } else {
                cbxClass.setSelectedIndex(-1);
            }
        } else { // Mode INSERT
            cbxGender.setSelectedIndex(0);
            cbxClass.setSelectedIndex(-1);
            datePickerBirthDate.clearSelectedDate(); // Kosongkan tanggal
        }
    }

    private void init() {
        setLayout(new BorderLayout());
        Color drawerBackground = UIManager.getColor("Drawer.background");
        setBackground(drawerBackground != null ? drawerBackground : UIManager.getColor("Panel.background"));
        setOpaque(true);
        putClientProperty(FlatClientProperties.STYLE, "border:20,20,20,20;");

        JPanel panelForm = new JPanel(new MigLayout("wrap 2, fillx, gapy 8, gapx 15", "[label]15[grow,fill]"));
        panelForm.setOpaque(false);

        panelForm.add(new JLabel("NIS"));
        txtNis = new JTextField();
        txtNis.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter NIS");
        panelForm.add(txtNis);

        panelForm.add(new JLabel("Student Name"));
        txtStudentName = new JTextField();
        txtStudentName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter student name");
        panelForm.add(txtStudentName);

        panelForm.add(new JLabel("Gender"));
        cbxGender = new JComboBox<>(new String[]{"Male", "Female"});
        panelForm.add(cbxGender);

        panelForm.add(new JLabel("Birth Date"));
        datePickerBirthDate = new DatePicker();
        datePickerBirthDate.setCloseAfterSelected(true);
        dateEditorBirthDate = new JFormattedTextField(); 
       
        datePickerBirthDate.setEditor(dateEditorBirthDate);
        panelForm.add(dateEditorBirthDate); 

        panelForm.add(new JLabel("Address"));
        txtAddress = new JTextArea(3, 20);
        txtAddress.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter address");
        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);
        JScrollPane scrollAddress = new JScrollPane(txtAddress);
        panelForm.add(scrollAddress, "h 60!");

        panelForm.add(new JLabel("Phone"));
        txtPhone = new JTextField();
        txtPhone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter phone number (optional)");
        panelForm.add(txtPhone);

        panelForm.add(new JLabel("Class"));
        cbxClass = new JComboBox<>();
        loadClassesToComboBox();
        panelForm.add(cbxClass);

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

    private void loadClassesToComboBox() {
        List<Kelas> classes = studentDaoReference.getAllActiveClasses();
        DefaultComboBoxModel<Kelas> model = new DefaultComboBoxModel<>();
        for (Kelas kls : classes) {
            model.addElement(kls);
        }
        cbxClass.setModel(model);
        if (this.modelStudent == null || this.modelStudent.getKelas() == null || this.modelStudent.getKelas().getIdClass() == 0) {
             cbxClass.setSelectedIndex(-1);
        }
    }

    private boolean validateInput() {
        Window ownerWindow = SwingUtilities.getWindowAncestor(this);
        if (ownerWindow == null) ownerWindow = FormManager.getFrame();

        if (txtNis.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(ownerWindow, "NIS cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtNis.requestFocusInWindow();
            return false;
        }
        if (txtStudentName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(ownerWindow, "Student Name cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtStudentName.requestFocusInWindow();
            return false;
        }
        if (datePickerBirthDate.getSelectedDate() == null) { // Validasi untuk Raven DatePicker
            JOptionPane.showMessageDialog(ownerWindow, "Birth Date must be selected!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            dateEditorBirthDate.requestFocusInWindow(); // Fokus ke editornya
            return false;
        }
        Object selectedClassItem = cbxClass.getSelectedItem();
        if (selectedClassItem == null || !(selectedClassItem instanceof Kelas) || ((Kelas)selectedClassItem).getIdClass() == 0) {
             JOptionPane.showMessageDialog(ownerWindow, "Class must be selected!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            cbxClass.requestFocusInWindow();
            return false;
        }

        Student tempStudentForValidation = new Student();
        tempStudentForValidation.setNis(txtNis.getText().trim());
        if (modelStudent != null) {
            tempStudentForValidation.setIdStudent(modelStudent.getIdStudent());
        } else {
            tempStudentForValidation.setIdStudent(0);
        }

        if (!serviceStudent.validasiNIS(tempStudentForValidation)) {
            JOptionPane.showMessageDialog(ownerWindow, "NIS '" + tempStudentForValidation.getNis() + "' already exists for another student!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtNis.requestFocusInWindow();
            return false;
        }
        return true;
    }

    private void saveData() {
        if (!validateInput()) {
            return;
        }

        Window ownerWindow = SwingUtilities.getWindowAncestor(this);
        if (ownerWindow == null) ownerWindow = FormManager.getFrame();

        String nis = txtNis.getText().trim();
        String studentName = txtStudentName.getText().trim();
        String gender = (String) cbxGender.getSelectedItem();

        LocalDate selectedLocalDate = datePickerBirthDate.getSelectedDate(); 
        String birthDateStr = selectedLocalDate.format(localDateFormatter); 

        String address = txtAddress.getText().trim();
        String phone = txtPhone.getText().trim();
        Kelas selectedClass = (Kelas) cbxClass.getSelectedItem();

        if (this.modelStudent == null) { // Mode Insert
            Student newStudent = new Student();
            newStudent.setNis(nis);
            newStudent.setStudentName(studentName);
            newStudent.setGender(gender);
            newStudent.setBirthDate(birthDateStr);
            newStudent.setAddress(address);
            newStudent.setPhone(phone);
            newStudent.setKelas(selectedClass);
            if (loggedInUser != null) {
                newStudent.setInsertBy(loggedInUser.getIdUser());
            } else {
                JOptionPane.showMessageDialog(ownerWindow, "User not logged in. Cannot save data.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            serviceStudent.insertData(newStudent);
            Toast.show(ownerWindow, Toast.Type.SUCCESS, "Student data added successfully!", AlertUtils.getOptionAlert());
        } else { 
            this.modelStudent.setNis(nis);
            this.modelStudent.setStudentName(studentName);
            this.modelStudent.setGender(gender);
            this.modelStudent.setBirthDate(birthDateStr);
            this.modelStudent.setAddress(address);
            this.modelStudent.setPhone(phone);
            this.modelStudent.setKelas(selectedClass);
            if (loggedInUser != null) {
                this.modelStudent.setUpdateBy(loggedInUser.getIdUser());
            } else {
                JOptionPane.showMessageDialog(ownerWindow, "User not logged in. Cannot update data.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            serviceStudent.updateData(this.modelStudent);
            Toast.show(ownerWindow, Toast.Type.SUCCESS, "Student data updated successfully!", AlertUtils.getOptionAlert());
        }

        if (formStudentInstance != null) formStudentInstance.refreshTable();
        closeModal();
    }

    private void closeModal() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JDialog) {
            ((JDialog) window).dispose();
        }
        
    }
}