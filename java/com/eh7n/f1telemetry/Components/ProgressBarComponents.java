package com.eh7n.f1telemetry.Components;
import java.awt.*;
import javax.swing.*;
import java.util.Random;


public class ProgressBarComponents extends JPanel {

    private JProgressBar progressBar;
    private static JTextField text;


// geef naam van de progresbar component mee
    public ProgressBarComponents(String t) {
        // inherrent van JPANEL
        //BorderLayout is 1 van de layoutmangeres, gebruikt om componenten in juiste volgorde te ordennen(juist te zetten)
        super(new BorderLayout());


        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        // zorgt ervoor dat de tekst gedisplayed wordt
        progressBar.setStringPainted(true);
        text = new JTextField(t);


        // creeer een panel (waar er plaats is om components in toetevoegen)
        JPanel panel = new JPanel();

        panel.add(text);
        panel.add(progressBar);

        //voegt de pannel toe (aan frame) en borderLayout.pagestart zegt waar je het toevoegd, denk ik
        add(panel, BorderLayout.PAGE_START);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    }
    public JProgressBar getProgressBar(){
        return progressBar;
    }


}