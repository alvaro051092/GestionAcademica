/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDT;

import java.io.Serializable;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * SDT SDT_Notificacion
 *
 * @author alvar
 */

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
        
public class SDT_Notificacion implements Serializable{
    private String to;
    private SDT_NotificacionDato data;
    private SDT_NotificacionNotification notification;
    
    public SDT_Notificacion() {
    }

    /**
     *
     * @return Retorno to
     */
    public String getTo() {
        return to;
    }

    /**
     *
     * @param to Recibe To
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     *
     * @return Retorna Dato
     */
    public SDT_NotificacionDato getData() {
        return data;
    }

    /**
     *
     * @param data Recibe Dato
     */
    public void setData(SDT_NotificacionDato data) {
        this.data = data;
    }

    /**
     *
     * @return Retorna Notificación
     */
    public SDT_NotificacionNotification getNotification() {
        return notification;
    }

    /**
     *
     * @param notification Recibe Notificación
     */
    public void setNotification(SDT_NotificacionNotification notification) {
        this.notification = notification;
    }
    
    
    
}
