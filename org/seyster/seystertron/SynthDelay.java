//SynthDelay.java
//A digital delay unit, with delay up to 2 seconds

package org.seyster.seystertron;

public class SynthDelay implements SynthComponent
{
    public static final int MAX_DELAY = 2; //Max delay, in seconds
    
    private SynthComponent source;
    
    private double[] auiData;
    
    private int rate;  //Sampling rate in Hz
    private int pos;  //Current position in the audio buffer
    private int delay;  //Delay, in samples
    
    private double level;  //The level of the delayed signal
    private double feedback;  //The level of the feedback
    
    private boolean active;  //Whether the delay is "on"

    public SynthDelay(SynthComponent sc, int r)
    {
        source = sc;
        rate = r;
        
        pos = 0;
        setDelay(0.3);
        level = 0.5;
        feedback = 0.2;
        active = false;
        
        auiData = new double[MAX_DELAY * rate];
        for (int i = 0 ; i < auiData.length ; i++)
            auiData[i] = 0.0;
    }
    
    public void setActive(boolean a)
    {
        active = a;
    }
    
    public void setDelay(double d)
    {
        if (d < 0.0)
            d = 0.0;
        
        if (d > MAX_DELAY)
            d = MAX_DELAY;
        
        delay = (int)((double)rate * d);
    }
    
    //Set the level of the delay as a fraction of the dry level
    public void setLevel(double l)
    {
        level = l;
        
        //Make sure the 
        if (level < 0.0)
            level = 0.0;
        
        if (level > 0.5)
            level = 0.5;
    }
    
    public void setFeedback(double f)
    {
        feedback = f;
        
        if (feedback < 0.0)
            feedback = 0.0;
        
        if (feedback > 1.0)
            feedback = 1.0;
       
    }
    
    public void readAudio(double[] out, int samples)
    {
        source.readAudio(out, samples);
        
        for (int i = 0 ; i < samples ; i++)
        {
            auiData[pos] = out[i];  //Store current sample to be played later
            
            //We always want to plack back the sample that is just about to
            //be overwritten, so we increment the pos variable
            pos++;
            
            if (pos >= delay)
                pos = 0;
            
            if (active)
            {                
                out[i] = (1.0 - level) * out[i] + level * auiData[pos];

                //Finally, do the feedback
                //We add the output back into the buffer
                if (pos > 0)
                    auiData[pos - 1] = auiData[pos - 1] + feedback * out[i];
                else
                    auiData[delay - 1] = auiData[delay - 1] + feedback * out[i];
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
