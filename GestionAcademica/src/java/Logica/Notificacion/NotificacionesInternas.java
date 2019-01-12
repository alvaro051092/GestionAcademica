/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica.Notificacion;

import Entidad.Calendario;
import Entidad.Notificacion;
import Entidad.NotificacionConsulta;
import Entidad.NotificacionDestinatario;
import Entidad.Persona;
import Enumerado.NotificacionSistema;
import Enumerado.ObtenerDestinatario;
import Enumerado.TipoConsulta;
import Enumerado.TipoNotificacion;
import Enumerado.TipoRepeticion;
import Logica.LoCalendario;
import Logica.LoNotificacion;
import Logica.LoPersona;
import Utiles.Retorno_MsgObj;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alvar
 */
public class NotificacionesInternas {
    
    /**
     * Cargar notificaciones
     */
    public void CargarNotificaciones(){
        
        //EVALUACION HABILITADA A INSCRIPCION
        LoNotificacion.GetInstancia().guardar(this.EVALUACION_HABILITADA_INSCRIPCION());
        
        //EVALUACION PROXIMA
        LoNotificacion.GetInstancia().guardar(this.EVALUACION_PROXIMA());
        
        //ESCOLARIDAD ACTUALIZADA
        LoNotificacion.GetInstancia().guardar(this.ESCOLARIDAD_ACTUALIZADA());
        
        //ALUMNOS INACTIVOS
        LoNotificacion.GetInstancia().guardar(this.ALUMNOS_INACTIVOS());
        
        //RESTABLECER PASSWORD
        LoNotificacion.GetInstancia().guardar(this.RESTABLECER_PASSWORD());
        
        //CAMBIAR PASSWORD
        LoNotificacion.GetInstancia().guardar(this.CAMBIAR_PASSWORD());
     }

    /**
     * Armar notificacion: Evaluacion habilitada para inscripcion
     * @return Notificacion
     */
    private Notificacion EVALUACION_HABILITADA_INSCRIPCION(){
        Notificacion notificacion = new Notificacion();
        notificacion.setNotNom(NotificacionSistema.EVALUACION_HABILITADA_INSCRIPCION.name());
        notificacion.setNotInt(Boolean.TRUE);       
        notificacion.setNotObtDest(ObtenerDestinatario.UNICA_VEZ);
        notificacion.setNotRepTpo(TipoRepeticion.DIAS);
        notificacion.setNotRepVal(1);
        notificacion.setNotTpo(TipoNotificacion.AUTOMATICA);

        notificacion.setNotAct(Boolean.TRUE);
        notificacion.setNotApp(Boolean.TRUE);
        notificacion.setNotEmail(Boolean.TRUE);
        notificacion.setNotWeb(Boolean.TRUE);
        
        notificacion.setNotDsc("Evaluación habilitada a inscripción");
        notificacion.setNotAsu("[%=EVALUACION] de [%=ESTUDIO]");
        notificacion.setNotCon("<p>Se puede inscribir a [%=EVALUACION] de [%=ESTUDIO] desde [%=DESDE], hasta: [%=HASTA]</p>");
        
        return notificacion;
    }
    
