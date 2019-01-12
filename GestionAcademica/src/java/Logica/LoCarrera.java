/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Entidad.Carrera;
import Entidad.Materia;
import Utiles.Mensajes;
import Entidad.Parametro;
import Entidad.PlanEstudio;
import Enumerado.TipoMensaje;
import Enumerado.TipoPeriodo;
import Moodle.MoodleCategory;
import Moodle.MoodleCourse;
import Persistencia.PerManejador;
import SDT.SDT_Parameters;
import Utiles.Retorno_MsgObj;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author aa
 */
public class LoCarrera implements Interfaz.InCarrera{
    private static LoCarrera    instancia;
    private final Parametro     param;
    private final LoCategoria   loCategoria;
    private final LoEstudio     loEstudio;
    
    private LoCarrera(){
        param               = LoParametro.GetInstancia().obtener();
        loCategoria         = LoCategoria.GetInstancia();
        loEstudio           = LoEstudio.GetInstancia();
    }
    
    /**
     * Obtener instancia de la clase
     * @return Instancia de la clase
     */
    public static LoCarrera GetInstancia(){
        if(instancia == null){
            instancia = new LoCarrera();
        }
        return instancia;
    }
    
    /**
     * Guardar carrera
     * @param pCarrera Carrera
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object guardar(Carrera pCarrera) 
    {
        boolean error           = false;
        Retorno_MsgObj retorno  = new Retorno_MsgObj(new Mensajes("Error al guardar la Carrera", TipoMensaje.ERROR), pCarrera);
        if(param.getParUtlMdl())
        {   
            if(pCarrera.getCarCatCod() == null)
            {
                retorno = this.Mdl_AgregarCategoria(0L, pCarrera.getCarNom(), pCarrera.getCarDsc());
            }
            else
            {
                retorno = this.Mdl_ActualizarCategoria(0L, pCarrera.getCarCatCod(), pCarrera.getCarNom(), pCarrera.getCarDsc());
            }
            error = retorno.SurgioError();
            
            pCarrera.setCarCatCod((Long) retorno.getObjeto());
            retorno.setObjeto(pCarrera);
        }
        

        if(!error)
        {
            pCarrera    = (Carrera) retorno.getObjeto();
            
            PerManejador perManager = new PerManejador();
            
            pCarrera.setObjFchMod(new Date());
            
            retorno = perManager.guardar(pCarrera);
            
            if(!retorno.SurgioErrorObjetoRequerido())
            {
                pCarrera.setCarCod((Long) retorno.getObjeto());
                retorno.setObjeto(pCarrera);
            }
            
            
        }
        return retorno;
    }

    /**
     * Actualizar carrera
     * @param pCarrera Carrera
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object actualizar(Carrera pCarrera) {
        boolean error       = false;
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error", TipoMensaje.ERROR), pCarrera);
        
        if (param.getParUtlMdl())
        {
            if(pCarrera.getCarCatCod() == null)
            {
                retorno = this.Mdl_AgregarCategoria(0L, pCarrera.getCarNom(), pCarrera.getCarDsc());
            }
            else
            {
                retorno = this.Mdl_ActualizarCategoria(0L, pCarrera.getCarCatCod(), pCarrera.getCarNom(), pCarrera.getCarDsc());
            }
            error = retorno.SurgioError();
            
            pCarrera.setCarCatCod((Long) retorno.getObjeto());
            retorno.setObjeto(pCarrera);
        }
        
        if (!error)
        {
            pCarrera = (Carrera) retorno.getObjeto();
            
            PerManejador perManager = new PerManejador();
            
            pCarrera.setObjFchMod(new Date());
            
            retorno = perManager.actualizar(pCarrera);
            
            retorno.setObjeto(pCarrera);
            
        }
        return retorno;
    }

    /**
     * Eliminar carrera
     * @param pCarrera Carrera
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object eliminar(Carrera pCarrera) {
        boolean error       = false;
        Retorno_MsgObj retorno  = new Retorno_MsgObj(new Mensajes("Error", TipoMensaje.ERROR));        
        if(param.getParUtlMdl() && pCarrera.getCarCatCod() != null)
        {
            retorno = this.Mdl_EliminarCategoria(pCarrera.getCarCatCod());
            error   = retorno.SurgioError();
        }

        if(!error)
        {
            PerManejador perManager = new PerManejador();
            retorno = (Retorno_MsgObj) perManager.eliminar(pCarrera);
        }
        return retorno;
    }

    /**
     * Obtener categoria
     * @param pCarCod Código de Categoria
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtener(Long pCarCod) {
        PerManejador perManager = new PerManejador();
        return perManager.obtener(pCarCod, Carrera.class);
    }

    /**
     * Obtener lista de carreras
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtenerLista() {
        
        PerManejador perManager = new PerManejador();

        return perManager.obtenerLista("Carrera.findAll", null);
        
    }
    
    //----------------------------------------------------------------------------------------------------
    //-Manejo de Plan Estudio
    //----------------------------------------------------------------------------------------------------

    /**
     * Agregar plan de estudio
     * @param plan Plan
     * @return Resultado: RETORNO_MSGOBJ
     */
    
