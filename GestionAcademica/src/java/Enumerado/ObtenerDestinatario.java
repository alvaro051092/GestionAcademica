/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado ObtenerDestinatario
 *
 * @author Alvaro
 */
public enum ObtenerDestinatario {
    POR_CADA_REGISTRO("Por cada registro", 1), 
    UNICA_VEZ("Única vez", 2);
    
    ObtenerDestinatario(){
        
    }
    
    private int valor;
    private String nombre;

    ObtenerDestinatario(String pNom, int pValor) {
        this.valor = pValor;
        this.nombre = pNom;
    }

    /**
     *
     * @return Retorna el valor de ObtenerDestinatario
     */
    public int getValor() {
        return valor;
    }
    
    /**
     *
     * @return Retorna el nombre de ObtenerDestinatario
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     *
     * @param pCod Recibe el código de ObtenerDestinatario
     * @return Retorna ObtenerDestinatario dado el código recibido
     */
    public static ObtenerDestinatario fromCode(int pCod) {
        for (ObtenerDestinatario obtDest  : ObtenerDestinatario.values()){
            if (obtDest.getValor() == pCod){
                return obtDest;
            }
        }
        throw new UnsupportedOperationException(
                "El tipo de Obtener Destinatario " + pCod + " is not supported!");
    }
    
}
