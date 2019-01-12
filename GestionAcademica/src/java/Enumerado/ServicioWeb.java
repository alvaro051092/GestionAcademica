/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado ServicioWeb
 *
 * @author Alvaro
 */
public enum ServicioWeb {
    LOGIN(1), 
    EVALUACION_ALUMNO(2), 
    ESTUDIOS(3),
    PERSONA(4),
    SOLICITUDES(5),
    SINCRONIZAR(6);
    
    ServicioWeb(){
        
    }
    
    private int valor;

    ServicioWeb(int pValor) {
        this.valor = pValor;
    }

    /**
     *
     * @return Retorna el valor de ServicioWeb
     */
    public int getValor() {
        return valor;
    }
    
}
