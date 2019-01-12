/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduler;

import Logica.LoPeriodo;
import Logica.LoSincronizacion;
import Logica.LoWS;
import Logica.Notificacion.ManejoNotificacion;
import Logica.Notificacion.NotificacionesInternas;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.SmartLifecycle;

/**
 * Scheduler ScheduledWorks
 *
 * @author alvar
 */

public class ScheduledWorks implements SmartLifecycle{
    
    private Integer diasBefore;

    /**
     *
     * @return Retorna Dias antes
     */
    public Integer getDiasBefore() {
        return diasBefore;
    }

    /**
     *
     * @param diasBefore Recibe Días antes
     */
    public void setDiasBefore(Integer diasBefore) {
        this.diasBefore = diasBefore;
    }
    
    public ScheduledWorks() {
        
    }
    
    private boolean isRunning = false;
    
    /**
     *  Método Notificar
     */
    public void Tarea_Notificar()
    {
        System.out.println("Notificar. Current time is :: "+ new Date());
        
        ManejoNotificacion notManager = new ManejoNotificacion();
        notManager.EjecutarNotificacionAutomaticamente();
        
    }
    
    /**
     *  Método Notificar Interno
     */
    public void Tarea_NotificarInterno()
    {
        System.out.println("Notificar interno. Current time is :: "+ new Date());
        
        NotificacionesInternas noInt = new NotificacionesInternas();
        noInt.EjecutarNotificacionesInternas();
    }
    
    /**
     * Método Borrar WS Bitácora
     */
    public void Tarea_BorrarWSBitacora()
    {
        System.out.println("Borrar WSBitacora. Current time is :: "+ new Date());
        LoWS.GetInstancia().EliminarBitacoraBeforeDate();
    }
    
    /**
     *  Método Sincronizar
     */
    public void Tarea_Sincronizar()
    {
        System.out.println("Sincronizar. Current time is :: "+ new Date());
        LoSincronizacion.GetInstancia().Sincronizar();
    }
    
    /**
     *  Método Importar Adjuntos
     */
    public void Tarea_ImportarAdjuntos(){
        System.out.println("Importar adjuntos de moodle. Current time is :: "+ new Date());
        LoPeriodo.GetInstancia().DocumentoImportarMoodle();
    }

    /**
     *  Método Comienzo
     */
    @Override
    public void start() {
        System.err.println("Iniciando scheduler");
        
        this.isRunning = true;
    }
 
    /**
     *  Método Detener
     */
    @Override
    public void stop() {
        System.err.println("Deteniendo scheduler");
        isRunning = false;
    }
 
    /**
     *
     * @param callback
     */
    @Override
    public void stop(final Runnable callback) {
        System.err.println("Deteniendo scheduler callback");
        isRunning = false;
 
        try {
            //Stop listening to the queue.
 
            //Sleeping for 120 seconds so that all threads 
            //get enough time to do their cleanup  
            TimeUnit.SECONDS.sleep(120);
 
            //Shudown complete. Regular shutdown will continue.
            callback.run();
        } catch (final InterruptedException e) {
            //Looks like we got exception while shutting down, 
            //log it or do something with it
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
 
    /**
     * This is the most important method. 
     * Returning Integer.MAX_VALUE only suggests that 
     * we will be the first bean to shutdown.
     * @return 
     */
    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isAutoStartup() {
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isRunning() {
        return isRunning;
    }
    
}
