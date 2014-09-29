/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.hdised.generator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.TimerTask;
import pl.polsl.hdised.models.NozzleReading;
import pl.polsl.hdised.utils.CSVLogger;
import pl.polsl.hdised.utils.ISO8601;

/**
 *
 * @author Mateusz
 */
public class NozzleDataAggregator extends TimerTask {
    
    ArrayList<NozzleReading> toAggregate;
    String aggregatedNozzleData;
    
    public NozzleDataAggregator(NozzleDataGenerator nozzleData)
    {
        toAggregate = nozzleData.getReadingsToAggregate();
        
        Properties properties = new Properties(); 
        
        //getClass().getResourceAsStream("/pl/polsl/hdised/config.properties");
        try {  
            InputStream inputStream = new FileInputStream("config.properties");
            properties.load(inputStream);  
        } catch (IOException e) {  
            System.out.println("Could not open config file");  
        }  
        aggregatedNozzleData = properties.getProperty("aggregatedNozzleData"); 
    }
    
    @Override
    public void run() {
        generateReading();
        
    }
    
    public void generateReading()
    {
        if (toAggregate.size() <= 2)
            return;
        
        CSVLogger logger = new CSVLogger();
        Double value = 0.;
        Double valueBrutto = 0.;
        Double valueNetto = 0.;
        for (NozzleReading reading : toAggregate)
        {
            value += reading.getMeterReading();
            valueBrutto += reading.getMeterReadingBrutto();
            valueNetto += reading.getMeterReadingNetto();
        }
        Calendar startDate, endDate;
        startDate = toAggregate.get(0).getTime();
        endDate = toAggregate.get(toAggregate.size()-1).getTime();
       
        logger.logLine(aggregatedNozzleData, Arrays.asList(toAggregate.get(0).getNozzleID().toString(),
                                                           toAggregate.get(0).getTankID().toString(), 
                                                           ISO8601.fromCalendar(startDate),
                                                           ISO8601.fromCalendar(endDate),
                                                           value.toString(),
                                                           valueBrutto.toString(), 
                                                           valueNetto.toString(),
                                                           toAggregate.get(0).getTemperature().toString()
                                                           ));
        
        while(toAggregate.size() > 1)
            toAggregate.remove(0);
        toAggregate.get(0).setMeterReading(0.);
        toAggregate.get(0).setMeterReadingBrutto(0.);
        toAggregate.get(0).setMeterReadingNetto(0.);
        
    }
    
}
