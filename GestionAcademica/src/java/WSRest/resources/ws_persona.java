/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WSRest.resources;

import Entidad.Persona;
import Enumerado.Constantes;
import Enumerado.EstadoServicio;
import Enumerado.ServicioWeb;
import Enumerado.TipoMensaje;
import Logica.LoPersona;
import Logica.LoWS;
import Logica.Seguridad;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * WSRest.Resourse ws_persona
 *
 * @author alvar
 */

@Path("/persona")
public class ws_persona {

    @Context HttpServletRequest requestContext;
            
    /**
     *
     * @param tkn
     * @param usr
     * @param psw
     * @return
     */
    @GET
    //@Path("login/{tkn}/{usr}/{psw}")
    @Path("login")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    //public Retorno_MsgObj login(@PathParam("tkn") String tkn, @PathParam("usr") String usr, @PathParam("psw")String psw) {
    public Retorno_MsgObj login(@QueryParam("tkn") String tkn, @QueryParam("usr") String usr, @QueryParam("psw")String psw) {
       
        System.err.println("Service login rest");
        
        Retorno_MsgObj retorno  = this.isAuthenticated(tkn);

        if(!retorno.SurgioError())
        {
            if(usr == null)
            {
                retorno.setMensaje(new Mensajes("No se recibió parametro", TipoMensaje.ERROR));
            }
            else
            {
                if(psw == null)
                {
                    retorno.setMensaje(new Mensajes("No se recibió parametro", TipoMensaje.ERROR));
                }
                else
                {


                    Seguridad seguridad = Seguridad.GetInstancia();
                    LoPersona loPersona = LoPersona.GetInstancia();


                    String usuarioDecrypt   = seguridad.decrypt(usr, Constantes.ENCRYPT_VECTOR_INICIO.getValor(), Constantes.ENCRYPT_SEMILLA.getValor());
                    String passwordDecrypt  = seguridad.decrypt(psw, Constantes.ENCRYPT_VECTOR_INICIO.getValor(), Constantes.ENCRYPT_SEMILLA.getValor());

                    Boolean resultado = loPersona.IniciarSesion(usuarioDecrypt, seguridad.cryptWithMD5(passwordDecrypt));
                    
                    if(resultado)
                    {
                        retorno.setMensaje(new Mensajes("Bienvenido " + usuarioDecrypt, TipoMensaje.MENSAJE));
                    }
                    else
                    {
                        retorno.setMensaje(new Mensajes("Usuario o contraseña incorrectos", TipoMensaje.ERROR));
                    }

                }
            }
        }
        
        System.err.println("Retorno: " + retorno);
        
        return retorno;
    }
    
    /**
     *
     * @param tkn
     * @param usr
     * @return
     */
    @GET
    @Path("token")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Retorno_MsgObj getToken(@QueryParam("tkn") String tkn, @QueryParam("usr") String usr) {
       
        
        Retorno_MsgObj retorno  = this.isAuthenticated(tkn);

        if(!retorno.SurgioError())
        {
            if(usr == null)
            {
                retorno.setMensaje(new Mensajes("No se recibió parametro", TipoMensaje.ERROR));
            }
            else
            {
                Seguridad seguridad = Seguridad.GetInstancia();
                LoPersona loPersona = LoPersona.GetInstancia();


                String usuarioDecrypt   = seguridad.decrypt(usr, Constantes.ENCRYPT_VECTOR_INICIO.getValor(), Constantes.ENCRYPT_SEMILLA.getValor());

                Retorno_MsgObj perRet = loPersona.obtenerByMdlUsr(usuarioDecrypt);
                  
                if(!perRet.SurgioErrorObjetoRequerido())
                {
                    Persona persona = (Persona) perRet.getObjeto();
                
                    retorno.setObjeto(persona.getPerLgnTkn());
                    retorno.setMensaje(new Mensajes("ok", TipoMensaje.MENSAJE));
                }
                else
                {
                    retorno.setMensaje(perRet.getMensaje());
                }
                
            }
        }
        
        
        return retorno;
    }
        
    
    private Retorno_MsgObj isAuthenticated(String token) {
       
        requestContext.getRemoteAddr();
        Retorno_MsgObj retorno  = new Retorno_MsgObj(new Mensajes("Autenticando", TipoMensaje.ERROR));

        String direccion           = "IP: "+requestContext.getRemoteAddr()+", Port: "+requestContext.getRemotePort()+", Host: "+requestContext.getRemoteHost();

                
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
            
        return retorno;

    }
}
