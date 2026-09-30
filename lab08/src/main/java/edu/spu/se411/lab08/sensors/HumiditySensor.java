package edu.spu.se411.lab08.sensors;

public class HumiditySensor extends Sensor {

    public HumiditySensor(String name) {
        super(name, "%");
    }

    @Override
    public HumiditySensor clone() {
        return (HumiditySensor) super.clone();
    }
}
