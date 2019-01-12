/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Entidad.Objeto;
import Entidad.Parametro;
import Entidad.SincInconsistenciaDatos;
import Entidad.SincRegistroEliminado;
import Entidad.Sincronizacion;
import Entidad.SincronizacionInconsistencia;
import Enumerado.EstadoInconsistencia;
import Enumerado.EstadoSincronizacion;
import Enumerado.Objetos;
import Enumerado.TipoMensaje;
import Enumerado.TipoRetorno;
import Interfaz.InABMGenerico;
import Logica.Notificacion.NotificacionesInternas;
import Persistencia.PerManejador;
import SDT.SDT_Parameters;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import Utiles.Utilidades;
import WSClient.SincronizarWSClient;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author alvar
 */
public class LoSincronizacion implements InABMGenerico{

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static LoSincronizacion instancia;
    private final Utilidades util = Utilidades.GetInstancia();

    private LoSincronizacion() {
    }
    
    /**
     * Obtener instancia
     * @return Instancia de clase
     */
    public static LoSincronizacion GetInstancia(){
        if (instancia==null)
        {
            instancia   = new LoSincronizacion();
        }

        return instancia;
    }

    /**
     * Guardar sincronizacion
     * @param pObjeto Sincronizacion
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object guardar(Object pObjeto) {
        Sincronizacion sincro = (Sincronizacion) pObjeto;
        
        PerManejador perManager = new PerManejador();
            
        Retorno_MsgObj retorno = perManager.guardar(sincro);

        if(!retorno.SurgioErrorObjetoRequerido())
        {
            sincro.setSncCod((Long) retorno.getObjeto());
            retorno.setObjeto(sincro);
        }
            
        return retorno; 
    }

    /**
     * Actualizar sincronización
     * @param pObjeto Sincronización
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object actualizar(Object pObjeto) {
        
        Sincronizacion sincro  = (Sincronizacion) pObjeto;
            
        PerManejador perManager = new PerManejador();

        return perManager.actualizar(sincro);
    }

    /**
     * Eliminar sincronización
     * @param pObjeto Sincronización
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object eliminar(Object pObjeto) {
        PerManejador perManager = new PerManejador();
        return perManager.eliminar(pObjeto);
    }

    /**
     * Obtener sincronización
     * @param pObjeto Long - SincCod
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtener(Object pObjeto) {
        PerManejador perManager = new PerManejador();
        return perManager.obtener((Long) pObjeto, Sincronizacion.class);        
    }

    /**
     * Obtener lista de sincronización
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtenerLista() {
        
        PerManejador perManager = new PerManejador();

        return perManager.obtenerLista("Sincronizacion.findAll", null);
    }
    
    /**
     * Obtener lista de sincronziación por estado
     * @param estado Estado
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj obtenerListaByEstado(EstadoSincronizacion estado){
        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(estado, "SncEst"));
        
        PerManejador perManager = new PerManejador();
        
        return perManager.obtenerLista("Sincronizacion.findByEst", lstParametros);
    }
    
    /**
     * Existe sincronización sin corregir
     * @return Existe
     */
    public Boolean existenSincroSinCorregir(){
        Retorno_MsgObj retorno = this.obtenerListaByEstado(EstadoSincronizacion.CON_ERRORES);
        
        if(retorno.getLstObjetos() != null)
        {
            if(retorno.getLstObjetos().size() > 0)
            {
                return true;
            }
        }
        
        return false;
        
    }
    
    /**
     * Depurar, elimina todas las sincronizaciones
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj Depurar(){
        Retorno_MsgObj lst = this.obtenerLista();
        
        if(!lst.SurgioError())
        {
            if(lst.getLstObjetos() != null)
            {
                for(Object objeto : lst.getLstObjetos())
                {
                    this.eliminar(objeto);
                }
            }
        }
        
        lst.setLstObjetos(null);
        
        return lst;
    }
    
    /**
     * Seleccionar objeto valido de una inconsistencia
     * @param sinc Sincronización
     * @param IncCod Código de inconsistencia
     * @param IncObjCod Código de objeto
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj InconsistenciaSeleccionarObjeto(Sincronizacion sinc, Long IncCod, Long IncObjCod){
        
        SincronizacionInconsistencia inc = sinc.GetInconsistencia(IncCod);
        
        inc.GetIncDato(IncObjCod).setObjSel(Boolean.TRUE);
        
        PerManejador perManager = new PerManejador();
        
        Retorno_MsgObj retorno = (Retorno_MsgObj) perManager.actualizar(inc);

        return retorno;
    }
    
    //------------------------------------------------------------------------
    //OBJETO
    //------------------------------------------------------------------------

    /**
     * Guardar objeto
     * @param pObjeto Objeto
     * @return  Resultado: RETORNO_MSGOBJ
     */
    
    public Object ObjetoGuardar(Object pObjeto) {
        Objeto objeto = (Objeto) pObjeto;
        PerManejador perManager = new PerManejador();
            
        Retorno_MsgObj retorno = perManager.guardarPkManual(objeto);
        
        return retorno; 
    }

    /**
     * Actualizar objeto
     * @param pObjeto Objeto
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Object ObjetoActualizar(Object pObjeto) {
        
        Objeto objeto  = (Objeto) pObjeto;
            
        PerManejador perManager = new PerManejador();

        return perManager.actualizar(objeto);
        
    }

    /**
     * Eliminar Objeto
     * @param pObjeto Objeto
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Object ObjetoEliminar(Object pObjeto) {
        PerManejador perManager = new PerManejador();
        return perManager.eliminar(pObjeto);
    }

    /**
     * Obtener objeto
     * @param pObjeto Long - ObjCod
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ObjetoObtener(Object pObjeto) {
        PerManejador perManager = new PerManejador();
        return perManager.obtener((Long) pObjeto, Objeto.class);        
    }

    /**
     * Obtener lista de objetos
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ObjetoObtenerLista() {
        
        PerManejador perManager = new PerManejador();

        return perManager.obtenerLista("Objeto.findAll", null);
    }
    
    /**
     * Obtener objeto por nombre
     * @param ObjNom Nombre
     * @return Objeto
     */
    public Objeto ObjetoObtenerByNombre(String ObjNom) {
        
        PerManejador perManager = new PerManejador();
        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(ObjNom, "ObjNom"));
        Retorno_MsgObj retorno = perManager.obtenerLista("Objeto.findByObjeto", lstParametros);
        
