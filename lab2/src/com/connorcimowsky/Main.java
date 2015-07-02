package com.connorcimowsky;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        simulation.startSimulation(1, 2, 3, 4, 5);
        simulation.computePerformance();
    }
}
