/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado TipoCampo
 *
 * @author Alvaro
 */
public enum TipoCampo {
    INT(1), STRING(2), LONG(3);
    
    TipoCampo(){
        
    }
    
    private int valor;

    TipoCampo(int pValor) {
        this.valor = pValor;
    }

    /**
     *
     * @return Retorna el valor de TipoCampo
     */
    public int getValor() {
        return valor;
    }
    
}
