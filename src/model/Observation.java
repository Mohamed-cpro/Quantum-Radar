package model;

import java.util.Date;

/**
 * A single observation reported by the physical radar hardware for one car
 * passing by: who it was, when, how fast it was going, and whether the
 * seatbelt was fastened.
 */
public class Observation {

    private final Car car;
    private final Date date;
    private final int speed;
    private final boolean seatbeltFastened;

    public Observation(Car car, Date date, int speed, boolean seatbeltFastened) {
        this.car = car;
        this.date = date;
        this.speed = speed;
        this.seatbeltFastened = seatbeltFastened;
    }

    public Car getCar() {
        return car;
    }

    public Date getDate() {
        return date;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isSeatbeltFastened() {
        return seatbeltFastened;
    }
}