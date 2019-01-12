/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado EstadoSolicitud
 *
 * @author Alvaro
 */
public enum EstadoSolicitud {
    SIN_TOMAR("Sin procesar", 1), 
    TOMADA("En proceso", 2), 
    FINALIZADA("Procesada", 3);
    
    EstadoSolicitud(){
    }
    
    private int vEstSol;
    private String vEstSolNom;

    EstadoSolicitud(String pEstSolNom, int pEstSol) {
        this.vEstSol = pEstSol;
        this.vEstSolNom = pEstSolNom;
    }

    /**
     *
     * @return Retirba el valor de EstadoSolicitud
     */
    public int getEstadoSolicitud() {
        return vEstSol;
    }
    
    /**
     *
     * @return Retorna el nombre del EstadoSolicitud
     */
    public String getNombre() {
        return vEstSolNom;
    }
    
    /**
     *
     * @param pCod Recibe el código de EstadoSolicitud
     * @return Retorna el EstadoSolicitud dado el codigo recibido
     */
    public static EstadoSolicitud fromCode(int pCod) {
        for (EstadoSolicitud estSol :EstadoSolicitud.values()){
            if (estSol.getEstadoSolicitud() == pCod){
                return estSol;
            }
        }
        throw new UnsupportedOperationException(
                "El estado de solicitud " + pCod + " is not supported!");
    }
    
}
