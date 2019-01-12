/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Dominio.SincHelper;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import Enumerado.TipoAprobacion;
import Enumerado.TipoPeriodo;
import Utiles.Utilidades;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * Entidad de Materia
 * 
 * @author alvar
 */

@Entity
@Table(name = "MATERIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Materia.findAll",       query = "SELECT t FROM Materia t"),
    @NamedQuery(name = "Materia.findModAfter",  query = "SELECT t FROM Materia t WHERE t.ObjFchMod >= :ObjFchMod"),
    @NamedQuery(name = "Materia.findByPlan",    query = "SELECT t FROM Materia t WHERE t.plan.PlaEstCod =:PlaEstCod"),
    @NamedQuery(name = "Materia.findByPeriodo", query = "SELECT m FROM Materia m WHERE (m.plan.PlaEstCod =:PlaEstCod or :PlaEstCod IS NULL) and m.MatTpoPer = :TpoPer and m.MatPerVal =:PerVal")
})

public class Materia extends SincHelper implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "MatCod")
    private Long MatCod;

    @ManyToOne(targetEntity = PlanEstudio.class)
    @JoinColumn(name="PlaEstCod", referencedColumnName="PlaEstCod")
    private PlanEstudio plan;

    @Column(name = "MdlCod")
    private Long MdlCod;
           
    @Column(name = "MatNom", length = 100)
    private String MatNom;
    @Column(name = "MatCntHor", precision=10, scale=2)
    private Double MatCntHor;    
    @Column(name = "MatTpoApr")
    private TipoAprobacion MatTpoApr;
    @Column(name = "MatTpoPer")
    private TipoPeriodo MatTpoPer;    
    @Column(name = "MatPerVal", precision=10, scale=2)
    private Double MatPerVal;
    @Column(name = "ObjFchMod", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ObjFchMod;
    
    @OneToMany(targetEntity = MateriaPrevia.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="MatCod", referencedColumnName="MatCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<MateriaPrevia> lstPrevias;
    
    @OneToMany(targetEntity = Evaluacion.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="MatEvlMatCod", referencedColumnName="MatCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<Evaluacion> lstEvaluacion;
    
   
    //-CONSTRUCTOR
    public Materia() {
        lstPrevias = new ArrayList<>();
    }
    
    
    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorno el código de la Materia
     */
    public Long getMatCod() {
        return MatCod;
    }

    /**
     *
     * @param MatCod Recibe el código de la Materia
     */
    public void setMatCod(Long MatCod) {
        this.MatCod = MatCod;
    }

    /**
     *
     * @return Retorna el plan de la Materia
     */
    public PlanEstudio getPlan() {
        return plan;
    }

    /**
     *
     * @param plan Recibe el plan de la Materia
     */
    public void setPlan(PlanEstudio plan) {
        this.plan = plan;
    }

    /**
     *
     * @return Retorno el nombre de la Materia
     */
    public String getMatNom() {
        return MatNom;
    }

    /**
     *
     * @param MatNom Recibe el nombre de la Materia
     */
    public void setMatNom(String MatNom) {
        this.MatNom = MatNom;
    }

    /**
     *
     * @return Retorna la cantidad de horas de la Materia
     */
    public Double getMatCntHor() {
        return MatCntHor;
    }

    /**
     *
     * @param MatCntHor Recibe la cantidad de horas de la Materia
     */
    public void setMatCntHor(Double MatCntHor) {
        this.MatCntHor = MatCntHor;
    }

    /**
     *
     * @return Retorna el tipo de aprobación de la Materia
     */
    public TipoAprobacion getMatTpoApr() {
        return MatTpoApr;
    }

    /**
     *
     * @param MatTpoApr Recibe el tipo de aprobación de la Materia
     */
    public void setMatTpoApr(TipoAprobacion MatTpoApr) {
        this.MatTpoApr = MatTpoApr;
    }

    /**
     *
     * @return Retorna el tipo de período de la Materia
     */
    public TipoPeriodo getMatTpoPer() {
        return MatTpoPer;
    }

    /**
     *
     * @param MatTpoPer Recibe el tipo de período de la Materia
     */
    public void setMatTpoPer(TipoPeriodo MatTpoPer) {
        this.MatTpoPer = MatTpoPer;
    }

    /**
     *
     * @return Retorna el valor del período de la Materia
     */
    public Double getMatPerVal() {
        return MatPerVal;
    }

    /**
     *
     * @param MatPerVal Recibe el tipo de período de la Materia
     */
    public void setMatPerVal(Double MatPerVal) {
        this.MatPerVal = MatPerVal;
    }

    /**
     *
     * @return Retorna la fecha de modificación de la Materia
     */
    public Date getObjFchMod() {
        return ObjFchMod;
    }

    /**
     *
     * @return Retorna el código del de moodle (Las materias se definen en moodle como SubCategorías)
     */
    public Long getMdlCod() {
        return MdlCod;
    }

    /**
     *
     * @param MdlCod Recibe el código de de moodle (Las materias se definen en moodle como SubCategorías)
     */
    public void setMdlCod(Long MdlCod) {
        this.MdlCod = MdlCod;
    }
    
    /**
     *
     * @param ObjFchMod Recibo la fecha de modificación de la materia
     */
    public void setObjFchMod(Date ObjFchMod) {
        this.ObjFchMod = ObjFchMod;
    }

    /**
     *
     * @return Retorno la lista de Previas de la materia
     */
    @JsonIgnore
    @XmlTransient
    public List<MateriaPrevia> getLstPrevias() {
        return lstPrevias;
    }

    /**
     *
     * @param lstPrevias Recibe la lista de Previas de la materia
     */
    public void setLstPrevias(List<MateriaPrevia> lstPrevias) {
        this.lstPrevias = lstPrevias;
    }

    /**
     *
     * @return Retorna la lista de Evaluaciones de la materia
     */
    @JsonIgnore
    @XmlTransient
    public List<Evaluacion> getLstEvaluacion() {
        return lstEvaluacion;
    }

    /**
     *
     * @param lstEvaluacion Recibe la lista de evaluaciones de la materia
     */
    public void setLstEvaluacion(List<Evaluacion> lstEvaluacion) {
        this.lstEvaluacion = lstEvaluacion;
    }

    /**
     *
     * @param EvaCod Recibe el código de la Evaluación
     * @return Retorna la evaluación dado el código recibido
     */
    public Evaluacion getEvaluacionById(Long EvaCod){
        
        Evaluacion pEva = new Evaluacion();
        
        for(Evaluacion eva : this.lstEvaluacion)
        {
            if(eva.getEvlCod().equals(EvaCod))
            {
                pEva = eva;
                break;
            }
        }
        
        return pEva;
    }
    
    /**
     *
     * @param MatPreCod Recibe el codigo de la materia previa
     * @return Retorna la materia previa dado el código recibido
     */
    public MateriaPrevia getPreviaById(Long MatPreCod){
        
        
        for(MateriaPrevia mat : this.lstPrevias)
        {
            if(mat.getMatPreCod().equals(MatPreCod)) return mat;
        }
        
        return null;
    }
    
    /**
     *
     * @param calificacion Recibe la calificación
     * @return Retorna si puede o no puede rendir el exámen
     */
    public boolean MateriaPuedeDarExamen(Double calificacion){
        boolean puede = false;
        switch(this.MatTpoApr)
        {
            case EXONERABLE_CON_GANANCIA:
                if(calificacion < 86 && calificacion >= 70) puede =  true;
                break;
            case EXONERABLE_SIN_GANANCIA:
                if(calificacion < 86) puede =  true;
                break;
            case NO_EXONERABLE_CON_GANANCIA: 
                if(calificacion >= 70) puede =  true;
                break;
            case NO_EXONERABLE_SIN_GANANCIA:
                puede =  true;
                break;
        }
        
        return puede;
    }
    
    /**
     *
     * @param calificacion Recibe la calificación
     * @return Retorna si Exonera o no la materia
     */
    public boolean MateriaExonera(Double calificacion){
        boolean puede = false;
        switch(this.MatTpoApr)
        {
            case EXONERABLE_CON_GANANCIA:
                if(calificacion >= 86) puede =  true;
                break;
            case EXONERABLE_SIN_GANANCIA:
                if(calificacion >= 86) puede =  true;
                break;
        }
        
        return puede;
    }
    
    /**
     *
     * @return Retorna los códigos de las materias previas
     */
    public String ObtenerPreviasCodigos(){
        List<Long> lstRetorno = new ArrayList<>();

        if(this.lstPrevias !=null)
        {
            for(MateriaPrevia matPrevia : this.lstPrevias)
            {
                lstRetorno.add(matPrevia.getMateriaPrevia().getMatCod());
            }
        }
        
        return Utilidades.GetInstancia().ObjetoToJson(lstRetorno);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.MatCod);
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
        final Materia other = (Materia) obj;
        if (!Objects.equals(this.MatCod, other.MatCod)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Materia{" + "MatCod=" + MatCod + '}';
    } 
    
    @Override
    public Long GetPrimaryKey() {
        return this.MatCod;
    }
}


