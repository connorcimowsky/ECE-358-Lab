package com.connorcimowsky;

public class Network {
    public enum State {
        IDLE,
        BUSY,
        COLLISION
    }

    private int busyCounter = 0;
    private int dirtyCounter = 0;

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

    public void addTraffic() {
        dirtyCounter += 1;
    }

    public void removeTraffic() {
        dirtyCounter -= 1;
    }

    public void flush() {
        busyCounter = dirtyCounter;
        dirtyCounter = 0;
    }
}
