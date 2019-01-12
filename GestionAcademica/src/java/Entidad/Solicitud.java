/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Dominio.SincHelper;
import Enumerado.TipoSolicitud;
import Enumerado.EstadoSolicitud;
import java.io.Serializable;
import java.util.Date;
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
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad Solicitud 
 *
 * @author alvar
 */
@Entity
@Table(name = "SOLICITUD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Solicitud.findAll",       query = "SELECT t FROM Solicitud t order by t.SolFchIng desc"),
    @NamedQuery(name = "Solicitud.findModAfter",  query = "SELECT t FROM Solicitud t  WHERE t.ObjFchMod >= :ObjFchMod order by t.SolFchIng desc"),
    @NamedQuery(name = "Solicitud.findByAlumno",  query = "SELECT t FROM Solicitud t where t.Alumno.PerCod =:PerCod order by t.SolFchIng desc")
})

public class Solicitud extends SincHelper implements Serializable {

    private static final long serialVersionUID = 1L;


    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "SolCod", nullable = false)
    private Long  SolCod;

    @ManyToOne(targetEntity = Persona.class)
    @JoinColumn(name="AluPerCod", referencedColumnName = "PerCod")
    private Persona Alumno;
    
    @ManyToOne(targetEntity = Persona.class)
    @JoinColumn(name="FunPerCod", referencedColumnName = "PerCod")
    private Persona Funcionario;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "SolTpo")
    private TipoSolicitud SolTpo;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "SolEst")
    private EstadoSolicitud SolEst;
    
    @Column(name = "SolFchIng", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date SolFchIng;
    
    @Column(name = "SolFchPrc", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date SolFchPrc;
    
    @Column(name = "SolFchFin", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date SolFchFin;
    
    @Column(name = "ObjFchMod", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ObjFchMod;
    
    //-CONSTRUCTOR
    public Solicitud() {
    }

  
    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorna el código de Spolicitud
     */
    public Long getSolCod() {
        return SolCod;
    }

    /**
     *
     * @param SolCod Recibe el código de Spolicitud
     */
    public void setSolCod(Long SolCod) {
        this.SolCod = SolCod;
    }

    /**
     *
     * @return Retorna el alumno de Spolicitud
     */
    public Persona getAlumno() {
        return Alumno;
    }

    /**
     *
     * @param Alumno Recibe el alumno de Spolicitud
     */
    public void setAlumno(Persona Alumno) {
        this.Alumno = Alumno;
    }

    /**
     * 
     * @return Retorna el funcionario de Spolicitud
     */
    public Persona getFuncionario() {
        return Funcionario;
    }

    /**
     *
     * @param Funcionario Recibe el funcionario de Spolicitud
     */
    public void setFuncionario(Persona Funcionario) {
        this.Funcionario = Funcionario;
    }

    /**
     *
     * @return Retorna el Tipo de Solicitud
     */
    public TipoSolicitud getSolTpo() {
        return SolTpo;
    }

    /**
     *
     * @param SolTpo Recibe el tipo de solicitud
     */
    public void setSolTpo(TipoSolicitud SolTpo) {
        this.SolTpo = SolTpo;
    }

    /**
     *
     * @return Retorna el estado de solicitud
     */
    public EstadoSolicitud getSolEst() {
        return SolEst;
    }

    /**
     *
     * @param SolEst Recibe el estado de solicitud
     */
    public void setSolEst(EstadoSolicitud SolEst) {
        this.SolEst = SolEst;
    }

    /**
     *
     * @return Retorna la fecha de ingreso de solicitud
     */
    public Date getSolFchIng() {
        return SolFchIng;
    }

    /**
     *
     * @param SolFchIng Recibe la fecha de ingreso de solicitud
     */
    public void setSolFchIng(Date SolFchIng) {
        this.SolFchIng = SolFchIng;
    }

    /**
     *
     * @return Retorna la fecha de proceso de Solicitud
     */
    public Date getSolFchPrc() {
        return SolFchPrc;
    }

    /**
     *
     * @param SolFchPrc Recibe la fecha de proceso de soliciud
     */
    public void setSolFchPrc(Date SolFchPrc) {
        this.SolFchPrc = SolFchPrc;
    }

    /**
     *
     * @return Retorna la fecha final de solicitud
     */
    public Date getSolFchFin() {
        return SolFchFin;
    }

    /**
     *
     * @param SolFchFin Recibe la fecha final de solicitud
     */
    public void setSolFchFin(Date SolFchFin) {
        this.SolFchFin = SolFchFin;
    }

    /**
     *
     * @return Retorna la fecha de modificación de Spolicitud
     */ 
    public Date getObjFchMod() {
        return ObjFchMod;
    }

    /**
     *
     * @param ObjFchMod Recibe la fecha de modificación de Spolicitud
     */
    public void setObjFchMod(Date ObjFchMod) {
        this.ObjFchMod = ObjFchMod;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (SolCod != null ? SolCod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Solicitud)) {
            return false;
        }
        Solicitud other = (Solicitud) object;
        if ((this.SolCod == null && other.SolCod != null) || (this.SolCod != null && !this.SolCod.equals(other.SolCod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Solicitud{" + "SolCod=" + SolCod + ", Alumno=" + Alumno + ", Funcionario=" + Funcionario + ", SolTpo=" + SolTpo + ", SolEst=" + SolEst + ", SolFchIng=" + SolFchIng + ", SolFchPrc=" + SolFchPrc + ", SolFchFin=" + SolFchFin + ", ObjFchMod=" + ObjFchMod + '}';
    }

    @Override
    public Long GetPrimaryKey() {
        return this.SolCod;
    }
    
}
