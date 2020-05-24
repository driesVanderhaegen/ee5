package com.eh7n.f1telemetry.Components;
import javax.swing.*;

public class Text {
    private static JTextField text;

    public Text(String t) {
        text = new JTextField(t);
    }

    public static JTextField getText() {
        return text;
    }
}
