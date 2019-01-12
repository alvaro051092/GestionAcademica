/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado EstadoServicio
 *
 * @author Alvaro
 */
public enum EstadoServicio {
    CORRECTO(1), CON_ERRORES(2);
    
    EstadoServicio(){
        
    }
    
    private int valor;

    EstadoServicio(int pValor) {
        this.valor = pValor;
    }

    /**
     *
     * @return Retorna el valor EstadoServicio
     */
    public int getValor() {
        return valor;
    }
    
}
