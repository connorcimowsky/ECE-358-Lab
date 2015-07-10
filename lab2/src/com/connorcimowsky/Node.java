package com.connorcimowsky;

public class Node {
    private enum State {
        IDLE,
        SENSING,
        SLOTWAIT,
        RANDOMWAIT,
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
    private double probability;
    private int backoffCounter;
    private int completedRequests;
    private int delayTime;

    public Node(long propagationDelay, Network network, long lambda, int packetLength, double probability) {
        this.propagationDelay = propagationDelay;
        this.time = ExponentialDistribution.randomVariable(lambda);
        this.currentState = State.IDLE;
        this.network = network;
        this.lambda = lambda;
        this.packetLength = packetLength;
        this.probability = probability;
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
            case SLOTWAIT:
                this.slotwait();
                break;
            case RANDOMWAIT:
                this.randomwait();
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
            // TODO better check or flag to indicate non-persistent
            if (probability == 100.0) {
                this.resetSenseTime();
            } else {
                this.currentState = State.RANDOMWAIT;
                // TODO binary exponential backoff, is this time correct?
                this.time = ExponentialDistribution.backoffRandom(this.backoffCounter);
            }

            return;
        }

        if (this.time != 0) {
            return;
        }

        if (Math.random() <= this.probability) {
            this.currentState = State.TRANSMITTING;
            this.network.addTraffic();
            this.time = this.propagationDelay + this.packetLength;
        } else {
            this.currentState = State.SLOTWAIT;
            // TODO Figure out how long to wait
            this.time = SENSING_TIME;
        }

    }

    private void randomwait() {
        if (this.time > 0) {
            return;
        }

        this.currentState = State.SENSING;
        // TODO Add this time?
        resetSenseTime();
    }

    private void slotwait() {
        if (this.time > 0) {
            return;
        }

        // TODO confirm we only want to check once and not poll
        if (!network.getNetworkState().equals(Network.State.IDLE)) {
            // TODO new/old/incr backoff counter? does it go back to sensing?
            // assume i doesnt reset and we go straight to backoff state -> sensing
            this.currentState = State.BACKOFF;
            this.time = ExponentialDistribution.backoffRandom(this.backoffCounter);
        } else {
            // TODO assume it does it on the same tick?
            this.sensing();
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
