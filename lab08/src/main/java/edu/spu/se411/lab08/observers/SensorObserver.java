package edu.spu.se411.lab08.observers;

import edu.spu.se411.lab08.observer.Observer;
import edu.spu.se411.lab08.observer.Subject;
import edu.spu.se411.lab08.sensors.Sensor;

/**
 * Base class for observers that watch sensors. It does the Subject-to-Sensor
 * check once, so each concrete observer only decides how to react.
 */
public abstract class SensorObserver implements Observer {

    private final String name;

    protected SensorObserver(String name) {
        this.name = name;
    }

    @Override
    public final void update(Subject subject) {
        if (subject instanceof Sensor) {
            onReading((Sensor) subject);
        }
    }

    /** Called every time an observed sensor reports a new reading. */
    protected abstract void onReading(Sensor sensor);

    public String getName() {
        return name;
    }
}
