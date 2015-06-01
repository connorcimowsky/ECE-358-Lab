import java.util.*;

public class Simulation {
    private final LinkedList<Long> sojournTimes = new LinkedList<Long>();
    private final LinkedList<Integer> queueSizes = new LinkedList<Integer>();
    private final LinkedList<Packet> queue = new LinkedList<Packet>();
    private final long packetsPerSecond;
    private final long serviceTime;

    private long totalTicks = 0;
    private long nextTick = 0;
    private long idleTime = 0;

    public Simulation(long packetsPerSecond, double packetLength, double transmissionRate) {
        System.out.println("Lambda,AvgQueueSize,AvgSojourn,Pidle,IdleTime,Ticks");

        this.packetsPerSecond = packetsPerSecond;
        this.serviceTime = (long)((packetLength / transmissionRate) * 1000000.0);
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
            queue.add(new Packet(tick));

            double randomVariable = ExponentialDistribution.randomVariable(this.packetsPerSecond);
            this.nextTick = tick + (long)(randomVariable * 1000000.0);

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
        System.out.println(this.packetsPerSecond + "," + this.averageQueueSize() + "," + this.averageSojournTime() + "," + this.percentIdle() + "," + this.idleTime + "," + this.totalTicks);
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
        return ((double)this.idleTime / (double)this.totalTicks);
    }
}
