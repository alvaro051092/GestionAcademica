/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad SincRegistroEliminado
 *
 * @author alvar
 */
@Entity
@Table(name = "SINC_REGISTRO_ELIMINADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SincRegistroELiminado.findModAfter",  query = "SELECT t FROM SincRegistroEliminado t where t.SncObjElimFch >= :ObjFchMod"),
    @NamedQuery(name = "SincRegistroELiminado.findAll",       query = "SELECT t FROM SincRegistroEliminado t")})

public class SincRegistroEliminado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "SncObjElimCod", nullable = false)
    private Long SncObjElimCod;

    @OneToOne(targetEntity = Objeto.class)
    @JoinColumn(name="ObjNom", referencedColumnName="ObjNom")
    private Objeto objeto;
    
    @Column(name = "SncObjElimFch", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date SncObjElimFch;
    
    @Column(name = "ObjElimCod")
    private Long ObjElimCod;
   
    
    //-CONSTRUCTOR
    public SincRegistroEliminado() {
    }
        
    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorna el código de SincRegistroEliminado
     */
    public Long getSncObjElimCod() {
        return SncObjElimCod;
    }

    /**
     * 
     * @param SncObjElimCod Recibe el código de SincRegistroEliminado
     */
    public void setSncObjElimCod(Long SncObjElimCod) {
        this.SncObjElimCod = SncObjElimCod;
    }

    /**
     *
     * @return Retorna el objeto de SincRegistroEliminado
     */
    public Objeto getObjeto() {
        return objeto;
    }

    /**
     *
     * @param objeto Recibe el Objeto de SincRegistroEliminado
     */
    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }

    /**
     *
     * @return Retorna la fecha del objeto SincRegistroEliminado eliminado
     */
    public Date getSncObjElimFch() {
        return SncObjElimFch;
    }

    /**
     *
     * @param SncObjElimFch Recibe la fecha del objeto SincRegistroEliminado aliminado
     */
    public void setSncObjElimFch(Date SncObjElimFch) {
        this.SncObjElimFch = SncObjElimFch;
    }

    /**
     *
     * @return Retorna el código del objeto eliminado
     */
    public Long getObjElimCod() {
        return ObjElimCod;
    }

    /**
     *
     * @param ObjElimCod Recibe el código del objeto eliminado
     */
    public void setObjElimCod(Long ObjElimCod) {
        this.ObjElimCod = ObjElimCod;
    }

    

      

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.SncObjElimCod);
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
        final SincRegistroEliminado other = (SincRegistroEliminado) obj;
        if (!Objects.equals(this.SncObjElimCod, other.SncObjElimCod)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SincRegistroEliminado{" + "SncObjElimCod=" + SncObjElimCod + ", objeto=" + objeto + ", SncObjElimFch=" + SncObjElimFch + ", ObjElimCod=" + ObjElimCod + '}';
    }

   
    
}


