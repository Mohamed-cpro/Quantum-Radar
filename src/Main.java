import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import model.Car;
import model.CarType;
import model.Observation;
import radar.QuRadar;
import rules.SeatbeltRule;
import rules.SpeedRule;

/**
 * Console demo: the user plays the role of the physical radar, typing in
 * each observation by hand. QuRadar evaluates it against the registered
 * rules and prints a fine immediately if there are violations.
 */
public class Main {

    public static void main(String[] args) {
        QuRadar radar = new QuRadar();

        radar.addRule(new SpeedRule(CarType.TRUCK, 60, 300));
        radar.addRule(new SpeedRule(CarType.PRIVATE, 80, 300));
        radar.addRule(new SpeedRule(CarType.BUS, 70, 300));
        radar.addRule(new SeatbeltRule(100));

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
            System.out.println("\nEnter observation (or type 'exit' to stop):");

            System.out.print("Plate number: ");
            String plate = scanner.nextLine().trim();
            if (plate.equalsIgnoreCase("exit")) {
                break;
            }

            System.out.print("Car type (PRIVATE, TRUCK, BUS): ");
            CarType carType = CarType.valueOf(scanner.nextLine().trim().toUpperCase());

            System.out.print("Speed: ");
            int speed = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Seatbelt fastened? (yes/no): ");
            boolean seatbelt = scanner.nextLine().trim().equalsIgnoreCase("yes");

            Observation observation = new Observation(new Car(plate, carType), new Date(), speed, seatbelt);
            radar.processObservation(observation); // prints the fine if any
            }

            System.out.println("\n=== All fines (total per plate) ===");
            for (Map.Entry<String, Double> entry : radar.getAllPossibleFines().entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue().longValue() + " EGP");
            }

            System.out.println("\n=== Violated rules (count) ===");
            for (Map.Entry<String, Integer> entry : radar.getAllViolatedRules().entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        }
    }
}