/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Dominio.SincHelper;
import Enumerado.TipoPeriodo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
 * Entidad Período
 * 
 * @author alvar
 */


@Entity
@Table(name = "PERIODO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Periodo.findAll",       query = "SELECT t FROM Periodo t"),
    @NamedQuery(name = "Periodo.findModAfter",  query = "SELECT t FROM Periodo t  WHERE t.ObjFchMod >= :ObjFchMod"),
    @NamedQuery(name = "Periodo.findByPK",      query = "SELECT t FROM Periodo t WHERE t.PeriCod =:PeriCod"),
    @NamedQuery(name = "Periodo.findLastByMat", query = "SELECT t FROM Periodo t, PeriodoEstudio e WHERE t.PeriCod = e.periodo.PeriCod and e.Materia.MatCod =:MatCod order by t.PerFchIni desc"),
    @NamedQuery(name = "Periodo.findLastByMod", query = "SELECT t FROM Periodo t, PeriodoEstudio e WHERE t.PeriCod = e.periodo.PeriCod and e.Modulo.ModCod =:ModCod order by t.PerFchIni desc"),
    @NamedQuery(name = "Periodo.findLast",      query = "SELECT t FROM Periodo t ORDER BY t.PerFchIni DESC")})

public class Periodo extends SincHelper implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "PeriCod", nullable = false)
    private Long PeriCod;
    
    @Column(name = "PerTpo")
    private TipoPeriodo PerTpo;

    @Column(name = "PerVal", precision=10, scale=2)
    private Double PerVal;

    @Column(name = "PerFchIni", columnDefinition="DATE")
    @Temporal(TemporalType.DATE)
    private Date PerFchIni;

    @Column(name = "ObjFchMod", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ObjFchMod;
    
    
    //----------------------------------------------------------------------
    @OneToMany(targetEntity = PeriodoEstudio.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="PeriCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<PeriodoEstudio> lstEstudio;
    //----------------------------------------------------------------------
    
    //-CONSTRUCTOR
    public Periodo() {
        this.lstEstudio = new ArrayList<>();
    }
    
    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorno el código del período
     */
    public Long getPeriCod() {
        return PeriCod;
    }

    /**
     *
     * @param PeriCod Recibe el código del período
     */
    public void setPeriCod(Long PeriCod) {
        this.PeriCod = PeriCod;
    }

    /**
     *
     * @return Retorno el tipo de período
     */
    public TipoPeriodo getPerTpo() {
        return PerTpo;
    }

    /**
     *
     * @param PerTpo Recibo el tipo de período
     */
    public void setPerTpo(TipoPeriodo PerTpo) {
        this.PerTpo = PerTpo;
    }

    /**
     *
     * @return Retorno el valor del período
     */
    public Double getPerVal() {
        return PerVal;
    }

    /**
     *
     * @param PerVal Recibo el valor del período
     */
    public void setPerVal(Double PerVal) {
        this.PerVal = PerVal;
    }

    /**
     *
     * @return Retorno la fecha de inicio del período
     */
    public Date getPerFchIni() {
        return PerFchIni;
    }

    /**
     *
     * @param PerFchIni Recibo la fecha de inicio del período
     */
    public void setPerFchIni(Date PerFchIni) {
        this.PerFchIni = PerFchIni;
    }

    /**
     *
     * @return Retorno la fecha de modificación del período
     */
    public Date getObjFchMod() {
        return ObjFchMod;
    }

    /**
     *
     * @param ObjFchMod Recibo la fecha de modificación del período
     */
    public void setObjFchMod(Date ObjFchMod) {
        this.ObjFchMod = ObjFchMod;
    }

    /**
     *
     * @return Retorno la lista de estudios
     */
    @JsonIgnore
    @XmlTransient
    public List<PeriodoEstudio> getLstEstudio() {
        return lstEstudio;
    }

    /**
     *
     * @param lstEstudio Recibe la lista de estudios
     */
    public void setLstEstudio(List<PeriodoEstudio> lstEstudio) {
        this.lstEstudio = lstEstudio;
    }
    
    /**
     *
     * @return Retorna el nombre del tipo de período
     */
    public String getPerTpoNombre() {
        return PerTpo.getTipoPeriodoNombre();
    }
    
    /**
     *
     * @param ModCod Recibo el código de módulo
     * @return Retorna si el período posee módulo o no dado el código recibido
     */
    public Boolean PeriodoPoseeModulo(Long ModCod)
    {
        for(PeriodoEstudio est : this.lstEstudio)
        {
            if(est.getModulo() != null) if(est.getModulo().getModCod().equals(ModCod)) return true;
        }
        return false;
    }
    
    /**
     *
     * @param MatCod Recibe el código de de la materia
     * @return Retorna si el período posee materia dado el código recibido
     */
    public Boolean PeriodoPoseeMateria(Long MatCod)
    {
        for(PeriodoEstudio est : this.lstEstudio)
        {
            if(est.getMateria() != null) if(est.getMateria().getMatCod().equals(MatCod)) return true;
        }
        return false;
    }

    /**
     *
     * @return Retorna TextoPeríodo con PerVal
     */
    public String TextoPeriodo(){
        return this.getPerTpoNombre() + ": " + this.PerVal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (PeriCod != null ? PeriCod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Periodo)) {
            return false;
        }
        Periodo other = (Periodo) object;
        if ((this.PeriCod == null && other.PeriCod != null) || (this.PeriCod != null && !this.PeriCod.equals(other.PeriCod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidad.Periodo[ id=" + PeriCod + " ]";
    }
    
    @Override
    public Long GetPrimaryKey() {
        return this.PeriCod;
    }
}
