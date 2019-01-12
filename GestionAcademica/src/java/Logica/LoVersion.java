/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Entidad.Version;
import Persistencia.PerManejador;
import Utiles.Retorno_MsgObj;

/**
 *
 * @author alvar
 */
public class LoVersion{
    
    private static LoVersion instancia;

    private LoVersion() {
    }
    
    /**
     * Obtener instancia
     * @return Instancia
     */
    public static LoVersion GetInstancia(){
        if (instancia==null)
        {
            instancia   = new LoVersion();
            
        }

        return instancia;
    }

    /**
     * Guardar version
     * @param pObjeto Version
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Object guardar(Version pObjeto) {
        
        pObjeto.setSisVerCod(Long.valueOf("1"));
        
        PerManejador perManejador   = new PerManejador();
        Retorno_MsgObj retorno      = perManejador.guardar(pObjeto);

        if(!retorno.SurgioError())
        {
            pObjeto.setSisVerCod((Long) retorno.getObjeto());
            retorno.setObjeto(pObjeto);
        }
        
        return retorno;
                
    }

    /**
     * Actualizar version
     * @param pObjeto Version
     */
    public void actualizar(Version pObjeto) {
        PerManejador perManejador   = new PerManejador();
        perManejador.actualizar(pObjeto);
    }

    /**
     * Obtener version
     * @param pCodigo Long - VerCod
     * @return Version
     */
    public Version obtener(Object pCodigo) {
        Version version             = new Version();
        PerManejador perManejador   = new PerManejador();
        
        Retorno_MsgObj retorno      = perManejador.obtener((Long) pCodigo, Version.class);
        if(!retorno.SurgioError())
        {
            version = (Version) retorno.getObjeto();
        }
        
        return version;
    }
    
    
}
