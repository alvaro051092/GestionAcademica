/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Entidad.ParametroEmail;
import Persistencia.PerManejador;
import Utiles.Retorno_MsgObj;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alvar
 */
public class LoParametroEmail implements Interfaz.InParametroEmail{
    
    private static LoParametroEmail   instancia;

    private LoParametroEmail() {
        
    }
    
    /**
     * Obtener instancia
     * @return instancia de clase
     */
    public static LoParametroEmail GetInstancia(){
        if (instancia==null)
        {
            instancia   = new LoParametroEmail();
            
        }

        return instancia;
    }

    /**
     * Guardar parametro de email
     * @param pObjeto Parametro de email
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object guardar(ParametroEmail pObjeto) {
        ParametroEmail eml = (ParametroEmail) pObjeto;
        
        PerManejador perManager = new PerManejador();
        Retorno_MsgObj retorno = perManager.guardar(eml);

        if(!retorno.SurgioErrorObjetoRequerido())
        {
            eml.setParEmlCod((Long) retorno.getObjeto());
            retorno.setObjeto(eml);
        }
            
        return retorno; 
        //return perParametroEmail.guardar(pObjeto);
    }

    /**
     * Actualizar parametro email
     * @param pObjeto parametro email
     */
    @Override
    public void actualizar(ParametroEmail pObjeto) {
        PerManejador perManager = new PerManejador();
        perManager.actualizar(pObjeto);
    }

    /**
     * Eliminar parametro de email
     * @param pObjeto Parametro de email
     */
    @Override
    public void eliminar(ParametroEmail pObjeto) {
        PerManejador perManager = new PerManejador();
        perManager.eliminar(pObjeto);
    }

    /**
     * Obtener Parametro de email
     * @param pCodigo Long - ParEmlCod 
     * @return Parametro de Email
     */
    @Override
    public ParametroEmail obtener(Object pCodigo) {
        PerManejador perManager = new PerManejador();
        Retorno_MsgObj retorno  = perManager.obtener((Long) pCodigo, ParametroEmail.class);
        
        ParametroEmail eml = null;
        
        if(!retorno.SurgioErrorObjetoRequerido())
        {
            eml = (ParametroEmail) retorno.getObjeto();
        }
        
        return eml;
    }
    
    /**
     * Obtener lista de parametros email
     * @return Lista Parametro Email
     */
    @Override
    public List<ParametroEmail> obtenerLista() {
        
        PerManejador perManager = new PerManejador();

        Retorno_MsgObj retorno = perManager.obtenerLista("ParametroEmail.findAll", null);
        
        List<ParametroEmail> lstEml = new ArrayList<>();
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            for(Object objeto : retorno.getLstObjetos())
            {
                lstEml.add((ParametroEmail) objeto);
            }
        }

        return lstEml;
    }
   
}
