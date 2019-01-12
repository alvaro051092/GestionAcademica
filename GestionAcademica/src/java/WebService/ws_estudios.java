/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import Entidad.Escolaridad;
import Entidad.PeriodoEstudio;
import Entidad.PeriodoEstudioDocumento;
import Entidad.Persona;
import Enumerado.EstadoServicio;
import Enumerado.ServicioWeb;
import Enumerado.TipoMensaje;
import Logica.LoCalendario;
import Logica.LoPeriodo;
import Logica.LoPersona;
import Logica.LoWS;
import SDT.SDT_PersonaEstudio;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import java.util.ArrayList;
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
 * Servicio estudios
 *
 * @author aa
 */
@WebService(serviceName = "ws_estudios")
public class ws_estudios {

    @Resource WebServiceContext context;
    
    /**
     * 
     * @param PerCod Recibe un código de persona
     * @return Retorna una lista de estudios dado el código recibido
     */
    @WebMethod(operationName = "lstEstudiosPorAlumno")
    public Retorno_MsgObj lstEstudiosPorAlumno(@WebParam(name = "PerCod") Long PerCod) 
    {
        Retorno_MsgObj retorno = this.isAuthenticated();
        
        LoPersona lopersona = LoPersona.GetInstancia();
        ArrayList<SDT_PersonaEstudio> lstEstudios = new ArrayList<>();
        
        if(!retorno.SurgioError())
        {
            if(PerCod == null)
            {
                retorno.setMensaje(new Mensajes("No se recibió ningún parametro Alumno", TipoMensaje.ERROR));
            }
            else
            {
                lstEstudios = lopersona.ObtenerEstudios(PerCod);
                for(SDT_PersonaEstudio lstEst : lstEstudios)
                {
                    retorno.getLstObjetos().add(lstEst);
                }
                retorno.setMensaje(new Mensajes("OK", TipoMensaje.MENSAJE));
            }
        }
        return retorno;
    }
    
    /**
     *
     * @param PerCod Recibe un código de persona
     * @return Retorna una lista de estudios previos dado el código recibido
     */
    @WebMethod(operationName = "lstEstudiosPreviosPorAlumno")
    public Retorno_MsgObj lstEstudiosPreviosPorAlumno(@WebParam(name = "PerCod") Long PerCod)
    {
        Retorno_MsgObj retorno = this.isAuthenticated();
        
        LoCalendario loCalendario = LoCalendario.GetInstancia();
        
        if(!retorno.SurgioError())
        {
            if(PerCod == null)
            {
                retorno.setMensaje(new Mensajes("No se recibió ningún parametro Alumno", TipoMensaje.ERROR));
            }
            else
            {
                retorno = loCalendario.ObtenerListaPendiente(PerCod);
                retorno.setMensaje(new Mensajes("OK", TipoMensaje.MENSAJE));
            }
        }
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
                    LoWS.GetInstancia().GuardarMensajeBitacora(null, direccion + "\n Token invalido", EstadoServicio.CON_ERRORES, ServicioWeb.ESTUDIOS);
                }
                else
                {
                    if(!LoWS.GetInstancia().ValidarConsumo(token, ServicioWeb.ESTUDIOS, direccion))
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
           LoWS.GetInstancia().GuardarMensajeBitacora(null, direccion + "\n Token invalido", EstadoServicio.CON_ERRORES, ServicioWeb.ESTUDIOS); 
        }


        return retorno;

    }
}
