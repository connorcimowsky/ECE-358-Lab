package com.connorcimowsky;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private List<Node> nodes;
    private Network network;

    public Simulation() {
        nodes = null;
        network = new Network();
    }

    public void startSimulation(long ticks, int N, long A, long W, long L) {
        this.nodes = new ArrayList<Node>(N);

        for (int i = 0; i < N; i++) {
            this.nodes.add(new Node(this.network));
        }

        for (long t = 0; t < ticks; t++) {
            for (Node node : this.nodes) {
                node.update();
            }
        }
    }

    public void computePerformance() {
    }
}
