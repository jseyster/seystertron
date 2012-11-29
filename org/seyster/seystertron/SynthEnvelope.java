//SynthEnvelope.java
//An ADSR envelope filter

package org.seyster.seystertron;

public class SynthEnvelope implements SynthComponent
{
    public static final double MAX_ATTACK = 0.5;  //Constants, in seconds
    public static final double MAX_DECAY = 0.5;
    public static final double MIN_RELEASE = 0.1;
    public static final double MAX_RELEASE = 5.0;
    
    private static final int ATTACK = 0;
    private static final int DECAY = 1;
    private static final int SUSTAIN = 2;
    private static final int RELEASE = 3;
    private static final int OFF = 4;
    
    private SynthComponent source;
    
    private int state;  //What state are we in (A, D, S, R)?
    private int time;  //How long have we been in that state, in samples?
    private int updateInterval; //How often to we update the gain, in samples?
    private int updateTime;  //How long since last update, in samples?
    
    private int attack; //Times for A, D, R
    private int decay;
    private double sustain;  //Sustain is a gain, not a time
    private int release;  //Release is a time constant
    
    private double gain;
    private double initGain;
    
    private int rate;  //The sampling rate
    
    public SynthEnvelope(SynthComponent sc, int r)
    {
        source = sc;
        
        rate = r;
        updateInterval = rate / 100;  //1/100 second update interval
        state = OFF;
        
        gain = 0;
        initGain = 0;
        
        attack = rate / 10;
        decay = rate / 10;
        sustain = .6;
        release = rate * 3;
    }
    
    public void triggerAttack()
    {
        state = ATTACK;
        
        initGain = gain;
        
        time = 0;
        updateTime = 0;
    }
    
    public void triggerRelease()
    {
        if (release == 0)
            state = SUSTAIN;  //Release is set to max
        else
            state = RELEASE;
        
        time = 0;
        updateTime = 0;
        initGain = gain;
    }
    
    public void setADSR(double a, double d, double s, double r)
    {
        //Set attack
        if (a < 0)
            a = 0;
        
        if (a > MAX_ATTACK)
            a = MAX_ATTACK;
        
        attack = (int)(a * rate);
        
        //Set decay
        if (d < 0)
            d = 0;
        
        if (d > MAX_DECAY)
            d = MAX_DECAY;
        
        decay = (int)(d * rate);
        
        //Set sustain
        if (s < 0)
            s = 0;
        
        if (s > 1.0)
            s = 1.0;
        
        sustain = s;
        
        //Set release
        if (r < MIN_RELEASE)
            r = MIN_RELEASE;
        
        if (r >= MAX_RELEASE) //Don't release
            r = 0;
        
        release = (int)(r * rate);
    }
    
    private void updateGain()
    {
        //Update the state
        if (state == ATTACK && time >= attack)
        {
            state = DECAY;
            time = 0;
        }
        else if (state == DECAY && time >= decay)
        {
            state = SUSTAIN;
            time = 0;
        }
        else if (state == RELEASE && time >= 3 * release) //95% of the change
        {
            state = OFF;
            time = 0;
        }
        
        //Update the gain
        switch(state)
        {
            case ATTACK:
                gain = (1-initGain) / (double)attack * (double)time + initGain;
                break;
            case DECAY:
                gain = 1 - (1 - sustain) / (double)decay * time;
                break;
            case SUSTAIN:
                gain = sustain;
                time = 0; //Time isn't relevant, and we don't want an overflow
                break;
            case RELEASE:
                //This is an exponential curve with time constant release!
                gain = initGain * Math.exp(- (double)time / (double)release);
                break;
            case OFF:
                gain = 0;
                break;
        }
    }
    
    public void readAudio(double[] out, int samples)
    {
        source.readAudio(out, samples);
        
        for (int i = 0 ; i < samples ; i++)
        {
            if (updateTime > updateInterval)
            {
                updateTime = 0;
                updateGain();
            }
            
            out[i] *= gain;
            
            time++;
            updateTime++;
        }
    }
    
    public void panic()
    {
        state = OFF;
        time = 0;
        gain = 0;
        
        source.panic();
    }
}
