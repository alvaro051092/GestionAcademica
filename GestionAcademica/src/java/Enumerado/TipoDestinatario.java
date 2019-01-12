/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado TipoDestinatario
 *
 * @author Alvaro
 */
public enum TipoDestinatario {
    PERSONA("Persona", 1), 
    EMAIL("Email", 2);
    
    TipoDestinatario(){
        
    }
    
    private int valor;
    private String nombre;

    TipoDestinatario(String pNombre, int pValor) {
        this.valor = pValor;
        this.nombre = pNombre;
    }

    /**
     *
     * @return Retorna el valor TipoDestinatario
     */
    public int getValor() {
        return valor;
    }
    
    /**
     *
     * @return Retorna el nombre de TipoDestinatario
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     *
     * @param pCod Recibe el código de TipoDestinatario
     * @return Retorna TipoDestinatario dado el código recibido
     */
    public static TipoDestinatario fromCode(int pCod) {
        for (TipoDestinatario objeto  : TipoDestinatario.values()){
            if (objeto.getValor() == pCod){
                return objeto;
            }
        }
        throw new UnsupportedOperationException(
                "El tipo de destinatario " + pCod + " is not supported!");
    }
    
}