    public Object PlanEstudioAgregar(PlanEstudio plan)
    {
        boolean error           = false;
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al agregar el Plan de Estudio",TipoMensaje.ERROR), plan);
       
        if(param.getParUtlMdl())
        {
            if(plan.getPlaEstCatCod() == null)
            {
                retorno = this.Mdl_AgregarCategoria(plan.getCarrera().getCarCatCod(), plan.getPlaEstNom(), plan.getPlaEstDsc());
            }
            else
            {
                retorno = this.Mdl_ActualizarCategoria(plan.getCarrera().getCarCatCod(), plan.getPlaEstCatCod(), plan.getPlaEstNom(), plan.getPlaEstDsc());
            }
            error = retorno.SurgioError();
            
            plan.setPlaEstCatCod((Long) retorno.getObjeto());
            retorno.setObjeto(plan);
        }        
        
        if(!error)
        {
            plan = (PlanEstudio) retorno.getObjeto();
            plan.setObjFchMod(new Date());
            PerManejador perManejador   = new PerManejador();
            retorno = (Retorno_MsgObj) perManejador.guardar(plan);
            
        }
        return retorno;
    }
    
    /**
     * Actualizar plan de estudio
     * @param plan Plan
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Object PlanEstudioActualizar(PlanEstudio plan)
    {
        boolean error           = false;
        Retorno_MsgObj retorno  = new Retorno_MsgObj(new Mensajes("Error al actualizar el Plan de Estudio", TipoMensaje.ERROR), plan);
       
        if(param.getParUtlMdl() && plan.getPlaEstCatCod()!= null)
        {
            if(plan.getPlaEstCatCod() == null)
            {
                retorno = this.Mdl_AgregarCategoria(plan.getCarrera().getCarCatCod(), plan.getPlaEstNom(), plan.getPlaEstDsc());
            }
            else
            {
                retorno = this.Mdl_ActualizarCategoria(plan.getCarrera().getCarCatCod(), plan.getPlaEstCatCod(), plan.getPlaEstNom(), plan.getPlaEstDsc());
            }
            error = retorno.SurgioError();
            
            plan.setPlaEstCatCod((Long) retorno.getObjeto());
            retorno.setObjeto(plan);
        }
        
        if(!error)
        {
            plan = (PlanEstudio) retorno.getObjeto();
            plan.setObjFchMod(new Date());
            
            PerManejador perManejador   = new PerManejador();
     
            retorno = (Retorno_MsgObj) perManejador.actualizar(plan);
        }
        return retorno;
    }
    
    /**
     * Eliminar plan de estudio
     * @param plan Plan
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Object PlanEstudioEliminar(PlanEstudio plan)
    {
        boolean error           = false;
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al eliminar el Plan de Estudio", TipoMensaje.ERROR), plan);
       
        if(param.getParUtlMdl() && plan.getPlaEstCatCod()!= null)
        {
            retorno = this.Mdl_EliminarCategoria(plan.getPlaEstCatCod());
            error = retorno.SurgioError();
        }
        
        if(!error)
        {
            
            PerManejador perManejador   = new PerManejador();
            retorno = (Retorno_MsgObj) perManejador.eliminar(plan);
        }
        return retorno;
    }
    
    /**
     * Obtener plan de estudio
     * @param pPlaEstCod Código del plan
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj PlanEstudioObtener(Long pPlaEstCod) {
        PerManejador perManager = new PerManejador();
        return perManager.obtener(pPlaEstCod, PlanEstudio.class);
    }
    
    //----------------------------------------------------------------------------------------------------
    //-Manejo de Materia
    //----------------------------------------------------------------------------------------------------

    /**
     * Guardar materia
     * @param mat Materia
     * @return Resultado: RETORNO_MSGOBJ
     */
    
    public Object MateriaAgregar(Materia mat)
    {
        boolean error           = false;
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al agregar la Materia",TipoMensaje.ERROR), mat);
       
        if(param.getParUtlMdl())
        {
            if(mat.getMdlCod() == null)
            {
                retorno = this.Mdl_AgregarCategoria(mat.getPlan().getPlaEstCatCod(), mat.getMatNom(), mat.getMatNom());
            }
            else
            {
                retorno = this.Mdl_ActualizarCategoria(mat.getPlan().getPlaEstCatCod(), mat.getMdlCod(), mat.getMatNom(), mat.getMatNom());
            }
            error = retorno.SurgioError();
            
            mat.setMdlCod((Long) retorno.getObjeto());
            retorno.setObjeto(mat);
        }        
        
        if(!error)
        {
            mat = (Materia) retorno.getObjeto();
            mat.setObjFchMod(new Date());
            
            PerManejador perManejador   = new PerManejador();
            retorno = (Retorno_MsgObj) perManejador.guardar(mat);
        } 
        return retorno;
    }
    
