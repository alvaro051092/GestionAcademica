/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Dominio.SincHelper;
import Enumerado.EstadoCalendarioEvaluacion;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad CalendarioAlumno
 * @author alvar
 */


@Entity
@Table(
        name = "CALENDARIO_ALUMNO",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"CalCod", "AluPerCod"})}
    )
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CalendarioAlumno.findModAfter",  query = "SELECT t FROM CalendarioAlumno t where t.ObjFchMod >= :ObjFchMod"),
    @NamedQuery(name = "CalendarioAlumno.findAll",       query = "SELECT t FROM CalendarioAlumno t")})

public class CalendarioAlumno  extends SincHelper implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "CalAlCod", nullable = false)
    private Long CalAlCod;
    
    @ManyToOne(targetEntity = Calendario.class)
    @JoinColumn(name="CalCod", referencedColumnName = "CalCod")
    private Calendario calendario;

    @ManyToOne(targetEntity = Persona.class)
    @JoinColumn(name="AluPerCod", referencedColumnName = "PerCod")
    private Persona Alumno;

    @Column(name = "EvlCalVal", precision=10, scale=2)
    private Double EvlCalVal;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "EvlCalEst", precision=10, scale=2)
    private EstadoCalendarioEvaluacion EvlCalEst;

    @Column(name = "EvlCalObs", length = 500)
    private String EvlCalObs;
    
    @Column(name = "EvlValObs", length = 500)
    private String EvlValObs;
    
    @Column(name = "EvlCalFch", columnDefinition="DATE")
    @Temporal(TemporalType.DATE)
    private Date EvlCalFch;
    
    @Column(name = "EvlValFch", columnDefinition="DATE")
    @Temporal(TemporalType.DATE)
    private Date EvlValFch;
    
    @Column(name = "ObjFchMod", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ObjFchMod;

    @ManyToOne(targetEntity = Persona.class)
    @JoinColumn(name="EvlCalPerCod", referencedColumnName = "PerCod")
    private Persona EvlCalPor;
    
    @ManyToOne(targetEntity = Persona.class)
    @JoinColumn(name="EvlValPerCod", referencedColumnName = "PerCod")
    private Persona EvlValPor;

    

    //-CONSTRUCTOR
    public CalendarioAlumno() {
    }

    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorna el Código de CalendarioAlumno
     */
    public Long getCalAlCod() {
        return CalAlCod;
    }

    /**
     *
     * @param CalAlCod Recibe el Código de CalendarioAlumno
     */
    public void setCalAlCod(Long CalAlCod) {
        this.CalAlCod = CalAlCod;
    }
    
    /**
     *
     * @return Retorna el CalendarioAlumno
     */
    public Calendario getCalendario() {
        return calendario;
    }

    /**
     *
     * @param calendario Recibe el CalendarioAlumno
     */
    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

    /**
     *
     * @return Retorna el Alumno del CalendarioAlumno
     */
    public Persona getAlumno() {
        return Alumno;
    }

    /**
     *
     * @param Alumno Recibe el Alumno del CalendarioAlumno
     */
    public void setAlumno(Persona Alumno) {
        this.Alumno = Alumno;
    }

    /**
     *
     * @return Retorna la calificación de la evaluación
     */
    public Double getEvlCalVal() {
        return EvlCalVal;
    }

    /**
     *
     * @param EvlCalVal Recibe la calificación de la evaluacion
     */
    public void setEvlCalVal(Double EvlCalVal) {
        this.EvlCalVal = EvlCalVal;
    }

    /**
     *
     * @return Retorna el estado de la evaluación (Estado: sin calificar, calificado, validado)
     */
    public EstadoCalendarioEvaluacion getEvlCalEst() {
        return EvlCalEst;
    }

    /**
     *
     * @param EvlCalEst Recibe el estado de la evaluación (Estado: sin calificar, calificado, validado)
     */
    public void setEvlCalEst(EstadoCalendarioEvaluacion EvlCalEst) {
        this.EvlCalEst = EvlCalEst;
    }

    /**
     *
     * @return Retorna las Observaciónes de la evaluación
     */
    public String getEvlCalObs() {
        return EvlCalObs;
    }

    /**
     *
     * @param EvlCalObs Recibe las Observaciones de la evaluación
     */
    public void setEvlCalObs(String EvlCalObs) {
        this.EvlCalObs = EvlCalObs;
    }

    /**
     *
     * @return Retorna las Observaciones de la validación
     */
    public String getEvlValObs() {
        return EvlValObs;
    }

    /**
     *
     * @param EvlValObs Recibe las Observaciones de la valicación
     */
    public void setEvlValObs(String EvlValObs) {
        this.EvlValObs = EvlValObs;
    }

    /**
     *
     * @return Retorna la fecha de calificación
     */
    public Date getEvlCalFch() {
        return EvlCalFch;
    }

    /**
     *
     * @param EvlCalFch Recibe la fecha de calificación
     */
    public void setEvlCalFch(Date EvlCalFch) {
        this.EvlCalFch = EvlCalFch;
    }

    /**
     *
     * @return Retorna la fecha de valicación
     */
    public Date getEvlValFch() {
        return EvlValFch;
    }

    /**
     *
     * @param EvlValFch Recibe la fecha de Validación
     */
    public void setEvlValFch(Date EvlValFch) {
        this.EvlValFch = EvlValFch;
    }

    /**
     *
     * @return Retorna la fecha de modificación
     */
    public Date getObjFchMod() {
        return ObjFchMod;
    }

    /**
     *
     * @param ObjFchMod Recibe la fecha de modificación
     */
    public void setObjFchMod(Date ObjFchMod) {
        this.ObjFchMod = ObjFchMod;
    }

    /**
     *
     * @return Retorna el código de la persona que calificó
     */
    public Persona getEvlCalPor() {
        return EvlCalPor;
    }

    /**
     *
     * @param EvlCalPor Recibe el código de la persona que calificó
     */
    public void setEvlCalPor(Persona EvlCalPor) {
        this.EvlCalPor = EvlCalPor;
    }

    /**
     *
     * @return Retorna el código de la persona que validó
     */
    public Persona getEvlValPor() {
        return EvlValPor;
    }

    /**
     *
     * @param EvlValPor Recibe el código de la persona que validó
     */
    public void setEvlValPor(Persona EvlValPor) {
        this.EvlValPor = EvlValPor;
    }
    
    /**
     *
     * @return Retorna si se puede calificar la evaluación o no
     */
    public boolean puedeCalificarse()
    {
        return (this.EvlCalEst.equals(EstadoCalendarioEvaluacion.CALIFICADO) || this.EvlCalEst.equals(EstadoCalendarioEvaluacion.PENDIENTE_CORRECCION) || this.EvlCalEst.equals(EstadoCalendarioEvaluacion.SIN_CALIFICAR));
    }
    
    /**
     *
     * @return Retorna si se puede enviar a validar
     */
    public boolean puedeEnviarToValidar()
    {
        return (this.EvlCalEst.equals(EstadoCalendarioEvaluacion.CALIFICADO));
    }
    
    /**
     *
     * @return Retorna si se puede validar o no.
     */
    public boolean puedeValidarse()
    {
        return (this.EvlCalEst.equals(EstadoCalendarioEvaluacion.PENDIENTE_VALIDACION));
    }
    
    /**
     *
     * @return Retorna si se puede editar o no.
     */
    public boolean puedeEditarlo()
    {
        return (!this.EvlCalEst.equals(EstadoCalendarioEvaluacion.VALIDADO));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.CalAlCod);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CalendarioAlumno other = (CalendarioAlumno) obj;
        if (!Objects.equals(this.CalAlCod, other.CalAlCod)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CalendarioAlumno{" + "CalAlCod=" + CalAlCod + ", Alumno=" + Alumno + ", EvlCalVal=" + EvlCalVal + ", EvlCalEst=" + EvlCalEst + ", EvlCalObs=" + EvlCalObs + ", EvlValObs=" + EvlValObs + ", EvlCalFch=" + EvlCalFch + ", EvlValFch=" + EvlValFch + ", ObjFchMod=" + ObjFchMod + ", EvlCalPor=" + EvlCalPor + ", EvlValPor=" + EvlValPor + '}';
    }

    @Override
    public Long GetPrimaryKey() {
        return this.CalAlCod;
    }
}



