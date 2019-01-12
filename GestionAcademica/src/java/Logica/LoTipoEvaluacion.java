/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Entidad.TipoEvaluacion;
import Interfaz.InTipoEvaluacion;
import Persistencia.PerManejador;
import Utiles.Retorno_MsgObj;
import java.util.Date;

/**
 *
 * @author alvar
 */
public class LoTipoEvaluacion implements InTipoEvaluacion{

    private static LoTipoEvaluacion instancia;

    private LoTipoEvaluacion() {
        System.err.println("Se crea instancia");
    }
    
    /**
     * Obtener instancia
     * @return Inconsistencia de clase
     */
    public static LoTipoEvaluacion GetInstancia(){
        if (instancia==null)
        {
            instancia   = new LoTipoEvaluacion();
        }

        return instancia;
    }
    
    /**
     * Guardar tipos de evaluación
     * @param pTipoEvaluacion Tipo de evaluación
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object guardar(TipoEvaluacion pTipoEvaluacion) {
        PerManejador perManejador   = new PerManejador();
        
        pTipoEvaluacion.setObjFchMod(new Date());
        
        Retorno_MsgObj retorno      = perManejador.guardar(pTipoEvaluacion);
        
        if(!retorno.SurgioError())
        {
            pTipoEvaluacion.setTpoEvlCod((Long) retorno.getObjeto());
            retorno.setObjeto(pTipoEvaluacion);
            
        }
        
        return retorno;
    }

    /**
     * Actualizar tipo de evaluacion
     * @param pTipoEvaluacion Tipo de evaluacion
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object actualizar(TipoEvaluacion pTipoEvaluacion) {
        PerManejador perManejador   = new PerManejador();
        
        pTipoEvaluacion.setObjFchMod(new Date());

        return perManejador.actualizar(pTipoEvaluacion);
    }

    /**
     * Eliminar tipo de evaluación
     * @param pTipoEvaluacion Tipo de evaluación
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object eliminar(TipoEvaluacion pTipoEvaluacion) {
        PerManejador perManejador   = new PerManejador();
        
        return perManejador.eliminar(pTipoEvaluacion);
    }

    /**
     * Obtener tipo de evaluación
     * @param pTpoEvlCod Long - Código
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtener(Long pTpoEvlCod) {
        PerManejador perManejador   = new PerManejador();
        return perManejador.obtener(pTpoEvlCod, TipoEvaluacion.class);
    }

    /**
     * Obtener lista de tipos de evaluación
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtenerLista() {
        PerManejador perManejador   = new PerManejador();
        return perManejador.obtenerLista("TipoEvaluacion.findAll", null);
    }

    
}
