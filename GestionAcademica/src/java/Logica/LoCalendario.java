/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Entidad.Calendario;
import Entidad.CalendarioAlumno;
import Entidad.CalendarioDocente;
import Entidad.Curso;
import Entidad.Evaluacion;
import Entidad.Inscripcion;
import Entidad.Materia;
import Entidad.MateriaPrevia;
import Entidad.Modulo;
import Entidad.PeriodoEstudio;
import Entidad.PeriodoEstudioAlumno;
import Entidad.PeriodoEstudioDocente;
import Entidad.Persona;
import Enumerado.EstadoCalendarioEvaluacion;
import Enumerado.TipoMensaje;
import Interfaz.InABMGenerico;
import Persistencia.PerManejador;
import SDT.SDT_Evento;
import SDT.SDT_Parameters;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alvar
 */
public class LoCalendario implements InABMGenerico{

    private static LoCalendario instancia;

    private LoCalendario() {
    }
    
    /**
     * Obtener instancia de la clase
     * @return Instancia de la clase
     */
    public static LoCalendario GetInstancia(){
        if (instancia==null)
        {
            instancia   = new LoCalendario();
        }

        return instancia;
    }
    
    /**
     * Guardar calendario 
     * @param pObjeto Calendario
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object guardar(Object pObjeto) {
        Calendario calendario = (Calendario) pObjeto;
        
        calendario.setObjFchMod(new Date());
        
        PerManejador perManejador   = new PerManejador();
        Retorno_MsgObj retorno      = perManejador.guardar(calendario);

        if(!retorno.SurgioError())
        {
            calendario.setCalCod((Long) retorno.getObjeto());
            retorno.setObjeto(calendario);
        }
        
        return retorno;
    }

    /**
     * Actualizar calendario
     * @param pObjeto Calendario
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object actualizar(Object pObjeto) {
        Calendario calendario = (Calendario) pObjeto;
        
        calendario.setObjFchMod(new Date());
        
        PerManejador perManejador   = new PerManejador();
        
        return perManejador.actualizar(calendario);
    }

    /**
     * Eliminar calendario
     * @param pObjeto Calendario 
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Object eliminar(Object pObjeto) {
        PerManejador perManejador   = new PerManejador();
        return perManejador.eliminar(pObjeto);
    }

    /**
     * Obtener calendario
     * @param pObjeto Long - CalCod
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtener(Object pObjeto) {
        PerManejador perManejador   = new PerManejador();
        return perManejador.obtener((Long) pObjeto, Calendario.class);
    }

    /**
     * Obtener lista de calendarios 
     * @return  Resultado: RETORNO_MSGOBJ
     */
    @Override
    public Retorno_MsgObj obtenerLista() {
        PerManejador perManejador   = new PerManejador();
        
        return perManejador.obtenerLista("Calendario.findAll", null);
    }
    
    /**
     * Guardar lista de calendarios. 
     * @param lstCalendario Lista de calendarios
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj guardarLista(List<Object> lstCalendario){
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
        Retorno_MsgObj retornoObj = new Retorno_MsgObj();
        
        for(Object objeto : lstCalendario)
        {
            Calendario calendario = (Calendario) objeto;
            
            if(calendario.getCalFch() != null )
            {
                try {
                    String fecha = sdf.format(new Date(calendario.getCalFch().getTime() + TimeUnit.DAYS.toMillis(1)));
                    calendario.setCalFch(sdf.parse(fecha));
                }catch (ParseException ex) {
                        Logger.getLogger(LoCalendario.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            
            if(calendario.getEvlInsFchDsd() != null )
            {
                try {
                    String fecha = sdf.format(new Date(calendario.getEvlInsFchDsd().getTime() + TimeUnit.DAYS.toMillis(1)));
                    calendario.setEvlInsFchDsd(sdf.parse(fecha));
                }catch (ParseException ex) {
                        Logger.getLogger(LoCalendario.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            
            if(calendario.getEvlInsFchHst() != null )
            {
                try {
                    String fecha = sdf.format(new Date(calendario.getEvlInsFchHst().getTime() + TimeUnit.DAYS.toMillis(1)));
                    calendario.setEvlInsFchHst(sdf.parse(fecha));
                }catch (ParseException ex) {
                        Logger.getLogger(LoCalendario.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            if(calendario.getEvaluacion().getEvlCod() !=null) calendario.setEvaluacion((Evaluacion) LoEvaluacion.GetInstancia().obtener(calendario.getEvaluacion().getEvlCod()).getObjeto());

            retornoObj = (Retorno_MsgObj) this.guardar(calendario);
            
            if(retornoObj.SurgioError())
            {
                int indice = lstCalendario.indexOf(objeto);
                EliminarCalendariosHasta(lstCalendario, indice);
                break;
            }

        }
        
        return retornoObj;
    }
    
    /**
     * Elimina calendarios hasta cierto indice
     * @param lstCalendario Lista de calendarios
     * @param indice Indice hasta el cual se eliminan los calendarios.
     */
    private void EliminarCalendariosHasta(List<Object> lstCalendario, int indice){
        for(Object objeto : lstCalendario)
        {
            if(lstCalendario.indexOf(objeto) < indice)
            {
                this.eliminar(objeto);
            }
            else
            {
                return;
            }
        }
        
    }
    
