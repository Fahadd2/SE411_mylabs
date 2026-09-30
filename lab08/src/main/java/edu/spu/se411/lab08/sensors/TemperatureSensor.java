package edu.spu.se411.lab08.sensors;

public class TemperatureSensor extends Sensor {

    public TemperatureSensor(String name) {
        super(name, "C");
    }

    @Override
    public TemperatureSensor clone() {
        return (TemperatureSensor) super.clone();
    }
}
