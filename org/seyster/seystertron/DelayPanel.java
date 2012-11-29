//DelayPanel.java
//A GUI for the digital delay unit

package org.seyster.seystertron;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class DelayPanel extends JPanel implements ActionListener, ChangeListener
{
    String name;
    SynthDelay delay;
    
    JSlider sliderDelay;
    JSlider sliderLevel;
    JSlider sliderFeedback;
    JCheckBox checkOn;
    
    public DelayPanel(String n, SynthDelay sd)
    {
        name = n;
        delay = sd;
        
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
        
        //Delay
        sliderDelay = new JSlider(JSlider.VERTICAL);
        sliderDelay.setPaintTicks(true);
        sliderDelay.setToolTipText("Delay");
        sliderDelay.setMinimum(100);
        sliderDelay.setMaximum(2000);
        sliderDelay.setMinorTickSpacing(38);
        sliderDelay.setMajorTickSpacing(380);
        sliderDelay.setValue(300);
        sliderDelay.addChangeListener(this);
        panel.add(sliderDelay);
        
        //Level
        sliderLevel = new JSlider(JSlider.VERTICAL);
        sliderLevel.setPaintTicks(true);
        sliderLevel.setToolTipText("Level");
        sliderLevel.setMinimum(0);
        sliderLevel.setMaximum(500);
        sliderLevel.setMinorTickSpacing(25);
        sliderLevel.setMajorTickSpacing(100);
        sliderLevel.setValue(250);
        sliderLevel.addChangeListener(this);
        panel.add(sliderLevel);
        
        //Feedback
        sliderFeedback = new JSlider(JSlider.VERTICAL);
        sliderFeedback.setPaintTicks(true);
        sliderFeedback.setToolTipText("Feedback");
        sliderFeedback.setMinimum(0);
        sliderFeedback.setMaximum(1000);
        sliderFeedback.setMinorTickSpacing(50);
        sliderFeedback.setMajorTickSpacing(200);
        sliderFeedback.setValue(200);
        sliderFeedback.addChangeListener(this);
        panel.add(sliderFeedback);
        
        return panel;
    }
    
    private JPanel labelPanel()
    {
        JLabel label;
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        
        //Delay
        label = new JLabel("Delay");
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label);
        
        //Level
        label = new JLabel("Level");
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label);
        
        //Feedback
        label = new JLabel("FB");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setToolTipText("Feeback");
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
    
/*
    private void initComponents()
    {
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        
        JLabel label;
        
        setBorder(new javax.swing.border.EtchedBorder());        
        setLayout(gridBag);
        
        label = new JLabel(name);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.WEST;
        gridBag.setConstraints(label, constraints);
        add(label);
        
        //Delay
        sliderDelay = new JSlider(JSlider.VERTICAL);
        sliderDelay.setPaintTicks(true);
        sliderDelay.setToolTipText("Delay");
        sliderDelay.setMinimum(100);
        sliderDelay.setMaximum(2000);
        sliderDelay.setMinorTickSpacing(38);
        sliderDelay.setMajorTickSpacing(380);
        sliderDelay.setValue(300);
        sliderDelay.addChangeListener(this);
        
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.ipadx = 20;
        gridBag.setConstraints(sliderDelay, constraints);
        add(sliderDelay);
        
        //Level
        sliderLevel = new JSlider(JSlider.VERTICAL);
        sliderLevel.setPaintTicks(true);
        sliderLevel.setToolTipText("Level");
        sliderLevel.setMinimum(0);
        sliderLevel.setMaximum(500);
        sliderLevel.setMinorTickSpacing(25);
        sliderLevel.setMajorTickSpacing(100);
        sliderLevel.setValue(250);
        sliderLevel.addChangeListener(this);
        
        gridBag.setConstraints(sliderLevel, constraints);
        add(sliderLevel);
        
        //Feedback
        sliderFeedback = new JSlider(JSlider.VERTICAL);
        sliderFeedback.setPaintTicks(true);
        sliderFeedback.setToolTipText("Feedback");
        sliderFeedback.setMinimum(0);
        sliderFeedback.setMaximum(1000);
        sliderFeedback.setMinorTickSpacing(50);
        sliderFeedback.setMajorTickSpacing(200);
        sliderFeedback.setValue(200);
        sliderFeedback.addChangeListener(this);
        
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.setConstraints(sliderFeedback, constraints);
        add(sliderFeedback);
        
        //Labels
        //Delay
        label = new JLabel("Delay");
        label.setToolTipText("Delay");
        constraints.gridwidth = 1;
        gridBag.setConstraints(label, constraints);
        add(label);
        
        //Level
        label = new JLabel("Level");
        label.setToolTipText("Level");
        gridBag.setConstraints(label, constraints);
        add(label);
        
        //Feedback
        label = new JLabel("FB");
        label.setToolTipText("Feedback");
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.setConstraints(label, constraints);
        add(label);
        
        //On, off button
        checkOn = new JCheckBox("On");
        checkOn.addActionListener(this);
        constraints.gridwidth = 1;
        gridBag.setConstraints(checkOn, constraints);
        add(checkOn);
    }
*/

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand() == "On")
            delay.setActive(checkOn.isSelected());
    }
    
    public void stateChanged(ChangeEvent e)
    {
        double delayTime, level, feedback;
        
        delayTime = (double)sliderDelay.getValue() / 1000.0d;
        level = (double)sliderLevel.getValue() / 1000.0d;
        feedback = (double)sliderFeedback.getValue() / 1000.0d;
        
        delay.setDelay(delayTime);
        delay.setLevel(level);
        delay.setFeedback(feedback);
    }
    
}
