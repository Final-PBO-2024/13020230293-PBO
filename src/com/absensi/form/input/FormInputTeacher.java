package com.absensi.form.input;

import com.absensi.dao.TeacherDAO;
import com.absensi.form.FormTeacher;
import com.absensi.main.FormManager;
import com.absensi.model.Teacher;
import com.absensi.model.User;
import com.absensi.service.ServiceTeacher;
import static com.absensi.util.AlertUtils.getOptionAlert;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Component;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DatePicker;
import raven.modal.ModalDialog;
import raven.modal.Toast;

public class FormInputTeacher extends JPanel {

    private JTextField txtName;
    private JTextField txtNip;

    private ButtonGroup groupGender;
    private JRadioButton rbMale;
    private JRadioButton rbFemale;

    private DatePicker datePicker;
    private JFormattedTextField dateEditor;

    private JTextArea txtAddress;
    private JTextField txtPhone;

    private JButton btnSave;
    private JButton btnCancel;

    private final ServiceTeacher servis = new TeacherDAO();
    private Teacher model;
    private FormTeacher formTeacher;
    private int idTeacher;
    private User loggedInUser;

    public FormInputTeacher(Teacher model, FormTeacher formTeacher) {
        init();

        this.loggedInUser = FormManager.getLoggedInUser();
        this.model = model;
        this.formTeacher = formTeacher;
        if (model != null) {
            loadData();
        }
    }

    private void init() {
        // ===== PERBAIKAN DI BARIS INI =====
        setLayout(new MigLayout(
            "fill, insets 5 30 5 30, wrap 2, gap 20px, width 400px!", // Argumen pertama: Layout Constraints
            "[grow, fill][grow, fill]",                               // Argumen kedua: Column Constraints
            "[50][fill, grow]"                                        // Argumen ketiga: Row Constraints
        ));
        // ===== AKHIR PERBAIKAN =====

        txtName = new JTextField();
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter teacher name");
        txtName.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        txtNip = new JTextField();
        txtNip.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter NIP");
        txtNip.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        rbMale = new JRadioButton("Male");
        rbFemale = new JRadioButton("Female");
        groupGender = new ButtonGroup();
        groupGender.add(rbMale);
        groupGender.add(rbFemale);

        rbMale.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radioPanel.add(rbMale);
        radioPanel.add(rbFemale);

        datePicker = new DatePicker();
        datePicker.setCloseAfterSelected(true);
        dateEditor = new JFormattedTextField();
        datePicker.setEditor(dateEditor);

        txtAddress = new JTextArea();
        txtAddress.setWrapStyleWord(true);
        txtAddress.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(txtAddress);

        txtPhone = new JTextField();
        txtPhone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter phone number");
        txtPhone.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        btnSave = new JButton("Save");
        btnSave.setIcon(new FlatSVGIcon("com/absensi/icon/save_white.svg", 0.5f));
        btnSave.setIconTextGap(5);
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255)");

        btnCancel = new JButton("Cancel");
        btnCancel.setIcon(new FlatSVGIcon("com/absensi/icon/cancel.svg", 0.5f));
        btnCancel.setIconTextGap(5);

        add(createSepator(), "span, growx, height 2!");
        add(new JLabel("Teacher Name"), "align right");
        add(txtName);

        add(new JLabel("NIP"), "align right");
        add(txtNip);

        add(new JLabel("Gender"), "align right");
        add(radioPanel);

        add(new JLabel("Birth Date"), "align right");
        add(dateEditor);

        add(new JLabel("Address"), "align right");
        add(scroll, "hmin 100");

        add(new JLabel("Phone Number"), "align right");
        add(txtPhone);

        add(createSepator(), "span, growx, height 2!");
        add(btnSave, "span, split 2, align center, sg btn, hmin 30");
        add(btnCancel, "sg btn, hmin 30");

        btnSave.addActionListener((e) -> {
            if (model == null) {
                insertData();
            } else {
                updateData();
            }
        });