    /**
     * Armar notificacion: Evaluacion proxima
     * @return Notificacion
     */
    private Notificacion EVALUACION_PROXIMA(){
        Notificacion notificacion = new Notificacion();
        notificacion.setNotNom(NotificacionSistema.EVALUACION_PROXIMA.name());
        notificacion.setNotInt(Boolean.TRUE);       
        notificacion.setNotObtDest(ObtenerDestinatario.POR_CADA_REGISTRO);
        notificacion.setNotRepTpo(TipoRepeticion.DIAS);
        notificacion.setNotRepVal(1);
        notificacion.setNotTpo(TipoNotificacion.AUTOMATICA);

        notificacion.setNotAct(Boolean.TRUE);
        notificacion.setNotApp(Boolean.TRUE);
        notificacion.setNotEmail(Boolean.TRUE);
        notificacion.setNotWeb(Boolean.TRUE);
        
        notificacion.setNotDsc("Evaluación próxima a la fecha");
        notificacion.setNotAsu("Se aproxima [%=EVALUACION_NOMBRE] de [%=EVALUACION_ESTUDIO]");
        notificacion.setNotCon("<p>[%=EVALUACION_NOMBRE] de [%=EVALUACION_ESTUDIO] el día [%=CALENDARIO_FCH]</p>");
        
        
        NotificacionConsulta contenido = new NotificacionConsulta();
        contenido.setNotCnsTpo(TipoConsulta.CONSULTA);
        contenido.setNotificacion(notificacion);
        contenido.setNotCnsSQL("SELECT \n" +
                                "        C.CalCod AS CALENDARIO_COD  \n" +
                                "        ,C.CalFch AS CALENDARIO_FCH\n" +
                                "        ,C.EvlCod AS EVALUACION_CODIGO\n" +
                                "        ,E.EvlNom AS EVALUACION_NOMBRE\n" +
                                "        ,(\n" +
                                "                CASE \n" +
                                "                        WHEN E.MatEvlMatCod IS NOT NULL THEN (SELECT MatNom FROM MATERIA MAT WHERE MAT.MatCod = E.MatEvlMatCod)\n" +
                                "                        WHEN E.ModEvlModCod IS NOT NULL THEN (SELECT ModNom FROM MODULO MD WHERE MD.ModCod = E.ModEvlModCod)\n" +
                                "                        WHEN E.CurEvlCurCod IS NOT NULL THEN (SELECT CurNom FROM CURSO CUR WHERE CUR.CurCod = E.CurEvlCurCod)\n" +
                                "                 END\n" +
                                "        ) AS EVALUACION_ESTUDIO\n" +
                                "        \n" +
                                "FROM\n" +
                                "        CALENDARIO C\n" +
                                "        ,EVALUACION E\n" +
                                "WHERE \n" +
                                "        C.EvlCod = E.EvlCod\n" +
                                "        AND CalFch = curdate() + (SELECT ParDiaEvlPrv FROM PARAMETRO P WHERE P.ParCod = 1)");
        
        notificacion.getLstConsulta().add(contenido);
        
        
        NotificacionConsulta destinatario = new NotificacionConsulta();
        destinatario.setNotCnsTpo(TipoConsulta.INC_DESTINATARIO);
        destinatario.setNotificacion(notificacion);
        destinatario.setNotCnsSQL("SELECT 1 AS TIPO, AluPerCod AS DESTINATARIO FROM CALENDARIO_ALUMNO WHERE CalCod = [%=CALENDARIO_COD]");
        
        notificacion.getLstConsulta().add(destinatario);
        
        return notificacion;
    }
    
