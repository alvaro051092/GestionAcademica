/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica.Notificacion;

import SDT.SDT_Destinatario;
import Entidad.Notificacion;
import Entidad.NotificacionBandeja;
import Entidad.NotificacionBitacora;
import Entidad.NotificacionConsulta;
import Entidad.NotificacionDestinatario;
import Entidad.Persona;
import Enumerado.BandejaEstado;
import Enumerado.BandejaTipo;
import Enumerado.NotificacionEstado;
import Enumerado.ObtenerDestinatario;
import Enumerado.TipoConsulta;
import Enumerado.TipoDestinatario;
import Enumerado.TipoEnvio;
import Enumerado.TipoMensaje;
import Enumerado.TipoNotificacion;
import Logica.LoBandeja;
import Logica.LoNotificacion;
import Logica.LoPersona;
import SDT.SDT_NotificacionEnvio;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author alvar
 */

public class ManejoNotificacion {
    
    private final LoNotificacion loNotificacion;
    private final NotificacionApp notApp;
    private final NotificacionEmail notEmail;
    private final NotificacionWeb notWeb;
    
    /**
     * Manejo de notificaciones
     */
    public ManejoNotificacion() {
        loNotificacion = LoNotificacion.GetInstancia();
        
        notApp      = new NotificacionApp();
        notEmail    = new NotificacionEmail();
        notWeb      = new NotificacionWeb();
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //MANEJO DE NOTIFICACIONES
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Ejecutar notificaciones automaticamente. 
     * Se obtienen desde la base de datos
     * No contempla las notificaciones internas.
     */
    
    public void EjecutarNotificacionAutomaticamente(){
    
        Retorno_MsgObj retorno = loNotificacion.obtenerListaByTipoActiva(Boolean.TRUE, TipoNotificacion.AUTOMATICA);
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            
            for(Object objeto : retorno.getLstObjetos())
            {
                Notificacion notificacion = (Notificacion) objeto;
                
                if(notificacion.NotificarAutomaticamente() && !notificacion.getNotInt())
                {
                    this.EjecutarNotificacion(notificacion);
                }
            }
        }
        
    }
    
