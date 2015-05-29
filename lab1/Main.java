public class Main {
    public static void main(String[] args) {
        Simulation simulation = new Simulation(100, 2000.0, 1000000.0);
        simulation.startSimulation(10000000000l);
//        simulation.computePerformance();
    }
}
