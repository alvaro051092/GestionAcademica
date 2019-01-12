/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Dominio.SincHelper;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad PeriodoEstudio
 *
 * @author alvar
 */


@Entity
@Table(
        name = "PERIODO_ESTUDIO",
        uniqueConstraints = {
                                @UniqueConstraint(columnNames = {"PeriCod", "MatEstMatCod"}),
                                @UniqueConstraint(columnNames = {"PeriCod", "ModEstModCod"})
                            }
    )
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PeriodoEstudio.findModAfter",  query = "SELECT t FROM PeriodoEstudio t  WHERE t.ObjFchMod >= :ObjFchMod order by t.periodo.PerFchIni desc"),
    @NamedQuery(name = "PeriodoEstudio.findByPersona",  query = "SELECT t FROM PeriodoEstudio t  WHERE ( :PerCod in (SELECT X.Alumno.PerCod FROM t.lstAlumno X) OR :PerCod IN (SELECT Y.Docente.PerCod FROM t.lstDocente Y)) order by t.periodo.PerFchIni desc"),
    @NamedQuery(name = "PeriodoEstudio.findAll",       query = "SELECT t FROM PeriodoEstudio t order by t.periodo.PerFchIni desc")
})
public class PeriodoEstudio extends SincHelper implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
    
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "PeriEstCod", nullable = false)
    private Long PeriEstCod;
    
    @OneToOne(targetEntity = Periodo.class)        
    @JoinColumn(name="PeriCod", referencedColumnName="PeriCod")
    private Periodo periodo;

    @OneToOne(targetEntity = Materia.class)        
    @JoinColumn(name="MatEstMatCod", referencedColumnName="MatCod")
    private Materia Materia;
    
    @OneToOne(targetEntity = Modulo.class)        
    @JoinColumn(name="ModEstModCod", referencedColumnName="ModCod")
    private Modulo Modulo;
    
    @Column(name = "ObjFchMod", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ObjFchMod;
    
    @Column(name = "MdlCod")
    private Long MdlCod;
   
    @Column(name = "FchSincMdl", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date FchSincMdl;
    
    //----------------------------------------------------------------------
    @OneToMany(targetEntity = PeriodoEstudioAlumno.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="PeriEstCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<PeriodoEstudioAlumno> lstAlumno;
    
    @OneToMany(targetEntity = PeriodoEstudioDocente.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="PeriEstCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<PeriodoEstudioDocente> lstDocente;
    
    @OneToMany(targetEntity = PeriodoEstudioDocumento.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="PeriEstCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<PeriodoEstudioDocumento> lstDocumento;
    //----------------------------------------------------------------------
    

    //-CONSTRUCTOR
    public PeriodoEstudio() {
        this.lstAlumno = new ArrayList<>();
        this.lstDocente = new ArrayList<>();
        this.lstDocumento = new ArrayList<>();
    }
    
    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorna el código del Período Estudio
     */
    public Long getPeriEstCod() {
        return PeriEstCod;
    }

    /**
     *
     * @param PeriEstCod Recibe el código del Período Estudio
     */
    public void setPeriEstCod(Long PeriEstCod) {
        this.PeriEstCod = PeriEstCod;
    }

    /**
     *
     * @return Retorno el período del Período Estudio
     */
    public Periodo getPeriodo() {
        return periodo;
    }

    /**
     *
     * @param Periodo Recibe el período del Período Estudio
     */
    public void setPeriodo(Periodo Periodo) {
        this.periodo = Periodo;
    }

    /**
     *
     * @return Retorna la Materia del Período Estudio
     */
    public Materia getMateria() {
        return Materia;
    }

    /**
     *
     * @param Materia Recibe la Materia del Período Estudio
     */
    public void setMateria(Materia Materia) {
        this.Materia = Materia;
    }

    /**
     *
     * @return Retorna el módulo del Período Estudio
     */
    public Modulo getModulo() {
        return Modulo;
    }

    /**
     *
     * @param Modulo Recibe el módulo del Período Estudio
     */
    public void setModulo(Modulo Modulo) {
        this.Modulo = Modulo;
    }

    /**
     *
     * @return retorna el código de mooldle del Período Estudio
     */
    public Long getMdlCod() {
        return MdlCod;
    }

    /**
     *
     * @param MdlCod Recibe el código de moodle del Período Estudio
     */
    public void setMdlCod(Long MdlCod) {
        this.MdlCod = MdlCod;
    }

    /**
     *
     * @return retorna la fecha de sincronización con moodle
     */
    public Date getFchSincMdl() {
        return FchSincMdl;
    }

    /**
     *
     * @param FchSincMdl Recibe la fecha de sincronización con moodle
     */
    public void setFchSincMdl(Date FchSincMdl) {
        this.FchSincMdl = FchSincMdl;
    }
    
    /**
     *
     * @return Retorno la fecha de modificación del Período Estudio
     */
    public Date getObjFchMod() {
        return ObjFchMod;
    }

    /**
     *
     * @param ObjFchMod Recibe la fecha de modificación del Período Estudio
     */
    public void setObjFchMod(Date ObjFchMod) {
        this.ObjFchMod = ObjFchMod;
    }
    
    
    //------------------------------------------------
    //PARA MOODLE
    //------------------------------------------------

    /**
     *
     * @return Retorna el nombre completo (EstudioNombre + Año)
     */
    public String getMdlFullName(){
        return this.getEstudioNombre() + " | Año: " + dateFormat.format(this.getPeriodo().getPerFchIni());
    }
    
    /**
     *
     * @return Retorna nombre corto (GetPrimaryKey + Nombre de Persona + Valor del período)
     */
    public String getMdlShortName(){
        return this.GetPrimaryKey() +"_"+ this.getPeriodo().getPerTpoNombre() +"_"+ this.getPeriodo().getPerVal(); 
    }
    
    /**
     *
     * @return Retorna descripcion de nombre 
     */
    public String getMdlDscName(){
        return this.getCarreraCursoNombre() + "\n" + this.getPeriodo().TextoPeriodo();
    }
    
    //------------------------------------------------
    
    /**
     *
     * @return Retorna la lista de alumnos
     */
    @JsonIgnore
    @XmlTransient
    public List<PeriodoEstudioAlumno> getLstAlumno() {
        return lstAlumno;
    }

    /**
     *
     * @param lstAlumno Recibe lista de alumnos
     */
    public void setLstAlumno(List<PeriodoEstudioAlumno> lstAlumno) {
        this.lstAlumno = lstAlumno;
    }

    /**
     *
     * @return Retorna lista de docentes
     */
    @JsonIgnore
    @XmlTransient
    public List<PeriodoEstudioDocente> getLstDocente() {
        return lstDocente;
    }

    /**
     *
     * @param lstDocente Recibe lista de docentess
     */
    public void setLstDocente(List<PeriodoEstudioDocente> lstDocente) {
        this.lstDocente = lstDocente;
    }

    /**
     *
     * @return Retorna lista Documentos
     */
    @JsonIgnore
    @XmlTransient
    public List<PeriodoEstudioDocumento> getLstDocumento() {
        if(lstDocumento ==  null) lstDocumento = new ArrayList();
        return lstDocumento;
    }

    /**
     *
     * @param lstDocumento Recibe lista Documentos
     */
    public void setLstDocumento(List<PeriodoEstudioDocumento> lstDocumento) {
        this.lstDocumento = lstDocumento;
    }

    /**
     *
     * @return Retorna EstudioNombre de (Materia o Módulo)
     */
    public String getEstudioNombre()
    {
        if(this.Materia != null) return this.Materia.getMatNom();
        
        if(this.Modulo != null) return this.Modulo.getModNom();
        
        return "";
    }
    
    /**
     *
     * @return retorna Cantidad de Alumnos
     */
    public int getCantidadAlumnos()
    {
        if(this.lstAlumno != null) return this.lstAlumno.size();
        
        return 0;
    }
    
    /**
     *
     * @return retorna cantidad de docentes
     */
    public int getCantidadDocente()
    {
        if(this.lstDocente != null) return this.lstDocente.size();
        
        return 0;
    }
    
    /**
     *
     * @param PeriEstAluCod Recibe el código de Periodo de Estudio Alumno
     * @return Retorna el alumno dado el código recibido
     */
    public PeriodoEstudioAlumno getAlumnoById(Long PeriEstAluCod){
        
        PeriodoEstudioAlumno pAlumno = new PeriodoEstudioAlumno();
        
        for(PeriodoEstudioAlumno alumn : this.lstAlumno)
        {
            if(alumn.getPeriEstAluCod().equals(PeriEstAluCod))
            {
                pAlumno = alumn;
                break;
            }
        }
        
        return pAlumno;
    }
    
    /**
     *
     * @param PeriEstDocCod Recibe el código del período de estudio del docente
     * @return Retorna el docente dado el código recibido
     */
    public PeriodoEstudioDocente getDocenteById(Long PeriEstDocCod){
        
        PeriodoEstudioDocente pDocente = new PeriodoEstudioDocente();
        
        for(PeriodoEstudioDocente docente : this.lstDocente)
        {
            if(docente.getPeriEstDocCod().equals(PeriEstDocCod))
            {
                pDocente = docente;
                break;
            }
        }
        
        return pDocente;
    }
    
    /**
     *
     * @param DocCod Recibe el código de documento
     * @return Retorna si existe el documento dado el código recibido
     */
    public boolean getExisteDocente(Long DocCod){
        
        boolean error;
        PeriodoEstudioDocente pDocente = new PeriodoEstudioDocente();
        
        for(PeriodoEstudioDocente docente : this.lstDocente)
        {
            if(docente.getDocente().getPerCod().equals(DocCod))
            {
                pDocente = docente;
                return true;
            }
        }
        return false;
    }
    
    /**
     *
     * @param DocCod Recibe el código de documento
     * @return Retorna el documento dado el código recibido
     */
    public PeriodoEstudioDocumento getDocumentoById(Long DocCod){
        
        PeriodoEstudioDocumento pDocumento = new PeriodoEstudioDocumento();
        
        for(PeriodoEstudioDocumento documento : this.lstDocumento)
        {
            if(documento.getDocCod().equals(DocCod))
            {
                pDocumento = documento;
                break;
            }
        }
        
        return pDocumento;
    }
    
    /**
     *
     * @return Retorna el tipo estudio (Materia o Módulo)
     */
    public String getEstudioTipo()
    {
        if(this.Materia != null)
        {
            return "MATERIA";
        }
        
        if(this.Modulo != null)
        {
            return "MODULO";
        }
        
        return "";
    }
    
    /**
     *
     * @return retorna el nombre de la Materia o el Módulo
     */
    public String getCarreraCursoNombre()
    {
        if(this.getModulo() != null)
        {
            return this.getModulo().getCurso().getCurNom();
        }
        
        if(this.getMateria() != null)
        {
            return this.getMateria().getPlan().getCarreraPlanNombre();
        }
        
        return "";
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.PeriEstCod);
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
        final PeriodoEstudio other = (PeriodoEstudio) obj;
        if (!Objects.equals(this.PeriEstCod, other.PeriEstCod)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PeriodoEstudio{" + "PeriEstCod=" + PeriEstCod + ", Periodo=" + periodo + ", Materia=" + Materia + ", Modulo=" + Modulo + ", ObjFchMod=" + ObjFchMod + '}';
    }
    
    @Override
    public Long GetPrimaryKey() {
        return this.PeriEstCod;
    }
}


