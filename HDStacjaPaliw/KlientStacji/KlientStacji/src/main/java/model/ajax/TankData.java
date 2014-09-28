/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ajax;

import model.ajax.Tank;
import java.util.HashMap;
import model.elasticsearch.ElasticsearchMaster;

/**
 *
 * @author PeerZet
 */
public class TankData {

    private HashMap<Integer, Tank> tanks = new HashMap<>();
    private ElasticsearchMaster master = new ElasticsearchMaster();

    public HashMap<Integer, Tank> getTanks() {
        HashMap<Integer, Boolean> tanks = master.searchAnomaly();
        for (int tankId : tanks.keySet()) {
            Tank singleTank = new Tank(tankId, tanks.get(tankId));
            this.tanks.put(tankId, singleTank);
        }
        return this.tanks;
    }

    public TankData() {

    }
}
