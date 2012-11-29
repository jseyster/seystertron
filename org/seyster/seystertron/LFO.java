/* LFO.java
 * A low frequency oscillator that can can control oscillator tuning and    *
 * pulsewidth, as well as the filter cutoff.  This is not a SynthComponent, *
 * since no audio data is routed through it.                                */

package org.seyster.seystertron;

public class LFO
{
    public static final double LFO_OFF = 1000.0d;
    
    public static final int TRIANGLE = 0;
    public static final int SAWTOOTH = 1;
    public static final int SQUARE = 2;
    
    public static final int OFF = 0;
    public static final int CUTOFF = 1;
    public static final int TUNING = 2;
    public static final int PULSEWIDTH = 3;
    
    public static final int ALL_OSCILLATORS = 1000;
    
    private SynthOsc[] oscArray;  //The oscillators to be controlled
    private SynthFilter filter;   //the filter to be controlled
    
    private int rate;  //Sampling rate
    
    private double freq; //Frequency in Hz
    private int period; //Period in samples
    private int pos;  //Position in one period
    
    private int wave;
    private int path; //What paramater to modify
    private int oscillator;  //Which oscillator to modify
    
    private double depth;
    
    public LFO(int r, SynthOsc[] so, SynthFilter sf)
    {
        rate = r;
        oscArray = so;
        filter = sf;
        
        setFreq(3.0);
        pos = 0;
        
        wave = TRIANGLE;
        depth = 0.8;
        
        setPath(OFF, ALL_OSCILLATORS);
    }
    
    public void setFreq(double f)
    {
        freq = f;
        period = (int)((double)rate / freq);
    }
    
    public void setDepth(double d)
    {
        depth = d;
        
        if (depth < 0)
            depth = 0;
        
        if (depth > 1.0)
            depth = 1.0;
    }
    
    public void setWave(int w)
    {
        wave = w;
    }
    
    //Set the param to be modifed by the LFO
    //If osc is set to negative, the oscillator path won't be modified
    public void setPath(int p, int osc)
    {
        //Tell the component that was in the LFO path that it is no longer.
        if (path == CUTOFF)
            filter.updateLFO(LFO_OFF);
        else
            updateOsc(LFO_OFF);
        
        //Set the new path
        path = p;
        
        //Set the new oscillator path, only if it is positive
        if (osc >= 0)
            oscillator = osc;
        
        //Make sure an invalid oscillator isn't set
        if (oscillator > oscArray.length)
            oscillator = ALL_OSCILLATORS;
    }
    
    //Calculate the amplitude of the wave at the current position
    public double calcAmp()
    {
        switch(wave)
        {
            case TRIANGLE:
                if (pos <= period / 2)
                    return (4.0 * (double)pos / (double)period) - 1.0;
                else
                    return 3.0 - (4.0 * (double)pos / (double)period);
            case SAWTOOTH:
                return 1.0 - (2.0 * (double)pos / (double)period);
            default: //SQUARE
                if (pos <= period / 2)
                    return 1.0d;
                else
                    return -1.0d;
        }
    }
    
    //Figure out what oscillators need to be updated
    private void updateOsc(double value)
    {
        if (path == TUNING)
        {
            if (oscillator == ALL_OSCILLATORS)
                for (int i = 0 ; i < oscArray.length ; i++)
                    oscArray[i].updateTuneLFO(value);
            else
                oscArray[oscillator].updateTuneLFO(value);
        }
        else if(path == PULSEWIDTH)
        {
            if (oscillator == ALL_OSCILLATORS)
                for (int i = 0 ; i < oscArray.length ; i++)
                    oscArray[i].updatePWLFO(value);
            else
                oscArray[oscillator].updatePWLFO(value);
        }
    }
    
    //Update whatever paramaters are being modified
    public void update(int samples)
    {
        pos += samples;
        pos %= period;
        
        double value = depth * calcAmp();
        
        if (path == CUTOFF)
            filter.updateLFO(value);
        else
            updateOsc(value);
    }
}
