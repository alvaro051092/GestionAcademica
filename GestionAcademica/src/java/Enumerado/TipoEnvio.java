/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado TipoEnvio
 *
 * @author Alvaro
 */
public enum TipoEnvio {
    COMUN("Común", 1), 
    AGRUPADO("Agrupado", 2);
    
    TipoEnvio(){
        
    }
    
    private int valor;
    private String nombre;

    TipoEnvio(String pNom, int pValor) {
        this.valor = pValor;
        this.nombre = pNom;
    }

    /**
     *
     * @return
     */
    public int getValor() {
        return valor;
    }
    
    /**
     *
     * @return Retorna el nombre de TipoEnvio
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     *
     * @param pCod Recibe el código de TipoEnvio
     * @return Retorna TipoEnvio dado el código recibido
     */
    public static TipoEnvio fromCode(int pCod) {
        for (TipoEnvio tpoEnv  : TipoEnvio.values()){
            if (tpoEnv.getValor() == pCod){
                return tpoEnv;
            }
        }
        throw new UnsupportedOperationException(
                "El tipo de envio " + pCod + " is not supported!");
    }
    
}
