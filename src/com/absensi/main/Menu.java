package com.absensi.main;

import com.absensi.form.FormDashboard;
import com.absensi.form.FormKelas;
import com.absensi.form.FormKelasRestore;
import com.absensi.form.FormStudent;
import com.absensi.form.FormStudentRestore;
import com.absensi.form.FormTeacher;
import com.absensi.form.FormTeacherRestore;
import com.absensi.model.User;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Insets;
import java.io.File;
import javax.swing.JComponent;
import raven.modal.drawer.data.MenuItem;
import javax.swing.UIManager;
import raven.extras.AvatarIcon;
import raven.modal.drawer.DrawerPanel;
import raven.modal.drawer.data.Item;
import raven.modal.drawer.menu.MenuAction;
import raven.modal.drawer.menu.MenuEvent;
import raven.modal.drawer.menu.MenuOption;
import raven.modal.drawer.menu.MenuStyle;
import raven.modal.drawer.renderer.DrawerStraightDotLineStyle;
import raven.modal.drawer.simple.SimpleDrawerBuilder;
import raven.modal.drawer.simple.footer.SimpleFooterData;
import raven.modal.drawer.simple.header.SimpleHeaderData;
import raven.modal.option.Option;

public class Menu extends SimpleDrawerBuilder{
    
    private final int SHADOW_SIZE = 0;
    
    public Menu() {
        super(createSimpleMenuOption());
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        User loggedInUser = FormManager.getLoggedInUser();
        
        String filePath = (loggedInUser != null) ? loggedInUser.getPhoto():null;
        
        AvatarIcon icon;
        
        if (filePath != null && new File(filePath).exists()){
            icon = new AvatarIcon(filePath, 50, 50, 100f);
        }else{
            icon = new AvatarIcon(getClass().getResource("/com/absensi/img/Profile.jpg"), 50, 50, 100f);
        }
        
        icon.setType(AvatarIcon.Type.ROUND);
        icon.setBorder(2, 2);
        
        changeAvatarIconBorderColor(icon);
        
        UIManager.addPropertyChangeListener((evt)->{
            if (evt.getPropertyName().equals("lookAndFeel")){
                changeAvatarIconBorderColor(icon);
            }
        });
        
        if (loggedInUser == null) {
            return new SimpleHeaderData()
                .setIcon(icon)
                .setTitle("Raihan Pratama")
                .setDescription("@rhannprtmaa");
        }
        
        return new SimpleHeaderData()
                .setIcon(icon)
                .setTitle(loggedInUser.getName())
                .setDescription(loggedInUser.getEmail());
    }
    
    private void changeAvatarIconBorderColor(AvatarIcon icon){
        // Menggunakan warna putih di kedua mode
        java.awt.Color borderColor = new java.awt.Color(255, 255, 255);
        icon.setBorderColor(new AvatarIcon.BorderColor(borderColor, 1.0f));
    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData()
            .setDescription("version" + Main.APP_VERSION);
    }
    
