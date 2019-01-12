/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Entidad.Notificacion;
import Entidad.NotificacionBitacora;
import Entidad.NotificacionConsulta;
import Entidad.NotificacionDestinatario;
import Enumerado.TipoMensaje;
import Enumerado.TipoNotificacion;
import Interfaz.InABMGenerico;
import Persistencia.PerManejador;
import SDT.SDT_Parameters;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import java.util.ArrayList;

/**
 *
 * @author alvar
 */
public class LoNotificacion implements InABMGenerico{

    private static LoNotificacion instancia;

    private LoNotificacion() {
    }
    
    /**
     * Obtener instancia
     * @return Instancia de clase
     */
    public static LoNotificacion GetInstancia(){
        if (instancia==null)
        {
            instancia   = new LoNotificacion();
        }

        return instancia;
    }
    
    /**
     * Guardar notificacion
     * @param pObjeto Notificacion
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object guardar(Object pObjeto) {
        Notificacion not = (Notificacion) pObjeto;
        
        PerManejador perManager = new PerManejador();
            
        Retorno_MsgObj retorno = perManager.guardar(not);

        if(!retorno.SurgioErrorObjetoRequerido())
        {
            not.setNotCod((Long) retorno.getObjeto());
            retorno.setObjeto(not);
        }
            
        return retorno; 
    }

    /**
     * Actualizar notificacion
     * @param pObjeto Notificacion
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object actualizar(Object pObjeto) {
        PerManejador perManager = new PerManejador();
        return perManager.actualizar(pObjeto);
    }

    /**
     * Eliminar notificacion
     * @param pObjeto Notificacion
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object eliminar(Object pObjeto) {
        PerManejador perManager = new PerManejador();
        return perManager.eliminar(pObjeto);
    }

    /**
     * Obtener notificacion
     * @param pObjeto Long - NotCod
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtener(Object pObjeto) {
        PerManejador perManager = new PerManejador();
        return perManager.obtener((Long) pObjeto, Notificacion.class);
    }

    /**
     * Obtener lista de notificaciones
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtenerLista() {
        PerManejador perManager = new PerManejador();

        return perManager.obtenerLista("Notificacion.findAll", null);
    }
    
    /**
     * Obtener por nombre
     * @param nombre nombre
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj obtenerByNom(String nombre) {
        
        PerManejador perManager = new PerManejador();
        
        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(nombre, "NotNom"));
        
        Retorno_MsgObj retorno = perManager.obtenerLista("Notificacion.findByNom", lstParametros);
        if(!retorno.SurgioErrorListaRequerida())
        {
            if(retorno.getLstObjetos().size()>0)
            {
                retorno.setObjeto(retorno.getLstObjetos().get(0));
                retorno.setLstObjetos(null);
            }
        }
        return retorno;
    }
    
    /**
     * Obtener lista por tipo y si esta activa
     * @param NotAct Activa
     * @param NotTpo Tipo
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj obtenerListaByTipoActiva(Boolean NotAct, TipoNotificacion NotTpo) {
        
        PerManejador perManager = new PerManejador();
        
        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(NotAct, "NotAct"));
        lstParametros.add(new SDT_Parameters(NotTpo, "NotTpo"));
        
        return perManager.obtenerLista("Notificacion.findAutoActiva", lstParametros);
    }
    
    /**
     * Obtener resultados de consulta SQL
     * @param query Sentencia SQL
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj obtenerResultadosQuery(String query){
        
        Retorno_MsgObj retorno = this.ValidarQuery(query);
        
        if(!retorno.SurgioError())
        {
            PerManejador perManager = new PerManejador();
            retorno = perManager.obtenerResultadosQuery(query);
        }
        
        return retorno;
    }
    
    /**
     * Validar consulta SQL
     * @param query Sentencia SQL
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ValidarQuery(String query){
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Ok", TipoMensaje.MENSAJE));
        
        if(query.toLowerCase().contains(";")) retorno.setMensaje(new Mensajes("La query contiene el caracter ';'", TipoMensaje.ERROR));
        if(query.toLowerCase().contains("insert")) retorno.setMensaje(new Mensajes("La query contiene el caracter 'insert'", TipoMensaje.ERROR));
        if(query.toLowerCase().contains("update")) retorno.setMensaje(new Mensajes("La query contiene el caracter 'update'", TipoMensaje.ERROR));
        if(query.toLowerCase().contains("drop")) retorno.setMensaje(new Mensajes("La query contiene el caracter 'drop'", TipoMensaje.ERROR));
        if(query.toLowerCase().contains("delete")) retorno.setMensaje(new Mensajes("La query contiene el caracter 'delete'", TipoMensaje.ERROR));
        if(query.toLowerCase().contains("create")) retorno.setMensaje(new Mensajes("La query contiene el caracter 'create'", TipoMensaje.ERROR));
        if(query.toLowerCase().contains("alter")) retorno.setMensaje(new Mensajes("La query contiene el caracter 'alter'", TipoMensaje.ERROR));
        
        return retorno;
    }
    
    
    //------------------------------------------------------------------------------------
    //-MANEJO DE DESTINATARIOS
    //------------------------------------------------------------------------------------

    /**
     * Agregar destinatario
     * @param destinatario Destinatario
     * @return  Resultado: RETORNO_MSGOBJ
     */
    
