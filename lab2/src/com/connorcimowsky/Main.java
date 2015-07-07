package com.connorcimowsky;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        simulation.startSimulation(100000000, 20, 7, 1, 1500);
        simulation.computePerformance();
    }
}
