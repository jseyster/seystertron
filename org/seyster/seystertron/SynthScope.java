//SynthScope.java
//An oscilliscope so we can watch the pretty signal

package org.seyster.seystertron;

import java.awt.*;
import javax.swing.*;

public class SynthScope extends javax.swing.JPanel implements SynthComponent
{
    SynthComponent source;
    
    private int width;
    private int height;
    
    private double trigger;  //Tells the scope where to trigger
    private boolean active;  //Whether or not the scope is "on"
    
    private int mark;  //Where we are in the auiData
    private double auiData[];
    
    public SynthScope(SynthComponent sc, int w, int h)
    {
        source = sc;
        width = w;
        height = h;
        trigger = 0;
        active = true;
        
        mark = 0;
        auiData = new double[width * 2];
        
        //Set the size
        Dimension size = new Dimension(width, height);
        setPreferredSize(size);
        
        setOpaque(true);
    }
    
    public void setTriggering(double t)
    {
        trigger = t;
        
        if (trigger < -1)
            trigger = -1;
        
        if (trigger > 1)
            trigger = 1;
    }
    
    public void setActive(boolean a)
    {
        active = a;
    }
    
    protected void paintComponent(Graphics g)
    {
        Dimension size = getSize();
        
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        
        g.setColor(Color.green);
        
        for (int i = 0 ; i < width ; i++)
        {
            g.fillRect(i, (int)((1 - auiData[i]) * height / 2), 1, 1);
        }
        
        //Draw in some grid-lines, for fun!        
        g.setColor(Color.gray);
        g.drawLine(0, height / 4, width, height / 4);
        g.drawLine(0, 3 * height / 4, width, 3 * height / 4);
        
        for (int i = width / 6 ; i < width ; i += width / 6)
            g.drawLine(i, 0, i, height);
        
        g.setColor(Color.lightGray);
        g.drawLine(0, height / 2, width, height / 2);
    }
    
    public void readAudio(double[] out, int samples)
    {
        source.readAudio(out, samples);
        
        if (!active)
            return;  //No need to do any more!
        
        for (int i = 0 ; i < samples ; i++)
        {
            //Prevent overflow
            if (mark >= auiData.length)
                mark = 0;
            
            /* Trigerring
             * The purpose of this is to jump back to the beginning of   *
             * scope at the start of a new cycle, so that the signal     *
             * displayed doesn't jump around.                            */
            if (mark > width && auiData[mark - 1] < trigger && out[i] > trigger)
                mark = 0;
            
            auiData[mark] = out[i];
            mark++;
        }
        
        if (mark > width)  //Repaint everytime we complete a full scan
            repaint();
    }
    
    public void panic()
    {
        for (int i = 0 ; i < auiData.length ; i++)
            auiData[i] = 0;
        
        source.panic();
    }
}
