/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Enumerado.TipoMenu;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad Menu
 * 
 * @author alvar
 */
@Entity
@Table(name = "MENU")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Menu.findAll", query = "SELECT m FROM Menu m"),
    @NamedQuery(name = "Menu.findOnlyFirstLevel", query = "SELECT m FROM Menu m WHERE m.superior is null")})
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "MenCod")
    private Long MenCod;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "MenTpo")
    private TipoMenu MenTpo;
    
    @Column(name = "MenUrl", length = 500)
    private String MenUrl;
    
    @Column(name = "MenNom", length = 100)
    private String MenNom;
    
    @Column(name = "MenOrd")
    private Integer MenOrd;
    
    @Column(name = "MenIsParent")
    private Boolean MenIsParent;
    
    @OneToOne(targetEntity = Menu.class)
    @JoinColumn(name="MenCodSup", referencedColumnName="MenCod")
    private Menu superior;
    
    @OneToMany(targetEntity = Menu.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="MenCodSup", referencedColumnName = "MenCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<Menu> lstSubMenu;

    /**
     *
     * @return Retorna el código de Menú
     */
    public Long getMenCod() {
        return MenCod;
    }

    /**
     *
     * @param menCod Recibe el código de Menú
     */
    public void setMenCod(Long menCod) {
        this.MenCod = menCod;
    }

    /**
     *
     * @return Retorna el Tipo de Menú
     */
    public TipoMenu getMenTpo() {
        return MenTpo;
    }

    /**
     *
     * @param MenTpo Recibe el tipo de Menú
     */
    public void setMenTpo(TipoMenu MenTpo) {
        this.MenTpo = MenTpo;
    }

    /**
     *
     * @return Retorna la URL del Menú
     */
    public String getMenUrl() {
        return MenUrl;
    }

    /**
     *
     * @param MenUrl Recibe la URL del Menú
     */
    public void setMenUrl(String MenUrl) {
        this.MenUrl = MenUrl;
    }

    /**
     *
     * @return Retorna el nombre del Menú
     */
    public String getMenNom() {
        return MenNom;
    }

    /**
     *
     * @param MenNom Recibe el nombre del Menú
     */
    public void setMenNom(String MenNom) {
        this.MenNom = MenNom;
    }

    /**
     *
     * @return Retorna el Orden del Menú
     */
    public Integer getMenOrd() {
        return MenOrd;
    }

    /**
     *
     * @param MenOrd Recibe el Orden del Menú
     */
    public void setMenOrd(Integer MenOrd) {
        this.MenOrd = MenOrd;
    }

    //CONSTRUCTOR
    public Menu() {
    }
    
    public Menu(TipoMenu MenTpo, String MenUrl, String MenNom, Integer MenOrd) {
        this.MenTpo = MenTpo;
        this.MenUrl = MenUrl;
        this.MenNom = MenNom;
        this.MenOrd = MenOrd;
    }

    /**
     *
     * @return Retorna la lista de SubMenu
     */
    public List<Menu> getLstSubMenu() {
        if(this.lstSubMenu == null) lstSubMenu = new ArrayList<>();
        return lstSubMenu;
    }

    /**
     *
     * @param lstSubMenu Recibe la lista de SubMenu
     */
    public void setLstSubMenu(List<Menu> lstSubMenu) {
        this.lstSubMenu = lstSubMenu;
    }

    /**
     *
     * @return 
     */
    public Boolean getMenIsParent() {
        if(MenIsParent == null) MenIsParent = false;
        return MenIsParent;
    }

    /**
     *
     * @param MenIsParent
     */
    public void setMenIsParent(Boolean MenIsParent) {
        this.MenIsParent = MenIsParent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (MenCod != null ? MenCod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Menu)) {
            return false;
        }
        Menu other = (Menu) object;
        if ((this.MenCod == null && other.MenCod != null) || (this.MenCod != null && !this.MenCod.equals(other.MenCod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidad.Menu[ MenCod=" + MenCod + " ]";
    }   
}
