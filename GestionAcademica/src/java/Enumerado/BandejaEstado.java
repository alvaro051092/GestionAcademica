/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado BandejaEstado
 *
 * @author Alvaro
 */
public enum BandejaEstado {
    SIN_LEER("Sin leer",1),
    LEIDA("Leida",2),
    ERROR("Error",3);
    
    private int vCod;
    private String vNom;

    BandejaEstado(String pNom, int pCod) {
        this.vCod = pCod;
        this.vNom = pNom;
    }

    private BandejaEstado() {
    }
    
    /**
     *
     * @return Retorna el código de BandejaEstado
     */
    public int getCod() {
        return vCod;
    }
    
    /**
     *
     * @return Retornal el nombre de BandejaEstado
     */
    public String getNom() {
        return vNom;
    }
    
    /**
     *
     * @param pCod Recibe el código de BandejaEstado
     * @return Retorna BandejaEstado dado el código recibido
     */
    public static BandejaEstado fromCode(int pCod) {
        for (BandejaEstado objeto :BandejaEstado.values()){
            if (objeto.getCod() == pCod){
                return objeto;
            }
        }
        throw new UnsupportedOperationException(
                "El objeto " + pCod + " is not supported!");
    }
    
}
