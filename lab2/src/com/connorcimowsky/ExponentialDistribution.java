package com.connorcimowsky;

public class ExponentialDistribution {
    public static long randomVariable(double lambda) {
        double uniformRandomVariable = Math.random();
        return (long)((Math.log(1 - uniformRandomVariable) / (-lambda)) * 1000000.0);
    }

    public static long backoffRandom(double backoffCounter) {
        return (long)((Math.random() * (Math.pow(2.0, backoffCounter) - 1.0)) * 512.0);
    }
}
