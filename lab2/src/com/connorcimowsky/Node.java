package com.connorcimowsky;

public class Node {
    private enum State {
        IDLE,
        SENSING,
        TRANSMITTING,
        JAMMING,
        BACKOFF
    }

    private long propagationDelay;
    private long time;
    private State currentState;
    private Network network;
    private long lambda;
    private int packetLength;

    public Node(long propagationDelay, Network network, long lambda, int packetLength) {
        this.propagationDelay = propagationDelay;
        this.time = ExponentialDistribution.randomVariable(lambda);
        this.currentState = State.IDLE;
        this.network = network;
        this.lambda = lambda;
        this.packetLength = packetLength;
    }

    public void update() {
        this.time -= 1;

        switch(currentState) {
            case IDLE:
                this.idle();
                break;
            case SENSING:
                this.sensing();
                break;
            case TRANSMITTING:
                this.transmitting();
                break;
            case JAMMING:
                this.jamming();
                break;
            case BACKOFF:
                this.backoff();
                break;
        }
    }

    private void idle() {
        if (this.time != 0) {
            return;
        }

        this.currentState = State.SENSING;
        this.resetSenseTime();
    }

    private void sensing() {
        if (network.getNetworkState() != Network.State.IDLE) {
            this.resetSenseTime();
            return;
        }

        if (this.time != 0) {
            return;
        }

        this.currentState = State.TRANSMITTING;
        this.network.addTraffic();
        this.time = this.propagationDelay + this.packetLength;
    }

    private void transmitting() {
        if (this.network.getNetworkState() == Network.State.COLLISION) {
            this.currentState = State.JAMMING;
            this.time = 48;

            return;
        }

        if (time == 0) {
            this.currentState = State.IDLE;
            this.time = ExponentialDistribution.randomVariable(this.lambda);
        }
    }

    private void jamming() {
        if (this.time == 0) {
            this.currentState = State.BACKOFF;
//            this.time =
        }
    }

    private void backoff() {

    }

    private void resetSenseTime() {
        this.time = 96;
    }
}
