/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utiles;

import Enumerado.TipoMensaje;

/**
 * Utilidades Mensajes
 *
 * @author alvar
 */
public class Mensajes {
    private String mensaje;
    private TipoMensaje tipoMensaje;

    /**
     *
     * @param mensaje
     * @param tipoMensaje
     */
    public Mensajes(String mensaje, TipoMensaje tipoMensaje) {
        this.mensaje = mensaje;
        this.tipoMensaje = tipoMensaje;
    }

    public Mensajes() {
    }

    /**
     *
     * @return Retorno un mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     *
     * @param mensaje Recibe un mensaje
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     *
     * @return Retorna el tipo de mensaje
     */
    public TipoMensaje getTipoMensaje() {
        return tipoMensaje;
    }

    /**
     *
     * @param tipoMensaje Recibe el tipo de mensaje
     */
    public void setTipoMensaje(TipoMensaje tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    @Override
    public String toString() {
        return tipoMensaje + ": " + mensaje;
    }
    
    
    
}
