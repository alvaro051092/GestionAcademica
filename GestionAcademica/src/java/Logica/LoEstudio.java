/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Entidad.Parametro;
import Entidad.PeriodoEstudioDocumento;
import Enumerado.Constantes;
import Enumerado.TipoMensaje;
import Moodle.MoodleCourse;
import Moodle.MoodleCourseContent;
import Moodle.MoodleFileContent;
import Moodle.MoodleModuleContent;
import Moodle.MoodleRestCourse;
import Moodle.MoodleRestEnrol;
import Moodle.MoodleRestException;
import Moodle.MoodleRestFile;
import Moodle.MoodleRestUserEnrolment;
import Moodle.MoodleRestUserEnrolmentException;
import Moodle.MoodleUserEnrolment;
import Moodle.Role;
import Moodle.UserList;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alvar
 */
public class LoEstudio {

    private final Parametro         param;
    private static LoEstudio      instancia;
    private final MoodleRestCourse  mdlCourse;
    

    private LoEstudio() {
        mdlCourse           = new MoodleRestCourse();
        param               = LoParametro.GetInstancia().obtener();
    }
    
    /**
     * Obtener instancia de la clase
     * @return Instancia de la clase
     */
    public static LoEstudio GetInstancia(){
        if (instancia==null)
        {
            instancia   = new LoEstudio();
            
        }

        return instancia;
    }
    
    /**
     * Agregar estudio a moodle
     * @param pCategory Código de categoría
     * @param pFullName Nombre largo
     * @param pShortName Nombre corto
     * @param pDescripcion Descripción
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj Mdl_AgregarEstudio(Long pCategory, String pFullName, String pShortName, String pDescripcion)
    {
        Mensajes mensaje;
        
        MoodleCourse mdlEstudio = new MoodleCourse();

        mdlEstudio.setCategoryId(pCategory);
        mdlEstudio.setFullname(pFullName);
        mdlEstudio.setShortname(pShortName);
        mdlEstudio.setSummary(pDescripcion);
        
        try {
            mdlEstudio    = mdlCourse.__createCourse(param.getParUrlMdl() + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), param.getParMdlTkn(), mdlEstudio);
            mensaje         = new Mensajes("Cambios correctos", TipoMensaje.MENSAJE);

        } catch (UnsupportedEncodingException | MoodleRestException ex) {
            mensaje         = new Mensajes("Error: " + ex.getMessage(), TipoMensaje.ERROR);
            Logger.getLogger(LoEstudio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(mensaje, mdlEstudio);

        return retorno;

    }
    
    /**
     * Actualizar estudio de moodle
     * @param pCodigo Código de estudio
     * @param pCategory Código de categoría
     * @param pFullName Nombre largo
     * @param pShortName Nombre corto
     * @param pDescripcion Descripción
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj Mdl_ActualizarEstudio(Long pCodigo, Long pCategory, String pFullName, String pShortName, String pDescripcion)
    {
        Mensajes mensaje;
        
        MoodleCourse mdlEstudio = this.Mdl_ObtenerEstudio(pCodigo);

        mdlEstudio.setCategoryId(pCategory);
        mdlEstudio.setFullname(pFullName);
        mdlEstudio.setShortname(pShortName);
        mdlEstudio.setSummary(pDescripcion);
        
        
        try {
            mdlCourse.__updateCourse(param.getParUrlMdl() + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), param.getParMdlTkn(), mdlEstudio);
            mensaje         = new Mensajes("Cambios correctos", TipoMensaje.MENSAJE);
    
        } catch (UnsupportedEncodingException | MoodleRestException ex) {
            mensaje         = new Mensajes("Error: " + ex.getMessage(), TipoMensaje.ERROR);
            Logger.getLogger(LoEstudio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(mensaje, mdlEstudio);

        return retorno;

    }
    
    /**
     * Eliminar estudio de moodle
     * @param codigo Código de estudio 
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj Mdl_EliminarEstudio(Long codigo)
    {
        Mensajes mensaje;
        
        try {
            mdlCourse.__deleteCourse(param.getParUrlMdl() + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), param.getParMdlTkn(), codigo);
            mensaje         = new Mensajes("Cambios correctos", TipoMensaje.MENSAJE);
        } catch (UnsupportedEncodingException | MoodleRestException ex) {
            mensaje         = new Mensajes("Error: " + ex.getMessage(), TipoMensaje.ERROR);
            Logger.getLogger(LoEstudio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Retorno_MsgObj retorno  = new Retorno_MsgObj(mensaje, null);

        return retorno;

    }
    
    /**
     * Obtener estudio de moodle
     * @param codigo Código de moodle
     * @return  Curso
     */
    public MoodleCourse Mdl_ObtenerEstudio(Long codigo){

        try {
            MoodleCourse course = mdlCourse.__getCourseFromId(param.getParUrlMdl() + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), param.getParMdlTkn(), codigo);
            return course;

        } catch (MoodleRestException | UnsupportedEncodingException ex) {
            Logger.getLogger(LoEstudio.class.getName()).log(Level.SEVERE, null, ex);
        }

