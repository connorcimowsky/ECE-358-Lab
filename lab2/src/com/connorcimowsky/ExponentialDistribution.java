package com.connorcimowsky;

public class ExponentialDistribution {
    public static double randomVariable(double lambda) {
        double uniformRandomVariable = Math.random();
        return Math.log(1 - uniformRandomVariable) / (-lambda);
    }
}
