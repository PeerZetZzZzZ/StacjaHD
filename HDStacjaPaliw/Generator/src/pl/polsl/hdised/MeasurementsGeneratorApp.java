/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.hdised;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import pl.polsl.hdised.generator.MeasurementsGenerator;
import pl.polsl.hdised.generator.NozzleDataGenerator;
import pl.polsl.hdised.generator.TankDataGenerator;

/**
 *
 * @author Mateusz
 */
public class MeasurementsGeneratorApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        MeasurementsGenerator generator = new MeasurementsGenerator();
        generator.start();
        
    }
    
}
