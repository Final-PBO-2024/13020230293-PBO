package com.absensi.auth;

import com.absensi.dao.UserDAO;
import com.absensi.main.Form;
import com.absensi.main.FormManager;
import com.absensi.model.User;
import com.absensi.service.ServiceUser;
import static com.absensi.util.AlertUtils.getOptionAlert;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

public class FormRegistrasi extends Form {
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox cbxRole;
    
    private JLabel lbPhotos;
    private String filePath;

    private JButton btnBrowse;
    private JButton btnRegistrasi;
    
    private final ServiceUser servis = new UserDAO();
    
    public FormRegistrasi() {
        init();
    }
    
    private void init() {
    setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));
    
    JPanel mainPanel = new JPanel(new MigLayout("insets 50", "[][]", "[fill][grow]"));
    mainPanel.putClientProperty(FlatClientProperties.STYLE, ""
            + "arc:20;"
            + "[light]background:rgb(255,255,255);"
            + "[dark]background:darken($Panel.background,5%)");
    
    JPanel panelLogo = new JPanel(new MigLayout("fill", "300,center", "[center]"));
    panelLogo.putClientProperty(FlatClientProperties.STYLE, ""
            + "[light]background:rgb(255,255,255);"
            + "[dark]background:darken($Panel.background,5%)");
    
    JPanel panelLogos = new JPanel(new MigLayout("fill,wrap", "center", "[center]"));
    panelLogos.putClientProperty(FlatClientProperties.STYLE, ""
            + "[light]background:rgb(255,255,255);"
            + "[dark]background:darken($Panel.background,5%)");
    
    JLabel imageLogo = new JLabel(new FlatSVGIcon("com/absensi/icon/logosekolah.svg", 100, 100));
    JLabel lbTitleLogo = new JLabel("MyAbsensi");
    lbTitleLogo.putClientProperty(FlatClientProperties.STYLE, ""
            + "[light]foreground:@accentColor;"
            + "font:bold italic +14");
    
    JLabel lbDetail = new JLabel("Attendance Management System");
    lbDetail.putClientProperty(FlatClientProperties.STYLE, ""
            + "[light]foreground:rgb(51,51,51);"
            + "font:bold 16");
    
    JLabel lbCreated = new JLabel("Created by @rhannprtamaa");
    lbCreated.putClientProperty(FlatClientProperties.STYLE, ""
            + "[light]foreground:rgb(162,178,189);"
            + "font:bold 12");
    
    
    panelLogos.add(imageLogo);
    panelLogos.add(lbTitleLogo);
    panelLogos.add(lbDetail);
    panelLogos.add(lbCreated);
    panelLogo.add(panelLogos);
    mainPanel.add(panelLogo);
    
    JPanel panelForm = new JPanel(new MigLayout("wrap, insets 20", "[grow, fill]"));
    panelForm.putClientProperty(FlatClientProperties.STYLE, ""
            + "arc:20;"
            + "[light]background:@accentColor;"
            + "[dark]background:darken($Panel.background,5%)");

        txtName = new JTextField();
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter the name");
        txtName.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        txtEmail = new JTextField();
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter the email");
        txtEmail.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        txtUsername = new JTextField();
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username");
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");
        txtPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtPassword.putClientProperty(FlatClientProperties.STYLE, ""
            + "showRevealButton:true;"
            + "showCapsLock:true;");

        cbxRole = new JComboBox();

        btnRegistrasi = new JButton("Registrasi");
        btnRegistrasi.putClientProperty(FlatClientProperties.STYLE, ""
            + "background:rgb(255,255,255);"
            + "foreground:@accentColor;"
            + "borderWidth:0;"
            + "focusWidth:0;"
            + "innerFocusWidth:0;"
            + "font:bold 16");
        
        JLabel lbTitleForm = new JLabel("Registrasi", JLabel.CENTER);
        lbTitleForm.putClientProperty(FlatClientProperties.STYLE,""
                + "[light]foreground:rgb(255,255,255);"
                + "font:bold +10" );
        
        JLabel lbDescription = new JLabel("Register now to simplify attendance management", JLabel.CENTER);
        lbDescription.putClientProperty(FlatClientProperties.STYLE,""
                + "[light]foreground:rgb(255,255,255)");
        
        JLabel lbName = new JLabel("Full Name");
        lbName.putClientProperty(FlatClientProperties.STYLE,"[light]foreground:rgb(255,255,255)");

        JLabel lbEmail = new JLabel("Email");
        lbEmail.putClientProperty(FlatClientProperties.STYLE, "[light]foreground:rgb(255,255,255)");

        JLabel lbUsername = new JLabel("Username");
        lbUsername.putClientProperty(FlatClientProperties.STYLE,"[light]foreground:rgb(255,255,255)");

        JLabel lbPassword = new JLabel("Password");
        lbPassword.putClientProperty(FlatClientProperties.STYLE, "[light]foreground:rgb(255,255,255)");

        JLabel lbRole = new JLabel("Role");
        lbRole.putClientProperty(FlatClientProperties.STYLE, "[light]foreground:rgb(255,255,255)");

        JLabel lbPhoto = new JLabel("Photo");
        lbPhoto.putClientProperty(FlatClientProperties.STYLE, "[light]foreground:rgb(255,255,255)");
        
        panelForm.add(lbTitleForm);
        panelForm.add(lbDescription);
        panelForm.add(lbName,"gapy 5");
        panelForm.add(txtName,"hmin 30");
        panelForm.add(lbEmail,"gapy 5");
        panelForm.add(txtEmail,"hmin 30");
        panelForm.add(lbUsername,"gapy 5");
        panelForm.add(txtUsername,"hmin 30");
        panelForm.add(lbPassword,"gapy 5");
        panelForm.add(txtPassword,"hmin 30");
        panelForm.add(lbRole,"gapy 5");
        panelForm.add(cbxRole,"hmin 30");
        panelForm.add(lbPhoto,"gapy 5");
        panelForm.add(createPhotoPanel(),"wrap");
        panelForm.add(btnRegistrasi,"hmin 30, gapy 15 15");
        
        
        btnBrowse.addActionListener((e) -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (jpg, jpeg, png)", "jpg","jpeg","png");
            fileChooser.setFileFilter(filter);
            
            int returnValue = fileChooser.showOpenDialog(null);
            
            if(returnValue == JFileChooser.APPROVE_OPTION){
                File selectedFile = fileChooser.getSelectedFile();
                filePath = selectedFile.getAbsolutePath();
                
                setPhoto(filePath);
            }
        });
        
        btnRegistrasi.addActionListener((e) -> {
            insertData();
            
        });
        
        mainPanel.add(panelForm);
        add(mainPanel);
        
        initComboItem(cbxRole);
        setColorCbx();
    }
    
    private void initComboItem(JComboBox cbx){
        cbx.addItem("Select Role");
        cbx.addItem("Admin");
        cbx.addItem("User");
    }
    
    private void setColorBasedOnSelection(){
        if (cbxRole.getSelectedItem().equals("Select Role")) {
            cbxRole.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground");
        }else{
            cbxRole.putClientProperty(FlatClientProperties.STYLE, "foreground:Textfield.foreground");
        }
    }
    
    private void setColorCbx(){
        cbxRole.addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                setColorBasedOnSelection();
            }
        });
        
        cbxRole.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setColorBasedOnSelection();
            }
            
        });
        
        setColorBasedOnSelection();
    }
    
    private JPanel createPhotoPanel(){
        JPanel panel = new JPanel(new MigLayout("insets 0, gap 5"));
        panel.setPreferredSize(new Dimension(120,100));
        panel.setOpaque(false);
        
        lbPhotos = new JLabel();
        lbPhotos.setPreferredSize(new Dimension(100,100));
        lbPhotos.setOpaque(true);
        lbPhotos.setBackground(Color.WHITE);
        lbPhotos.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
        lbPhotos.setHorizontalAlignment(JLabel.CENTER);
        lbPhotos.setVerticalAlignment(JLabel.CENTER);
        
        Icon iconUser = new FlatSVGIcon(new FlatSVGIcon("com/absensi/icon/user_profile.svg", 80, 80));
        lbPhotos.setIcon(iconUser);
        
        btnBrowse = new JButton();
        btnBrowse.setIcon(new FlatSVGIcon("com/absensi/icon/find_photo.svg", 0.4f));
        
        panel.add(lbPhotos, "w 100!, h 100!");
        panel.add(btnBrowse,"align left, top, gapy 2");
        
        return panel;
    }
    
    private void setPhoto(String path){
        if (path != null && !path.isEmpty()) {
            ImageIcon originalIcon = new ImageIcon(path);
            Image originalImage = originalIcon.getImage();
            
            int labelWidth = lbPhotos.getWidth();
            int labelHeight = lbPhotos.getHeight();
            
            int imgWidth = originalIcon.getIconWidth();
            int imgHeight = originalIcon.getIconHeight();
            
            double aspectRatio = (double) imgWidth / imgHeight;
            int newWidth, newHeight;
            
            if(imgWidth > imgHeight){
                newWidth = labelWidth;
                newHeight = (int)(labelWidth/aspectRatio);
            }else{
                newHeight = labelHeight;
                newWidth = (int)(labelHeight * aspectRatio);
            }
            
            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            lbPhotos.setIcon(new ImageIcon(scaledImage));
        }else{
            lbPhotos.setIcon(new FlatSVGIcon("com/absensi/icon/user_profile.svg", 80, 80));
        }
    }
    
    private boolean validasiInput(){
        boolean valid = false;
        String username = txtUsername.getText().trim();
        
        if(txtName.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "please enter name", getOptionAlert());
        }else if(txtEmail.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "please enter email", getOptionAlert());
        }else if(txtUsername.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "please enter username", getOptionAlert());
        }else if(txtPassword.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "please enter password", getOptionAlert());
        }else if(cbxRole.getSelectedIndex()== 0){
            Toast.show(this, Toast.Type.INFO, "please select a role", getOptionAlert());
        }else if(filePath == null){
            Toast.show(this, Toast.Type.INFO, "please choose a photo", getOptionAlert());
        }else{
            User modelUser = new User();
            modelUser.setUsername(username);
            if (servis.validateUsername(modelUser)) {
                valid = true;
            }else {
                 Toast.show(this, 
                         Toast.Type.WARNING,
                         "This username is already taken\nplease choose another one",
                         getOptionAlert());
            }
        }
        return valid;
    }
    
    private void insertData(){
        if (validasiInput()==true) {
            String name = txtName.getText();
            String email = txtEmail.getText();
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            String role = cbxRole.getSelectedItem().toString();
            String newFilePath = saveFileToProject(new File(filePath));
            
            User modelUser = new User();
            modelUser.setName(name);
            modelUser.setEmail(email);
            modelUser.setUsername(username);
            modelUser.setPassword(password);
            modelUser.setRole(role);
            modelUser.setPhoto(newFilePath);
            modelUser.setInsertBy(0);
            
            servis.insertData(modelUser);
            Toast.show(this, Toast.Type.SUCCESS, "Registration completed succesfully", getOptionAlert());
        
            resetForm();
            FormManager.logout();
        }
    }
    
    private String saveFileToProject(File file){
        String projectDir = System.getProperty("user.dir");
        String folderName = "files";
        File destFolder = new File(projectDir,folderName);
        
        if(!destFolder.exists()){
            destFolder.mkdirs();
        }
        String appPrefix = "AppsAbsensi_";
        String originalFileName = file.getName();
        
        String extention = originalFileName.substring(originalFileName.lastIndexOf("."));
        String timestamp = String.valueOf(System.currentTimeMillis());
        String newFileName = appPrefix + timestamp + extention;
        
        File destFile = new File(destFolder,newFileName);
        try {
            Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return folderName + "/" + newFileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    private void resetForm(){
        txtName.setText("");
        txtEmail.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cbxRole.setSelectedIndex(0);
        Icon iconUser = new FlatSVGIcon("com/absensi/icon/user_profile.svg",80,80);
        lbPhotos.setIcon(iconUser);
    }
} 