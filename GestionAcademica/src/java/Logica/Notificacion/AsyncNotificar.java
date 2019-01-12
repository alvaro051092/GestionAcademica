/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica.Notificacion;

import Entidad.Notificacion;
import Enumerado.TipoNotificacion;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alvar
 */
public class AsyncNotificar extends Thread {
        /** The command agent to execute. */
        private final Notificacion not;
        private final TipoNotificacion tpoNot;
 
        /**
         * Constructor
         * 
         * @param pNot - The agent to execute.
            * @param tpoNotificacion
         */
        public AsyncNotificar(Notificacion pNot, TipoNotificacion tpoNotificacion) {
            this.not = pNot;
            this.tpoNot = tpoNotificacion;
        }
 
        /**
         * Run the thread.
         */
        @Override
        public void run() {
            try {
                
                ManejoNotificacion notManager = new ManejoNotificacion();
        
                if(tpoNot.equals(TipoNotificacion.AUTOMATICA))
                {
                    notManager.EjecutarNotificacionAutomaticamente();
                }
                if(tpoNot.equals(TipoNotificacion.A_DEMANDA))
                {
                    notManager.EjecutarNotificacion(not);                    
                }
                
            } catch (Exception e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
    }