package com.connorcimowsky;

public class ExponentialDistribution {
    public static long randomVariable(double lambda) {
        double uniformRandomVariable = Math.random();
        return (long)((Math.log(1 - uniformRandomVariable) / (-lambda)) * 1000000.0);
    }
}