    /**
     * Armar notificacion: Escolaridad actualizada
     * @return  Notificacion
     */
    private Notificacion ESCOLARIDAD_ACTUALIZADA(){
        Notificacion notificacion = new Notificacion();
        notificacion.setNotNom(NotificacionSistema.ESCOLARIDAD_ACTUALIZADA.name());
        notificacion.setNotInt(Boolean.TRUE);       
        notificacion.setNotObtDest(ObtenerDestinatario.POR_CADA_REGISTRO);
        notificacion.setNotRepTpo(TipoRepeticion.DIAS);
        notificacion.setNotRepVal(1);
        notificacion.setNotTpo(TipoNotificacion.AUTOMATICA);

        notificacion.setNotAct(Boolean.TRUE);
        notificacion.setNotApp(Boolean.TRUE);
        notificacion.setNotEmail(Boolean.TRUE);
        notificacion.setNotWeb(Boolean.TRUE);
        
        notificacion.setNotDsc("Escolaridad actualizada");
        notificacion.setNotAsu("Escolaridad actualizada");
        notificacion.setNotCon("<p>Se actualizó su escolaridad ([%=ESTUDIO]), puede consultarla desde la aplicación</p>");
        
        
        NotificacionConsulta contenido = new NotificacionConsulta();
        contenido.setNotCnsTpo(TipoConsulta.CONSULTA);
        contenido.setNotificacion(notificacion);
        contenido.setNotCnsSQL("SELECT \n" +
                                "        EscAluPerCod AS ALUMNO\n" +
                                "        ,CASE\n" +
                                "                WHEN EscMatCod IS NOT NULL THEN (SELECT MatNom FROM MATERIA MAT WHERE MAT.MatCod = EscMatCod)\n" +
                                "                WHEN EscModCod IS NOT NULL THEN (SELECT ModNom FROM MODULO MD WHERE MD.ModCod = EscModCod)\n" +
                                "                WHEN EscCurCod IS NOT NULL THEN (SELECT CurNom FROM CURSO CUR WHERE CUR.CurCod = EscCurCod)\n" +
                                "         END AS ESTUDIO\n" +
                                "FROM \n" +
                                "        ESCOLARIDAD \n" +
                                "WHERE \n" +
                                "        ESCFCH = CURDATE()");
        
        notificacion.getLstConsulta().add(contenido);
        
        
        NotificacionConsulta destinatario = new NotificacionConsulta();
        destinatario.setNotCnsTpo(TipoConsulta.INC_DESTINATARIO);
        destinatario.setNotificacion(notificacion);
        destinatario.setNotCnsSQL("SELECT 1 AS TIPO, [%=ALUMNO] AS DESTINATARIO");
        
        notificacion.getLstConsulta().add(destinatario);
        
        
        return notificacion;
    }
    
    /**
     * Armar notificacion: Alumnos inactivos
     * @return Notificacion
     */
    private Notificacion ALUMNOS_INACTIVOS(){
        Notificacion notificacion = new Notificacion();
        notificacion.setNotNom(NotificacionSistema.ALUMNOS_INACTIVOS.name());
        notificacion.setNotInt(Boolean.TRUE);       
        notificacion.setNotObtDest(ObtenerDestinatario.POR_CADA_REGISTRO);
        notificacion.setNotRepTpo(TipoRepeticion.MESES);
        notificacion.setNotRepVal(1);
        notificacion.setNotTpo(TipoNotificacion.AUTOMATICA);

        notificacion.setNotAct(Boolean.TRUE);
        notificacion.setNotApp(Boolean.TRUE);
        notificacion.setNotEmail(Boolean.TRUE);
        notificacion.setNotWeb(Boolean.TRUE);
        
        notificacion.setNotDsc("Alumnos inactivos");
        notificacion.setNotAsu("Falta poco para tu titulo de [%=INSCRIPCION_ESTUDIO]");
        notificacion.setNotCon("<p>Puedes dar las evaluaciones para poder conseguir tu titulo</p>");
        
        NotificacionConsulta contenido = new NotificacionConsulta();
        contenido.setNotCnsTpo(TipoConsulta.CONSULTA);
        contenido.setNotificacion(notificacion);
        contenido.setNotCnsSQL("SELECT\n" +
                                "        I.AluFchInsc AS INSCRIPCION_FECHA\n" +
                                "        ,I.InsGenAnio AS INSCRIPION_GENERACION\n" +
                                "        ,I.AluPerCod AS INSCRIPCION_ALUMNO\n" +
                                "        ,CASE\n" +
                                "                WHEN CurInsCurCod IS NOT NULL THEN (SELECT CURNOM FROM CURSO WHERE CURSO.CURCOD = CurInsCurCod)\n" +
                                "                WHEN CarInsPlaEstCod IS NOT NULL THEN (SELECT CONCAT(C.CARNOM, ' - ',  P.PLAESTNOM) FROM PLAN_ESTUDIO P, CARRERA C WHERE P.CARCOD = C.CARCOD AND P.PLAESTCOD = CarInsPlaEstCod)\n" +
                                "         END AS INSCRIPCION_ESTUDIO\n" +
                                "        ,E.ULTIMA_ESCOLARIDAD AS ESCOLARIDAD_FECHA\n" +
                                "FROM\n" +
                                "        INSCRIPCION I\n" +
                                "        LEFT OUTER JOIN (SELECT MAX(ESCFCH) AS ULTIMA_ESCOLARIDAD, EscAluPerCod FROM ESCOLARIDAD GROUP BY ESCALUPERCOD) E\n" +
                                "        ON I.AluPerCod = E.EscAluPerCod\n" +
                                "WHERE \n" +
                                "        I.AluFchCert IS NULL\n" +
                                "        AND E.ULTIMA_ESCOLARIDAD <= DATE_SUB(CURDATE(), INTERVAL (SELECT ParTieIna FROM PARAMETRO WHERE PARCOD = 1) MONTH)");
        
        notificacion.getLstConsulta().add(contenido);
        
        
        NotificacionConsulta destinatario = new NotificacionConsulta();
        destinatario.setNotCnsTpo(TipoConsulta.INC_DESTINATARIO);
        destinatario.setNotificacion(notificacion);
        destinatario.setNotCnsSQL("SELECT 1 AS TIPO, [%=INSCRIPCION_ALUMNO] AS DESTINATARIO");
        
        notificacion.getLstConsulta().add(destinatario);
        
        return notificacion;
    }
    
