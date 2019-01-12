/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado EstadoCalendarioEvaluacion
 *
 * @author Alvaro
 */
public enum EstadoCalendarioEvaluacion {
    SIN_CALIFICAR("Sin calificar", 1), 
    CALIFICADO("Calificado", 2), 
    PENDIENTE_VALIDACION("Pendiente de validación", 3), 
    PENDIENTE_CORRECCION("Pendiente de corrección", 4), 
    VALIDADO("Validado", 5);
    
    EstadoCalendarioEvaluacion(){
        
    }
    
    private String vEstadoNom;
    private int vEstado;

    EstadoCalendarioEvaluacion(String estadoNom, int pEstado) {
        this.vEstado = pEstado;
        this.vEstadoNom = estadoNom;
    }

    /**
     *
     * @return Retorna el valor de EstadoCalendarioEvaluacion
     */
    public int getEstado() {
        return vEstado;
    }
    
    /**
     *
     * @return Retorna el nombre de EstadoCalendarioEvaluacion
     */
    public String getEstadoNombre() {
        return vEstadoNom;
    }
    
    /**
     *
     * @param cod Recibe el código de EstadoCalendarioEvaluacion
     * @return Retorna el EstadoCalendarioEvaluacion dado el código recibido
     */
    public static EstadoCalendarioEvaluacion fromCode(int cod) {
        for (EstadoCalendarioEvaluacion estado :EstadoCalendarioEvaluacion.values()){
            if (estado.getEstado() == cod){
                return estado;
            }
        }
        throw new UnsupportedOperationException(
                "El estado " + cod + " is not supported!");
    }
}
