/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica.Notificacion;

import Entidad.NotificacionBandeja;
import Enumerado.BandejaEstado;
import Enumerado.BandejaTipo;
import Enumerado.TipoMensaje;
import Logica.LoBandeja;
import SDT.SDT_NotificacionEnvio;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;

/**
 *
 * @author alvar
 */
public class NotificacionWeb {

    /**
     * Notificar por web
     */
    public NotificacionWeb() {
    }
    
    /**
     * Crea notificacion web
     * @param notificacion Notificacion
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj Notificar(SDT_NotificacionEnvio notificacion)
    {
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Web", TipoMensaje.MENSAJE));
        
        if(notificacion.getDestinatario() == null)
        {
            retorno.setMensaje(new Mensajes("No se reibió destinatario", TipoMensaje.ERROR));
        }
        else
        {
            if(notificacion.getDestinatario().getPersona() == null)
            {
                retorno.setMensaje(new Mensajes("No se reibió destinatario", TipoMensaje.ERROR));
            }
        }
        
        if(!retorno.SurgioError())
        {
            NotificacionBandeja bandeja = new NotificacionBandeja();

            bandeja.setDestinatario(notificacion.getDestinatario().getPersona());
            bandeja.setNotBanAsu(notificacion.getAsunto());
            bandeja.setNotBanEst(BandejaEstado.SIN_LEER);
            bandeja.setNotBanMen(notificacion.getContenido());
            bandeja.setNotBanTpo(BandejaTipo.WEB);

            retorno = (Retorno_MsgObj) LoBandeja.GetInstancia().guardar(bandeja);
            
            if(!retorno.SurgioError())
            {
                retorno.setMensaje(new Mensajes("Ok", TipoMensaje.MENSAJE));
            }
        }
        return retorno;
    }
}
