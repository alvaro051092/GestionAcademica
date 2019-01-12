/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica.Notificacion;

import Logica.LoBandeja;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alvar
 */
public class AsyncBandeja extends Thread {
        /** The command agent to execute. */
        private final Long perCod;
 
        /**
         * Constructor
         * 
     * @param pCod
         */
        public AsyncBandeja(Long pCod) {
            this.perCod = pCod;
        }
 
        /**
         * Run the thread.
         */
        @Override
        public void run() {
            try {
                
               LoBandeja.GetInstancia().NotificarPendientes(this.perCod);
               
            } catch (Exception e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            }
        }
}