/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.elasticsearch;

import model.MonitorMaster;

/**
 *
 * @author PeerZet
 */
public class Main {

    public static ElasticsearchMaster master;

    public static void main(String args[]) {
        MonitorMaster monitor = new MonitorMaster();
        monitor.check();
    }
}
