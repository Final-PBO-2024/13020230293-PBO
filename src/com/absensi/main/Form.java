
package com.absensi.main;

import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class Form extends JPanel{
    private LookAndFeel oldTheme = UIManager.getLookAndFeel();
    
    public Form(){
        init();
}

    private void init() {
        
    }
    
    public void formInit() {
        
    }
    
    public void formOpen() {
        
    }

    public void formRefresh() {
        
    }
    
    protected final void formCheck(){
        if(oldTheme != UIManager.getLookAndFeel()){
            oldTheme = UIManager.getLookAndFeel();
            SwingUtilities.updateComponentTreeUI(this);
        }
    }
}

