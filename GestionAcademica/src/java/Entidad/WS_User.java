/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad WS_User
 *
 * @author alvar
 */
@Entity
@Table(name = "WS_USER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WS_User.findByUsrName",       query = "SELECT t FROM WS_User t WHERE t.WsUsr =:WsUsr"),
    @NamedQuery(name = "WS_User.findAll",       query = "SELECT t FROM WS_User t")})

public class WS_User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //-ATRIBUTOS
    
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "WsUsrCod", nullable = false)
    private Long WsUsrCod;
        
    @Column(name = "WsUsr", length = 100)
    private String WsUsr;
    
    @Column(name = "WsUsrPsw", length = 500)
    private String WsUsrPsw;
    
    //----------------------------------------------------------------------
    @OneToMany(targetEntity = WS_UserServicio.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="WsUsrCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<WS_UserServicio> lstServicio;
    
    @OneToMany(targetEntity = WS_Bit.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="WsUsrCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<WS_Bit> lstBitacora;
    //----------------------------------------------------------------------
    
    
    //-CONSTRUCTOR
    public WS_User() {
        this.lstBitacora = new ArrayList<>();
        this.lstServicio = new ArrayList<>();
    }
    
    
    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorna el código de WS_User
     */
    public Long getWsUsrCod() {
        return WsUsrCod;
    }

    /**
     *
     * @param WsUsrCod Recibe el código de WS_User
     */
    public void setWsUsrCod(Long WsUsrCod) {
        this.WsUsrCod = WsUsrCod;
    }

    /**
     *
     * @return Retorno el nick de usuario
     */
    public String getWsUsr() {
        return WsUsr;
    }

    /**
     *
     * @param WsUsr Recibe el nick de usuario
     */
    public void setWsUsr(String WsUsr) {
        this.WsUsr = WsUsr;
    }

    /**
     *
     * @return Retorno el password
     */
    public String getWsUsrPsw() {
        return WsUsrPsw;
    }

    /**
     *
     * @param WsUsrPsw Recibe el password
     */
    public void setWsUsrPsw(String WsUsrPsw) {
        this.WsUsrPsw = WsUsrPsw;
    }

    /**
     *
     * @return Retorna la lista de servicio
     */
    @XmlTransient
    @JsonIgnore
    public List<WS_UserServicio> getLstServicio() {
        return lstServicio;
    }

    /**
     *
     * @param lstServicio Recibe la lista de servicio
     */
    public void setLstServicio(List<WS_UserServicio> lstServicio) {
        this.lstServicio = lstServicio;
    }

    /**
     *
     * @return Retorna la lista de Bitacora
     */
    @XmlTransient
    @JsonIgnore
    public List<WS_Bit> getLstBitacora() {
        return lstBitacora;
    }

    /**
     *
     * @param lstBitacora Recibe la lista de Bitacora
     */
    public void setLstBitacora(List<WS_Bit> lstBitacora) {
        this.lstBitacora = lstBitacora;
    }

    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (WsUsrCod != null ? WsUsrCod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WS_User)) {
            return false;
        }
        WS_User other = (WS_User) object;
        if ((this.WsUsrCod == null && other.WsUsrCod != null) || (this.WsUsrCod != null && !this.WsUsrCod.equals(other.WsUsrCod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidad.WS_User[ id=" + WsUsrCod + " ]";
    }
    
}
