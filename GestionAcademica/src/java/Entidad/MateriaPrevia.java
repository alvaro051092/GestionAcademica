/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Dominio.SincHelper;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad MateriaPrevia
 * 
 * @author alvar
 */


@Entity
@Table(
        name = "MATERIA_PREVIA",
        uniqueConstraints = {
                                @UniqueConstraint(columnNames = {"MatCod", "PreMatCod"})
                            }
)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MateriaPrevia.findModAfter",  query = "SELECT t FROM MateriaPrevia t  WHERE t.ObjFchMod >= :ObjFchMod"),
    @NamedQuery(name = "MateriaPrevia.findAll",       query = "SELECT t FROM MateriaPrevia t")
})
public class MateriaPrevia extends SincHelper implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "MatPreCod")
    private Long MatPreCod;
    
    @ManyToOne(targetEntity = Materia.class)
    @JoinColumn(name="MatCod", referencedColumnName="MatCod")
    private Materia materia;
    
    @ManyToOne(targetEntity = Materia.class)
    @JoinColumn(name="PreMatCod", referencedColumnName="MatCod")
    private Materia materiaPrevia;
    
    @Column(name = "ObjFchMod", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ObjFchMod;

    /**
     *
     * @return Retorna el código de la materia previa
     */
    public Long getMatPreCod() {
        return MatPreCod;
    }

    /**
     *
     * @param MatPreCod Recibe el código de la materia previa
     */
    public void setMatPreCod(Long MatPreCod) {
        this.MatPreCod = MatPreCod;
    }

    /**
     *
     * @return Retorna la Materia de la materia previa
     */
    public Materia getMateria() {
        return materia;
    }

    /**
     *
     * @param materia Recibe la Materia de la materia previa
     */
    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    /**
     *
     * @return Retorna la Materia Previa
     */
    public Materia getMateriaPrevia() {
        return materiaPrevia;
    }

    /**
     *
     * @param materiaPrevia Recibe la Materia Previa
     */
    public void setMateriaPrevia(Materia materiaPrevia) {
        this.materiaPrevia = materiaPrevia;
    }

    //CONSTRUCTOR
    public MateriaPrevia() {
    }

    /**
     *
     * @return Retorna la fecha de modificación de la materia previa
     */
    public Date getObjFchMod() {
        return ObjFchMod;
    }

    /**
     *
     * @param ObjFchMod Recibe la fecha de modificación de la materia previa
     */
    public void setObjFchMod(Date ObjFchMod) {
        this.ObjFchMod = ObjFchMod;
    }
    
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (MatPreCod != null ? MatPreCod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MateriaPrevia)) {
            return false;
        }
        MateriaPrevia other = (MateriaPrevia) object;
        if ((this.MatPreCod == null && other.MatPreCod != null) || (this.MatPreCod != null && !this.MatPreCod.equals(other.MatPreCod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidad.MateriaPrevia[ id=" + MatPreCod + " ]";
    }
    
    @Override
    public Long GetPrimaryKey() {
        return this.MatPreCod;
    }
    
}
