//MixerPanel.java
//GUI for the mixer

package org.seyster.seystertron;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class MixerPanel extends javax.swing.JPanel implements ActionListener
{
    String name;
    SynthMixer mixer;
    
    JSlider sliderFader;
    
    public MixerPanel(String n, SynthMixer m)
    {
        name = n;
        mixer = m;
        
        initComponents();
    }
    
    private void initComponents()
    {
        setBorder(new javax.swing.border.EtchedBorder());
        setLayout(new BorderLayout());
        
        add(new JLabel(name), BorderLayout.NORTH);
        
        add(addRadioButtons(), BorderLayout.CENTER);
        add(addFader(), BorderLayout.SOUTH);
    }
    
    private JPanel addRadioButtons()
    {
        JPanel panel = new JPanel();
        ButtonGroup buttons = new ButtonGroup();
        JRadioButton radio;
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        radio = new JRadioButton("Add");
        radio.setSelected(true);
        radio.addActionListener(this);
        buttons.add(radio);
        panel.add(radio);
        
        radio = new JRadioButton("Ring Modulation");
        radio.addActionListener(this);
        buttons.add(radio);
        panel.add(radio);
        
        radio = new JRadioButton("Oscillator 1");
        radio.addActionListener(this);
        buttons.add(radio);
        panel.add(radio);
        
        radio = new JRadioButton("Oscillator 2");
        radio.addActionListener(this);
        buttons.add(radio);
        panel.add(radio);
        
        return panel;
    }
    
    private JPanel addFader()
    {
        JPanel panel = new JPanel();
        
        panel.setLayout(new FlowLayout());
        
        sliderFader = new JSlider();
        sliderFader.setPaintTicks(true);
        Dimension size = sliderFader.getPreferredSize();
        size.width = 100;
        sliderFader.setPreferredSize(size);
        sliderFader.setToolTipText("Cross Fader");
        sliderFader.setMinimum(0);
        sliderFader.setMaximum(100);
        sliderFader.setMinorTickSpacing(10);
        sliderFader.setMajorTickSpacing(50);
        sliderFader.setValue(50);
        sliderFader.addChangeListener(new FaderListener(mixer));
        
        panel.add(new JLabel("Osc 1"));
        panel.add(sliderFader);
        panel.add(new JLabel("Osc 2"));
        
        return panel;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        String actionCommand = e.getActionCommand();
        
        if (actionCommand == "Add")
        {
            sliderFader.setEnabled(true);
            mixer.setMixMode(SynthMixer.ADD);
        }
        else if (actionCommand == "Ring Modulation")
        {
            sliderFader.setEnabled(false);
            mixer.setMixMode(SynthMixer.RING_MOD);
        }
        else if (actionCommand == "Oscillator 1")
        {
            sliderFader.setEnabled(false);
            mixer.setMixMode(SynthMixer.SOURCE1);
        }
        else if (actionCommand == "Oscillator 2")
        {
            sliderFader.setEnabled(false);
            mixer.setMixMode(SynthMixer.SOURCE2);
        }
    }
}

class FaderListener implements ChangeListener
{
    SynthMixer mixer;
    
    public FaderListener(SynthMixer sm)
    {
        mixer = sm;
    }
    
    public void stateChanged(ChangeEvent e)
    {
        JSlider sliderFader = (JSlider)e.getSource();
        int value = (int)sliderFader.getValue();
        mixer.setMix(value);
    }
}
