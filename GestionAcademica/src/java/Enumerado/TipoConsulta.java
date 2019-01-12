/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado TipoConsulta
 *
 * @author Alvaro
 */
public enum TipoConsulta {
    CONSULTA("Consulta", 1), 
    INC_DESTINATARIO("Destinatarios a incluir", 2),
    EXC_DESTINATARIO("Destinatarios a excluir", 3);
    
    TipoConsulta(){
        
    }
    
    private int valor;
    private String nombre;

    TipoConsulta(String pNombre, int pValor) {
        this.valor = pValor;
        this.nombre = pNombre;
    }

    /**
     *
     * @return Retorna el valor de TipoConsulta
     */
    public int getValor() {
        return valor;
    }
    
    /**
     *
     * @return Retorna el nombre de TipoConsulta
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     *
     * @param pCod Recibe el código de TipoConsulta
     * @return Retorna TipoConsulta dado el código recibido
     */
    public static TipoConsulta fromCode(int pCod) {
        for (TipoConsulta tpoCns  : TipoConsulta.values()){
            if (tpoCns.getValor() == pCod){
                return tpoCns;
            }
        }
        throw new UnsupportedOperationException(
                "El tipo de consulta " + pCod + " is not supported!");
    }
    
}
