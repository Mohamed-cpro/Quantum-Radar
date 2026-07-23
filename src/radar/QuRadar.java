package radar;

import model.Car;
import model.Fine;
import model.Observation;
import model.Violation;
import rules.Rule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * QuRadar - Quantum Radar traffic monitoring system.
 *
 * Overview:
 * QuRadar receives observations streamed from a physical roadside radar unit.
 * Each observation carries the data captured for a single car as it passes
 * the radar: plate number, timestamp, car type (Private, Truck, Bus), speed,
 * and seatbelt status (fastened / not fastened). In a real deployment the
 * physical radar would use an on-device AI perception pipeline - a YOLO-style
 * object detector to locate and classify the vehicle (car type), an OCR
 * model (e.g. a CRNN/CNN-based License Plate Recognition model) to read the
 * plate number, a laser/doppler speed sensor for the speed reading, and a
 * lightweight CNN image classifier trained to detect whether the driver's
 * seatbelt is visibly fastened. QuRadar itself is agnostic to how that data
 * was produced; it simply consumes the resulting Observation objects.
 *
 * QuRadar does not hard-code any traffic law. Instead, it holds a
 * collection of Rule objects (see the rules package), each of which knows
 * how to evaluate a single Observation and produce zero or one Violation.
 * On every observation, QuRadar runs all registered rules; any violations
 * found are bundled into a Fine, printed, and recorded. New rules can be
 * added at any time via addRule(Rule) without ever modifying this class,
 * satisfying the system's extensibility requirement.
 *
 * QuRadar also keeps a running ledger of all fines issued (queryable via
 * getAllPossibleFines, aggregated per plate number) and a tally of how many
 * times each kind of violation has occurred (getAllViolatedRules).
 */
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