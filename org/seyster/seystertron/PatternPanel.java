//PatternPanel.java
//Some buttons to control the Pattern Editor

package org.seyster.seystertron;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PatternPanel extends JPanel implements ActionListener
{
    PatternEditor editor;
    
    String name;
    PatternFrame frame;
    
    JButton buttonPlay;
    JButton buttonStop;
    
    public PatternPanel(String n, PatternEditor pe)
    {
        name = n;
        editor = pe;
        
        frame = new PatternFrame(editor);
        
        initComponents();
    }
    
    private void initComponents()
    {
        JPanel panel;
        JLabel label;
        JButton button;
        
        setBorder(new javax.swing.border.EtchedBorder());  
        setLayout(new GridLayout(3, 1));
        
        label = new JLabel(name);
        label.setVerticalAlignment(JLabel.TOP);
        add(label);
        
        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        
        button = new JButton("Show Pattern");
        button.addActionListener(this);
        panel.add(button);
        
        buttonPlay = new JButton("Play");
        buttonPlay.addActionListener(this);
        panel.add(buttonPlay);
        
        buttonStop = new JButton("Stop");
        buttonStop.addActionListener(this);
        buttonStop.setEnabled(false);
        panel.add(buttonStop);
        
        add(panel);
        
        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        
        button = new JButton("Clear Pattern");
        button.addActionListener(this);
        panel.add(button);
        
        button = new JButton("Panic!");
        button.setForeground(Color.red);
        button.addActionListener(this);
        panel.add(button);
        
        add(panel);        
    }
    
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        
        if (command == "Show Pattern")
        {
            frame.show();
            frame.setState(JFrame.NORMAL);
        }
        else if (command == "Play")
        {
            buttonPlay.setText("Pause");
            buttonPlay.setActionCommand("Pause");
            
            buttonStop.setEnabled(true);
            
            editor.play();
        }
        else if (command == "Pause")
        {
            buttonPlay.setText("Play");
            buttonPlay.setActionCommand("Play");
            
            buttonStop.setEnabled(false);
            
            editor.pause();
        }
        else if (command == "Stop")
        {
            buttonPlay.setText("Play");
            buttonPlay.setActionCommand("Play");
            
            buttonStop.setEnabled(false);
            
            editor.stop();
        }
        else if (command == "Clear Pattern")
        {
            editor.clear();
        }
        else if (command == "Panic!")
        {
            buttonPlay.setText("Play");
            buttonPlay.setActionCommand("Play");
            
            buttonStop.setEnabled(false);
            
            editor.panic();
        }
    }    
}

class PatternFrame extends JFrame
{
    private PatternEditor editor;
    
    public PatternFrame(PatternEditor pe)
    {
        editor = pe;
        
        getContentPane().add(pe);
        
        setSize(370, 430);
    }
    
}