        btnCancel.addActionListener((e) -> {
            if (model == null) {
                ModalDialog.closeModal("form input");
            } else {
                ModalDialog.closeModal("form update");
            }
        });
    }

    private JSeparator createSepator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206)");
        return separator;
    }

    private boolean validasiInput(boolean isUpdate) {
        boolean valid = false;
        String nip = txtNip.getText().trim();
        String currentNIP = isUpdate ? model.getNip() : null;

        if (txtName.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Please enter the teacher's name", getOptionAlert());
        } else if (txtNip.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Please enter the teacher's nip", getOptionAlert());
        } else if (!rbMale.isSelected() && !rbFemale.isSelected()) {
            Toast.show(this, Toast.Type.INFO, "Please select a gender", getOptionAlert());
        } else if (datePicker.getSelectedDate() == null) {
            Toast.show(this, Toast.Type.INFO, "Please select a date", getOptionAlert());
        } else if (txtAddress.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Please enter the teacher's address", getOptionAlert());
        } else if (txtPhone.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Please enter the teacher's phone number", getOptionAlert());
        } else {
            if (isUpdate && nip.equals(currentNIP)) {
                valid = true;
            } else {
                Teacher modelTeacher = new Teacher();
                modelTeacher.setNip(nip);
                if (servis.validasiNIP(modelTeacher)) {
                    valid = true;
                } else {
                    Toast.show(this, Toast.Type.WARNING,
                        "This NIP is already taken\nPlease choose another one",
                        getOptionAlert());
                }
            }
        }

        return valid;
    }

    private void loadData() {
        btnSave.setText("Update");

        idTeacher = model.getIdTeacher();
        txtName.setText(model.getTeacherName());
        txtNip.setText(model.getNip());

        if (model != null) {
            if (model.getGender().equals("Male")) {
                rbMale.setSelected(true);
            } else if (model.getGender().equals("Female")) {
                rbFemale.setSelected(true);
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(model.getBirthDate(), formatter);
        datePicker.setSelectedDate(birthDate);

        txtAddress.setText(model.getAddress());
        txtPhone.setText(model.getPhone());
    }

    private void insertData() {
        if (validasiInput(false)) {
            String name = txtName.getText();
            String nip = txtNip.getText();
            String gender = rbMale.isSelected() ? "Male" : "Female";

            LocalDate selectDate = datePicker.getSelectedDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String birthDate = selectDate.format(formatter);

            String address = txtAddress.getText();
            String phone = txtPhone.getText();

            Teacher modelTeacher = new Teacher();
            modelTeacher.setTeacherName(name);
            modelTeacher.setNip(nip);
            modelTeacher.setGender(gender);
            modelTeacher.setBirthDate(birthDate);
            modelTeacher.setAddress(address);
            modelTeacher.setPhone(phone);
            modelTeacher.setInsertBy(loggedInUser.getIdUser());

            servis.insertData(modelTeacher);
            Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully added", getOptionAlert());

            formTeacher.refreshTable();
            resetForm();
        }
    }

    private void updateData() {
        if (validasiInput(true)) { // Perhatikan di sini saya ubah menjadi true untuk update
            String name = txtName.getText();
            String nip = txtNip.getText();
            String gender = rbMale.isSelected() ? "Male" : "Female";

            LocalDate selectDate = datePicker.getSelectedDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String birthDate = selectDate.format(formatter);

            String address = txtAddress.getText();
            String phone = txtPhone.getText();

            Teacher modelTeacher = new Teacher();
            modelTeacher.setIdTeacher(idTeacher);
            modelTeacher.setTeacherName(name);
            modelTeacher.setNip(nip);
            modelTeacher.setGender(gender);
            modelTeacher.setBirthDate(birthDate);
            modelTeacher.setAddress(address);
            modelTeacher.setPhone(phone);
            modelTeacher.setUpdateBy(loggedInUser.getIdUser());

            servis.updateData(modelTeacher);
            Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully updated", getOptionAlert()); // Pesan update

            formTeacher.refreshTable();
            resetForm();
            ModalDialog.closeModal("form update");
        }
    }

    private void resetForm() {
        txtName.setText("");
        txtNip.setText("");
        groupGender.clearSelection();
        datePicker.clearSelectedDate();
        txtAddress.setText("");
        txtPhone.setText("");
    }
}