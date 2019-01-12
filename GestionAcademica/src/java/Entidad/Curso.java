/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Dominio.SincHelper;
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
 * Entidad Curso
 * 
 * @author alvar
 */
@Entity
@Table(name = "CURSO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Curso.findAll", query = "SELECT c FROM Curso c"),
    @NamedQuery(name = "Curso.findModAfter", query = "SELECT c FROM Curso c WHERE c.ObjFchMod >= :ObjFchMod"),
    @NamedQuery(name = "Curso.findByCurCod", query = "SELECT c FROM Curso c WHERE c.CurCod = :CurCod"),
    @NamedQuery(name = "Curso.findByCurNom", query = "SELECT c FROM Curso c WHERE c.CurNom = :CurNom"),
    @NamedQuery(name = "Curso.findLastCurso", query = "SELECT  c FROM Curso c ORDER BY c.CurCod DESC")})

public class Curso extends SincHelper implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "CurCod", nullable = false)
    private Long CurCod;
    
    @Column(name = "CurNom", length = 100)
    private String CurNom;
    
    @Column(name = "CurDsc", length = 500)
    private String CurDsc;
    
    @Column(name = "CurFac", length = 255)
    private String CurFac;
    
    @Column(name = "CurCrt", length = 255)
    private String CurCrt;
    
    @Column(name = "CurCatCod")
    private Long CurCatCod;
    
    @Column(name = "ObjFchMod", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ObjFchMod;

    @OneToMany(targetEntity = Modulo.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="CurCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<Modulo> lstModulos;
    
    @OneToMany(targetEntity = Evaluacion.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="CurEvlCurCod", referencedColumnName="CurCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<Evaluacion> lstEvaluacion;

    /**
     *
     * @return Retorna el código del Curso
     */
    public Long getCurCod() {
        return CurCod;
    }

    /**
     *
     * @param CurCod Recibe el código del curso
     */
    public void setCurCod(Long CurCod) {
        this.CurCod = CurCod;
    }

    /**
     *
     * @return Retorna el Nombre del curso
     */
    public String getCurNom() {
        return CurNom;
    }

    /**
     *
     * @param CurNom Recibe el Nombre del curso
     */
    public void setCurNom(String CurNom) {
        this.CurNom = CurNom;
    }

    /**
     *
     * @return Retorna la Descripción del Curso
     */
    public String getCurDsc() {
        return CurDsc;
    }

    /**
     *
     * @param CurDsc Recibe la Descripción del Curso
     */
    public void setCurDsc(String CurDsc) {
        this.CurDsc = CurDsc;
    }

    /**
     *
     * @return Retorna la Facultad del Curso
     */
    public String getCurFac() {
        return CurFac;
    }

    /**
     *
     * @param CurFac Recibe la Facultad del Curso
     */
    public void setCurFac(String CurFac) {
        this.CurFac = CurFac;
    }

    /**
     *
     * @return Retorna la certificación del Curso
     */
    public String getCurCrt() {
        return CurCrt;
    }

    /**
     *
     * @param CurCrt Recibe la certificación del Curso
     */
    public void setCurCrt(String CurCrt) {
        this.CurCrt = CurCrt;
    }

    /**
     *
     * @return Retorna el código de la categoría del curso (El curso en moodle se define como categoría)
     */
    public Long getCurCatCod() {
        return CurCatCod;
    }

    /**
     *
     * @param CurCatCod recibe el código de la categoría del curso (El curso en moodle se define como categoría)
     */
    public void setCurCatCod(Long CurCatCod) {
        this.CurCatCod = CurCatCod;
    }

    /**
     *
     * @return Retorna la fecha de modificación del Curso
     */
    public Date getObjFchMod() {
        return ObjFchMod;
    }

    /**
     *
     * @param ObjFchMod recibe la fecha de modificación del Curso
     */
    public void setObjFchMod(Date ObjFchMod) {
        this.ObjFchMod = ObjFchMod;
    }

    /**
     *
     * @return Retorna la lista de módulos
     */
    @JsonIgnore
    @XmlTransient
    public List<Modulo> getLstModulos() {
        return lstModulos;
    }

    /**
     *
     * @param lstModulos Recibe la lista de módulos
     */
    public void setLstModulos(List<Modulo> lstModulos) {
        this.lstModulos = lstModulos;
    }

    /**
     *
     * @return Retorna la lista de evaluaciones
     */
    @JsonIgnore
    @XmlTransient
    public List<Evaluacion> getLstEvaluacion() {
        return lstEvaluacion;
    }

    /**
     *
     * @param lstEvaluacion Recibe la lista de evaluaciones
     */
    public void setLstEvaluacion(List<Evaluacion> lstEvaluacion) {
        this.lstEvaluacion = lstEvaluacion;
    }
    
    /**
     *
     * @param EvlCod Recibe el código de la Evaluación
     * @return Retorna la Evaluación dado el código recibido
     */
    public Evaluacion getEvaluacionById(Long EvlCod){
        
        Evaluacion evaluacion = new Evaluacion();
        
        for(Evaluacion evl : this.lstEvaluacion)
        {
            System.err.println("Evaluacion: " + evl.toString());
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
     * @param ModCod Recibe el código del módulo
     * @return Retorna el módulo dado el código recibido
     */
    public Modulo getModuloById(Long ModCod){
        
        Modulo pModulo = new Modulo();
        
        for(Modulo modulo : this.lstModulos)
        {
            if(modulo.getModCod().equals(ModCod))
            {
                pModulo = modulo;
                break;
            }
        }
        
        return pModulo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.CurCod);
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
        final Curso other = (Curso) obj;
        if (!Objects.equals(this.CurCod, other.CurCod)) {
            return false;
        }
        return true;
    }
   
   @Override
    public Long GetPrimaryKey() {
        return this.CurCod;
    }  
}
