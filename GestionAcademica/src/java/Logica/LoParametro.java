/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Entidad.Parametro;
import Logica.Notificacion.NotificacionesInternas;
import Persistencia.PerManejador;
import Utiles.Retorno_MsgObj;

/**
 *
 * @author alvar
 */
public class LoParametro{
    
    private static LoParametro instancia;
    
    private Parametro parametro;

    private LoParametro() {
        
    }
    
    /**
     * Obtener instancia
     * @return Instancia de clase
     */
    public static LoParametro GetInstancia(){
        if (instancia==null)
        {
            instancia   = new LoParametro();
            
        }

        return instancia;
    }

    /**
     * Guardar parametro
     * @param pObjeto parametro
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Object guardar(Parametro pObjeto) {
        
        pObjeto.setParCod(Long.valueOf("1"));
        
        PerManejador perManejador   = new PerManejador();
        Retorno_MsgObj retorno      = perManejador.guardar(pObjeto);

        if(!retorno.SurgioError())
        {
            pObjeto.setParCod((Long) retorno.getObjeto());
            retorno.setObjeto(pObjeto);
            this.parametro = pObjeto;
        }
        
        return retorno;
        
    }

    /**
     * actualizar parametro
     * @param pObjeto parametro
     */
    public void actualizar(Parametro pObjeto) {
        this.parametro = pObjeto;
        PerManejador perManejador   = new PerManejador();
        perManejador.actualizar(pObjeto);
    }

    /**
     * Obtener parametro
     * @return Parametro
     */
    public Parametro obtener() {
        
        if(this.parametro == null)
        {
            parametro         = new Parametro();
            PerManejador perManejador   = new PerManejador();

            Retorno_MsgObj retorno      = perManejador.obtener(Long.valueOf("1"), Parametro.class);
            if(!retorno.SurgioErrorObjetoRequerido())
            {
                parametro = (Parametro) retorno.getObjeto();
                
                if(parametro.getParUtlMdl())
                {
                    if(!Utiles.Utilidades.GetInstancia().ConexionValida(parametro.getParUrlMdl()))
                    {
                        NotificacionesInternas not = new NotificacionesInternas();
                        not.Notificar_ErrorSistema("Se desactiva sincronización con Moodele. Motivo: No se puede conectar a url: " + parametro.getParUrlMdl());

                        parametro.setParUtlMdl(Boolean.FALSE);
                    }
                }
            }
            else
            {
                parametro = null;
            }
            
            
        }
        
        return parametro;
    }
    
    
}
