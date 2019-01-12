/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado EstadoInconsistencia
 *
 * @author Alvaro
 */
public enum EstadoInconsistencia {
    CORRECTO(1), CON_ERRORES(2);
    
    EstadoInconsistencia(){
        
    }
    
    private int valor;

    EstadoInconsistencia(int pValor) {
        this.valor = pValor;
    }

    /**
     *
     * @return Retorna el valor de EstadoInconsistencia
     */
    public int getValor() {
        return valor;
    }
    
}
