/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.hdised.generator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Random;
import java.util.TimerTask;
import pl.polsl.hdised.utils.CSVLogger;
import pl.polsl.hdised.utils.ISO8601;

/**
 *
 * @author Mateusz
 */
public class TankDataGenerator extends TimerTask {
    
    private Double meterState;
    private Double minTankRefillVolume;
    private Double maxTankRefillVolume;
    Double minWaterIntrusionPerHour;
    Double maxWaterIntrusionPerHour;
    Double minFuelLeakagePerHour;
    Double maxFuelLeakagePerHour;
    Double temperature;
    Integer nozzleAggregatedEntryInterval;
    
    private Double waterIntrusionPerHour = 0.;
    private Double fuelLeakagePerHour = 0.;
    
    Integer tankID;
    String tankDataFile;
    
    public TankDataGenerator(Integer tankID, Double initialMeterState) 
    {
        this.tankID = tankID;
        meterState = initialMeterState;
        Properties properties = new Properties(); 
        //InputStream inputStream = getClass().getResourceAsStream("/pl/polsl/hdised/config.properties");
        try {  
            InputStream inputStream = new FileInputStream("config.properties");
            properties.load(inputStream);  
        } catch (IOException e) {  
            System.out.println("Could not open config file");  
        }   
        minTankRefillVolume = Double.parseDouble(properties.getProperty("minTankRefillVolume"));  
        maxTankRefillVolume = Double.parseDouble(properties.getProperty("maxTankRefillVolume"));
        minWaterIntrusionPerHour = Double.parseDouble(properties.getProperty("minWaterIntrusionPerHour")); 
        maxWaterIntrusionPerHour = Double.parseDouble(properties.getProperty("maxWaterIntrusionPerHour")); 
        minFuelLeakagePerHour = Double.parseDouble(properties.getProperty("minFuelLeakagePerHour")); 
        maxFuelLeakagePerHour = Double.parseDouble(properties.getProperty("maxFuelLeakagePerHour")); 
        nozzleAggregatedEntryInterval = Integer.parseInt(properties.getProperty("nozzleAggregatedEntryInterval")); 
        tankDataFile = properties.getProperty("tankData");
        temperature = Double.parseDouble(properties.getProperty("temperature"));
    }
    
    @Override
    public void run() {
        generateReading();
    }
    
    public void generateReading()
    {
        meterState += waterIntrusionPerHour * (nozzleAggregatedEntryInterval.doubleValue()/3600.0);
        meterState -= fuelLeakagePerHour * (nozzleAggregatedEntryInterval.doubleValue()/3600.0);
        
        CSVLogger logger = new CSVLogger();
        Calendar date = GregorianCalendar.getInstance();
        Double t = randomizeTemperature();
       

        logger.logLine(tankDataFile, Arrays.asList(tankID.toString(), ISO8601.fromCalendar(date), meterState.toString(), calculateNetto(meterState, t).toString(), "", t.toString()));
    }
    
    public double waterIntrusion()
    {
        Random r = new Random();
        double randomIntrusion = minWaterIntrusionPerHour + (maxWaterIntrusionPerHour - minWaterIntrusionPerHour) * r.nextDouble();
        waterIntrusionPerHour = randomIntrusion;
        
        return randomIntrusion;
        
    }
    
    /**
     * Dummy calculation
     * 
     * @param volume
     * @param temperature
     * @return 
     */
    public Double calculateNetto(Double volume, Double temperature)
    {
        return volume * 1.03;
    }
    
    public Double randomizeTemperature()
    {
        Random r = new Random();
        return temperature + r.nextDouble() * 0.1;
    }
    
    public double fuelLeakage()
    {
        Random r = new Random();
        double randomLeakage = minFuelLeakagePerHour + (maxFuelLeakagePerHour - minFuelLeakagePerHour) * r.nextDouble();
        fuelLeakagePerHour = randomLeakage;
        
        return randomLeakage;
    }
    
    public double tankRefill()
    {
        Random r = new Random();
        double randomRefill = minTankRefillVolume + (maxTankRefillVolume - minTankRefillVolume) * r.nextDouble();
        setMeterState((Double) (getMeterState() + randomRefill));
        
        return randomRefill;
    }
    
    public void addFuel(Double fuelToAdd)
    {
        setMeterState((Double) (getMeterState() + fuelToAdd));
    }
    
    public void removeFuel(Double fuelToRemove)
    {
        setMeterState((Double) (getMeterState() - fuelToRemove));
    }

    /**
     * @return the meterState
     */
    public Double getMeterState() {
        return meterState;
    }

    /**
     * @param meterState the meterState to set
     */
    public void setMeterState(Double meterState) {
        this.meterState = meterState;
    }

    /**
     * @return the waterIntrusionPerHour
     */
    public Double getWaterIntrusionPerHour() {
        return waterIntrusionPerHour;
    }

    /**
     * @param waterIntrusionPerHour the waterIntrusionPerHour to set
     */
    public void setWaterIntrusionPerHour(Double waterIntrusionPerHour) {
        this.waterIntrusionPerHour = waterIntrusionPerHour;
    }

    /**
     * @return the fuelLeakagePerHour
     */
    public Double getFuelLeakagePerHour() {
        return fuelLeakagePerHour;
    }

    /**
     * @param fuelLeakagePerHour the fuelLeakagePerHour to set
     */
    public void setFuelLeakagePerHour(Double fuelLeakagePerHour) {
        this.fuelLeakagePerHour = fuelLeakagePerHour;
    }
    
}
