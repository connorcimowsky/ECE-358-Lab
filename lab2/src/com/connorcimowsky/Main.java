package com.connorcimowsky;

import java.util.Scanner;

public class Main {

    private static final int NUM_TICKS = 100000000;
    private static final int NETWORK_SPEED = 1;
    private static final int PACKET_LENGTH = 1500;

    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        simulation.startSimulation(NUM_TICKS, 20, 7, 1, 1500);
        simulation.computePerformance();
    }

    private static void questionOneAndThree(Simulation s) {
        for (int A = 5; A <= 7; A++) {
            System.out.println("Running for A = " + A);
            System.out.println();
            for (int n = 20; n <= 100; n+= 20) {
                System.out.println("Running for n = " + n);
                System.out.println();
                s.startSimulation(NUM_TICKS, n, A, NETWORK_SPEED, PACKET_LENGTH);
                s.computePerformance();
            }
        }
    }

    private static void questionTwoAndFour(Simulation s) {
        for (int n = 20; n <= 40; n += 10) {
            System.out.println("Running for n = " + n);
            System.out.println();
            for (int A = 4; A <= 20; A += 4) {
                System.out.println("Running for A = " + A);
                System.out.println();
                s.startSimulation(NUM_TICKS, n, A, NETWORK_SPEED, PACKET_LENGTH);
                s.computePerformance();
            }
        }
    }
}