    /**
     * Armar notificacion: Restablecer password
     * @return Notificacion
     */
    private Notificacion RESTABLECER_PASSWORD(){
        Notificacion notificacion = new Notificacion();
        notificacion.setNotNom(NotificacionSistema.PASSWORD_RESTABLECER.name());
        notificacion.setNotInt(Boolean.TRUE);       
        notificacion.setNotObtDest(ObtenerDestinatario.UNICA_VEZ);
        notificacion.setNotRepTpo(TipoRepeticion.SIN_REPETICION);
        notificacion.setNotRepVal(0);
        notificacion.setNotTpo(TipoNotificacion.A_DEMANDA);

        notificacion.setNotAct(Boolean.TRUE);
        notificacion.setNotApp(Boolean.FALSE);
        notificacion.setNotEmail(Boolean.TRUE);
        notificacion.setNotWeb(Boolean.FALSE);
        
        notificacion.setNotDsc("Restablecer contraseña");
        notificacion.setNotAsu("Solicitud para restablecer contraseña");
        notificacion.setNotCon("<p>Se ha solicitado restablecer la contraseña, haga click en el siguiente enlace: [%=ENLACE_CORTO] para ingresar una nueva. Si el enlace no funciona, copie y pegue en su navegador el siguiente enlace: [%=ENLACE_LARGO]</p>");
        
        return notificacion;
    }
    
    /**
     * Armar notificacion: Cambiar contraseña
     * @return Notificacion
     */
    private Notificacion CAMBIAR_PASSWORD(){
        Notificacion notificacion = new Notificacion();
        notificacion.setNotNom(NotificacionSistema.PASSWORD_CAMBIAR.name());
        notificacion.setNotInt(Boolean.TRUE);       
        notificacion.setNotObtDest(ObtenerDestinatario.UNICA_VEZ);
        notificacion.setNotRepTpo(TipoRepeticion.SIN_REPETICION);
        notificacion.setNotRepVal(0);
        notificacion.setNotTpo(TipoNotificacion.A_DEMANDA);

        notificacion.setNotAct(Boolean.TRUE);
        notificacion.setNotApp(Boolean.FALSE);
        notificacion.setNotEmail(Boolean.TRUE);
        notificacion.setNotWeb(Boolean.FALSE);
        
        notificacion.setNotDsc("Cambió la contraseña");
        notificacion.setNotAsu("Su contraseña ha cambiado");
        notificacion.setNotCon("<p>Su contraseña ha sido modificada</p>");
        
        return notificacion;
    }
    
