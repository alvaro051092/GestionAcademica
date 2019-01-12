/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado TipoMensaje
 *
 * @author Alvaro
 */
public enum TipoMensaje {
    ERROR("ERROR"), 
    ADVERTENCIA("ADVERTENCIA"),
    MENSAJE("MENSAJE");

    TipoMensaje(){
        
    }
    
    private String vValor;

    TipoMensaje(String pValor) {
        this.vValor = pValor;
    }

    /**
     *
     * @return Retorna el valor de TipoMensaje
     */
    public String getValor() {
        return vValor;
    }
    
}
