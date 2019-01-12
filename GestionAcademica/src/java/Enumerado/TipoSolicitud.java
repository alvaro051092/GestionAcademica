/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado TipoSolicitud
 *
 * @author Alvaro
 */
public enum TipoSolicitud {
    ESCOLARIDAD("Escolaridad", 1), 
    CONSTANCIA_ESTUDIO("Constancia de estudio", 2), 
    DUPLICADO_FACTURA("Duplicado de factura", 3);
    
    TipoSolicitud(){
        
    }
    
    private int vTpoSol;
    private String vTpoSolNom;

    TipoSolicitud(String pTpoSolNom, int pTpoSol) {
        this.vTpoSol = pTpoSol;
        this.vTpoSolNom = pTpoSolNom;
    }

    /**
     *
     * @return Retorna el TipoSolicitud
     */
    public int getTipoSolicitud() {
        return vTpoSol;
    }
    
    /**
     *
     * @return Retorna el nombre TipoSolicitud
     */
    public String getNombre() {
        return vTpoSolNom;
    }
    
    /**
     *
     * @param pCod Recibe el código de TipoSolicitud
     * @return Retorna el TipoSolicitud dado el código recibido
     */
    public static TipoSolicitud fromCode(int pCod) {
        for (TipoSolicitud tpoSol :TipoSolicitud.values()){
            if (tpoSol.getTipoSolicitud()== pCod){
                return tpoSol;
            }
        }
        throw new UnsupportedOperationException(
                "El tipo de solicitud " + pCod + " is not supported!");
    }
    
}