    /**
     * Armar notificacion: Nueva solicitud
     * @return Notificacion
     */
    private Notificacion NUEVA_SOLICITUD(){
        Notificacion notificacion = new Notificacion();
        notificacion.setNotNom(NotificacionSistema.NUEVA_SOLICITUD.name());
        notificacion.setNotInt(Boolean.TRUE);       
        notificacion.setNotObtDest(ObtenerDestinatario.UNICA_VEZ);
        notificacion.setNotRepTpo(TipoRepeticion.SIN_REPETICION);
        notificacion.setNotRepVal(0);
        notificacion.setNotTpo(TipoNotificacion.A_DEMANDA);

        notificacion.setNotAct(Boolean.TRUE);
        notificacion.setNotApp(Boolean.FALSE);
        notificacion.setNotEmail(Boolean.TRUE);
        notificacion.setNotWeb(Boolean.TRUE);
        
        notificacion.setNotDsc("Nueva solicitud");
        notificacion.setNotAsu("Nueva solicitud");
        notificacion.setNotCon("<p>Existe una nueva solicitud, ingrese al sistema para verla</p>");
        
        return notificacion;
    }
    
    /**
     * Armar notificacion: Nueva inconsistencia
     * @return Notificacion
     */
    private Notificacion NUEVA_INCONSISTENCIA(){
        Notificacion notificacion = new Notificacion();
        notificacion.setNotNom(NotificacionSistema.NUEVA_INCONSISTENCIA.name());
        notificacion.setNotInt(Boolean.TRUE);       
        notificacion.setNotObtDest(ObtenerDestinatario.UNICA_VEZ);
        notificacion.setNotRepTpo(TipoRepeticion.SIN_REPETICION);
        notificacion.setNotRepVal(0);
        notificacion.setNotTpo(TipoNotificacion.A_DEMANDA);

        notificacion.setNotAct(Boolean.TRUE);
        notificacion.setNotApp(Boolean.FALSE);
        notificacion.setNotEmail(Boolean.TRUE);
        notificacion.setNotWeb(Boolean.TRUE);
        
        notificacion.setNotDsc("Nueva Inconsistencia");
        notificacion.setNotAsu("Nueva inconsistencia");
        notificacion.setNotCon("<p>Existe una nueva inconsistencia en la sincronización, ingrese al sistema para resolverla</p>");
        
        return notificacion;
    }
   
    /**
     * Armar notificacion: Error en el sistema
     * @return Notificacion
     */
    private Notificacion ERROR_SISTEMA(){
        Notificacion notificacion = new Notificacion();
        notificacion.setNotNom(NotificacionSistema.ERROR_SISTEMA.name());
        notificacion.setNotInt(Boolean.TRUE);       
        notificacion.setNotObtDest(ObtenerDestinatario.UNICA_VEZ);
        notificacion.setNotRepTpo(TipoRepeticion.SIN_REPETICION);
        notificacion.setNotRepVal(0);
        notificacion.setNotTpo(TipoNotificacion.A_DEMANDA);

        notificacion.setNotAct(Boolean.TRUE);
        notificacion.setNotApp(Boolean.FALSE);
        notificacion.setNotEmail(Boolean.TRUE);
        notificacion.setNotWeb(Boolean.TRUE);
        
        notificacion.setNotDsc("Error de sistema");
        notificacion.setNotAsu("Error de sistema - SGA");
        notificacion.setNotCon("<p>Surgió un error en el sistema, consulte la bitácora para más información</p>");
        
        return notificacion;
    }
    
    //--------------------------------------------------------------------------
    //EJECUTAR
    //--------------------------------------------------------------------------

    /**
     * Ejecutar notificaciones internas
     */
    
