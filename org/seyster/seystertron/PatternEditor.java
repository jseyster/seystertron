//PatternEditor.java
//Allows the user to specify patterns for the synth to play

package org.seyster.seystertron;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PatternEditor extends javax.swing.JComponent
{
    //Is the key a black key?
    private static final boolean[] keyColor = {false, true, false, true,
                                               false, false, true, false,
                                               true, false, true, false};
    private static final int UNSELECTED = 100;
    
    private int[] notes;  //Frequency of all the notes
    
    private SynthComponent source;
    private SynthOsc[] oscArray;
    private SynthEnvelope envelope;
    private LFO[] lfoArray;
    
    private int[] grid;
    private int position;
    
    private boolean playing;
    private int rate;  //Sampling rate
    private int noteLength; //How long is a sixteen note, in samples?
    private int notePos;
    
    private int lfoPos;  //Control how often the LFO is updated
    private int lfoInterval;
    
    public PatternEditor(SynthComponent sc, SynthOsc[] so, SynthEnvelope se,
                         LFO[] lfo, int samplingRate)
    {
        source = sc;
        oscArray = so;
        envelope = se;
        lfoArray = lfo;
        rate = samplingRate;
        
        noteLength = rate / 8;  //About 120 BPM
        notePos = 0;
        playing = false;
        
        lfoPos = 0;
        lfoInterval = rate / 100; //Update every 1/100 seconds
        
        notes = new int[37];
        initNotes();
        
        setPreferredSize(new Dimension(360, 390));
        setOpaque(true);
        
        position = 0;
        grid = new int[32];
        for (int i = 0 ; i < 32 ; i++)
            grid[i] = UNSELECTED;
        
        //Set up the pretty default pattern.  Thanks to Robert Kipp for this pattern!
        grid[0] = 24;
        grid[1] = 21;
        grid[2] = 19;
        grid[3] = 18;
        grid[4] = 17;
        grid[5] = 14;
        grid[6] = 12;
        grid[7] = 14;
        grid[8] = 12;
        grid[9] = 14;
        grid[10] = 17;
        grid[11] = 18;
        grid[12] = 21;
        grid[13] = 24;
        grid[15] = 22;
        grid[27] = 19;
        grid[28] = 17;
        grid[29] = 15;
        grid[30] = 17;
        grid[31] = 19;
        
        enableEvents(MouseEvent.MOUSE_CLICKED);
    }
    
    protected void paintComponent(Graphics g)
    {
        int y = 10;
        int height;
        
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        //Paint the keys
        g.setColor(Color.black);
        
        //Start with the highest C
        g.drawRect(-1, 0, 41, 10);
        
        for (int i = 0 ; i < 3 ; i++)
        {
            for (int j = keyColor.length - 1 ; j >= 0 ; j--)
            {
                height = 10;
            
                if (j != keyColor.length - 1 && keyColor[j + 1])
                {
                    height += 5;
                    y -= 5;
                }
            
                if (j != 0 && keyColor[j - 1])
                    height += 5;
            
                if (keyColor[j])
                {
                    y -= 5;
                    g.fillRect(-1, y, 30, height);
                }
                else
                {
                    g.drawRect(-1, y, 41, height);
                }
            
                y += height;                
            }
        }
        
        //Draw the grid
        g.setColor(Color.lightGray);
        for (int i = 50 ; i < 361 ; i += 10)
            g.drawLine(i, 0, i, 380);
        
        g.setColor(Color.black);
        for (int i = 40 ; i < 361 ; i += 40)
            g.drawLine(i, 0, i, 380);
        
        g.setColor(Color.red);
        g.drawLine(200, 0, 200, 380);
        
        g.setColor(Color.black);
        for (int i = 0 ; i < 381 ; i+= 10)
            g.drawLine(40, i, 360, i);
        
        //Rest
        g.drawString("Rest", 4, 380);
        
        //Paint in the pattern
        int x, width;
        int value = 0;
        
        g.setColor(Color.green);
        for (int i = 0 ; i < 32 ; i ++)
        {
            width = 7;
            x = 42 + i * 10;
            
            if (grid[i] != UNSELECTED)
                value = grid[i];
            
            if (grid[i] == UNSELECTED)
            {
                x -= 2;
                width += 2;
            }
            
            if (i < 31 && grid[i + 1] == UNSELECTED)
            {
                width += 2;
            }
            
            g.fillRect(x, 2 + value * 10, width, 7);
        }
        
        //Draw the position marker
        x = position * 10 + 42;
        g.fillOval(x, 382, 7, 7);
    }
    
    protected void processMouseEvent(MouseEvent e)
    {
        int id = e.getID();
        int x = e.getX();
        int y = e.getY();
        
        if (id == MouseEvent.MOUSE_CLICKED)
        {
            if (x >= 40 && x <= 360 && y <= 380)
            {
                int box = (x - 40) / 10;
                int value = y / 10;
                
                if (grid[box] == value && box != 0)
                    grid[box] = UNSELECTED;
                else
                    grid[box] = value;
            }
            
            repaint();
        }
    }
    
    private void initNotes()
    {
        double halfStep = Math.pow(2.0d, 1.0d / 12.0d);
        double freq = 440;
        
        notes[15] = 440; //Concert A;
        
        //Down the scale!
        for (int i = 16 ; i < notes.length ; i++)
        {
            freq /= halfStep;
            notes[i] = (int)freq;
        }
        
        //Up the scale!
        freq = 440;
        
        for (int i = 14 ; i >= 0 ; i--)
        {
            freq *= halfStep;
            notes[i] = (int)freq;
        }
    }
    
    private void incPosition()
    {
        position++;
        
        if (position > 31)
            position = 0;
        
        repaint();
        
        //Change the note on the oscillators
        if (grid[position] == 37) //Rest
        {
            envelope.triggerRelease();
        }
        else if(grid[position] != UNSELECTED)
        {
            envelope.triggerAttack();
            for (int i = 0 ; i < oscArray.length ; i++)
                oscArray[i].setFreq(notes[grid[position]]);
        }
    }
    
    public void play()
    {
        playing = true;
        
        //Play the current note
        notePos = 0;
        position--;
        incPosition();
    }
    
    public void pause()
    {
        playing = false;
    }
    
    public void stop()
    {
        playing = false;
        position = 0;
        envelope.triggerRelease();
        
        repaint();
    }
    
    public void clear()
    {
        grid[0] = 37;
        
        for (int i = 1 ; i < 32 ; i++)
            grid[i] = UNSELECTED;
        
        envelope.triggerRelease();
        
        repaint();
    }
    
    //Stop everything!  Clear all audio buffers!
    public void panic()
    {
        stop();
        source.panic();
    }
    
    public void readAudio(double[] out, int samples)
    {
        source.readAudio(out, samples);
        
        //Update the LFO's
        lfoPos += samples;
        
        if (lfoPos >= lfoInterval)
        {
            for (int i = 0 ; i < lfoArray.length ; i++)
                lfoArray[i].update(lfoPos);
            
            lfoPos %= lfoInterval;
        }
        
        //Check if it's time to play the next note
        if (playing)
        {
            notePos += samples;
        
            if (notePos > noteLength)
            {
                notePos -= noteLength;
                incPosition();
            }
        }
    }    
}
