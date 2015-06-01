import java.util.*;

public class SimulationK {
    private final LinkedList<Long> sojournTimes = new LinkedList<Long>();
    private final LinkedList<Integer> queueSizes = new LinkedList<Integer>();
    private final LinkedList<Packet> queue = new LinkedList<Packet>();
    private final long packetsPerSecond;
    private final long serviceTime;
    private final int bufferSize;

    private long totalTicks = 0;
    private long nextTick = 0;
    private long idleTime = 0;
    private long packetCounter = 0;
    private long dropCounter = 0;

    public SimulationK(long packetsPerSecond, double packetLength, double transmissionRate, int bufferSize) {
        this.packetsPerSecond = packetsPerSecond;
        this.serviceTime = (long)((packetLength / transmissionRate) * 1000000.0);
        this.bufferSize = bufferSize;
    }

    public void startSimulation(long ticks) {
        this.totalTicks = ticks;

        for (long tick = 0; tick < ticks; tick++) {
            if (arrival(tick)) {
                queueSizes.add(this.queue.size());
            }

            if (departure(tick)) {
                queueSizes.add(this.queue.size());
            }
        }
    }

    public boolean arrival(long tick) {
        if (tick >= nextTick) {
            packetCounter += 1;

            double randomVariable = ExponentialDistribution.randomVariable(this.packetsPerSecond);
            this.nextTick = tick + (long)(randomVariable * 1000000.0);

            if (this.queue.size() <= bufferSize) {
                this.queue.add(new Packet(tick));
            } else {
                this.dropCounter += 1;
                return false;
            }

            return true;
        }

        return false;
    }

    public boolean departure(long tick) {
        if (queue.isEmpty()) {
            idleTime += 1;
            return false;
        }

        Packet packet = this.queue.getFirst();

        if (!packet.isServicing()) {
            packet.service(tick);
        }

        if (tick == packet.departureTick() + this.serviceTime) {
            // service time has elapsed
            this.queue.remove();

            long queueingDelay = packet.departureTick() - packet.arrivalTick();
            long sojournTime = queueingDelay + this.serviceTime;
            this.sojournTimes.add(sojournTime);

            return true;
        }

        return false;
    }

    public void computePerformance() {
        System.out.println("E[N]:\t" + this.averageQueueSize());
        System.out.println("E[T]:\t" + this.averageSojournTime() + " µs");
        System.out.println("P_IDLE:\t" + this.percentIdle() + "%");
        System.out.println("P_LOSS:\t" + this.percentLoss() + "%");
    }

    private double averageQueueSize() {
        long sum = 0;

        for (Integer queueSize : this.queueSizes) {
            sum += queueSize;
        }

        return (double)sum / (double)this.queueSizes.size();
    }

    private double averageSojournTime() {
        long sum = 0;

        for (Long sojournTime : this.sojournTimes) {
            sum += sojournTime;
        }

        return (double)sum / (double)this.sojournTimes.size();
    }

    private double percentIdle() {
        return ((double)this.idleTime / (double)this.totalTicks) * 100.0;
    }

    private double percentLoss() {
        return ((double)this.dropCounter / (double)this.packetCounter) * 100.0;
    }
}
