/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.hdised.models;

import java.util.Calendar;

/**
 *
 * @author Mateusz
 */
public class NozzleReading {
    
    private Integer tankID;
    private Integer nozzleID;
    private Calendar time;
    private Double meterReading;
    private Double meterReadingNetto;
    private Double meterReadingBrutto;
    private Double temperature;
    
    public NozzleReading() {}
    
    public NozzleReading(Integer tankID, Integer nozzleID, Calendar time, Double meterReading, Double meterReadingBrutto, Double meterReadingNetto, Double temperature)
    {
        this.tankID = tankID;
        this.nozzleID = nozzleID;
        this.time = time;
        this.meterReading = meterReading;        
        this.temperature = temperature;
        this.meterReadingBrutto = meterReadingBrutto;
        this.meterReadingNetto = meterReadingNetto;
    }

    /**
     * @return the time
     */
    public Calendar getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Calendar time) {
        this.time = time;
    }

    /**
     * @return the meterReading
     */
    public Double getMeterReading() {
        return meterReading;
    }

    /**
     * @param meterReading the meterReading to set
     */
    public void setMeterReading(Double meterReading) {
        this.meterReading = meterReading;
    }

    /**
     * @return the tankID
     */
    public Integer getTankID() {
        return tankID;
    }

    /**
     * @param tankID the tankID to set
     */
    public void setTankID(Integer tankID) {
        this.tankID = tankID;
    }

    /**
     * @return the nozzleID
     */
    public Integer getNozzleID() {
        return nozzleID;
    }

    /**
     * @param nozzleID the nozzleID to set
     */
    public void setNozzleID(Integer nozzleID) {
        this.nozzleID = nozzleID;
    }

    /**
     * @return the temperature
     */
    public Double getTemperature() {
        return temperature;
    }

    /**
     * @param temperature the temperature to set
     */
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    /**
     * @return the meterReadingNetto
     */
    public Double getMeterReadingNetto() {
        return meterReadingNetto;
    }

    /**
     * @param meterReadingNetto the meterReadingNetto to set
     */
    public void setMeterReadingNetto(Double meterReadingNetto) {
        this.meterReadingNetto = meterReadingNetto;
    }

    /**
     * @return the meterReadingBrutto
     */
    public Double getMeterReadingBrutto() {
        return meterReadingBrutto;
    }

    /**
     * @param meterReadingBrutto the meterReadingBrutto to set
     */
    public void setMeterReadingBrutto(Double meterReadingBrutto) {
        this.meterReadingBrutto = meterReadingBrutto;
    }
}
