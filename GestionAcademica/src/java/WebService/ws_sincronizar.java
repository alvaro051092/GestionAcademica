/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import Entidad.Parametro;
import Enumerado.EstadoServicio;
import Enumerado.ServicioWeb;
import Enumerado.TipoMensaje;
import Logica.LoParametro;
import Logica.LoSincronizacion;
import Logica.LoWS;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
//import Utiles.SincRetorno;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource; 
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

/**
 * Servicio Sincronizar
 * 
 *
 * @author alvar
 */
@WebService(serviceName = "ws_Sincronizar")
public class ws_sincronizar {

    @Resource WebServiceContext context;
    
    /**
     * Servicio de sincronizacion
     * @param cambios recibe los cambios a impactar
     * @return retorna los cambios nuevos, o las inconsistencias
     */
    @WebMethod(operationName = "sincronizar")
    public Retorno_MsgObj sincronizar(@WebParam(name = "cambios") Retorno_MsgObj cambios) {

        System.err.println("Consumiendo sincronizar");
        
        Retorno_MsgObj retorno  = this.isAuthenticated();
                
        if(!retorno.SurgioError())
        {
            
            if(cambios == null)
            {
                retorno.setMensaje(new Mensajes("No se recibieron cambios, objeto nulo", TipoMensaje.ERROR));
            }
            else
            {
                retorno = LoSincronizacion.GetInstancia().Sincronizar(cambios);
            }
        }

        return retorno;
    }

    /**
     * Actualiza fecha de sincronizacion
     * @param fecha fecha a impactar
     * @return retorna los cambios nuevos, o las inconsistencias
     */
    @WebMethod(operationName = "update_fecha")
    public Retorno_MsgObj update_fecha(@WebParam(name = "fecha") Date fecha) {
        
        Retorno_MsgObj retorno  = this.isAuthenticated();
                
        if(!retorno.SurgioError())
        {
            if(fecha == null)
            {
                retorno.setMensaje(new Mensajes("No se recibio fecha, objeto nulo", TipoMensaje.ERROR));
            }
            else
            {
                Parametro param = LoParametro.GetInstancia().obtener();
                param.setParFchUltSinc(fecha);
                LoParametro.GetInstancia().actualizar(param);
                retorno.setMensaje(new Mensajes("Modificado ok", TipoMensaje.MENSAJE));

            }
        }
        
        
        return retorno;
    }

    /**
     * Web service operation
     * @param cambios recibe inconsistencias
     * @return retorna el resultado de la operacion 
     */
    @WebMethod(operationName = "impactar_inconsistencia")
    public Retorno_MsgObj impactar_inconsistencia(@WebParam(name = "cambios") Retorno_MsgObj cambios) {
        Retorno_MsgObj retorno  = this.isAuthenticated();
                
        if(!retorno.SurgioError())
        {
            if(cambios == null)
            {
                retorno.setMensaje(new Mensajes("No se recibieron cambios, objeto nulo", TipoMensaje.ERROR));
            }
            else
            {
                if(cambios.getObjeto() != null)
                {
                    retorno = LoSincronizacion.GetInstancia().ProcesarInconsistencia(cambios);
                }
            }
        }
        
        
        return retorno;
    }
    
    private Retorno_MsgObj isAuthenticated() {
        
        Retorno_MsgObj retorno  = new Retorno_MsgObj(new Mensajes("Autenticando", TipoMensaje.ERROR));

        MessageContext messageContext = context.getMessageContext();
        Map httpHeaders = (Map) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
        List tknList = (List) httpHeaders.get("token");

        if (tknList != null)
        {
            if(tknList.size() > 0)
            {
                String token = (String) tknList.get(0);
                
                HttpServletRequest request = (HttpServletRequest)context.getMessageContext().get(MessageContext.SERVLET_REQUEST);
                String direccion           = "IP: "+request.getRemoteAddr()+", Port: "+request.getRemotePort()+", Host: "+request.getRemoteHost();

                

                if(token == null)
                {
                    retorno.setMensaje(new Mensajes("No se recibió token", TipoMensaje.ERROR));
                    LoWS.GetInstancia().GuardarMensajeBitacora(null, direccion + "\n Token invalido", EstadoServicio.CON_ERRORES, ServicioWeb.SINCRONIZAR);
                }
                else
                {
                    if(!LoWS.GetInstancia().ValidarConsumo(token, ServicioWeb.SINCRONIZAR, direccion))
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

        
        return retorno;

    }
}
