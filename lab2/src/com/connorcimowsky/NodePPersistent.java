package com.connorcimowsky;

public class NodePPersistent {
    private enum State {
        IDLE,
        SENSING,
        SLOTWAIT,
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
    private long completedRequests;
    private long delayTime;
    private double P;

    public NodePPersistent(long propagationDelay, Network network, long lambda, int packetLength, double P) {
        this.propagationDelay = propagationDelay;
        this.time = ExponentialDistribution.randomVariable(lambda);
        this.currentState = State.IDLE;
        this.network = network;
        this.lambda = lambda;
        this.packetLength = packetLength;
        this.backoffCounter = 0;
        this.completedRequests = 0;
        this.delayTime = 0;
        this.P = P;
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
            case SLOTWAIT:
                this.slotWait();
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

    public long getCompletedRequests() {
        return this.completedRequests;
    }

    public long getDelayTime() {
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

        if (Math.random() <= this.P) {
            this.currentState = State.TRANSMITTING;
            this.network.addTraffic();
            this.time = this.propagationDelay + this.packetLength;
        } else {
            this.currentState = State.SLOTWAIT;
            this.resetSenseTime();
        }
    }

    private void slotWait() {
        if (!network.getNetworkState().equals(Network.State.IDLE)) {
            collision();
            return;
        }

        if (this.time != 0) {
            return;
        }

        if (Math.random() <= this.P) {
            this.currentState = State.TRANSMITTING;
            this.network.addTraffic();
            this.time = this.propagationDelay + this.packetLength;
        } else {
            this.resetSenseTime();
        }
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

    private void collision() {
        this.currentState = State.BACKOFF;
        this.network.removeTraffic();

        if (this.backoffCounter < 10) {
            this.backoffCounter += 1;
        }

        this.time = ExponentialDistribution.backoffRandom(this.backoffCounter);
    }

    private void jamming() {
        if (this.time > 0) {
            return;
        }

        this.collision();
    }

    private void backoff() {
        if (time == 0) {
            this.currentState = State.SENSING;
            resetSenseTime();
        }
    }

    private void resetSenseTime() {
        this.time = SENSING_TIME;

        if (this.P == 0.0) {
            this.time += ExponentialDistribution.backoffRandom(this.backoffCounter);
        }
    }
}