    public Retorno_MsgObj DestinatarioAgregar(NotificacionDestinatario destinatario){
        Retorno_MsgObj retorno = this.ValidarDestinatario(destinatario);
        Boolean error = retorno.SurgioError();
        
        if(!error)
        {
            Notificacion notificacion = destinatario.getNotificacion();
            notificacion.getLstDestinatario().add(destinatario);
            retorno = (Retorno_MsgObj) this.actualizar(notificacion);
        }
        
        return retorno;
    }
    
    /**
     * Actualizar destinatario
     * @param destinatario Destinatario
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj DestinatarioActualizar(NotificacionDestinatario destinatario){
        
        Retorno_MsgObj retorno = this.ValidarDestinatario(destinatario);
        Boolean error = retorno.SurgioError();
        
        if(!error)
        {
            Notificacion notificacion = destinatario.getNotificacion();
            int indice  = notificacion.getLstDestinatario().indexOf(destinatario);
            notificacion.getLstDestinatario().set(indice, destinatario);
            retorno = (Retorno_MsgObj) this.actualizar(notificacion);
        }
        
        return retorno;
    }
    
    /**
     * Destinatario eliminar
     * @param destinatario Destinatario
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj DestinatarioEliminar(NotificacionDestinatario destinatario){
        Retorno_MsgObj retorno = this.ValidarDestinatario(destinatario);
        Boolean error = retorno.SurgioError();
        
        if(!error)
        {
            Notificacion notificacion = destinatario.getNotificacion();
            int indice  = notificacion.getLstDestinatario().indexOf(destinatario);
            notificacion.getLstDestinatario().remove(indice);
            retorno = (Retorno_MsgObj) this.actualizar(notificacion);
        }
        
        return retorno;
    }
    
    /**
     * Validar destinatario
     * @param destinatario Destinatario
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ValidarDestinatario(NotificacionDestinatario destinatario){
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Ok", TipoMensaje.MENSAJE));
        
        if(destinatario == null)
        {
            retorno.setMensaje(new Mensajes("No se recibio destinatario", TipoMensaje.ERROR));
            
        }
        else{
            if(destinatario.getNotificacion() == null)
            {
                retorno.setMensaje(new Mensajes("No se recibio notificacion", TipoMensaje.ERROR));
            }
            else
            {
                if(destinatario.getPersona() == null && destinatario.getNotEmail() == null)
                {
                    retorno.setMensaje(new Mensajes("No se recibio destinatario", TipoMensaje.ERROR));
                }
                else
                {
                    if(destinatario.getPersona() != null && destinatario.getNotEmail() != null)
                    {
                        retorno.setMensaje(new Mensajes("Se debe indicar una persona, o un email, no ambos.", TipoMensaje.ERROR));
                    }
                }
            }
        }
        
        return retorno;
    }
    
    //------------------------------------------------------------------------------------
    //-MANEJO DE CONSULTAS
    //------------------------------------------------------------------------------------

    /**
     * Agregar consulta SQL
     * @param consulta Consulta
     * @return  Resultado: RETORNO_MSGOBJ
     */
    
    public Retorno_MsgObj ConsultaAgregar(NotificacionConsulta consulta){
        Retorno_MsgObj retorno = this.ValidarConsulta(consulta);
        Boolean error = retorno.SurgioError();
        
        if(!error)
        {
            Notificacion notificacion = consulta.getNotificacion();
            notificacion.getLstConsulta().add(consulta);
            retorno = (Retorno_MsgObj) this.actualizar(notificacion);
        }
        
        return retorno;
    }
    
