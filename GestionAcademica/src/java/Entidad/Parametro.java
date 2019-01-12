/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Utiles.JSonDateTimeSerializer;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Entidad Parámetro
 *
 * @author alvar
 */
@JsonAutoDetect
@Entity
@Table(name = "PARAMETRO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Parametro.findAll",       query = "SELECT t FROM Parametro t")})

public class Parametro implements Serializable {

    private static final long serialVersionUID = 1L;

    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @Column(name = "ParCod", nullable = false)
    private Long ParCod;
    
    @ManyToOne(targetEntity = ParametroEmail.class, optional=true)
    @JoinColumn(name="ParEmlCod", referencedColumnName="ParEmlCod")
    private ParametroEmail parametroEmail;
    
    @Column(name = "ParFchUltSinc", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ParFchUltSinc;
    
    @Column(name = "ParSisLocal")
    private Boolean ParSisLocal;
    
    @Column(name = "ParUtlMdl")
    private Boolean ParUtlMdl;
    
    @Column(name = "ParUrlMdl", length = 500)
    private String ParUrlMdl;
    
    @Column(name = "ParMdlTkn", length = 500)
    private String ParMdlTkn;
    
    @Column(name = "ParUrlSrvSnc", length = 500)
    private String ParUrlSrvSnc;
    
    @Column(name = "ParPswValExp", length = 500)
    private String ParPswValExp;
    
    @Column(name = "ParPswValMsg", length = 500)
    private String ParPswValMsg;
    
    @Column(name = "ParImpDateFormat", length = 100)
    private String ParImpDateFormat;
    
    @Column(name = "ParDiaEvlPrv")
    private Integer ParDiaEvlPrv;
    
    @Column(name = "ParTieIna")
    private Integer ParTieIna;
    
    @Column(name = "ParSncAct")
    private Boolean ParSncAct;
    
    @Column(name = "ParUrlSis", length = 500)
    private String ParUrlSis;

    
    //-CONSTRUCTOR
    public Parametro() {
    }
    
    
    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorno Código de Parámetro
     */
    public Long getParCod() {
        return ParCod;
    }

    /**
     *
     * @param ParCod Recibe código de Parámetro
     */
    public void setParCod(Long ParCod) {
        this.ParCod = ParCod;
    }

    /**
     *
     * @return Retorno parametroEmail
     */
    public ParametroEmail getParametroEmail() {
        return parametroEmail;
    }

    /**
     *
     * @param parametroEmail Recibe ParametroEmail
     */
    public void setParametroEmail(ParametroEmail parametroEmail) {
        this.parametroEmail = parametroEmail;
    }

    /**
     *
     * @return Retorno si utiliza sincronización
     */
    @JsonSerialize(using=JSonDateTimeSerializer.class)
    public Date getParFchUltSinc() {
        return ParFchUltSinc;
    }

    /**
     *
     * @param ParFchUltSinc Recibe si utiliza sincronización
     */
    public void setParFchUltSinc(Date ParFchUltSinc) {
        this.ParFchUltSinc = ParFchUltSinc;
    }

    /**
     *
     * @return Retorno si utiliza sistema local
     */
    public Boolean getParSisLocal() {
        return ParSisLocal;
    }

    /**
     *
     * @param ParSisLocal Recibe si utiliza sistema local
     */
    public void setParSisLocal(Boolean ParSisLocal) {
        this.ParSisLocal = ParSisLocal;
    }

    /**
     *
     * @return Retorno 
     */
    public String getParUrlSrvSnc() {
        return ParUrlSrvSnc;
    }

    /**
     *
     * @param ParUrlSrvSnc
     */
    public void setParUrlSrvSnc(String ParUrlSrvSnc) {
        this.ParUrlSrvSnc = ParUrlSrvSnc;
    }

    /**
     *
     * @return
     */
    public Integer getParDiaEvlPrv() {
        return ParDiaEvlPrv;
    }

    /**
     *
     * @param ParDiaEvlPrv
     */
    public void setParDiaEvlPrv(Integer ParDiaEvlPrv) {
        this.ParDiaEvlPrv = ParDiaEvlPrv;
    }

    /**
     *
     * @return
     */
    public Integer getParTieIna() {
        return ParTieIna;
    }

    /**
     *
     * @param ParTieIna
     */
    public void setParTieIna(Integer ParTieIna) {
        this.ParTieIna = ParTieIna;
    }

    /**
     *
     * @return
     */
    public Boolean getParSncAct() {
        return ParSncAct;
    }

    /**
     *
     * @param ParSncAct
     */
    public void setParSncAct(Boolean ParSncAct) {
        this.ParSncAct = ParSncAct;
    }

    /**
     *
     * @return Retorna URL de moodle
     */
    public String getParUrlMdl() {
        return ParUrlMdl;
    }

    /**
     *
     * @param ParUrlMdl Recibe URL de moodle
     */
    public void setParUrlMdl(String ParUrlMdl) {
        this.ParUrlMdl = ParUrlMdl;
    }

    /**
     *
     * @return Retorna el token de moodle
     */
    public String getParMdlTkn() {
        return ParMdlTkn;
    }

    /**
     *
     * @param ParMdlTkn Recibe el token de moodle
     */
    public void setParMdlTkn(String ParMdlTkn) {
        this.ParMdlTkn = ParMdlTkn;
    }

    /**
     *
     * @return 
     */
    public String getParPswValExp() {
        return ParPswValExp;
    }

    /**
     *
     * @param ParPswValExp
     */
    public void setParPswValExp(String ParPswValExp) {
        this.ParPswValExp = ParPswValExp;
    }

    /**
     *
     * @return
     */
    public String getParPswValMsg() {
        return ParPswValMsg;
    }

    /**
     *
     * @param ParPswValMsg
     */
    public void setParPswValMsg(String ParPswValMsg) {
        this.ParPswValMsg = ParPswValMsg;
    }

    /**
     *
     * @return Retorna url sistema web
     */
    public String getParUrlSis() {
        return ParUrlSis;
    }

    /**
     *
     * @param ParUrlSis Recibe url sistema web
     */
    public void setParUrlSis(String ParUrlSis) {
        this.ParUrlSis = ParUrlSis;
    }

    /**
     *
     * @return Retorna si utiliza o no moodle
     */
    public Boolean getParUtlMdl() {
        return ParUtlMdl;
    }

    /**
     *
     * @param ParUtlMdl Recibe si utiliza o no moodle
     */
    public void setParUtlMdl(Boolean ParUtlMdl) {
        this.ParUtlMdl = ParUtlMdl;
    }

    /**
     *
     * @return Retorna 
     */
    public String getParImpDateFormat() {
        if(ParImpDateFormat == null) ParImpDateFormat = "dd/MM/yyyy";
        return ParImpDateFormat;
    }

    /**
     *
     * @param ParImpDateFormat
     */
    public void setParImpDateFormat(String ParImpDateFormat) {
        this.ParImpDateFormat = ParImpDateFormat;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ParCod != null ? ParCod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Parametro)) {
            return false;
        }
        Parametro other = (Parametro) object;
        if ((this.ParCod == null && other.ParCod != null) || (this.ParCod != null && !this.ParCod.equals(other.ParCod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidad.Parametro[ id=" + ParCod + " ]";
    }
    
}
