/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado TipoPeriodo
 *
 * @author Alvaro
 */
public enum TipoPeriodo {
    MENSUAL("Mes", 1), SEMESTRAL("Semestre", 2), ANUAL("Año", 3), MODULAR("Módulo", 4);
    
    TipoPeriodo(){
        
    }
    
    private String vTpoPerNom;
    private int vTpoPerCod;

    TipoPeriodo(String tpoPerNom, int tpoPerCod) {
        this.vTpoPerCod = tpoPerCod;
        this.vTpoPerNom = tpoPerNom;
    }

    /**
     *
     * @return Retorna el TipoPeriodo
     */
    public int getTipoPeriodo() {
        return vTpoPerCod;
    }
    
    /**
     *
     * @return Retorna el nombre de TipoPeriodo
     */
    public String getTipoPeriodoNombre() {
        return vTpoPerNom;
    }
    
    /**
     *
     * @param tpoPerCod Recibe el código de TipoPeriodo
     * @return Retorna TipoPeriodo dado el código recibido
     */
    public static TipoPeriodo fromCode(int tpoPerCod) {
        for (TipoPeriodo tipoPeriodo :TipoPeriodo.values()){
            if (tipoPeriodo.getTipoPeriodo() == tpoPerCod){
                return tipoPeriodo;
            }
        }
        throw new UnsupportedOperationException(
                "El tipo de periodo " + tpoPerCod + " is not supported!");
    }
    
}
