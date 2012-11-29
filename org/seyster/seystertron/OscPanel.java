//OscPanel.java
//GUI for an oscillator

package org.seyster.seystertron;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class OscPanel extends javax.swing.JPanel implements ActionListener
{
    String name;
    SynthOsc osc;
    
    JSlider sliderPW;
    JSlider sliderTune;
    ButtonGroup radioWave;
    
    public OscPanel(String n, SynthOsc so)
    {
        super();
        
        name = n;
        osc = so;
        
        initComponents();
    }
    
    private void initComponents()
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
        
        panel = waveOptions();
        constraints.gridwidth = 2;
        constraints.gridheight = 2;
        gridBag.setConstraints(panel, constraints);
        add(panel);
        
        sliderPW = new JSlider(JSlider.VERTICAL);
        sliderPW.setPaintTicks(true);
        size = new Dimension();
        size.width = sliderPW.getPreferredSize().width;
        size.height = panel.getPreferredSize().height;
        //Set sliderPW options
        sliderPW.setPreferredSize(size);
        sliderPW.setToolTipText("Pulsewidth");
        sliderPW.setMinimum(10);
        sliderPW.setMaximum(90);
        sliderPW.setMinorTickSpacing(10);
        sliderPW.setMajorTickSpacing(20);
        sliderPW.setValue(50);
        sliderPW.setEnabled(false);
        sliderPW.addChangeListener(new PWListener(osc));
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = 1;
        gridBag.setConstraints(sliderPW, constraints);
        add(sliderPW);
        
        label = new JLabel("PW");
        label.setToolTipText("Pulsewidth");
        gridBag.setConstraints(label, constraints);
        add(label);
        
        label = new JLabel("Tune:");
        constraints.gridwidth = 1;
        gridBag.setConstraints(label, constraints);
        add(label);
        
        sliderTune = new JSlider();
        sliderTune.setPaintTicks(true);
        size = new Dimension();
        size.width = 75;
        size.height = sliderTune.getPreferredSize().height;
        sliderTune.setPreferredSize(size);
        sliderTune.setToolTipText("Tuning");
        sliderTune.setMinimum(-10);
        sliderTune.setMaximum(10);
        sliderTune.setMinorTickSpacing(1);
        sliderTune.setMajorTickSpacing(5);
        sliderTune.setValue(0);
        sliderTune.addChangeListener(new TuneListener(osc));
        gridBag.setConstraints(sliderTune, constraints);        
        add(sliderTune);
    }
    
    private JPanel waveOptions()
    {
        JPanel panel = new JPanel();
        JRadioButton radio;
        
        radioWave = new ButtonGroup();
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        radio = new JRadioButton("Sine");
        radio.setSelected(true);
        radio.addActionListener(this);
        radioWave.add(radio);
        panel.add(radio);
        
        radio = new JRadioButton("Pulse");
        radio.addActionListener(this);
        radioWave.add(radio);
        panel.add(radio);
        
        radio = new JRadioButton("Triangle");
        radio.addActionListener(this);
        radioWave.add(radio);
        panel.add(radio);
        
        radio = new JRadioButton("Sawtooth");
        radio.addActionListener(this);
        radioWave.add(radio);
        panel.add(radio);
        
        return panel;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        String actionCommand = e.getActionCommand();
        
        if (actionCommand == "Sine")
        {
            osc.setWave(SynthOsc.SINE);
            sliderPW.setEnabled(false);
        }
        else if(actionCommand == "Pulse")
        {
            osc.setWave(SynthOsc.PULSE);
            sliderPW.setEnabled(true);
        }
        else if(actionCommand == "Triangle")
        {
            osc.setWave(SynthOsc.TRIANGLE);
            sliderPW.setEnabled(false);
        }
        else if(actionCommand == "Sawtooth")
        {
            sliderPW.setEnabled(false);
            osc.setWave(SynthOsc.SAWTOOTH);
        }
    }
    
}

class PWListener implements ChangeListener
{
    SynthOsc osc;
    
    public PWListener(SynthOsc so)
    {
        osc = so;
    }
    
    public void stateChanged(ChangeEvent e)
    {
        JSlider sliderPW = (JSlider)e.getSource();
        int pw = (int)sliderPW.getValue();
        osc.setPW(pw);
    }
}

class TuneListener implements ChangeListener
{
    SynthOsc osc;
    
    public TuneListener(SynthOsc so)
    {
        osc = so;
    }
    
    public void stateChanged(ChangeEvent e)
    {
        JSlider sliderTune = (JSlider)e.getSource();
        int tune = (int)sliderTune.getValue();
        osc.setTune(tune);
    }
}
