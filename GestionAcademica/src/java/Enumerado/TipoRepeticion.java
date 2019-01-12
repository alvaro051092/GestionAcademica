/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado TipoRepeticion
 *
 * @author Alvaro
 */
public enum TipoRepeticion {
    SIN_REPETICION("Sin repetición", 0), 
    MINUTOS("Minutos", 1), 
    HORAS("Horas", 2), 
    DIAS("Dias", 3), 
    SEMANAS("Semanas", 4), 
    MESES("Meses", 5), 
    ANIOS("Años", 6);
    
    TipoRepeticion(){
        
    }
    
    private int valor;
    private String nombre;

    TipoRepeticion(String pNombre, int pValor) {
        this.valor  = pValor;
        this.nombre = pNombre;
    }

    /**
     *
     * @return Retorno valor TipoRepeticion
     */
    public int getValor() {
        return valor;
    }
    
    /**
     *
     * @return Retorno el nombre TipoRepeticion
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     *
     * @param pCod Recibe el código de TipoRepeticion
     * @return Retorna TipoRepeticion dado el código recibido
     */
    public static TipoRepeticion fromCode(int pCod) {
        for (TipoRepeticion tpoRep  : TipoRepeticion.values()){
            if (tpoRep.getValor() == pCod){
                return tpoRep;
            }
        }
        throw new UnsupportedOperationException(
                "El tipo de Repeticion " + pCod + " is not supported!");
    }
}
