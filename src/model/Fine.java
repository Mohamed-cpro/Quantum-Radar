package model;

import java.util.Collections;
import java.util.List;

/**
 * A fine issued for a car after one or more violations were detected in a
 * single observation. Aggregates the violations and their total cost.
 */
public class Fine {

    private final String plateNumber;
    private final List<Violation> violations;

    public Fine(String plateNumber, List<Violation> violations) {
        this.plateNumber = plateNumber;
        this.violations = violations;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public List<Violation> getViolations() {
        return Collections.unmodifiableList(violations);
    }

    public double getTotalAmount() {
        double total = 0;
        for (Violation v : violations) {
            total += v.getFee();
        }
        return total;
    }

    /**
     * Prints the fine exactly in the format required by the system, e.g.:
     *
     * Traffic for car ABC1234
     * Total amount: 400 EGP
     * Violations:
     * - Seatbelt not fastned : 100 EGP
     * - speed of 94 exceeded max allowed 80 : 300 EGP
     */
    public void print() {
        StringBuilder sb = new StringBuilder();
        sb.append("Traffic for car ").append(plateNumber).append("\n");
        sb.append("Total amount: ").append(formatAmount(getTotalAmount())).append(" EGP\n");
        sb.append("Violations:");
        for (Violation v : violations) {
            sb.append("\n- ").append(v.getDescription()).append(" : ")
              .append(formatAmount(v.getFee())).append(" EGP");
        }
        System.out.println(sb);
    }

    private String formatAmount(double amount) {
        if (amount == Math.floor(amount)) {
            return String.valueOf((long) amount);
        }
        return String.valueOf(amount);
    }
}