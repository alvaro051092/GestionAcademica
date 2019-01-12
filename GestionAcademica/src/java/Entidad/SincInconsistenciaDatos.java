/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad SincInconsistenciaDatos
 *
 * @author alvar
 */

@JsonIgnoreProperties({"inconsistencia"})

@Entity
@Table(name = "SINC_INCONSITENCIA_DATOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SincInconsistenciaDatos.findAll",       query = "SELECT t FROM SincInconsistenciaDatos t")})

public class SincInconsistenciaDatos implements Serializable {

    private static final long serialVersionUID = 1L;
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "IncObjCod", nullable = false)
    private Long IncObjCod;
    
    @OneToOne(targetEntity = SincronizacionInconsistencia.class)
    @JoinColumn(name="IncCod", referencedColumnName="IncCod")
    private SincronizacionInconsistencia inconsistencia;
    
    @Column(name = "ObjVal", length = 4000)
    private String ObjVal;
    
    @Column(name = "ObjSel")
    private Boolean ObjSel;
    
    //-CONSTRUCTOR
    public SincInconsistenciaDatos() {
        this.ObjSel = Boolean.FALSE;
    }

    public SincInconsistenciaDatos(SincronizacionInconsistencia inconsistencia, String ObjVal) {
        this.inconsistencia = inconsistencia;
        this.ObjVal = ObjVal;
        this.ObjSel = Boolean.FALSE;
    }
    
    
    
    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorna el código de SincInconsistenciaDatos
     */
    public Long getIncObjCod() {
        return IncObjCod;
    }

    /**
     *
     * @param IncObjCod recibe el código de SincInconsistenciaDatos
     */
    public void setIncObjCod(Long IncObjCod) {
        this.IncObjCod = IncObjCod;
    }

    /**
     *
     * @return Retorna la inconsistencia
     */
    @XmlTransient
    public SincronizacionInconsistencia getInconsistencia() {
        return inconsistencia;
    }

    /**
     *
     * @param inconsistencia Recibe la inconsistencia
     */
    public void setInconsistencia(SincronizacionInconsistencia inconsistencia) {
        this.inconsistencia = inconsistencia;
    }

    /**
     *
     * @return Retorna el valor de objeto
     */
    public String getObjVal() {
        return ObjVal;
    }

    /**
     *
     * @param ObjVal Recibe el valor de Objeto
     */
    public void setObjVal(String ObjVal) {
        this.ObjVal = ObjVal;
    }

    /**
     *
     * @return Retorna el seleccionado
     */
    public Boolean getObjSel() {
        return ObjSel;
    }

    /**
     *
     * @param ObjSel Recibe el objeto seleccionado
     */
    public void setObjSel(Boolean ObjSel) {
        this.ObjSel = ObjSel;
    }

    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.IncObjCod);
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
        final SincInconsistenciaDatos other = (SincInconsistenciaDatos) obj;
        if (!Objects.equals(this.IncObjCod, other.IncObjCod)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SincInconsistenciaDatos{" + "IncObjCod=" + IncObjCod + ", ObjVal=" + ObjVal + '}';
    }

    

}

