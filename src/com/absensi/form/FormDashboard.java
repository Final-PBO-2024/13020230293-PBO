
package com.absensi.form;

import com.absensi.main.Form;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;

public class FormDashboard extends Form{
    
    public FormDashboard(){
        init();
    }

    private void init() {
        setLayout(new MigLayout("insets 10, fill, wrap", "fill","[]0[fill, grow]"));
    
        JLabel title = new JLabel("Tessssssssssss");
        add(title);
    }
    
}
