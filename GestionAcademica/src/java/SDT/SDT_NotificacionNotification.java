/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDT;

/**
 * SDT SDT_NotificacionNotification
 *
 * @author alvar
 */
public class SDT_NotificacionNotification {
    private String body;
    private String title;
    private String icon;
    private String sound;

    public SDT_NotificacionNotification(String body, String title, String icon, String sound) {
        this.body = body;
        this.title = title;
        this.icon = icon;
        this.sound = sound;
    }

    /**
     *
     * @return Retorna el cuerpo
     */
    public String getBody() {
        return body;
    }

    /**
     *
     * @param body recibe el cuerpo
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     *
     * @return Retorna el titulo
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title Recibe el titulo
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return Retorna el Icono
     */
    public String getIcon() {
        return icon;
    }

    /**
     *
     * @param icon Recibe el Icono
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     *
     * @return Retorna el Sound
     */
    public String getSound() {
        return sound;
    }

    /**
     *
     * @param sound Recibe el Sound
     */
    public void setSound(String sound) {
        this.sound = sound;
    }

    
    
    
}
