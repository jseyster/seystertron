//SynthOsc.class
//A simple digital, monophonic oscillator

package org.seyster.seystertron;

public class SynthOsc implements SynthComponent
{
    public static final int SINE = 0;
    public static final int PULSE = 1;
    public static final int TRIANGLE = 2;
    public static final int SAWTOOTH = 3;
    
    private static final int BUF_SIZE = 1000;
    
    private double auiData[]; //Stores one period of PCM audio
    private int mark; //Where we currently are in outputing from the buffer
    private boolean oscFlag;    //Set when the pulse storied in AuiData needs
                                //needs to be recalculated
    
    private double lfoTuneValue;  //Values that may be modified by LFOs
    private double lfoPWValue;
    
    private int rate;  //Sampling rate (in Hz) of output
    private int noteFreq;  //Frequency of note
    private int freq;  //Frequency (in Hz) to oscillate at
    private int tune;  //How far the oscillator is detuned from the note
    private int period; //Period (in samples) of one oscillation
    private int pwValue; //Pulsewidth as set by user
    private int pw;   //Actual pulsewidth
    private int wave; //The type of wave
    
    public SynthOsc(int r)
    {
        rate = r;
        wave = SINE;
        pw = 50;
        tune = 0;
        setFreq(440);
        
        lfoTuneValue = LFO.LFO_OFF;
        lfoPWValue = LFO.LFO_OFF;
        
        auiData = new double[BUF_SIZE];
        
        mark = 0;
        oscFlag = false;
        
        calcOsc();
    }
    
    public int getFreq()
    {
        return noteFreq;
    }
    
    //Set the base frequency of the note
    public void setFreq(int f)
    {
        noteFreq = f;
        
        setFreq();
    }
    
    
    //Update the frequency, for tuning, LFO, etc
    public void setFreq()
    {        
        //Set the frequency to be played
        if(lfoTuneValue == LFO.LFO_OFF)
            freq = noteFreq * (100 + tune) / 100;
        else
            freq = noteFreq * (int)(100.0 + lfoTuneValue * 10.0) / 100;
        
        period = rate / freq;
        
        oscFlag = true;
    }
    
    public void setWave(int w)
    {
        wave = w;
        oscFlag = true;
    }
    
    public void setPW(int p)
    {
        pwValue = p;
        
        if (pwValue < 10)
            pwValue = 10;
        
        if (pwValue > 90)
            pwValue = 90;
        
        //Only update the actual Pulsewidth if the LFO isn't modifying it
        if (lfoPWValue == LFO.LFO_OFF)
        {
            pw = pwValue;
            
            if (wave == PULSE)
                oscFlag = true;
        }
    }
    
    public void setTune(int t)
    {
        tune = t;
        
        if (tune < -10)
            tune = -10;
        
        if (tune > 10)
            tune = 10;
        
        //Only set the actual frequency if it's not being modified by the LFO
        if (lfoTuneValue == LFO.LFO_OFF)
            setFreq();
    }
    
    public void updatePWLFO(double value)
    {
        lfoPWValue = value;
        
        //Set the pulsewidth
        if (lfoPWValue == LFO.LFO_OFF)
        {
            pw = pwValue;  //Restore the user-set pulsewidth
            
            if (wave == PULSE)
                oscFlag = true;
        }
        else
        {
            //Set the LFO defined pulsewidth
            if (wave == PULSE)
            {
                pw = (int)(40.0 * lfoPWValue) + 50;
                oscFlag = true;
            }
        }
    }
    
    public void updateTuneLFO(double value)
    {
        lfoTuneValue = value;
        
        //This will automagically:
        //    a) set the value according to the LFO
        //    b) restore the user value for tuning
        setFreq();
    }
        
    //Output the audio data. Samples is the number of audio samples to output.
    public void readAudio(double[] out, int samples)
    {       
        for (int i = 0 ; i < samples ; i++)
        {
            if (mark >= period)
            {
                mark = 0;

                if (oscFlag)
                    calcOsc();
            }
            
            out[i] = auiData[mark];
            
            mark++;
        }
    }
    
    public void panic()
    {
        //Just in case the waveform somehow got munged, clear the buffer.
        for (int i = 0 ; i < auiData.length ; i++)
            auiData[i] = 0;
    }
    
    /* This family of functions is used to calculate a single period (i.e. *
     * one pulse) of audio data.                                           */
    private void calcOsc()
    {
        switch(wave)
        {
            case SINE:
                calcSine();
                break;
            case PULSE:
                calcPulse();
                break;
            case TRIANGLE:
                calcTriangle();
                break;
            default:
                calcSawtooth();
                break;
        }
        
        oscFlag = false;
    }
    
    private void calcSine()
    {
        for (int i = 0 ; i < period ; i++)
            auiData[i] = Math.sin(2 * Math.PI * i / period);
    }
    
    private void calcPulse()
    {
        for (int i = 0 ; i < period * pw / 100 ; i++)
            auiData[i] = 1;
        
        for (int i = period * pw / 100 ; i < period ; i++)
            auiData[i] = -1;
    }
    
    private void calcTriangle()
    {
        int i;
        
        for (i = 0 ; i < period / 4 ; i++)
            auiData[i] = i * 4.0 / (double)period;
        
        i = 0;
        
        for (i = period / 4 ; i < period / 2 ; i++)
            auiData[i] = (period / 4 - i) * 4.0 / (double)period + 1;
        
        i = 0;
        
        for (i = period / 2 ; i < period ; i++)
            auiData[i] = -auiData[i - period / 2];
        
        i = 0;
            
    }
    
    private void calcSawtooth()
    {
        for (int i = 0 ; i < period ; i++)
            auiData[i] = i * 2.0 / (double)period - 1;
    }
}
