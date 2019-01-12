/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado TipoAutenticacion
 *
 * @author Alvaro
 */
public enum TipoAutenticacion {
    NORMAL("Normal Password",0), 
    ENCRYPTED("Encrypted Password",1),
    KERBEROS("Kerberos/GSSAPI",2),
    NTLM("NTLM",3);
    
    TipoAutenticacion(){
        
    }
    
    private int vCod;
    private String vNom;

    TipoAutenticacion(String pNom, int pCod) {
        this.vCod = pCod;
        this.vNom = pNom;
    }

    /**
     *
     * @return Retorna el código de TipoAutenticacion 
    */
    public int getCod() {
        return vCod;
    }

    /**
     *
     * @return Retorna el nombre de TipoAutenticacion
     */
    public String getNom() {
        return vNom;
    }
     
    /**
     *
     * @param pCod Recibe el código de TipoAutenticacion
     * @return Retorna TipoAutenticacion dado el código recibidos
     */
    public static TipoAutenticacion fromCode(int pCod) {
        for (TipoAutenticacion tpoAut :TipoAutenticacion.values()){
            if (tpoAut.getCod() == pCod){
                return tpoAut;
            }
        }
        throw new UnsupportedOperationException(
                "El tipo de autenticacion " + pCod + " is not supported!");
    }
    
}
