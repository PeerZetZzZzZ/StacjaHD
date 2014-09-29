/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.hdised.generator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import java.util.Timer;

/**
 *
 * @author Mateusz
 */
public class MeasurementsGenerator {
    
    Integer nozzleMeasurementInterval;
    Integer tankMeasurementInterval;
    Integer nozzleAggregatedEntryInterval;
    
    public MeasurementsGenerator()
    {
        Properties properties = new Properties(); 
        InputStream inputStream = getClass().getResourceAsStream("/pl/polsl/hdised/config.properties");
        try {  
            properties.load(inputStream);  
        } catch (IOException e) {  
            System.out.println("Could not open config file");  
        } 
            
        nozzleMeasurementInterval = Integer.parseInt(properties.getProperty("nozzleMeasurementInterval"));  
        tankMeasurementInterval = Integer.parseInt(properties.getProperty("tankMeasurementInterval")); 
        nozzleAggregatedEntryInterval = Integer.parseInt(properties.getProperty("nozzleAggregatedEntryInterval")); 
    }
    
    public void start()
    {
        TankDataGenerator tankData = new TankDataGenerator(435, 2000.0);
        NozzleDataGenerator nozzleData = new NozzleDataGenerator(Integer.valueOf(32670), tankData);
        NozzleDataAggregator nozzleDataAggregated = new NozzleDataAggregator(nozzleData);
        
        Timer timerNozzle = new Timer(true);
        timerNozzle.scheduleAtFixedRate(nozzleData, 0, nozzleMeasurementInterval * 1000);
        
        Timer timerTank = new Timer(true);
        timerTank.scheduleAtFixedRate(tankData, 0, tankMeasurementInterval * 1000);
        
        Timer timerAggregation = new Timer(true);
        timerAggregation.scheduleAtFixedRate(nozzleDataAggregated, 0, nozzleAggregatedEntryInterval * 1000);
        
        Boolean continueLoop = true;
        while(continueLoop) {
            int i = 0;
            Scanner input = new Scanner(System.in);
            System.out.println("Options:\n1. Start tanking\n2. Tank refill\n3. Start/stop fuel leak simulation\n4. Start/stop water intrusion simulation\n5. Exit");
            i = input.nextInt();
            
            switch (i) {
                case 1: double tankingVolume = nozzleData.singleTanking();
                        System.out.println(tankingVolume + " litres of fuel has been tanked by nozzle");
                        break;
                case 2: double refillVolume = tankData.tankRefill();
                        System.out.println("Tank has been refilled by " + refillVolume + ". It's volume is now " + tankData.getMeterState());
                        break;
                case 3: if (tankData.getFuelLeakagePerHour() == 0.) {
                            double fuelLeakagePerHour = tankData.fuelLeakage();
                            System.out.println("Tank is now leaking " + fuelLeakagePerHour + " litres of fuel per hour");
                        }
                        else {
                            tankData.setWaterIntrusionPerHour(0.);
                            System.out.println("Fuel leak stopped");
                        }  
                        break;
                case 4: if (tankData.getWaterIntrusionPerHour() == 0.) {
                            double waterIntrusionPerHour = tankData.waterIntrusion();
                            System.out.println("Now water intrudes tank by " + waterIntrusionPerHour + " litres per hour");
                        }
                        else {
                            tankData.setWaterIntrusionPerHour(0.);
                            System.out.println("Water intrusion stopped");
                        }
                        break;
                case 5: continueLoop = false;
                        break;
            }
            
        } 
        
    }
    
}
