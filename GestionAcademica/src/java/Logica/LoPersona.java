/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Entidad.Curso;
import Entidad.Escolaridad;
import Entidad.Inscripcion;
import Entidad.Materia;
import Entidad.MateriaRevalida;
import Entidad.Modulo;
import Entidad.Parametro;
import Entidad.Persona;
import Enumerado.Constantes;
import Enumerado.MoodleAuth;
import Enumerado.TipoMensaje;
import Logica.Notificacion.AsyncBandeja;
import Logica.Notificacion.NotificacionesInternas;
import Moodle.Criteria;
import Moodle.MoodleCourse;
import Moodle.MoodleRestCourse;
import Moodle.MoodleRestEnrol;
import Moodle.MoodleRestException;
import Moodle.MoodleRestUser;
import Moodle.MoodleUser;
import Moodle.MoodleUserRoleException;
import Moodle.UserRole;
import Persistencia.PerManejador;
import SDT.SDT_Parameters;
import SDT.SDT_PersonaEstudio;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alvar
 */
public class LoPersona implements Interfaz.InPersona{
    
    private static LoPersona        instancia;
    private final MoodleRestUser    mdlRestUser;
    private final MoodleRestEnrol   mdlEnrol;
    private final MoodleRestCourse  mdlCourse;
    private final Parametro         param;
    private final Seguridad         seguridad;

    private LoPersona() {
        param               = LoParametro.GetInstancia().obtener();
        mdlRestUser         = new MoodleRestUser();
        seguridad           = Seguridad.GetInstancia();
        mdlEnrol            = new  MoodleRestEnrol();
        mdlCourse           = new MoodleRestCourse();
    
    }
    
    /**
     * Obtener instancia
     * @return Instancia de clase
     */
    public static LoPersona GetInstancia(){
        if (instancia==null)
        {
            instancia   = new LoPersona();
            
        }

        return instancia;
    }

