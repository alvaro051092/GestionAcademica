/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDT;

import Entidad.NotificacionDestinatario;

/**
 * SDT SDT_NotificacionEnvio
 *
 * @author alvar
 */
public class SDT_NotificacionEnvio {
    private Boolean NotApp;
    private Boolean NotWeb;
    private Boolean NotEml;
    private NotificacionDestinatario destinatario;
    private String contenido;
    private String asunto;
    private Boolean esHTML;

    public SDT_NotificacionEnvio() {
    }

    public SDT_NotificacionEnvio(Boolean NotApp, Boolean NotWeb, Boolean NotEml, NotificacionDestinatario destinatario, String contenido, String asunto, Boolean pHtml) {
        this.NotApp = NotApp;
        this.NotWeb = NotWeb;
        this.NotEml = NotEml;
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.asunto = asunto;
        this.esHTML = pHtml;
        
    }

    /**
     *
     * @return Retorna si notifica por la aplicación android o no
     */
    public Boolean getNotApp() {
        return NotApp;
    }

    /**
     *
     * @param NotApp Recibe si notifica por la aplicación android o no
     */
    public void setNotApp(Boolean NotApp) {
        this.NotApp = NotApp;
    }

    /**
     *
     * @return Retorna si notifica por la web o no
     */
    public Boolean getNotWeb() {
        return NotWeb;
    }

    /**
     *
     * @param NotWeb Recibe si notifica por la web o no
     */
    public void setNotWeb(Boolean NotWeb) {
        this.NotWeb = NotWeb;
    }

    /**
     *
     * @return Retorna si notifica por Email o no
     */
    public Boolean getNotEml() {
        return NotEml;
    }

    /**
     *
     * @param NotEml Recibe si notifica por Email o no
     */
    public void setNotEml(Boolean NotEml) {
        this.NotEml = NotEml;
    }

    /**
     *
     * @return retorna el destinatario
     */
    public NotificacionDestinatario getDestinatario() {
        return destinatario;
    }

    /**
     *
     * @param destinatario recibe destinatario
     */
    public void setDestinatario(NotificacionDestinatario destinatario) {
        this.destinatario = destinatario;
    }

    /**
     *
     * @return Retorna contenido
     */
    public String getContenido() {
        return contenido;
    }

    /**
     *
     * @param contenido Recibe contenido
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     *
     * @return Retorna Asunto
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     *
     * @param asunto Recibe Asunto
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /**
     *
     * @return Retorna esHTML
     */
    public Boolean getEsHTML() {
        return esHTML;
    }

    /**
     *
     * @param esHTML Recibe esHTML
     */
    public void setEsHTML(Boolean esHTML) {
        this.esHTML = esHTML;
    }
    
    
    
}
