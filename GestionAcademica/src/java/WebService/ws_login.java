/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import Enumerado.Constantes;
import Enumerado.EstadoServicio;
import Enumerado.ServicioWeb;
import Enumerado.TipoMensaje;
import Logica.LoPersona;
import Logica.LoWS;
import Logica.Seguridad;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
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
 * Servicio Login
 *
 * @author alvar
 */
@WebService(serviceName = "ws_login")
public class ws_login {

    @Resource WebServiceContext context;
   
    /**
     * Inicia sesión
     * @param pUser usuario
     * @param pPassword contraseña
     * @return Retorna el resultado de loguarse en el sistema
     */
    @WebMethod(operationName = "Login")
    public String Login(@WebParam(name = "pUser") String pUser, @WebParam(name = "pPassword") String pPassword) {
        //TODO write your implementation code here:
        
        Boolean resultado       = false;
        Retorno_MsgObj retorno  = this.isAuthenticated();

        if(!retorno.SurgioError())
        {
            if(pUser == null)
            {
                retorno.setMensaje(new Mensajes("No se recibió parametro", TipoMensaje.ERROR));
            }
            else
            {
                if(pPassword == null)
                {
                    retorno.setMensaje(new Mensajes("No se recibió parametro", TipoMensaje.ERROR));
                }
                else
                {


                    Seguridad seguridad = Seguridad.GetInstancia();
                    LoPersona loPersona = LoPersona.GetInstancia();


                    String usuarioDecrypt   = seguridad.decrypt(pUser, Constantes.ENCRYPT_VECTOR_INICIO.getValor(), Constantes.ENCRYPT_SEMILLA.getValor());
                    String passwordDecrypt  = seguridad.decrypt(pPassword, Constantes.ENCRYPT_VECTOR_INICIO.getValor(), Constantes.ENCRYPT_SEMILLA.getValor());

                    resultado = loPersona.IniciarSesion(usuarioDecrypt, seguridad.cryptWithMD5(passwordDecrypt));

                }
            }
        }
        
        
        return resultado.toString();
    }
    
  
    /**
     * Cierra sesión
     * @param PerCod usuario
     * @return Resultado
     */
    @WebMethod(operationName = "Logout")
    public Retorno_MsgObj LogOut(@WebParam(name = "pPerCod") String PerCod) {
        //TODO write your implementation code here:
        
        //OBTENER DIRECCION DE QUIEN LLAMA AL SERVICIO
        
        Retorno_MsgObj retorno  = this.isAuthenticated();

        if(!retorno.SurgioError())
        {
            if(PerCod == null)
            {
                retorno.setMensaje(new Mensajes("No se recibió parametro", TipoMensaje.ERROR));
            }
            else
            {

                Seguridad seguridad = Seguridad.GetInstancia();
                LoPersona loPersona = LoPersona.GetInstancia();

                String usuarioDecrypt   = seguridad.decrypt(PerCod, Constantes.ENCRYPT_VECTOR_INICIO.getValor(), Constantes.ENCRYPT_SEMILLA.getValor());

                retorno = loPersona.LimpiarToken(Long.valueOf(usuarioDecrypt));

            }
        } 
        
        retorno.setObjeto(null);
        
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
                    LoWS.GetInstancia().GuardarMensajeBitacora(null, direccion + "\n Token invalido", EstadoServicio.CON_ERRORES, ServicioWeb.LOGIN);
                }
                else
                {
                    if(!LoWS.GetInstancia().ValidarConsumo(token, ServicioWeb.LOGIN, direccion))
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
           LoWS.GetInstancia().GuardarMensajeBitacora(null, direccion + "\n Token invalido", EstadoServicio.CON_ERRORES, ServicioWeb.LOGIN); 
        }


        return retorno;

    }

}
