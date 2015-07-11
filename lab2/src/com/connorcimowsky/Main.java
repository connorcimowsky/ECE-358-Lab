package com.connorcimowsky;

import java.util.Scanner;

public class Main {

    private static final int NUM_TICKS = 100000000;
    private static final int NETWORK_SPEED = 1;
    private static final int PACKET_LENGTH = 1500;

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        System.out.print("Level of persistence: ");

        double P = scanner.nextDouble();

        System.out.println("");

        long W = 0;
        int L = 0;
        int N = 0;
        long A = 0;

        System.out.println("Enter a value for N: ");
        N = scanner.nextInt();

        System.out.println("Enter a value for A: ");
        A = scanner.nextLong();

        System.out.println("Enter a value for W: ");
        W = scanner.nextLong();

        System.out.println("Enter a value for L: ");
        L = scanner.nextInt();

        System.out.println("");

        SimulationInterface simulation = null;

        if (P == 0.0) {
            simulation = new Simulation();
            System.out.println("Starting non-persistent simulation...");
        } else if (P == 1.0) {
            simulation = new Simulation();
            System.out.println("Starting 1-persistent simulation...");
        } else {
            simulation = new SimulationPPersistent();
            System.out.println("Starting " + P + "-persistent simulation...");
        }

        System.out.println("");

        simulation.startSimulation(NUM_TICKS, N, A, W, L, P);
        simulation.computePerformance();
    }
}
