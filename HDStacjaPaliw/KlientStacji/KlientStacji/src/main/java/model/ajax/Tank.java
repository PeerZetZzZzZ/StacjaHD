/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ajax;

/**
 *
 * @author PeerZet
 */
public class Tank {

    private Integer id;
    private Boolean state;

    public Tank(Integer id, Boolean state) {
        this.id = id;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getState() {
        return state;
    }

}