    public void EjecutarNotificacionesInternas(){
        Retorno_MsgObj retorno = LoNotificacion.GetInstancia().obtenerLista();
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            for(Object objeto : retorno.getLstObjetos())
            {
                Notificacion notificacion = (Notificacion) objeto;
                
                if(notificacion.getNotAct() && notificacion.getNotInt())
                {
                    
                    if(notificacion.NotificarAutomaticamente())
                    {
                        
                        if(notificacion.getNotNom().equals(NotificacionSistema.EVALUACION_HABILITADA_INSCRIPCION.name()))
                        {
                            this.Notificar_EVALUACION_HABILITADA_INSCRIPCION(notificacion);
                        }

                        if(notificacion.getNotNom().equals(NotificacionSistema.EVALUACION_PROXIMA.toString()))
                        {
                            this.Notificar_EVALUACION_PROXIMA(notificacion);
                        }

                        if(notificacion.getNotNom().equals(NotificacionSistema.ESCOLARIDAD_ACTUALIZADA.toString()))
                        {
                            this.Notificar_ESCOLARIDAD_ACTUALIZADA(notificacion);
                        }

                        if(notificacion.getNotNom().equals(NotificacionSistema.ALUMNOS_INACTIVOS.toString()))
                        {
                            this.Notificar_ALUMNOS_INACTIVOS(notificacion);
                        }
                    }
                }
            }
        }
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Enviar notificacion: Evaluacion habilitada a inscripcion
     * @param notificacion Notificacion
     */
    private void Notificar_EVALUACION_HABILITADA_INSCRIPCION(Notificacion notificacion){
        ManejoNotificacion notManager = new ManejoNotificacion();
        
        ArrayList<NotificacionDestinatario> lstDstOriginal = new ArrayList<>();
        for(NotificacionDestinatario dst : notificacion.getLstDestinatario())
        {
            lstDstOriginal.add(dst);
        }
        
        Retorno_MsgObj lstPersona = LoPersona.GetInstancia().obtenerLista();
        
        String asunto = notificacion.getNotAsu();
        String conten = notificacion.getNotCon();
        
        Retorno_MsgObj retorno = LoCalendario.GetInstancia().ObtenerListaInscripcionHoy();
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            for(Object objeto : retorno.getLstObjetos())
            {
                Calendario calendar = (Calendario) objeto;
                
                
                if(!lstPersona.SurgioErrorListaRequerida())
                {
                    for(Object obj : lstPersona.getLstObjetos())
                    {
                        Persona persona = (Persona) obj;

                        if(persona.getPerEsAlu())
                        {
                            if(!LoPersona.GetInstancia().PersonaAproboEstudio(persona.getPerCod(), calendar.getEvaluacion().getMatEvl(), calendar.getEvaluacion().getModEvl(), calendar.getEvaluacion().getCurEvl()) || !LoPersona.GetInstancia().PersonaRevalidaMateria(persona.getPerCod(), calendar.getEvaluacion().getMatEvl()))
                            {
                                if(LoCalendario.GetInstancia().AlumnoPuedeDarExamen(persona.getPerCod(), calendar.getEvaluacion().getMatEvl(), calendar.getEvaluacion().getModEvl(), calendar.getEvaluacion().getCurEvl()))
                                {
                                    notificacion = this.ArmarDestinatario(notificacion, persona);
                                }
                            }
                        }
                    }
                }
                
                notificacion.setNotAsu(asunto);
                notificacion.setNotCon(conten);
                
                notificacion.setNotAsu(notificacion.getNotAsu().replace("[%=EVALUACION]", calendar.getEvaluacion().getEvlNom()));
                notificacion.setNotAsu(notificacion.getNotAsu().replace("[%=ESTUDIO]", calendar.getEvaluacion().getEstudioNombre()));
                
                notificacion.setNotCon(notificacion.getNotCon().replace("[%=EVALUACION]", calendar.getEvaluacion().getEvlNom()));
                notificacion.setNotCon(notificacion.getNotCon().replace("[%=ESTUDIO]", calendar.getEvaluacion().getEstudioNombre()));
                notificacion.setNotCon(notificacion.getNotCon().replace("[%=DESDE]", calendar.getEvlInsFchDsd().toString()));
                notificacion.setNotCon(notificacion.getNotCon().replace("[%=HASTA]", calendar.getEvlInsFchHst().toString()));
                
                
                notManager.EjecutarNotificacion(notificacion);
            }
        }
        