    /**
     * Guardar persona
     * @param pObjeto Persona
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object guardar(Persona pObjeto) {
        boolean error           = false;
        Retorno_MsgObj retorno  = new Retorno_MsgObj(new Mensajes("Error", TipoMensaje.ERROR), pObjeto);
        
        if(this.ValidarEmail(pObjeto.getPerEml(), null))
        {
            retorno = new Retorno_MsgObj(new Mensajes("El email ya existe", TipoMensaje.ERROR), null);
            error   = true;
        }
        
        if(this.ValidarUsuario(pObjeto.getPerUsrMod(), null))
        {
            retorno = new Retorno_MsgObj(new Mensajes("El usuario ya existe", TipoMensaje.ERROR), null);
            error   = true;
        }
        
        if(param.getParUtlMdl())
        {
            if(pObjeto.getPerUsrModID() == null)
            {
                retorno = this.Mdl_AgregarUsuario(pObjeto);
            }
            else
            {
                retorno = this.Mdl_ActualizarUsuario(pObjeto);
            }
            
            error = retorno.SurgioError();
        }    


        
        if(!error)
        {
            PerManejador perManager = new PerManejador();
            
            pObjeto.setObjFchMod(new Date());
            
            retorno = perManager.guardar(pObjeto);
            
            if(!retorno.SurgioErrorObjetoRequerido())
            {
                pObjeto.setPerCod((Long) retorno.getObjeto());
                retorno.setObjeto(pObjeto);
            }
            
        }
        
        return retorno;
    }

    /**
     * Actualizar persona
     * @param pObjeto Persona
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object actualizar(Persona pObjeto) {
        boolean error           = false;
        Retorno_MsgObj retorno  = new Retorno_MsgObj(new Mensajes("Error al actualizar", TipoMensaje.ERROR), pObjeto);
        
        
        if(this.ValidarEmail(pObjeto.getPerEml(), pObjeto.getPerCod()))
        {
            retorno = new Retorno_MsgObj(new Mensajes("El email ya existe", TipoMensaje.ERROR), null);
            error   = true;
        }
        
        if(this.ValidarUsuario(pObjeto.getPerUsrMod(), pObjeto.getPerCod()))
        {
            retorno = new Retorno_MsgObj(new Mensajes("El usuario ya existe", TipoMensaje.ERROR), null);
            error   = true;
        }
        
        if(param.getParUtlMdl())
        {
            if(pObjeto.getPerUsrModID() == null)
            {
                retorno = this.Mdl_AgregarUsuario(pObjeto);
            }
            else
            {
                retorno = this.Mdl_ActualizarUsuario(pObjeto);
            }
            
            error = retorno.SurgioErrorObjetoRequerido();
        }


        if(!error)
        {
            PerManejador perManager = new PerManejador();
            
            pObjeto = (Persona) retorno.getObjeto();
            
            pObjeto.setObjFchMod(new Date());
            
            retorno = perManager.actualizar(pObjeto);
            
        }
        
        return retorno;
    }

    /**
     * Eliminar Persona
     * @param pObjeto Persona 
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object eliminar(Persona pObjeto) {
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Eliminando", TipoMensaje.MENSAJE));
        
        if(param.getParUtlMdl())
        {
            retorno  = this.Mdl_EliminarUsuario(pObjeto);
        }
        
       if(!retorno.SurgioError())
       {
           PerManejador perManager = new PerManejador();
           retorno = (Retorno_MsgObj) perManager.eliminar(pObjeto);
       }
       
       return retorno;
    }

    /**
     * Obtener persona
     * @param pCodigo Long - PerCod
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtener(Object pCodigo) {
        PerManejador perManager = new PerManejador();
        return perManager.obtener((Long) pCodigo, Persona.class);
    }
    
    /**
     * Obtener persona por usuario
     * @param pMdlUsr Usuario
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtenerByMdlUsr(String pMdlUsr) {
        PerManejador perManager = new PerManejador();

        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        SDT_Parameters parametro = new SDT_Parameters(pMdlUsr, "MdlUsr");
        lstParametros.add(parametro);

        Retorno_MsgObj retorno = perManager.obtenerLista("Persona.findByMdlUsr", lstParametros);
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            if (!retorno.getLstObjetos().isEmpty())
            {
                retorno.setObjeto(retorno.getLstObjetos().get(0));
                retorno.setLstObjetos(null);
            }
        }
        
        return retorno;
    }

    /**
     * Obtener lista de personas
     * @return Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtenerLista() {
        PerManejador perManager = new PerManejador();

        return perManager.obtenerLista("Persona.findAll", null);
    }
    
    /**
     * Obtener lista de personas para popup
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj obtenerPopUp(){
        
        Retorno_MsgObj retorno = this.obtenerLista();
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            for(Object objeto : retorno.getLstObjetos())
            {
                Persona persona = (Persona) objeto;

                persona.setLstEstudios(this.ObtenerEstudios(persona.getPerCod()));

            }
        }

        return retorno;
        
    }
    
    /**
     * Obtener persona por email
     * @param pEmail Email
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj obtenerByEmail(String pEmail) {
        PerManejador perManager = new PerManejador();

        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        SDT_Parameters parametro = new SDT_Parameters(pEmail, "PerEml");
        lstParametros.add(parametro);

        Retorno_MsgObj retorno = perManager.obtenerLista("Persona.findByEmail", lstParametros);
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            if (!retorno.getLstObjetos().isEmpty())
            {
                retorno.setObjeto(retorno.getLstObjetos().get(0));
                retorno.setLstObjetos(null);
            }
        }
        
        return retorno;
    }
    
    /**
     * Obtener persona por token de aplicación
     * @param PerAppTkn token
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ObtenerPersonaByAppTkn(String PerAppTkn){
        PerManejador perManager = new PerManejador();
                
        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        SDT_Parameters parametro = new SDT_Parameters(PerAppTkn, "PerAppTkn");
        lstParametros.add(parametro);
        
        Retorno_MsgObj retorno = perManager.obtenerLista("Persona.findByAppTkn", lstParametros);
                
        return retorno;
        
    }
    
    //----------------------------------------------------------------------------------------------------

    /**
     * Obtener estudios de una persona
     * @param PerCod Código de Persona
     * @return Resultado: RETORNO_MSGOBJ
     */
    