    /**
     * Ejecuta una notificación
     * @param notificacion Notificación
     */
    public void EjecutarNotificacion(Notificacion notificacion){
        Retorno_MsgObj retorno      = new Retorno_MsgObj(new Mensajes("Ejecutar notificacion", TipoMensaje.MENSAJE));
        
        List<SDT_Destinatario> destExcluir = new ArrayList<>();
        
        if(notificacion.getLstConsulta() != null)
        {
            for(NotificacionConsulta consulta : notificacion.getLstConsulta())
            {
                if(consulta.getNotCnsTpo().equals(TipoConsulta.EXC_DESTINATARIO))
                {
                    retorno = this.ProcesarDestinatariosExcluir(destExcluir, consulta.getNotCnsSQL());

                    if(!retorno.SurgioError())
                    {
                        for(Object objeto : retorno.getLstObjetos())
                        {
                            destExcluir.add((SDT_Destinatario) objeto);
                        }
                    }
                }
            }
        }
        
        if(!retorno.SurgioError())
        {
        
            //OBTENDRA EL ASUNTO Y CONTENIDO Y ENVIARA PARA CADA DESTINATARIO
            if(notificacion.getNotObtDest().equals(ObtenerDestinatario.UNICA_VEZ))
            {
                retorno = this.ProcesarUnicaVez(notificacion, destExcluir);
            }

            //OBTENDRA EL ASUNTO Y CONTENIDO Y DESTINATARIOS EN BASE A ELLO
            if(notificacion.getNotObtDest().equals(ObtenerDestinatario.POR_CADA_REGISTRO))
            {
                retorno = this.ProcesarPorCadaRegistro(notificacion, destExcluir);
            }
        }
        
        if(retorno.SurgioError())
        {
            this.ProcesoBitacora(null, notificacion, notificacion.getNotAsu(), notificacion.getNotCon(), notificacion.ObtenerDestinatariosAgrupados(), NotificacionEstado.ENVIO_CON_ERRORES, retorno.getMensaje().toString());
        }
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //ENVIO DE NOTIFICACIONES
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Armar envio
     * @param notificacion Notificacion
     * @param destinatario Destinatario
     * @param contenido Contenido
     * @param asunto Asunto
     * @return Notificacion armada
     */
    
    public SDT_NotificacionEnvio ArmarEnvio(Notificacion notificacion, NotificacionDestinatario destinatario, String contenido, String asunto){
        SDT_NotificacionEnvio envio = new SDT_NotificacionEnvio(notificacion.getNotApp(), notificacion.getNotWeb(), notificacion.getNotEmail(), destinatario, contenido, asunto, false);
        return envio;
    }
    
    /**
     * Notificar una lista
     * @param notificacion Notificacion
     * @param lstMensajes Lista de Mensajes
     * @param destExcluir Destinatarios a excluir
     */
    public void NotificarLista(Notificacion notificacion, List<SDT_NotificacionEnvio> lstMensajes, List<SDT_Destinatario> destExcluir){
        
        for(SDT_NotificacionEnvio mensaje : lstMensajes)
        {
            this.Notificar(notificacion, mensaje, destExcluir);
        }
        
    }
    
    /**
     * Notificar
     * @param notificacion Notificacion
     * @param sdtNotificacion Notificacion armada
     * @param destExcluir Destinatarios a excluir
     */
    public void Notificar(Notificacion notificacion, SDT_NotificacionEnvio sdtNotificacion, List<SDT_Destinatario> destExcluir){
        //ACA IRIA PARA CADA CLASE A NOTIFICAR, DEPENDIENDO DE A DONDE ESTA ESTIPULADA LA NOTIFICACION, Y SI EL DESTINATARIO ES UNA PERSONA, QUE TIENE CONFIGURADO RECIBIR.
        if(sdtNotificacion.getDestinatario() == null)
        {
            this.ProcesoBitacora(null, notificacion, sdtNotificacion.getAsunto(), sdtNotificacion.getContenido(), "", NotificacionEstado.ENVIO_CON_ERRORES, (new Date()) + ": No se recibio destinatario");
        }
        else
        {
            sdtNotificacion.setEsHTML(Boolean.TRUE);
            
            NotificacionBitacora bitacora   = this.ProcesoBitacora(null, notificacion, sdtNotificacion.getAsunto(), sdtNotificacion.getContenido(), sdtNotificacion.getDestinatario().GetTextoDestinatario(), NotificacionEstado.ENVIO_EN_PROGRESO, (new Date()) + ": Iniciando proceso");

            Boolean excluirDestinatario = false;

            for(SDT_Destinatario destinatario : destExcluir)
            {
                if(sdtNotificacion.getDestinatario().getPersona() != null)
                {    if(destinatario.getPerCod() != null)
                    {    
                        excluirDestinatario = (sdtNotificacion.getDestinatario().getPersona().getPerCod().equals(destinatario.getPerCod()));
                    }
                }

                if(sdtNotificacion.getDestinatario().getNotEmail() != null)
                {
                    if(destinatario.getEmail() != null)
                    {
                        excluirDestinatario = (sdtNotificacion.getDestinatario().getNotEmail().equals(destinatario.getEmail()));
                    }
                }

            }
            
            
            if(!excluirDestinatario)
            {
                Retorno_MsgObj retorno;
                 
                //-------------------------------------------------------------------------------------------------------------
                //TIPO DE NOTIFICACION: EMAIL TIPO DE DESTINATARIO: EMAIL
                //-------------------------------------------------------------------------------------------------------------
                
                if(sdtNotificacion.getNotEml() && sdtNotificacion.getDestinatario().getNotEmail() != null)
                {
                    //NOTIFICA POR EMAIL
                    retorno = notEmail.Notificar(sdtNotificacion);
                    if(retorno.SurgioError())
                    {
                        bitacora = this.ProcesoBitacora(bitacora, notificacion, sdtNotificacion.getAsunto(), sdtNotificacion.getContenido()
                                , sdtNotificacion.getDestinatario().GetTextoDestinatario()
                                , NotificacionEstado.ENVIO_CON_ERRORES, (new Date()) 
                                + ": Notificacion por email: "
                                + retorno.getMensaje().getMensaje()
                        );
                    }
                    else
                    {
                        bitacora = this.ProcesoBitacora(bitacora, notificacion, sdtNotificacion.getAsunto()
                                , sdtNotificacion.getContenido()
                                , sdtNotificacion.getDestinatario().GetTextoDestinatario()
                                , NotificacionEstado.ENVIO_EN_PROGRESO, (new Date()) 
                                        + ": Notificado por email: " 
                                        +  retorno.getMensaje().getMensaje());
                    }
                }

                
                //-------------------------------------------------------------------------------------------------------------
                //TIPO DE DESTINATARIO: PERSONA
                //-------------------------------------------------------------------------------------------------------------
                if(sdtNotificacion.getDestinatario().getPersona() != null)
                {
                    Persona per = sdtNotificacion.getDestinatario().getPersona();
                    

                    //-------------------------------------------------------------------------------------------------------------
                    //TIPO DE NOTIFICACION: APP
                    //-------------------------------------------------------------------------------------------------------------

                    if(sdtNotificacion.getNotApp() && per.getPerNotApp())
                    {
                        
                        if(per.getPerAppTkn() != null)
                        {
                            if(!per.getPerAppTkn().isEmpty())
                            {
                                //NOTIFICAR POR APP MOVIL
                                retorno = notApp.Notificar(sdtNotificacion);
                                
                                
                                if(retorno.SurgioError())
                                {
                                    bitacora = this.ProcesoBitacora(bitacora, notificacion, sdtNotificacion.getAsunto(), sdtNotificacion.getContenido()
                                            , sdtNotificacion.getDestinatario().GetTextoDestinatario()
                                            , NotificacionEstado.ENVIO_CON_ERRORES, (new Date()) 
                                            + ": Notificacion por app: "
                                            + retorno.getMensaje().toString()
                                    );
                                }
                                else
                                {
                                    bitacora = this.ProcesoBitacora(bitacora, notificacion, sdtNotificacion.getAsunto()
                                            , sdtNotificacion.getContenido()
                                            , sdtNotificacion.getDestinatario().GetTextoDestinatario()
                                            , NotificacionEstado.ENVIO_EN_PROGRESO, (new Date()) + ": Notificado por app: Ok");
                                }
                            }
                            else
                            {
                                NotificacionBandeja bandeja = new NotificacionBandeja();
                                bandeja.setDestinatario(per);
                                bandeja.setNotBanAsu(sdtNotificacion.getAsunto());
                                bandeja.setNotBanMen(sdtNotificacion.getContenido());
                                bandeja.setNotBanEst(BandejaEstado.SIN_LEER);
                                bandeja.setNotBanTpo(BandejaTipo.APP);
                                
                                retorno = (Retorno_MsgObj) LoBandeja.GetInstancia().guardar(bandeja);
                                
                                if(retorno.SurgioError())
                                {
                                    bitacora = this.ProcesoBitacora(bitacora, notificacion, sdtNotificacion.getAsunto()
                                        , sdtNotificacion.getContenido()
                                        , sdtNotificacion.getDestinatario().GetTextoDestinatario()
                                        , NotificacionEstado.ENVIO_CON_ERRORES, (new Date()) + ": Error: " + retorno.getMensaje().getMensaje()
                                    );
                                }
                                else
                                {
                                    bitacora = this.ProcesoBitacora(bitacora, notificacion, sdtNotificacion.getAsunto()
                                        , sdtNotificacion.getContenido()
                                        , sdtNotificacion.getDestinatario().GetTextoDestinatario()
                                        , NotificacionEstado.ENVIO_EN_PROGRESO, (new Date()) + ": El destinatario: " 
                                        + sdtNotificacion.getDestinatario().GetTextoDestinatario()
                                        + ", recibira la notificacion cuando inicie sesion"
                                    );
                                }
                                
                            }
                        }
                        else
                        {
                            NotificacionBandeja bandeja = new NotificacionBandeja();
                            bandeja.setDestinatario(per);
                            bandeja.setNotBanAsu(sdtNotificacion.getAsunto());
                            bandeja.setNotBanMen(sdtNotificacion.getContenido());
                            bandeja.setNotBanEst(BandejaEstado.SIN_LEER);
                            bandeja.setNotBanTpo(BandejaTipo.APP);

                            retorno = (Retorno_MsgObj) LoBandeja.GetInstancia().guardar(bandeja);

                            if(retorno.SurgioError())
                            {
                                bitacora = this.ProcesoBitacora(bitacora, notificacion, sdtNotificacion.getAsunto()
                                    , sdtNotificacion.getContenido()
                                    , sdtNotificacion.getDestinatario().GetTextoDestinatario()
                                    , NotificacionEstado.ENVIO_CON_ERRORES, (new Date()) + ": Error: " + retorno.getMensaje().getMensaje()
                                );
                            }
                            else
                            {
                                bitacora = this.ProcesoBitacora(bitacora, notificacion, sdtNotificacion.getAsunto()
                                    , sdtNotificacion.getContenido()
                                    , sdtNotificacion.getDestinatario().GetTextoDestinatario()
                                    , NotificacionEstado.ENVIO_EN_PROGRESO, (new Date()) + ": El destinatario: " 
                                    + sdtNotificacion.getDestinatario().GetTextoDestinatario()
                                    + ", recibira la notificacion cuando inicie sesion"
                                );
                            }
                        }
                    }

                    //-------------------------------------------------------------------------------------------------------------
                    //TIPO DE NOTIFICACION: WEB
                    //-------------------------------------------------------------------------------------------------------------
                    if(sdtNotificacion.getNotWeb())
                    {
                        //NOTIFICAR VIA WEB
                        retorno = notWeb.Notificar(sdtNotificacion);
                        if(retorno.SurgioError())
                        {
                            bitacora = this.ProcesoBitacora(bitacora, notificacion, sdtNotificacion.getAsunto()
                                    , sdtNotificacion.getContenido()
                                    , sdtNotificacion.getDestinatario().GetTextoDestinatario()
                                    , NotificacionEstado.ENVIO_CON_ERRORES, (new Date()) 
                                    + ": Notificacion por web: "
                                    + retorno.getMensaje().getMensaje()
                            );
                        }
                        else
                        {
                            bitacora = this.ProcesoBitacora(bitacora, notificacion, sdtNotificacion.getAsunto()
                                    , sdtNotificacion.getContenido()
                                    , sdtNotificacion.getDestinatario().GetTextoDestinatario()
                                    , NotificacionEstado.ENVIO_EN_PROGRESO, (new Date()) + ": Notificado por web: Ok");
                        }
                    }

                    //-------------------------------------------------------------------------------------------------------------
                    //TIPO DE NOTIFICACION: EMAIL
                    //-------------------------------------------------------------------------------------------------------------
                    if(sdtNotificacion.getNotEml() && per.getPerNotEml())
                    {

                        if(per.getPerEml() != null)
                        {
                            //NOTIFICAR EMAIL
                            retorno = notEmail.Notificar(sdtNotificacion);
                            if(retorno.SurgioError())
                            {
                                bitacora = this.ProcesoBitacora(bitacora, notificacion, sdtNotificacion.getAsunto()
                                        , sdtNotificacion.getContenido()
                                        , sdtNotificacion.getDestinatario().GetTextoDestinatario()
                                        , NotificacionEstado.ENVIO_CON_ERRORES, (new Date()) 
                                        + ": Notificacion por email: "
                                        + retorno.getMensaje().toString()
                                );
                            }
                            else
                            {
                                bitacora = this.ProcesoBitacora(bitacora, notificacion, sdtNotificacion.getAsunto()
                                        , sdtNotificacion.getContenido()
                                        , sdtNotificacion.getDestinatario().GetTextoDestinatario()
                                        , NotificacionEstado.ENVIO_EN_PROGRESO, (new Date()) + ": Notificado por email: " + retorno.getMensaje().getMensaje());
                            }
                        }
                        else
                        {
                            bitacora = this.ProcesoBitacora(bitacora, notificacion, sdtNotificacion.getAsunto()
                                    , sdtNotificacion.getContenido()
                                    , sdtNotificacion.getDestinatario().GetTextoDestinatario()
                                    , NotificacionEstado.ENVIO_CON_ERRORES, (new Date()) + ": El destinatario: " 
                                        + sdtNotificacion.getDestinatario().GetTextoDestinatario()
                                        + ", no tiene un email"
                                );
                        }
                    }
                }
            }
            else
            {
                this.ProcesoBitacora(bitacora, notificacion, sdtNotificacion.getAsunto()
                        , sdtNotificacion.getContenido()
                        , sdtNotificacion.getDestinatario().GetTextoDestinatario()
                        , NotificacionEstado.ENVIO_CON_ERRORES, (new Date()) + ": Se omite el destinatario: " + sdtNotificacion.getDestinatario().GetTextoDestinatario());
            }


            this.ProcesoBitacora(bitacora, notificacion, sdtNotificacion.getAsunto()
                    , sdtNotificacion.getContenido()
                    , sdtNotificacion.getDestinatario().GetTextoDestinatario()
                    , (bitacora.getNotBitEst().equals(NotificacionEstado.ENVIO_EN_PROGRESO) ? NotificacionEstado.ENVIO_CORRECTO : null)
                    , (new Date()) + ": Fin del proceso");
        
        }
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //PROCESO MENSAJE
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Procesar notificación, donde el destinatario se obtiene una unica vez
     * @param notificacion Notificacion
     * @param destExcluir Destinatarios a excluir
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj ProcesarUnicaVez(Notificacion notificacion, List<SDT_Destinatario> destExcluir){
        
        
        Retorno_MsgObj retorno = this.ProcesarDestinatarios(notificacion);

        if(retorno.SurgioError())
        {
            return retorno;
        }
        else
        {
            notificacion = (Notificacion) retorno.getObjeto();
        }

        for(NotificacionDestinatario destinatario : notificacion.getLstDestinatario())
        {
            Boolean tieneConsulta = false;

            //OBTENGO DATOS A PARTIR DE LA CONSULTA
            for(NotificacionConsulta consulta : notificacion.getLstConsulta())
            {
                if(consulta.getNotCnsTpo().equals(TipoConsulta.CONSULTA))
                {
                    tieneConsulta   = true;
                    String query    = consulta.getNotCnsSQL();

                    //REEMPLAZO EL TAG POR EL CODIGO DE LA PERSONA, NO APLICA A EMAILS.
                    if(destinatario.getPersona() != null) query = query.replace("[%=DESTINATARIO]", destinatario.getPersona().getPerCod().toString());
                   
                    retorno = loNotificacion.obtenerResultadosQuery(query);

                    if(retorno.SurgioError())
                    {
                        return retorno;
                    }
                    else
                    {
                        List lista = (List) retorno.getObjeto();

                        //TIPO DE ENVIO COMUN, UN EMAIL POR REGISTRO PARA TODOS LOS DESTINATARIOS
                        if(notificacion.getNotTpoEnv().equals(TipoEnvio.COMUN))
                        {
                            /*
                                PARA CADA REGISTRO
                                - REEMPLAZO TAGS DESTINATARIO
                                - OBTENGO DATOS
                                - ENVIO
                            */

                            retorno = this.ProcesoMensajeIndividual(notificacion, destinatario, lista);
                            if(retorno.SurgioError())
                            {
                                return retorno;
                            }

                            List<SDT_NotificacionEnvio> lstEnvio = new ArrayList<>();

                            for(Object objeto : retorno.getLstObjetos())
                            {
                                lstEnvio.add((SDT_NotificacionEnvio) objeto);
                            }

                            this.NotificarLista(notificacion, lstEnvio, destExcluir);
                            

                        }

                        //TIPO DE ENVIO AGRUPADO, UN EMAIL CON TODOS LOS REGISTROS PARA CADA DESTINATARIO
                        if(notificacion.getNotTpoEnv().equals(TipoEnvio.AGRUPADO))
                        {
                            /*
                                PARA CADA REGISTRO
                                - REEMPLAZO TAGS DESTINATARIO
                                - OBTENGO DATOS
                                - AGRUPO CONTENIDO
                                - ENVIO
                            */

                            retorno = this.ProcesoMensajeAgrupado(notificacion, destinatario, lista);
                            if(retorno.SurgioError())
                            {
                                return retorno;
                            }

                            List<SDT_NotificacionEnvio> lstEnvio = new ArrayList<>();

                            for(Object objeto : retorno.getLstObjetos())
                            {
                                lstEnvio.add((SDT_NotificacionEnvio) objeto);
                            }

                            this.NotificarLista(notificacion, lstEnvio, destExcluir);

                        }
                    }
                }
            }
            
            //SI NO POSEE CONSULTAS, ENVIO A LOS DESTINATARIOS EL MENSAJE DEFINIDO POR DEFECTO.
            if(!tieneConsulta)
            {
                this.Notificar(notificacion, this.ArmarEnvio(notificacion, destinatario, notificacion.getNotCon(), notificacion.getNotAsu()), destExcluir);
            }
        }

        return retorno;
    }
    
    /**
     * Procesar notificación, donde el destinatario se obtiene por cada registro
     * @param notificacion Notificacion
     * @param destExcluir Destinatarios a excluir
     * @return   Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj ProcesarPorCadaRegistro(Notificacion notificacion, List<SDT_Destinatario> destExcluir){
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Procesar por cada registro", TipoMensaje.MENSAJE));
        
        //OBTENGO DATOS A PARTIR DE LA CONSULTA
        for(NotificacionConsulta consulta : notificacion.getLstConsulta())
        {
            if(consulta.getNotCnsTpo().equals(TipoConsulta.CONSULTA))
            {
                String query = consulta.getNotCnsSQL();

                retorno = loNotificacion.obtenerResultadosQuery(query);

                if(retorno.SurgioError())
                {
                    return retorno;
                }
                else
                {
                    List lista = (List) retorno.getObjeto();

                    //TIPO DE ENVIO COMUN, UN EMAIL POR REGISTRO PARA TODOS LOS DESTINATARIOS
                    if(notificacion.getNotTpoEnv().equals(TipoEnvio.COMUN))
                    {
                        /*
                            PARA CADA REGISTRO
                            - OBTENGO NOMBRE DE COLUMNAS PARA HACER TAGS
                            - REEMPLAZO TAGS EN QUERY DESTINATARIOS
                            - ENVIO NOTIFICACION PARA CADA DESTINATARIO POR CADA REGISTRO
                        */

                        retorno = this.ProcesoMensajeIndividual(notificacion, null, lista);
                        if(retorno.SurgioError())
                        {
                            return retorno;
                        }
                        
                        List<SDT_NotificacionEnvio> lstEnvio = new ArrayList<>();
                        
                        for(Object objeto : retorno.getLstObjetos())
                        {
                            lstEnvio.add((SDT_NotificacionEnvio) objeto);
                        }

                        this.NotificarLista(notificacion, lstEnvio, destExcluir);
                        
                    }

                    //TIPO DE ENVIO AGRUPADO, UN EMAIL CON TODOS LOS REGISTROS PARA CADA DESTINATARIO
                    if(notificacion.getNotTpoEnv().equals(TipoEnvio.AGRUPADO))
                    {
                        /*
                            PARA CADA REGISTRO
                            - OBTENGO NOMBRE DE COLUMNAS PARA HACER TAGS
                            - REEMPLAZO TAGS EN QUERY DESTINATARIOS
                            - AGREGO DESTINATARIO A LISTA
                            - ENVIO A LOS DESTINATARIOS UN UNICO EMAIL
                        */
                        
                        retorno = this.ProcesoMensajeAgrupado(notificacion, null, lista);
                        if(retorno.SurgioError())
                        {
                            return retorno;
                        }
                        
                        List<SDT_NotificacionEnvio> lstEnvio = new ArrayList<>();
                        
                        for(Object objeto : retorno.getLstObjetos())
                        {
                            lstEnvio.add((SDT_NotificacionEnvio) objeto);
                        }

                        this.NotificarLista(notificacion, lstEnvio, destExcluir);
                    }
                }
            }
        }

        return retorno;
    }
    
    /**
     * Procesar notificacion, donde el mensaje es individual
     * @param notificacion Notificacion
     * @param destinatario Destinatario
     * @param registros Registros
     * @return   Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj ProcesoMensajeIndividual(Notificacion notificacion, NotificacionDestinatario destinatario, List registros){
        
        List<NotificacionDestinatario> lstDestinatarioOriginal = new ArrayList<>();
        
        for(NotificacionDestinatario dest : notificacion.getLstDestinatario())
        {
            lstDestinatarioOriginal.add(dest);
        }
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Proceso Mensaje Individual", TipoMensaje.MENSAJE));
        
        List<SDT_NotificacionEnvio> lstEnvio = new ArrayList<>();
        
        String asunto = notificacion.getNotAsu();
        String contenido = notificacion.getNotCon();
        
        //POSEO DESTINATARIO
        if(destinatario != null)
        {
            //UN ENVIO POR REGISTRO - DESTINATARIO
            Iterator iterator = registros.iterator();
            while(iterator.hasNext()){
                Object registro = iterator.next();
                lstEnvio.add(this.ArmarEnvio(notificacion, destinatario, this.ProcesoTags(contenido, registro).trim(), this.ProcesoTags(asunto, registro).trim()));
                
            }
        }
        else
        {
            //OBTENER DESTINATARIOS
            
            //UN ENVIO POR REGISTRO - DESTINATARIO
            
            Iterator iterator = registros.iterator();
            while(iterator.hasNext()){
                
                Object registro = iterator.next();
                
                for(NotificacionConsulta conDest : notificacion.getLstConsulta())
                {
                    if(conDest.getNotCnsTpo().equals(TipoConsulta.INC_DESTINATARIO))
                    {
                        String query = this.ProcesoTags(conDest.getNotCnsSQL(), registro);
                        
                        notificacion.getLstDestinatario().clear();
                        
                        retorno = this.ProcesarDestinatariosPorRegistro(notificacion, query);
                        
                        if(retorno.SurgioError())
                        {
                            return retorno;
                        }
                        
                        notificacion = (Notificacion) retorno.getObjeto();

                        //-AGREGO DESTINATARIOS ORIGINALES
                        for(NotificacionDestinatario dest : lstDestinatarioOriginal)
                        {
                            notificacion.getLstDestinatario().add(dest);
                        }
                        
                        for(NotificacionDestinatario dest : notificacion.getLstDestinatario())
                        {
                            asunto  = notificacion.getNotAsu();
                            if(dest.getPersona() != null) asunto = asunto.replace("[%=DESTINATARIO]", dest.getPersona().getPerCod().toString().trim());
                            if(dest.getEmail() != null) asunto = asunto.replace("[%=DESTINATARIO]", dest.getEmail().trim());
                            
                            lstEnvio.add(this.ArmarEnvio(notificacion, dest, this.ProcesoTags(contenido, registro).trim(), this.ProcesoTags(asunto, registro).trim()));
                        }
                    }
                }
            }
        }
        
        List<Object> lstObjeto = new ArrayList<>();
        
        for(SDT_NotificacionEnvio envio : lstEnvio)
        {
            lstObjeto.add(envio);
        }
            
        retorno.setLstObjetos(lstObjeto);
        
        return retorno;
        
    }

    /**
     * Procesar notificacion, donde el mensaje es agrupado
     * @param notificacion Notificacion
     * @param destinatario Destinatario
     * @param registros Registros
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj ProcesoMensajeAgrupado(Notificacion notificacion, NotificacionDestinatario destinatario, List registros){
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Proceso Mensaje Agrupado", TipoMensaje.MENSAJE));
        
        List<SDT_NotificacionEnvio> lstEnvio = new ArrayList<>();
        
        String asunto       = notificacion.getNotAsu();
        String contenido    = notificacion.getNotCon();
        
        String repeticionTags   = contenido.substring(contenido.indexOf("[%=INICIO_REPETICION]"), contenido.indexOf("[%=FIN_REPETICION]"));
        repeticionTags          = repeticionTags.replace("[%=INICIO_REPETICION]", "");
        
        //POSEO DESTINATARIO
        if(destinatario != null)
        {
            String repeticion       = "";  
            
            //UN ENVIO POR REGISTRO - DESTINATARIO
            Iterator iterator = registros.iterator();
            while(iterator.hasNext()){
                Object registro = iterator.next();
                
                repeticion +=  this.ProcesoTags(repeticionTags, registro).trim() + "\n";
                
            }
            
            if(destinatario.getPersona() != null) asunto = asunto.replace("[%=DESTINATARIO]", destinatario.getPersona().getPerCod().toString().trim());
            if(destinatario.getEmail() != null) asunto = asunto.replace("[%=DESTINATARIO]", destinatario.getEmail().trim());
            contenido = contenido.replace("[%=INICIO_REPETICION]" + repeticionTags + "[%=FIN_REPETICION]", repeticion);
            
            lstEnvio.add(this.ArmarEnvio(notificacion, destinatario, contenido, asunto));
            
        }
        else
        {
            //OBTENER DESTINATARIOS
            
            //UN ENVIO POR REGISTRO - DESTINATARIO
            Iterator iterator = registros.iterator();
            while(iterator.hasNext()){
                
                Object registro = iterator.next();
                
                for(NotificacionConsulta conDest : notificacion.getLstConsulta())
                {
                    if(conDest.getNotCnsTpo().equals(TipoConsulta.INC_DESTINATARIO))
                    {
                        String query = this.ProcesoTags(conDest.getNotCnsSQL(), registro);
                        
                        retorno = this.ProcesarDestinatariosPorRegistro(notificacion, query);
                        
                        if(retorno.SurgioError())
                        {
                            return retorno;
                        }
                        
                        notificacion = (Notificacion) retorno.getObjeto();
                    }
                }
            }
            
            
            for(NotificacionDestinatario dest : notificacion.getLstDestinatario())
            {
                
                String repeticion       = "";
                    
                 //UN ENVIO POR REGISTRO - DESTINATARIO
                Iterator ite_Registros = registros.iterator();
                while(ite_Registros.hasNext()){

                        Object registro = ite_Registros.next();
                        repeticion +=  this.ProcesoTags(repeticionTags, registro).trim() + "\n";

                }
                
                asunto  = notificacion.getNotAsu();
                
                if(dest.getPersona() != null) asunto = asunto.replace("[%=DESTINATARIO]", dest.getPersona().getPerCod().toString().trim());
                if(dest.getEmail() != null) asunto = asunto.replace("[%=DESTINATARIO]", dest.getEmail().trim());
                
                contenido = contenido.replace("[%=INICIO_REPETICION]" + repeticionTags + "[%=FIN_REPETICION]", repeticion.trim()).trim();

                lstEnvio.add(this.ArmarEnvio(notificacion, dest, contenido, asunto));
                
            }
        }
        
        List<Object> lstObjeto = new ArrayList<>();
        
        for(SDT_NotificacionEnvio envio : lstEnvio)
        {
            lstObjeto.add(envio);
        }
            
        retorno.setLstObjetos(lstObjeto);
        
        return retorno;
    }
    
    /**
     * Procesar tags en los mensajes
     * @param mensaje Mensaje
     * @param registro Registro de base de datos
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private String ProcesoTags(String mensaje, Object registro){
        List<String> tags = this.ObtenerTags(mensaje);
        
        Map map=(Map) registro;
        
        for(String unTag : tags)
        {
            String campo = unTag.replace("[%=", "");
            campo = campo.replace("]", "");
            
            if(map.containsKey(campo)) mensaje = mensaje.replace(unTag, map.get(campo).toString().trim()).trim();
        }
            
        return mensaje;
    }

    /**
     * Obtener tags en los mensajes
     * @param mensaje Mensajes
     * @return Lista de tags
     */
    private List<String> ObtenerTags(String mensaje){
        List<String> tags = new ArrayList<>();
        
        boolean continuar = true;
        int indice = 0;

        while(continuar){
            indice = mensaje.indexOf("[%=", indice);
            
            if(indice >= 0)
            {
                int fin = mensaje.indexOf("]", indice) + 1;
                String tag = mensaje.substring(indice, fin);
                tags.add(tag);
                
                indice  = fin;
            }
            else
            {
                continuar = false;
            }
        }
        
        return tags;
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //DESTINATARIOS
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Procesar destinatarios de una notificacion
     * @param notificacion Notificacion
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj ProcesarDestinatarios(Notificacion notificacion){
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Procesar destinatarios", TipoMensaje.MENSAJE));
        
        for(NotificacionConsulta consulta : notificacion.getLstConsulta())
        {
            if(consulta.getNotCnsTpo().equals(TipoConsulta.INC_DESTINATARIO))
            {
                retorno = loNotificacion.obtenerResultadosQuery(consulta.getNotCnsSQL());
                
                if(retorno.SurgioError())
                {
                    return retorno;
                }
                else
                {
                    List lista = (List) retorno.getObjeto();
                    Iterator iterator = lista.iterator();
                    while(iterator.hasNext()){
                        
                        NotificacionDestinatario destino = new NotificacionDestinatario();
                        
                        Map map=(Map)iterator.next();
                        
                        TipoDestinatario tpoDest = null;
                        String tpo = "";
                        String dst = "";
                        
                        //------------------------------------------------------------------------------------------
                        
                        if(map.containsKey("TIPO"))         tpo = map.get("TIPO").toString();
                        if(map.containsKey("DESTINATARIO")) dst = map.get("DESTINATARIO").toString();
                        
                        //------------------------------------------------------------------------------------------
                        
                        if(tpo != null) if(!tpo.isEmpty()) tpoDest = TipoDestinatario.fromCode(Integer.valueOf(tpo));
                        
                        if(tpoDest != null) if(tpoDest.equals(TipoDestinatario.EMAIL)) destino.setNotEmail(dst);
                        
                        if(tpoDest != null) if(tpoDest.equals(TipoDestinatario.PERSONA)) 
                            destino.setPersona((Persona) LoPersona.GetInstancia().obtener(Long.valueOf(dst)).getObjeto());
                        
                        //------------------------------------------------------------------------------------------
                        
                        if(destino.getNotEmail() != null || destino.getPersona() != null)
                        {
                            notificacion.getLstDestinatario().add(destino);
                        }
                        
                        //------------------------------------------------------------------------------------------
                        
                    }
                }
            }
        }
        
        retorno.setObjeto(notificacion);
        
        return retorno;
    }
    
    /**
     * Procesar destinatarios por cada registro
     * @param notificacion Notificacion
     * @param query Consulta SQL
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj ProcesarDestinatariosPorRegistro(Notificacion notificacion, String query){
        
        Retorno_MsgObj retorno = loNotificacion.obtenerResultadosQuery(query);

        if(retorno.SurgioError())
        {
            return retorno;
        }
        else
        {
            List lista = (List) retorno.getObjeto();
            Iterator iterator = lista.iterator();
            while(iterator.hasNext()){

                Map map=(Map)iterator.next();
                
                NotificacionDestinatario notDest = new NotificacionDestinatario();
                TipoDestinatario tpoDest = null;
                String tpo = "";
                String dst = "";

                //------------------------------------------------------------------------------------------

                if(map.containsKey("TIPO"))         tpo = map.get("TIPO").toString();
                if(map.containsKey("DESTINATARIO")) dst = map.get("DESTINATARIO").toString();

                //------------------------------------------------------------------------------------------

                if(tpo != null) if(!tpo.isEmpty()) tpoDest = TipoDestinatario.fromCode(Integer.valueOf(tpo));

                if(tpoDest != null) if(tpoDest.equals(TipoDestinatario.EMAIL)) notDest.setNotEmail(dst);

                if(tpoDest != null) if(tpoDest.equals(TipoDestinatario.PERSONA)) 
                    notDest.setPersona((Persona) LoPersona.GetInstancia().obtener(Long.valueOf(dst)).getObjeto());

                //------------------------------------------------------------------------------------------

                if(notDest.getNotEmail() != null || notDest.getPersona() != null)
                {
                    notificacion.getLstDestinatario().add(notDest);
                }

                //------------------------------------------------------------------------------------------
              

            }
        }
        
        retorno.setObjeto(notificacion);
        
        return retorno;
    }

    /**
     * Procesar destinatarios a excluir
     * @param lstDestinatario Lista de destinatarios
     * @param query Consulta SQL
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj ProcesarDestinatariosExcluir(List<SDT_Destinatario> lstDestinatario, String query){
        
        Retorno_MsgObj retorno = loNotificacion.obtenerResultadosQuery(query);
        
        List<Object> lstObjeto = new ArrayList<>();

        if(retorno.SurgioError())
        {
            return retorno;
        }
        else
        {
            List lista = (List) retorno.getObjeto();
            Iterator iterator = lista.iterator();
            while(iterator.hasNext()){

                
                Map map=(Map)iterator.next();
                
                
                SDT_Destinatario destinatario = new SDT_Destinatario();
                TipoDestinatario tpoDest = null;
                String tpo = "";
                String dst = "";

                //------------------------------------------------------------------------------------------

                if(map.containsKey("TIPO"))         tpo = map.get("TIPO").toString();
                if(map.containsKey("DESTINATARIO")) dst = map.get("DESTINATARIO").toString();

                //------------------------------------------------------------------------------------------

                if(tpo != null) if(!tpo.isEmpty()) tpoDest = TipoDestinatario.fromCode(Integer.valueOf(tpo));

                if(tpoDest != null) if(tpoDest.equals(TipoDestinatario.EMAIL)) destinatario.setEmail(dst);

                if(tpoDest != null) if(tpoDest.equals(TipoDestinatario.PERSONA)) destinatario.setPerCod(Long.valueOf(dst));

                //------------------------------------------------------------------------------------------

                if(destinatario.getEmail() != null || destinatario.getPerCod() != null)
                {
                    lstDestinatario.add(destinatario);
                }

                //------------------------------------------------------------------------------------------


            }
        }
        
        
        
        for(SDT_Destinatario destinatario : lstDestinatario)
        {
            lstObjeto.add(destinatario);
        }
            
        retorno.setLstObjetos(lstObjeto);

        return retorno;
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //BITACORA
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Procesar bitacora
     * @param bitacora Bitacora
     * @param notificacion Notificacion
     * @param asunto Asunto
     * @param contenido Contenido
     * @param destinatario Destinatario
     * @param estado Estado
     * @param mensaje Mensaje
     * @return Bitacora
     */
    private NotificacionBitacora ProcesoBitacora(NotificacionBitacora bitacora, Notificacion notificacion, String asunto, String contenido, String destinatario, NotificacionEstado estado, String mensaje){
        bitacora = this.CrearMensajeBitacora(bitacora, notificacion, asunto, contenido, destinatario, estado, mensaje);

        if(notificacion.getNotCod() != null)
        {
            if(bitacora.getNotBitCod() == null)
            {
                bitacora = this.AgregoBitacora(bitacora);
            }
            else
            {
                this.ActualizoBitacora(bitacora);
            }
        }
        
        return bitacora;
    }
    
    /**
     * Crear mensaje
     * @param bitacora Bitacora
     * @param notificacion Notificacion
     * @param asunto Asunto 
     * @param contenido Contenido
     * @param destinatario Destinatario 
     * @param estado Estado
     * @param mensaje Mensaje
     * @return  Bitacora
     */
    private NotificacionBitacora CrearMensajeBitacora(NotificacionBitacora bitacora, Notificacion notificacion, String asunto, String contenido, String destinatario, NotificacionEstado estado, String mensaje){
        
        
        if(bitacora == null) bitacora = new NotificacionBitacora();
        
        
        bitacora.setNotBitAsu(asunto);
        bitacora.setNotBitCon(contenido);
        bitacora.setNotBitDst(destinatario);
        bitacora.setNotBitFch(new java.util.Date());
        
        if(notificacion.getNotCod() != null) bitacora.setNotificacion((Notificacion) loNotificacion.obtener(notificacion.getNotCod()).getObjeto());
        
        if(estado != null) bitacora.setNotBitEst(estado);
        
        
        if(bitacora.getNotBitDet() != null) mensaje = bitacora.getNotBitDet() + "\n" + mensaje;
        bitacora.setNotBitDet(mensaje);
        
        return bitacora;
    }
    
    /**
     * Agregar bitacora
     * @param bitacora Bitacora
     * @return Bitacora
     */
    private NotificacionBitacora AgregoBitacora(NotificacionBitacora bitacora){
        bitacora = (NotificacionBitacora) loNotificacion.BitacoraAgregar(bitacora).getObjeto();
        return bitacora;
    }
    
    /**
     * Actualizar bitacora
     * @param bitacora  Bitacora
     */
    private void ActualizoBitacora(NotificacionBitacora bitacora){
        loNotificacion.BitacoraActualizar(bitacora);
    }
    
    
}
