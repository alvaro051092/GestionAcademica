/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Dominio.SincHelper;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/** 
 * Entidad PeriodoEstudioAlumno
 *
 * @author alvar
 */


@Entity
@Table(
        name = "PERIODO_ESTUDIO_ALUMNO",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"PeriEstCod", "AluPerCod"})}
    )
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PeriodoEstudioAlumno.findModAfter",  query = "SELECT t FROM PeriodoEstudioAlumno t  WHERE t.ObjFchMod >= :ObjFchMod"),
    @NamedQuery(name = "PeriodoEstudioAlumno.findAll",       query = "SELECT t FROM PeriodoEstudioAlumno t")
})
public class PeriodoEstudioAlumno extends SincHelper implements Serializable {

    private static final long serialVersionUID = 1L;
   
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "PeriEstAluCod", nullable = false)
    private Long PeriEstAluCod;
    
    @OneToOne(targetEntity = PeriodoEstudio.class)
    @JoinColumn(name="PeriEstCod", referencedColumnName="PeriEstCod")
    private PeriodoEstudio periodoEstudio;

    @ManyToOne(targetEntity = Persona.class)
    @JoinColumn(name="AluPerCod", referencedColumnName="PerCod")
    private Persona Alumno;
   
    
    @ManyToOne(targetEntity = Persona.class)
    @JoinColumn(name="PerInsPerCod", referencedColumnName="PerCod")
    private Persona InscritoPor;

    @Column(name = "PerInsFchInsc", columnDefinition="DATE")
    @Temporal(TemporalType.DATE)
    private Date PerInsFchInsc;
    @Column(name = "PerInsCalFin", precision=10, scale=2)
    private Double PerInsCalFin;
    @Column(name = "PerInsFrz")
    private Boolean PerInsFrz;
    
    @Column(name = "ObjFchMod", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ObjFchMod;


    //-CONSTRUCTOR
    public PeriodoEstudioAlumno() {
    }
    
    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorna el código de PeriodoEstudioAlumno
     */
    public Long getPeriEstAluCod() {
        return PeriEstAluCod;
    }

    /**
     *
     * @param PeriEstAluCod Recibe el código de PeriodoEstudioAlumno
     */
    public void setPeriEstAluCod(Long PeriEstAluCod) {
        this.PeriEstAluCod = PeriEstAluCod;
    }

    /**
     *
     * @return Retorna el período de estudio
     */
    public PeriodoEstudio getPeriodoEstudio() {
        return periodoEstudio;
    }

    /**
     *
     * @param PeriodoEstudio Recibe el período de estudio
     */
    public void setPeriodoEstudio(PeriodoEstudio PeriodoEstudio) {
        this.periodoEstudio = PeriodoEstudio;
    }

    /**
     *
     * @return Retorna el alumno de PeriodoEstudioAlumno
     */
    public Persona getAlumno() {
        return Alumno;
    }

    /**
     *
     * @param Alumno Recibe el alumno de PeriodoEstudioAlumno
     */
    public void setAlumno(Persona Alumno) {
        this.Alumno = Alumno;
    }

    /**
     *
     * @return Retorna si es inscripto por alguien mas
     */
    public Persona getInscritoPor() {
        return InscritoPor;
    }

    /**
     *
     * @param InscritoPor Recibe si es inscripto por alguien mas
     */
    public void setInscritoPor(Persona InscritoPor) {
        this.InscritoPor = InscritoPor;
    }

    /**
     *
     * @return Retorna la fecha de inscripción
     */
    public Date getPerInsFchInsc() {
        return PerInsFchInsc;
    }

    /**
     *
     * @param PerInsFchInsc Recibe la fecha de iscripción
     */
    public void setPerInsFchInsc(Date PerInsFchInsc) {
        this.PerInsFchInsc = PerInsFchInsc;
    }

    /**
     *
     * @return Retorna la calificación final
     */ 
    public Double getPerInsCalFin() {
        return PerInsCalFin;
    }

    /**
     *
     * @param PerInsCalFin Recibe la calificación final
     */
    public void setPerInsCalFin(Double PerInsCalFin) {
        this.PerInsCalFin = PerInsCalFin;
    }

    /**
     *
     * @return Retorna la inscripción Forzada
     */
    public Boolean getPerInsFrz() {
        return PerInsFrz;
    }

    /**
     *
     * @param PerInsFrz Recibe la inscripción Forzada
     */
    public void setPerInsFrz(Boolean PerInsFrz) {
        this.PerInsFrz = PerInsFrz;
    }

    /**
     *
     * @return Retorna la fecha de modificación de PeriodoEstudioAlumno
     */
    public Date getObjFchMod() {
        return ObjFchMod;
    }

    /**
     *
     * @param ObjFchMod Recibe la fecha de modificación de PeriodoEstudioAlumno
     */
    public void setObjFchMod(Date ObjFchMod) {
        this.ObjFchMod = ObjFchMod;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.PeriEstAluCod);
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
        final PeriodoEstudioAlumno other = (PeriodoEstudioAlumno) obj;
        if (!Objects.equals(this.PeriEstAluCod, other.PeriEstAluCod)) {
            return false;
        }
        return true;
    }

   

    @Override
    public String toString() {
        return "PeriodoEstudioAlumno{" + "PeriEstAluCod=" + PeriEstAluCod + ", PeriodoEstudio=" + periodoEstudio + ", Alumno=" + Alumno + ", InscritoPor=" + InscritoPor + ", PerInsFchInsc=" + PerInsFchInsc + ", PerInsCalFin=" + PerInsCalFin + ", PerInsFrz=" + PerInsFrz + ", ObjFchMod=" + ObjFchMod + '}';
    }
    
    @Override
    public Long GetPrimaryKey() {
        return this.PeriEstAluCod;
    }
    
    
}


