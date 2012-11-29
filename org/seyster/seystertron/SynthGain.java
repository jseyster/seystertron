//SynthGain
//The final component in the chain of audio effects, this does gain and fixes
//clipping.

package org.seyster.seystertron;

public class SynthGain implements SynthComponent
{
    private SynthComponent source;
    
    private double gain;
    
    public SynthGain(SynthComponent sc)
    {
        source = sc;
        
        gain = 1.0;
    }
    
    public void setGain(double g)
    {
        if (g > 3.0)
            g = 3.0;
        
        if (g < 0.0)
            g = 0.0;
        
        gain = g;
    }
    
    public void readAudio(double[] out, int samples)
    {
        source.readAudio(out, samples);
        

        for (int i = 0 ; i < samples ; i++)
            if (out[i] > 0)
                out[i] = Math.min(1.0d, out[i] * gain);
            else
                out[i] = Math.max(-1.0d, out[i] * gain);

    }
    
    public void panic()
    {
        source.panic();
    }
    
}
