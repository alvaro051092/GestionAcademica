/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Enumerado.EstadoServicio;
import Enumerado.ServicioWeb;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/** 
 * Entidad WS_Bit
 *
 * @author alvar
 */
@Entity
@Table(name = "WS_BIT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WS_Bit.findAll",       query = "SELECT t FROM WS_Bit t order by t.WsBitFch desc"),
    @NamedQuery(name = "WS_Bit.findByBeforeDate",    query = "SELECT t FROM WS_Bit t WHERE t.WsBitFch <= :WsBitFch")})

public class WS_Bit implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "WsBitCod", nullable = false)
    private Long WsBitCod;
    
    @OneToOne(targetEntity = WS_User.class)
    @JoinColumn(name="WsUsrCod", referencedColumnName="WsUsrCod")
    private WS_User usuario;
    
    @Column(name = "WsSrv")
    private ServicioWeb WsSrv;
    
    @Column(name = "WsBitFch", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date WsBitFch;
    
    @Column(name = "WsBitEst")
    private EstadoServicio WsBitEst;
    
    @Column(name = "WsBitDet", length = 4000)
    private String WsBitDet;
    
    
    //-CONSTRUCTOR
    public WS_Bit() {    
    }

    public WS_Bit(WS_User usuario, ServicioWeb WsSrv, Date WsBitFch, EstadoServicio WsBitEst, String WsBitDet) {
        this.usuario = usuario;
        this.WsSrv = WsSrv;
        this.WsBitFch = WsBitFch;
        this.WsBitEst = WsBitEst;
        this.WsBitDet = WsBitDet;
    }
    
    

    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorna el código de WS_Bit
     */
    public Long getWsBitCod() {
        return WsBitCod;
    }

    /**
     *
     * @param WsBitCod Recibe el código de WS_Bit
     */
    public void setWsBitCod(Long WsBitCod) {
        this.WsBitCod = WsBitCod;
    }

    /**
     *
     * @return Retorna el usuario de WS_Bit
     */
    public WS_User getUsuario() {
        return usuario;
    }

    /**
     *
     * @param usuario recibe el usuario de WS_Bit
     */
    public void setUsuario(WS_User usuario) {
        this.usuario = usuario;
    }

    /**
     *
     * @return
     */
    public ServicioWeb getWsSrv() {
        return WsSrv;
    }

    /**
     *
     * @param WsSrv
     */
    public void setWsSrv(ServicioWeb WsSrv) {
        this.WsSrv = WsSrv;
    }

    /**
     *
     * @return Retorna la fecha de WS_Bit
     */
    public Date getWsBitFch() {
        return WsBitFch;
    }

    /**
     *
     * @param WsBitFch Recibe la fecha de WS_Bit
     */
    public void setWsBitFch(Date WsBitFch) {
        this.WsBitFch = WsBitFch;
    }

    /**
     *
     * @return retorna el estado de WS_Bit
     */
    public EstadoServicio getWsBitEst() {
        return WsBitEst;
    }

    /**
     *
     * @param WsBitEst Recibe el estado de WS_Bit
     */
    public void setWsBitEst(EstadoServicio WsBitEst) {
        this.WsBitEst = WsBitEst;
    }

    /**
     *
     * @return 
     */
    public String getWsBitDet() {
        return WsBitDet;
    }

    /**
     *
     * @param WsBitDet
     */
    public void setWsBitDet(String WsBitDet) {
        this.WsBitDet = WsBitDet;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (WsBitCod != null ? WsBitCod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WS_Bit)) {
            return false;
        }
        WS_Bit other = (WS_Bit) object;
        if ((this.WsBitCod == null && other.WsBitCod != null) || (this.WsBitCod != null && !this.WsBitCod.equals(other.WsBitCod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidad.WS_Bit[ id=" + WsBitCod + " ]";
    }
    
}
