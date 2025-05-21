package com.absensi.main;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.BorderLayout;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.Timer;
import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;
import raven.modal.demo.icons.SVGIconUIColor;

public class MainForm extends JPanel{
    
    private JPanel mainPanel;
    private JButton buttonUndo;
    private JButton buttonRedo;

    public MainForm() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx, wrap, insets 0, gap 0", "[fill]", "[][fill, grow] [][]"));
        add(createHeader());
        add(createMain());
        add(new JSeparator(),"height 2!");
        add(createFooter());
        
    }
    
    private JPanel createHeader(){
        JPanel panel = new JPanel(new MigLayout("insets 3", "[]push[]push","[fill]"));
        
        JToolBar toolBar = new JToolBar();
        JButton buttonDrawer = new JButton(new FlatSVGIcon("raven/modal/demo/icons/menu.svg", 0.5f));
        buttonUndo = new JButton(new FlatSVGIcon("raven/modal/demo/icons/undo.svg", 0.5f));
        buttonRedo = new JButton(new FlatSVGIcon("raven/modal/demo/icons/redo.svg", 0.5f));
        
        buttonDrawer.addActionListener((e) -> {
            if(Drawer.isOpen()){
                Drawer.showDrawer();
            }else{
                Drawer.toggleMenuOpenMode();
            }
        });
        
        buttonUndo.addActionListener((e)->FormManager.undo());
        buttonUndo.addActionListener((e)->FormManager.redo());
        
        toolBar.add(buttonDrawer);
        toolBar.add(buttonUndo);
        toolBar.add(buttonRedo);
        
        panel.add(toolBar);
        return panel;
    }
    
    private JPanel createFooter(){
        JPanel panel = new JPanel (new MigLayout("insets 1 n 1 n, al trailing center, gapx 10, height 30!","[]push[][]", "fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "[]background:tint($Panel.background,20%);"
                + "[dark]background:tint ($Panel.background, 5%)");
        
        JLabel lbAppVersion = new JLabel("MyAbsensi v" + Main.APP_VERSION);
        lbAppVersion.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disableForeground");
        lbAppVersion.setIcon (new SVGIconUIColor("raven/modal/demo/icons/git.svg", 1f, "Label.disableForeground"));
        panel.add(lbAppVersion);
        
        String javaVendor = System.getProperty("java.vendor");
        if (javaVendor  .equals("Oracle Corporation")){
            javaVendor = "";
        }
        String java = javaVendor +  " v" + System.getProperty("java.version").trim();
        String st = "Running on: Java %s";
        JLabel lbJava = new JLabel(String.format(st, java));
        lbAppVersion.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disableForeground");
        lbAppVersion.setIcon (new SVGIconUIColor("raven/modal/demo/icons/java.svg", 1f, "Label.disableForeground"));
        panel.add(lbJava);
        
        panel.add(new JSeparator(JSeparator.VERTICAL));
        
        JLabel lbDate = new JLabel();
        lbDate.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disableForeground");
        Timer timer = new Timer(1, (e)-> {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd MM HH:mm:ss");
            lbDate.setText(df.format(new Date()));
        });
        panel.add(lbDate);
        
        timer.start();
        return panel;
    }
    
    private Component createMain(){
        mainPanel = new JPanel(new BorderLayout());
        return mainPanel;
    }
    
    public void setForm (Form form){
        mainPanel.removeAll();
        mainPanel.add(form);
        mainPanel.repaint();
        mainPanel.revalidate();
        
        buttonUndo.setEnabled(FormManager.FORMS.isUndoAble());
        buttonRedo.setEnabled(FormManager.FORMS.isRedoAble());
        
        if (mainPanel.getComponentOrientation().isLeftToRight() != form.getComponentOrientation().isLeftToRight()){
            applyComponentOrientation(mainPanel.getComponentOrientation());
        }
    }
}
