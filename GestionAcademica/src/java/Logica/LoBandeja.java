/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Entidad.Notificacion;
import Entidad.NotificacionBandeja;
import Entidad.NotificacionDestinatario;
import Enumerado.BandejaEstado;
import Enumerado.BandejaTipo;
import Enumerado.ObtenerDestinatario;
import Enumerado.TipoEnvio;
import Interfaz.InABMGenerico;
import Logica.Notificacion.ManejoNotificacion;
import Persistencia.PerManejador;
import SDT.SDT_Parameters;
import Utiles.Retorno_MsgObj;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author alvar
 */
public class LoBandeja implements InABMGenerico{

    private static LoBandeja instancia;

    private LoBandeja() {
    }
    
    /**
     * Obtener instancia de la clase
     * @return Instancia de la clase
     */
    public static LoBandeja GetInstancia(){
        if (instancia==null)
        {
            instancia   = new LoBandeja();
        }

        return instancia;
    }
    
    /**
     * Guardar Bandeja
     * @param pObjeto Bandeja
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object guardar(Object pObjeto) {
        
        NotificacionBandeja bandeja = (NotificacionBandeja) pObjeto;
        
        bandeja.setNotBanFch(new Date());
        
        PerManejador perManejador   = new PerManejador();
        Retorno_MsgObj retorno      = perManejador.guardar(bandeja);

        if(!retorno.SurgioError())
        {
            bandeja.setNotBanCod((Long) retorno.getObjeto());
            retorno.setObjeto(bandeja);
        }
        
        return retorno;
    }

    /**
     * Actualizar bandeja
     * @param pObjeto Bandeja
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object actualizar(Object pObjeto) {
        
        PerManejador perManejador   = new PerManejador();

        return perManejador.actualizar(pObjeto);
    }

    /**
     * Eliminar bandeja
     * @param pObjeto Bandeja
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object eliminar(Object pObjeto) {
        PerManejador perManejador   = new PerManejador();
        return perManejador.eliminar(pObjeto);
    }

    /**
     * Obtener bandeja 
     * @param pObjeto Long - NotBanCod
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtener(Object pObjeto) {
        
        PerManejador perManejador   = new PerManejador();
        
        return perManejador.obtener((Long) pObjeto, NotificacionBandeja.class);
    }

    /**
     * Obtener lista de bandejas
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtenerLista() {
        PerManejador perManejador   = new PerManejador();
        
        return perManejador.obtenerLista("NotificacionBandeja.findAll", null);
    }
    
    /**
     * Obtener lista de bandejas, a partir del tipo, persona y estado
     * @param PerCod Código de persona
     * @param NotBanTpo Tipo de bandeja
     * @param NotBanEst Estado
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj obtenerListaByTipoEstado(Long PerCod, BandejaTipo NotBanTpo, BandejaEstado NotBanEst) {
        PerManejador perManejador   = new PerManejador();
        
        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(PerCod, "PerCod"));
        lstParametros.add(new SDT_Parameters(NotBanTpo, "NotBanTpo"));
        lstParametros.add(new SDT_Parameters(NotBanEst, "NotBanEst"));
        
        return perManejador.obtenerLista("NotificacionBandeja.findByTpoEst", lstParametros);
    }
    
    /** Notificar pendientes.
     * <p>Cuando una persona inicia sesión, se buscan sus notificaciones pendientes y se las envia a  su dispositivo</p>      
     * @param PerCod Código de la persona que inicia sesión
     */
    public void NotificarPendientes(Long PerCod){
        Retorno_MsgObj retorno = this.obtenerListaByTipoEstado(PerCod, BandejaTipo.APP, BandejaEstado.SIN_LEER);
        
        if(!retorno.SurgioError())
        {
            for(Object objeto : retorno.getLstObjetos())
            {
                NotificacionBandeja bandeja = (NotificacionBandeja) objeto;
                
                Notificacion notificacion = new Notificacion();
                
                notificacion.setNotApp(Boolean.TRUE);
                notificacion.setNotAsu(bandeja.getNotBanAsu());
                notificacion.setNotCon(bandeja.getNotBanMen());
                notificacion.setNotTpoEnv(TipoEnvio.COMUN);
                notificacion.setNotObtDest(ObtenerDestinatario.UNICA_VEZ);
                
                NotificacionDestinatario destinatario = new NotificacionDestinatario();
                destinatario.setNotificacion(notificacion);
                destinatario.setPersona(bandeja.getDestinatario());
                
                notificacion.setLstDestinatario(new ArrayList<NotificacionDestinatario>());
                
                notificacion.getLstDestinatario().add(destinatario);
                
                ManejoNotificacion manager = new ManejoNotificacion();
                manager.EjecutarNotificacion(notificacion);
                  
                  
               bandeja.setNotBanEst(BandejaEstado.LEIDA);
               this.actualizar(bandeja);
                  
            }
        }
        
    }
    
   
}
