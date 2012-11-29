//SynthMixer.java
//Mixes two audio sources

package org.seyster.seystertron;

public class SynthMixer implements SynthComponent
{
    public static final int ADD = 0;
    public static final int RING_MOD = 1;
    public static final int SOURCE1 = 2;
    public static final int SOURCE2 = 3;
    
    private SynthComponent source1;
    private SynthComponent source2;
    
    private int mixMode;
    private int mix;  //How loud should the sources be compared to each other?
    
    public SynthMixer(SynthComponent s1, SynthComponent s2)
    {
        source1 = s1;
        source2 = s2;
        
        mixMode = ADD;
        mix = 50;
    }
    
    public void setMixMode(int mode)
    {
        mixMode = mode;
    }
    
    /* The mix is a percentage.  At 0 percent, source1 is all the way up,    *
     * and source2 is all the way down.  At 100%, the opposite is true.  And *
     * at 50%, the sources are at the same level                             */
    public void setMix(int m)
    {
        mix = m;
        
        if (mix < 0)
            mix = 0;
        
        if (mix > 100)
            mix = 100;
    }
    
    //Mix the two sources and output the audio
    public void readAudio(double[] out, int samples)
    {
        double[] auiData1;
        double[] auiData2;
        
        double a, b;

        switch (mixMode)
        {
            case ADD:
                b = (double) mix / 100.0d;
                a = 1.0 - b;
                auiData1 = new double[samples];
                auiData2 = new double[samples];
                source1.readAudio(auiData1, samples);
                source2.readAudio(auiData2, samples);
                
                for (int i = 0 ; i < samples ; i++)
                    out[i] = a * auiData1[i] + b * auiData2[i];
                
                break;
                
            case RING_MOD:
                auiData1 = new double[samples];
                auiData2 = new double[samples];
                source1.readAudio(auiData1, samples);
                source2.readAudio(auiData2, samples);
                
                for (int i = 0 ; i < samples ; i++)
                    out[i] = auiData1[i] * auiData2[i];
                
                break;
                
            case SOURCE1:
                source1.readAudio(out, samples);
                break;
            default:
                source2.readAudio(out, samples);
                break;
        }
    }
    
    public void panic()
    {
        source1.panic();
        source2.panic();
    }
}
