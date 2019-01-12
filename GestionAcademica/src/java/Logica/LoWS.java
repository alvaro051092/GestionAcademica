/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Entidad.WS_Bit;
import Entidad.WS_User;
import Entidad.WS_UserServicio;
import Enumerado.Constantes;
import Enumerado.EstadoServicio;
import Enumerado.ServicioWeb;
import Enumerado.TipoMensaje;
import Interfaz.InABMGenerico;
import Persistencia.PerManejador;
import SDT.SDT_Parameters;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alvar
 */
public class LoWS implements InABMGenerico{

    private static LoWS instancia;

    private LoWS() {
    }
    
    /**
     * Obtener instancia
     * @return Instancia
     */
    public static LoWS GetInstancia(){
        if (instancia==null)
        {
            instancia   = new LoWS();
        }

        return instancia;
    }
    
    /**
     * Guardar servicio
     * @param pObjeto Servicio
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object guardar(Object pObjeto) {
        WS_User wsUser = (WS_User) pObjeto;
        
        PerManejador perManager = new PerManejador();
        
        Retorno_MsgObj retorno = perManager.guardar(wsUser);

        if(!retorno.SurgioErrorObjetoRequerido())
        {
            wsUser.setWsUsrCod((Long) retorno.getObjeto());
            retorno.setObjeto(wsUser);
        }
            
        return retorno; 
    }

    /**
     * Actualizar servicio
     * @param pObjeto Servicio
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object actualizar(Object pObjeto) {
        
            
        PerManejador perManager = new PerManejador();

        return perManager.actualizar(pObjeto);
        
    }

    /**
     * Eliminar WS
     * @param pObjeto Servicio
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object eliminar(Object pObjeto) {
        PerManejador perManager = new PerManejador();
        return perManager.eliminar(pObjeto);
    }

    /**
     * Obtener WS
     * @param pObjeto Código 
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtener(Object pObjeto) {
        PerManejador perManager = new PerManejador();
        return perManager.obtener((Long) pObjeto, WS_User.class);        
    }

    /**
     * Obtener lista de WS
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtenerLista() {
        
        PerManejador perManager = new PerManejador();

        return perManager.obtenerLista("WS_User.findAll", null);
    }
    
    /**
     * Obtener por WS User
     * @param usr WS User
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj obtenerByUsrNom(String usr){
        PerManejador perManager = new PerManejador();

        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(usr, "WsUsr"));
        
        Retorno_MsgObj retorno = perManager.obtenerLista("WS_User.findByUsrName", lstParametros);
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            if(retorno.getLstObjetos().size() > 0)
            {
                retorno.setObjeto(retorno.getLstObjetos().get(0));
                retorno.setLstObjetos(null);
            }
        }
        
        return retorno;
    }
    
    /**
     * Validar consumo de servicio
     * @param token Token
     * @param ws_metodo Metodo
     * @param direccion Direccion
     * @return Autorizado
     */
    public Boolean ValidarConsumo(String token, ServicioWeb ws_metodo, String direccion){
        String tokenDes = Seguridad.GetInstancia().decrypt(token, Constantes.ENCRYPT_VECTOR_INICIO.getValor(), Constantes.ENCRYPT_SEMILLA.getValor());
        
        try
        {
            if(tokenDes.indexOf(Constantes.SEPARADOR.getValor()) < 1)
            {
                this.GuardarMensajeBitacora(null, direccion + "\n Token invalido", EstadoServicio.CON_ERRORES , ws_metodo);
            }
            else
            {
                String usr = tokenDes.substring(0, tokenDes.indexOf(Constantes.SEPARADOR.getValor()));
                String psw = tokenDes.substring(tokenDes.indexOf(Constantes.SEPARADOR.getValor()), tokenDes.length());
                psw        = psw.replace(Constantes.SEPARADOR.getValor(), "");

                if(usr != null && psw != null)
                {

                    usr = Seguridad.GetInstancia().decrypt(usr, Constantes.ENCRYPT_VECTOR_INICIO.getValor(), Constantes.ENCRYPT_SEMILLA.getValor());
                    psw = Seguridad.GetInstancia().decrypt(psw, Constantes.ENCRYPT_VECTOR_INICIO.getValor(), Constantes.ENCRYPT_SEMILLA.getValor());
                    
                    Retorno_MsgObj retorno = this.obtenerByUsrNom(usr);

                    if(!retorno.SurgioErrorObjetoRequerido())
                    {
                        WS_User usuario = (WS_User) retorno.getObjeto();

                        if(usuario.getWsUsrPsw().equals(Seguridad.GetInstancia().cryptWithMD5(psw)))
                        {
                            for(WS_UserServicio servicio : usuario.getLstServicio())
                            {
                                if(servicio.getWsSrv().equals(ws_metodo))
                                {
                                    this.GuardarMensajeBitacora(usuario, direccion, EstadoServicio.CORRECTO , ws_metodo);
                                    return true;
                                }
                            }
                            
                            this.GuardarMensajeBitacora(usuario, direccion + "\n No tiene permiso para consumir el servicio", EstadoServicio.CON_ERRORES , ws_metodo);
                            
                        }
                        else
                        {
                            this.GuardarMensajeBitacora(usuario, direccion + "\n Contraseña invalida", EstadoServicio.CON_ERRORES , ws_metodo);
                        }
                    } 
                    else
                    {
                        this.GuardarMensajeBitacora(null, direccion + "\n Error al obtener usuario de token", EstadoServicio.CON_ERRORES , ws_metodo);
                    }
                }
                else
                {
                    this.GuardarMensajeBitacora(null, direccion + "\n Usuario o contraseña invalidos", EstadoServicio.CON_ERRORES , ws_metodo);
                }
            }
        }
        catch(Exception e)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }

