/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import Entidad.Periodo;
import Entidad.PeriodoEstudioDocumento;
import Entidad.Persona;
import Enumerado.EstadoServicio;
import Enumerado.ServicioWeb;
import Enumerado.TipoMensaje;
import Enumerado.TipoPeriodo;
import Logica.LoCalendario;
import Logica.LoPersona;
import Logica.LoWS;
import Persistencia.PerManejador;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

/**
 * Servicio persona
 *
 * @author alvar
 */
@WebService(serviceName = "ws_persona")
public class ws_persona {
    
    @Resource WebServiceContext context;

    /**
     * Retorna usuario por codigo
     * @param pPerCod codigo de persona
     * @return objeto persona
     */
    @WebMethod(operationName = "ObtenerPersonaByCod")
    public Retorno_MsgObj ObtenerPersonaByCod(@WebParam(name = "pPerCod") Long pPerCod) {
        
        Retorno_MsgObj retorno = this.isAuthenticated();
        
        if(!retorno.SurgioError())
        {
            if(pPerCod == null)
            {
                retorno.setMensaje(new Mensajes("No se recibió parametro", TipoMensaje.ERROR));
            }
            else
            {
                retorno = LoPersona.GetInstancia().obtener(pPerCod);
                
                //LIMPIO CONTRASEÑA, NO DEBE VIAJAR POR SERVICIO
                if(!retorno.SurgioErrorObjetoRequerido())
                {
                    Persona persona = (Persona) retorno.getObjeto();
                    persona.setPerPass(null);
                    
                    retorno.setObjeto(persona);
                }
            }
        }
        
        return retorno;
    }
    
    /**
     * Retorna usuario por codigo
     * @param pUser codigo de usuario
     * @return objeto persona
     */
    @WebMethod(operationName = "ObtenerPersonaByUser")
    public Retorno_MsgObj ObtenerPersonaByUser(@WebParam(name = "pUser") String pUser) {
        Retorno_MsgObj retorno = this.isAuthenticated();
        
        if(!retorno.SurgioError())
        {
            if(pUser == null)
            {
                retorno.setMensaje(new Mensajes("No se recibió parametro", TipoMensaje.ERROR));
            }
            else
            {
                retorno = LoPersona.GetInstancia().obtenerByMdlUsr(pUser);
                
                //LIMPIO CONTRASEÑA, NO DEBE VIAJAR POR SERVICIO
                if(!retorno.SurgioErrorObjetoRequerido())
                {
                    Persona persona = (Persona) retorno.getObjeto();
                    persona.setPerPass(null);
                    
                    retorno.setObjeto(persona);
                    
                    System.err.println("Persona: " + persona.toString());
                }
            }
        }
        
        return retorno;
    }

    /**
     * Actualizar datos de la persona
     * @param pPerCod codigo de persona
     * @param PerAppTkn token de app
     * @return Resultado
     */
    @WebMethod(operationName = "PersonaActualizarToken")
    public Retorno_MsgObj PersonaActualizarToken(@WebParam(name = "pPerCod") Long pPerCod, @WebParam(name = "pPerAppTkn") String PerAppTkn) {
        //TODO write your implementation code here:
        
        Retorno_MsgObj retorno = this.isAuthenticated();
        
        if(!retorno.SurgioError())
        {
            if(pPerCod == null)
            {
                retorno.setMensaje(new Mensajes("No se recibio persona", TipoMensaje.ERROR));
            }
            else
            {
                if(PerAppTkn == null)
                {
                    retorno.setMensaje(new Mensajes("No se recibio parametro", TipoMensaje.ERROR));
                }
                else
                {
                    retorno = LoPersona.GetInstancia().ActualizarToken(pPerCod, PerAppTkn);
                }
            }
        }
        
        retorno.setObjeto(null);
        
        return retorno;
    }


    /**
     * Prueba
     * @return Resultado
     */
    @WebMethod(operationName = "Prueba")
    public Retorno_MsgObj Prueba() {
        //TODO write your implementation code here:
        String path = Utiles.Utilidades.GetInstancia().getAppPath();
        
        System.err.println("Path: " + path);
        
        PerManejador perManager = new PerManejador();
        Retorno_MsgObj retorno = perManager.obtener(1L, PeriodoEstudioDocumento.class);
        
        PeriodoEstudioDocumento doc = (PeriodoEstudioDocumento) retorno.getObjeto();
        System.err.println("Sentencia: " + doc.getUpdateQuery());
        
        return retorno;
    }

    private Retorno_MsgObj isAuthenticated() {
        
        Retorno_MsgObj retorno  = new Retorno_MsgObj(new Mensajes("Autenticando", TipoMensaje.ERROR));

        MessageContext messageContext = context.getMessageContext();
        HttpServletRequest request = (HttpServletRequest) messageContext.get(MessageContext.SERVLET_REQUEST);
        Map httpHeaders = (Map) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
        
        String direccion           = "IP: "+request.getRemoteAddr()+", Port: "+request.getRemotePort()+", Host: "+request.getRemoteHost();

        List tknList = (List) httpHeaders.get("token");
        
        if (tknList != null)
        {
            if(tknList.size() > 0)
            {
                String token = (String) tknList.get(0);
                
                if(token == null)
                {
                    retorno.setMensaje(new Mensajes("No se recibió token", TipoMensaje.ERROR));
                    LoWS.GetInstancia().GuardarMensajeBitacora(null, direccion + "\n Token invalido", EstadoServicio.CON_ERRORES, ServicioWeb.PERSONA);
                }
                else
                {
                    if(!LoWS.GetInstancia().ValidarConsumo(token, ServicioWeb.PERSONA, direccion))
                    {
                        retorno.setMensaje(new Mensajes("Token invalido, no se puede consumir el servicio", TipoMensaje.ERROR));
                    }
                    else
                    {
                        retorno.setMensaje(new Mensajes("Token valido, puede consumir el servicio", TipoMensaje.MENSAJE));                        
                    }
                }
            }
        }
        else
        {
           retorno.setMensaje(new Mensajes("No se recibió token", TipoMensaje.ERROR));
           LoWS.GetInstancia().GuardarMensajeBitacora(null, direccion + "\n Token invalido", EstadoServicio.CON_ERRORES, ServicioWeb.PERSONA); 
        }


        return retorno;

    }

}
