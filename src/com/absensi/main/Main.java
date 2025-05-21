package com.absensi.main;


import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.UIManager;
import raven.modal.demo.utils.DemoPreferences;

public class Main extends JFrame {
    
    public static final String APP_VERSION = "1.0.0";
    
    public Main() {
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        FormManager.install(this);
        setSize(new Dimension(1366,768));
        setLocationRelativeTo(null);
    }
    
    public static void main(String[] args) {
        DemoPreferences.init();
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("com.absensi.themes");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY,Font.PLAIN, 13));
        DemoPreferences.setupLaf();
        EventQueue.invokeLater(()->{});
            new Main().setVisible(true);
    }
}
