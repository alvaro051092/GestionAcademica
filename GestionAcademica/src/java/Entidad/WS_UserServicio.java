/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Enumerado.ServicioWeb;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad WS_UserServicio
 *
 * @author alvar
 */
@Entity
@Table(name = "WS_USERS_SERVICIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WS_UserServicio.findAll",       query = "SELECT t FROM WS_UserServicio t")})

public class WS_UserServicio implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //-ATRIBUTOS 
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "WsSrvCod", nullable = false)
    private Long WsSrvCod;   
    
    @OneToOne(targetEntity = WS_User.class)
    @JoinColumn(name="WsUsrCod", referencedColumnName="WsUsrCod")
    private WS_User usuario;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "WsSrv")
    private ServicioWeb WsSrv;

    //-CONSTRUCTORES
    public WS_UserServicio() {
    }

    public WS_UserServicio(WS_User usuario, ServicioWeb WsSrv) {
        this.usuario = usuario;
        this.WsSrv = WsSrv;
    }
    
    
    
    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorna el código de WS_UserServicio
     */

    public Long getWsSrvCod() {
        return WsSrvCod;
    }

    /**
     *
     * @param WsSrvCod Recibe el código de WS_UserServicio
     */
    public void setWsSrvCod(Long WsSrvCod) {
        this.WsSrvCod = WsSrvCod;
    }

    /**
     *
     * @return Retorna el usuario
     */
    public WS_User getUsuario() {
        return usuario;
    }

    /**
     *
     * @param usuario Recibe el usuario
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.WsSrvCod);
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
        final WS_UserServicio other = (WS_UserServicio) obj;
        if (!Objects.equals(this.WsSrvCod, other.WsSrvCod)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WS_UserServicio{" + "WsSrvCod=" + WsSrvCod + ", usuario=" + usuario + ", WsSrv=" + WsSrv + '}';
    }

    
    
    
    
}



