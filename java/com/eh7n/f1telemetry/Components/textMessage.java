package com.eh7n.f1telemetry.Components;
import java.awt.*;
import javax.swing.*;
import java.util.Random;


public class textMessage extends JPanel {

    // private JProgressBar progressBar;
    private  JTextField text;



    public textMessage(String t) {
        super(new BorderLayout());



        text = new JTextField(t);



        JPanel panel = new JPanel();
        text.setText("aaaaaaa");

        panel.add(text);
        // panel.add(progressBar);


        add(panel, BorderLayout.PAGE_START);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    }
    public JTextField gettext(){
        return text;
    }


}