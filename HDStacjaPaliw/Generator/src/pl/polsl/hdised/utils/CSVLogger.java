/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.hdised.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author Mateusz
 */
public class CSVLogger {
    public CSVLogger()
    {}
    
    public void logLine(String filename, List<String> logData)
    {
        try {
            FileWriter fw = new FileWriter(filename, true);
            PrintWriter pw = new PrintWriter(fw, true);       

            String dataLine = "";
            for(String data : logData)
            {
            dataLine += data;
            dataLine += ";";
            }
            dataLine = dataLine.substring(0, dataLine.length()-1);
            pw.println(dataLine);

            pw.close();
            fw.close();
        }
        catch(IOException ioe) {
            System.out.println(ioe);       
        }
    }
}