        Objeto objeto = null;
        
        if(retorno.getLstObjetos() != null)
        {
            if(retorno.getLstObjetos().size() > 0)
            {
                objeto = (Objeto) retorno.getLstObjetos().get(0);
            }
        }
        
        return objeto;
    }
    
    /**
     * Carga inicial de objetos a sincronizar
     */
    public void CargaInicialObjetos(){
        if(this.ObjetoObtenerByNombre(Objetos.TIPO_EVALUACION.name()) == null)
        {
            this.ObjetoGuardar(new Objeto(Objetos.TIPO_EVALUACION.name()
                    , Objetos.TIPO_EVALUACION.getNamedQuery()
                    , Objetos.TIPO_EVALUACION.getPrimaryKey()
                    , Objetos.TIPO_EVALUACION.getClassName()));
            
            this.ObjetoGuardar(new Objeto(Objetos.PERSONA.name()
                    , Objetos.PERSONA.getNamedQuery()
                    , Objetos.PERSONA.getPrimaryKey()
                    , Objetos.PERSONA.getClassName()));
            
            this.ObjetoGuardar(new Objeto(Objetos.CARRERA.name()
                    , Objetos.CARRERA.getNamedQuery()
                    , Objetos.CARRERA.getPrimaryKey()
                    , Objetos.CARRERA.getClassName()));
            
            this.ObjetoGuardar(new Objeto(Objetos.PLAN_ESTUDIO.name()
                    , Objetos.PLAN_ESTUDIO.getNamedQuery()
                    , Objetos.PLAN_ESTUDIO.getPrimaryKey()
                    , Objetos.PLAN_ESTUDIO.getClassName()));

            this.ObjetoGuardar(new Objeto(Objetos.MATERIA.name()
                    , Objetos.MATERIA.getNamedQuery()
                    , Objetos.MATERIA.getPrimaryKey()
                    , Objetos.MATERIA.getClassName()));

            this.ObjetoGuardar(new Objeto(Objetos.MATERIA_PREVIA.name()
                    , Objetos.MATERIA_PREVIA.getNamedQuery()
                    , Objetos.MATERIA_PREVIA.getPrimaryKey()
                    , Objetos.MATERIA_PREVIA.getClassName()));

            this.ObjetoGuardar(new Objeto(Objetos.CURSO.name()
                    , Objetos.CURSO.getNamedQuery()
                    , Objetos.CURSO.getPrimaryKey()
                    , Objetos.CURSO.getClassName()));

            this.ObjetoGuardar(new Objeto(Objetos.MODULO.name()
                    , Objetos.MODULO.getNamedQuery()
                    , Objetos.MODULO.getPrimaryKey()
                    , Objetos.MODULO.getClassName()));

            this.ObjetoGuardar(new Objeto(Objetos.EVALUACION.name()
                    , Objetos.EVALUACION.getNamedQuery()
                    , Objetos.EVALUACION.getPrimaryKey()
                    , Objetos.EVALUACION.getClassName()));
            
            this.ObjetoGuardar(new Objeto(Objetos.PERIODO.name()
                    , Objetos.PERIODO.getNamedQuery()
                    , Objetos.PERIODO.getPrimaryKey()
                    , Objetos.PERIODO.getClassName()));

            this.ObjetoGuardar(new Objeto(Objetos.PERIODO_ESTUDIO.name()
                    , Objetos.PERIODO_ESTUDIO.getNamedQuery()
                    , Objetos.PERIODO_ESTUDIO.getPrimaryKey()
                    , Objetos.PERIODO_ESTUDIO.getClassName()));

            this.ObjetoGuardar(new Objeto(Objetos.PERIODO_ESTUDIO_ALUMNO.name()
                    , Objetos.PERIODO_ESTUDIO_ALUMNO.getNamedQuery()
                    , Objetos.PERIODO_ESTUDIO_ALUMNO.getPrimaryKey()
                    , Objetos.PERIODO_ESTUDIO_ALUMNO.getClassName()));

            this.ObjetoGuardar(new Objeto(Objetos.PERIODO_ESTUDIO_DOCENTE.name()
                    , Objetos.PERIODO_ESTUDIO_DOCENTE.getNamedQuery()
                    , Objetos.PERIODO_ESTUDIO_DOCENTE.getPrimaryKey()
                    , Objetos.PERIODO_ESTUDIO_DOCENTE.getClassName()));

            this.ObjetoGuardar(new Objeto(Objetos.PERIODO_ESTUDIO_DOCUMENTO.name()
                    , Objetos.PERIODO_ESTUDIO_DOCUMENTO.getNamedQuery()
                    , Objetos.PERIODO_ESTUDIO_DOCUMENTO.getPrimaryKey()
                    , Objetos.PERIODO_ESTUDIO_DOCUMENTO.getClassName()));
            
            this.ObjetoGuardar(new Objeto(Objetos.CALENDARIO.name()
                    , Objetos.CALENDARIO.getNamedQuery()
                    , Objetos.CALENDARIO.getPrimaryKey()
                    , Objetos.CALENDARIO.getClassName()));
 
            this.ObjetoGuardar(new Objeto(Objetos.CALENDARIO_ALUMNO.name()
                    , Objetos.CALENDARIO_ALUMNO.getNamedQuery()
                    , Objetos.CALENDARIO_ALUMNO.getPrimaryKey()
                    , Objetos.CALENDARIO_ALUMNO.getClassName()));
 
            this.ObjetoGuardar(new Objeto(Objetos.CALENDARIO_DOCENTE.name()
                    , Objetos.CALENDARIO_DOCENTE.getNamedQuery()
                    , Objetos.CALENDARIO_DOCENTE.getPrimaryKey()
                    , Objetos.CALENDARIO_DOCENTE.getClassName()));
 
            this.ObjetoGuardar(new Objeto(Objetos.ESCOLARIDAD.name()
                    , Objetos.ESCOLARIDAD.getNamedQuery()
                    , Objetos.ESCOLARIDAD.getPrimaryKey()
                    , Objetos.ESCOLARIDAD.getClassName()));

            this.ObjetoGuardar(new Objeto(Objetos.INSCRIPCION.name()
                    , Objetos.INSCRIPCION.getNamedQuery()
                    , Objetos.INSCRIPCION.getPrimaryKey()
                    , Objetos.INSCRIPCION.getClassName()));

            this.ObjetoGuardar(new Objeto(Objetos.MATERIA_REVALIDA.name()
                    , Objetos.MATERIA_REVALIDA.getNamedQuery()
                    , Objetos.MATERIA_REVALIDA.getPrimaryKey()
                    , Objetos.MATERIA_REVALIDA.getClassName()));

            this.ObjetoGuardar(new Objeto(Objetos.SOLICITUD.name()
                    , Objetos.SOLICITUD.getNamedQuery()
                    , Objetos.SOLICITUD.getPrimaryKey()
                    , Objetos.SOLICITUD.getClassName()));
            
            this.ObjetoGuardar(new Objeto(Objetos.ARCHIVO.name()
                    , Objetos.ARCHIVO.getNamedQuery()
                    , Objetos.ARCHIVO.getPrimaryKey()
                    , Objetos.ARCHIVO.getClassName()));
        }
    }
    
    //------------------------------------------------------------------------
    //SINCRONIZAR
    //------------------------------------------------------------------------

    /**
     * Inicia sincronización local
     */
    public void Sincronizar(){
        Parametro param = LoParametro.GetInstancia().obtener();
        
        if(param.getParSisLocal() && param.getParSncAct())
        {
            if(!this.existenSincroSinCorregir())
            {
                Date inicioProceso = new Date();
                
                Sincronizacion sincro = new Sincronizacion();
                sincro.addDetalle(dateFormat.format(inicioProceso)  + " - Inicio el proceso de sincronización");
        
                Date fechaUltimaSincronizacion  = param.getParFchUltSinc();

                /*
                    Busco registros locales para determinar si debo enviar al sistema online
                */

                Retorno_MsgObj resulSincOnline = this.SincronizarSistemaOnline(this.ObtenerCambios(fechaUltimaSincronizacion));

                if(resulSincOnline.SurgioError())
                {
                    System.err.println("Surgio error al sincronizar con el sistema online");
                    sincro.setSncEst(EstadoSincronizacion.CON_ERRORES);
                    sincro = this.GenerarInconsistencias(sincro, resulSincOnline);
                    
                }
                else
                {
                    sincro.addDetalle(dateFormat.format(new Date()) + " - " + resulSincOnline.getMensaje().getMensaje());

                    /*
                        Impactar modificaciones en sistema local
                    */

                    Retorno_MsgObj resultado = this.ImpactarCambios(resulSincOnline);
                
                    if(resultado.SurgioError())
                    {
                        sincro.setSncEst(EstadoSincronizacion.CON_ERRORES);
                    }
                    else
                    {
                        sincro.setSncEst(EstadoSincronizacion.CORRECTO);
                    }
                    
                    sincro.addDetalle(resultado.getMensaje().getMensaje());
                    sincro.setSncObjCnt((int) resultado.getObjeto());
                    
                    //Actualizo la fecha de sincronización
                    this.ActualizarFechaSincronizacion(param);
                }
                
                sincro.setSncFch(inicioProceso);
                sincro.setSncDur(this.ObtenerDuracion(inicioProceso));
                sincro.addDetalle(dateFormat.format(new Date()) + " - Fin del proceso");


                this.guardar(sincro);
                
            }
        }
        
    }
    
    /**
     * Inicia sincronización online
     * @param cambios Cambios de sistema local
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj Sincronizar(Retorno_MsgObj cambios){
        Parametro param                 = LoParametro.GetInstancia().obtener();
        Retorno_MsgObj cambiosLocales   = this.ObtenerCambios(param.getParFchUltSinc());
        
        System.err.println("Cambios recibidos: " + cambios.getLstObjetos().size());
        System.err.println("Cambios locales: " + cambiosLocales.getLstObjetos().size());
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Iniciando sincronizacion online", TipoMensaje.MENSAJE));
        
        if(this.ExistenCambios(param.getParFchUltSinc()))
        {
            retorno = this.ValidarSincronizacion(cambiosLocales, cambios);
        }
        
        if(!retorno.SurgioError())
        {
            retorno = this.ImpactarCambios(cambios);
            
            if(!retorno.SurgioError())
            {
                cambiosLocales.setMensaje(retorno.getMensaje());
                retorno = cambiosLocales;
                
                System.err.println("Cambios locales 2: " + retorno.getLstObjetos().size());
            }
        }
        else
        {
            retorno = this.ArmarInconsistenciaRetorno(cambiosLocales, cambios);
        }
        
        return retorno;
    }

    /**
     * Determina si existen cambios a partir de una fecha
     * @param fecha Fecha
     * @return Existen cambios
     */
    private Boolean ExistenCambios(Date fecha){
        
        Retorno_MsgObj retorno = this.ObtenerCambios(fecha);
        
        if(retorno.getLstObjetos() != null)
        {
            if(retorno.getLstObjetos().size() > 0)
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Obtener cambios a partir de una fecha
     * @param fecha Fecha
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj ObtenerCambios(Date fecha){
        PerManejador perManager = new PerManejador();
        
        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(fecha, "ObjFchMod"));
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Obtener cambios", TipoMensaje.MENSAJE));
        retorno.setLstObjetos(new ArrayList<>());
        
        
        //OBTENER ELIMINACIONES
        Retorno_MsgObj Deletes      = new Retorno_MsgObj(new Mensajes(TipoRetorno.ELIMINACION.name(), TipoMensaje.MENSAJE));
        Retorno_MsgObj lstObjeto    = perManager.obtenerLista("SincRegistroELiminado.findModAfter", lstParametros);

        if(lstObjeto.getLstObjetos() != null)
        {
            if(lstObjeto.getLstObjetos().size() > 0)
            {
                Deletes.setLstObjetos(lstObjeto.getLstObjetos());
                retorno.getLstObjetos().add(Deletes);
            }
        }
        
        
        
        //--------------------------------------------------------------------------------------------------------------

        //OBTENER INSERTS Y UPDATES
        Retorno_MsgObj InsertsUpdates = new Retorno_MsgObj(new Mensajes(TipoRetorno.INSERT_UPDATE.name(), TipoMensaje.MENSAJE));
        
        lstObjeto = this.ObjetoObtenerLista();
        if(lstObjeto.getLstObjetos() != null)
        {
            if(lstObjeto.getLstObjetos().size() > 0)
            {
                InsertsUpdates.setLstObjetos(new ArrayList<>());
                
                for(Object obj : lstObjeto.getLstObjetos())
                {
                    Objeto objeto       = (Objeto) obj;
                    
                    /*
                        Llamo a la query por defecto, para obtener la cantidad de registros modificados despues de X fecha
                    */
                    
                    Retorno_MsgObj modObjects = perManager.obtenerLista(objeto.getObjNmdQry() + ".findModAfter", lstParametros);
                    if(modObjects.getLstObjetos() != null)
                    {
                        if(modObjects.getLstObjetos().size() > 0)
                        {
                            System.err.println("Objeto: " + objeto.getObjNmdQry());
                            System.err.println("Cambios: " + modObjects.getLstObjetos().toString());
                            Retorno_MsgObj objetoModificado = new Retorno_MsgObj();
                            
                            objetoModificado.setObjeto(obj);
                            objetoModificado.setLstObjetos(modObjects.getLstObjetos());
                            
                            InsertsUpdates.getLstObjetos().add(objetoModificado);
                        }
                    }
                }
                
                retorno.getLstObjetos().add(InsertsUpdates);
            }
        }
        
        return retorno;
    }

    /**
     * Enviar y obtener cambios del sistema online 
     * @param modificaciones Notificaciones
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj SincronizarSistemaOnline(Retorno_MsgObj modificaciones){
        SincronizarWSClient cliWS = new SincronizarWSClient();
        return cliWS.Sincronizar(modificaciones);
    }
    
    /**
     * Impactar cambios 
     * @param cambios Cambios
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj ImpactarCambios(Retorno_MsgObj cambios){
        
        ForeignKeyControl(false);
        
        Integer registrosAfectados = 0;

        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes(dateFormat.format(new Date()) + " - Impactando cambios", TipoMensaje.MENSAJE), registrosAfectados);
        
        if(cambios.getLstObjetos() != null)
        {
            PerManejador perManager = new PerManejador();
            
            if(cambios.getLstObjetos().size() > 0)
            {
                if(cambios.getLstObjetos().size() > 2)
                {
                    retorno.getMensaje().setMensaje(retorno.getMensaje().getMensaje() + "\n" + dateFormat.format(new Date()) + " - Solo se pueden recibir dos elementos, registros y eliminaciones.");
                    retorno.getMensaje().setTipoMensaje(TipoMensaje.ERROR);
                    
                    System.err.println("ERROR: Solo se pueden recibir dos elementos, registros y eliminaciones.");
                }
                else
                {
                    //OBTENGO LISTA MODIFICACIONES Y LISTA DE INGRESOS
                    Retorno_MsgObj Deletes      = new Retorno_MsgObj();
                    Retorno_MsgObj InsertUpdate = new Retorno_MsgObj();
                    
                        
                    for(Object obj : cambios.getLstObjetos())
                    {
                        Retorno_MsgObj auxiliar = (Retorno_MsgObj) obj;
                        if(auxiliar.getMensaje().getMensaje().equals(TipoRetorno.ELIMINACION.name()))
                        {
                            Deletes = auxiliar;
                            registrosAfectados += Deletes.getLstObjetos().size();
                        }
                        if(auxiliar.getMensaje().getMensaje().equals(TipoRetorno.INSERT_UPDATE.name()))
                        {
                            InsertUpdate = auxiliar;
                        }
                    }
                    
                    
                    //ELIMINACIONES
                    if(Deletes.getLstObjetos() != null)
                    {
                        
                        for(Object deleted : Deletes.getLstObjetos())
                        {
                            SincRegistroEliminado objetoEliminado = (SincRegistroEliminado) deleted;

                            this.EliminarRegistro(objetoEliminado);                          
                        }
                    }
                    
                    
                    //MODIFICACIONES E INGRESOS                  
                    if(InsertUpdate.getLstObjetos() != null)
                    {
                        for(Object camb : InsertUpdate.getLstObjetos())
                        {
                            Retorno_MsgObj objetoModificado = (Retorno_MsgObj) camb;

                            //Objeto modificado
                            Objeto objMod = (Objeto) objetoModificado.getObjeto();
                            
                            registrosAfectados += objetoModificado.getLstObjetos().size();

                            for(Object registro : objetoModificado.getLstObjetos())
                            {
                                if(this.ExisteRegistro(registro))
                                {
                                    perManager.ejecutarUpdateQuery(util.ObtenerUpdateQuery(registro));
                                }
                                else
                                {
                                    perManager.ejecutarQuery(util.ObtenerInsertQuery(registro));
                                    /*
                                    Long idOriginal = util.ObtenerPrimaryKey(registro);

                                    SincRetorno regNuevo = perManager.guardar(registro);
                                    
                                    if(!regNuevo.SurgioError())
                                    {
                                        Long idGenerado = (long) regNuevo.getObjeto();
                                        this.ActualizarPrimaryKeyManualmente(objMod, idGenerado, idOriginal);
                                    }
                                    else
                                    {
                                        retorno.setMensaje(regNuevo.getMensaje());
                                        return retorno;
                                    }
                                    */

                                }
                            }

                        }
                    }
                }
            }
        }
        
        ForeignKeyControl(true);
        
        retorno.setObjeto(registrosAfectados);
        
        return retorno;
    }

    /**
     * Determina si un registro ya existe en el sistema
     * @param registro Registro
     * @return Existe registro
     */
    private Boolean ExisteRegistro(Object registro){
        
        PerManejador perManager = new PerManejador();
        return !perManager.obtener(util.ObtenerPrimaryKey(registro), registro.getClass()).SurgioErrorObjetoRequerido();
        
    }

    /**
     * Determina si existe un objeto
     * @param codigo Código
     * @param objeto Objeto
     * @return Existe registro
     */
    private Boolean ExisteRegistro(Long codigo, Object objeto){
        
        PerManejador perManager = new PerManejador();
        return !perManager.obtener(codigo, objeto.getClass()).SurgioErrorObjetoRequerido();
        
    }
    
    /**
     * Activa o desactiva el control de claves foraneas.
     * Se suele desactivar cuando se impactan los cambios, y se activa cuando finaliza
     * @param activo Activar
     */
    private void ForeignKeyControl(Boolean activo){
        
        String query = "SET foreign_key_checks = " + activo;
        
        PerManejador perManager = new PerManejador();
        Retorno_MsgObj retorno  = perManager.ejecutarQuery(query);
        
        if(retorno.SurgioError())
        {
            System.err.println(retorno.getMensaje().toString());
        }
        
    }

    /**
     * Eliminar registro
     * @param objEliminado Registro
     */
    private void EliminarRegistro(SincRegistroEliminado objEliminado){
        
        if(this.ExisteRegistro(objEliminado.getObjElimCod(), util.GetObjectByName(objEliminado.getObjeto().getObjClsNom())))
        {
            String query = "DELETE FROM " + objEliminado.getObjeto().getObjNmdQry() 
                        + " WHERE "+ objEliminado.getObjeto().getPrimaryKey().getObjCmpNom() 
                        +" = " + objEliminado.getObjElimCod();


            PerManejador perManager = new PerManejador();
            Retorno_MsgObj retorno  = perManager.ejecutarUpdateQuery(query);

            if(retorno.SurgioError())
            {
                System.err.println(retorno.getMensaje().toString());
            }
        }
        
    }

    /**
     * Actualizar fecha de sinronización
     * @param param Parametros
     */
    private void ActualizarFechaSincronizacion(Parametro param){
        param.setParFchUltSinc(new Date());
        LoParametro.GetInstancia().actualizar(param);
        
        //Actualizar fecha al sistema online
        SincronizarWSClient cliWS = new SincronizarWSClient();
        cliWS.ActualizarFecha(param.getParFchUltSinc());
    }

    /**
     * Generar inconsistencias
     * @param sincro Sincronización
     * @param retorno Resultado: RETORNO_MSGOBJ
     * @return Retorna sincronización
     */
    private Sincronizacion GenerarInconsistencias(Sincronizacion sincro, Retorno_MsgObj retorno){
        if(retorno.getMensaje().getMensaje().equals(TipoRetorno.INCONSISTENCIA.name()))
        {
            NotificacionesInternas notInt = new NotificacionesInternas();
            notInt.Notificar_NUEVA_INCONSISTENCIA();
            
            sincro.addDetalle(dateFormat.format(new Date()) + " - Surgio error al sincronizar con el sistema online - Genero inconsistencias que deberan ser corregidas");
            
            if(retorno.getLstObjetos() != null)
            {
                for(Object obj : retorno.getLstObjetos())
                {
                    SincronizacionInconsistencia sncInc = (SincronizacionInconsistencia) obj;
                    sncInc.setSincronizacion(sincro);
                    sincro.getLstInconsistencia().add(sncInc);
                }
            }
        }
        else
        {
            sincro.addDetalle(dateFormat.format(new Date()) + " - " + retorno.getMensaje().toString());
        }
        
        return sincro;
    }
    
    /**
     * Calcular duración del proceso de sincronización
     * @param inicioProceso Fecha de inicio del proceso
     * @return Duración en lenguaje humano
     */
    private String ObtenerDuracion(Date inicioProceso){
        Long tiempoMls  =  new Date().getTime() - inicioProceso.getTime();
        String duracion;
        
        if(tiempoMls > 1000)
        {
            Long tiempoS = tiempoMls / 1000;
            
            if(tiempoS > 60)
            {
                Long tiempoM = tiempoS / 60;
                if(tiempoM > 60)
                {
                    Long tiempoH = tiempoM / 60;
                    if(tiempoH > 24)
                    {
                        Long tiempoD = tiempoH / 24;
                        
                        duracion = tiempoD + " días";
                    }
                    else
                    {
                        duracion = tiempoH + " horas";
                    }
                }
                else
                {
                    duracion = tiempoM + " minutos";
                }
            }
            else
            {
                duracion = tiempoS + " segundos";
            }
        }
        else
        {
            duracion = tiempoMls + " milisegundos";
        }
        
        return duracion;
    }
    
    /**
     * Validar sincronización. Analiza que no existan inconsistencias
     * @param cambiosLocales Cambios locales
     * @param cambiosNuevos Cambios nuevos
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj ValidarSincronizacion(Retorno_MsgObj cambiosLocales, Retorno_MsgObj cambiosNuevos){
        /*
        * Verifico que no existan inconsistencias.
        */
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Importacion valida", TipoMensaje.MENSAJE));
        retorno.setLstObjetos(new ArrayList<>());
        
        if(cambiosNuevos.getLstObjetos() != null)
        {
            if(cambiosNuevos.getLstObjetos().size() > 0)
            {
                if(cambiosNuevos.getLstObjetos().size() > 2)
                {
                    retorno.getMensaje().setMensaje(retorno.getMensaje().getMensaje() + "\n" + dateFormat.format(new Date()) + " - Solo se pueden recibir dos elementos, registros y eliminaciones.");
                    retorno.getMensaje().setTipoMensaje(TipoMensaje.ERROR);
                    
                    System.err.println("ERROR: Solo se pueden recibir dos elementos, registros y eliminaciones.");
                }
                else
                {
                    //OBTENGO LISTA MODIFICACIONES Y LISTA DE INGRESOS
                    Retorno_MsgObj InsertUpdate = new Retorno_MsgObj();
                    
                        
                    for(Object obj : cambiosNuevos.getLstObjetos())
                    {
                        Retorno_MsgObj auxiliar = (Retorno_MsgObj) obj;
                        if(auxiliar.getMensaje().getMensaje().equals(TipoRetorno.INSERT_UPDATE.name()))
                        {
                            InsertUpdate = auxiliar;
                        }
                    }
                    
                    //MODIFICACIONES E INGRESOS                  
                    if(InsertUpdate.getLstObjetos() != null)
                    {
                        
                        for(Object camb : InsertUpdate.getLstObjetos())
                        {
                            Retorno_MsgObj objetoModificado = (Retorno_MsgObj) camb;

                            //Objeto modificado
                            Objeto objMod = (Objeto) objetoModificado.getObjeto();

                            for(Object registro : objetoModificado.getLstObjetos())
                            {
                                //VALIDAR
                                Retorno_MsgObj validar = this.ValidarSincronizacionNivelDos(objMod, registro, cambiosLocales);
                                if(validar.SurgioError())
                                {
                                    if(validar.getMensaje().getMensaje().equals(TipoRetorno.INCONSISTENCIA.name()))
                                    {
                                        //Agrego inconsistencia
                                        //retorno.getLstObjetos().add(validar.getObjeto());
                                        retorno.setMensaje(validar.getMensaje());
                                        return retorno;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return retorno;
    }
    
    /**
     * Validar segundo nivel de sincronización
     * @param objMod Objeto modificado
     * @param registro Registro
     * @param cambiosLocales Cambios locales
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj ValidarSincronizacionNivelDos(Objeto objMod, Object registro, Retorno_MsgObj cambiosLocales){
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Sin inconsistencia", TipoMensaje.MENSAJE));
        
        if(cambiosLocales.getLstObjetos() != null)
        {
            if(cambiosLocales.getLstObjetos().size() > 0)
            {
                if(cambiosLocales.getLstObjetos().size() > 2)
                {
                    retorno.getMensaje().setMensaje(retorno.getMensaje().getMensaje() + "\n" + dateFormat.format(new Date()) + " - Solo se pueden recibir dos elementos, registros y eliminaciones.");
                    retorno.getMensaje().setTipoMensaje(TipoMensaje.ERROR);
                    
                    System.err.println("ERROR: Solo se pueden recibir dos elementos, registros y eliminaciones.");
                }
                else
                {
                    //OBTENGO LISTA MODIFICACIONES Y LISTA DE INGRESOS
                    Retorno_MsgObj InsertUpdate = new Retorno_MsgObj();
                    
                        
                    for(Object obj : cambiosLocales.getLstObjetos())
                    {
                        Retorno_MsgObj auxiliar = (Retorno_MsgObj) obj;
                        if(auxiliar.getMensaje().getMensaje().equals(TipoRetorno.INSERT_UPDATE.name()))
                        {
                            InsertUpdate = auxiliar;
                        }
                    }
                    
                    //MODIFICACIONES E INGRESOS                  
                    if(InsertUpdate.getLstObjetos() != null && objMod != null)
                    {
                        
                        for(Object camb : InsertUpdate.getLstObjetos())
                        {
                            Retorno_MsgObj objetoModificado = (Retorno_MsgObj) camb;

                            //Objeto modificado
                            Objeto objMd = (Objeto) objetoModificado.getObjeto();

                            for(Object reg : objetoModificado.getLstObjetos())
                            {
                                if(objMod.equals(objMd))
                                {
                                    //VALIDAR
                                    if(this.EsInconsistencia(registro, reg))
                                    {
                                        //ERROR,SIGNIFICA QUE AMBOS MODIFICARON EL MISMO DATO, RETORNA INCONSISTENCIA
                                        retorno.setObjeto(this.ArmarInconsistencia(objMd, registro, reg));
                                        retorno.setMensaje(new Mensajes(TipoRetorno.INCONSISTENCIA.name(), TipoMensaje.ERROR));
                                        return retorno;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }  
        
        return retorno;
    }
    
    //------------------------------------------------------------------------
    //INCONSISTENCIAS
    //------------------------------------------------------------------------
    
    /**
     * Determina si los dos objetos son inconsistentes
     * @param objLocal Objeto local
     * @param objNuevo Objeto nuevo
     * @return Es inconsistencia
     */
    private Boolean EsInconsistencia(Object objLocal, Object objNuevo){
        return objLocal.equals(objNuevo);

    }

    /**
     * Crea objeto de inconsistencia para retorno
     * @param cambiosLocales Cambios locales
     * @param cambiosNuevos Cambios nuevos
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj ArmarInconsistenciaRetorno(Retorno_MsgObj cambiosLocales, Retorno_MsgObj cambiosNuevos){
        /*
            Este procedimiento actua cuando se conoce una inconsistencia, por ello arma un registro de inconsistencias 
            con los datos duplicados, y un registro individual con todos los cambios, para no perder otras modificaciones 
            cuando se corrijan las diferencias
        */
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes(TipoRetorno.INCONSISTENCIA.name(), TipoMensaje.ERROR));
        List<Object> lstObjModificados = new ArrayList<>();
        
        /*
            Recorro cambios locales
        */
        
        if(cambiosLocales.getLstObjetos() != null)
        {
            if(cambiosLocales.getLstObjetos().size() > 0)
            {
                if(cambiosLocales.getLstObjetos().size() > 2)
                {
                    retorno.getMensaje().setMensaje(retorno.getMensaje().getMensaje() + "\n" + dateFormat.format(new Date()) + " - Solo se pueden recibir dos elementos, registros y eliminaciones.");
                    retorno.getMensaje().setTipoMensaje(TipoMensaje.ERROR);
                    
                    System.err.println("ERROR: Solo se pueden recibir dos elementos, registros y eliminaciones.");
                }
                else
                {
                    //OBTENGO LISTA MODIFICACIONES Y LISTA DE INGRESOS
                    Retorno_MsgObj InsertUpdate = new Retorno_MsgObj();
                    
                        
                    for(Object obj : cambiosLocales.getLstObjetos())
                    {
                        Retorno_MsgObj auxiliar = (Retorno_MsgObj) obj;
                        if(auxiliar.getMensaje().getMensaje().equals(TipoRetorno.INSERT_UPDATE.name()))
                        {
                            InsertUpdate = auxiliar;
                        }
                    }
                    
                    //MODIFICACIONES E INGRESOS                  
                    if(InsertUpdate.getLstObjetos() != null)
                    {
                        
                        for(Object camb : InsertUpdate.getLstObjetos())
                        {
                            Retorno_MsgObj objetoModificado = (Retorno_MsgObj) camb;

                            //Objeto modificado
                            Objeto objMod = (Objeto) objetoModificado.getObjeto();

                            for(Object registro : objetoModificado.getLstObjetos())
                            {
                                //lstObjModificados = this.AgregarObjetoModificado(registro);
                                
                                //VALIDAR
                                Retorno_MsgObj validar = this.ValidarSincronizacionNivelDos(objMod, registro, cambiosNuevos);
                                if(validar.SurgioError())
                                {
                                    if(validar.getMensaje().getMensaje().equals(TipoRetorno.INCONSISTENCIA.name()))
                                    {
                                        //Agrego inconsistencia
                                        retorno.getLstObjetos().add(validar.getObjeto());
                                        lstObjModificados.add(registro);
                                    }
                                }
                            }
                        }
                    }
                }
                
                retorno = this.ArmarInconsistenciaComun(retorno, lstObjModificados, cambiosLocales, cambiosNuevos);
                
            }
        }
        
        return retorno;
    }

    /**
     * Armar inconsistencia comun
     * @param retorno Retorno
     * @param lstObjModificados Objetos modificados
     * @param cambiosLocales Cambios locales
     * @param cambiosNuevos Cambios nuevos
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj ArmarInconsistenciaComun(Retorno_MsgObj retorno, List<Object> lstObjModificados, Retorno_MsgObj cambiosLocales, Retorno_MsgObj cambiosNuevos){
        retorno = this.ArmarInconsistenciaAuxiliar(retorno, lstObjModificados, cambiosNuevos);
        retorno = this.ArmarInconsistenciaAuxiliar(retorno, lstObjModificados, cambiosLocales);
        return retorno;
    }
    
    /**
     * Metodo Auxiliar del armado de inconsistencia
     * @param retorno Retorno
     * @param lstObjModificados Objetos modificados
     * @param cambios Cambios
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj ArmarInconsistenciaAuxiliar(Retorno_MsgObj retorno, List<Object> lstObjModificados, Retorno_MsgObj cambios){
        
        if(cambios.getLstObjetos() != null)
        {
            if(cambios.getLstObjetos().size() > 0)
            {
                if(cambios.getLstObjetos().size() > 2)
                {
                    retorno.getMensaje().setMensaje(retorno.getMensaje().getMensaje() + "\n" + dateFormat.format(new Date()) + " - Solo se pueden recibir dos elementos, registros y eliminaciones.");
                    retorno.getMensaje().setTipoMensaje(TipoMensaje.ERROR);
                    
                    System.err.println("ERROR: Solo se pueden recibir dos elementos, registros y eliminaciones.");
                }
                else
                {
                    //OBTENGO LISTA MODIFICACIONES Y LISTA DE INGRESOS
                    Retorno_MsgObj InsertUpdate = new Retorno_MsgObj();
                    
                        
                    for(Object obj : cambios.getLstObjetos())
                    {
                        Retorno_MsgObj auxiliar = (Retorno_MsgObj) obj;
                        if(auxiliar.getMensaje().getMensaje().equals(TipoRetorno.INSERT_UPDATE.name()))
                        {
                            InsertUpdate = auxiliar;
                        }
                    }
                    
                    //MODIFICACIONES E INGRESOS                  
                    if(InsertUpdate.getLstObjetos() != null)
                    {
                        
                        for(Object camb : InsertUpdate.getLstObjetos())
                        {
                            Retorno_MsgObj objetoModificado = (Retorno_MsgObj) camb;

                            //Objeto modificado
                            Objeto objMd = (Objeto) objetoModificado.getObjeto();

                            for(Object reg : objetoModificado.getLstObjetos())
                            {
                                if(!lstObjModificados.contains(reg))
                                {
                                    //ERROR,SIGNIFICA QUE AMBOS MODIFICARON EL MISMO DATO, RETORNA INCONSISTENCIA
                                    SincronizacionInconsistencia inc = this.ArmarInconsistencia(objMd, reg, null);
                                    inc.getLstDatos().get(0).setObjSel(Boolean.TRUE);
                                    inc.setIncEst(EstadoInconsistencia.CORRECTO);
                                    lstObjModificados.add(reg);
                                    
                                    retorno.getLstObjetos().add(inc);
                                }
                            }
                        }
                    }
                }
            }
        }  
        
        return retorno;
    }
    
    /**
     * Armar objeto inconsistencia
     * @param objeto Objeto
     * @param regUno Registro inconsistente uno
     * @param regDos Registro inconsistente dos
     * @return Inconsistencia
     */
    private SincronizacionInconsistencia ArmarInconsistencia(Objeto objeto, Object regUno, Object regDos){
        
        SincronizacionInconsistencia inc = new SincronizacionInconsistencia(objeto);
        inc.setIncEst(EstadoInconsistencia.CON_ERRORES);

        ArrayList<SincInconsistenciaDatos> lstDatos = new ArrayList<>();
        
        if(regUno != null) lstDatos.add(new SincInconsistenciaDatos(inc, util.ObjetoToJson(regUno)));
        if(regDos != null) lstDatos.add(new SincInconsistenciaDatos(inc, util.ObjetoToJson(regDos)));
        
        inc.setLstDatos(lstDatos);
        
        return inc;
    }
    
    /**
     * Procesar inconsistencia
     * @param sinc Sincronizacion
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ProcesarInconsistencia(Sincronizacion sinc){
        Retorno_MsgObj retorno = this.ValidarInconsistencias(sinc);
        Parametro param = LoParametro.GetInstancia().obtener();
        
        if(!retorno.SurgioError())
        {
            PerManejador perManager = new PerManejador();

            if(sinc.getLstInconsistencia() != null)
            {
                retorno = this.ImpactarInconsistencia(sinc);
                
                if(!retorno.SurgioError())
                {
                    //Enviar a ws para impactar forzado

                    Retorno_MsgObj parametro = new Retorno_MsgObj();
                    parametro.setObjeto(sinc);
                    
                    SincronizarWSClient cliWS = new SincronizarWSClient();
                    retorno = cliWS.ImpactarInconsistencia(parametro);
                    
                }

                if(!retorno.SurgioError())
                {
                    //Actualizo estado de inconsistencias y de sincronizacion
                    for(SincronizacionInconsistencia inc : sinc.getLstInconsistencia())
                    {
                        inc.setIncEst(EstadoInconsistencia.CORRECTO);
                        retorno = perManager.actualizar(inc);
                    }

                    if(!retorno.SurgioError())
                    {
                        sinc.addDetalle(dateFormat.format(new Date()) + " - " + retorno.getMensaje().getMensaje());
                        sinc.setSncEst(EstadoSincronizacion.CORRECTO);
                        retorno = perManager.actualizar(sinc);
                        
                        if(!retorno.SurgioError())
                        {
                            //ACTUALIZO FECHAS
                            this.ActualizarFechaSincronizacion(param);                            
                        }
                        else
                        {
                            System.err.println("Error " + retorno.getMensaje().toString());
                        }
                    }
                    else
                    {
                        System.err.println("Error " + retorno.getMensaje().toString());
                    }
                }
            }
            else 
            {
                System.err.println("Error " + retorno.getMensaje().toString());
            }
        }

        return retorno;
    }
    
    /**
     * Procesar inconsistencia
     * @param objeto Objeto
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ProcesarInconsistencia(Retorno_MsgObj objeto){
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Impactando inconsistencias recibidas por servicio web", TipoMensaje.MENSAJE));
        
        if(objeto.getObjeto() != null)
        {
            Sincronizacion sinc = (Sincronizacion) objeto.getObjeto();
            retorno = this.ImpactarInconsistencia(sinc);
        }
        else
        {
            retorno.setMensaje(new Mensajes("No se recibio sincronizacion", TipoMensaje.ERROR));
        }
        
        return retorno;
    }
    
    /**
     * Impactar inconsistencia 
     * @param sinc Sincronización
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj ImpactarInconsistencia(Sincronizacion sinc){
        ForeignKeyControl(false);
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Impactando inconsistencia", TipoMensaje.MENSAJE));
        if(sinc.getLstInconsistencia() != null)
        {
            PerManejador perManager = new PerManejador();

            for(SincronizacionInconsistencia inc : sinc.getLstInconsistencia())
            {
                SincInconsistenciaDatos dato = inc.getObjetoSeleccionado();

                Object objeto = util.GetObjectByName(inc.getObjeto().getObjClsNom());

                objeto = util.JsonToObject(dato.getObjVal(), objeto);
                
                if(this.ExisteRegistro(objeto))
                {
                    retorno = perManager.ejecutarUpdateQuery(util.ObtenerUpdateQuery(objeto));
                }
                else
                {
                    
                   retorno = perManager.ejecutarQuery(util.ObtenerInsertQuery(objeto));
                   
                    /*
                    Long idOriginal = util.ObtenerPrimaryKey(objeto);

                    SincRetorno regNuevo = perManager.guardar(objeto);

                    if(!regNuevo.SurgioError())
                    {
                        Long idGenerado = (long) regNuevo.getObjeto();
                        this.ActualizarPrimaryKeyManualmente(inc.getObjeto(), idGenerado, idOriginal);
                    }
                    else
                    {
                        retorno.setMensaje(regNuevo.getMensaje());
                        return retorno;
                    }
                    */
                }

                if(retorno.SurgioError())
                {
                    return retorno;
                }
            }
        }

        ForeignKeyControl(true);
        
        return retorno;
    }

    /**
     * Validar inconsistencias, antes de procesarlas
     * @param sinc Sincronización
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj ValidarInconsistencias(Sincronizacion sinc){
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Inconsistencias", TipoMensaje.MENSAJE));
        
        if(sinc.getLstInconsistencia() != null)
        {
            for(SincronizacionInconsistencia inc : sinc.getLstInconsistencia())
            {
                SincInconsistenciaDatos dato = inc.getObjetoSeleccionado();
                if(dato == null)
                {
                    retorno.setMensaje(new Mensajes("No se ha seleccionado un objeto en todas las inconsistencias"
                        , TipoMensaje.ERROR));

                    return retorno;
                }
            }
        }
        
        return retorno;
    }
    
    
}
