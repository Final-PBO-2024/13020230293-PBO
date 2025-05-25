package com.absensi.util;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;
import raven.modal.listener.ModalCallback;
import raven.modal.toast.ToastPanel;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class MessageModal extends ModalBorder {

    private final Type type;

    public MessageModal(Type type, String message, String title, int optionType, ModalCallback callback) {
        super(createMessage(type, message), title, optionType, callback);
        this.type = type;
    }

    private static Component createMessage(Type type, String message) {
        JTextArea text = new JTextArea(message);
        text.setWrapStyleWord(true);
        text.setEditable(false);
        text.setCaret(new DefaultCaret() {
            @Override
            public void paint(Graphics g) {
            }
        });
        String gap = type == Type.DEFAULT ? "30" : "62";
        text.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:0," + gap + ",10,30;" +
                "[light]foreground:lighten($Label.foreground,20%);" +
                "[dark]foreground:darken($Label.foreground,20%);");
        return text;
    }

    @Override
    protected JComponent createTitleComponent(String title) {
        if (type == Type.DEFAULT) {
            return super.createTitleComponent(title);
        }
        Icon icon = createIcon(type);
        JLabel label = (JLabel) super.createTitleComponent(title);
        label.setIconTextGap(10);
        label.setIcon(icon);
        return label;
    }

    @Override
    protected JComponent createOptionButton(Option[] optionsType) {
        JPanel panel = (JPanel) super.createOptionButton(optionsType);
        // modify layout option
        if (panel.getLayout() instanceof MigLayout) {
            MigLayout layout = (MigLayout) panel.getLayout();
            layout.setColumnConstraints("[]12[]");
        }

        // revers order
        Component[] components = panel.getComponents();
        panel.removeAll();
        for (int i = components.length - 1; i >= 0; i--) {
            panel.add(components[i]);
        }
        return panel;
    }

    @Override
    protected JButton createButtonOption(Option option) {
        JButton button = super.createButtonOption(option);
        String colors[] = getColorKey(type);
        if (button.isDefaultButton()) {
            button.setIcon(new FlatSVGIcon("com/absensi/icon/check.svg", 0.4f));
            button.putClientProperty(FlatClientProperties.STYLE, "" +
                    "arc:10;" +
                    "margin:3,15,3,15;" +
                    "borderWidth:0;" +
                    "focusWidth:0;" +
                    "innerFocusWidth:0;" +
                    "default.borderWidth:0;" +
                    "[light]background:@accentColor;" +
                    "[light]foreground:rgb(255,255,255);" +
                    "[dark]background:" + colors[1] + ";");
        } else {
            button.setIcon(new FlatSVGIcon("com/absensi/icon/cancel.svg", 0.4f));
            button.putClientProperty(FlatClientProperties.STYLE, "" +
                    "arc:10;" +
                    "margin:3,15,3,15;" +
                    "borderWidth:1;" +
                    "focusWidth:0;" +
                    "innerFocusWidth:1;" +
                    "background:null;" +
                    "[light]borderColor:@accentColor;" +
                    "[dark]borderColor:" + colors[1] + ";" +
                    "[light]focusedBorderColor:@accentColor;" +
                    "[dark]focusedBorderColor:" + colors[1] + ";" +
                    "[light]focusColor:rgb(65, 92, 122);" +
                    "[dark]focusColor:" + colors[1] + ";" +
                    "[light]hoverBorderColor:@accentColor;" +
                    "[dark]hoverBorderColor:" + colors[1] + ";" +
                    "[light]foreground:@accentColor;" +
                    "[dark]foreground:" + colors[1] + ";");
        }
        return button;
    }

    protected Icon createIcon(Type type) {
        ToastPanel.ThemesData data = Toast.getThemesData().get(asToastType(type));
        FlatSVGIcon icon = new FlatSVGIcon(data.getIcon(), 0.7f);
        FlatSVGIcon.ColorFilter colorFilter = new FlatSVGIcon.ColorFilter();
        colorFilter.add(Color.decode("#969696"), Color.decode(data.getColors()[0]), Color.decode(data.getColors()[1]));
        icon.setColorFilter(colorFilter);
        return icon;
    }

    protected String[] getColorKey(Type type) {
        if (type == Type.DEFAULT) {
            // use accent color as default type
            return new String[]{"$Component.accentColor", "$Component.accentColor"};
        }
        ToastPanel.ThemesData data = Toast.getThemesData().get(asToastType(type));
        return data.getColors();
    }

    private Toast.Type asToastType(Type type) {
        switch (type) {
            case DEFAULT:
                return Toast.Type.DEFAULT;
            case SUCCESS:
                return Toast.Type.SUCCESS;
            case INFO:
                return Toast.Type.INFO;
            case WARNING:
                return Toast.Type.WARNING;
            default:
                return Toast.Type.ERROR;
        }
    }

    public enum Type {
        DEFAULT, SUCCESS, INFO, WARNING, ERROR
    }
}