    public ArrayList<SDT_PersonaEstudio> ObtenerEstudios(Long PerCod){
        ArrayList<SDT_PersonaEstudio> lstEstudios = new ArrayList<>();;
        
        Retorno_MsgObj inscripcion                  = LoInscripcion.GetInstancia().obtenerListaByAlumno(PerCod);
        for(Object objeto : inscripcion.getLstObjetos())
        {
            lstEstudios  = PersonaAgregarEstudio(lstEstudios, null, (Inscripcion) objeto);
        }
        
        Retorno_MsgObj retorno = LoEscolaridad.GetInstancia().obtenerListaByAlumno(PerCod);
        
        if(!retorno.SurgioErrorListaRequerida())
        {

            
                    
            for(Object objeto : retorno.getLstObjetos())
            {
                Escolaridad escolaridad = (Escolaridad) objeto;
                
                if(escolaridad.getMateria() != null) retorno = LoInscripcion.GetInstancia().obtenerInscByPlan(PerCod, escolaridad.getMateria().getPlan().getPlaEstCod());
                
                if(escolaridad.getModulo() != null) retorno = LoInscripcion.GetInstancia().obtenerInscByCurso(PerCod, escolaridad.getModulo().getCurso().getCurCod());
                
                if(escolaridad.getCurso() != null) retorno = LoInscripcion.GetInstancia().obtenerInscByCurso(PerCod, escolaridad.getCurso().getCurCod());

                if(!retorno.SurgioError()) lstEstudios = PersonaAgregarEstudio(lstEstudios, escolaridad, (Inscripcion) retorno.getObjeto());
                
                
            }
        }
        
        return lstEstudios;
    }

    /**
     * Agrega estudio a la lista
     * @param lstEstudio Lista de estudio
     * @param escolaridad Escolaridad
     * @param inscripcion Inscripción
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private ArrayList<SDT_PersonaEstudio> PersonaAgregarEstudio(ArrayList<SDT_PersonaEstudio> lstEstudio, Escolaridad escolaridad, Inscripcion inscripcion){
        boolean existe = false;
        
        if(inscripcion == null)
        {
           inscripcion = new Inscripcion();
           inscripcion.setInsCod(Long.valueOf("0"));
        }
        
        for(SDT_PersonaEstudio est : lstEstudio)
        {
            if(est.getInscripcion().getInsCod().equals(inscripcion.getInsCod())){
                existe = true;
                est.getEscolaridad().add(escolaridad);
                break;
            }
        }
        
        if(!existe){
            SDT_PersonaEstudio est = new SDT_PersonaEstudio();
            
            if(inscripcion.getPlanEstudio() != null)
            {                    
                Collections.sort(inscripcion.getPlanEstudio().getLstMateria(), new Comparator<Materia>(){
                            public int compare(Materia o1, Materia o2){
                            if(o1.getMatPerVal() == o2.getMatPerVal())
                                return 0;
                            return o1.getMatPerVal() < o2.getMatPerVal() ? -1 : 1;
                        }
                   });
            }
            
            if(inscripcion.getCurso() != null)
            {
                Collections.sort(inscripcion.getCurso().getLstModulos(), new Comparator<Modulo>(){
                            public int compare(Modulo o1, Modulo o2){
                            if(o1.getModPerVal() == o2.getModPerVal())
                                return 0;
                            return o1.getModPerVal() < o2.getModPerVal() ? -1 : 1;
                        }
                   });
            }
            
            est.setInscripcion(inscripcion);
            
            if(escolaridad != null) est.getEscolaridad().add(escolaridad);
            
            for(MateriaRevalida matRvl : inscripcion.getLstRevalidas())
            {
                Escolaridad esc = new Escolaridad();
                esc.setAlumno(inscripcion.getAlumno());
                esc.setEscCalVal(Double.NaN);
                esc.setMateria(matRvl.getMateria());
                est.getEscolaridad().add(esc);
            }
            
            lstEstudio.add(est);
        }
        
        

        return lstEstudio;
    }
    
    /**
     * Retorna si una persona aprobo un estudio
     * @param PerCod Código de persona
     * @param materia Materia
     * @param modulo Modulo
     * @param curso Curso
     * @return Resultado: RETORNO_MSGOBJ
     */
    public boolean PersonaAproboEstudio(Long PerCod, Materia materia, Modulo modulo, Curso curso){
        
        ArrayList<SDT_PersonaEstudio> lstEstudio = this.ObtenerEstudios(PerCod);
        Boolean estudioAprobado = false;
        for(SDT_PersonaEstudio estudio : lstEstudio)
        {
            for(Escolaridad escolaridad : estudio.getEscolaridad())
            {
                if(!estudioAprobado)
                {
                    if(escolaridad.getMateria() != null && materia != null)  if(escolaridad.getMateria().equals(materia)) estudioAprobado = escolaridad.getAprobado();
                    if(escolaridad.getCurso() != null && curso != null)  if(escolaridad.getCurso().equals(curso))  estudioAprobado = escolaridad.getAprobado();
                    if(escolaridad.getModulo() != null && modulo != null)  if(escolaridad.getModulo().equals(modulo))  estudioAprobado =  escolaridad.getAprobado();
                }
            }
        }
        
        return estudioAprobado;
    }
    