    /**
     * Retorna lista de calendarios donde la persona recibida sea 
     * alumno, y su calificación haya sido validadada.
     * @param PerCod Código de persona
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ObtenerListaPorAlumno(Long PerCod){
         
        PerManejador perManager = new PerManejador();

        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(PerCod, "PerCod"));

        Retorno_MsgObj retorno = perManager.obtenerLista("Calendario.findByAlumno", lstParametros);
        
        List<Object> lstRetorno = new ArrayList<>();
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            for(Object objeto : retorno.getLstObjetos())
            {
                Calendario calendar = (Calendario) objeto;
                
                if(calendar.getAlumnoByPersona(PerCod).getEvlCalEst().equals(EstadoCalendarioEvaluacion.VALIDADO))
                {
                    lstRetorno.add(objeto);
                }
            }
        }
       
        retorno.setLstObjetos(lstRetorno);
        return retorno;
    }
    
    /**
     * Retorna una lista de calendario, donde la persona que se recibio
     * pueda inscribirse.
     * <p>El calendario debe cumplir con las siguientes condiciones:</p>
     * <p>Evaluación de tipo examen</p>
     * <p>Fecha de evaluación debe ser superior a fecha actual.</p>
     * <p>Persona NO debe haber aprobado el estudio </p>
     * <p>Persona puede dar el examen, ya sea por que tiene nota de curso suficiente, o la materia es sin ganancia.</p>
     * 
     * @param PerCod Código de persona
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ObtenerListaParaInscripcion(Long PerCod){
        Retorno_MsgObj retorno  = this.obtenerLista();
        List<Object> lstRetorno = new ArrayList<>();
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            for(Object objeto : retorno.getLstObjetos())
            {
                Calendario calendar = (Calendario) objeto;
                if(calendar.getEvaluacion().getTpoEvl().getTpoEvlExm() && calendar.getCalFch().after(new Date())) 
                {
                    if(!LoPersona.GetInstancia().PersonaAproboEstudio(PerCod, calendar.getEvaluacion().getMatEvl(), calendar.getEvaluacion().getModEvl(), calendar.getEvaluacion().getCurEvl()) && !LoPersona.GetInstancia().PersonaRevalidaMateria(PerCod, calendar.getEvaluacion().getMatEvl()))
                    {
                        if(AlumnoPuedeDarExamen(PerCod, calendar.getEvaluacion().getMatEvl(), calendar.getEvaluacion().getModEvl(), calendar.getEvaluacion().getCurEvl()))
                        {
                            lstRetorno.add(objeto);
                        }
                    }
                    
                }
            }
        }
        
        retorno.setLstObjetos(lstRetorno);
        
        return retorno;
    }
    
    /**
     * Retorna todas los calendarios, cuya fecha de inscripción incie hoy
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ObtenerListaInscripcionHoy(){
        PerManejador perManejador   = new PerManejador();
        
        return perManejador.obtenerLista("Calendario.findInscripcion", null);
    }
    
    /**
     * Obtiene lista de calendarios donde una persona este
     * inscrita, y su calificación haya sido validada.  
     * @param PerCod
     * @return
     */
    public Retorno_MsgObj ObtenerListaPendiente(Long PerCod){
        PerManejador perManager = new PerManejador();

        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(PerCod, "PerCod"));

