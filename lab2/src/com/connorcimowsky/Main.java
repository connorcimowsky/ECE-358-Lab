package com.connorcimowsky;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        simulation.startSimulation(600, 20, 5, 1, 1500);
        simulation.computePerformance();
    }
}
