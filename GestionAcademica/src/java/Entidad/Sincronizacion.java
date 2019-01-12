/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Enumerado.EstadoSincronizacion;
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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad Sincronización
 *
 * @author alvar
 */
@Entity
@Table(name = "SINCRONIZACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sincronizacion.findByEst",     query = "SELECT t FROM Sincronizacion t where t.SncEst = :SncEst"),
    @NamedQuery(name = "Sincronizacion.findAll",       query = "SELECT t FROM Sincronizacion t ORDER BY t.SncFch DESC")})

public class Sincronizacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "SncCod", nullable = false)
    private Long SncCod;
    
    @Column(name = "SncFch", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date SncFch;
    
    @Column(name = "SncEst")
    private EstadoSincronizacion SncEst;

    @Column(name = "SncObjCnt")
    private Integer SncObjCnt;

    @Column(name = "SncObjDet", length = 500)
    private String SncObjDet;

    @Column(name = "SncDur", length = 100)
    private String SncDur;
    
    @OneToMany(targetEntity = SincronizacionInconsistencia.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="SncCod", referencedColumnName="SncCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<SincronizacionInconsistencia> lstInconsistencia;
    

    //-CONSTRUCTOR
    public Sincronizacion() {
        this.lstInconsistencia = new ArrayList<>();
    }
    
    //-GETTERS Y SETTERS

    /**
     *
     * @return retorna el código de la Sincronización
     */
    public Long getSncCod() {
        return SncCod;
    }

    /**
     *
     * @param SncCod Recibe el código de la Sincronización
     */
    public void setSncCod(Long SncCod) {
        this.SncCod = SncCod;
    }

    /**
     *
     * @return Retorna la fecha de Sincronización
     */
    public Date getSncFch() {
        return SncFch;
    }

    /**
     *
     * @param SncFch recibe la fecha de Sincronización
     */
    public void setSncFch(Date SncFch) {
        this.SncFch = SncFch;
    }

    /**
     *
     * @return Retorna el estado de Sincronización
     */
    public EstadoSincronizacion getSncEst() {
        return SncEst;
    }

    /**
     *
     * @param SncEst Recibe el estado de Sincronización
     */
    public void setSncEst(EstadoSincronizacion SncEst) {
        this.SncEst = SncEst;
    }

    /**
     *
     * @return Retorna la cantidad de objetos sincronizados
     */
    public Integer getSncObjCnt() {
        return SncObjCnt;
    }

    /**
     *
     * @param SncObjCnt Recibe la cantidad de objetos sincronizados
     */
    public void setSncObjCnt(Integer SncObjCnt) {
        this.SncObjCnt = SncObjCnt;
    }

    /**
     *
     * @return retorna el detalle de los objetos sincronizados
     */
    public String getSncObjDet() {
        return SncObjDet;
    }

    /**
     *
     * @param SncObjDet Recibe el detalle de los objetos sincronizados
     */
    public void setSncObjDet(String SncObjDet) {
        this.SncObjDet = SncObjDet;
    }

    /**
     *
     * @return Retorna la duración del proceso de Sincronización
     */
    public String getSncDur() {
        return SncDur;
    }

    /**
     *
     * @param SncDur Recibe la duración del proceso de Sincronización
     */
    public void setSncDur(String SncDur) {
        this.SncDur = SncDur;
    }

    /**
     *
     * @return Retorna la lista de inconsistencias
     */
    public List<SincronizacionInconsistencia> getLstInconsistencia() {
        if(this.lstInconsistencia == null)
        {
            lstInconsistencia = new ArrayList<>();
        }
        return lstInconsistencia;
    }

    /**
     *
     * @param lstInconsistencia Recibe la lista de inconsistencias
     */
    public void setLstInconsistencia(List<SincronizacionInconsistencia> lstInconsistencia) {
        this.lstInconsistencia = lstInconsistencia;
    }

    /**
     *
     * @param msg Setea el detalle
     */
    public void addDetalle(String msg){
        if(this.SncObjDet == null)
        {
            this.SncObjDet = "";
        }
        
        this.SncObjDet += msg + "\n";
    }
    
    /**
     *
     * @param IncCod Recibe el código de inconsistencia
     * @return retora la inconsistencia dado el codigo recibido
     */
    public SincronizacionInconsistencia GetInconsistencia(Long IncCod){
        for(SincronizacionInconsistencia inc : lstInconsistencia)
        {
            if(inc.getIncCod().equals(IncCod))
            {
                return inc;
            }
        }

        return  null;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (SncCod != null ? SncCod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sincronizacion)) {
            return false;
        }
        Sincronizacion other = (Sincronizacion) object;
        if ((this.SncCod == null && other.SncCod != null) || (this.SncCod != null && !this.SncCod.equals(other.SncCod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Sincronizacion{" + "SncCod=" + SncCod + ", SncFch=" + SncFch + ", SncEst=" + SncEst + ", SncObjCnt=" + SncObjCnt + ", SncObjDet=" + SncObjDet + ", SncDur=" + SncDur + ", lstInconsistencia=" + lstInconsistencia + '}';
    }


    
}
