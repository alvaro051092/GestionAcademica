/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Dominio.SincHelper;
import Enumerado.TipoArchivo;
import java.io.File;
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
import javax.xml.bind.annotation.XmlInlineBinaryData;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author alvar
 */



@Entity
@Table(name = "PERIODO_ESTUDIO_DOCUMENTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PeriodoEstudioDocumento.findModAfter",  query = "SELECT t FROM PeriodoEstudioDocumento t  WHERE t.ObjFchMod >= :ObjFchMod"),
    @NamedQuery(name = "PeriodoEstudioDocumento.findAll",       query = "SELECT t FROM PeriodoEstudioDocumento t")
})
public class PeriodoEstudioDocumento extends SincHelper implements Serializable {

    private static final long serialVersionUID = 1L;
   
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "DocCod", nullable = false)
    private Long DocCod;
    
    @OneToOne(targetEntity = PeriodoEstudio.class)
    @JoinColumn(name="PeriEstCod", referencedColumnName="PeriEstCod")
    private PeriodoEstudio periodoEstudio;
    
    @OneToOne(targetEntity = Archivo.class)
    @JoinColumn(name="ArcCod")
    private Archivo archivo;
  
    @Column(name = "ObjFchMod", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ObjFchMod;


    //-CONSTRUCTOR
    public PeriodoEstudioDocumento() {
    }
    
    //-GETTERS Y SETTERS

    public Archivo getObjArchivo(){
        return archivo;
    }
    
    public void setObjArchivo(Archivo archivo){
        this.archivo = archivo;
    }

    @JsonIgnore
    @XmlTransient
    public File getArchivo(){
        if(this.archivo == null) return null;
        return this.archivo.getArchivo();
    }
    
    public Long getArcCod(){
        if(archivo == null) return null;
        
        return archivo.getArcCod();
    }

    public void setArchivo(File pArchivo) {
        archivo = new Archivo();
        this.archivo.setArchivo(pArchivo, TipoArchivo.PERIODO_DOCUMENTO);
    }

    public Long getDocCod() {
        return DocCod;
    }

    public void setDocCod(Long DocCod) {
        this.DocCod = DocCod;
    }

    public PeriodoEstudio getPeriodo() {
        return periodoEstudio;
    }

    public void setPeriodo(PeriodoEstudio periodo) {
        this.periodoEstudio = periodo;
    }

    public String getDocNom() {
        if(this.archivo == null) return null;
        return this.archivo.getArcNom();
    }

    public void setDocNom(String DocNom) {
        this.archivo.setArcNom(DocNom);
    }

    public String getDocExt() {
        if(this.archivo == null) return null;
        return this.archivo.getArcExt();
    }

    public void setDocExt(String DocExt) {
        this.archivo.setArcExt(DocExt);
    }

    public Date getObjFchMod() {
        return ObjFchMod;
    }

    public void setObjFchMod(Date ObjFchMod) {
        this.ObjFchMod = ObjFchMod;
    }

    public String getFileBase64(){
        if(this.archivo == null) return null;
        return this.archivo.getFileBase64();
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.DocCod);
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
        final PeriodoEstudioDocumento other = (PeriodoEstudioDocumento) obj;
        if (!Objects.equals(this.DocCod, other.DocCod)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PeriodoEstudioDocumento{" + "DocCod=" + DocCod + ", periodoEstudio=" + periodoEstudio + ", archivo=" + archivo + ", ObjFchMod=" + ObjFchMod + '}';
    }

    public void setDocFch(Date date) {
        this.archivo.setArcFch(date);
    }

  
}


