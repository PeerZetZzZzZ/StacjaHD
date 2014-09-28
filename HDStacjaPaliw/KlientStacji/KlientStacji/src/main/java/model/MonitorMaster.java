/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.elasticsearch.ElasticsearchMaster;

/**
 *
 * @author PeerZet
 */
public class MonitorMaster {

    ElasticsearchMaster elastic = new ElasticsearchMaster();

    public MonitorMaster() {

    }

    public void check() {
        elastic.searchAnomaly();
    }
}
