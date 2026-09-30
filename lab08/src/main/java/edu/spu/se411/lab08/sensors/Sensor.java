package edu.spu.se411.lab08.sensors;

import java.util.ArrayList;
import java.util.List;

import edu.spu.se411.lab08.observer.Observer;
import edu.spu.se411.lab08.observer.Subject;

/**
 * Base class for every sensor. Holds the observer list and the notification
 * logic once, so concrete sensors only describe what they measure.
 */
public abstract class Sensor implements Subject, Cloneable {

    private final String name;
    private final String unit;
    private double reading;
    private List<Observer> observers = new ArrayList<>();

    protected Sensor(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    @Override
    public void register(Observer o) {
        if (o != null && !observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void unregister(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

    /** Stores the new reading and notifies observers only if it actually changed. */
    public void setReading(double reading) {
        if (Double.compare(this.reading, reading) != 0) {
            this.reading = reading;
            notifyObservers();
        }
    }

    public double getReading() {
        return reading;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public int getObserverCount() {
        return observers.size();
    }

    /**
     * The clone copies the sensor's state (name, unit, reading) but starts with
     * an empty observer list: it is a new subject with its own observers.
     */
    @Override
    public Sensor clone() {
        try {
            Sensor copy = (Sensor) super.clone();
            copy.observers = new ArrayList<>();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Sensor implements Cloneable", e);
        }
    }

    @Override
    public String toString() {
        return String.format("%s[%.2f %s]", name, reading, unit);
    }
}
