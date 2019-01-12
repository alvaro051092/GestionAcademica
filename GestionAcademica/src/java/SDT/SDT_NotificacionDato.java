/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDT;

/**
 * SDT SDT_NotificacionDato
 *
 * @author alvar
 */
public class SDT_NotificacionDato {
    private String message;
    private String titulo;

    public SDT_NotificacionDato() {
    }

    /**
     *
     * @return Retorna mensaje
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message recibe Mensaje
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return Retorna el Titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     *
     * @param titulo Recibe el titulo
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     *
     * @param message Recibe el mensaje
     * @param titulo Recibe el titulo
     */
    public SDT_NotificacionDato(String message, String titulo) {
        this.message = message;
        this.titulo = titulo;
    }

    
    
    
    
}
