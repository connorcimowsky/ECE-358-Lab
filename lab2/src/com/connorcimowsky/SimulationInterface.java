package com.connorcimowsky;

/**
 * Created by Connor on 7/11/15.
 */
public interface SimulationInterface {
    void startSimulation(long ticks, int N, long lambda, long networkSpeed, int packetLength, double P);

    void computePerformance();
}
