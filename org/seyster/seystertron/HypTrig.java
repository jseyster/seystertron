//HypTrig.java
//Would it have killed Sun to implement the hyperbolic trig functions in
//java.lang.Math?

package org.seyster.seystertron;

public class HypTrig
{
    public static double sinh(double x)
    {
        return (Math.exp(x) - Math.exp(-x)) / 2;
    }
    
    public static double asinh(double x)
    {
        return Math.log(x + Math.sqrt(x * x + 1));
    }
    
    public static double cosh(double x)
    {
        return (Math.exp(x) + Math.exp(-x)) / 2;
    }
    
    public static double acosh(double x)
    {
        return Math.log(x + Math.sqrt(x * x - 1));
    }
}
