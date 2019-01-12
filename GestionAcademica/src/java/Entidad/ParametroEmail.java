/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Enumerado.ProtocoloEmail;
import Enumerado.TipoAutenticacion;
import Enumerado.TipoSSL;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 *  Entidad ParámetroEmail
 *
 * @author alvar
 */
@Entity
@Table(name = "PARAMETRO_EMAIL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParametroEmail.findAll",       query = "SELECT t FROM ParametroEmail t"),
    @NamedQuery(name = "ParametroEmail.findLast",      query = "SELECT t FROM ParametroEmail t ORDER BY t.ParEmlCod DESC")})

public class ParametroEmail implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "ParEmlCod", nullable = false)
    private Long ParEmlCod;
    
    @Column(name = "ParEmlNom", length = 100)
    private String ParEmlNom;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "ParEmlPro")
    private ProtocoloEmail ParEmlPro;
    
    @Column(name = "ParEmlSrv", length = 255)
    private String ParEmlSrv;
    
    @Column(name = "ParEmlPrt")
    private Integer ParEmlPrt;

    @Column(name = "ParEmlDeNom", length = 100)
    private String ParEmlDeNom;

    @Column(name = "ParEmlDeEml", length = 255)
    private String ParEmlDeEml;

    @Column(name = "ParEmlUtlAut")
    private Boolean ParEmlUtlAut;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "ParEmlTpoAut")
    private TipoAutenticacion ParEmlTpoAut;  

    @Column(name = "ParEmlDom", length = 255)
    private String ParEmlDom;

    @Column(name = "ParEmlUsr", length = 255)
    private String ParEmlUsr;

    @Column(name = "ParEmlPsw", length = 500)
    private String ParEmlPsw;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "ParEmlSSL")
    private TipoSSL ParEmlSSL;  

    @Column(name = "ParEmlTmpEsp")
    private Integer ParEmlTmpEsp;  

    @Column(name = "ParEmlDebug")
    private Boolean ParEmlDebug;
    
    @Column(name = "ParEmlReqConf")
    private Boolean ParEmlReqConf;

    //-CONSTRUCTOR
    public ParametroEmail() {
        this.ParEmlTpoAut   = TipoAutenticacion.NORMAL;
        this.ParEmlSSL      = TipoSSL.SSL;
    }

    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorna el Código de Parámetro envío Email
     */
    public Long getParEmlCod() {
        return ParEmlCod;
    }

    /**
     *
     * @param ParEmlCod Recibe Código de Parámetro envío Email
     */
    public void setParEmlCod(Long ParEmlCod) {
        this.ParEmlCod = ParEmlCod;
    }

    /**
     *
     * @return retorna Nombre de Parámetro envío Email
     */
    public String getParEmlNom() {
        return ParEmlNom;
    }

    /**
     *
     * @param ParEmlNom Recibe Nombre de Parámetro envío Email
     */
    public void setParEmlNom(String ParEmlNom) {
        this.ParEmlNom = ParEmlNom;
    }

    /**
     *
     * @return Retorna el Protocolo de Parámetro envío Email
     */
    public ProtocoloEmail getParEmlPro() {
        return ParEmlPro;
    }

    /**
     *
     * @param ParEmlPro Recibo el Protocolo de Parámetro envío Email
     */
    public void setParEmlPro(ProtocoloEmail ParEmlPro) {
        this.ParEmlPro = ParEmlPro;
    }

    /**
     *
     * @return Retorna Servidor - Host de Parámetro envío Email
     */
    public String getParEmlSrv() {
        return ParEmlSrv;
    }

    /**
     *
     * @param ParEmlSrv Recibe Servidor - Host de Parámetro envío Email
     */
    public void setParEmlSrv(String ParEmlSrv) {
        this.ParEmlSrv = ParEmlSrv;
    }

    /**
     *
     * @return Retorna Puerto de Parámetro envío Email
     */
    public Integer getParEmlPrt() {
        return ParEmlPrt;
    }

    /**
     *
     * @param ParEmlPrt Recibe el Puerto de Parámetro envío Email
     */
    public void setParEmlPrt(Integer ParEmlPrt) {
        this.ParEmlPrt = ParEmlPrt;
    }

    /**
     *
     * @return Retorna el Remitente nombre de Parámetro envío Email
     */
    public String getParEmlDeNom() {
        return ParEmlDeNom;
    }

    /**
     *
     * @param ParEmlDeNom Recibe el Remitente nombre de Parámetro envío Email
     */
    public void setParEmlDeNom(String ParEmlDeNom) {
        this.ParEmlDeNom = ParEmlDeNom;
    }

    /**
     *
     * @return Retorno el Remitente email de Parámetro envío Email
     */
    public String getParEmlDeEml() {
        return ParEmlDeEml;
    }

    /**
     *
     * @param ParEmlDeEml Recibe el Remitente email de Parámetro envío Email
     */
    public void setParEmlDeEml(String ParEmlDeEml) {
        this.ParEmlDeEml = ParEmlDeEml;
    }

    /**
     *
     * @return Retorna Utiliza autenticacion
     */
    public Boolean getParEmlUtlAut() {
        return ParEmlUtlAut;
    }

    /**
     *
     * @param ParEmlUtlAut Recibe Utiliza autenticacion
     */
    public void setParEmlUtlAut(Boolean ParEmlUtlAut) {
        this.ParEmlUtlAut = ParEmlUtlAut;
    }

    /**
     *
     * @return Retorna Tipo de autenticacion
     */
    public TipoAutenticacion getParEmlTpoAut() {
        return ParEmlTpoAut;
    }

    /**
     *
     * @param ParEmlTpoAut Recibe Tipo de autenticacion
     */
    public void setParEmlTpoAut(TipoAutenticacion ParEmlTpoAut) {
        this.ParEmlTpoAut = ParEmlTpoAut;
    }

    /**
     *
     * @return Retorna Dominio
     */
    public String getParEmlDom() {
        return ParEmlDom;
    }

    /**
     *
     * @param ParEmlDom Recibe Dominio
     */
    public void setParEmlDom(String ParEmlDom) {
        this.ParEmlDom = ParEmlDom;
    }

    /**
     *
     * @return Retorna Usuario
     */
    public String getParEmlUsr() {
        return ParEmlUsr;
    }

    /**
     *
     * @param ParEmlUsr Recibe Usuario
     */
    public void setParEmlUsr(String ParEmlUsr) {
        this.ParEmlUsr = ParEmlUsr;
    }

    /**
     *
     * @return Retorna Contraseña
     */
    public String getParEmlPsw() {
        return ParEmlPsw;
    }

    /**
     *
     * @param ParEmlPsw Recibe Contraseña
     */
    public void setParEmlPsw(String ParEmlPsw) {
        this.ParEmlPsw = ParEmlPsw;
    }

    /**
     *
     * @return Retorna Tipo de SSL
     */
    public TipoSSL getParEmlSSL() {
        return ParEmlSSL;
    }

    /**
     *
     * @param ParEmlSSL Recibe Tipo de SSL
     */
    public void setParEmlSSL(TipoSSL ParEmlSSL) {
        this.ParEmlSSL = ParEmlSSL;
    }

    /**
     *
     * @return Retorna Tiempo de espera
     */
    public Integer getParEmlTmpEsp() {
        return ParEmlTmpEsp;
    }

    /**
     *
     * @param ParEmlTmpEsp Recibe Tiempo de espera
     */
    public void setParEmlTmpEsp(Integer ParEmlTmpEsp) {
        this.ParEmlTmpEsp = ParEmlTmpEsp;
    }

    /**
     *
     * @return Retorna Debug
     */
    public Boolean getParEmlDebug() {
        return ParEmlDebug;
    }

    /**
     *
     * @param ParEmlDebug Recibe Debug
     */
    public void setParEmlDebug(Boolean ParEmlDebug) {
        this.ParEmlDebug = ParEmlDebug;
    }

    /**
     *
     * @return Retorna Solicitar confirmacion
     */
    public Boolean getParEmlReqConf() {
        return ParEmlReqConf;
    }

    /**
     *
     * @param ParEmlReqConf Recibe Solicitar confirmacion
     */
    public void setParEmlReqConf(Boolean ParEmlReqConf) {
        this.ParEmlReqConf = ParEmlReqConf;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ParEmlCod != null ? ParEmlCod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParametroEmail)) {
            return false;
        }
        ParametroEmail other = (ParametroEmail) object;
        if ((this.ParEmlCod == null && other.ParEmlCod != null) || (this.ParEmlCod != null && !this.ParEmlCod.equals(other.ParEmlCod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidad.ParametroEmail[ id=" + ParEmlCod + " ]";
    }
    
}
