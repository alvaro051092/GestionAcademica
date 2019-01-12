/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado NotificacionEstado
 *
 * @author Alvaro
 */
public enum NotificacionEstado {
    ENVIO_CORRECTO("Envío correcto", 1), 
    ENVIO_CON_ERRORES("Envío con errores", 2), 
    ENVIO_EN_PROGRESO("Envío en progreso", 3);
    
    NotificacionEstado(){
        
    }
    
    private int valor;
    private String nombre;

    NotificacionEstado(String pNombre, int pValor) {
        this.valor = pValor;
        this.nombre = pNombre;
    }

    /**
     *
     * @return Retorna el valor de NotificacionEstado
     */
    public int getValor() {
        return valor;
    }
    
    /**
     *
     * @return Retorna el nombre de NotificacionEstado
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     *
     * @param pCod Recibe el código de NotificacionEstado
     * @return Retorna NotificacionEstado dado el código recibido
     */
    public static NotificacionEstado fromCode(int pCod) {
        for (NotificacionEstado objeto  : NotificacionEstado.values()){
            if (objeto.getValor() == pCod){
                return objeto;
            }
        }
        throw new UnsupportedOperationException(
                "El notificacion estado " + pCod + " is not supported!");
    }
    
}
