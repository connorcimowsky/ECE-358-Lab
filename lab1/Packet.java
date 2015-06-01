public class Packet {
    private final long arrivalTick;
    private boolean servicing = false;
    private long departureTick = 0;

    public Packet(long arrivalTick) {
        this.arrivalTick = arrivalTick;
    }

    public long arrivalTick() {
        return this.arrivalTick;
    }

    public long departureTick() {
        return this.departureTick;
    }

    public boolean isServicing() {
        return this.servicing;
    }

    public void service(long tick) {
        this.servicing = true;
        this.departureTick = tick;
    }
}
