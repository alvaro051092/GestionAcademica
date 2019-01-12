/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado MoodleAuth
 *
 * @author Alvaro
 */
public enum MoodleAuth {
    MANUAL("manual"), 
    WEBSERVICE("ws"),
    EMAIL("email"),
    NO_LOGIN("nologin");
    
    MoodleAuth(){
        
    }
    
    private String valor;

    MoodleAuth(String  pValor) {
        this.valor = pValor;
    }

    /**
     *
     * @return Retorna el valor MoodleAuth
     */
    public String getValor() {
        return valor;
    }
    
    /**
     *
     * @param pCod Recibe el código de MoodleAuth
     * @return Retorna el MoodleAuth
     */
    public static MoodleAuth fromCode(String pCod) {
        for (MoodleAuth objeto  : MoodleAuth.values()){
            if (objeto.getValor().equals(pCod)){
                return objeto;
            }
        }
        throw new UnsupportedOperationException(
                "El auth " + pCod + " is not supported!");
    }
    
}
