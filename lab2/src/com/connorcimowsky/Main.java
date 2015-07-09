package com.connorcimowsky;

import java.util.Scanner;

public class Main {

    private static final int NUM_TICKS = 100000000;
    private static final int NETWORK_SPEED = 1;
    private static final int PACKET_LENGTH = 1500;

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        System.out.print("1-persistent? [y/n] ");

        boolean onePersistent = (scanner.nextLine().equals("y"));

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
        System.out.println("Starting simulation...");
        System.out.println("");

        if (onePersistent) {
            Simulation simulation = new Simulation();
            simulation.startSimulation(NUM_TICKS, N, A, W, L);
            simulation.computePerformance();
        }

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