    /**
     * Determinar si una persona revalida una materia
     * @param PerCod Código de persona
     * @param materia Materia
     * @return Resultado: RETORNO_MSGOBJ
     */
    public boolean PersonaRevalidaMateria(Long PerCod, Materia materia){
        if(materia == null)
        {
            return false;
        }
        
        ArrayList<SDT_PersonaEstudio> lstEstudio = this.ObtenerEstudios(PerCod);
        
        for(SDT_PersonaEstudio estudio : lstEstudio)
        {
            for(Escolaridad escolaridad : estudio.getEscolaridad())
            {
                if(escolaridad.getMateria() != null && materia != null)  if(escolaridad.getMateria().equals(materia)) return escolaridad.Revalida();
            }
        }
        
        return false;
    }
    
    /**
     * Determinar si una persona puede dar un examen
     * @param PerCod Código de persona
     * @param materia Materia
     * @param modulo Modulo
     * @param curso Curso
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Boolean PersonaPuedeDarExamen(Long PerCod, Materia materia, Modulo modulo, Curso curso){
        return LoCalendario.GetInstancia().AlumnoPuedeDarExamen(PerCod, materia, modulo, curso);
        
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Actualizar token de aplicación android
     * @param PerCod Código de persona
     * @param PerAppTkn Token de aplicación android
     * @return Resultado: RETORNO_MSGOBJ
     */
    