    public static MenuOption createSimpleMenuOption(){
        String role = "Admin";
        MenuOption SimpleMenuOption = new MenuOption();
        
        MenuItem[] adminMenu = new MenuItem[]{
            new Item.Label("MAIN"),
            new Item("Dashboard", "dashboard.svg", FormDashboard.class),
            new Item.Label("MASTER"),
            new Item("Teacher", "teacher.svg")
                .subMenu("Teacher Data", FormTeacher.class)
                .subMenu("Teacher Data Restore ", FormTeacherRestore.class),
            new Item("Class", "class.svg")
                .subMenu("Class Data", FormKelas.class)
                .subMenu("Class Data Restore",FormKelasRestore.class),
            new Item("Student", "student.svg")
                .subMenu("Student Data",FormStudent.class)
                .subMenu("QR Code Student")
                .subMenu("Student Data Restore",FormStudentRestore.class),
            new Item("Management User", "user.svg")
                .subMenu("User Data")
                .subMenu("User Data Restore")
                .subMenu("Profile")
                .subMenu("Change Password"),
            new Item.Label("TRANSAKSI"),
            new Item("Attendance", "attendance.svg"),
            new Item("Management Attendance", "manage_attendance.svg")
                .subMenu("Attendance Data")
                .subMenu("Attendance Data Restore"),
            new Item.Label("REPORT"),
            new Item("Report", "report.svg")
                .subMenu("Teacher Report")
                .subMenu("Class Report")
                .subMenu("Student Report")
                .subMenu(new Item("Attendance Report")
                .subMenu("Attendance by Period")
                .subMenu("Attendance Summary")),
            new Item("About", "about.svg"),
            new Item("Logout", "logout.svg")
        };
        
        MenuItem[] userMenu = new MenuItem[]{
            new Item.Label("MAIN"),
            new Item("Dashboard", "dashboard.svg", FormDashboard.class),
            new Item("Logout", "logout.svg")
        };
        
        MenuItem[] menuToUse;
       
        switch (role) {
            case "Admin":
                menuToUse = adminMenu;
                break;
            case "User":
                menuToUse = userMenu;
                break;
            default:
                menuToUse = new MenuItem[0];
                break;
        }
        
        SimpleMenuOption.setMenuStyle(new MenuStyle(){
            @Override
            public void styleMenu(JComponent component) {
                // Mengatur latar belakang dan warna teks putih untuk semua komponen
                component.putClientProperty(FlatClientProperties.STYLE, 
                    getDrawerBackgroundStyle() + 
                    ";foreground:rgb(255,255,255)");
            }

            public void styleMenuItem(JComponent component) {
                // Mengatur item menu berwarna putih
                component.putClientProperty(FlatClientProperties.STYLE, 
                    "foreground:rgb(255,255,255)");
            }

            public void styleLabel(JComponent component) {
                // Mengatur label seperti "MAIN", "MASTER" berwarna putih
                component.putClientProperty(FlatClientProperties.STYLE, 
                    "foreground:rgb(255,255,255)");
            }
        });
        
        SimpleMenuOption.getMenuStyle().setDrawerLineStyleRenderer(new DrawerStraightDotLineStyle());
        SimpleMenuOption.setMenuItemAutoSelectionMode(MenuOption.MenuItemAutoSelectionMode.SELECT_SUB_MENU_LEVEL);
        SimpleMenuOption.addMenuEvent(new MenuEvent() {
            @Override
            public void selected(MenuAction action, int[] ints) {
                Class<?> itemClass = action.getItem().getItemClass();
                
                int i = ints[0];
                if (role.equals("Admin")) {
                    if (i == 9) {
                        action.consume();
                        FormManager.logout();
                        return;
                    }
                    handleFormAction(itemClass, action);
                } else if (role.equals("User")) {
                    if (i == 9) {
                        action.consume();
                        FormManager.logout();
                        return;
                    }
                    handleFormAction(itemClass, action);
                }
            }
            private void handleFormAction(Class<?> itemClass, MenuAction action){
                if (itemClass == null || !Form.class.isAssignableFrom(itemClass)){
                    action.consume();
                    return;
                }
                
                @SuppressWarnings("unchecked")
                Class<? extends Form> formClass = (Class<? extends Form>) itemClass;
                FormManager.showForm(AllForms.getForm(formClass));
            }
        });
        
        SimpleMenuOption.setMenus(menuToUse)
                .setBaseIconPath("com/absensi/icon")
                .setIconScale(0.45f);
        return SimpleMenuOption;
    }

    @Override
    public int getDrawerWidth() {
        return 270 + SHADOW_SIZE;
    }

    @Override
    public int getDrawerCompactWidth() {
        return 80 + SHADOW_SIZE;
    }

    @Override
    public int getOpenDrawerAt() {
       return 1000;
    }

    @Override
    public Option getOption() {
        Option option = super.getOption();
        option.setOpacity(0.3f);
        option.getBorderOption()
                .setShadowSize(new Insets(0,0,0,SHADOW_SIZE));
        return option;
    }

    @Override
    public boolean openDrawerAtScale() {
        return false;
    }

    @Override
    public void build(DrawerPanel drawerPanel) {
        // Memastikan gaya diterapkan saat membangun panel
        drawerPanel.putClientProperty(FlatClientProperties.STYLE, 
            getDrawerBackgroundStyle() + 
            ";foreground:rgb(255,255,255)");
    }
    
    private static String getDrawerBackgroundStyle(){
        return ""
               + "[light]background:@accentColor;" // Mode light: @accentColor (sesuai btnRestore)
               + "[dark]background:shade(@accentColor, 20%)"; // Mode dark: lebih gelap dari @accentColor
    }
}