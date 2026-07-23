package rules;

import model.CarType;
import model.Observation;
import model.Violation;

/**
 * Rule that flags cars of a given type exceeding a maximum allowed speed,
 * e.g. Truck speed shouldn't exceed 60, Private car speed shouldn't exceed 80.
 */
public class SpeedRule implements Rule {

    private final CarType carType;
    private final int maxSpeed;
    private final double fee;

    public SpeedRule(CarType carType, int maxSpeed, double fee) {
        this.carType = carType;
        this.maxSpeed = maxSpeed;
        this.fee = fee;
    }

    @Override
    public Violation evaluate(Observation observation) {
        if (observation.getCar().getCarType() == carType && observation.getSpeed() > maxSpeed) {
            String description = "speed of " + observation.getSpeed()
                    + " exceeded max allowed " + maxSpeed;
            return new Violation("Speed Limit", description, fee);
        }
        return null;
    }
}