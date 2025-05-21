package com.absensi.main;

import com.absensi.auth.FormLogin;
import com.absensi.auth.FormRegistrasi;
import com.absensi.dao.UserDAO;
import com.absensi.form.FormDashboard;
import com.absensi.model.User;
import com.absensi.service.ServiceUser;
import javax.swing.JFrame;
import raven.modal.Drawer;
import raven.modal.demo.utils.UndoRedo;

public class FormManager{
    
    public static final UndoRedo<Form> FORMS = new UndoRedo<>();
    private static JFrame frame;
    private static FormLogin formLogin;
    private static MainForm mainForm;
    private static User loggedInUser    ;
    
    public static void install (JFrame f){
        frame = f;
        ServiceUser servis = new UserDAO();
        
        if(!servis.isUserExists()){
            registrasi();
        }else{
            logout();
        }
    }
    
    public static void showForm (Form form){
        if(form!=FORMS.getCurrent()){
            FORMS.add(form);
            form.formCheck();
            form.formOpen();
            mainForm.setForm(form);
        }
    }
    
    public static void undo(){
        if (FORMS.isUndoAble()) {
            Form form = FORMS.undo();
            form.formCheck();
            form.formOpen();
            Drawer.setSelectedItemClass(form.getClass());
            mainForm.setForm(form);
        }
    }
    
    public static void redo(){
        if (FORMS.isRedoAble()) {
            Form form = FORMS.redo();
            form.formCheck();
            form.formOpen();
            Drawer.setSelectedItemClass(form.getClass());
            mainForm.setForm(form); 
        }
    }
    
    public static void login(User user){
        loggedInUser = user;
        
        Drawer.installDrawer(frame, new Menu());
        Drawer.setVisible(true);
        
        frame.getContentPane().removeAll();
        frame.getContentPane().add(getMainForm());
        Drawer.setSelectedItemClass(FormDashboard.class);
        frame.repaint();
        frame.revalidate();
    }
    
    public static User getLoggedInUser(){
        return loggedInUser;
    }
    
     public static void registrasi() {
        Drawer.installDrawer(frame, new Menu());
        Drawer.setVisible(false);
        frame.getContentPane().removeAll();
        FormRegistrasi formRegistrasi = new FormRegistrasi();
        frame.getContentPane().add(formRegistrasi);
        frame.repaint();
        frame.revalidate();
    }
    
    public static void logout() {
        if (loggedInUser == null) {
             Drawer.installDrawer(frame, new Menu());
        }
        Drawer.setVisible(false);
        frame.getContentPane().removeAll();
        FormLogin login = getLogin();
        login.formCheck();
        frame.getContentPane().add(login);
        frame.repaint();
        frame.revalidate();
    }

    public static JFrame getFrame() {
        return frame;
    }
    
    private static MainForm getMainForm(){
        if(mainForm == null){
            mainForm = new MainForm();
        }
        return mainForm;
    }
    
    private static FormLogin getLogin(){
        if(formLogin == null){
            formLogin = new FormLogin();
        }
        
        return formLogin;
    }
}
