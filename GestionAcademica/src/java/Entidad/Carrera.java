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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad Carrera
 * 
 * @author alvar
 */

@Entity
@Table(name = "CARRERA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Carrera.findAll",           query = "SELECT c FROM Carrera c"),
    @NamedQuery(name = "Carrera.findModAfter",      query = "SELECT c FROM Carrera c WHERE c.ObjFchMod >= :ObjFchMod"),
    @NamedQuery(name = "Carrera.findByCarCod",      query = "SELECT c FROM Carrera c WHERE c.CarCod = :CarCod"),
    @NamedQuery(name = "Carrera.findByCarNom",      query = "SELECT c FROM Carrera c WHERE c.CarNom = :CarNom"),
    @NamedQuery(name = "Carrera.findLastCarrera",   query = "SELECT c FROM Carrera c ORDER BY c.CarCod DESC")})

public class Carrera extends SincHelper implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name="native", strategy="native")
    @Column(name="CarCod", nullable = false)
    private Long CarCod;
    
    @Column(name = "CarNom", length = 100)
    private String CarNom;
    @Column(name = "CarDsc", length = 500)
    private String CarDsc;
    @Column(name = "CarFac", length = 255)
    private String CarFac;
    @Column(name = "CarCrt", length = 255)
    private String CarCrt;
    @Column(name = "CarCatCod")
    private Long CarCatCod;
    @Column(name = "ObjFchMod", columnDefinition = "DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ObjFchMod;
    
    @OneToMany(targetEntity = PlanEstudio.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="CarCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<PlanEstudio> lstPlanEstudio;
    
   //CONSTRUCTOR
    public Carrera() {
        this.lstPlanEstudio = new ArrayList<>();
    }

    public Carrera(Long CarCod, String CarNom, String CarDsc, String CarFac, String CarCrt, Long CarCatCod, Date ObjFchMod) {
        this.CarCod = CarCod;
        this.CarNom = CarNom;
        this.CarDsc = CarDsc;
        this.CarFac = CarFac;
        this.CarCrt = CarCrt;
        this.CarCatCod = CarCatCod;
        this.ObjFchMod = ObjFchMod;
    }
    
    /**
     *
     * @return Retorna el código de la carrera
     */
    public Long getCarCod() {
        return CarCod;
    }

    /**
     *
     * @param CarCod Recibe el código de la Carrera
     */
    public void setCarCod(Long CarCod) {
        this.CarCod = CarCod;
    }

    /**
     *
     * @return Retorna el nombre de la Carrera
     */
    public String getCarNom() {
        return CarNom;
    }

    /**
     *
     * @param CarNom Recibe el nombre de la Carrera
     */
    public void setCarNom(String CarNom) {
        this.CarNom = CarNom;
    }

    /**
     *
     * @return Retorna la descripción de la Carrera
     */
    public String getCarDsc() {
        return CarDsc;
    }

    /**
     *
     * @param CarDsc Recibe la descripción de la Carrera
     */
    public void setCarDsc(String CarDsc) {
        this.CarDsc = CarDsc;
    }

    /**
     *
     * @return Retorna la Facultad de la Carrera
     */
    public String getCarFac() {
        return CarFac;
    }

    /**
     *
     * @param CarFac Recibe la Facultad de la Carrera
     */
    public void setCarFac(String CarFac) {
        this.CarFac = CarFac;
    }

    /**
     *
     * @return Retorna la Certificación de la Carrera
     */
    public String getCarCrt() {
        return CarCrt;
    }

    /**
     *
     * @param CarCrt Recibe la Certificación de la Carrera
     */
    public void setCarCrt(String CarCrt) {
        this.CarCrt = CarCrt;
    }

    /**
     *
     * @return Retorna el codigo de la categoría de moodle, la carrera en moodle se define como Categoría
     */
    public Long getCarCatCod() {
        return CarCatCod;
    }

    /**
     *
     * @param CarCatCod Recibe el codigo de la categoría de moodle, la carrera en moodle se define como Categoría
     */
    public void setCarCatCod(Long CarCatCod) {
        this.CarCatCod = CarCatCod;
    }

    /**
     *
     * @return Retorna la fecha de modificación de la Carrera
     */
    public Date getObjFchMod() {
        return ObjFchMod;
    }

    /**
     *
     * @param ObjFchMod Recibe la fecha de modificación de la Carrera
     */
    public void setObjFchMod(Date ObjFchMod) {
        this.ObjFchMod = ObjFchMod;
    }

    /**
     *
     * @return Retorna el plan de la Carrera
     */
    @JsonIgnore
    @XmlTransient
    public List<PlanEstudio> getPlan() {
        if(lstPlanEstudio == null) lstPlanEstudio = new ArrayList<>();
        return lstPlanEstudio;
    }

    /**
     *
     * @param lstPlanEstudio Recibe el plan de la Carrera
     */
    public void setPlan(List<PlanEstudio> lstPlanEstudio) {
        this.lstPlanEstudio = lstPlanEstudio;
    }
    
    /**
     *
     * @param PlaEstCod Recibe el código del Plan
     * @return Retorna el Plan de Estudio
     */
    public PlanEstudio getPlanEstudioById(Long PlaEstCod){
        
        PlanEstudio pPlan = new PlanEstudio();
        
        for(PlanEstudio plan : this.lstPlanEstudio)
        {
            if(plan.getPlaEstCod().equals(PlaEstCod))
            {
                pPlan = plan;
                break;
            }
        }
        
        return pPlan;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.CarCod);
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
        final Carrera other = (Carrera) obj;
        if (!Objects.equals(this.CarCod, other.CarCod)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Carrera{" + "CarCod=" + CarCod + ", CarNom=" + CarNom + ", CarDsc=" + CarDsc + ' ' + (lstPlanEstudio!= null ? lstPlanEstudio.size() : "") + '}';
    } 
}