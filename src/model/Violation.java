package model;

/**
 * A single rule breach detected in an observation, with a human-readable
 * description and the fee charged for it.
 */
public class Violation {

    private final String ruleName;
    private final String description;
    private final double fee;

    /**
     * @param ruleName    stable name of the rule that was broken (used for
     *                    aggregating counts, e.g. "Speed Limit")
     * @param description human-readable, observation-specific detail (used
     *                    when printing a fine)
     * @param fee         the fee charged for this violation
     */
    public Violation(String ruleName, String description, double fee) {
        this.ruleName = ruleName;
        this.description = description;
        this.fee = fee;
    }

    public String getRuleName() {
        return ruleName;
    }

    public String getDescription() {
        return description;
    }

    public double getFee() {
        return fee;
    }
}