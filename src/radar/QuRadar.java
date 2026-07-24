package radar;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Car;
import model.Fine;
import model.Observation;
import model.Violation;
import rules.Rule;


public class QuRadar {

    private final List<Rule> rules;
    private final List<Fine> issuedFines;

    public QuRadar() {
        this.rules = new ArrayList<>();
        this.issuedFines = new ArrayList<>();
    }

    /**
     * Registers a new rule with the radar. This is the extension point that
     * allows new traffic laws to be plugged in without changing QuRadar.
     */
    public void addRule(Rule rule) {
        rules.add(rule);
    }

    /**
     * Processes a single observation reported by the physical radar,
     * evaluates it against every registered rule, and issues a fine
     * (printing it) if any violations were found.
     *
     * @return the issued Fine, or null if the observation had no violations
     */
    public Fine processObservation(Observation observation) {
        List<Violation> violations = new ArrayList<>();

        for (Rule rule : rules) {
            Violation violation = rule.evaluate(observation);
            if (violation != null) {
                violations.add(violation);
            }
        }

        if (violations.isEmpty()) {
            return null;
        }

        Car car = observation.getCar();
        Fine fine = new Fine(car.getPlateNumber(), violations);
        issuedFines.add(fine);
        fine.print();
        return fine;
    }

    /**
     * Returns the total fined amount per plate number, aggregated across
     * every fine issued so far.
     */
    public Map<String, Double> getAllPossibleFines() {
        Map<String, Double> totalsByPlate = new LinkedHashMap<>();
        for (Fine fine : issuedFines) {
            totalsByPlate.merge(fine.getPlateNumber(), fine.getTotalAmount(), Double::sum);
        }
        return totalsByPlate;
    }

    /**
     * Returns how many times each distinct violation (by description) has
     * been recorded across all issued fines.
     */
    public Map<String, Integer> getAllViolatedRules() {
        Map<String, Integer> violationCounts = new LinkedHashMap<>();
        for (Fine fine : issuedFines) {
            for (Violation violation : fine.getViolations()) {
                violationCounts.merge(violation.getRuleName(), 1, Integer::sum);
            }
        }
        return violationCounts;
    }

    public List<Fine> getIssuedFines() {
        return issuedFines;
    }
}