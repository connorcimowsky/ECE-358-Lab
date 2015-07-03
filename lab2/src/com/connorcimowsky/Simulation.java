package com.connorcimowsky;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private List<Node> nodes;
    private Network network;
    private double networkSpeed;
    private int packetLength;

    public Simulation() {
        nodes = null;
        network = new Network();
    }

    public void startSimulation(long ticks, int N, long lambda, long networkSpeed, int packetLength) {
        this.networkSpeed = networkSpeed;
        this.packetLength = packetLength * 8;

        this.nodes = new ArrayList<Node>(N);

        for (int i = 0; i < N; i++) {
            this.nodes.add(new Node(Simulation.propagationDelay(i), this.network, lambda, packetLength));
        }

        for (long t = 0; t < ticks; t++) {
            for (Node node : this.nodes) {
                node.update();
            }

            network.flush();
        }
    }

    public void computePerformance() {
    }

    public static long propagationDelay(int index) {
        double distance = (double)index * 10.0;
        double delaySeconds = distance / 2E8;
        return (long)(delaySeconds * 1E6);
    }
}
