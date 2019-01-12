/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Dominio.SincHelper;
import java.io.Serializable;
import java.util.Date;
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
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad de Evaluación
 *
 * @author alvar
 */

@Entity
@Table(name = "EVALUACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Evaluacion.findAll",            query = "SELECT t FROM Evaluacion t"),
    @NamedQuery(name = "Evaluacion.findModAfter",       query = "SELECT t FROM Evaluacion t where t.ObjFchMod >= :ObjFchMod"),
    @NamedQuery(name = "Evaluacion.findByPK",           query = "SELECT t FROM Evaluacion t WHERE t.EvlCod =:EvlCod"),
    @NamedQuery(name = "Evaluacion.findByCurso",        query = "SELECT t FROM Evaluacion t WHERE t.CurEvl.CurCod =:CurCod"),
    @NamedQuery(name = "Evaluacion.findByModulo",       query = "SELECT t FROM Evaluacion t WHERE t.ModEvl.curso.CurCod =:CurCod and t.ModEvl.ModCod =:ModCod"),
    
  //  @NamedQuery(name = "Evaluacion.findByEstudio",       query = "SELECT t FROM Evaluacion t WHERE (t.CurEvl.CurCod =:CurCod OR :CurCod IS NULL) OR (t.ModEvl.ModCod = :ModCod OR :ModCod IS NULL) OR (T.MatEvl.MatCod =:MatCod OR :MatCod IS NULL)"),
        
    @NamedQuery(name = "Evaluacion.findLast",      query = "SELECT t FROM Evaluacion t ORDER BY t.EvlCod DESC")})

public class Evaluacion extends SincHelper implements Serializable {

