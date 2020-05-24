
package com.eh7n.f1telemetry.Components;



import javax.swing.*;
import java.awt.*;
// geom levert 2d java classes     setColor(new Color(r, g, b));
// general path om lijnen(in verschillende kleuren) te tekekne, het houd alle teken geschiedenis bij zodat je er een path(pad) van kan maken en als dit een gesloten path is kan je een schape maken, en een shape kan je fillen
import java.awt.geom.GeneralPath;
// om lijn te tekenen
import java.awt.geom.Line2D;
// rechthoek tekenen
import java.awt.geom.Rectangle2D;

/**
 * Created by chenjensen on 16/11/26.
 */

/**
 * Dashboard overgenomen van Jcomponent
 *Ondersteunt momenteel lineaire wijzerplaat, boogwijzerplaat, ronde wijzerplaat, halfronde wijzerplaat
 */
public class Battery extends JComponent {

    private static final int VALUE_FONT_SIZE = 18;


    private double from = 0;
    // tot welke maxsnelheid we gaan
    private double to = 10;

    public String type = "line";

    //Maximale en minimale schaal
    // Hoeveel lijnen er tussen de grote lijnen zitten
    // vb major =50,to=350 om de 50 een grote lijn
    private double major = 1;
    // hoeveel afstand/lijnene er tussen de grote lijnen is van kleien lijnen
    // vb minor = 10 om de 10 een kleine lijn
    private double minor = 0.1;

    //Huidige wijzerpositie
    private String value = "";

    //eenheid
    private String unit = "";

    private double width;
    private double height;
    // subclasse van awt, om in te tekenen
    public Graphics2D g2;
    private int fontSize;


    public Color first = new Color(0,204,0);
    private Color second= new Color(0,204,0);
    private Color third= new Color(0,204,0);
    private Color fourth= new Color(0,204,0);
    private Color fift= new Color(0,204,0);
    private Color FRWColor= new Color(0,204,0);

    public Battery() {
        super();
        this.setBackground(Color.WHITE);
    }

    public void paintComponent(Graphics g) {
        // om afmetingen van indicator te krijgen
        width = getWidth();
        height = getHeight();


        // om het witte vierkant rond indicator te maken
        g2 = graphicsConfig(g);

        drawCar();
        //g2.fill();




    }

    private Graphics2D graphicsConfig(Graphics g) {
        //lettergroote
        fontSize = 20;
        Graphics2D g2 = (Graphics2D) g;
        //rendering is weergave(dingen weergeven), dus om de weergave optieties zoals qualiteit en snelheid aantepassen
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(this.getBackground());
        g2.fillRect(0, 0, (int) width, (int) height);
        g2.setColor(this.getForeground());
        // stroking is a process of drawing a shape’s outline applying stroke width, line style, and color attribute
        // integenstelling tot filling:is a process of painting the shape’s interior with solid color or a color gradient, or a texture pattern
        g2.setStroke(new BasicStroke(1));
        // font is lettertype, dus om dit aantepassen
        g2.setFont(new Font(Font.SERIF, Font.PLAIN, fontSize));
        return  g2;
    }


    private void drawCar()
    {
        //w=100,h=200
        int h = (int)height;
        int w = (int)width;




        g2.setColor(first);
        g2.fillRect((int)(w*0.1),(int)(h*0.16) ,(int)(w*0.15),(int)(h*(0.36)));
        g2.setColor(second);
        g2.fillRect((int)(w*0.25),(int)(h*0.16) ,(int)(w*0.15),(int)(h*(0.36)));
        g2.setColor(third);
        g2.fillRect((int)(w*0.4),(int)(h*0.16) ,(int)(w*0.15),(int)(h*(0.36)));
        g2.setColor(fourth);
        g2.fillRect((int)(w*0.55),(int)(h*0.16) ,(int)(w*0.15),(int)(h*(0.36)));
        g2.setColor(fift);
        g2.fillRect((int)(w*0.7),(int)(h*0.23) ,(int)(w*0.1),(int)(h*(0.22)));
        //g2.setColor(Color.ORANGE);
        // g2.fillRect((int)(w*0.2),(int)(h*0.16) ,(int)(w*0.3),(int)(h*(0.36)));





    }



    // maakt string van een double
    private String format(double d) {
        if ((int) d == d) {
            return String.valueOf((int) d);
        } else {
            return String.valueOf(d);
        }
    }

    private double toDouble(String string) {
        try {
            return Double.valueOf(string);
        } catch (Exception e) {
            return 0;
        }
    }

    private boolean near(double d1, double d2) {
        return Math.round(d1 * 1000000) == Math.round(d2 * 1000000);
    }

    private static Rectangle2D getStrBounds(Graphics2D g, String str) {
        Font f = g.getFont();
        Rectangle2D rect = f.getStringBounds(str, g.getFontRenderContext());
        if (rect.getHeight() < f.getSize()) {
            rect.setFrame(rect.getX(), rect.getY(), rect.getWidth(), f.getSize() + 1);
        }
        return rect;
    }

    public void setFrom(double from) {
        this.from = from;
    }

    public double getFrom() {
        return from;
    }



    public double getTo() {
        return to;
    }

    public void setTo(double to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if(value == null)
            this.value = "0";
        else    this.value = value;
    }



    public void setfirst(Color first) {
        this.first = first;
    }
    public void setsecond(Color second) {
        this.second = second;
    }
    public void setthird(Color third) {
        this.third = third;
    }
    public void setfourth(Color fourth) {
        this.fourth = fourth;
    }
    public void setfift(Color fift) {
        this.fift = fift;
    }
    public void setWingFRColor(Color FRWColor) {
        this.FRWColor = FRWColor;
    }


//////////////////////////////////////////////////////////////////////////////////////////////

}
