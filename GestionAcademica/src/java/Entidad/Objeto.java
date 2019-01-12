/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Enumerado.TipoCampo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

/**
 *  Entidad Objeto
 * 
 * @author alvar
 */
@Entity
@Table(name = "OBJETO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Objeto.findAll",       query = "SELECT t FROM Objeto t"),
    @NamedQuery(name = "Objeto.findByObjeto",  query = "SELECT t FROM Objeto t WHERE t.ObjNom =:ObjNom")})

public class Objeto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @Column(name = "ObjNom", length = 100)
    private String ObjNom;
    
    @Column(name = "ObjNmdQry", length = 100)
    private String ObjNmdQry;
    
    @Column(name = "ObjClsNom", length = 500)
    private String ObjClsNom;
    
    @Column(name = "ObjFchMod", columnDefinition="DATE")
    @Temporal(TemporalType.DATE)
    private Date ObjFchMod;
    
    @OneToMany(targetEntity = ObjetoCampo.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="ObjNom", referencedColumnName="ObjNom")
    @Fetch(FetchMode.SUBSELECT)
    private List<ObjetoCampo> lstCampo;
    
    //-CONSTRUCTORES
    public Objeto() {
        this.lstCampo = new ArrayList<>();
    }

    public Objeto(String ObjNom, List<ObjetoCampo> lstCampo) {
        this.ObjNom = ObjNom;
        this.lstCampo = lstCampo;
    }

    public Objeto(String ObjNom, String ObjNmdQry, String PrimaryKey, String ObjClsNom) {
        this.ObjNom = ObjNom;
        this.ObjNmdQry = ObjNmdQry;
        this.ObjClsNom = ObjClsNom;
        this.lstCampo = new ArrayList<>();
        
        this.lstCampo.add(new ObjetoCampo(this, PrimaryKey, TipoCampo.LONG, Boolean.TRUE));
        
    }
    

    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorna el nombre del objeto
     */
    public String getObjNom() {
        return ObjNom;
    }

    /**
     *
     * @param ObjNom Recibe el nombre del objeto
     */
    public void setObjNom(String ObjNom) {
        this.ObjNom = ObjNom;
    }

    /**
     *
     * @return Retorna la fecha de modificación del objeto
     */
    public Date getObjFchMod() {
        return ObjFchMod;
    }

    /**
     *
     * @param ObjFchMod Recibe la fecha de modificación del objeto
     */
    public void setObjFchMod(Date ObjFchMod) {
        this.ObjFchMod = ObjFchMod;
    }

    /**
     *
     * @return Retorna la lista de Campo
     */
    public List<ObjetoCampo> getLstCampo() {
        return lstCampo;
    }

    /**
     *
     * @param lstCampo Recibe la lista de Campo
     */
    public void setLstCampo(List<ObjetoCampo> lstCampo) {
        this.lstCampo = lstCampo;
    }

    /**
     *
     * @return
     */
    public String getObjNmdQry() {
        return ObjNmdQry;
    }

    /**
     *
     * @param ObjNmdQry
     */
    public void setObjNmdQry(String ObjNmdQry) {
        this.ObjNmdQry = ObjNmdQry;
    }

    /**
     *
     * @return 
     */
    public ObjetoCampo getPrimaryKey(){
        for(ObjetoCampo objCmp : this.getLstCampo())
        {
            if(objCmp.getObjCmpPK())
            {
                return objCmp;
            }
        }
        
        return null;
    }

    /**
     *
     * @return
     */
    public String getObjClsNom() {
        return ObjClsNom;
    }

    /**
     *
     * @param ObjClsNom
     */
    public void setObjClsNom(String ObjClsNom) {
        this.ObjClsNom = ObjClsNom;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.ObjNom);
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
        final Objeto other = (Objeto) obj;
        if (!Objects.equals(this.ObjNom, other.ObjNom)) {
            return false;
        }
        return true;
    }
    



    @Override
    public String toString() {
        return "Objeto{" + " ObjNom=" + ObjNom + ", ObjFchMod=" + ObjFchMod + '}';
    }
    
    
    
}