    /**
     * Actualizar consulta SQL
     * @param consulta Consulta SQL
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ConsultaActualizar(NotificacionConsulta consulta){
        
        Retorno_MsgObj retorno = this.ValidarConsulta(consulta);
        Boolean error = retorno.SurgioError();
        
        if(!error)
        {
            Notificacion notificacion = consulta.getNotificacion();
            int indice  = notificacion.getLstConsulta().indexOf(consulta);
            notificacion.getLstConsulta().set(indice, consulta);
            retorno = (Retorno_MsgObj) this.actualizar(notificacion);
        }
        
        return retorno;
    }
    
    /**
     * Eliminar consulta SQL
     * @param consulta Consulta SQL
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ConsultaEliminar(NotificacionConsulta consulta){
        Retorno_MsgObj retorno = this.ValidarConsulta(consulta);
        Boolean error = retorno.SurgioError();
        
        if(!error)
        {
            Notificacion notificacion = consulta.getNotificacion();
            int indice  = notificacion.getLstConsulta().indexOf(consulta);
            notificacion.getLstConsulta().remove(indice);
            retorno = (Retorno_MsgObj) this.actualizar(notificacion);
        }
        
        return retorno;
    }
    
    /**
     * Validar consulta SQL
     * @param consulta Consulta SQL
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ValidarConsulta(NotificacionConsulta consulta){
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Ok", TipoMensaje.MENSAJE));
        
        if(consulta == null)
        {
            retorno.setMensaje(new Mensajes("No se recibio consulta", TipoMensaje.ERROR));
            
        }
        else{
            if(consulta.getNotificacion() == null)
            {
                retorno.setMensaje(new Mensajes("No se recibio notificacion", TipoMensaje.ERROR));
            }
            else
            {
                if(consulta.getNotCnsSQL() == null)
                {
                    retorno.setMensaje(new Mensajes("No se recibio consulta", TipoMensaje.ERROR));
                }
                else
                {
                    if(consulta.getNotCnsSQL().isEmpty())
                    {
                        retorno.setMensaje(new Mensajes("No se recibio tipo de consulta", TipoMensaje.ERROR));
                    }
                    else
                    {
                        if(consulta.getNotCnsTpo() == null)
                        {
                            retorno.setMensaje(new Mensajes("No se recibio tipo de consulta", TipoMensaje.ERROR));
                        }
                    }
                }
            }
        }
        
        return retorno;
    }
    
    //------------------------------------------------------------------------------------
    //-MANEJO DE BITACORA
    //------------------------------------------------------------------------------------

    /**
     * Agregar bitacora
     * @param bitacora Bitacora
     * @return  Resultado: RETORNO_MSGOBJ
     */
    
    public Retorno_MsgObj BitacoraAgregar(NotificacionBitacora bitacora){
        Retorno_MsgObj retorno = this.ValidarBitacora(bitacora);
        Boolean error = retorno.SurgioError();
        
        if(!error)
        {
            Notificacion notificacion = bitacora.getNotificacion();
            notificacion.getLstBitacora().add(bitacora);
            retorno = (Retorno_MsgObj) this.actualizar(notificacion);
        }
        
        retorno.setObjeto(bitacora);
        
        return retorno;
    }
    
    /**
     * Actualizar bitacora
     * @param bitacora Bitacora
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj BitacoraActualizar(NotificacionBitacora bitacora){
        
        Retorno_MsgObj retorno = this.ValidarBitacora(bitacora);
        Boolean error = retorno.SurgioError();
        
        if(!error)
        {
            Notificacion notificacion = bitacora.getNotificacion();
            int indice  = notificacion.getLstBitacora().indexOf(bitacora);
            
            notificacion.getLstBitacora().set(indice, bitacora);
            retorno = (Retorno_MsgObj) this.actualizar(notificacion);
        }
        
        retorno.setObjeto(bitacora);
        
        return retorno;
    }
    
    /**
     * Eliminar Bitacora
     * @param bitacora Bitacora
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj BitacoraEliminar(NotificacionBitacora bitacora){
        Retorno_MsgObj retorno = this.ValidarBitacora(bitacora);
        Boolean error = retorno.SurgioError();
        
        if(!error)
        {
            Notificacion notificacion = bitacora.getNotificacion();
            int indice  = notificacion.getLstBitacora().indexOf(bitacora);
            notificacion.getLstBitacora().remove(indice);
            retorno = (Retorno_MsgObj) this.actualizar(notificacion);
        }
        
        return retorno;
    }
    
    /**
     * Depurar bitacora
     * @param notificacion Notificacion
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj BitacoraDepurar(Notificacion notificacion){
        
        notificacion.setLstBitacora(null);
        
        Retorno_MsgObj retorno = (Retorno_MsgObj) this.actualizar(notificacion);
        
        return retorno;
    }
    
    /**
     * Validar bitacora
     * @param bitacora bitacora
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ValidarBitacora(NotificacionBitacora bitacora){
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Ok", TipoMensaje.MENSAJE));
        
        if(bitacora == null)
        {
            retorno.setMensaje(new Mensajes("No se recibio bitacora", TipoMensaje.ERROR));
            
        }
        else{
            if(bitacora.getNotificacion() == null)
            {
                retorno.setMensaje(new Mensajes("No se recibio notificacion", TipoMensaje.ERROR));
            }
        }
        
        return retorno;
    }
}
