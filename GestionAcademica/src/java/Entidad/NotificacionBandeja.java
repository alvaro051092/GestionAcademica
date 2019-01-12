/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Enumerado.BandejaEstado;
import Enumerado.BandejaTipo;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad NotificacionBandeja
 *
 * @author alvar
 */
@Entity
@Table(name = "NOTIFICACION_BANDEJA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotificacionBandeja.findByTpoEst",       query = "SELECT t FROM NotificacionBandeja t WHERE t.destinatario.PerCod =:PerCod AND t.NotBanTpo =:NotBanTpo AND t.NotBanEst =:NotBanEst ORDER BY t.NotBanFch DESC"),
    @NamedQuery(name = "NotificacionBandeja.findAll",       query = "SELECT t FROM NotificacionBitacora t")
})

public class NotificacionBandeja implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "NotBanCod", nullable = false)
    private Long NotBanCod;
    
    @ManyToOne(targetEntity = Persona.class)
    @JoinColumn(name="NotBanPerCod", referencedColumnName="PerCod")
    private Persona destinatario;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "NotBanTpo")
    private BandejaTipo NotBanTpo;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "NotBanEst")
    private BandejaEstado NotBanEst;

    @Column(name = "NotBanAsu", length = 4000)
    private String NotBanAsu;
    
    @Column(name = "NotBanMen", length = 4000)
    private String NotBanMen;
    
    @Column(name = "NotBanFch", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date NotBanFch;
    

    /**
     *
     * @return Retorna el código de NotificacionBandeja
     */
    public Long getNotBanCod() {
        return NotBanCod;
    }

    /**
     *
     * @param NotBanCod Recibe el código de NotificacionBandeja
     */
    public void setNotBanCod(Long NotBanCod) {
        this.NotBanCod = NotBanCod;
    }

    /**
     *
     * @return Retorna el destinatario
     */
    public Persona getDestinatario() {
        return destinatario;
    }

    /**
     *
     * @param destinatario Recibe el destinatario
     */
    public void setDestinatario(Persona destinatario) {
        this.destinatario = destinatario;
    }

    /**
     *
     * @return Retorna el tipo de NotificacionBandeja
     */
    public BandejaTipo getNotBanTpo() {
        return NotBanTpo;
    }

    /**
     *
     * @param NotBanTpo Recibe el tipo de NotificacionBandeja
     */
    public void setNotBanTpo(BandejaTipo NotBanTpo) {
        this.NotBanTpo = NotBanTpo;
    }

    /**
     *
     * @return Retorna el estado de la NotificacionBandeja
     */
    public BandejaEstado getNotBanEst() {
        return NotBanEst;
    }

    /**
     *
     * @param NotBanEst Recibe el estado de la NotificacionBandeja
     */
    public void setNotBanEst(BandejaEstado NotBanEst) {
        this.NotBanEst = NotBanEst;
    }

    /**
     *
     * @return Retorna el asunto de NotificacionBandeja
     */
    public String getNotBanAsu() {
        if(NotBanAsu != null) NotBanAsu = NotBanAsu.replace("\"", "'");
        return NotBanAsu;
    }

    /**
     *
     * @param NotBanAsu Recibe el asunto de NotificacionBandeja
     */
    public void setNotBanAsu(String NotBanAsu) {
        this.NotBanAsu = NotBanAsu;
    }

    /**
     *
     * @return Retorna el mensaje de NotificacionBandeja
     */
    public String getNotBanMen() {
        return NotBanMen;
    }

    /**
     *
     * @param NotBanMen Recibe el mensaje de NotificacionBandeja
     */
    public void setNotBanMen(String NotBanMen) {
        this.NotBanMen = NotBanMen;
    }

    /**
     *
     * @return Retorna la fecha de NotificacionBandeja
     */
    public Date getNotBanFch() {
        return NotBanFch;
    }

    /**
     *
     * @param NotBanFch Recibe la fecha de NotificacionBandeja
     */
    public void setNotBanFch(Date NotBanFch) {
        this.NotBanFch = NotBanFch;
    }

    /**
     *
     * @param longitud
     * @return
     */
    public String getAsuntoRecortado(int longitud){
        if(this.getNotBanAsu().length() > longitud)
        {
            return this.getNotBanAsu().substring(0, longitud) + "...";
        }
        
        return this.getNotBanAsu();
    }
    
    //-----------------------------------------------------------------
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (NotBanCod != null ? NotBanCod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificacionBandeja)) {
            return false;
        }
        NotificacionBandeja other = (NotificacionBandeja) object;
        if ((this.NotBanCod == null && other.NotBanCod != null) || (this.NotBanCod != null && !this.NotBanCod.equals(other.NotBanCod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidad.NotificacionBandeja[ NotBanCod=" + NotBanCod + " ]";
    }
    
}
