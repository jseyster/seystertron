//SynthComponent.java
//Defines an interface for all synthesizer components to use

package org.seyster.seystertron;

public interface SynthComponent
{    
    public void readAudio(double[] out, int samples);
    public void panic();
}
