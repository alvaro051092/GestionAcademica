/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Enumerado.EstadoInconsistencia;
import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad de SincronizacionInconsistencia
 *
 * @author alvar
 */

@JsonIgnoreProperties({"sincronizacion"})

@Entity
@Table(name = "SINC_INCONSITENCIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SincronizacionInconsistencia.findAll",       query = "SELECT t FROM SincronizacionInconsistencia t")})

public class SincronizacionInconsistencia implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "IncCod", nullable = false)
    private Long  IncCod;
           
    @OneToOne(targetEntity = Sincronizacion.class)
    @JoinColumn(name="SncCod", referencedColumnName="SncCod")
    private Sincronizacion sincronizacion;
    
    @Column(name = "IncEst")
    private EstadoInconsistencia IncEst;
    
    @ManyToOne(targetEntity = Objeto.class)
    @JoinColumn(name="ObjNom", referencedColumnName="ObjNom")
    private Objeto objeto;
    
    @OneToMany(targetEntity = SincInconsistenciaDatos.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="IncCod", referencedColumnName="IncCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<SincInconsistenciaDatos> lstDatos;
    
    //-CONSTRUCTOR
    public SincronizacionInconsistencia() {
    }

    /**
     *
     * @param objeto
     */
    public SincronizacionInconsistencia(Objeto objeto) {
        this.objeto = objeto;
    }
    
    
    
    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorna el código de SincronizacionInconsistencia
     */
    public Long getIncCod() {
        return IncCod;
    }

    /**
     *
     * @param IncCod Recibe el código de SincronizacionInconsistencia
     */
    public void setIncCod(Long IncCod) {
        this.IncCod = IncCod;
    }

    /**
     *
     * @return Retorna la Sincronización
     */
    @XmlTransient
    public Sincronizacion getSincronizacion() {
        return sincronizacion;
    }

    /**
     *
     * @param sincronizacion Recibe la Sincronización
     */
    public void setSincronizacion(Sincronizacion sincronizacion) {
        this.sincronizacion = sincronizacion;
    }

    /**
     *
     * @return Retorna el estado de la Sincronización
     */
    public EstadoInconsistencia getIncEst() {
        return IncEst;
    }

    /**
     *
     * @param IncEst Recibe el estado de la Sincronización
     */
    public void setIncEst(EstadoInconsistencia IncEst) {
        this.IncEst = IncEst;
    }

    /**
     *
     * @return Retorna la lista de datos de la Sincronización
     */
    public List<SincInconsistenciaDatos> getLstDatos() {
        if(this.lstDatos == null)
        {
            lstDatos = new ArrayList<>();
        }
        return lstDatos;
    }

    /**
     *
     * @param lstDatos Recibe la lista de datos de la Sincronización
     */
    public void setLstDatos(List<SincInconsistenciaDatos> lstDatos) {
        this.lstDatos = lstDatos;
    }

    /**
     *
     * @return Retorna el objeto
     */
    public Objeto getObjeto() {
        return objeto;
    }

    /**
     *
     * @param objeto Recibe el objeto
     */
    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }
    
    /**
     *
     * @param IncObjCod Recibe el código de Objeto
     * @return retorna el dato inconsistente
     */
    public SincInconsistenciaDatos GetIncDato(Long IncObjCod){
        for(SincInconsistenciaDatos dat : lstDatos)
        {
            if(dat.getIncObjCod().equals(IncObjCod))
            {
                return dat;
            }
        }

        return  null;
    }
    
    /**
     *
     * @return Retorna el objeto seleccionado
     */
    public SincInconsistenciaDatos getObjetoSeleccionado(){
        for(SincInconsistenciaDatos dat : lstDatos)
        {
            if(dat.getObjSel())
            {
                return dat;
            }
        }

        return  null;
    }

    /**
     *
     * @return Retorna si tiene objeto seleccionado o no
     */
    public Boolean TieneObjetoSeleccionado(){
        for(SincInconsistenciaDatos dat : lstDatos)
        {
            if(dat.getObjSel())
            {
                return true;
            }
        }

        return  false;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.IncCod);
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
        final SincronizacionInconsistencia other = (SincronizacionInconsistencia) obj;
        if (!Objects.equals(this.IncCod, other.IncCod)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SincronizacionInconsistencia{" + "IncCod=" + IncCod + ", IncEst=" + IncEst + ", lstDatos=" + lstDatos + '}';
    }

    

    
}