        notificacion.setNotAsu(asunto);
        notificacion.setNotCon(conten);

        notificacion = (Notificacion) LoNotificacion.GetInstancia().obtener(notificacion.getNotCod()).getObjeto();
        notificacion.setLstDestinatario(lstDstOriginal);
        LoNotificacion.GetInstancia().actualizar(notificacion);
        
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Enviar notificacion: Evaluacion proxima
     * @param notificacion  Notificacion
     */
    private void Notificar_EVALUACION_PROXIMA(Notificacion notificacion){
        ManejoNotificacion notManager = new ManejoNotificacion();
        notManager.EjecutarNotificacion(notificacion);
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Enviar notificación: Escolaridad actualizada
     * @param notificacion Notificacion
     */
    private void Notificar_ESCOLARIDAD_ACTUALIZADA(Notificacion notificacion){
        ManejoNotificacion notManager = new ManejoNotificacion();
        notManager.EjecutarNotificacion(notificacion);
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Enviar notificacion: Alumnos inactivos
     * @param notificacion  Notificacion
     */
    private void Notificar_ALUMNOS_INACTIVOS(Notificacion notificacion){
        ManejoNotificacion notManager = new ManejoNotificacion();
        notManager.EjecutarNotificacion(notificacion);
    }
    
    //--------------------------------------------------------------------------

    /**
     * Enviar notificacion: Cambiar contraseña
     * @param persona Persona
     */

    public void Notificar_CAMBIAR_PASSWORD(Persona persona){
        
        Retorno_MsgObj retorno = LoNotificacion.GetInstancia().obtenerByNom(NotificacionSistema.PASSWORD_CAMBIAR.name());
        
        Notificacion not = (Notificacion) retorno.getObjeto();
        
        not = this.ArmarDestinatario(not, persona);
        
        AsyncNotificar xthread = null;
        try {
          xthread = new AsyncNotificar(not, TipoNotificacion.A_DEMANDA);
          xthread.start();
        } catch (Exception ex) {

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, "[InterfacesAgent] Error" + ex);
        } finally {
          if (xthread != null && xthread.isAlive()) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, "[InterfacesAgent] Interrupting" );
            xthread.interrupt();
          }
        }
        
    }
    
    //--------------------------------------------------------------------------

    /**
     * Enviar notificacion: Restablecer contrsaeña
     * @param persona Persona
     * @param enlaceCorto Enlace corto
     * @param enlaceLargo Enlace largo
     */

    public void Notificar_RESTABLECER_PASSWORD(Persona persona, String enlaceCorto, String enlaceLargo){
        Retorno_MsgObj retorno = LoNotificacion.GetInstancia().obtenerByNom(NotificacionSistema.PASSWORD_RESTABLECER.name());
        
        Notificacion not = (Notificacion) retorno.getObjeto();
        
        not = this.ArmarDestinatario(not, persona);
        
        not.setNotCon(not.getNotCon().replace("[%=ENLACE_CORTO]", enlaceCorto));
        not.setNotCon(not.getNotCon().replace("[%=ENLACE_LARGO]", enlaceLargo));
        
        AsyncNotificar xthread = null;
        try {
          xthread = new AsyncNotificar(not, TipoNotificacion.A_DEMANDA);
          xthread.start();
        } catch (Exception ex) {

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, "[InterfacesAgent] Error" + ex);
        } finally {
          if (xthread != null && xthread.isAlive()) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, "[InterfacesAgent] Interrupting" );
            xthread.interrupt();
          }
        }
    }
    
    //--------------------------------------------------------------------------

    /**
     * Enviar notificacion: Nueva solicitud
     */
    
    public void Notificar_NUEVA_SOLICITUD(){
        
        
        Notificacion not = this.NUEVA_SOLICITUD();
        
        Retorno_MsgObj retorno = LoPersona.GetInstancia().obtenerLista();
        
        for(Object objeto : retorno.getLstObjetos())
        {
            Persona persona = (Persona) objeto;
            if(persona.getPerEsAdm())
            {
                not.getLstDestinatario().add(new NotificacionDestinatario(persona));
            }
            
        }
        
        AsyncNotificar xthread = null;
        try {
          xthread = new AsyncNotificar(not, TipoNotificacion.A_DEMANDA);
          xthread.start();
        } catch (Exception ex) {

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, "[InterfacesAgent] Error" + ex);
        } finally {
          if (xthread != null && xthread.isAlive()) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, "[InterfacesAgent] Interrupting" );
            xthread.interrupt();
          }
        }
        
    }
    
    //--------------------------------------------------------------------------

    /**
     * Enviar notificacion: Nueva inconsistencia
     */

    public void Notificar_NUEVA_INCONSISTENCIA(){
        
        
        Notificacion not = this.NUEVA_INCONSISTENCIA();
        
        Retorno_MsgObj retorno = LoPersona.GetInstancia().obtenerLista();
        
        for(Object objeto : retorno.getLstObjetos())
        {
            Persona persona = (Persona) objeto;
            if(persona.getPerEsAdm())
            {
                not.getLstDestinatario().add(new NotificacionDestinatario(persona));
            }
            
        }
        
        AsyncNotificar xthread = null;
        try {
          xthread = new AsyncNotificar(not, TipoNotificacion.A_DEMANDA);
          xthread.start();
        } catch (Exception ex) {

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, "[InterfacesAgent] Error" + ex);
        } finally {
          if (xthread != null && xthread.isAlive()) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, "[InterfacesAgent] Interrupting" );
            xthread.interrupt();
          }
        }
        
    }
    
    //--------------------------------------------------------------------------
    
    /**
     * Enviar notificacion: Error de sistema
     * @param mensaje Mensaje
     */
    public void Notificar_ErrorSistema(String mensaje){
        
        
        Notificacion not = this.ERROR_SISTEMA();
        
        not.setNotCon(not.getNotCon() + "<p>"+ mensaje + "</p>");
        
        Retorno_MsgObj retorno = LoPersona.GetInstancia().obtenerLista();
        
        for(Object objeto : retorno.getLstObjetos())
        {
            Persona persona = (Persona) objeto;
            if(persona.getPerEsAdm())
            {
                not.getLstDestinatario().add(new NotificacionDestinatario(persona));
            }
            
        }
        
        AsyncNotificar xthread = null;
        try {
          xthread = new AsyncNotificar(not, TipoNotificacion.A_DEMANDA);
          xthread.start();
        } catch (Exception ex) {

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, "[InterfacesAgent] Error" + ex);
        } finally {
          if (xthread != null && xthread.isAlive()) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, "[InterfacesAgent] Interrupting" );
            xthread.interrupt();
          }
        }
        
    }
    
    //--------------------------------------------------------------------------
    
    
    /**
     * Armar destinatario
     * @param notificacion Notificacion
     * @param persona Persona
     * @return Notificacion
     */
    private Notificacion ArmarDestinatario(Notificacion notificacion, Persona persona){
        NotificacionDestinatario dest = new NotificacionDestinatario();
        
        dest.setPersona(persona);
        dest.setNotificacion(notificacion);
        
        notificacion.getLstDestinatario().add(dest);
        
        return notificacion;
    }
}
