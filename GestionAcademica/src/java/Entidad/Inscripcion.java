/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Dominio.SincHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author alvar
 */
@Entity
@Table(
        name = "INSCRIPCION",
        uniqueConstraints = {
                                @UniqueConstraint(columnNames = {"AluPerCod", "CarInsPlaEstCod"}),
                                @UniqueConstraint(columnNames = {"AluPerCod", "CurInsCurCod"})
                            }
    )
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inscripcion.findAll",       query = "SELECT t FROM Inscripcion t"),
    @NamedQuery(name = "Inscripcion.findModAfter",  query = "SELECT t FROM Inscripcion t WHERE t.ObjFchMod >= :ObjFchMod"),
    @NamedQuery(name = "Inscripcion.findByAlumno",  query = "SELECT t FROM Inscripcion t WHERE t.Alumno.PerCod =:PerCod"),
    @NamedQuery(name = "Inscripcion.findByPlan",    query = "SELECT t FROM Inscripcion t WHERE (t.Alumno.PerCod =:PerCod or :PerCod IS NULL) and t.PlanEstudio.PlaEstCod =:PlaEstCod"),
    @NamedQuery(name = "Inscripcion.findByCurso",   query = "SELECT t FROM Inscripcion t WHERE (t.Alumno.PerCod =:PerCod or :PerCod IS NULL) and t.Curso.CurCod =:CurCod")
})

public class Inscripcion extends SincHelper implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "InsCod", nullable = false)
    private Long InsCod; 
    
    
    @OneToOne(targetEntity = Persona.class)        
    @JoinColumn(name="AluPerCod", referencedColumnName="PerCod")
    private Persona Alumno;

    @OneToOne(targetEntity = Persona.class)        
    @JoinColumn(name="InsPerCod", referencedColumnName="PerCod")
    private Persona PersonaInscribe;
    
    @ManyToOne(targetEntity = PlanEstudio.class)        
    @JoinColumn(name="CarInsPlaEstCod", referencedColumnName="PlaEstCod")
    private PlanEstudio PlanEstudio;
    
    @OneToOne(targetEntity = Curso.class)        
    @JoinColumn(name="CurInsCurCod", referencedColumnName="CurCod")
    private Curso Curso;
          
    @Column(name = "AluFchCert", columnDefinition="DATE")
    @Temporal(TemporalType.DATE)
    private Date AluFchCert;   
    
    @Column(name = "AluFchInsc", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date AluFchInsc;
    
    @Column(name = "ObjFchMod", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ObjFchMod;
    
    @Column(name = "InsGenAnio")
    private Integer InsGenAnio;
    
    @OneToMany(targetEntity = MateriaRevalida.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="InsCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<MateriaRevalida> lstRevalidas;
    
    //-CONSTRUCTOR
    public Inscripcion() {
    }
    
    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorna el código de la Inscripción
     */
    public Long getInsCod() {
        return InsCod;
    }

    /**
     *
     * @param InsCod Recibe el código de la Inscripción
     */
    public void setInsCod(Long InsCod) {
        this.InsCod = InsCod;
    }

    /**
     *
     * @return Retorna el alumno
     */
    public Persona getAlumno() {
        return Alumno;
    }

    /**
     *
     * @param Alumno Recibe el alumno
     */
    public void setAlumno(Persona Alumno) {
        this.Alumno = Alumno;
    }

    /**
     *
     * @return Retorna la Persona
     */
    public Persona getPersonaInscribe() {
        return PersonaInscribe;
    }

    /**
     *
     * @param PersonaInscribe Recobe la persona
     */
    public void setPersonaInscribe(Persona PersonaInscribe) {
        this.PersonaInscribe = PersonaInscribe;
    }

    /**
     *
     * @return Retorna el plan de estudio de la Inscripción
     */
    public PlanEstudio getPlanEstudio() {
        return PlanEstudio;
    }

    /**
     *
     * @param PlanEstudio Recibe el Plan de Estudio de la Inscripción
     */
    public void setPlanEstudio(PlanEstudio PlanEstudio) {
        this.PlanEstudio = PlanEstudio;
    }

    /**
     *
     * @return Retorna el curso de la Inscripción
     */
    public Curso getCurso() {
        return Curso;
    }

    /**
     *
     * @param Curso Recibe el curso de la Inscripción
     */
    public void setCurso(Curso Curso) {
        this.Curso = Curso;
    }

    /**
     *
     * @return Retorna la fecha de certificación
     */
    public Date getAluFchCert() {
        return AluFchCert;
    }

    /**
     *
     * @param AluFchCert Recibe la fecha de certificación
     */
    public void setAluFchCert(Date AluFchCert) {
        this.AluFchCert = AluFchCert;
    }

    /**
     *
     * @return Retorna la fecha de Inscripción
     */
    public Date getAluFchInsc() {
        return AluFchInsc;
    }

    /**
     *
     * @param AluFchInsc Recibe la fecha de inscripción
     */
    public void setAluFchInsc(Date AluFchInsc) {
        this.AluFchInsc = AluFchInsc;
    }

    /**
     *
     * @return Retorno la fecha de modificación
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
     * @return Retorno el nombre del estudio
     */
    public String getNombreEstudio()
    {
        
        if(this.PlanEstudio != null) return this.PlanEstudio.getCarreraPlanNombre();
        if(this.Curso != null) return this.Curso.getCurNom();

        return "";
    }

    /**
     *
     * @return Retorno el año de la generación a inscribir
     */
    public Integer getInsGenAnio() {
        return InsGenAnio;
    }

    /**
     *
     * @param InsGenAnio Recibo el año de la generación a inscribir
     */
    public void setInsGenAnio(Integer InsGenAnio) {
        this.InsGenAnio = InsGenAnio;
    }

    /**
     *
     * @return Retorna la lista de Materias Revalidas
     */
    @JsonIgnore
    @XmlTransient
    public List<MateriaRevalida> getLstRevalidas() {
        if(lstRevalidas == null) lstRevalidas = new ArrayList<>();
        return lstRevalidas;
    }

    /**
     *
     * @param lstRevalidas Recibe la lista de materias revalidas
     */
    public void setLstRevalidas(List<MateriaRevalida> lstRevalidas) {
        this.lstRevalidas = lstRevalidas;
    }
    
    /**
     *
     * @param MatCod Recibe el código de la materia
     * @return Retorna la materia revalidas dado el código recibido
     */
    public boolean MateriaRevalidada(Long MatCod)
    {
        for(MateriaRevalida matRvl : this.lstRevalidas)
        {
            if(matRvl.getMateria().getMatCod().equals(MatCod))
            {
                return true;
            }
        }
        return false;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.InsCod);
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
        final Inscripcion other = (Inscripcion) obj;
        if (!Objects.equals(this.InsCod, other.InsCod)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Inscripcion{" + "InsCod=" + InsCod + ", Persona=" + Alumno + ", PersonaInscribe=" + PersonaInscribe + ", PlanEstudio=" + PlanEstudio + ", Curso=" + Curso + ", AluFchCert=" + AluFchCert + ", AluFchInsc=" + AluFchInsc + ", ObjFchMod=" + ObjFchMod + '}';
    }

    @Override
    public Long GetPrimaryKey() {
        return this.InsCod;
    }
    
    
}