    public Retorno_MsgObj ActualizarToken(Long PerCod, String PerAppTkn){
        
        Boolean error = false;
        Retorno_MsgObj retorno;
        
        //LIMPIO TOKEN IGUAL EN OTRAS PERSONAS, ESTO SUCEDE SI ALGUIEN INICIA SESION CON DISTINTAS CUENTAS EN UN MISMO DISPOSITIVO
        
        retorno = this.ObtenerPersonaByAppTkn(PerAppTkn); //perPersona.obtenerByAppTkn(PerAppTkn);
        error   = retorno.SurgioError();
        
        if(!error)
        {
            for(Object perTkn : retorno.getLstObjetos())
            {
                Persona persona = (Persona) perTkn;
                persona.setPerAppTkn(null);
                retorno = (Retorno_MsgObj) this.actualizar(persona);
                
                if(retorno.SurgioError())
                {
                    error = true;
                    break;
                }
            }
        }
        
        if(!error)
        {
            retorno = this.obtener(PerCod);

            if(!retorno.SurgioErrorObjetoRequerido())
            {
                Persona persona = (Persona) retorno.getObjeto();

                persona.setPerAppTkn(PerAppTkn);

                retorno = (Retorno_MsgObj) this.actualizar(persona);
                
                if(!retorno.SurgioError())
                {
                    //ENVIO NOTIFICACIONES
                    //LoBandeja.GetInstancia().NotificarPendientes(persona.getPerCod());
                    
                    AsyncBandeja xthread = null;
                    //Long milliseconds = 10000; // 10 seconds
                      try {
                        xthread = new AsyncBandeja(persona.getPerCod());
                        xthread.start();
                      //  xthread.join(milliseconds);
                      } catch (Exception ex) {

                          Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, "[InterfacesAgent] Error" + ex);
                      } finally {
                        if (xthread != null && xthread.isAlive()) {
                          Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, "[InterfacesAgent] Interrupting" );
                          xthread.interrupt();
                        }
                      }
                }
            }
        }
        
        return retorno;
    }
    
    /**
     * Limpia el token aplicación movil de una persona, esto se hace cuando 
     * cierra sesion en el dispositivo
     * @param PerCod Código de persona
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj LimpiarToken(Long PerCod){
        
        Retorno_MsgObj retorno  = this.obtener(PerCod);
        Boolean error           = retorno.SurgioErrorObjetoRequerido();
        
        if(!error)
        {
            Persona persona = (Persona) retorno.getObjeto();

            persona.setPerAppTkn(null);

            retorno = (Retorno_MsgObj) this.actualizar(persona);
        }
        
        return retorno;
    }
    
    //----------------------------------------------------------------------------------------------------

    /**
     * Inicar sesión
     * @param usuario Usuario
     * @param password Contraseña (MD5)
     * @return Inicio correcto
     */
    
    public Boolean IniciarSesion(String usuario, String password){
        boolean resultado = false;
        
            Persona persona = new Persona();
            
            Retorno_MsgObj retorno = this.obtenerByMdlUsr(usuario);
            
            if(!retorno.SurgioErrorObjetoRequerido())
            {
                    persona = (Persona) retorno.getObjeto();
            }
            else
            {
                retorno = this.obtenerByEmail(usuario);
                
                if(!retorno.SurgioErrorObjetoRequerido())
                {
                    persona = (Persona) retorno.getObjeto();
                }
            }
            
            if(persona.getPerCod() != null)
            {
            
                resultado = persona.getPerPass().equals(password);

                if(resultado)
                {
                    persona.setPerFchLog(new Date());
                    persona.setPerCntIntLgn(0);
                        
                    try {
                        persona.setPerLgnTkn(seguridad.crypt(Utiles.Utilidades.GetInstancia().GenerarToken(20)));
                    } catch (Exception ex) {
                        Logger.getLogger(LoPersona.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
                }
                else
                {
                    persona.setPerCntIntLgn((persona.getPerCntIntLgn() != null ? persona.getPerCntIntLgn() + 1 : 1));
                }

                
                PerManejador perManager = new PerManejador();
                perManager.actualizar(persona);
            }
            
        
        return resultado;
    }
    
    /**
     * Cambiar contraseña
     * @param usuario Usuario
     * @param pswActual Contraseña actual (Sin MD5)
     * @param pswNueva Contraseña nueva (Sin MD5)
     * @param pswConf Confirmación de contraseña (Sin MD5)
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj CambiarPassword(String usuario, String pswActual, String pswNueva, String pswConf){
        

        Retorno_MsgObj retorno = this.obtenerByMdlUsr(usuario);
        if(!retorno.SurgioErrorObjetoRequerido())
        {
            Persona persona = (Persona) retorno.getObjeto();
            if(seguridad.cryptWithMD5(pswActual).equals(persona.getPerPass()))
            {
                boolean error = false;
                
                if(param.getParPswValExp() != null)
                {
                    if(!pswNueva.matches(param.getParPswValExp()))
                    {
                        retorno.setMensaje(new Mensajes(param.getParPswValMsg(), TipoMensaje.ERROR));
                        error = true;
                    }
                }   
                
                if(!pswConf.equals(pswNueva))
                {
                    error = true;
                    retorno.setMensaje(new Mensajes("No coincide la confirmación", TipoMensaje.ERROR));
                }

                if(!error)
                {
                    
                    persona.setPerPass(seguridad.cryptWithMD5(pswNueva));
                    retorno = (Retorno_MsgObj) this.actualizar(persona);

                    if(!retorno.SurgioError())
                    {
                        NotificacionesInternas notInt = new NotificacionesInternas();
                        notInt.Notificar_CAMBIAR_PASSWORD(persona);
                    }
                }
            }
            else
            {
                retorno.setMensaje(new Mensajes("Contraseña actual no es valida", TipoMensaje.ERROR));
            }
        }
        return retorno;
    }
    
    /**
     * Validar cambio de contraseña externo
     * @param usr Usuario
     * @param tkn Token
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ValCambiarPasswordExterno(String usr, String tkn){

        String usuario = seguridad.decrypt(usr
                , Constantes.ENCRYPT_VECTOR_INICIO.getValor()
                , Constantes.ENCRYPT_SEMILLA.getValor());
        
        Retorno_MsgObj retorno = this.obtenerByMdlUsr(usuario);
        if(!retorno.SurgioErrorObjetoRequerido())
        {
            Persona persona = (Persona) retorno.getObjeto();
            if(tkn.equals(persona.getPerLgnTkn()))
            {
                retorno.setMensaje(new Mensajes("OK", TipoMensaje.MENSAJE));
            }
            else
            {
                retorno.setMensaje(new Mensajes("Token invalido, posiblemente acceso ilegal o finalizo sesion", TipoMensaje.ERROR));
            }
        }
        return retorno;
    }
    
    /**
     * Usuario solicita restablecer contraseña
     * @param usuario Usuario
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj SolicitaRestablecerPassword(String usuario){
        Retorno_MsgObj retorno = this.obtenerByMdlUsr(usuario);
        if(!retorno.SurgioErrorObjetoRequerido())
        {
            Persona persona = (Persona) retorno.getObjeto();
            String psw      = this.GenerarPassword(10);
            
            try {
                
                String token    =  URLEncoder.encode(seguridad.crypt(persona.getPerCod().toString()
                                + Constantes.SEPARADOR.getValor()
                                + psw), "UTF-8");
                
                String url = Utiles.Utilidades.GetInstancia().GetUrlSistema()
                        + "pswRecovery.jsp?tkn=" + token;
                
                String urlCorta = "<a href='" + url
                        + "' > Recuperar contraseña </a>";
                
                String urlLarga = "<a href='" + url
                        + "' >" + url + "</a>";
                
                persona.setPerPassAux(seguridad.cryptWithMD5(psw));
                
                this.actualizar(persona);
            
                NotificacionesInternas notInt = new NotificacionesInternas();
                notInt.Notificar_RESTABLECER_PASSWORD(persona, urlCorta, urlLarga);
            
            } catch (Exception ex) {
                Logger.getLogger(LoPersona.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        else
        {
            retorno.setMensaje(new Mensajes("El usuario que indico no existe", TipoMensaje.ERROR));
        }
        return retorno;
    }
    
    /**
     * Usuario restablece contraseña
     * @param PerCod Código de persona
     * @param tkn Token
     * @param pswNueva Contraseña nueva (Sin MD5)
     * @param pswConf Confirmación de contraseña (Sin MD5   )
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj RestablecerPassword(Long PerCod, String tkn, String pswNueva, String pswConf){
        Retorno_MsgObj retorno = this.obtener(PerCod);
        if(!retorno.SurgioErrorObjetoRequerido())
        {
            if(!pswConf.equals(pswNueva))
            {
                retorno.setMensaje(new Mensajes("Las contraseñas no coinciden", TipoMensaje.ERROR));
            }
            else
            {
                
                Persona persona = (Persona) retorno.getObjeto();
                
                String pswTknCrypt      = seguridad.cryptWithMD5(tkn);
                String pswNuevaCrypt    = seguridad.cryptWithMD5(pswNueva);

                if(pswTknCrypt.equals(persona.getPerPassAux()))
                {
                    if(!persona.getPerPass().equals(pswNuevaCrypt))
                    {
                        persona.setPerPass(pswNuevaCrypt);
                        persona.setPerPassAux(null);
                        retorno = (Retorno_MsgObj) this.actualizar(persona);

                        if(!retorno.SurgioError())
                        {
                            NotificacionesInternas notInt = new NotificacionesInternas();
                            notInt.Notificar_CAMBIAR_PASSWORD(persona);
                        }
                    }
                    else
                    {
                        retorno.setMensaje(new Mensajes("Ingreso la misma contraseña", TipoMensaje.ERROR));
                    }
                }
                else
                {
                    retorno.setMensaje(new Mensajes("Token invalido, puede que su cuenta ya haya sido recuperada", TipoMensaje.ERROR));
                }
            }
        }
        return retorno;
    }
    
    /**
     * Generar nueva contraseña aleatoria
     * @param longitud Longitud
     * @return Contraseña
     */
    private String GenerarPassword(Integer longitud){
        return Utiles.Utilidades.GetInstancia().GenerarToken(longitud);
    }

    /**
     * Valida que el email no exista en otra persona que la original
     * @param email Email
     * @param idOriginal Código de persona original
     * @return Email valido
     */
    private boolean ValidarEmail(String email, Long idOriginal){
        boolean error = false;

        Retorno_MsgObj retorno = this.obtenerByEmail(email);
        
        if(idOriginal == null)
        {
            error = retorno.getObjeto() != null;
        }
        else
        {
            if(retorno.getObjeto() != null)
            {
                error = !((Persona) retorno.getObjeto()).getPerCod().equals(idOriginal);
            }
        }
        
        return error;
    }

    /**
     * Validar que el usuario no exista en otra persona que la original
     * @param usuario Usuario
     * @param idOriginal Código de persona original
     * @return Usuario valido
     */
    private boolean ValidarUsuario(String usuario, Long idOriginal){
        boolean error = false;

        Retorno_MsgObj retorno = this.obtenerByMdlUsr(usuario);
        
        if(idOriginal == null)
        {
            error = retorno.getObjeto() != null;
        }
        else
        {
            if(retorno.getObjeto() != null)
            {
                error = !((Persona) retorno.getObjeto()).getPerCod().equals(idOriginal);
            }
        }
        
        return error;
    }

    
    //----------------------------------------------------------------------------------------------------
    //-Modle
    //----------------------------------------------------------------------------------------------------

    /**
     * Obtener usuarios de moodle, y crearlo como personas
     */
    
    public void SincronizarUsuariosMoodleSistema()
    {
        List<Persona> lstPersonas = this.MdlObtenerUsuarios();
        
        for(Persona persona : lstPersonas)
        {
            if(!persona.getPerUsrMod().equals("") )
            {
 
                if(persona.getPerCod() == null)
                {
                    persona.setPerPass(seguridad.cryptWithMD5("admin"));
                    this.guardar(persona);
                }
                else
                {
                    this.actualizar(persona);
                }
            }
        }
        
        SincronizarTipoUsuarioMdl();
    }
    
    /**
     * Analiza los roles de usuario en moodle, y actualiza
     * el tipo de usuario en la persona
     */
    private void SincronizarTipoUsuarioMdl()
    {
        try {
            MoodleCourse[] lstCurso = mdlCourse.__getAllCourses(param.getParUrlMdl()+ Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), param.getParMdlTkn());
            
            for(int i = 0; i<lstCurso.length; i++)
            {
                MoodleCourse curso      = lstCurso[i];
                MoodleUser[] lstUser    = mdlEnrol.__getEnrolledUsers(param.getParUrlMdl()+ Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), param.getParMdlTkn(), curso.getId(), null);
               
                if(lstUser != null)
                {
                    for(int e = 0; e < lstUser.length; e++)
                    {
                        MoodleUser mdlUsr           = lstUser[e];
                        ArrayList<UserRole> roles   = mdlUsr.getRoles();

                        Persona persona = (Persona) this.obtenerByMdlUsr(mdlUsr.getUsername()).getObjeto();
                        
                        for(UserRole userRole : roles)
                        {
                            switch(userRole.getRole())
                            {
                                case STUDENT:
                                    persona.setPerEsAlu(Boolean.TRUE);
                                    break;
                                case TEACHER:
                                    persona.setPerEsDoc(Boolean.TRUE);
                                    break;
                            }
                        }
                        
                        PerManejador perManager = new PerManejador();
                        perManager.actualizar(persona);
                        
                        //perManager.actualizar(persona);

                    }
                }
                
            }
            
        } catch (MoodleRestException | UnsupportedEncodingException | MoodleUserRoleException ex) {
            Logger.getLogger(LoPersona.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * Obtener usuarios de moodle
     * @return Lista de personas
     */
    private List<Persona> MdlObtenerUsuarios()
    {
        List<Persona> lstPersonas = new ArrayList<>();
        
        try
        {
            Criteria criteria = new Criteria();

            criteria.setKey("name");
            criteria.setValue("");

            Criteria[] lstCriteria   = new Criteria[1];
            lstCriteria[0]           = criteria;
            MoodleUser[] lstUsr = mdlRestUser.__getUsers(param.getParUrlMdl() + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), param.getParMdlTkn(), lstCriteria);


             for(MoodleUser usr: lstUsr)
             {
                Persona persona = Mdl_UsuarioToPersona(usr);
                lstPersonas.add(persona);
             }
        }
        catch(MoodleRestException | MoodleUserRoleException ex)
        {
            System.err.println("Error: " + ex.getLocalizedMessage());
        }


        return lstPersonas;
    }
    
    /**
     * Castea el usuario de moodle a persona
     * @param usuario Usuario
     * @return Persona
     */
    private Persona Mdl_UsuarioToPersona(MoodleUser usuario)
    {
        Persona persona = (Persona) this.obtenerByMdlUsr(usuario.getUsername()).getObjeto();
        if(persona == null)
        {
            persona = new Persona();
        }
        persona.setPerUsrMod(usuario.getUsername());
        persona.setPerEml(usuario.getEmail());
        persona.setPerNom(usuario.getFirstname());
        persona.setPerApe(usuario.getLastname());
        persona.setPerUsrModID(usuario.getId());
        
        return persona;
    }
    
    //----------------------------------------------------------------------------------------------------
    
    /**
     * Agregar persona como usuario a Moodle
     * @param persona Persona
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj Mdl_AgregarUsuario(Persona persona)
    {
        Retorno_MsgObj retorno;

        MoodleUser usr = new MoodleUser();
        
        usr.setUsername(persona.getPerUsrMod());
        usr.setEmail(persona.getPerEml());
        usr.setFirstname(persona.getPerNom());
        usr.setLastname(persona.getPerApe());
        usr.setFirstname(persona.getPerNom());
        usr.setPassword("21!_646?adD.09Ajku");
        usr.setAuth(MoodleAuth.WEBSERVICE.getValor());
        
        try {

            usr = mdlRestUser.__createUser(param.getParUrlMdl() + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), param.getParMdlTkn(), usr);
            persona.setPerUsrModID(usr.getId());
            retorno = new Retorno_MsgObj(new Mensajes("Cambios correctos", TipoMensaje.MENSAJE), persona);
            
        } catch (MoodleRestException ex) {

            retorno = new Retorno_MsgObj(new Mensajes("Error: " + ex.getMessage(), TipoMensaje.ERROR), persona);
            Logger.getLogger(LoEstudio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return retorno;
    }

    /**
     * Actualizar persona como usuario en Moodle
     * @param persona Persona
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj Mdl_ActualizarUsuario(Persona persona)
    {
        Retorno_MsgObj retorno = Mdl_ObtenerUsuarioByID(persona.getPerUsrModID());
        
        if(!retorno.SurgioErrorObjetoRequerido())
        {
            MoodleUser usr      = (MoodleUser) retorno.getObjeto();
        
            usr.setUsername(persona.getPerUsrMod());
            usr.setEmail(persona.getPerEml());
            usr.setFirstname(persona.getPerNom());
            usr.setLastname(persona.getPerApe());
            usr.setFirstname(persona.getPerNom());
        
            try
            {
                mdlRestUser.__updateUser(param.getParUrlMdl() + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), param.getParMdlTkn(), usr);
                retorno = new Retorno_MsgObj(new Mensajes("Cambios correctos", TipoMensaje.MENSAJE), persona);
            } catch (MoodleRestException ex) {

                retorno = new Retorno_MsgObj(new Mensajes("Error: " + ex.getMessage(), TipoMensaje.ERROR), persona);
                Logger.getLogger(LoEstudio.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
        return retorno;
    }

    /**
     * Eliminar usuario en Moodle, en base a persona
     * @param persona Persona
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj Mdl_EliminarUsuario(Persona persona)
    {
        Retorno_MsgObj retorno;
        
        try
        {
            mdlRestUser.__deleteUser(param.getParUrlMdl() + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), param.getParMdlTkn(), persona.getPerUsrModID());
            
            retorno = new Retorno_MsgObj(new Mensajes("Cambios correctos", TipoMensaje.MENSAJE), null);
        } catch (MoodleRestException ex) {

            retorno = new Retorno_MsgObj(new Mensajes("Error: " + ex.getMessage(), TipoMensaje.ERROR), persona);
            Logger.getLogger(LoEstudio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            retorno = new Retorno_MsgObj(new Mensajes("Error: " + ex.getMessage(), TipoMensaje.ERROR), persona);
            Logger.getLogger(LoPersona.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return retorno;
    }
    
    /**
     * Obtener usuario en moodle, en base a el ID
     * @param id Código de usuario en Moodle
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj Mdl_ObtenerUsuarioByID(Long id)
    {
        Retorno_MsgObj retorno;
 
        try
        {
            MoodleUser usr = mdlRestUser.__getUserById(param.getParUrlMdl() + Constantes.URL_FOLDER_SERVICIO_MDL.getValor(), param.getParMdlTkn(), id);
            retorno = new Retorno_MsgObj(new Mensajes("Ok", TipoMensaje.MENSAJE), usr);
        }
        catch (MoodleRestException ex) {

            retorno = new Retorno_MsgObj(new Mensajes("Error: " + ex.getMessage(), TipoMensaje.ERROR), null);
            Logger.getLogger(LoEstudio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            retorno = new Retorno_MsgObj(new Mensajes("Error: " + ex.getMessage(), TipoMensaje.ERROR), null);
            Logger.getLogger(LoPersona.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retorno;
    }
    //----------------------------------------------------------------------------------------------------
    //-Fin Modle
    //----------------------------------------------------------------------------------------------------
}
