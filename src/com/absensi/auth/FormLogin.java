package com.absensi.auth;

import com.absensi.dao.UserDAO;
import com.absensi.main.Form;
import com.absensi.main.FormManager;
import com.absensi.model.User;
import com.absensi.service.ServiceUser;
import com.absensi.util.AlertUtils;
import static com.absensi.util.AlertUtils.getOptionAlert;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import net.miginfocom.layout.AC;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

public class FormLogin extends Form{
    
    private final ServiceUser servis = new UserDAO();
            
    private JLabel imageLogo;
    private JPanel mainPanel;
    private JPanel panelForm;
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    
    
    public FormLogin(){
        init();
        setActionButton();
        updateLogo();
    }
    
    private void updateLogo(){
        if(FlatLaf.isLafDark()){
            imageLogo.setIcon(new FlatSVGIcon("com/absensi/icon/logosekolah.svg",100,100));
        }else{
            imageLogo.setIcon(new FlatSVGIcon("com/absensi/icon/logosekolah.svg",100,100));
        }
    }

    private void init() {
        setLayout(new MigLayout("fill, insets 20","[center]","[center]"));
        
        
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        btnLogin = new JButton("");
        
        mainPanel = new JPanel(new MigLayout("insets 50", "", "[fill][grow]"));
        mainPanel.putClientProperty(FlatClientProperties.STYLE,"" 
                + "arc:20;"
                + "[light]background:rgb(255,255,255);"
                + "[dark]background:darken($Panel.background, 5%)");
        
        JPanel panelLogo = new JPanel( new MigLayout("wrap", "300", "[]0[]"));
        panelLogo.putClientProperty(FlatClientProperties.STYLE,"" 
                + "[light]background:rgb(255,255,255);"
                + "[dark]background:darken($Panel.background, 5%)");
        
        imageLogo = new JLabel();
        updateLogo();
        
        JLabel lbTitleLogo = new JLabel ("MyAbsensi");
        lbTitleLogo.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:rgb(0,0,0);"
                + "font:bold italic +14");
        
        JLabel lbDetail = new JLabel("Attendance Management System");
        lbDetail.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:rgb(51,51,51);"
                + "font:bold 16");
        
        JLabel lbCreated = new JLabel("Created by Raihan");
        lbCreated.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:rgb(140,140,140);"
                + "font:12");
        
        panelLogo.add(imageLogo, "align center, gapy 50, gap 25px 0px");
        panelLogo.add(lbTitleLogo , "align center, gap 25px 0px");
        panelLogo.add(lbDetail, "align center, gap 25px 0px");
        panelLogo.add(lbCreated, "align center, gap 25px 0px");
        
        panelForm = new JPanel(new MigLayout("wrap, insets 20" ,"fill, 200:250"));
        panelForm.putClientProperty(FlatClientProperties.STYLE,"" 
                + "arc:20;"
                + "[light]background:rgb(88,135,178);"
                + "[dark]background:tint($Panel.background, 5%)");
        
        JLabel lbTitleForm = new JLabel("Login", JLabel.CENTER);
        lbTitleForm.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:rgb(255,255,255);"
                + "font:bold +10");
        
        JLabel lbDescription = new JLabel("Please sign in to access your dashboard");
        lbDescription.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:rgb(255,255,255)");
        
        JLabel lbUsername = new JLabel("Username");
        lbUsername.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:rgb(255,255,255)");
        
        txtUsername = new JTextField();
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Username");
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("com/absensi/icon/username.svg", 20, 20)
                );
        txtUsername.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        JLabel lbPassword = new JLabel("Password");
        lbPassword.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:rgb(255,255,255)");
        
        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
        txtPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("com/absensi/icon/password.svg", 20, 20)
                );
        txtPassword.putClientProperty(FlatClientProperties.STYLE,""
                + "arc:10;"
                + "showRevealButton:true;"
                + "showCapsLock:true;");
        
        btnLogin = new JButton("Login");
        btnLogin.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:rgb(255,255,255);"
                + "[light]foreground:rgb(88,135,178);"
                + "arc:10;"
                + "borderWidth:0;" 
                + "focusWidth:0;"
                + "innerFocusWidth:0;"
                + "font:bold 16");
        
        
        panelForm.add(lbTitleForm);
        panelForm.add(lbDescription);
        panelForm.add(lbUsername,"gapy 8");
        panelForm.add(txtUsername,"hmin 30");
        panelForm.add(lbPassword,"gapy 8");
        panelForm.add(txtPassword,"hmin 30");
        panelForm.add(btnLogin,"hmin 30, gapy 15 15");
        
        mainPanel.add(panelForm);
        mainPanel.add(panelLogo);
        
        add(mainPanel);
        
        btnLogin.addActionListener((e)->{
            processLogin();
        });
    }
    
    private void setActionButton(){
        txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER) {
                    processLogin();
                }
            }
            
        });
        
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
               if (e.getKeyCode()==KeyEvent.VK_ENTER) {
                    processLogin();
                }
            }
            
        });
    }
    

    private boolean validasiInput(){
        boolean valid = false;
        if (txtUsername.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Username cannot be empty", getOptionAlert());
            txtUsername.putClientProperty("JComponent.outline", "error");
        }else if(txtUsername.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Password cannot be empty", getOptionAlert());
            txtUsername.putClientProperty("JComponent.outline", "error");
        }else{
            valid = true;
            txtUsername.putClientProperty("JComponent.outline", null);
            txtPassword.putClientProperty("JComponent.outline", null);
        }
        return valid;
    }
    
    private void processLogin() {
        if (validasiInput()==true) {
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            
            User modelUser = new User();
            modelUser.setUsername(username);
            modelUser.setPassword(password);
            
            User user = servis.processLogin(modelUser);
            if (user != null) {
                FormManager.login(user);
                resetForm();
            }else {
                Toast.show(this, Toast.Type.ERROR, "Incorrect username & Password. Please try agains", getOptionAlert());
            }
        }
    }
    
    private void resetForm(){
        txtUsername.setText("");
        txtPassword.setText("");
    }
    
}
