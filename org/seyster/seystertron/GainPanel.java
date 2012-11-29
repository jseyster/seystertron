//GainPanel.java
//GUI interface for the gain control

package org.seyster.seystertron;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class GainPanel extends javax.swing.JPanel
{ 
    String name;
    SynthGain gain;
    
    JSlider sliderGain;
    JSlider sliderLatency;
    
    int latency;  //The latency, in samples
    
    public GainPanel(String n, SynthGain sg)
    {
        name = n;
        gain = sg;
        
        latency = 4410;
        
        initComponents();
    }
    
    private void initComponents()
    {
        setBorder(new javax.swing.border.EtchedBorder()); 
        setLayout(new BorderLayout());
        
        add(new JLabel(name), BorderLayout.NORTH);
        
        //Set sliderGain options
        sliderGain = new JSlider(JSlider.VERTICAL);
        sliderGain.setPaintTicks(true);
        sliderGain.setToolTipText("Master Gain");
        sliderGain.setMinimum(0);
        sliderGain.setMaximum(3000);
        sliderGain.setMinorTickSpacing(1000);
        sliderGain.setMajorTickSpacing(3000);
        sliderGain.setValue(1000);
        sliderGain.addChangeListener(new GainListener(gain));
        add(sliderGain, BorderLayout.CENTER);
        
        add(new JLabel("Gain"), BorderLayout.SOUTH);
        
        add(latencyPanel(), BorderLayout.EAST);
    }    
    
    //Returns a panel with the latency slider
    private JPanel latencyPanel()
    {
        JPanel panel = new JPanel();
        JLabel label;
        
        panel.setLayout(new GridLayout(4, 1));
        panel.add(new JLabel()); //Filler label
        
        label = new JLabel("Latency:");
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalAlignment(JLabel.LEFT);
        panel.add(label);
        
        sliderLatency = new JSlider(JSlider.HORIZONTAL);
        sliderLatency.setPreferredSize(new Dimension(
            label.getPreferredSize().width * 3 / 2,
            sliderLatency.getPreferredSize().height));
        sliderLatency.setPaintTicks(true);
        sliderLatency.setToolTipText("Latency");
        sliderLatency.setMinimum(2205);
        sliderLatency.setMaximum(8820);
        sliderLatency.setMinorTickSpacing(2205);
        sliderLatency.setMajorTickSpacing(4410);
        sliderLatency.setValue(4410);
        sliderLatency.addChangeListener(new LatencyListener(this));
        panel.add(sliderLatency);
        
        panel.add(new JLabel());  //Bottom filler
        
        return panel;
    }
    
    //The gain panel holds the master options, so it is responsible for
    //storing the value for master latency
    public void setLatency(int l)
    {
        if (l < 2205)
            l = 2205;
        
        if (l > 8820)
            l = 8820;
        
        latency = l;
    }
    
    public int getLatency()
    {
        return latency;
    }
}

class GainListener implements ChangeListener
{
    SynthGain gain;
    
    public GainListener(SynthGain sg)
    {
        gain = sg;
    }
    
    public void stateChanged(ChangeEvent e)
    {
        JSlider sliderGain = (JSlider)e.getSource();
        int gainValue = (int)sliderGain.getValue();
        gain.setGain((double)gainValue / 1000.0d);
    }
}

class LatencyListener implements ChangeListener
{
    GainPanel gainPanel;
    
    public LatencyListener(GainPanel gp)
    {
        gainPanel = gp;
    }
    
    public void stateChanged(ChangeEvent e)
    {
        JSlider sliderLatency = (JSlider)e.getSource();
        int latency = (int)sliderLatency.getValue();
        gainPanel.setLatency(latency);
    }
}
