//GUI interface for the chorus effect

package org.seyster.seystertron;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class ChorusPanel extends JPanel implements ActionListener,ChangeListener
{
    String name;
    SynthChorus chorus;
    
    JSlider sliderRate;
    JSlider sliderLevel;
    JSlider sliderDepth;
    JCheckBox checkOn;
    
    public ChorusPanel(String n, SynthChorus sc)
    {
        name = n;
        chorus = sc;
        
        initComponents();
    }
    
    private JPanel centerPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        panel.add(sliderPanel(), BorderLayout.CENTER);
        panel.add(labelPanel(), BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel sliderPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        
        //Rate
        sliderRate = new JSlider(JSlider.VERTICAL);
        sliderRate.setPaintTicks(true);
        sliderRate.setToolTipText("Rate");
        sliderRate.setMinimum(500);
        sliderRate.setMaximum(3000);
        sliderRate.setMinorTickSpacing(250);
        sliderRate.setMajorTickSpacing(500);
        sliderRate.setValue(1000);
        sliderRate.addChangeListener(this);
        panel.add(sliderRate);
        
        //Level
        sliderLevel = new JSlider(JSlider.VERTICAL);
        sliderLevel.setPaintTicks(true);
        sliderLevel.setToolTipText("Level");
        sliderLevel.setMinimum(0);
        sliderLevel.setMaximum(1000);
        sliderLevel.setMinorTickSpacing(50);
        sliderLevel.setMajorTickSpacing(200);
        sliderLevel.setValue(800);
        sliderLevel.addChangeListener(this);
        panel.add(sliderLevel);
        
        //Depth
        sliderDepth = new JSlider(JSlider.VERTICAL);
        sliderDepth.setPaintTicks(true);
        sliderDepth.setToolTipText("Depth");
        sliderDepth.setMinimum(10);
        sliderDepth.setMaximum(45);
        sliderDepth.setMinorTickSpacing(5);
        sliderDepth.setMajorTickSpacing(15);
        sliderDepth.setValue(30);
        sliderDepth.addChangeListener(this);
        panel.add(sliderDepth);
        
        return panel;
    }
    
    private JPanel labelPanel()
    {
        JLabel label;
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        
        //Rate
        label = new JLabel("Rate");
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label);
        
        //Level
        label = new JLabel("Level");
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label);
        
        //Depth
        label = new JLabel("Depth");
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label);
        
        return panel;
    }
    
    private void initComponents()
    {
        setBorder(new javax.swing.border.EtchedBorder());
        setLayout(new BorderLayout());
        
        add(new JLabel(name), BorderLayout.NORTH);
        add(centerPanel(), BorderLayout.CENTER);
        
        //On, off button
        checkOn = new JCheckBox("On");
        checkOn.addActionListener(this);
        add(checkOn, BorderLayout.SOUTH);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand() == "On")
            chorus.setActive(checkOn.isSelected());
    }
    
    public void stateChanged(ChangeEvent e)
    {
        double rate, level, depth;
        
        rate = (double)sliderRate.getValue() / 1000.0d;
        level = (double)sliderLevel.getValue() / 1000.0d;
        depth = (double)sliderDepth.getValue() / 10000.0d;
        
        chorus.setFreq(rate);
        chorus.setLevel(level);
        chorus.setDepth(depth);
    }
    
}
