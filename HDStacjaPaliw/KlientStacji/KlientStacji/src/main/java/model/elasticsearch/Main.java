/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.elasticsearch;

/**
 *
 * @author PeerZet
 */
public class Main {
    public static ElasticsearchMaster master;
    public static  void main(String args[]){
        master = new ElasticsearchMaster();
        master.searchAnomaly();
    }
}
