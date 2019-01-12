/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

import Entidad.*;

/**
 * Enumerado Objetos
 *
 * @author Alvaro
 */
public enum Objetos {
    TIPO_EVALUACION(TipoEvaluacion.class.getSimpleName(), "TpoEvlCod", TipoEvaluacion.class.getName()),
    
    PERSONA(Persona.class.getSimpleName(), "PerCod", Persona.class.getName()),
    
    CARRERA(Carrera.class.getSimpleName(), "CarCod", Carrera.class.getName()),
    PLAN_ESTUDIO(PlanEstudio.class.getSimpleName(), "PlaEstCod", PlanEstudio.class.getName()),
    MATERIA(Materia.class.getSimpleName(), "MatCod", Materia.class.getName()),
    MATERIA_PREVIA(MateriaPrevia.class.getSimpleName(), "MatPreCod", MateriaPrevia.class.getName()),
    
    CURSO(Curso.class.getSimpleName(), "CurCod", Curso.class.getName()),
    MODULO(Modulo.class.getSimpleName(), "ModCod", Modulo.class.getName()),
    
    EVALUACION(Evaluacion.class.getSimpleName(), "EvlCod", Evaluacion.class.getName()),
    
    PERIODO(Periodo.class.getSimpleName(), "PeriCod", Periodo.class.getName()),
    PERIODO_ESTUDIO(PeriodoEstudio.class.getSimpleName(), "PeriEstCod", PeriodoEstudio.class.getName()),
    PERIODO_ESTUDIO_ALUMNO(PeriodoEstudioAlumno.class.getSimpleName(), "PeriEstAluCod", PeriodoEstudioAlumno.class.getName()),
    PERIODO_ESTUDIO_DOCENTE(PeriodoEstudioDocente.class.getSimpleName(), "PeriEstDocCod", PeriodoEstudioDocente.class.getName()),
    PERIODO_ESTUDIO_DOCUMENTO(PeriodoEstudioDocumento.class.getSimpleName(), "DocCod", PeriodoEstudioDocumento.class.getName()),
    
    CALENDARIO(Calendario.class.getSimpleName(), "CalCod", Calendario.class.getName()),
    CALENDARIO_ALUMNO(CalendarioAlumno.class.getSimpleName(), "CalAlCod", CalendarioAlumno.class.getName()),
    CALENDARIO_DOCENTE(CalendarioDocente.class.getSimpleName(), "CalDocCod", CalendarioDocente.class.getName()),
    
    ESCOLARIDAD(Escolaridad.class.getSimpleName(), "EscCod", Escolaridad.class.getName()),

    INSCRIPCION(Inscripcion.class.getSimpleName(), "InsCod", Inscripcion.class.getName()),
    MATERIA_REVALIDA(MateriaRevalida.class.getSimpleName(), "MatRvlCod", MateriaRevalida.class.getName()),
    
    SOLICITUD(Solicitud.class.getSimpleName(), "SolCod", Solicitud.class.getName()),
    
    ARCHIVO(Archivo.class.getSimpleName(), "ArcCod", Archivo.class.getName());
    
    Objetos(){
        
    }
    
    private String namedQuery;
    private String primaryKey;
    private String className;

    Objetos(String pNombre, String pPrimaryKey, String pClassName) {
        this.namedQuery = pNombre;
        this.primaryKey = pPrimaryKey;
        this.className = pClassName;
    }

    /**
     *
     * @return
     */
    public String getNamedQuery() {
        return namedQuery;
    }
    
    /**
     *
     * @return
     */
    public String getPrimaryKey() {
        return primaryKey;
    }

    /**
     *
     * @return
     */
    public String getClassName() {
        return className;
    }
    
    /**
     *
     * @param name
     * @return
     */
    public static Boolean contains(String name){
        for (Objetos objeto  : Objetos.values()){
            if (objeto.name().toUpperCase().equals(name)){
                return true;
            }
        }
        return false;
    }
    
    /**
     *
     * @param upperQueryName
     * @return
     */
    public static Objetos fromQueryName(String upperQueryName) {
        for (Objetos objeto  : Objetos.values()){
            if (objeto.getNamedQuery().toUpperCase().equals(upperQueryName)){
                return objeto;
            }
        }
        throw new UnsupportedOperationException(
                "El objeto " + upperQueryName + " is not supported!");
    }
 
}
