/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Dominio.SincHelper;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad de CalendarioDocente
 * 
 * @author alvar
 */

@Entity
@Table(
        name = "CALENDARIO_DOCENTE",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"CalCod", "DocPerCod"})}
    )
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CalendarioDocente.findModAfter",  query = "SELECT t FROM CalendarioDocente t where t.ObjFchMod >= :ObjFchMod"),
    @NamedQuery(name = "CalendarioDocente.findAll",       query = "SELECT t FROM CalendarioDocente t")})

public class CalendarioDocente extends SincHelper  implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "CalDocCod", nullable = false)
    private Long CalDocCod;
    
    @Column(name = "ObjFchMod", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ObjFchMod;
    
    @OneToOne(targetEntity = Calendario.class)
    @JoinColumn(name="CalCod", referencedColumnName = "CalCod")
    private Calendario calendario;

    @ManyToOne(targetEntity = Persona.class)
    @JoinColumn(name="DocPerCod", referencedColumnName = "PerCod")
    private Persona Docente;

    //-CONSTRUCTOR
    public CalendarioDocente() {
    }

    //-GETTERS Y SETTERS
    /**
     * 
     * @return Retorna el código de CalendarioDocente
     */
    public Long getCalDocCod() {
        return CalDocCod;
    }

    /**
     *
     * @param CalDocCod Recibe el código de CalendarioDocente
     */
    public void setCalDocCod(Long CalDocCod) {
        this.CalDocCod = CalDocCod;
    }

    /**
     *
     * @return Retorna la fecha de modificación de CalendarioDocente
     */
    public Date getObjFchMod() {
        return ObjFchMod;
    }

    /**
     *
     * @param ObjFchMod Revibe la fecha de modificación de CalendarioDocente
     */
    public void setObjFchMod(Date ObjFchMod) {
        this.ObjFchMod = ObjFchMod;
    }

    /**
     *
     * @return Retorna el calendario de CalendarioDocente
     */
    public Calendario getCalendario() {
        return calendario;
    }

    /**
     *
     * @param calendario Recibe el calendario de CalendarioDocente
     */
    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

    /**
     *
     * @return Retorna el docente de CalendarioDocente
     */
    public Persona getDocente() {
        return Docente;
    }

    /**
     *
     * @param Docente Recibe el docente de CalendarioDocente
     */
    public void setDocente(Persona Docente) {
        this.Docente = Docente;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.CalDocCod);
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
        final CalendarioDocente other = (CalendarioDocente) obj;
        if (!Objects.equals(this.CalDocCod, other.CalDocCod)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CalendarioDocente{" + "CalDocCod=" + CalDocCod + ", ObjFchMod=" + ObjFchMod + ", calendario=" + calendario + ", Docente=" + Docente + '}';
    }
  
    @Override
    public Long GetPrimaryKey() {
        return this.CalDocCod;
    }    
}



