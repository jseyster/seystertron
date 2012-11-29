package org.seyster.seystertron;

import java.awt.*;
import javax.swing.*;
import javax.sound.sampled.*;

public class JSynth extends javax.swing.JApplet implements Runnable
{
    AudioFormat auiFormat;
    DataLine.Info info;
    SynthOsc osc1;
    SynthOsc osc2;
    SynthMixer mixer;
    SynthFilter filter;
    SynthDCFilter dcFilter;
    SynthEnvelope envelope;
    SynthChorus chorus;
    SynthDelay delay;
    SynthGain gain;
    SynthScope scope;
    GainPanel masterPanel;
    LFO lfo1, lfo2;
    PatternEditor editor;
    Thread kicker;
    
    public void init()
    {
        auiFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                                    44100.0f, 8, 1, 1, 44100.0f, false);
        
        info = new DataLine.Info(SourceDataLine.class, auiFormat);
        
        //The SynthComponent chain gets set up here!
        //osc1&2 -> mixer -> filter -> DCfilter -> envelope -> chorus ->
        //delay -> master gain -> oscilliscope
        osc1 = new SynthOsc(44100);
        osc2 = new SynthOsc(44100);
        mixer = new SynthMixer(osc1, osc2);
        filter = new SynthFilter(mixer, 44100);
        dcFilter = new SynthDCFilter(filter);
        envelope = new SynthEnvelope(dcFilter, 44100);
        chorus = new SynthChorus(envelope, 44100);
        delay = new SynthDelay(chorus, 44100);
        gain = new SynthGain(delay);
        scope = new SynthScope(gain, 300, 150);
        lfo1 = new LFO(44100, new SynthOsc[] {osc1, osc2}, filter);
        lfo2 = new LFO(44100, new SynthOsc[] {osc1, osc2}, filter);
        editor = new PatternEditor(scope, new SynthOsc[] {osc1,osc2}, envelope,
                                   new LFO[] {lfo1, lfo2}, 44100);
        
        initComponents();
    }
    
    public void start()
    {
        kicker = new Thread(this);
        kicker.start();
    }
    
    private void initComponents()
    {
        JPanel oscPanel, mixerPanel, filterPanel, lfoPanel, bottomPanel, panel;
        JPanel scopePanel;
        Container contentPane = getContentPane();
        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        
        contentPane.setLayout(gridBag);

        oscPanel = new OscPanel("Oscillator 1", osc1);
        gridBag.setConstraints(oscPanel, constraints);
        contentPane.add(oscPanel);
        
        panel = new OscPanel("Oscillator 2", osc2);
        gridBag.setConstraints(panel, constraints);
        contentPane.add(panel);

        mixerPanel = new MixerPanel("Mixer", mixer);
        mixerPanel.setPreferredSize(
            new Dimension(mixerPanel.getPreferredSize().width,
            oscPanel.getPreferredSize().height));
        gridBag.setConstraints(mixerPanel, constraints);
        contentPane.add(mixerPanel);
        
        panel = new LFOPanel("LFO 1", lfo1);
        panel.setPreferredSize(new Dimension(panel.getPreferredSize().width,
            oscPanel.getPreferredSize().height));
        gridBag.setConstraints(panel, constraints);
        contentPane.add(panel);
        
        lfoPanel = new LFOPanel("LFO 2", lfo2);
        lfoPanel.setPreferredSize(new Dimension(
            lfoPanel.getPreferredSize().width,
            oscPanel.getPreferredSize().height));
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.setConstraints(lfoPanel, constraints);
        contentPane.add(lfoPanel);

        filterPanel = new FilterPanel("Filter", filter);
        filterPanel.setPreferredSize(new Dimension(
            oscPanel.getPreferredSize().width,
            filterPanel.getPreferredSize().height));
        constraints.gridwidth = 1;
        gridBag.setConstraints(filterPanel, constraints);
        contentPane.add(filterPanel);
        
        panel = new EnvelopePanel("Envelope", envelope);
        panel.setPreferredSize(
            new Dimension(oscPanel.getPreferredSize().width +
            mixerPanel.getPreferredSize().width,
            filterPanel.getPreferredSize().height));
        constraints.gridwidth = 2;
        gridBag.setConstraints(panel, constraints);
        contentPane.add(panel);
        
        panel = new ChorusPanel("Chorus", chorus);
        panel.setPreferredSize(
            new Dimension(lfoPanel.getPreferredSize().width,
            filterPanel.getPreferredSize().height));
        constraints.gridwidth = 1;
        gridBag.setConstraints(panel, constraints);
        contentPane.add(panel);
        
        panel = new DelayPanel("Digital Delay", delay);
        panel.setPreferredSize(
            new Dimension(lfoPanel.getPreferredSize().width,
            filterPanel.getPreferredSize().height));
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.setConstraints(panel, constraints);
        contentPane.add(panel);
        
        bottomPanel = new JPanel();
        scopePanel = new ScopePanel("Oscilliscope", scope);
        masterPanel = new GainPanel("Master", gain);
        masterPanel.setPreferredSize(
            new Dimension(masterPanel.getPreferredSize().width,
            scopePanel.getPreferredSize().height));
        
        bottomPanel.setLayout(new BorderLayout());
        
        bottomPanel.add(masterPanel, BorderLayout.WEST);
        bottomPanel.add(new PatternPanel("Pattern", editor),
                                         BorderLayout.CENTER);
        bottomPanel.add(scopePanel, BorderLayout.EAST);
        
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        gridBag.setConstraints(bottomPanel, constraints);
        contentPane.add(bottomPanel);
    }
    
    public void run()
    {
        double[] auiData = new double[128];
        byte[] PCM = new byte[128];
        SourceDataLine sdl = null;
        
        try
        {
            sdl = (SourceDataLine)AudioSystem.getLine(info);
            sdl.open();
            sdl.start();
        }
        catch (Exception e) 
        {
            System.out.println(e);
            System.exit(1);
        }
        
        while(true)
        {
            //Compute audio data whenever the amount stored in the sound card's
            //buffer is less than the user set amount of latency
            if(sdl.getBufferSize() - sdl.available() < masterPanel.getLatency())
            {
                editor.readAudio(auiData, 128);
                for (int i = 0 ; i < 128 ; i++)
                    PCM[i] = (byte)(120 * auiData[i]);
            
                sdl.write(PCM, 0, 128);
            }
        }
    }    
}
