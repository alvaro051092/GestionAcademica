/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/** 
 * Enumerado Genero
 *
 * @author Alvaro
 */
public enum Genero {
    FEMENINO("Femenino"),
    INDEFINIDO("Indefinido"),
    MASCULINO("Masculino");
    
    Genero(){
        
    }
    
    private String valor;

    Genero(String  pValor) {
        this.valor = pValor;
    }

    /**
     *
     * @return Retorna el genero
     */
    public String getValor() {
        return valor;
    }
    
    /**
     *
     * @param pCod Recibe el código
     * @return Retorna el genero
     */
    public static Genero fromCode(String pCod) {
        for (Genero objeto  : Genero.values()){
            if (objeto.getValor().equals(pCod)){
                return objeto;
            }
        }
        throw new UnsupportedOperationException(
                "El generos " + pCod + " is not supported!");
    }
    
}
