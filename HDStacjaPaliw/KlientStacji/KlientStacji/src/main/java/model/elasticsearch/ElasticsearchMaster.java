/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.elasticsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.node.Node;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import org.elasticsearch.search.SearchHit;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author PeerZet
 */
public class ElasticsearchMaster {

    private Node node;
    Client client;

    DateTime time = new DateTime();
    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");//because of the pattern in .csv files
    int periodEalierMinutesAmount = 400;//it will always scan 30 minutes before

    String tankIndexName = "tank";
    String tankTypeName = "tank";
    String nozzleIndexName = "nozzle";
    String nozzleTypeName = "nozzle";

    public ElasticsearchMaster() {
        node = nodeBuilder().node();
        client = node.client();
    }

    public void disconnect() {
        node.close();
    }

    /**
     * Checks if everything goes right. If not - returns the list with the id's
     * of the broken tank
     *
     * @return
     */
    public List<Integer> searchAnomaly() {
        DateTime now = new DateTime();//now time
        DateTime periodEalier = now.minusMinutes(periodEalierMinutesAmount);
        getNozzlesMeasures(periodEalier.toString(formatter), now.toString(formatter));

        return new ArrayList<Integer>();
    }

    private SearchResponse getNozzlesMeasures(String start, String end) {
        start = getDateTimeWithT(start);
        end = getDateTimeWithT(end);
        SearchResponse response = client.prepareSearch("nozzle").setTypes("nozzle").
                setPostFilter(FilterBuilders.rangeFilter("czasPoczatkowy").from(start).to(end)).execute().actionGet();
        Map<Integer, SearchHit> newestNozzles = getNewestNozzleMeasurements(response);
        for (SearchHit nozzle : newestNozzles.values()) {
            double beforeStartTankMeasurement = findBeforeStartTankMeasurement((String) nozzle.getSource().get("czasPoczatkowy"),
                    Integer.valueOf((String) nozzle.getSource().get("idZbiornika")));
//            double afterStartTankMeasurement = findAfterStartTankMeasurement();
//            double beforeFinishTankMeasurement = findBeforeFinishTankMeasurement();
//            double afterFinisihTankMeasurement = findAfterFinishTankMeasurement();
//            double nozzleValue = (double) nozzle.getSource().get("objetoscBrutto");
//            boolean risk = checkIfFuelIsOut(nozzleValue, beforeStartTankMeasurement, afterStartTankMeasurement, beforeFinishTankMeasurement,
//                    afterFinisihTankMeasurement);
        }
        return response;

    }

    private Map<Integer, SearchHit> getNewestNozzleMeasurements(SearchResponse response) {
        Map<Integer, SearchHit> listOfNozzleMeasurements = new HashMap<>();//this will contain the last measurement for every nozzle
        SearchHit newestHit;
        for (SearchHit hit : response.getHits()) {
            Integer nozzleId = Integer.valueOf((String) hit.getSource().get("idPistoletu"));
            //If we dont have measurement for this nozzle yet, we add it
            if (!listOfNozzleMeasurements.containsKey(nozzleId)) {
                listOfNozzleMeasurements.put(nozzleId, hit);
            }//else we must find the NEWEST nozzle measurement
            else {
                String dateTime = (String) hit.getSource().get("czasKoncowy");//we are looking for the one which finished last
                DateTime nozzleFinishTime = new DateTime(dateTime);
                DateTime theLastNozzleTime = new DateTime(listOfNozzleMeasurements.get(nozzleId).getSource().get("czasKoncowy"));
                if (nozzleFinishTime.isAfter(theLastNozzleTime)) {
                    listOfNozzleMeasurements.replace(nozzleId, hit);//we found the NEWEST and replace older
                }
            }
        }
        return listOfNozzleMeasurements;

    }

    /**
     * Returns the given datetime with T sign, based on ISO_8601
     *
     * @param dateTime
     * @return
     */
    private String getDateTimeWithT(String dateTime) {
        int startIndexForT = dateTime.indexOf(" ");
        String part = dateTime.substring(0, startIndexForT);
        String finaldateTime = part + "T" + dateTime.substring(startIndexForT + 1, dateTime.length());
        return finaldateTime;
    }

    /**
     * This method returns the first tank measurement just before startTime of
     * the nozzle measurement. Default value for checking the tank measurements
     * is 4 minutes before startTime, if anythning won't be found, it will be
     * next +4 minutes.
     *
     * @param startTime
     * @return
     */
    private double findBeforeStartTankMeasurement(String startTime, int tankId) {
        System.out.println("tankid:" + tankId);
        DateTime startDateTime = new DateTime(startTime);
        int INTERVAL = 4;
        SearchResponse response = null;
        for (int i = 0; i < 14; i++) { //if we won't get result for 1h before sth is broken
            DateTime beforeDateTime = startDateTime.minusMinutes(INTERVAL);//we check measurement 4 minutes before startTime of the nozzle measure
            response = client.prepareSearch("tank").setTypes("tank").
                    setPostFilter(FilterBuilders.rangeFilter("stempelCzasowy").from(beforeDateTime).to(startTime)).
                    setPostFilter(FilterBuilders.rangeFilter("idZbiornika").from(tankId).to(tankId)).
                    execute().actionGet();
            if (response != null) {
                if (response.getHits().hits().length > 0) {
                    System.out.println("Ile wynikow: " + response.getHits().hits().length);
                    break;
                }
            }
            INTERVAL += 4;
        }//we found the newest tank measurement, the closest to the startTime of the nozzle measurement
        SearchHit theClosestTankTimestamp = null;
        for (SearchHit hit : response.getHits()) {
            //If we dont have any tank measurement, well we have first
            if (theClosestTankTimestamp == null) {
                theClosestTankTimestamp = hit;
            }//else we must find the closes tank measurement
            else {
                String dateTime = (String) hit.getSource().get("stempelCzasowy");//we are looking for the one which finished last
                DateTime currentTankTimestamp = new DateTime(dateTime);
                DateTime theLastNozzleTime = new DateTime(theClosestTankTimestamp.getSource().get("stempelCzasowy"));
                if (currentTankTimestamp.isAfter(theLastNozzleTime)) {
                    theClosestTankTimestamp = hit;//we found the closest
                }
            }
        }
        System.out.println("Czas zbiornika " + theClosestTankTimestamp.getSource().get("stempelCzasowy"));
        return Double.valueOf((String) theClosestTankTimestamp.getSource().get("objetoscBrutto"));
    }

    private double findAfterStartTankMeasurement() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private double findBeforeFinishTankMeasurement() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private double findAfterFinishTankMeasurement() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean checkIfFuelIsOut(double nozzleValue, double beforeStartTankMeasurement, double afterStartTankMeasurement, double beforeFinishTankMeasurement, double afterFinisihTankMeasurement) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
