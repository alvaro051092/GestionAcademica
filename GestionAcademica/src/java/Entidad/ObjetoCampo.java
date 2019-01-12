/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Enumerado.TipoCampo;
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
 * Entidad ObjetoCampo
 * 
 * @author alvar
 */

@JsonIgnoreProperties({"objeto"})

@Entity
@Table(name = "OBJETO_CAMPO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ObjetoCampo.findAll",       query = "SELECT t FROM ObjetoCampo t")})

public class ObjetoCampo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @Column(name = "ObjCmpNom", length = 100)
    private String ObjCmpNom;

    @OneToOne(targetEntity = Objeto.class)
    @JoinColumn(name="ObjNom", referencedColumnName="ObjNom")
    private Objeto objeto;

    
    @Column(name = "ObjCmpTpoDat")
    private TipoCampo ObjCmpTpoDat;
    
    @Column(name = "ObjCmpPK")
    private Boolean ObjCmpPK;
    
    //-CONSTRUCTOR

    /**
     *
     */
    public ObjetoCampo() {
    }

    /**
     *
     * @param objeto
     * @param ObjCmpNom
     * @param ObjCmpTpoDat
     * @param ObjCmpPK
     */
    public ObjetoCampo(Objeto objeto, String ObjCmpNom, TipoCampo ObjCmpTpoDat, Boolean ObjCmpPK) {
        this.objeto = objeto;
        this.ObjCmpNom = ObjCmpNom;
        this.ObjCmpTpoDat = ObjCmpTpoDat;
        this.ObjCmpPK = ObjCmpPK;
    }
    
    
    
    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorna Objeto
     */

    @XmlTransient
    public Objeto getObjeto() {
        return objeto;
    }

    /**
     *
     * @param objeto Recibe Objeto
     */
    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }

    /**
     *
     * @return Retorna Nombre Objeto Campo
     */
    public String getObjCmpNom() {
        return ObjCmpNom;
    }

    /**
     *
     * @param ObjCmpNom Recibe Nombre Objeto Campo
     */
    public void setObjCmpNom(String ObjCmpNom) {
        this.ObjCmpNom = ObjCmpNom;
    }

    /**
     *
     * @return Retorna Tipo de dato Objeto Campo
     */
    public TipoCampo getObjCmpTpoDat() {
        return ObjCmpTpoDat;
    }

    /**
     *
     * @param ObjCmpTpoDat Recibe Tipo de dato Objeto Campo
     */
    public void setObjCmpTpoDat(TipoCampo ObjCmpTpoDat) {
        this.ObjCmpTpoDat = ObjCmpTpoDat;
    }

    /**
     *
     * @return
     */
    public Boolean getObjCmpPK() {
        return ObjCmpPK;
    }

    /**
     *
     * @param ObjCmpPK
     */
    public void setObjCmpPK(Boolean ObjCmpPK) {
        this.ObjCmpPK = ObjCmpPK;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.ObjCmpNom);
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
        final ObjetoCampo other = (ObjetoCampo) obj;
        if (!Objects.equals(this.ObjCmpNom, other.ObjCmpNom)) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return "ObjetoCampo{" + "objeto=" + objeto + ", ObjCmpNom=" + ObjCmpNom + ", ObjCmpTpoDat=" + ObjCmpTpoDat + ", ObjCmpPK=" + ObjCmpPK + '}';
    }


}



