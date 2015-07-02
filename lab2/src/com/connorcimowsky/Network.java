package com.connorcimowsky;

public class Network {
    public enum State {
        IDLE,
        BUSY,
        COLLISION
    }

    private int busyCounter = 0;

    public State getNetworkState() {
        switch (busyCounter) {
            case 0:
                return State.IDLE;
            case 1:
                return State.BUSY;
            default:
                return State.COLLISION;
        }
    }

    public void incrementBusyCounter() {
        busyCounter += 1;
    }

    public void decrementBusyCounter() {
        busyCounter -= 1;
    }
}
