package edu.spu.se411.lab08;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.spu.se411.lab08.observers.DashboardObserver;
import edu.spu.se411.lab08.observers.LoggerObserver;
import edu.spu.se411.lab08.sensors.HumiditySensor;
import edu.spu.se411.lab08.sensors.TemperatureSensor;

public class App {

    static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("Application is starting...");

        TemperatureSensor temp = new TemperatureSensor("Temperature");
        HumiditySensor humidity = new HumiditySensor("Humidity");

        DashboardObserver dashboard = new DashboardObserver("Dashboard");
        LoggerObserver log = new LoggerObserver("Logger");

        temp.register(dashboard);
        temp.register(log);
        humidity.register(dashboard);
        humidity.register(log);

        System.out.println("=== Part 1 & 2: simulating sensor readings ===");
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            System.out.printf("--- Reading #%d ---%n", i + 1);
            temp.setReading(20 + random.nextDouble() * 15);
            humidity.setReading(40 + random.nextDouble() * 20);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println();
        System.out.println("=== Part 3: cloning a sensor ===");
        TemperatureSensor tempClone = temp.clone();
        System.out.println("Original: " + temp + " with " + temp.getObserverCount() + " observers");
        System.out.println("Clone:    " + tempClone + " with " + tempClone.getObserverCount() + " observers");

        System.out.println("Changing the clone's reading (no one is observing it yet, so nothing is printed):");
        tempClone.setReading(99.0);

        DashboardObserver cloneDashboard = new DashboardObserver("Clone Dashboard");
        tempClone.register(cloneDashboard);
        System.out.println("Registered a separate dashboard on the clone, changing its reading again:");
        tempClone.setReading(25.5);

        System.out.println("Changing the original's reading (only its own dashboard and logger react):");
        temp.setReading(30.0);

        System.out.println("Unregistering the logger from the original, changing its reading again:");
        temp.unregister(log);
        temp.setReading(31.0);

        logger.info("Application finished.");
    }
}
