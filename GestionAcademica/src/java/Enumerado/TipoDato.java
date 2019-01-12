/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado TipoDato
 *
 * @author Alvaro
 */
public enum TipoDato {
    NUMERO_ENTERO(1), 
    BOOLEAN(2), 
    URL(3);
    
    TipoDato(){
        
    }
    
    private int valor;

    TipoDato(int pValor) {
        this.valor = pValor;
    }

    /**
     *
     * @return Retorna el valor TipoDato
     */
    public int getValor() {
        return valor;
    }
    
}
