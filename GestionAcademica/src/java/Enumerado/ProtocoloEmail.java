/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado ProtocoloEmail
 *
 * @author Alvaro
 */
public enum ProtocoloEmail {
    SMTP("SMTP", 1), 
    EWS("EWS",2);
    
    ProtocoloEmail(){
        
    }
    
    private String vProtocoloNom;
    private int vProtocoloCod;

    ProtocoloEmail(String pProtocoloNom, int pProtocoloCod) {
        this.vProtocoloNom = pProtocoloNom;
        this.vProtocoloCod = pProtocoloCod;
   }

    /**
     *
     * @return Retorna el código de ProtocoloEmail
     */
    public int getCod() {
        return vProtocoloCod;
    }
    
    /**
     *
     * @return Retorna el nombre de ProtocoloEmail
     */
    public String getNom()
    {
        return this.vProtocoloNom;
    }
    
    /**
     *
     * @param pCod Recibe el código de ProtocoloEmail
     * @return Retorna ProtocoloEmail dado el código recibido
     */
    public static ProtocoloEmail fromCode(int pCod) {
        for (ProtocoloEmail protocoloEmail :ProtocoloEmail.values()){
            if (protocoloEmail.getCod() == pCod){
                return protocoloEmail;
            }
        }
        throw new UnsupportedOperationException(
                "El protocolo de email " + pCod + " is not supported!");
    }
    
    /**
     *
     * @param pCod Recibe el código de ProtocoloEmail
     * @return Valida el protocolo de Email
     */
    public static boolean ValidarProtocoloEmail(int pCod)
    {
        for (ProtocoloEmail protocoloEmail :ProtocoloEmail.values()){
            if (protocoloEmail.getCod() == pCod){
                return true;
            }
        }
        throw new UnsupportedOperationException(
                "El protocolo de email " + pCod + " is not supported!");
        
    }
}
