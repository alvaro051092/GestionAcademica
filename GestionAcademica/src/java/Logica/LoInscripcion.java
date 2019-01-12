/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Entidad.Inscripcion;
import Interfaz.InABMGenerico;
import Persistencia.PerManejador;
import SDT.SDT_Parameters;
import Utiles.Retorno_MsgObj;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author alvar
 */
public class LoInscripcion implements InABMGenerico{

    private static LoInscripcion instancia;

    private LoInscripcion() {
    }
    
    /**
     *  Obtener instancia de la clase
     * @return Instancia de la clase
     */
    public static LoInscripcion GetInstancia(){
        if (instancia==null)
        {
            instancia   = new LoInscripcion();
        }

        return instancia;
    }
    
    /**
     * Guardar inscripción
     * @param pObjeto Inscripcion
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object guardar(Object pObjeto) {
        Inscripcion insc = (Inscripcion) pObjeto;
        
        PerManejador perManager = new PerManejador();
            
        insc.setObjFchMod(new Date());

        Retorno_MsgObj retorno = perManager.guardar(insc);

        if(!retorno.SurgioErrorObjetoRequerido())
        {
            insc.setInsCod((Long) retorno.getObjeto());
            retorno.setObjeto(insc);
        }
            
        return retorno;        
    }

    /**
     * Actualizar inscripción
     * @param pObjeto Inscripcion
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object actualizar(Object pObjeto) {
        Inscripcion insc = (Inscripcion) pObjeto;
            
        PerManejador perManager = new PerManejador();

        insc.setObjFchMod(new Date());
        
        return perManager.actualizar(insc);
    }

    /**
     * Eliminar inscripcion
     * @param pObjeto Inscripcion
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object eliminar(Object pObjeto) {
        PerManejador perManager = new PerManejador();
        return perManager.eliminar(pObjeto);
    }

    /**
     * Obtener inscripcion
     * @param pObjeto Long - InsCod
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtener(Object pObjeto) {
        PerManejador perManager = new PerManejador();
        return perManager.obtener((Long) pObjeto, Inscripcion.class);
    }

    /**
     * Obtener lista de inscripciones
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtenerLista() {
        PerManejador perManager = new PerManejador();

        return perManager.obtenerLista("Inscripcion.findAll", null);
    }
    
    /**
     * Obtener inscripciones por curso y persona
     * @param PerCod Código de persona
     * @param CurCod Código de curso
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj obtenerInscByCurso(Long PerCod, Long CurCod) {
        
        Retorno_MsgObj retorno = this.obtenerListaByCurso(PerCod, CurCod);
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            if(retorno.getLstObjetos().size() > 0) retorno.setObjeto(retorno.getLstObjetos().get(0));
        }
        retorno.setLstObjetos(null);
        return retorno;
    }
    
    /**
     * Obtener inscripcion por plan de estudio
     * @param PerCod Código de persona
     * @param PlaEstCod Código de plan de estudio
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj obtenerInscByPlan(Long PerCod, Long PlaEstCod) {
        
        Retorno_MsgObj retorno = this.obtenerListaByPlan(PerCod, PlaEstCod);
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            if(retorno.getLstObjetos().size() > 0) retorno.setObjeto(retorno.getLstObjetos().get(0));
        }
        retorno.setLstObjetos(null);
        return retorno;
    }
    
    /**
     * Obtener inscripción por curso
     * @param PerCod Código de persona
     * @param CurCod Código de curso
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj obtenerListaByCurso(Long PerCod, Long CurCod) {
        
        PerManejador perManager = new PerManejador();
        
        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(CurCod, "CurCod"));
        lstParametros.add(new SDT_Parameters(PerCod, "PerCod"));
        
        return perManager.obtenerLista("Inscripcion.findByCurso", lstParametros);
    }
    
    /**
     * Obtener lista por plan de estudio
     * @param PerCod Código de persona
     * @param PlaEstCod Código de plan de estudio
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj obtenerListaByPlan(Long PerCod, Long PlaEstCod) {
        PerManejador perManager = new PerManejador();

        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(PerCod, "PerCod"));
        lstParametros.add(new SDT_Parameters(PlaEstCod, "PlaEstCod"));

        return perManager.obtenerLista("Inscripcion.findByPlan", lstParametros);
        
    }
    
    /**
     * Obtener lista por alumno
     * @param PerCod código de persona
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj obtenerListaByAlumno(Long PerCod) {
        PerManejador perManager = new PerManejador();

        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(PerCod, "PerCod"));

        return perManager.obtenerLista("Inscripcion.findByAlumno", lstParametros);
    }
    
}
