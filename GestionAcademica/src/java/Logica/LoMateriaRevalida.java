/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Entidad.MateriaRevalida;
import Persistencia.PerManejador;
import Utiles.Retorno_MsgObj;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author alvar
 */
public class LoMateriaRevalida implements Interfaz.InMateriaRevalida{
    
    private static LoMateriaRevalida instancia;

    private LoMateriaRevalida() {
    }
    
    /**
     * Obtener instancia
     * @return instancia de clase
     */
    public static LoMateriaRevalida GetInstancia(){
        if (instancia==null)
        {
            instancia   = new LoMateriaRevalida();
            
        }

        return instancia;
    }

    /**
     * Guardar materia revalida
     * @param pObjeto Materia Revalida
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object guardar(MateriaRevalida pObjeto) {
        MateriaRevalida mat = (MateriaRevalida) pObjeto;
        
        PerManejador perManager = new PerManejador();
            
        mat.setObjFchMod(new Date());

        Retorno_MsgObj retorno = perManager.guardar(mat);

        if(!retorno.SurgioErrorObjetoRequerido())
        {
            mat.setMatRvlCod((Long) retorno.getObjeto());
            retorno.setObjeto(mat);
        }
            
        return retorno;
    }

    /**
     * Actualizar materia revalida
     * @param pObjeto Materia revalida
     */
    @Override
    public void actualizar(MateriaRevalida pObjeto) {
        MateriaRevalida mat = (MateriaRevalida) pObjeto;
            
        PerManejador perManager = new PerManejador();

        mat.setObjFchMod(new Date());
        
        perManager.actualizar(mat);
    }

    /**
     * Eliminar materia revalida
     * @param pObjeto Materia revalida
     */
    @Override
    public void eliminar(MateriaRevalida pObjeto) {
        PerManejador perManager = new PerManejador();
        perManager.eliminar(pObjeto);
    }

    /**
     * Obtener materia revalida
     * @param pCodigo Codigo
     * @return  Materia revalida
     */
    @Override
    public MateriaRevalida obtener(Object pCodigo) {
        PerManejador perManager = new PerManejador();
        Retorno_MsgObj retorno = perManager.obtener((Long) pCodigo, MateriaRevalida.class);
        
        MateriaRevalida mat = null;
        
        if(!retorno.SurgioErrorObjetoRequerido())
        {
            mat = (MateriaRevalida) retorno.getObjeto();
        }
        
        return mat;
    }

    /**
     * Obtener lista de materias revalidas
     * @return Lista de materias revalidas
     */
    @Override
    public List<MateriaRevalida> obtenerLista() {
        PerManejador perManager = new PerManejador();

        Retorno_MsgObj retorno = perManager.obtenerLista("MateriaRevalida.findAll", null);
        
        List<MateriaRevalida> lstMat = new ArrayList<>();
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            for(Object objeto : retorno.getLstObjetos())
            {
                lstMat.add((MateriaRevalida) objeto);
            }
        }

        return lstMat;
    }
    
}
