/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado Expresiones regulares
 *
 * @author Alvaro
 */
public enum ExpresionesRegulares {
    BOOLEAN("^(true|false|0|1)$"), 
    NUMERO_ENTERO("^(0|[1-9][0-9]*)$"),
    URL("^https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)$")
    ;

    ExpresionesRegulares(){
        
    }
    
    private String vValor;

    ExpresionesRegulares(String pValor) {
        this.vValor = pValor;
    }

    /**
     *
     * @return Retorna el valor de la expresión regular
     */
    public String getValor() {
        return vValor;
    }
    
}
