//FilterPanel.java
//Provides a GUI interface to the low-pass/high-pass filter

package org.seyster.seystertron;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class FilterPanel extends javax.swing.JPanel implements ActionListener
{
    String name;
    SynthFilter filter;
    
    JSlider sliderCutoff;
    JSlider sliderResonance;
    
    public FilterPanel(String n, SynthFilter sf)
    {
        name = n;
        filter = sf;
        
        initComponents();
    }
    
    public void initComponents()
    {
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        
        JLabel label;
        JPanel panel;
        Dimension size;
        
        setBorder(new javax.swing.border.EtchedBorder());
        setLayout(gridBag);
        
        label = new JLabel(name);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.setConstraints(label, constraints);
        add(label);
        
        panel = filterOptions();
        gridBag.setConstraints(panel, constraints);
        add(panel);
        
        sliderCutoff = new JSlider(JSlider.VERTICAL);
        sliderCutoff.setPaintTicks(true);
        sliderCutoff.setToolTipText("Cutoff Frequency");
        sliderCutoff.setMinimum(0);
        sliderCutoff.setMaximum(500);
        sliderCutoff.setMinorTickSpacing(25);
        sliderCutoff.setMajorTickSpacing(100);
        sliderCutoff.setValue(100);
        sliderCutoff.addChangeListener(new CutoffListener(filter));
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridwidth = 1;
        gridBag.setConstraints(sliderCutoff, constraints);
        add(sliderCutoff);
        
        sliderResonance = new JSlider(JSlider.VERTICAL);
        sliderResonance.setPaintTicks(true);
        sliderResonance.setToolTipText("Resonance (Pass-band Ripple)");
        sliderResonance.setMinimum(1);
        sliderResonance.setMaximum(58);
        sliderResonance.setMinorTickSpacing(3);
        sliderResonance.setMajorTickSpacing(57);
        sliderResonance.setValue(1);
        sliderResonance.addChangeListener(new ResonanceListener(filter));
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.setConstraints(sliderResonance, constraints);
        add(sliderResonance);
        
        label = new JLabel("Cutoff");
        constraints.gridwidth = 1;
        constraints.ipadx = 10;
        gridBag.setConstraints(label, constraints);
        add(label);
        
        label = new JLabel("Resonance");
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.setConstraints(label, constraints);
        add(label);
    }
    
    private JPanel filterOptions()
    {
        Dimension size;
        JPanel panel = new JPanel();
        
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        
        ButtonGroup buttons = new ButtonGroup();
        JRadioButton radio;
        JLabel label;
        JLabel filler;
        
        panel.setLayout(gridBag);
        
        label = new JLabel("Type:");
        constraints.anchor = GridBagConstraints.WEST;
        gridBag.setConstraints(label, constraints);
        panel.add(label);
        
        radio = new JRadioButton("Low-pass");
        radio.addActionListener(this);
        radio.setSelected(true);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.setConstraints(radio, constraints);
        buttons.add(radio);
        panel.add(radio);
        
        filler = new JLabel();
        constraints.gridwidth = 1;
        gridBag.setConstraints(label, constraints);
        panel.add(filler);
        
        radio = new JRadioButton("High-pass");
        radio.addActionListener(this);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.setConstraints(radio, constraints);
        buttons.add(radio);
        panel.add(radio);
        
        return panel;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        String actionCommand = e.getActionCommand();
        
        if (actionCommand == "Low-pass")
            filter.setLowpass(true);
        else if (actionCommand == "High-pass")
            filter.setLowpass(false);
    }
    
}

class CutoffListener implements ChangeListener
{
    SynthFilter filter;
    
    public CutoffListener(SynthFilter sf)
    {
        filter = sf;
    }
    
    public void stateChanged(ChangeEvent e)
    {
        JSlider sliderCutoff = (JSlider)e.getSource();
        int cutoff = (int)sliderCutoff.getValue();
        filter.setCutoff((double)cutoff / 4000.0d);
    }
}

class ResonanceListener implements ChangeListener
{
    SynthFilter filter;
    
    public ResonanceListener(SynthFilter sf)
    {
        filter = sf;
    }
    
    public void stateChanged(ChangeEvent e)
    {
        JSlider sliderResonance = (JSlider)e.getSource();
        int ripple = (int)sliderResonance.getValue();
        filter.setRipple((double)ripple / 2.0d);
    }
}
