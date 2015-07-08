package com.connorcimowsky;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private List<Node> nodes;
    private Network network;
    private int packetLength;
    private long ticks;

    public Simulation() {
        nodes = null;
        network = new Network();
    }

    public void startSimulation(long ticks, int N, long lambda, long networkSpeed, int packetLength) {
        this.packetLength = packetLength * 8;
        this.ticks = ticks;
        this.nodes = new ArrayList<Node>(N);

        for (int i = 0; i < N; i++) {
            this.nodes.add(new Node(Simulation.propagationDelay(i), this.network, lambda, this.packetLength));
        }

        for (long t = 0; t < ticks; t++) {
            for (Node node : this.nodes) {
                node.update();
            }

            network.flush();
        }
    }

    public void computePerformance() {
        int totalSent = 0;
        for (Node n : nodes) {
            totalSent += n.getCompletedRequests();
        }

        double throughput = (double)totalSent / (double)ticks;

        System.out.println("Total sent: " + totalSent);
        System.out.println("Total ticks: " + ticks);
        System.out.println("Throughput: " + throughput);
    }

    public static long propagationDelay(int index) {
        double distance = (double)index * 10.0;
        double delaySeconds = distance / 2E8;
        return (long)(delaySeconds * 1E6);
    }
}
