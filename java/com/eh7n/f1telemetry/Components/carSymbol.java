
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
public class carSymbol extends JComponent {

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


    public Color FLColor = new Color(0,153,0);
    private Color FRColor= new Color(0,153,0);
    private Color BLColor= new Color(0,153,0);
    private Color BRColor= new Color(0,153,0);
    private Color FLWColor= new Color(0,204,0);
    private Color FRWColor= new Color(0,204,0);

    public carSymbol() {
        super();
        this.setBackground(Color.WHITE);
    }

    public void paintComponent(Graphics g) {
        // om afmetingen van indicator te krijgen
        width = getWidth();
        height = getHeight();


        // om het witte vierkant rond indicator te maken
        g2 = graphicsConfig(g);
        //to = 500;
        // major = 50;
        //minor = 10;
        // om de schaal die erbijstaat te tekenen
           /* g2.fillOval(0,0,60,60);
            g2.setColor(Color.RED);
            g2.drawRect(60,60,80,30);*/
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



        g2.setColor(new Color(0,204,0));
        g2.fillOval((int)(w*0.14),(int)(h*(1 -0.505 )) ,(int)(w*0.32),(int)(h*0.16));
        g2.setColor(new Color(0,204,0));
        g2.fillOval((int)(w*0.54),(int)(h*(1 -0.505 )) ,(int)(w*0.32),(int)(h*0.16));
        g2.setColor(new Color(0,255,0));
        g2.fillRect((int)(w*0.14),(int)(h*(1-0.425)) ,(int)(w*0.72),(int)(h*(0.33)));
        g2.setColor(this.BLColor);
        g2.fillRect((int)(w*0.02),(int)(h*(1 -(0.2375))) ,(int)(w*0.20),(int)(h*0.35/2));
        g2.setColor(this.BRColor);
        g2.fillRect((int)(w*(1-(0.22))),(int)(h*(1 -0.2375)) ,(int)(w*0.20),(int)(h*0.35/2));
        g2.setColor(new Color(0,255,0));
        g2.fillRect((int)(w*0.3),(int)(h*(1-(0.625))) ,(int)(w*0.40),(int)(h*0.40));
        g2.setColor(new Color(0,204,0));
        g2.fillRect((int)(w*0.22),(int)(h*(1 -0.75)) ,(int)(w*0.56),(int)(h*0.025));
        g2.setColor(this.FRWColor);
        g2.fillRect((int)(w*0.5),(int)(h*(1-(0.85+0.04))) ,(int)(w*0.4),(int)(h*(0.05)));
        g2.setColor(this.FLWColor);
        g2.fillRect((int)(w*(0.5-0.4)),(int)(h*(1-(0.85+0.04))) ,(int)(w*0.4),(int)(h*(0.05)));
        g2.setColor(new Color(0,255,0));
        g2.fillOval((int)(w*0.4),(int)(h*(1 -(0.85+0.1) )) ,(int)(w*0.2),(int)(w*0.2));
        g2.setColor(new Color(0,255,0));
        g2.fillRect((int)(w*(0.4)),(int)(h*(1 -(0.85+0.05))) ,(int)(w*0.20),(int)(h*(0.225+0.055)));
        g2.setColor(FLColor);
        g2.fillRect((int)(w*0.02),(int)(h*(1 -0.825)) ,(int)(w*0.20),(int)(h*0.35/2));
        g2.setColor(FRColor);
        g2.fillRect((int)(w*(1-(0.22))),(int)(h*(1 -0.825)) ,(int)(w*0.20),(int)(h*0.35/2));
        g2.setColor(new Color(0,204,0));
        g2.fillRect((int)(w*0.12),(int)(h*(1-0.10))  ,(int)(w*0.76),(int)(h*0.15/2));
        g2.setColor(new Color(0,204,0));
        g2.fillRect((int)(w*0.4),(int)(h*(1-0.40)) ,(int)(w*0.2),(int)(h*(0.2)));

            /*
             int h = (int)height;
            int w = (int)width;
            g2.fillRect(12,h -20 ,76,15);
            g2.setColor(Color.GREEN);
            g2.fillRect(2,h -(35+(20-15)+15/2) ,20,35);
            g2.fillRect(w-(2+20),h -(35+(20-15)+15/2) ,20,35);
            g2.setColor(Color.RED);
            g2.fillRect(12+2,h -15-5 -65 ,76-2-2,65);
            g2.setColor(Color.BLUE);
            g2.fillRect((int)(12+2+(72/2-40/2)),h -(5+15+65+40) ,40,40);
            g2.setColor(Color.PINK);
            g2.fillRect((int)(12+2+(72/2-20/2)),h -(5+15+70+35+45) ,20,45);
            g2.setColor(Color.GREEN);
            g2.fillRect(2,h -(5+15+70+35+40) ,20,35);
            g2.fillRect(w-(2+20),h -(5+15+70+35+40) ,20,35);
            g2.setColor(Color.GRAY);
            g2.fillRect(2+20,h -(5+15+70+35+40/2+5) ,76-20,5);
            */



        // g2.setColor(Color.RED);
        // g2.drawRect(60,60,80,30);
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



    public void setTireFLColor(Color FLColor) {
        this.FLColor = FLColor;
    }
    public void setTireFRColor(Color FRColor) {
        this.FRColor = FRColor;
    }
    public void setTireBLColor(Color BLColor) {
        this.BLColor = BLColor;
    }
    public void setTireBRColor(Color BRColor) {
        this.BRColor = BRColor;
    }
    public void setWingFLColor(Color FLWColor) {
        this.FLWColor = FLWColor;
    }
    public void setWingFRColor(Color FRWColor) {
        this.FRWColor = FRWColor;
    }


//////////////////////////////////////////////////////////////////////////////////////////////

}
