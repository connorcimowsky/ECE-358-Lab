package com.connorcimowsky;

public class Node {
    private enum State {
        IDLE,
        SENSING,
        TRANSMITTING,
        JAMMING,
        BACKOFF
    }

    private long time;
    private State currentState;
    private Network network;

    public Node(Network network) {
        this.time = 0;
        this.currentState = State.IDLE;
        this.network = network;
    }

    public void update() {
        switch(currentState) {
            case IDLE:
                break;
            case SENSING:
                break;
            case TRANSMITTING:
                break;
            case JAMMING:
                break;
            case BACKOFF:
                break;
        }
    }
}
