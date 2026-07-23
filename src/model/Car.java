package model;

/**
 * Represents a car identified by the radar, e.g. via its license plate.
 */
public class Car {

    private final String plateNumber;
    private final CarType carType;

    public Car(String plateNumber, CarType carType) {
        this.plateNumber = plateNumber;
        this.carType = carType;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public CarType getCarType() {
        return carType;
    }
}