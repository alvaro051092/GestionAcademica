/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Entidad.Parametro;
import Enumerado.Constantes;
import Enumerado.TipoMensaje;
import Moodle.MoodleCategory;
import Moodle.MoodleRestCourse;
import Moodle.MoodleRestException;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alvar
 */
public class LoCategoria {

    private final Parametro         param;
    private static LoCategoria      instancia;
    private final MoodleRestCourse  mdlCourse;
    

    private LoCategoria() {
        mdlCourse           = new MoodleRestCourse();
        LoParametro loParam = LoParametro.GetInstancia();
        param               = loParam.obtener();
    }
    
    /**
     * Obtener instancia de la clase
     * @return Instancia de la clase
     */
    public static LoCategoria GetInstancia(){
        if (instancia==null)
        {
            instancia   = new LoCategoria();
            
        }

        return instancia;
    }
    
    /**
     * Agregar categoría en Moodle
     * @param pDsc Descripcion
     * @param pNom Nombre
     * @param pVisible Visible
     * @param parent Padre
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj Mdl_AgregarCategoria(String pDsc, String pNom, Boolean pVisible, Long parent)
    {
        Mensajes mensaje;
        
        MoodleCategory mdlCategoria = new MoodleCategory();

        mdlCategoria.setDescription(pDsc);
        mdlCategoria.setName(pNom);
        mdlCategoria.setVisible(pVisible);
        mdlCategoria.setParent(parent);
        
        try {
            mdlCategoria    = mdlCourse.__createCategory(param.getParUrlMdl() + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), param.getParMdlTkn(), mdlCategoria);
            mensaje         = new Mensajes("Cambios correctos", TipoMensaje.MENSAJE);
        } catch (UnsupportedEncodingException | MoodleRestException ex) {
            mensaje        = new Mensajes("Error: " + ex.getMessage(), TipoMensaje.ERROR);
            Logger.getLogger(LoCategoria.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(mensaje, mdlCategoria);

        return retorno;

    }
    
    /**
     * Actualizar categoría en moodle
     * @param codigo Código
     * @param pDsc Descripción
     * @param pNom Nombre
     * @param pVisible Visible
     * @param parent Padre
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj Mdl_ActualizarCategoria(Long codigo, String pDsc, String pNom, Boolean pVisible, Long parent)
    {
        Mensajes mensaje;
        
        MoodleCategory mdlCategoria = this.Mdl_ObtenerCategoria(codigo);

        mdlCategoria.setDescription(pDsc);
        mdlCategoria.setName(pNom);
        mdlCategoria.setVisible(pVisible);
        mdlCategoria.setParent(parent);
        
        try {
            mdlCourse.__updateCategory(param.getParUrlMdl() + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), param.getParMdlTkn(), mdlCategoria);
            mensaje         = new Mensajes("Cambios correctos", TipoMensaje.MENSAJE);
        } catch (UnsupportedEncodingException | MoodleRestException ex) {
            mensaje        = new Mensajes("Error: " + ex.getMessage(), TipoMensaje.ERROR);
            Logger.getLogger(LoCategoria.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(mensaje, mdlCategoria);

        return retorno;

    }
    
    /**
     * Elimina categoría de moodle
     * @param codigo Código
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj Mdl_EliminarCategoria(Long codigo)
    {
        Mensajes mensaje;
        MoodleCategory mdlCat   = this.Mdl_ObtenerCategoria(codigo);
        
        try {
            mdlCourse.__deleteCategory(param.getParUrlMdl() + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), param.getParMdlTkn(), mdlCat);
            mensaje         = new Mensajes("Cambios correctos", TipoMensaje.MENSAJE);
        } catch (UnsupportedEncodingException | MoodleRestException ex) {
            mensaje        = new Mensajes("Error: " + ex.getMessage(), TipoMensaje.ERROR);
            Logger.getLogger(LoCategoria.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(mensaje, null);

        return retorno;

    }
    
    /**
     * Retorna lista de categorías de moodle
     * @return Categorias
     */
    public MoodleCategory[] Mdl_ObtenerListaCategorias(){
        
        try {
            MoodleCategory[] lstCategorias = mdlCourse.__getCategories(param.getParUrlMdl() + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), param.getParMdlTkn());
            return lstCategorias;
            
        } catch(MoodleRestException | UnsupportedEncodingException ex) {
            Logger.getLogger(LoCategoria.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Obtiene una categoría de moodle
     * @param codigo Código
     * @return Categoría
     */
    public MoodleCategory Mdl_ObtenerCategoria(Long codigo){

        try {
            MoodleCategory[] lstCategorias = mdlCourse.__getCategories(param.getParUrlMdl() + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), param.getParMdlTkn(), codigo);
            if(lstCategorias.length > 0)
            {
                return lstCategorias[0];
            }

        } catch (MoodleRestException | UnsupportedEncodingException ex) {
            Logger.getLogger(LoCategoria.class.getName()).log(Level.SEVERE, null, ex);
        }

       return null;
    }
    
}
