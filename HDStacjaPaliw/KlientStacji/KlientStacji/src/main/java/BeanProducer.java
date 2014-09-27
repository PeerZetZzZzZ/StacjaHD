
import model.MonitorMaster;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author PeerZet
 */
@Configuration
public class BeanProducer {
    
    
    @Bean
    public MonitorMaster tankMonitor(){
        return new MonitorMaster();
    }
}
