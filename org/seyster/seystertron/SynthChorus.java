//SynthChorus.java
//A simple chorus effect

package org.seyster.seystertron;

public class SynthChorus implements SynthComponent
{
    private static final double MAX_DELAY = 0.03;  //Max delay in seconds
    
    SynthComponent source;
    
    private int rate;
    
    private int time;  //Time in samples since beginning of cycle
    private double freq;  //Frequency of cycle in Hz
    private int period;
    
    private double level;  //Volume of the dry signal
    private double chorusLevel;  //Volume of the chorus voice
    private double depth;  //How deeply the delay time changes
                           //(i.e. how much the pitch will vary)
    private boolean active;  //Whether or not the effect is on
    
    private int interval;  //How frequently to update delay time
    private int updateTime;  //Time passed since last delay update
    
    private double delay;  //Delay time, in samples
    
    private int pos;  //The position in the audio buffer
    private double[] auiData;
    
    public SynthChorus(SynthComponent sc, int r)
    {
        source = sc;
        rate = r;
        
        
        time = 0;
        setFreq(1.0);
        
        level = 0.6;
        chorusLevel = 0.4;
        depth = .003;
        active = false;
        
        interval = rate / 100;  //Update every 1/100 seconds
        updateTime = 0;
        
        delay = rate * .025d; //25ms delay
        
        pos = 0;
        auiData = new double[(int)(MAX_DELAY * rate)];
        
        for (int i = 0 ; i < auiData.length ; i++)
            auiData[i] = 0.0;
    }
    
    public void setFreq(double f)
    {
        if (f < 0.5)
            f = 0.5;
        
        if (f > 5.0)
            f = 5.0;
        
        freq = f;
        period = (int)(rate / freq);
    }
    
    //The level of the chorus.  Takes a value from 0 to 1.0
    public void setLevel(double l)
    {
        chorusLevel = l / 2.0;
        
        if (chorusLevel < 0.0)
            chorusLevel = 0.0;
        
        if (chorusLevel > 0.5)
            chorusLevel = 0.5;
        
        //Set the dry level so that the two levels added equal 1.0
        level = 1.0 - chorusLevel;
    }
    
    public void setDepth(double d)
    {
        if (d < 0.001)
            d = 0.001;
        
        if (d > 0.0045)
            d = 0.0045;
        
        depth = d;
    }
    
    public void setActive(boolean a)
    {
        active = a;
    }
    
    private void updateDelay()
    {
        //Don't allow the time variable to grow greater than one period
        time %= period;
        
        delay = ( .025 + depth * Math.sin(2 * Math.PI * time / period) ) * rate;
    }
    
    //Interpolate between samples to read information from the delay line
    private double getDelayData(double t)
    {
        double value1 = auiData[(int)t];
        double value2;
        
        if ((int)t + 1 >= auiData.length)
            value2 = auiData[0];
        else
            value2 = auiData[(int)t + 1] ;
        
        return (value2 - value1) * (t - (int)t) + value2;
    }
    
    public void readAudio(double[] out, int samples)
    {
        source.readAudio(out, samples);
        
        for (int i = 0 ; i < samples ; i++)
        {
            auiData[pos] = out[i];
            
            pos++;            
            if (pos >= auiData.length)
                pos = 0;
            
            time++;
            updateTime++;            
            if (updateTime >= interval)
            {
                updateDelay();
                updateTime = 0;
            }
            
            if(active)
            {
                //Calculate the chorus output
                double chorusPos = (double)pos - delay;
                if (chorusPos < 0)
                    chorusPos += auiData.length;
            
                out[i] = level * out[i] + chorusLevel * getDelayData(chorusPos);
            }
        }
    }
    
    public void panic()
    {
        //Clear the buffer!
        for (int i = 0 ; i < auiData.length ; i++)
            auiData[i] = 0;
        
        source.panic();
    }
}