        return false;
    }
    
    /**
     * Guardar mensaje en bitacora
     * @param usuario Usuario
     * @param msg Mensaje
     * @param estado Estado
     * @param servicio Servicio
     */
    public void GuardarMensajeBitacora(WS_User usuario, String msg, EstadoServicio estado, ServicioWeb servicio){
        if(usuario == null)
        {
            usuario = (WS_User) this.obtenerByUsrNom(Constantes.WS_USR.getValor()).getObjeto();            
        }
                
        this.BitacoraAgregar(new WS_Bit(usuario, servicio, new Date(), estado, msg));
    }
    
    /**
     * Carga inicial de WS Usuarios
     */
    public void CargarUsuariosWS(){
        //-Moodle
        WS_User usuario = new WS_User();
        usuario.setWsUsr(Constantes.WS_USR_MOODLE.getValor());
        usuario.setWsUsrPsw(Seguridad.GetInstancia().cryptWithMD5(Constantes.WS_PSW_MOODLE.getValor()));
        usuario.setLstServicio(new ArrayList<WS_UserServicio>());
        
        usuario.getLstServicio().add(new WS_UserServicio(usuario, ServicioWeb.LOGIN));
        
        this.guardar(usuario);
        
        //-App
        usuario = new WS_User();
        usuario.setWsUsr(Constantes.WS_USR_APP.getValor());
        usuario.setWsUsrPsw(Seguridad.GetInstancia().cryptWithMD5(Constantes.WS_PSW_APP.getValor()));
        usuario.setLstServicio(new ArrayList<WS_UserServicio>());
        
        usuario.getLstServicio().add(new WS_UserServicio(usuario, ServicioWeb.LOGIN));
        usuario.getLstServicio().add(new WS_UserServicio(usuario, ServicioWeb.ESTUDIOS));
        usuario.getLstServicio().add(new WS_UserServicio(usuario, ServicioWeb.EVALUACION_ALUMNO));
        usuario.getLstServicio().add(new WS_UserServicio(usuario, ServicioWeb.PERSONA));
        usuario.getLstServicio().add(new WS_UserServicio(usuario, ServicioWeb.SOLICITUDES));
        
        this.guardar(usuario);
        
        //-Web
        usuario = new WS_User();
        usuario.setWsUsr(Constantes.WS_USR_WEB.getValor());
        usuario.setWsUsrPsw(Seguridad.GetInstancia().cryptWithMD5(Constantes.WS_PSW_WEB.getValor()));
        usuario.setLstServicio(new ArrayList<WS_UserServicio>());
        
        usuario.getLstServicio().add(new WS_UserServicio(usuario, ServicioWeb.SINCRONIZAR));

        this.guardar(usuario);
        
        //-Generico
        usuario = new WS_User();
        usuario.setWsUsr(Constantes.WS_USR.getValor());
        this.guardar(usuario);
        
        
    }
    
    /**
     * Eliminar bitacora
     */
    public void EliminarBitacoraBeforeDate(){
        Retorno_MsgObj usuarios = this.obtenerLista();
        
        PerManejador perManager = new PerManejador();
        
        if(!usuarios.SurgioErrorListaRequerida())
        {
            for(Object objeto : usuarios.getLstObjetos())
            {
                WS_User user = (WS_User) objeto;
                
                if(user.getLstBitacora() != null)
                {
                    for(WS_Bit bitacora : user.getLstBitacora())
                    {
                         perManager.eliminar(bitacora);
                    }
                }
            }
        }
    }
    
    //-------------------------------------------------------------------------
    //SERVICIOS
    //-------------------------------------------------------------------------

    /**
     * Agregar servicio
     * @param usuarioServicio Servicio
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Object ServicioAgregar(WS_UserServicio usuarioServicio){
        boolean error           = false;
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al agregar",TipoMensaje.ERROR), usuarioServicio);
        
        if(!error)
        {
            WS_User usr = usuarioServicio.getUsuario();
            usr.getLstServicio().add(usuarioServicio);
            retorno = (Retorno_MsgObj) this.actualizar(usr);
        }
       
        
        return retorno;
    }
    
    /**
     * Actualizar servicio
     * @param usuarioServicio Servicio
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Object ServicioActualizar(WS_UserServicio usuarioServicio){
        
        WS_User usr = usuarioServicio.getUsuario();
        int indice  = usr.getLstServicio().indexOf(usuarioServicio);
        usr.getLstServicio().set(indice, usuarioServicio);
        
        Retorno_MsgObj retorno = (Retorno_MsgObj) this.actualizar(usr);

        return retorno;
    }
    
    /**
     * Eliminar servicio
     * @param usuarioServicio Servicio
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Object ServicioEliminar(WS_UserServicio usuarioServicio){
        boolean error           = false;
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al eliminar", TipoMensaje.ERROR), usuarioServicio);
       
        if(!error)
        {
            WS_User usr = usuarioServicio.getUsuario();
            int indice  = usr.getLstServicio().indexOf(usuarioServicio);
            usr.getLstServicio().remove(indice);
       
            retorno = (Retorno_MsgObj) this.actualizar(usr);
        }
        return retorno;
    }
    
    /**
     * Obtener servicio
     * @param WsSrvCod Codigo
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ServicioObtener(Long WsSrvCod){
        PerManejador perManager = new PerManejador();
        return perManager.obtener(WsSrvCod, WS_UserServicio.class);
    }
    
    //-------------------------------------------------------------------------
    //BITACORA
    //-------------------------------------------------------------------------

    /**
     * Agregar bitacora
     * @param bitacora Bitacora
     * @return Resultado: RETORNO_MSGOBJ
     */

    public Object BitacoraAgregar(WS_Bit bitacora){
        boolean error           = false;
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al agregar",TipoMensaje.ERROR), bitacora);
        
        if(!error)
        {
            WS_User usr = bitacora.getUsuario();
            usr.getLstBitacora().add(bitacora);
            retorno = (Retorno_MsgObj) this.actualizar(usr);
        }
       
        
        return retorno;
    }
    
    /**
     * Actualizar bitacora
     * @param bitacora bitacora
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Object BitacoraActualizar(WS_Bit bitacora){
        
        WS_User usr = bitacora.getUsuario();
        int indice  = usr.getLstBitacora().indexOf(bitacora);
        usr.getLstBitacora().set(indice, bitacora);
        
        Retorno_MsgObj retorno = (Retorno_MsgObj) this.actualizar(usr);

        return retorno;
    }
    
    /**
     * Eliminar bitacora
     * @param bitacora Bitacora
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Object BitacoraEliminar(WS_Bit bitacora){
        boolean error           = false;
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al eliminar", TipoMensaje.ERROR), bitacora);
       
        if(!error)
        {
            WS_User usr = bitacora.getUsuario();
            int indice  = usr.getLstBitacora().indexOf(bitacora);
            usr.getLstBitacora().remove(indice);
       
            retorno = (Retorno_MsgObj) this.actualizar(usr);
        }
        return retorno;
    }
    
    /**
     * Obtener bitacora
     * @param WsBitCod Codigo
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj BitacoraObtener(Long WsBitCod){
        PerManejador perManager = new PerManejador();
        return perManager.obtener(WsBitCod, WS_Bit.class);
    }
    
    /**
     * Obtener lista de bitacora
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj BitacoraObtenerLista(){
        PerManejador perManager = new PerManejador();
        return perManager.obtenerLista("WS_Bit.findAll", null);
    }
    
    
    
}