    /**
     * Actualizar materia
     * @param mat Materia
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Object MateriaActualizar(Materia mat)
    {
        boolean error           = false;
        Retorno_MsgObj retorno  = new Retorno_MsgObj(new Mensajes("Error al actualizar la Materia", TipoMensaje.ERROR), mat);
       
        if(param.getParUtlMdl())
        {
            if(mat.getMdlCod() == null)
            {
                retorno = this.Mdl_AgregarCategoria(mat.getPlan().getPlaEstCatCod(), mat.getMatNom(), mat.getMatNom());
            }
            else
            {
                retorno = this.Mdl_ActualizarCategoria(mat.getPlan().getPlaEstCatCod(), mat.getMdlCod(), mat.getMatNom(), mat.getMatNom());
            }
            error = retorno.SurgioError();
            
            mat.setMdlCod((Long) retorno.getObjeto());
            retorno.setObjeto(mat);
        } 
        
        if(!error)
        {
            mat = (Materia) retorno.getObjeto();
            mat.setObjFchMod(new Date());
            
            PerManejador perManejador   = new PerManejador();
            retorno = (Retorno_MsgObj) perManejador.actualizar(mat);
        }
        return retorno;
    }
    
    /**
     * Eliminar Materia
     * @param mat Materia
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Object MateriaEliminar(Materia mat)
    {
        boolean error           = false;
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al eliminar la Materia", TipoMensaje.ERROR), mat);
       
        if(param.getParUtlMdl())
        {
            retorno = this.Mdl_EliminarCategoria(mat.getMdlCod());
            error = retorno.SurgioError();
        }
        
        if(!error)
        {
            
            PerManejador perManejador   = new PerManejador();
            retorno = (Retorno_MsgObj) perManejador.eliminar(mat);
        }
        return retorno;
    }
    
    /**
     * Obtener Materia
     * @param pMatCod Código de materia
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj MateriaObtener(Long pMatCod) {
        PerManejador perManager = new PerManejador();
        return perManager.obtener(pMatCod, Materia.class);
    }
    
    /**
     * Obtener materias para popup, en base a plan de estudio
     * @param PlaEstCod Código del plan
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj obtenerPopUp(Long PlaEstCod)
    {
        PerManejador perManager = new PerManejador();

        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(PlaEstCod, "PlaEstCod"));

        return perManager.obtenerLista("Materia.findByPlan", lstParametros);
        
    }
    
    /**
     * Materia por periodo
     * @param PlaEstCod Código del plan
     * @param tpoPer Tipo de período
     * @param perVal Valor del periodo
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj MateriaPorPeriodo(Long PlaEstCod, TipoPeriodo tpoPer, Double perVal)
    {
        
        PerManejador perManager = new PerManejador();

        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(tpoPer, "TpoPer"));
        lstParametros.add(new SDT_Parameters(perVal, "PerVal"));
        lstParametros.add(new SDT_Parameters(PlaEstCod, "PlaEstCod"));

        return perManager.obtenerLista("Materia.findByPeriodo", lstParametros);
    }
    
    
    //--------------------------------------------------------------------------------------------------------
    //Moodle
    //--------------------------------------------------------------------------------------------------------
    
    /**
     * Agrega nueva catetgoria en moodle
     * @param parent Código del padre
     * @param mdlNom Nombre
     * @param mdlDsc Descripción
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj Mdl_AgregarCategoria(Long parent, String mdlNom, String mdlDsc)
    {
        Retorno_MsgObj retorno = loCategoria.Mdl_AgregarCategoria(mdlDsc, mdlNom, Boolean.TRUE, parent);
        
        if(!retorno.SurgioErrorObjetoRequerido())
        {
            MoodleCategory mdlCategoria = (MoodleCategory) retorno.getObjeto();
            Long mdlCod = mdlCategoria.getId();
            
            retorno.setObjeto(mdlCod);
        }
        
        return retorno;

    }
    
    /**
     * Actualizar categoría en moodle
     * @param parent Código del padre
     * @param mdlCod Código de la categoría
     * @param mdlNom Nombre
     * @param mdlDsc Descripción
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj Mdl_ActualizarCategoria(Long parent, Long mdlCod, String mdlNom, String mdlDsc)
    {
        Retorno_MsgObj retorno = loCategoria.Mdl_ActualizarCategoria(mdlCod, mdlDsc, mdlNom, Boolean.TRUE, parent);
        
        MoodleCategory mdlCategoria = (MoodleCategory) retorno.getObjeto();
        retorno.setObjeto(mdlCategoria.getId());

        return retorno;
    }
    
    /**
     * Eliminar categoría
     * @param mdlCod Código de categoría
     * @return Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj Mdl_EliminarCategoria(Long mdlCod)
    {
        return loCategoria.Mdl_EliminarCategoria(mdlCod);
    }

}
