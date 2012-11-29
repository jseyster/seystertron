//LFOPanel.java
//Provides a GUI interface for the low-frequency oscillators

package org.seyster.seystertron;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class LFOPanel extends javax.swing.JPanel implements ActionListener
{
    private String name;
    private LFO lfo;
    
    private JComboBox comboPath;
    private JSlider sliderFreq;
    private JSlider sliderDepth;
    
    public LFOPanel(String n, LFO l)
    {
        name = n;
        lfo = l;
        
        initComponents();
    }


    private void initComponents()
    {
        setBorder(new javax.swing.border.EtchedBorder());
        setLayout(new BorderLayout());
        
        JLabel label;
        
        label = new JLabel(name);
        add(label, BorderLayout.NORTH);
        add(waveOptions(), BorderLayout.WEST);
        add(sliderPanel(), BorderLayout.CENTER);
        add(pathPanel(), BorderLayout.SOUTH);
    }
    
    //Returns a panel with radio buttons in it
    private JPanel waveOptions()
    {
        JPanel panel = new JPanel();
        JRadioButton radio;
        ButtonGroup radioWave = new ButtonGroup();
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        radio = new JRadioButton("Triangle");
        radio.setSelected(true);
        radio.addActionListener(this);
        radioWave.add(radio);
        panel.add(radio);
        
        radio = new JRadioButton("Sawtooth");
        radio.addActionListener(this);
        radioWave.add(radio);
        panel.add(radio);
        
        radio = new JRadioButton("Square");
        radio.addActionListener(this);
        radioWave.add(radio);
        panel.add(radio);
        
        return panel;
    }
    
    //Returns a panel with the sliders in it
    private JPanel sliderPanel()
    {
        //GridBagLayout gridBag = new GridBagLayout();
        //GridBagConstraints constraints = new GridBagConstraints();
        
        JPanel panel = new JPanel();
        JPanel sliderPanel = new JPanel();
        JPanel labelPanel = new JPanel();
        JLabel label;
        
        panel.setLayout(new BorderLayout());
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.X_AXIS));
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
        
        sliderFreq = new JSlider(JSlider.VERTICAL);
        sliderFreq.setPaintTicks(true);
        
        //Set sliderFreq options
        sliderFreq.setToolTipText("Frequency");
        sliderFreq.setMinimum(1000);
        sliderFreq.setMaximum(8000);
        sliderFreq.setMinorTickSpacing(1000);
        sliderFreq.setMajorTickSpacing(7000);
        sliderFreq.setValue(3000);
        sliderFreq.addChangeListener(new FreqListener(lfo));
        //constraints.fill = GridBagConstraints.VERTICAL;
        //gridBag.setConstraints(sliderFreq, constraints);
        sliderPanel.add(sliderFreq);
        
        sliderDepth = new JSlider(JSlider.VERTICAL);
        sliderDepth.setPaintTicks(true);
        //Set sliderFreq options
        sliderDepth.setToolTipText("LFO Depth");
        sliderDepth.setMinimum(200);
        sliderDepth.setMaximum(1000);
        sliderDepth.setMinorTickSpacing(100);
        sliderDepth.setMajorTickSpacing(200);
        sliderDepth.setValue(800);
        sliderDepth.addChangeListener(new DepthListener(lfo));
        //constraints.gridwidth = GridBagConstraints.REMAINDER;
        //gridBag.setConstraints(sliderDepth, constraints);
        sliderPanel.add(sliderDepth);
        
        label = new JLabel("Freq");
        label.setToolTipText("Frequency");
        //constraints.gridwidth = 1;
        //constraints.fill = GridBagConstraints.NONE;
        //gridBag.setConstraints(label, constraints);
        labelPanel.add(label);
        
        label = new JLabel("Depth");
        label.setToolTipText("LFO Depth");
        //constraints.gridwidth = GridBagConstraints.REMAINDER;
        //gridBag.setConstraints(label, constraints);
        labelPanel.add(label);
        
        panel.add(sliderPanel, BorderLayout.CENTER);
        panel.add(labelPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    //Returns a panel with the path combo box in it
    private JPanel pathPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        
        panel.add(new JLabel("Path:"));
        
        //Set up the combo box
        String[] stringPath = new String[] {"Off",
                                            "Filter Cutoff",
                                            "Osc 1 Tune",
                                            "Osc 2 Tune",
                                            "Osc 1 & 2 Tune",
                                            "Osc 1 PW",
                                            "Osc 2 PW",
                                            "Osc 1 & 2 PW" };

        comboPath = new JComboBox(stringPath);
        comboPath.addActionListener(this);
        panel.add(comboPath);
        
        return panel;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        String actionCommand = e.getActionCommand();
        
        if (actionCommand == "comboBoxChanged")
        {
            switch(comboPath.getSelectedIndex())
            {
                case 0: //Off
                    lfo.setPath(LFO.OFF, -1);
                    break;
                case 1: //Filter Cutoff
                    lfo.setPath(LFO.CUTOFF, -1);
                    break;
                case 2: //Osc 1 Tune
                    lfo.setPath(LFO.TUNING, 0);
                    break;
                case 3: //Osc 2 Tune
                    lfo.setPath(LFO.TUNING, 1);
                    break;
                case 4: //Osc 1 & 2 Tune
                    lfo.setPath(LFO.TUNING, LFO.ALL_OSCILLATORS);
                    break;
                case 5: //Osc 1 PW
                    lfo.setPath(LFO.PULSEWIDTH, 0);
                    break;
                case 6: //Osc 2 PW
                    lfo.setPath(LFO.PULSEWIDTH, 1);
                    break;
                case 7: //Osc 1 & 2 PW
                    lfo.setPath(LFO.PULSEWIDTH, LFO.ALL_OSCILLATORS);
                    break;
            }
        }
        else if (actionCommand == "Triangle")
        {
            lfo.setWave(LFO.TRIANGLE);
        }
        else if (actionCommand == "Sawtooth")
        {
            lfo.setWave(LFO.SAWTOOTH);
        }
        else if (actionCommand == "Square")
        {
            lfo.setWave(LFO.SQUARE);
        }
    }
}

class FreqListener implements ChangeListener
{
    LFO lfo;
    
    public FreqListener(LFO l)
    {
        lfo = l;
    }
    
    public void stateChanged(ChangeEvent e)
    {
        JSlider sliderFreq = (JSlider)e.getSource();
        double freq = (double)sliderFreq.getValue() / 1000.0d;
        lfo.setFreq(freq);
    }
}

class DepthListener implements ChangeListener
{
    LFO lfo;
    
    public DepthListener(LFO l)
    {
        lfo = l;
    }
    
    public void stateChanged(ChangeEvent e)
    {
        JSlider sliderDepth = (JSlider)e.getSource();
        double depth = (double)sliderDepth.getValue() / 1000.0d;
        lfo.setDepth(depth);
    }
}
