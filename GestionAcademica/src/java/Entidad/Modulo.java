/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Dominio.SincHelper;
import Enumerado.TipoPeriodo;
import java.io.Serializable;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad de Módulo
 * 
 * @author alvar
 */

@Entity
@Table(name = "MODULO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Modulo.findAll", query = "SELECT m FROM Modulo m"),
    @NamedQuery(name = "Modulo.findModAfter", query = "SELECT m FROM Modulo m  WHERE m.ObjFchMod >= :ObjFchMod"),
    @NamedQuery(name = "Modulo.findByPK", query = "SELECT m FROM Modulo m WHERE m.curso.CurCod = :CurCod and m.ModCod = :ModCod"),
    @NamedQuery(name = "Modulo.findByCurso", query = "SELECT m FROM Modulo m WHERE m.curso.CurCod = :CurCod"),
    @NamedQuery(name = "Modulo.findByPeriodo", query = "SELECT m FROM Modulo m WHERE (m.curso.CurCod =:CurCod or :CurCod IS NULL) and m.ModTpoPer = :TpoPer and m.ModPerVal =:PerVal"),
    @NamedQuery(name = "Modulo.findLast", query = "SELECT  m FROM Modulo m WHERE m.curso.CurCod = :CurCod ORDER BY m.ModCod desc")})

public class Modulo extends SincHelper implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "ModCod")
    private Long ModCod;
        
    @OneToOne(targetEntity = Curso.class)
    @JoinColumn(name="CurCod", referencedColumnName = "CurCod")
    private Curso curso;    
    
    @Column(name = "ModNom", length = 100, unique = true)
    private String ModNom;
    
    @Column(name = "ModDsc", length = 500)
    private String ModDsc;
    
    
    @Column(name = "ModTpoPer")
    private TipoPeriodo ModTpoPer;
    
    @Column(name = "ModPerVal",precision=10, scale=2)
    private Double ModPerVal;
    
    @Column(name = "ModCntHor",precision=10, scale=2)
    private Double ModCntHor;
    
    @Column(name = "ModEstCod")
    private Long ModEstCod;
    
    @Column(name = "ObjFchMod", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ObjFchMod;
    
    @OneToMany(targetEntity = Evaluacion.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="ModEvlModCod", referencedColumnName="ModCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<Evaluacion> lstEvaluacion;
    
    //CONSTRUCTORES
    public Modulo() {
    }

    /**
     *
     * @return Retorna la lista de Evaluación
     */
    @JsonIgnore
    @XmlTransient
    public List<Evaluacion> getLstEvaluacion() {
        return lstEvaluacion;
    }

    /**
     *
     * @param lstEvaluacion Recibe la lista de Evaluación
     */
    public void setLstEvaluacion(List<Evaluacion> lstEvaluacion) {
        this.lstEvaluacion = lstEvaluacion;
    }
    
    /**
     *
     * @param EvlCod Recibe el código de la Evaluación
     * @return Retorna la evaluación dado el código recibido
     */
    public Evaluacion getEvaluacionById(Long EvlCod){
        
        Evaluacion evaluacion = new Evaluacion();
        
        for(Evaluacion evl : this.lstEvaluacion)
        {
            if(evl.getEvlCod().equals(EvlCod))
            {
                evaluacion = evl;
                break;
            }
        }
        
        return evaluacion;
    }
    
    /**
     *
     * @return Retorna el codigo del módulo
     */
    public Long getModCod() {
        return this.ModCod;
    }

    /**
     *
     * @param pModCod Recibe el código del Módulo
     */
    public void setModCod(Long pModCod) {
        this.ModCod = pModCod;
    }

    /**
     *
     * @return Retorna el codigo de MóduloEstudio
     */
    public Long getModEstCod() {
        return ModEstCod;
    }

    /**
     *
     * @param ModEstCod Recibe el codigo de MóduloEstudio
     */
    public void setModEstCod(Long ModEstCod) {
        this.ModEstCod = ModEstCod;
    }

    /**
     *
     * @return Retorna el Curso
     */
    public Curso getCurso() {
        return this.curso;
    }

    /**
     *
     * @param pCurso Rcibe el Curso
     */
    public void setCurso(Curso pCurso) {
        this.curso = pCurso;
    }

    /**
     *
     * @return Retorna el Nombre del Módulo
     */
    public String getModNom() {
        return ModNom;
    }

    /**
     *
     * @param ModNom Recibe el Nombre del Módulo
     */
    public void setModNom(String ModNom) {
        this.ModNom = ModNom;
    }

    /**
     *
     * @return Retorna la descripción del módulo
     */
    public String getModDsc() {
        return ModDsc;
    }

    /**
     *
     * @param ModDsc Recibe la descripción del módulo
     */
    public void setModDsc(String ModDsc) {
        this.ModDsc = ModDsc;
    }

    /**
     *
     * @return retorno el Tipo Período del módulo
     */
    public TipoPeriodo getModTpoPer() {
        return ModTpoPer;
    }

    /**
     *
     * @param ModTpoPer Recibe el Tipo Período del módulo
     */
    public void setModTpoPer(TipoPeriodo ModTpoPer) {
        this.ModTpoPer = ModTpoPer;
    }

    /**
     *
     * @return Retorna el valor del período
     */
    public Double getModPerVal() {
        return ModPerVal;
    }

    /**
     *
     * @param ModPerVal Recibe el valor del período
     */
    public void setModPerVal(Double ModPerVal) {
        this.ModPerVal = ModPerVal;
    }

    /**
     *
     * @return Retorna la Cantidad de horas del módulo
     */
    public Double getModCntHor() {
        return ModCntHor;
    }

    /**
     *
     * @param ModCntHor Recibe la Cantidad de horas del módulo
     */
    public void setModCntHor(Double ModCntHor) {
        this.ModCntHor = ModCntHor;
    }

    /**
     *
     * @return Retorna la fecha de modificación del módulo
     */
    public Date getObjFchMod() {
        return ObjFchMod;
    }

    /**
     *
     * @param ObjFchMod Recibe la fecha de modificación del módulo
     */
    public void setObjFchMod(Date ObjFchMod) {
        this.ObjFchMod = ObjFchMod;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.ModCod) + Objects.hashCode(this.curso);
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
        final Modulo other = (Modulo) obj;
        if (!Objects.equals(this.ModCod, other.ModCod)) {
            return false;
        }
        if (!Objects.equals(this.curso, other.curso)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modulo{" + "ModCod=" + ModCod + ", curso=" + curso + ", ModNom=" + ModNom + ", ModDsc=" + ModDsc + ", ModTpoPer=" + ModTpoPer + ", ModPerVal=" + ModPerVal + ", ModCntHor=" + ModCntHor + ", ModEstCod=" + ModEstCod + ", ObjFchMod=" + ObjFchMod + ", lstEvaluacion=" + lstEvaluacion + '}';
    }
    
    @Override
    public Long GetPrimaryKey() {
        return this.ModCod;
    }
}

