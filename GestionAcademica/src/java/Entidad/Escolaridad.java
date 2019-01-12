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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad Escolaridad
 * 
 * @author alvar
 */
@Entity
@Table(name = "ESCOLARIDAD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Escolaridad.findAll",       query = "SELECT t FROM Escolaridad t"),
    @NamedQuery(name = "Escolaridad.findModAfter",  query = "SELECT t FROM Escolaridad t WHERE t.ObjFchMod >= :ObjFchMod"),
    @NamedQuery(name = "Escolaridad.findByAlumno",  query = "SELECT t FROM Escolaridad t WHERE t.alumno.PerCod = :PerCod")
})
public class Escolaridad extends SincHelper implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "EscCod", nullable = false)
    private Long EscCod;
    
    @ManyToOne(targetEntity = Persona.class)
    @JoinColumn(name="EscAluPerCod", referencedColumnName="PerCod")
    private Persona alumno;

    @ManyToOne(targetEntity = Materia.class)
    @JoinColumn(name="EscMatCod", referencedColumnName="MatCod")
    private Materia materia;
    
    @ManyToOne(targetEntity = Modulo.class)
    @JoinColumn(name="EscModCod", referencedColumnName="ModCod")
    private Modulo modulo;
    
    @ManyToOne(targetEntity = Curso.class)
    @JoinColumn(name="EscCurCod", referencedColumnName="CurCod")
    private Curso curso;
    
    @ManyToOne(targetEntity = Persona.class)
    @JoinColumn(name="EscPerCod", referencedColumnName="PerCod")
    private Persona IngresadaPor;

    @Column(name = "EscCalVal", precision=10, scale=2)
    private Double EscCalVal;
    
    @Column(name = "EscCurVal", precision=10, scale=2)
    private Double EscCurVal;
    
    @Column(name = "EscFch", columnDefinition="DATE")
    @Temporal(TemporalType.DATE)
    private Date EscFch;
    
    @Column(name = "ObjFchMod", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ObjFchMod;
    

    //-CONSTRUCTOR
    public Escolaridad() {
    }

    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorno el código de la Escolaridad
     */
    public Long getEscCod() {
        return EscCod;
    }

    /**
     *
     * @param EscCod Recibo el código de la Escolaridad
     */
    public void setEscCod(Long EscCod) {
        this.EscCod = EscCod;
    }

    /**
     *
     * @return Retorno la materia de la Escolaridad
     */
    public Materia getMateria() {
        return materia;
    }

    /**
     *
     * @param materia Recibo la materia de la Escolaridad
     */
    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    /**
     *
     * @return Retorno el módulo de la Escolaridad
     */
    public Modulo getModulo() {
        return modulo;
    }

    /**
     *
     * @param modulo Recibo el módulo de la Escolaridad
     */
    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    /**
     *
     * @return Retorno la persona que ingreso la escolaridad
     */
    public Persona getIngresadaPor() {
        return IngresadaPor;
    }

    /**
     *
     * @param IngresadaPor Recibo la persona que ingreso la escolaridad
     */
    public void setIngresadaPor(Persona IngresadaPor) {
        this.IngresadaPor = IngresadaPor;
    }

    /**
     *
     * @return Retorno el valor de la calificación de la Escolaridad
     */
    public Double getEscCalVal() {
        return EscCalVal;
    }

    /**
     *
     * @param EscCalVal Recibo le valor de la calificación de la Escolaridad
     */
    public void setEscCalVal(Double EscCalVal) {
        this.EscCalVal = EscCalVal;
    }

    /**
     *
     * @return Retorno la fecha de la Escolaridad
     */
    public Date getEscFch() {
        return EscFch;
    }

    /**
     *
     * @param EscFch Recibo la fecha de la Escolaridad
     */
    public void setEscFch(Date EscFch) {
        this.EscFch = EscFch;
    }

    /**
     *
     * @return Retorno la fecha de modificación de la Escolaridad
     */
    public Date getObjFchMod() {
        return ObjFchMod;
    }

    /**
     *
     * @param ObjFchMod Recibo la fecha de modificación de la Escolaridad
     */
    public void setObjFchMod(Date ObjFchMod) {
        this.ObjFchMod = ObjFchMod;
    }

    /**
     *
     * @return Retorno el curso de la Escolaridad
     */
    public Curso getCurso() {
        return curso;
    }

    /**
     *
     * @param curso Recibo el curso de la Escolaridad
     */
    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    /**
     *
     * @return Retorno el alumno de la Escolaridad
     */
    public Persona getAlumno() {
        return alumno;
    }

    /**
     *
     * @param alumno Recibo el alumno de la Escolaridad
     */
    public void setAlumno(Persona alumno) {
        this.alumno = alumno;
    }

    /**
     *
     * @return Retorno el valor del Curso de la Escolaridad
     */
    public Double getEscCurVal() {
        return EscCurVal;
    }

    /**
     *
     * @param EscCurVal Recibo el valor del Curso de la Escolaridad
     */
    public void setEscCurVal(Double EscCurVal) {
        this.EscCurVal = EscCurVal;
    }
    
    /**
     *
     * @return Retorno si la materia o curso es Revalida, Aprobado o Eliminado
     */
    public String getAprobacion() 
    {
        if(this.Revalida()) return "Revalida";
        
        if(this.getMateria() != null) if(this.materia.MateriaExonera(EscCurVal)) return "Exonera";
        
        if(this.EscCalVal >= 70) return "Aprobado";
        if(this.EscCalVal < 70) return "Eliminado";
        return "";
    }
    
    /**
     *
     * @return Retorno el estado de la materia o curso es Aprobada o no
     */
    public Boolean getAprobado() {
        if(this.EscCalVal != null) if(this.EscCalVal >= 70) return true;
        if(this.EscCurVal != null && this.materia != null) if(this.materia.MateriaExonera(EscCurVal)) if(this.EscCurVal >= 70) return true;
        return false;
    }
    
    /**
     *
     * @return Retorno si la materia o curso es revalida o no
     */
    public Boolean Revalida(){
        if(this.EscCalVal == null) return false;
        return this.EscCalVal.equals(Double.NaN);
    }
    
    /**
     *
     * @return Retirbi el nombre del estudio dependiendo de si es Curso, Materia o Módulo
     */
    public String getNombreEstudio()
    {
        
        if(this.getModulo() != null) return this.getModulo().getModNom();
        if(this.getCurso() != null) return this.getCurso().getCurNom();
        if(this.getMateria() != null) return this.getMateria().getMatNom();

        return "";
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.EscCod);
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
        final Escolaridad other = (Escolaridad) obj;
        if (!Objects.equals(this.EscCod, other.EscCod)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Escolaridad{" + "EscCod=" + EscCod + ", materia=" + materia + ", modulo=" + modulo + ", IngresadaPor=" + IngresadaPor + ", EscCalVal=" + EscCalVal + ", EscFch=" + EscFch + ", ObjFchMod=" + ObjFchMod + '}';
    }

    @Override
    public Long GetPrimaryKey() {
        return this.EscCod;
    }
  
}


