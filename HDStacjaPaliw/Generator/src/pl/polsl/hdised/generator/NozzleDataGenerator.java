/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.hdised.generator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.TimerTask;
import pl.polsl.hdised.models.NozzleReading;
import pl.polsl.hdised.utils.CSVLogger;
import pl.polsl.hdised.utils.ISO8601;

/**
 *
 * @author Mateusz
 */
public class NozzleDataGenerator extends TimerTask {
    
    private Double nozzleMeterState = 0.;
    private Double fuelToTank = 0.;
    Integer nozzleID = 0;
    ArrayList<NozzleReading> readings;
    TankDataGenerator tankData;
    
    private Double minSingleTankingVolume;
    private Double maxSingleTankingVolume;
    private Double nozzleFlowRate;
    Double temperature;
    private ArrayList<NozzleReading> readingsToAggregate;
    private Integer nozzleMeasurementInterval;
    String nozzleDataFile;
    
    @Override
    public void run() {
        generateReading();
        
    }
    
    public void generateReading()
    {
        CSVLogger logger = new CSVLogger();
        Double value = 0.;
        Double valueBrutto = 0.;
        Double valueNetto = 0.;
        Double t = randomizeTemperature();
        Calendar date = GregorianCalendar.getInstance();
       
        if(fuelToTank-(nozzleFlowRate*nozzleMeasurementInterval) > 0)
        {
            fuelToTank -= nozzleFlowRate*nozzleMeasurementInterval;
            value = nozzleFlowRate*nozzleMeasurementInterval;
            tankData.removeFuel(nozzleFlowRate*nozzleMeasurementInterval);
        }
        else
        {
            tankData.removeFuel(fuelToTank);
            value = fuelToTank;
            fuelToTank = 0.;
        }
        valueBrutto = convertToBrutto(value);
        valueNetto = convertToNetto(valueBrutto, temperature);
        readingsToAggregate.add(new NozzleReading(tankData.tankID, nozzleID, date, value, valueBrutto, valueNetto, t));
        logger.logLine(nozzleDataFile, Arrays.asList(tankData.tankID.toString(), nozzleID.toString(), ISO8601.fromCalendar(date), value.toString()));
        
    }
    
    public Double convertToBrutto(Double value)
    {
        return value * 1.18;
    }
    
    public Double convertToNetto(Double bruttoValue, Double temperature)
    {
        return bruttoValue * 1.03;
    }
    
    public Double randomizeTemperature()
    {
        Random r = new Random();
        return temperature + r.nextDouble() * 0.1;
    }
    
    public NozzleDataGenerator(Integer nozzleID, TankDataGenerator tankData) {
        readings = new ArrayList();
        this.tankData = tankData;
        nozzleMeterState = 0.;
        this.nozzleID = nozzleID;
        readingsToAggregate = new ArrayList<>();
        
        Properties properties = new Properties(); 
        //InputStream inputStream = getClass().getResourceAsStream("/pl/polsl/hdised/config.properties");
        try {  
            InputStream inputStream = new FileInputStream("config.properties");
            properties.load(inputStream);  
        } catch (IOException e) {  
            System.out.println("Could not open config file");  
        }  
        minSingleTankingVolume = Double.parseDouble(properties.getProperty("minSingleTankingVolume"));  
        maxSingleTankingVolume = Double.parseDouble(properties.getProperty("maxSingleTankingVolume")); 
        nozzleMeasurementInterval = Integer.parseInt(properties.getProperty("nozzleMeasurementInterval"));
        nozzleFlowRate = Double.parseDouble(properties.getProperty("nozzleFlowRate"));
        nozzleDataFile = properties.getProperty("nozzleData");
        temperature = Double.parseDouble(properties.getProperty("temperature"));
        
    }
    
    public double singleTanking()
    {
        Random r = new Random();
        double randomTanking = minSingleTankingVolume + (maxSingleTankingVolume - minSingleTankingVolume) * r.nextDouble();
        setFuelToTank(getFuelToTank() + randomTanking);     
        
        return randomTanking;
    }

    /**
     * @return the nozzleMeterState
     */
    public Double getNozzleMeterState() {
        return nozzleMeterState;
    }

    /**
     * @param nozzleMeterState the nozzleMeterState to set
     */
    public void setNozzleMeterState(Double nozzleMeterState) {
        this.nozzleMeterState = nozzleMeterState;
    }

    /**
     * @return the fuelToTank
     */
    public Double getFuelToTank() {
        return fuelToTank;
    }

    /**
     * @param fuelToTank the fuelToTank to set
     */
    public void setFuelToTank(Double fuelToTank) {
        this.fuelToTank = fuelToTank;
    }

    /**
     * @return the readingsToAggregate
     */
    public ArrayList<NozzleReading> getReadingsToAggregate() {
        return readingsToAggregate;
    }

    /**
     * @param readingsToAggregate the readingsToAggregate to set
     */
    public void setReadingsToAggregate(ArrayList<NozzleReading> readingsToAggregate) {
        this.readingsToAggregate = readingsToAggregate;
    }
}
