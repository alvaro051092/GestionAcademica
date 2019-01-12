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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad TipoEvaluacion
 *
 * @author alvar
 */
@Entity
@Table(name = "TIPO_EVALUACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoEvaluacion.findAll", query = "SELECT tpoEvl FROM TipoEvaluacion tpoEvl"),
    @NamedQuery(name = "TipoEvaluacion.findModAfter", query = "SELECT tpoEvl FROM TipoEvaluacion tpoEvl WHERE tpoEvl.ObjFchMod >= :ObjFchMod"),
    @NamedQuery(name = "TipoEvaluacion.findByTpoEvlCod", query = "SELECT tpoEvl FROM TipoEvaluacion tpoEvl WHERE tpoEvl.TpoEvlCod = :TpoEvlCod"),
    @NamedQuery(name = "TipoEvaluacion.findByTpoEvlNom", query = "SELECT tpoEvl FROM TipoEvaluacion tpoEvl WHERE tpoEvl.TpoEvlNom = :TpoEvlNom"),
    @NamedQuery(name = "TipoEvaluacion.findLastTpoEvl", query = "SELECT  tpoEvl FROM TipoEvaluacion tpoEvl ORDER BY tpoEvl.TpoEvlCod DESC")})

 public class TipoEvaluacion extends SincHelper implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "TpoEvlCod", nullable = false)
    private Long TpoEvlCod;

    @Column(name = "TpoEvlNom", length = 100)
    private String TpoEvlNom;

    @Column(name = "TpoEvlExm")
    private Boolean TpoEvlExm;

    @Column(name = "TpoEvlInsAut")
    private Boolean TpoEvlInsAut;

    @Column(name = "ObjFchMod", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ObjFchMod;

    public TipoEvaluacion() {
    }

    public TipoEvaluacion(Long pTpoEvlCod) {
        this.TpoEvlCod = pTpoEvlCod;
    }

    /**
     *
     * @return Código
     */
    public Long getTpoEvlCod() {
        return TpoEvlCod;
    }

    /**
     *
     * @param pTpoEvlCod Código
     */
    public void setTpoEvlCod(Long pTpoEvlCod) {
        this.TpoEvlCod = pTpoEvlCod;
    }

    /**
     *
     * @return Nombre
     */
    public String getTpoEvlNom() {
        return TpoEvlNom;
    }

    /**
     *
     * @param pTpoEvlNom Nombre
     */
    public void setTpoEvlNom(String pTpoEvlNom) {
        this.TpoEvlNom = pTpoEvlNom;
    }

    /**
     *
     * @return Examen
     */
    public Boolean getTpoEvlExm() {
        return TpoEvlExm;
    }

    /**
     *
     * @param TpoEvlExm Examen
     */
    public void setTpoEvlExm(Boolean TpoEvlExm) {
        this.TpoEvlExm = TpoEvlExm;
    }

    /**
     *
     * @return Inscripcion automatica
     */
    public Boolean getTpoEvlInsAut() {
        return TpoEvlInsAut;
    }

    /**
     *
     * @param TpoEvlInsAut Inscripcion automatica
     */
    public void setTpoEvlInsAut(Boolean TpoEvlInsAut) {
        this.TpoEvlInsAut = TpoEvlInsAut;
    }

    /**
     *
     * @return Fecha de modificacion
     */
    public Date getObjFchMod() {
        return ObjFchMod;
    }

    /**
     *
     * @param ObjFchMod Fecha de modificacion
     */
    public void setObjFchMod(Date ObjFchMod) {
        this.ObjFchMod = ObjFchMod;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (TpoEvlCod != null ? TpoEvlCod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoEvaluacion)) {
            return false;
        }
        TipoEvaluacion other = (TipoEvaluacion) object;
        if ((this.TpoEvlCod == null && other.TpoEvlCod != null) || (this.TpoEvlCod != null && !this.TpoEvlCod.equals(other.TpoEvlCod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TipoEvaluacion{" + "TpoEvlCod=" + TpoEvlCod + ", TpoEvlNom=" + TpoEvlNom + ", TpoEvlExm=" + TpoEvlExm + ", TpoEvlInsAut=" + TpoEvlInsAut + ", ObjFchMod=" + ObjFchMod + '}';
    }

    @Override
    public Long GetPrimaryKey() {
        return this.TpoEvlCod;
    }
    
    public String GetReportColumns(){
        return "columns: [{title: \"Código\"}, {title: \"Nombre\"}]";
    }
    
    public String GetReportData(){
        return "[\""+this.getTpoEvlCod()+"\", \""+this.getTpoEvlNom()+"\"]";
    }
    
}
