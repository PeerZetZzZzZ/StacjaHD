/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.beanproducer.BeanDelivery;
import model.elasticsearch.ElasticsearchMaster;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author PeerZet
 */
public class MonitorMaster {

    ElasticsearchMaster elastic = new ElasticsearchMaster();

    public MonitorMaster() {
        //SpringIOC container init
//        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
//        ctx.register(BeanDelivery.class);
//        ctx.refresh();
//        elastic = ctx.getBean(ElasticsearchMaster.class);
    }
    public void check() {
        elastic.searchAnomaly();
    }
}
