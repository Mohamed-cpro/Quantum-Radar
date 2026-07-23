package rules;

import model.Observation;
import model.Violation;

/**
 * Contract for a single traffic rule. New rules can be added to the system
 * by simply implementing this interface and registering the rule with
 * QuRadar - no changes to QuRadar itself are required, keeping the system
 * open for extension but closed for modification (Open/Closed Principle).
 */
public interface Rule {

    /**
     * Evaluates the rule against a single radar observation.
     *
     * @param observation the reading captured by the radar
     * @return a Violation if the observation breaks this rule, or null if it doesn't
     */
    Violation evaluate(Observation observation);
}