       return null;
    }
    
    /**
     * Obtener contenido de curso
     * @param curso Código de curso
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public MoodleCourseContent[] Mdl_ObtenerEstudioContent(Long curso){

        try {
            MoodleCourseContent[] contenido = mdlCourse.__getCourseContent(param.getParUrlMdl() 
                    + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), 
                    param.getParMdlTkn()
                    , curso
                    , null);
            return contenido;

        } catch (MoodleRestException | UnsupportedEncodingException ex) {
            Logger.getLogger(LoEstudio.class.getName()).log(Level.SEVERE, null, ex);
        }

       return null; 
    }
    
    /**
     * Obtener archivo de contenido de curso
     * @param contenido Contenido de curso
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public PeriodoEstudioDocumento Mdl_ObtenerEstudioArchivo(MoodleModuleContent contenido){
        try {

            URL website = new URL(contenido.getFileURL() + "&token=" + param.getParMdlTkn());
            
            //URL url = new URL("https://upload.wikimedia.org/wikipedia/en/8/87/Example.JPG");
            InputStream in = website.openStream();
            
            String carpeta = Utiles.Utilidades.GetInstancia().getPrivateTempStorage();
            //carpeta = carpeta.substring(1, carpeta.length());
            carpeta = carpeta + "/" + contenido.getFilename();
            
            Path path = Paths.get(carpeta);
            
            System.err.println("Path: "  + path.toString());
            
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
            in.close();
            
            PeriodoEstudioDocumento pe = new PeriodoEstudioDocumento();
            pe.setArchivo(path.toFile());
            
            pe.setDocFch(new Date(contenido.getTimeCreated() * 1000));
            
            Retorno_MsgObj ret = Utiles.Utilidades.GetInstancia().eliminarArchivo(carpeta);

            if(ret.SurgioError())
            {
                System.err.println(ret.getMensaje().toString());
            }
            
            return pe;
            
            //Utiles.Utilidades.GetInstancia().getPrivateTempStorage() + contenido.getFilename()
            
            //PeriodoEstudioDocumento pe = new PeriodoEstudioDocumento();
            //pe.setDocAdj(in.toString().getBytes());
            /*
            PeriodoEstudioDocumento pe = new PeriodoEstudioDocumento();
            pe.setArchivo(new File(Utiles.Utilidades.GetInstancia().getPrivateTempStorage() 
                    + contenido.getFilename()));
            
            
            return pe;
*/

        } catch (MalformedURLException ex) {
            Logger.getLogger(LoEstudio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoEstudio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Subir archivo a moodle
     * @param pe Documento de estudio
     */
    public void Mdl_SubirArchivoEstudio(PeriodoEstudioDocumento pe){
        try {
            MoodleRestFile restFile = new MoodleRestFile();
            
            MoodleFileContent params = new MoodleFileContent();
            
            params.setComponent("user");
            params.setFileArea("draft");
            params.setItemId(0L);
            params.setFilePath("/");
            params.setFileName(pe.getDocNom() + "." + pe.getDocExt());
            params.setFileContent(pe.getFileBase64());
            params.setContextlevel("course");
            params.setInstanceid(13L);
            
            restFile.__upload(param.getParUrlMdl() + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), param.getParMdlTkn(), params);
            
        } catch (UnsupportedEncodingException | MoodleRestException ex) {
            Logger.getLogger(LoEstudio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Asigna rol a usuario en un curso
     * @param perModId ID del usuario en moodle
     * @param mdlCod Código del curso
     * @param mdlRol Rol
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj Mdl_AsignUserCourse(Long perModId, Long mdlCod, Role mdlRol){
        Retorno_MsgObj retorno = new Retorno_MsgObj();
        
        //-mdlRol
        //-5 Estudiante
        //-4 Profesor sin permiso edición
        //-3 Profesor
        
        MoodleUserEnrolment usr = new MoodleUserEnrolment();
        usr.setRoleId(mdlRol.toIntegerValue());
        usr.setCourseId(mdlCod);
        usr.setUserId(perModId);
        
        
        MoodleRestUserEnrolment enrol = new MoodleRestUserEnrolment();
        
        try {
            enrol.__enrolUser(param.getParUrlMdl() 
                    + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(),
                    param.getParMdlTkn(), usr);
            
            retorno.setMensaje(new Mensajes("Asignacion correcta", TipoMensaje.MENSAJE));
            
        } catch (UnsupportedEncodingException | MoodleRestUserEnrolmentException | MoodleRestException ex) {
            retorno.setMensaje(new Mensajes("Error al asignar usuario: " + ex, TipoMensaje.ERROR));
            Logger.getLogger(LoEstudio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return retorno;
    }
    /*
    public Retorno_MsgObj Mdl_GetUsuarioCurso(Long perModId, Long mdlCod, Integer mdlRol){
        Retorno_MsgObj retorno = new Retorno_MsgObj();
        
        //-mdlRol
        //-5 Estudiante
        //-4 Profesor sin permiso edición
        //-3 Profesor
        
        MoodleRestEnrol enrole  = new MoodleRestEnrol();

        try { 
            
            MoodleUser[] lstUsr     = enrole.__getEnrolledUsers(param.getParUrlMdl()
                    + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(),
                    param.getParMdlTkn(), mdlCod, null);
            
            if(lstUsr != null)
            {
                for(MoodleUser usr : lstUsr)
                {
                    if(usr.getId().equals(perModId))
                    {
                        usr.getRoles().get(0).getRole().
                    }
                }
            }
            
            
        } catch (UnsupportedEncodingException | MoodleRestException | MoodleUserRoleException ex) {
            retorno.setMensaje(new Mensajes("Error: " + ex, TipoMensaje.ERROR));
            Logger.getLogger(LoEstudio.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        
        return retorno;
    }
    */
    
    /**
     * Quitar usuario de un curso 
     * @param perModId ID del usuario en moodle
     * @param mdlCod Codigo del curso
     * @param mdlRol Rol
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj Mdl_UnAsignUserCourse(Long perModId, Long mdlCod, Role mdlRol){
        Retorno_MsgObj retorno = new Retorno_MsgObj();
        
        //-mdlRol
        //-5 Estudiante
        //-4 Profesor sin permiso edición
        //-3 Profesor
        
        
        MoodleRestEnrol enrole  = new MoodleRestEnrol();
        UserList usr     = new UserList();
        
        usr.setCourseId(mdlCod);
        usr.setRoleId(mdlRol.toIntegerValue());
        usr.setUserId(perModId);
        
        
        try { 
            enrole.__enrolManualUnenrolUsers(param.getParUrlMdl()
                    + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(),
                    param.getParMdlTkn(), usr);
            
             retorno.setMensaje(new Mensajes("Asignacion correcta", TipoMensaje.MENSAJE));
             
        } catch (UnsupportedEncodingException | MoodleRestException ex) {
             retorno.setMensaje(new Mensajes("Error: " + ex, TipoMensaje.ERROR));
            Logger.getLogger(LoEstudio.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return retorno;
    }
    
    /**
     * Retorno lista de cursos
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj Mdl_ListaCursos(){
        MoodleRestCourse crs = new MoodleRestCourse();
        
        try {
            MoodleCourse[] lstCurso = crs.__getAllCourses(param.getParUrlMdl()
                    + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(),
                    param.getParMdlTkn());
            
            
            System.err.println("Cursos: " + lstCurso.length);
            
            Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Ok", TipoMensaje.MENSAJE));
            
            retorno.getLstObjetos().addAll(Arrays.asList(lstCurso));
            
            System.err.println("Objetos: " + retorno.getLstObjetos().size());
            
            return retorno;
            
            /*
            for(MoodleCourse s : lstCurso)
            {
                retorno.getLstObjetos().add(s);
            }
            */
            
        } catch (MoodleRestException | UnsupportedEncodingException ex) {
            Logger.getLogger(LoEstudio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new Retorno_MsgObj();
    }
}