    private static final long serialVersionUID = 1L;

    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "EvlCod", nullable = false)
    private Long EvlCod;
    
    @OneToOne(targetEntity = Materia.class, optional=true)
    @JoinColumn(name="MatEvlMatCod", referencedColumnName="MatCod")
    private Materia MatEvl;
    
    @OneToOne(targetEntity = Curso.class, optional=true)
    @JoinColumn(name="CurEvlCurCod", referencedColumnName="CurCod")
    private Curso CurEvl;
    
    @OneToOne(targetEntity = Modulo.class, optional=true)
    @JoinColumn(name="ModEvlModCod", referencedColumnName="ModCod")
    private Modulo ModEvl;
    
    @ManyToOne(targetEntity = TipoEvaluacion.class, optional=true)
    @JoinColumn(name="TpoEvlCod", referencedColumnName="TpoEvlCod")
    private TipoEvaluacion tipoEvaluacion;
        
    @Column(name = "EvlNom", length = 100)
    private String EvlNom;
    @Column(name = "EvlDsc", length = 500)
    private String EvlDsc;
    @Column(name = "EvlNotTot", precision=10, scale=2)
    private Double EvlNotTot;
    @Column(name = "ObjFchMod", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ObjFchMod;

    //-CONSTRUCTOR
    public Evaluacion() {
    }
    
    
    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorno el código de la Evaluación
     */
    public Long getEvlCod() {
        return EvlCod;
    }

    /**
     *
     * @param EvlCod Recibo el código de la Evaluación
     */
    public void setEvlCod(Long EvlCod) {
        this.EvlCod = EvlCod;
    }
    
    /**
     *
     * @return Retorno la materia de la Evaluación
     */
    public Materia getMatEvl() {
        return MatEvl;
    }
    
    /**
     *
     * @param MatEvl recibo la materia de la Evaluación
     */
    public void setMatEvl(Materia MatEvl) {
        this.MatEvl = MatEvl;
    }
    
    /**
     *
     * @return Retorno el módulo de la Evaluación
     */
    public Modulo getModEvl() {
        return ModEvl;
    }

    /**
     *
     * @param ModEvl Recibo el módulo de la Evaluación
     */
    public void setModEvl(Modulo ModEvl) {
        this.ModEvl = ModEvl;
    }

    /**
     *
     * @return Retorno el nombre de la Evaluación
     */
    public String getEvlNom() {
        return EvlNom;
    }

    /**
     *
     * @param EvlNom Recibo el nombre de la Evaluación
     */
    public void setEvlNom(String EvlNom) {
        this.EvlNom = EvlNom;
    }

    /**
     *
     * @return Retorno la descripción de la Evaluación
     */
    public String getEvlDsc() {
        return EvlDsc;
    }

    /**
     *
     * @param EvlDsc Recibo la descripción de la Evaluación
     */
    public void setEvlDsc(String EvlDsc) {
        this.EvlDsc = EvlDsc;
    }

    /**
     *
     * @return Retorno la nota Total
     */
    public Double getEvlNotTot() {
        return EvlNotTot;
    }

    /**
     *
     * @param EvlNotTot Recibo la nota total
     */
    public void setEvlNotTot(Double EvlNotTot) {
        this.EvlNotTot = EvlNotTot;
    }

    /**
     *
     * @return Retorno el tipo de evaluación
     */
    public TipoEvaluacion getTpoEvl() {
        if(tipoEvaluacion == null)  tipoEvaluacion = new TipoEvaluacion();
        return tipoEvaluacion;
    }

    /**
     *
     * @param TpoEvl Recibo el tipo de evaluación
     */
    public void setTpoEvl(TipoEvaluacion TpoEvl) {
        this.tipoEvaluacion = TpoEvl;
    }

    /**
     *
     * @return Retorno la fecha de modificación de la evaluación
     */
    public Date getObjFchMod() {
        return ObjFchMod;
    }
    
    /**
     *
     * @return Retorno la evaluación del curso
     */
    public Curso getCurEvl() {
        return CurEvl;
    }

    /**
     *
     * @param CurEvl Recibo la evaluación del curso
     */
    public void setCurEvl(Curso CurEvl) {
        this.CurEvl = CurEvl;
    }

    /**
     *
     * @param ObjFchMod Recibo la fecha de modificación de la evaluación
     */
    public void setObjFchMod(Date ObjFchMod) {
        this.ObjFchMod = ObjFchMod;
    }

    /**
     *
     * @return Retorno el tipo de estudio (Curso, Mateira o Modulo)
     */
    public String getEstudioTipo()
    {
        if(this.getCurEvl() != null)
        {
            return "Curso";
        }
        
        if(this.getMatEvl() != null)
        {
            return "Materia";
        }
        
        if(this.getModEvl() != null)
        {
            return "Modulo";
        }
        
        return "";
    }
    
    /**
     *
     * @return Retorno Inscripción automatica o no
     * 
     */
    public String getInscripcionAutomatica()
    {
        return (this.tipoEvaluacion.getTpoEvlInsAut() ? "Si" :  "No");
    }
    
    /**
     *
     * @return Retorno el nombre del estudio (Curso, Mateira o Modulo)
     */
    public String getEstudioNombre()
    {
        if(this.getCurEvl() != null)
        {
            return this.getCurEvl().getCurNom();
        }
        
        if(this.getMatEvl() != null)
        {
            return this.getMatEvl().getMatNom();
        }
        
        if(this.getModEvl() != null)
        {
            return this.getModEvl().getModNom();
        }
        
        return "";
    }
    
    /**
     *
     * @return Retorno El nombre del Curso, Mateira o Modulo
     */
    public String getCarreraCursoNombre()
    {
        if(this.getCurEvl() != null)
        {
            return this.getCurEvl().getCurNom();
        }
        
        if(this.getMatEvl() != null)
        {
            if(this.getMatEvl().getPlan() != null) return this.getMatEvl().getPlan().getCarreraPlanNombre();
        }
        
        if(this.getModEvl() != null)
        {
            if(this.getModEvl().getCurso() != null) return this.getModEvl().getCurso().getCurNom();
        }
        
        return "";
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (EvlCod != null ? EvlCod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Evaluacion)) {
            return false;
        }
        Evaluacion other = (Evaluacion) object;
        if ((this.EvlCod == null && other.EvlCod != null) || (this.EvlCod != null && !this.EvlCod.equals(other.EvlCod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidad.Evaluacion[ id=" + EvlCod + " ]";
    }
    
    @Override
    public Long GetPrimaryKey() {
        return this.EvlCod;
    }
    
}
