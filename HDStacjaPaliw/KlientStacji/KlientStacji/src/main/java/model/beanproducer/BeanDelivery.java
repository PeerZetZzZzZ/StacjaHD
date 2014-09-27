/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.beanproducer;

import model.elasticsearch.ElasticsearchMaster;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Produces the Spring beans
 * @author PeerZet
 */
@Configuration
public class BeanDelivery {
    
   @Bean
   public ElasticsearchMaster elasticsearchMaster(){
       return new ElasticsearchMaster();
   }
}
