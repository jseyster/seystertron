//SynthDCFilter.java
//Attempts to remove DC component from the signal

package org.seyster.seystertron;

public class SynthDCFilter implements SynthComponent
{
    private static final int BUFFER_SIZE = 1000;
    
    private SynthComponent source;
    
    private int mark;  //Where we are in the data
    private double[] auiData;
    private double sum;  //A running sum, used for the average
    
    public SynthDCFilter(SynthComponent sc)
    {
        source = sc;
        
        auiData = new double[BUFFER_SIZE];
        mark = 0;
        sum = 0;
        
        //Initialize auiData array
        for (int i = 0 ; i < BUFFER_SIZE ; i++)
            auiData[i] = 0;
    }
    
    public void readAudio(double[] out, int samples)
    {
        double offset;  //The DC offset
        source.readAudio(out, samples);
        
        //Calculate the running sum
        for (int i = 0 ; i < samples ; i++)
        {
            sum -= auiData[mark];
            sum += out[i];
            
            auiData[mark] = out[i];
            
            mark++;
            if (mark >= BUFFER_SIZE)
                mark = 0;
        }
        
        //Use the sum to calculate an average, which should be the DC offset
        offset = sum / (double)BUFFER_SIZE;
        
        //Remove the offset
        for (int i = 0 ; i < samples ; i++)
            out[i] -= offset;
    }
    
    public void panic()
    {
        //Clear the buffer!
        for (int i = 0 ; i < auiData.length ; i++)
            auiData[i] = 0;
        
        sum = 0;
        
        source.panic();
    }
    
}
