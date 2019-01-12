/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDT;

import Enumerado.TipoDestinatario;

/**
 * SDT SDT_Destinatario
 *
 * @author alvar
 */
public class SDT_Destinatario {
    
    private Long PerCod;
    private String Email;
    private TipoDestinatario tipoDestinatario;

    public SDT_Destinatario() {
    }

    /**
     *
     * @return Retorna Código de Persona
     */
    public Long getPerCod() {
        return PerCod;
    }

    /**
     *
     * @param PerCod Recibe Código de Persona
     */
    public void setPerCod(Long PerCod) {
        this.PerCod = PerCod;
    }

    /**
     *
     * @return Retorna Email de Persona
     */
    public String getEmail() {
        return Email;
    }

    /**
     *
     * @param Email Recibe Email de Persona
     */
    public void setEmail(String Email) {
        this.Email = Email;
    }

    /**
     *
     * @return Retorna Tipo de Destinatario
     */
    public TipoDestinatario getTipoDestinatario() {
        return tipoDestinatario;
    }

    /**
     *
     * @param tipoDestinatario Recibe Tipo de Destinatario
     */
    public void setTipoDestinatario(TipoDestinatario tipoDestinatario) {
        this.tipoDestinatario = tipoDestinatario;
    }

    
}
