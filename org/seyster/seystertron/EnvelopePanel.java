//EnvelopePanel.java
//Provides a GUI for the ADSR Envelope

package org.seyster.seystertron;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class EnvelopePanel extends JPanel implements ChangeListener
{
    SynthEnvelope envelope;
    
    String name;
    
    JSlider sliderAttack;
    JSlider sliderDecay;
    JSlider sliderSustain;
    JSlider sliderRelease;
    
    public EnvelopePanel(String n, SynthEnvelope se)
    {
        name = n;
        envelope = se;
        
        initComponents();
    }

    private void initComponents()
    {
        setLayout(new BorderLayout());
        setBorder(new javax.swing.border.EtchedBorder());
        
        add(new JLabel(name), BorderLayout.NORTH);
        add(sliderPanel(), BorderLayout.CENTER);
        add(labelPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel sliderPanel()
    {
        JPanel panel;
        JLabel label;
        
        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 4));
        
        //Atack
        sliderAttack = new JSlider(JSlider.VERTICAL);
        sliderAttack.setPaintTicks(true);
        sliderAttack.setToolTipText("Attack");
        sliderAttack.setMinimum(0);
        sliderAttack.setMaximum(500);
        sliderAttack.setMinorTickSpacing(25);
        sliderAttack.setMajorTickSpacing(100);
        sliderAttack.setValue(100);
        sliderAttack.addChangeListener(this);
        panel.add(sliderAttack);
        
        //Decay
        sliderDecay = new JSlider(JSlider.VERTICAL);
        sliderDecay.setPaintTicks(true);
        sliderDecay.setToolTipText("Decay");
        sliderDecay.setMinimum(0);
        sliderDecay.setMaximum(500);
        sliderDecay.setMinorTickSpacing(25);
        sliderDecay.setMajorTickSpacing(100);
        sliderDecay.setValue(100);
        sliderDecay.addChangeListener(this);
        panel.add(sliderDecay);
        
        //Sustain
        sliderSustain = new JSlider(JSlider.VERTICAL);
        sliderSustain.setPaintTicks(true);
        sliderSustain.setToolTipText("Sustain");
        sliderSustain.setMinimum(0);
        sliderSustain.setMaximum(1000);
        sliderSustain.setMinorTickSpacing(50);
        sliderSustain.setMajorTickSpacing(200);
        sliderSustain.setValue(600);
        sliderSustain.addChangeListener(this);
        panel.add(sliderSustain);
        
        //Release
        sliderRelease = new JSlider(JSlider.VERTICAL);
        sliderRelease.setPaintTicks(true);
        sliderRelease.setToolTipText("Release");
        sliderRelease.setMinimum(1000);
        sliderRelease.setMaximum(5000);
        sliderRelease.setMinorTickSpacing(200);
        sliderRelease.setMajorTickSpacing(800);
        sliderRelease.setValue(3000);
        sliderRelease.addChangeListener(this);
        panel.add(sliderRelease);
        
        return panel;
    }
    
    private JPanel labelPanel()
    {
        JPanel panel;
        JLabel label;
        
        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 4));
        
        //Attack
        label = new JLabel("A");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setToolTipText("Attack");
        panel.add(label);
        
        //Decay
        label = new JLabel("D");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setToolTipText("Decay");
        panel.add(label);
        
        //Sustain
        label = new JLabel("S");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setToolTipText("Sustain");
        panel.add(label);
        
        //Release
        label = new JLabel("R");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setToolTipText("Release");
        panel.add(label);
        
        return panel;
    }

    public void stateChanged(ChangeEvent e)
    {
        double a, d, s, r;
        
        a = (double)sliderAttack.getValue() / 1000.0d;
        d = (double)sliderDecay.getValue() / 1000.0d;
        s = (double)sliderSustain.getValue() / 1000.0d;
        r = (double)sliderRelease.getValue() / 1000.0d;
        
        envelope.setADSR(a, d, s, r);
    }
    
}
