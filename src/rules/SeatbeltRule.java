package rules;

import model.Observation;
import model.Violation;

/**
 * Rule that flags an observation where the seatbelt was not fastened.
 */
public class SeatbeltRule implements Rule {

    private final double fee;

    public SeatbeltRule(double fee) {
        this.fee = fee;
    }

    @Override
    public Violation evaluate(Observation observation) {
        if (!observation.isSeatbeltFastened()) {
            return new Violation("Seatbelt", "Seatbelt not fastned", fee);
        }
        return null;
    }
}