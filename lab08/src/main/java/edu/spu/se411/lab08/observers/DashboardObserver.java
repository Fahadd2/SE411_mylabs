package edu.spu.se411.lab08.observers;

import edu.spu.se411.lab08.sensors.Sensor;

/** Shows the latest reading of each sensor on the screen. */
public class DashboardObserver extends SensorObserver {

    public DashboardObserver(String name) {
        super(name);
    }

    @Override
    protected void onReading(Sensor sensor) {
        System.out.printf("[%s] %-12s -> %6.2f %s%n",
                getName(), sensor.getName(), sensor.getReading(), sensor.getUnit());
    }
}
