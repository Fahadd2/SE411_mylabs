package edu.spu.se411.lab08.observers;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.spu.se411.lab08.sensors.Sensor;

/** Records every reading change, both on the console and in the log file. */
public class LoggerObserver extends SensorObserver {

    private static final Logger logger = LoggerFactory.getLogger(LoggerObserver.class);
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    public LoggerObserver(String name) {
        super(name);
    }

    @Override
    protected void onReading(Sensor sensor) {
        System.out.printf("[%s] %s  %s changed to %.2f %s%n",
                getName(), LocalTime.now().format(TIME), sensor.getName(),
                sensor.getReading(), sensor.getUnit());
        logger.info("{} changed to {} {}", sensor.getName(),
                String.format("%.2f", sensor.getReading()), sensor.getUnit());
    }
}