        Retorno_MsgObj retorno = perManager.obtenerLista("Calendario.findByAlumno", lstParametros);
        
        List<Object> lstRetorno = new ArrayList<>();
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            for(Object objeto : retorno.getLstObjetos())
            {
                Calendario calendar = (Calendario) objeto;
                if(!calendar.getAlumnoByPersona(PerCod).getEvlCalEst().equals(EstadoCalendarioEvaluacion.VALIDADO))
                {
                    lstRetorno.add(objeto);
                }
            }
        }
        
        retorno.setLstObjetos(lstRetorno);
        
        return retorno;
    }
    
    /**
     * Retorna una colección de eventos, que el calendario pueda interpretar.
     * @return Lista de eventos
     */
    public ArrayList<SDT_Evento> ObtenerEventoTodosCalendario(){
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<SDT_Evento> lstEvento = new ArrayList<>();
        Retorno_MsgObj retorno = this.obtenerLista();
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            for(Object objeto : retorno.getLstObjetos())
            {
                SDT_Evento evento   = new SDT_Evento();
                Calendario calendar = (Calendario) objeto;
                
                evento.setId(calendar.getCalCod());
                evento.setAllDay(Boolean.TRUE);
                evento.setDurationEditable(Boolean.FALSE);
                evento.setEditable(Boolean.FALSE);
                evento.setStart(calendar.getCalFch());
                evento.setResourceEditable(Boolean.FALSE);
                evento.setStartEditable(Boolean.FALSE);
                evento.setTitle(calendar.getEvaluacion().getEvlNom() + " - " + calendar.getEvaluacion().getEstudioNombre());
                evento.setDescription(calendar.getEvaluacion().getCarreraCursoNombre() + " - " + calendar.getEvaluacion().getEstudioNombre() + " " + calendar.getEvaluacion().getEvlNom());
                
                lstEvento.add(evento);
                
                //Si es una evaluacion que requiere un plazo, agrego como evento el periodo de inscripcion
                if(!calendar.getEvaluacion().getTpoEvl().getTpoEvlInsAut())
                {
                    SDT_Evento evPeriodo   = new SDT_Evento();
                    
                    String fechaHasta = sdf.format(new Date(calendar.getEvlInsFchHst().getTime() + TimeUnit.DAYS.toMillis(1)));
                    
                    evPeriodo.setId(calendar.getCalCod());
                    evPeriodo.setAllDay(Boolean.TRUE);
                    evPeriodo.setDurationEditable(Boolean.FALSE);
                    evPeriodo.setEditable(Boolean.FALSE);
                    evPeriodo.setStart(calendar.getEvlInsFchDsd());
                    try {
                        evPeriodo.setEnd(sdf.parse(fechaHasta));
                    } catch (ParseException ex) {
                        Logger.getLogger(LoCalendario.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    evPeriodo.setResourceEditable(Boolean.FALSE);
                    evPeriodo.setStartEditable(Boolean.FALSE);
                    evPeriodo.setTitle("Inscripción: " + calendar.getEvaluacion().getEvlNom() + " - " + calendar.getEvaluacion().getEstudioNombre());
                    evPeriodo.setDescription("Inscripción: " + calendar.getEvaluacion().getCarreraCursoNombre() + " - " + calendar.getEvaluacion().getEstudioNombre() + " " + calendar.getEvaluacion().getEvlNom());

                    lstEvento.add(evPeriodo); 
                }
                
            }
        }
        
        return lstEvento;
    }

    /**
     * Retorna lista de calendarios para una determinada materia y persona
     * @param PerCod Código de persona
     * @param materia Código de materia
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj obtenerByMateriaPersona(Long PerCod, Materia materia){
        PerManejador perManager = new PerManejador();
        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(PerCod, "PerCod"));
        lstParametros.add(new SDT_Parameters(materia.getMatCod(), "MatCod"));

        return perManager.obtenerLista("Calendario.findMateriaAlumno", lstParametros);
    }

    /**
     * Retorna lista de calendarios para un determinado modulo y persona
     * @param PerCod Código de persona
     * @param modulo Código de modulo
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj obtenerByModuloPersona(Long PerCod, Modulo modulo){
        PerManejador perManager = new PerManejador();
        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(PerCod, "PerCod"));
        lstParametros.add(new SDT_Parameters(modulo.getModCod(), "ModCod"));

        return perManager.obtenerLista("Calendario.findModuloAlumno", lstParametros);
    }
    
    /**
     * Retorna lista de calendarios para un determinado curso y persona
     * @param PerCod Código de persona
     * @param curso Curso
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj obtenerByCursoPersona(Long PerCod, Curso curso){
        PerManejador perManager = new PerManejador();
        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(PerCod, "PerCod"));
        lstParametros.add(new SDT_Parameters(curso.getCurCod(), "CurCod"));

        return perManager.obtenerLista("Calendario.findCursoAlumno", lstParametros);
    }
    
    //------------------------------------------------------------------------------------
    //-MANEJO DE ALUMNOS
    //------------------------------------------------------------------------------------

    /**
     * Inscribe un alumno a un calendario
     * @param alumno Alumno 
     * @return  Resultado: RETORNO_MSGOBJ
     */
    
    public Object AlumnoAgregar(CalendarioAlumno alumno){
        boolean error           = false;
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al agregar",TipoMensaje.ERROR), alumno);
        
        if(alumno.getCalendario().getEvaluacion().getMatEvl() != null)
        {
            Inscripcion inscripcion = (Inscripcion) LoInscripcion.GetInstancia().obtenerInscByPlan(alumno.getAlumno().getPerCod(), alumno.getCalendario().getEvaluacion().getMatEvl().getPlan().getPlaEstCod()).getObjeto();
            
            if(inscripcion != null)
            {
                if(inscripcion.MateriaRevalidada(alumno.getCalendario().getEvaluacion().getMatEvl().getMatCod()))
                {
                    error = true;
                    retorno.setMensaje(new Mensajes("El alumno revalida la materia", TipoMensaje.ERROR));
                }
            }
        }
        
        if(alumno.getCalendario().getEvaluacion().getTpoEvl().getTpoEvlExm() && !error)
        {
            Long perCod = alumno.getAlumno().getPerCod();
            Materia mat = alumno.getCalendario().getEvaluacion().getMatEvl();
            Modulo mod = alumno.getCalendario().getEvaluacion().getModEvl();
            Curso cur = alumno.getCalendario().getEvaluacion().getCurEvl();
            if(LoPersona.GetInstancia().PersonaAproboEstudio(perCod, mat, mod, cur) || LoPersona.GetInstancia().PersonaRevalidaMateria(perCod, mat))
            {
                retorno.setMensaje(new Mensajes("El alumno ya aprobó o revalida el estudio", TipoMensaje.ERROR));
                error = true;                
            }
            else
            {
                if(!AlumnoAproboPrevias(perCod, mat))
                {
                    retorno.setMensaje(new Mensajes("El alumno no puede dar examen, no aprobo las materias previas", TipoMensaje.ERROR));
                    error = true;
                }
                
               if(!AlumnoPuedeDarExamen(perCod, mat, mod, cur) && !error)
                {
                    retorno.setMensaje(new Mensajes("El alumno no puede dar examen, no posee los creditos suficientes", TipoMensaje.ERROR));
                    error = true;
                }
                
            }
        }
        
        if(!error)
        {
            alumno.setObjFchMod(new Date());
            alumno.setEvlCalEst(EstadoCalendarioEvaluacion.SIN_CALIFICAR);

            PerManejador perManejador   = new PerManejador();
            retorno = (Retorno_MsgObj) perManejador.guardar(alumno);
            
            if(!retorno.SurgioError())
            {
                alumno.setCalAlCod((Long) retorno.getObjeto());
                retorno.setObjeto(alumno);
            }
        }
       
        
        return retorno;
    }
    
    /**
     * Actualiza un alumno en un calendario
     * @param alumno Alumno
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Object AlumnoActualizar(CalendarioAlumno alumno){
        
        alumno.setObjFchMod(new Date());
        PerManejador perManejador   = new PerManejador();
        return perManejador.actualizar(alumno);
        
    }
    
    /**
     * Elimina un alumno de un calendario
     * @param alumno Alumno
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Object AlumnoEliminar(CalendarioAlumno alumno){
        PerManejador perManejador   = new PerManejador();
        return perManejador.eliminar(alumno);
    }
    
    /**
     * Obtiene una lista de alumnos para un calendario, a partir de un usuario
     * @param calendario Calendario
     * @param usuario Usuario que desea ver los alumnos
     * @return Lista de alumnos
     */
    public List<CalendarioAlumno> AlumnoObtenerListaPorUsuario(Calendario calendario, String usuario){
        
        List<CalendarioAlumno> retorno = new ArrayList<>();
        
        Persona persona = new Persona();
        if(usuario != null) persona = (Persona) LoPersona.GetInstancia().obtenerByMdlUsr(usuario).getObjeto();

        if(persona != null)
        {
            if(persona.getPerEsAdm())
            {
                retorno = calendario.getLstAlumnos();
            }
            else
            {
                if(persona.getPerEsDoc())
                {
                    if(calendario.getDocenteByPersona(persona.getPerCod()).getCalDocCod() != null)
                    {
                        for(CalendarioAlumno alumno : calendario.getLstAlumnos())
                        {
                            if(alumno.getEvlCalEst().equals(EstadoCalendarioEvaluacion.CALIFICADO) || alumno.getEvlCalEst().equals(EstadoCalendarioEvaluacion.PENDIENTE_CORRECCION) || alumno.getEvlCalEst().equals(EstadoCalendarioEvaluacion.SIN_CALIFICAR))
                            {
                                retorno.add(alumno);
                            }
                        }
                    }
                }
            }
        }
        return retorno;
    }
    
    /**
     * Agrega alumnos a un calendario, a partir de un periodo
     * @param CalCod Calendario
     * @param PeriEstCod Periodo de estudio
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj AlumnoAgregarPorPeriodo(Long CalCod, Long PeriEstCod){
        
        int agregados   = 0;
        int noAgregados = 0;

        Calendario calendario  = (Calendario) this.obtener(CalCod).getObjeto();
        PeriodoEstudio periEst = (PeriodoEstudio) LoPeriodo.GetInstancia().EstudioObtener(PeriEstCod).getObjeto();
        
        for(PeriodoEstudioAlumno alumno : periEst.getLstAlumno())
        {
            boolean agregar = true;
            
            if(calendario.getEvaluacion().getMatEvl() != null)
            {
                if(calendario.getEvaluacion().getMatEvl().getLstPrevias() != null)
                {
                    for(MateriaPrevia matPrevia : calendario.getEvaluacion().getMatEvl().getLstPrevias())
                    {
                        agregar = LoPersona.GetInstancia().PersonaAproboEstudio(alumno.getAlumno().getPerCod(), matPrevia.getMateriaPrevia(), null, null);
                        if(!agregar) break;
                    }
                }
            }
            
            if(agregar)
            {
                boolean cargar = true;
                
                if(alumno.getPeriodoEstudio().getMateria() != null)
                {
                    Inscripcion inscripcion = (Inscripcion) LoInscripcion.GetInstancia().obtenerInscByPlan(alumno.getAlumno().getPerCod(), alumno.getPeriodoEstudio().getMateria().getPlan().getPlaEstCod()).getObjeto();

                    if(inscripcion != null)
                    {
                        if(inscripcion.MateriaRevalidada(alumno.getPeriodoEstudio().getMateria().getMatCod()))
                        {
                            cargar = false;
                        }
                    }
                }
                
                if(cargar)
                {
                    CalendarioAlumno calAlumno = new CalendarioAlumno();
                    calAlumno.setAlumno(alumno.getAlumno());
                    calAlumno.setCalendario(calendario);
                    calAlumno.setEvlCalEst(EstadoCalendarioEvaluacion.SIN_CALIFICAR);

                    Retorno_MsgObj resultado = (Retorno_MsgObj) this.AlumnoAgregar(calAlumno);

                    if(resultado.SurgioError())
                    {
                        noAgregados += 1;
                    }
                    else
                    {
                        agregados += 1;
                    }
                }
                else
                {
                    noAgregados += 1;
                }
                
            }
        }
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Alumnos agregados: " + agregados + ". Alumnos no agregados: " + noAgregados, (noAgregados > 0 ? TipoMensaje.ADVERTENCIA : TipoMensaje.MENSAJE)));
        
        return retorno;
    }
    
    /**
     * Determina si un alumno aprobo una materia
     * @param PerCod Código de persona
     * @param materia Materia
     * @return Aprobado
     */
    public Boolean AlumnoAproboPrevias(Long PerCod, Materia materia){
        if(materia != null)
        {
            if(materia.getLstPrevias() != null)
            {
                for(MateriaPrevia matPrevia : materia.getLstPrevias())
                {
                    boolean agregar = LoPersona.GetInstancia().PersonaAproboEstudio(PerCod, matPrevia.getMateriaPrevia(), null, null);
                    if(!agregar) return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Determina en base al estudio y la persona, si el alumno puede dar el examen
     * @param PerCod Código de persona
     * @param materia Materia
     * @param modulo Modulo
     * @param curso Curso
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Boolean AlumnoPuedeDarExamen(Long PerCod, Materia materia, Modulo modulo, Curso curso){
        
        if(materia != null)
        {
            Retorno_MsgObj retorno = this.obtenerByMateriaPersona(PerCod, materia);
            
            if(!retorno.SurgioErrorListaRequerida())
            {
                Double creditosParciales    = 0.0;

                for(Object objeto : retorno.getLstObjetos())
                {
                    Calendario calendar = (Calendario) objeto;

                    if(!calendar.getEvaluacion().getTpoEvl().getTpoEvlExm() && calendar.getAlumnoByPersona(PerCod).getEvlCalEst().equals(EstadoCalendarioEvaluacion.VALIDADO))
                    {
                        creditosParciales += calendar.getAlumnoCalificacion(PerCod);
                    }

                }

                if(materia.MateriaPuedeDarExamen(creditosParciales)) return true;

            }
        }
        
        if(modulo != null)
        {
            Retorno_MsgObj aux = LoInscripcion.GetInstancia().obtenerInscByCurso(PerCod, modulo.getCurso().getCurCod());
            return !aux.SurgioErrorObjetoRequerido();
        }
        
        if(curso != null)
        {
            Retorno_MsgObj aux = LoInscripcion.GetInstancia().obtenerInscByCurso(PerCod, curso.getCurCod());
            return !aux.SurgioErrorObjetoRequerido();
        }
        
        return false;
    }
    
    /**
     * Determina si el alumno exonera el estudio
     * @param PerCod Código de persona
     * @param materia Materia
     * @param modulo Modulo
     * @param curso Curso
     * @return Exonera
     */
    public Boolean AlumnoExonera(Long PerCod, Materia materia, Modulo modulo, Curso curso){
        
        
        if(materia != null)
        {
            
            Retorno_MsgObj retorno = this.obtenerByMateriaPersona(PerCod, materia);
            
            if(!retorno.SurgioErrorListaRequerida())
            {
                Double creditosParciales    = 0.0;

                for(Object objeto : retorno.getLstObjetos())
                {
                    Calendario calendar = (Calendario) objeto;

                    if(!calendar.getEvaluacion().getTpoEvl().getTpoEvlExm() && calendar.getAlumnoByPersona(PerCod).getEvlCalEst().equals(EstadoCalendarioEvaluacion.VALIDADO))
                    {
                        creditosParciales += calendar.getAlumnoCalificacion(PerCod);
                    }

                }

                if(materia.MateriaExonera(creditosParciales)) return true;

            }
        }
        
        if(modulo != null)
        {
            return false;
        }
        
        if(curso != null)
        {
            return false;
        }
        
        return false;
    }
    
    /**
     * Obtener creditos parciales del alumno, suma de todas las evaluaciones
     * de un estudio que no sean examen.
     * @param PerCod Código de persona
     * @param materia Materia
     * @param modulo Modulo
     * @param curso Curso
     * @return Calificación
     */
    public Double AlumnoCreditoParcial(Long PerCod, Materia materia, Modulo modulo, Curso curso){
        Retorno_MsgObj retorno      = new Retorno_MsgObj();
        Double creditosParciales    = 0.0;
         
        if(materia != null)
        {
            retorno = this.obtenerByMateriaPersona(PerCod, materia);
        }
        if(modulo != null)
        {
            retorno = this.obtenerByModuloPersona(PerCod, modulo);
        }
        if(curso != null)
        {
            retorno = this.obtenerByCursoPersona(PerCod, curso);
        }
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            for(Object objeto : retorno.getLstObjetos())
            {
                Calendario calendar = (Calendario) objeto;

                if(!calendar.getEvaluacion().getTpoEvl().getTpoEvlExm() && calendar.getAlumnoByPersona(PerCod).getEvlCalEst().equals(EstadoCalendarioEvaluacion.VALIDADO))
                {
                    creditosParciales += calendar.getAlumnoCalificacion(PerCod);
                }

            }
        }
        
        return creditosParciales;
    }

    /**
     * Retorna si un alumno se encuentra en un estudio
     * @param PerCod Código de persona
     * @param materia Materia
     * @param modulo modulo
     * @param curso Curso 
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Boolean AlumnoCursoEstudio(Long PerCod, Materia materia, Modulo modulo, Curso curso){
        Retorno_MsgObj retorno      = new Retorno_MsgObj();
         
        if(materia != null)
        {
            retorno = this.obtenerByMateriaPersona(PerCod, materia);
        }
        if(modulo != null)
        {
            retorno = this.obtenerByModuloPersona(PerCod, modulo);
        }
        if(curso != null)
        {
            retorno = this.obtenerByCursoPersona(PerCod, curso);
        }
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            return retorno.getLstObjetos().size() > 0;
        }
        
        return false;
    }    
    
    //------------------------------------------------------------------------------------
    //-MANEJO DE DOCENTES
    //------------------------------------------------------------------------------------

    /**
     * Agregar docente al calendario
     * @param docente Docente
     * @return  Resultado: RETORNO_MSGOBJ
     */
    
    public Object DocenteAgregar(CalendarioDocente docente){
        docente.setObjFchMod(new Date());
        
        PerManejador perManejador   = new PerManejador();
        return  perManejador.guardar(docente);
    }
    
    /**
     * Actualiza el docente a un calendario
     * @param docente Docente
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Object DocenteActualizar(CalendarioDocente docente){
        
        docente.setObjFchMod(new Date());
        PerManejador perManejador   = new PerManejador();
        return  perManejador.actualizar(docente);
    }
    
    /**
     * Elimina un docente de un calendario
     * @param docente Docente
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Object DocenteEliminar(CalendarioDocente docente){
        PerManejador perManejador   = new PerManejador();
        return  perManejador.eliminar(docente);
    }
    
    /**
     * Agrega docentes a un calendario por período
     * @param CalCod Código de calendario
     * @param PeriEstCod Código del periodo de estudio
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj DocenteAgregarPorPeriodo(Long CalCod, Long PeriEstCod){
        
        int agregados   = 0;
        int noAgregados = 0;

        Calendario calendario  = (Calendario) this.obtener(CalCod).getObjeto();
        PeriodoEstudio periEst = (PeriodoEstudio) LoPeriodo.GetInstancia().EstudioObtener(PeriEstCod).getObjeto();
        
        for(PeriodoEstudioDocente docente : periEst.getLstDocente())
        {
            CalendarioDocente calDocente = new CalendarioDocente();
            calDocente.setDocente(docente.getDocente());
            calDocente.setCalendario(calendario);
                    
            Retorno_MsgObj resultado = (Retorno_MsgObj) this.DocenteAgregar(calDocente);

            if(resultado.SurgioError())
            {
                noAgregados += 1;
            }
            else
            {
                agregados += 1;
            }
                
            
        }
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Docentes agregados: " + agregados + ". Docentes no agregados: " + noAgregados, (noAgregados > 0 ? TipoMensaje.ADVERTENCIA : TipoMensaje.MENSAJE)));
        
        return retorno;
    }
    
    /**
     * Retorna lista de calendarios para un docente
     * @param PerCod Código de docente
     * @return  Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ObtenerEvaluacionesDocentes(Long PerCod)
    {
        PerManejador perManager = new PerManejador();

        ArrayList<SDT_Parameters> lstParametros = new ArrayList<>();
        lstParametros.add(new SDT_Parameters(PerCod, "PerCod"));

        Retorno_MsgObj retorno = perManager.obtenerLista("Calendario.findByDocente", lstParametros);
        
        List<Object> lstRetorno = new ArrayList<>();
        
        if(!retorno.SurgioErrorListaRequerida())
        {
            for(Object objeto : retorno.getLstObjetos())
            {
                lstRetorno.add(objeto);
            }
        }
        
        retorno.setLstObjetos(lstRetorno);
        
        return retorno;
    }
}
