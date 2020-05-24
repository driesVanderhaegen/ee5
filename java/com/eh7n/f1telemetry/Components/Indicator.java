package com.eh7n.f1telemetry.Components;



import javax.swing.*;
import java.awt.*;
// geom levert 2d java classes
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
public class Indicator extends JComponent {

    private static final int VALUE_FONT_SIZE = 18;

    public static final String LINE = "line";
    public static final String ARC = "arc";
    public static final String CIRCLE = "circle";
    //public static final String  SEMI_CIRCLE = "semi_circle";

    public static final double CIRCLE_ANGLE = 360;
    public static final int ARC_ANGLE_INDEX = 3;
    public static final int SEMI_ANGLE_INDEX = 11;
    public static final double CIRCLE_ANGLE_START = 0;

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

    private Color majorScaleColor;
    private Color minorScaleColor;
    private Color pointerColor;
    private Color textColor;
    private Color valueColor;

    public Indicator() {
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
        drawLine();
        drawLineText();


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







    private void drawLine() {
        // als het breder is dan hoog(gewoon) ga je een horizontaleschaal gebruiken(met verticale lijnen)
        if (width > height) {
            //bereken eenheden en teken de schaal
            // dunit is een schalingsfactor(voor de breedte van indicator en de to(maxkmperuur)
            double dunit = width / (to - from);
            drawHorizontalLineScale(dunit);
            drawHorizontalLineValue(dunit);
        } else
        {// anders verticaal
            int max = (int) Math.max(getStrBounds(g2, format(from)).getWidth(), getStrBounds(g2, format(to)).getWidth());
            double dunit = height / (to - from);
            drawVerticalLineScale(max, dunit);
            drawVerticalLineValue(max, dunit);
        }
    }

    private void drawVerticalLineScale(double max, double dunit) {
        for (int i = 0; i <= (to - from) / major; i++) {
            if(majorScaleColor != null)
                g2.setColor(majorScaleColor);
            g2.draw(new Line2D.Double(0, height - i * major * dunit, width - max, height - i * major * dunit));
            if (i < (to - from) / major && minor > 0) {
                for (int j = 1; j < major / minor; j++) {
                    if(minorScaleColor != null)
                        g2.setColor(minorScaleColor);
                    g2.draw(new Line2D.Double(0, height - (i * major + j * minor) * dunit, (width - max) / 2, height - (i * major + j * minor) * dunit));
                }
            }
        }
    }

    private void drawHorizontalLineScale(double dunit) {
        //major = 50;
        //dunit = 2;
        // om de grote lijnen te tekenen
        // vb to = 350,from=0 en major = 250 dus je hebt i grote lijnenen
        for (int i = 0; i <= (to - from) / major; i++) {
            if(majorScaleColor != null)
                g2.setColor(majorScaleColor);
            //een lijn tekenen die telkens de major uitelkaar ligt
            // en die gescaleert is met dunit om hem tusse de breedtevan het scherm te krijgen(width) met rekening te houde met de to
            g2.draw(new Line2D.Double(i * major * dunit, 0, i * major * dunit, height - fontSize));
            // om de kleine lijne te tekenen
            if (i < (to - from) / major && minor > 0) {
                for (int j = 1; j < major / minor; j++) {
                    if(minorScaleColor != null)
                        g2.setColor(minorScaleColor);
                    g2.draw(new Line2D.Double((i * major + j * minor) * dunit, 0, (i * major + j * minor) * dunit, (height - fontSize) / 2));
                }
            }
        }
    }

    private void drawVerticalLineValue(double max, double dunit) {
        if (value.length() > 0) {
            double val = toDouble(value);
            GeneralPath p = new GeneralPath();
            p.moveTo((width - max) / 2, height - (val - from) * dunit);
            p.lineTo(width - max, height - (val - from) * dunit - 4);
            p.lineTo(width - max, height - (val - from) * dunit + 4);
            p.closePath();
            if(valueColor != null)
                g2.setColor(valueColor);
            g2.fill(p);
        }
    }

    private void drawHorizontalLineValue(double dunit) {
        if (value.length() > 0) {
            // de stringwaarde omzetten naar een double
            double val = toDouble(value);
            //from = -20;
            GeneralPath p = new GeneralPath();
            p.moveTo((val - from) * dunit, (height - fontSize) / 2);
            p.lineTo((val - from) * dunit - 6, height - fontSize-20);
            p.lineTo((val - from) * dunit - 2, height - fontSize-20);
            p.lineTo((val - from) * dunit - 2, height - fontSize);
            p.lineTo((val - from) * dunit + 2, height - fontSize);
            p.lineTo((val - from) * dunit + 2, height - fontSize-20);
            p.lineTo((val - from) * dunit + 6, height - fontSize-20);
            p.closePath();
            if(valueColor != null)
                g2.setColor(valueColor);
            g2.fill(p);
        }
    }

    private void drawLineText() {
        if(textColor != null)
            g2.setColor(textColor);
        if (width > height) {
            double dunit = width / (to - from);
            int off = 0;
            String str;
            for (int i = 0; i <= (to - from) / major; i++) {
                // maak van elke grote lijn(i(een grote lijn) * major(afstand tussen de grotelijnen(in km/u)) een string waarde
                str = format(from + i * major);
                if (i == 0) {
                    //unit vb km/u
                    str += unit;
                    off = 0;
                } else if (i == (to - from) / major) {
                    off = (int) -getStrBounds(g2, str).getWidth();
                } else {
                    off = (int) (-getStrBounds(g2, str).getWidth() / 2);
                }
                g2.drawString(str, (int) (i * major * dunit) + off, (int) (height - 2));
            }
            if (value.length() > 0) {
                double val = toDouble(value);
                value = format(val);
                if (val == from) {
                    off = 0;
                } else if (val == to) {
                    off = (int) -getStrBounds(g2, value + unit).getWidth();
                } else {
                    off = (int) (-getStrBounds(g2, value + unit).getWidth() / 2);
                }
                if ((height - fontSize) / 2 >= fontSize) {
                    drawValue(g2, value + unit, (int) ((val - from) * dunit) + off, (int) ((height - fontSize) / 2));
                } else {
                    drawValue(g2, value + unit, (int) ((val - from) * dunit) + off, fontSize);
                }
            }
        } else {
            double dunit = height / (to - from);
            int max = (int) Math.max(getStrBounds(g2, format(from)).getWidth(), getStrBounds(g2, format(to)).getWidth());
            int off = 0;
            for (int i = 0; i <= (to - from) / major; i++) {
                if (i == 0) {
                    off = 0;
                } else if (i == (to - from) / major) {
                    off = fontSize;
                } else {
                    off = fontSize / 2;
                }
                g2.drawString(format(from + i * major), (int) (width - max + 1), (int) (height - i * major * dunit) + off);
            }
            if (unit.length() > 0) {
                g2.drawString(unit, (int) ((width - max) / 2 + 1), (int) (height - fontSize));
            }
            if (value.length() > 0) {
                double val = toDouble(value);
                value = format(val);
                if (val == 0) {
                    off = 0;
                } else if (val == to) {
                    off = VALUE_FONT_SIZE;
                } else {
                    off = VALUE_FONT_SIZE / 2;
                }
                drawValue(g2, value + unit, (int) ((width - getStrBounds(g2, value + unit).getWidth()) / 2), (int) (height - (val - from) * dunit + off));
            }
        }
    }





    private void drawValue(Graphics2D g2, String value, int x, int y) {
        g2.setFont(new Font(Font.SERIF, Font.BOLD, VALUE_FONT_SIZE));
        if(valueColor != null)
            g2.setColor(valueColor);
        // teken de string waarde op de juiste positie op de schaal(dus niet de pijl)
        g2.drawString(value, x, y);
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

    public double getMajor() {
        return major;
    }

    public void setMajor(double major) {
        this.major = major;
    }

    public double getMinor() {
        return minor;
    }

    public void setMinor(double minor) {
        this.minor = minor;
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

    public void setMajorScaleColor(Color majorScaleColor) {
        this.majorScaleColor = majorScaleColor;
    }

    public void setMinorScaleColor(Color minorScaleColor) {
        this.minorScaleColor = minorScaleColor;
    }

    public void setPointerColor(Color pointerColor) {
        this.pointerColor = pointerColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public void setValueColor(Color valueColor) {
        this.valueColor = valueColor;
    }

}
//////////////////////////////////////////////////////////////////////////////////////////////
