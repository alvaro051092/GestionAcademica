/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Entidad.Archivo;
import Enumerado.TipoArchivo;
import Interfaz.InABMGenerico;
import Persistencia.PerManejador;
import SDT.SDT_Parameters;
import Utiles.Retorno_MsgObj;
import java.util.ArrayList;
import java.util.Date;

/** Lógica Archivos.
 * <p>Logica para el manejo de archivos</p>.
 * @author alvar
 */
public class LoArchivo implements InABMGenerico{

    private static LoArchivo instancia;

    private LoArchivo() {
    }
    
    /**
     * Obtener instancia de la clase
     * @return Instancia de la clase
     */
    public static LoArchivo GetInstancia(){
        if (instancia==null)
        {
            instancia   = new LoArchivo();
        }

        return instancia;
    }
    
    /**
     * Guardar archivo
     * @param pObjeto Archvivo
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object guardar(Object pObjeto) {
        
        Archivo archivo = (Archivo) pObjeto;
        
        archivo.setObjFchMod(new Date());
        
        PerManejador perManejador   = new PerManejador();
        Retorno_MsgObj retorno      = perManejador.guardar(archivo);

        if(!retorno.SurgioError())
        {
            archivo.setArcCod((Long) retorno.getObjeto());
            retorno.setObjeto(archivo);
        }
        
        return retorno;
    }

    /**
     *Actualizar archivo
     * @param pObjeto Archivo
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object actualizar(Object pObjeto) {
        
        PerManejador perManejador   = new PerManejador();

        return perManejador.actualizar(pObjeto);
    }

    /**
     * Eliminar archivo
     * @param pObjeto Archivo
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object eliminar(Object pObjeto) {
        PerManejador perManejador   = new PerManejador();
        return perManejador.eliminar(pObjeto);
    }

    /**
     * Retorna un archivo
     * @param pObjeto Long - ArcCod
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtener(Object pObjeto) {
        
        PerManejador perManejador   = new PerManejador();
        
        return perManejador.obtener((Long) pObjeto, Archivo.class);
    }

    /**
     * Retorna una lista de archivos
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtenerLista() {
        PerManejador perManejador   = new PerManejador();
        
        return perManejador.obtenerLista("Archivo.findAll", null);
    }
    
    /**
     * Retorna una lista de archivos, a partir de un tipo de archivo
     * @param tpoArch Tipo de archivo
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj obtenerListaByTipo(TipoArchivo tpoArch) {
        PerManejador perManejador   = new PerManejador();
        
        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(tpoArch, "ArcTpo"));
        
        return perManejador.obtenerLista("Archivo.findByTipo", lstParametros);
    }
    
   
}
