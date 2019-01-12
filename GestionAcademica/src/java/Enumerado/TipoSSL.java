/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado TipoSSL
 *
 * @author Alvaro
 */
public enum TipoSSL {
    NINGUNO("Ninguno",0),
    STARTTLS("STARTTLS",1), 
    SSL("SSL",2);
    
    private int vCod;
    private String vNom;

    TipoSSL(String pNom, int pCod) {
        this.vCod = pCod;
        this.vNom = pNom;
    }

    private TipoSSL() {
    }
    
    /**
     *
     * @return Retorna el codigo de TipoSSL
     */
    public int getCod() {
        return vCod;
    }
    
    /**
     *
     * @return Retorna el nombre de TipoSSL
     */
    public String getNom() {
        return vNom;
    }
    
    /**
     *
     * @param pCod Recibe el código de TipoSSL
     * @return Retorna TipoSSL dado el código recibido
     */
    public static TipoSSL fromCode(int pCod) {
        for (TipoSSL tpoSSL :TipoSSL.values()){
            if (tpoSSL.getCod() == pCod){
                return tpoSSL;
            }
        }
        throw new UnsupportedOperationException(
                "El tipo de ssl " + pCod + " is not supported!");
    }
    
}
