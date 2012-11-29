//ScopePanel.java
//A GUI for SynthScope

package org.seyster.seystertron;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class ScopePanel extends javax.swing.JPanel implements ActionListener
{
    SynthScope scope;
    String name;
    
    JSlider sliderTrigger;
    JCheckBox checkOn;
    
    public ScopePanel(String n, SynthScope ss)
    {
        scope = ss;
        name = n;
        
        initComponents();
    }
    
    private void initComponents()
    {
        setBorder(new javax.swing.border.EtchedBorder());
        setLayout(new BorderLayout());
        
        add(new JLabel(name), BorderLayout.NORTH);
        
        add(scope, BorderLayout.CENTER);
        
        sliderTrigger = new JSlider(JSlider.VERTICAL);
        sliderTrigger.setPaintTicks(true);
        sliderTrigger.setToolTipText("Trigerring");
        sliderTrigger.setMinimum(-950);
        sliderTrigger.setMaximum(950);
        sliderTrigger.setMinorTickSpacing(50);
        sliderTrigger.setMajorTickSpacing(950);
        sliderTrigger.setValue(0);
        sliderTrigger.addChangeListener(new TriggerListener(scope));
        
        Dimension size = sliderTrigger.getPreferredSize();
        size.height = scope.getHeight();
        sliderTrigger.setPreferredSize(size);
        
        add(sliderTrigger, BorderLayout.EAST);
        
        checkOn = new JCheckBox("On");
        checkOn.setSelected(true);
        checkOn.addActionListener(this);
        add(checkOn, BorderLayout.SOUTH);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand() == "On")
            scope.setActive(checkOn.isSelected());
    }
    
}

class TriggerListener implements ChangeListener
{
    SynthScope scope;
    
    public TriggerListener(SynthScope ss)
    {
        scope = ss;
    }
    
    public void stateChanged(ChangeEvent e)
    {
        JSlider sliderTrigger = (JSlider)e.getSource();
        int value = (int)sliderTrigger.getValue();
        scope.setTriggering((double)value / 1000.0d);
    }
}
