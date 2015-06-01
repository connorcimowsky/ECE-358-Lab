import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        System.out.print("Would you like to specify a value for K? [y/n] ");

        boolean isBounded = (scanner.nextLine().equals("y"));

        if (isBounded) {
            System.out.println("Simulating a M/D/1/K queue.");
        } else {
            System.out.println("Simulating a M/D/1 queue.");
        }

        System.out.println("");

        long n = 0;
        long lambda = 0;
        double L = 0.0;
        double C = 0.0;

        System.out.print("Enter a value for n: ");
        n = scanner.nextLong();

        System.out.print("Enter a value for λ: ");
        lambda = scanner.nextLong();

        System.out.print("Enter a value for L: ");
        L = scanner.nextDouble();

        System.out.print("Enter a value for C: ");
        C = scanner.nextDouble();

        if (isBounded) {
            System.out.print("Enter a value for K: ");
            int K = scanner.nextInt();

            System.out.println("");
            System.out.println("Starting simulation...");
            System.out.println("");

            SimulationK simulation = new SimulationK(lambda, L, C, K);
            simulation.startSimulation(n);
            simulation.computePerformance();
        } else {
            System.out.println("");
            System.out.println("Starting simulation...");
            System.out.println("");

            Simulation simulation = new Simulation(lambda, L, C);
            simulation.startSimulation(n);
            simulation.computePerformance();
        }
    }
}
