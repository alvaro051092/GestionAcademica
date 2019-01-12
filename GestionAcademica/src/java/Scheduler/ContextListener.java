/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduler;

import Scheduler.ScheduledWorks;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *  Scheduler ContextListener
 *
 * @author alvar
 */
public class ContextListener implements ServletContextListener {

    ConfigurableApplicationContext applicationContext = null;
    ScheduledWorks scheduler = null;
    
    /**
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
       
        
        if(applicationContext == null)
        {
            applicationContext  = new ClassPathXmlApplicationContext("sga_config.xml");
        }
        
        

        String estado = "Scheduler no iniciado";
        
        if(applicationContext != null)
        {
            try{
                scheduler           = applicationContext.getBean(ScheduledWorks.class);

                if(scheduler!= null)
                {
                    estado = "Scheduler : " + scheduler.isRunning();
                }
            }
            catch(BeansException ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.CONFIG, null, ex);
            }
        }
        
        System.err.println("--------------------------------------------------------------");
        System.err.println("Contexto iniciado: " + estado);
        System.err.println("--------------------------------------------------------------");

    }

    /**
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
        
         String estado = "No scheduler";
        
        if(applicationContext != null)
        {
           applicationContext.close();
         
           if(scheduler != null){
               estado = "Cerro: " + scheduler.isRunning();
           }
        }
        
        System.err.println("--------------------------------------------------------------");
        System.err.println("Contexto detenido: " + estado);
        System.err.println("--------------------------------------------------------------");
    }
    
}
