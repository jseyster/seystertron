//SynthFilter.java
//A two-pole Chebyshev low-pass or high-pass filter.
//The "resonance" of the filter is a property known as pass-band ripple.

package org.seyster.seystertron;

public class SynthFilter implements SynthComponent
{
    private SynthComponent source;

    private boolean lowpass; //True if lowpass, false if highpass
    private double cutoffValue; //Cutoff as fraction of the sampling rate
    private double lfoValue; //Information from the LFO
    private double ripple; //Pass-band ripple
    private int rate; //Sampling rate
    
    //Used for the IIR
    private double[] prevOut;
    private double[] prevInput;
    
    //Recursion coefficients
    private double a0;
    private double a1;
    private double a2;
    private double b1;
    private double b2;
    
    public SynthFilter(SynthComponent sc, int r)
    {
        source = sc;
        rate = r;
        
        prevOut = new double[2];
        prevInput = new double[2];
        
        prevOut[0] = prevInput[0] = 0;
        prevOut[1] = prevInput[1] = 0;
        
        lowpass = true;;
        cutoffValue = 0.025;
        lfoValue = LFO.LFO_OFF;
        ripple = 0.5;
        
        calcCoefficients();
    }
    
    //True for a low-pass filter, false for a high-pass filter
    public void setLowpass(boolean lp)
    {
        lowpass = lp;
        
        calcCoefficients();
    }
    
    public void setCutoff(double freq)
    {
        cutoffValue = freq;
        
        if (cutoffValue < 0.001)
            cutoffValue = 0.001;
        
        if (cutoffValue > 0.499)
            cutoffValue = .499;
        
        calcCoefficients();
    }
    
    public void setRipple(double r)
    {
        ripple = r;
        
        if (ripple < 0.5)
            ripple = 0.5;
        
        if (ripple > 29)
            ripple = 29;
        
        calcCoefficients();
    }
    
    public void updateLFO(double value)
    {
        lfoValue = value;
        calcCoefficients();
    }
    
    //This is where the math magic is.  Don't worry; it hurts my head too.
    private void calcCoefficients()
    {
        double cutoff;

        //The two poles are represented in the s-plane as poleRead+-poleImag.
        double poleReal, poleImag;
        double v, k, epsilon;
        double m, t, d;
        double gain;
        double tA0, tA1, tA2, tB1, tB2; //Temporary variables
        
        if (lfoValue == LFO.LFO_OFF)
            cutoff = cutoffValue;
        else
            cutoff = (lfoValue + 1.0) * 0.124 + .001;
        
        //Begin with a Butterworth filter with a cutoff of 1 radian
        poleImag = Math.sin(Math.PI / 4);
        poleReal = -poleImag;  //This only works for a two pole filter
        
        //Flatten the circle to create a Chebyshev filter
        epsilon = Math.sqrt(Math.pow( (100.0 / (100.0 - ripple)), 2.0 ) - 1.0);
        v = HypTrig.asinh(1 / epsilon) / 2;
        k = HypTrig.cosh(0.5 * HypTrig.acosh(1 / epsilon));
        
        poleReal *= HypTrig.sinh(v) / k;
        poleImag *= HypTrig.cosh(v) / k;
        
        //Bilinear transform
        //This transforms the poles to the z-plane and gives us coefficients.
        m = poleReal * poleReal + poleImag * poleImag;
        t = 2 * Math.tan(0.5);
        d = 4 - 4 * poleReal * t + m * t * t;
        
        tA0 = t * t / d;
        tA1 = 2 * tA0;
        tA2 = tA0;
        tB1 = (8 - 2 * m * t * t) / d;
        tB2 = (-4 - 4 * poleReal * t - m * t * t) / d;
        
        //Change the cutoff frequency
        if (lowpass)
            k = Math.sin(0.5 - cutoff * Math.PI) /
                 Math.sin(0.5 + cutoff * Math.PI);
        else
            k = -Math.cos(cutoff * Math.PI + 0.5) /
                  Math.cos(cutoff * Math.PI - 0.5);

        d = 1 + tB1 * k - tB2 * k * k;
        
        a0 = (tA0 - tA1 * k + tA2 * k * k) / d;
        a1 = (-2 * tA0 * k + tA1 + tA1 * k * k -  2 * tA2 * k) / d;
        //a2 = (tA0 * k * k - tA1 * k + tA2) / d;
        a2 = a0; //The actual equation is above, but it winds up equal to a0.
        b1 = (2 * k + tB1 + tB1 * k * k - 2 * tB2 * k) / d;
        b2 = (-(k * k) - tB1 * k + tB2) / d;
        
        if (!lowpass)
        {
            a1 = -a1;
            b1 = -b1;
        }
        
        //Finally, we just need to normalize the gain
        if (lowpass)
            gain = (a0 + a1 + a2) / ( 1 - (b1 + b2) );
        else
            gain = (a0 - a1 + a2) / ( 1 - (-b1 + b2) );

        a0 /= gain;
        a1 /= gain;
        a2 /= gain;
    }
    
    public void readAudio(double[] out, int samples)
    {
        double[] input = new double[samples];
        
        source.readAudio(input, samples);
        
        out[0] = a0 * input[0] + a1 * prevInput[1] + a2 * prevInput[0];
        out[0] += b1 * prevOut[1] + b2 * prevOut[0];
        
        out[1] = a0 * input[1] + a1 * input[0] + a2 * prevInput[1];
        out[1] += b1 * out[0] + b2 * prevOut[1];
        
        for (int i = 2 ; i < samples ; i++)
        {
            out[i] = a0 * input[i] + a1 * input[i - 1] + a2 * input[i - 2];
            out[i] += b1 * out[i - 1] + b2 * out[i - 2];
        }
        
        prevOut[0] = out[samples - 2];
        prevOut[1] = out[samples - 1];
        prevInput[0] = input[samples - 2];
        prevInput[1] = input[samples - 1];
    }
    
    public void panic()
    {
        prevOut[0] = prevInput[0] = 0;
        prevOut[1] = prevInput[1] = 0;
        
        source.panic();
    }
}
