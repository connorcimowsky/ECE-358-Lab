package com.connorcimowsky;

import java.util.ArrayList;
import java.util.List;

public class SimulationPPersistent implements SimulationInterface {
    private List<NodePPersistent> nodes;
    private Network network;
    private int packetLength;
    private long ticks;
    private double P;
    private int N;
    private long lambda;
    private long networkSpeed;

    public SimulationPPersistent() {
        nodes = null;
        network = new Network();
    }

    @Override
    public void startSimulation(long ticks, int N, long lambda, long networkSpeed, int packetLength, double P) {
        this.N = N;
        this.lambda = lambda;
        this.networkSpeed = networkSpeed;
        this.nodes = new ArrayList<NodePPersistent>(N);
        this.packetLength = packetLength * 8;
        this.ticks = ticks;
        this.P = P;

        for (int i = 0; i < N; i++) {
            this.nodes.add(new NodePPersistent(Simulation.propagationDelay(i), this.network, lambda, this.packetLength, P));
        }

        for (long t = 0; t < ticks; t++) {
            for (NodePPersistent node : this.nodes) {
                node.update();
            }

            network.flush();
        }
    }

    @Override
    public void computePerformance() {
        int totalSent = 0;
        long delayTime = 0;
        for (NodePPersistent n : nodes) {
            totalSent += n.getCompletedRequests();
            delayTime += n.getDelayTime();
        }

        double throughput = (double)totalSent / (double)ticks;
        double averageDelay = (double)delayTime / (double)totalSent;

        System.out.println("Total sent: " + totalSent);
        System.out.println("Total ticks: " + ticks);
        System.out.println("Throughput: " + throughput);
        System.out.println("Average Delay: " + averageDelay);
    }

    public static long propagationDelay(int index) {
        double distance = (double)index * 10.0;
        double delaySeconds = distance / 2E8;
        return (long)(delaySeconds * 1E6);
    }
}
