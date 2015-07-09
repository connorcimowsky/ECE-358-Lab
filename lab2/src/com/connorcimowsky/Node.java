package com.connorcimowsky;

public class Node {
    private enum State {
        IDLE,
        SENSING,
        TRANSMITTING,
        JAMMING,
        BACKOFF
    }

    private static final int JAMMING_TIME = 48;
    private static final int SENSING_TIME = 96;

    private long propagationDelay;
    private long time;
    private State currentState;
    private Network network;
    private long lambda;
    private int packetLength;
    private int backoffCounter;
    private int completedRequests;
    private int delayTime;

    public Node(long propagationDelay, Network network, long lambda, int packetLength) {
        this.propagationDelay = propagationDelay;
        this.time = ExponentialDistribution.randomVariable(lambda);
        this.currentState = State.IDLE;
        this.network = network;
        this.lambda = lambda;
        this.packetLength = packetLength;
        this.backoffCounter = 0;
        this.completedRequests = 0;
        this.delayTime = 0;
    }

    public void update() {
        this.time -= 1;

        if (currentState != State.IDLE) {
            delayTime += 1;
        }

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

    public int getCompletedRequests() {
        return this.completedRequests;
    }

    public int getDelayTime() {
        return this.delayTime;
    }

    private void idle() {
        if (this.time != 0) {
            return;
        }

        this.currentState = State.SENSING;
        this.resetSenseTime();
    }

    private void sensing() {
        if (!network.getNetworkState().equals(Network.State.IDLE)) {
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
            this.time = JAMMING_TIME;

            return;
        }

        if (time == 0) {
            this.currentState = State.IDLE;
            this.network.removeTraffic();
            this.time = ExponentialDistribution.randomVariable(this.lambda);
            this.completedRequests += 1;
            this.backoffCounter = 0;
        }
    }

    private void jamming() {
        if (this.time > 0) {
            return;
        }

        this.currentState = State.BACKOFF;
        this.network.removeTraffic();

        if (this.backoffCounter < 10) {
            this.backoffCounter += 1;
        }

        this.time = ExponentialDistribution.backoffRandom(this.backoffCounter);
    }

    private void backoff() {
        if (time == 0) {
            this.currentState = State.SENSING;
            resetSenseTime();
        }
    }

    private void resetSenseTime() {
        this.time = SENSING_TIME;
    }
}
