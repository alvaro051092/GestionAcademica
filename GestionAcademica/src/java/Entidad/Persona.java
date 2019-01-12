/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Dominio.SincHelper;
import Enumerado.Filial;
import Enumerado.Genero;
import Enumerado.TipoArchivo;
import SDT.SDT_PersonaEstudio;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad persona
 * 
 * @author alvar
 */
@Entity
@Table(name = "PERSONA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Persona.findAll",           query = "SELECT p FROM Persona p"),
    @NamedQuery(name = "Persona.findModAfter",      query = "SELECT p FROM Persona p WHERE p.ObjFchMod >= :ObjFchMod"),
    @NamedQuery(name = "Persona.findByPerCod",      query = "SELECT p FROM Persona p WHERE p.PerCod     = :PerCod"),
    @NamedQuery(name = "Persona.findLast",          query = "SELECT p FROM Persona p order by p.PerCod     desc"),
    @NamedQuery(name = "Persona.findByMdlUsr",      query = "SELECT p FROM Persona p WHERE p.PerUsrMod  = :MdlUsr"),
    @NamedQuery(name = "Persona.findByPerNom",      query = "SELECT p FROM Persona p WHERE p.PerNom     = :PerNom"),
    @NamedQuery(name = "Persona.findByEmail",       query = "SELECT p FROM Persona p WHERE p.PerEml     = :PerEml"),
    @NamedQuery(name = "Persona.findByAppTkn",      query = "SELECT p FROM Persona p WHERE p.PerAppTkn   = :PerAppTkn"),

/*    
    @NamedQuery(name = "Persona.findPopUp",         query = "SELECT p FROM Persona p "
            + "WHERE (p.PerEsAdm = :PerEsAdm or :PerEsAdm is null) "
            + "AND (p.PerEsAlu = :PerEsAlu or :PerEsAlu is null) "
            + "AND (p.PerEsDoc = :PerEsDoc or :PerEsDoc is null) "
            + "AND (p.PerNom like :PerNom or :PerNom is null) "
            + "AND (p.PerApe like :PerApe or :PerApe is null) "
            + "AND (p.PerCod in (SELECT e.AluPerCod FROM Inscripcion e where e.Curso.CurCod = :CurCod) or :CurCod is null)"),
  */  
    @NamedQuery(name = "Persona.findLastPersona",   query = "SELECT p FROM Persona p ORDER BY p.PerCod DESC")})

public class Persona extends SincHelper implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "PerCod", nullable = false)
    private Long PerCod;
    
    @Column(name = "PerNom", length = 100)
    private String PerNom;
    
    @Column(name = "PerApe", length = 100)
    private String PerApe;
    
    @Column(name = "PerApe2", length = 100)
    private String PerApe2;
    
    @Column(name = "PerDoc", length = 15)
    private String PerDoc;
    
    @Column(name = "PerUsrMod", length = 255, unique = true)
    private String PerUsrMod;
    
    @Column(name = "PerUsrModID")
    private Long PerUsrModID;
    
    @Column(name = "PerEsDoc")
    private Boolean PerEsDoc;
    
    @Column(name = "PerEsAdm")
    private Boolean PerEsAdm;
    
    @Column(name = "PerEsAlu")
    private Boolean PerEsAlu;
    
    @Column(name = "PerNroLib")
    private Integer PerNroLib;
    
    @Column(name = "PerNroEstOrt")
    private Integer PerNroEstOrt;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "PerFil")
    private Filial PerFil;
    
    @Column(name = "PerEml", length = 255, unique = true)
    private String PerEml;
    
    @Column(name = "PerNotEml")
    private Boolean PerNotEml;
    
    @Column(name = "PerNotApp")
    private Boolean PerNotApp;
    
    @Column(name = "PerPass", length = 500)
    private String PerPass;
    
    @Column(name = "PerPassAux", length = 500)
    private String PerPassAux;
    
    @Column(name = "PerAppTkn", length = 500)
    private String PerAppTkn;
    
    @Column(name = "PerLgnTkn", length = 500)
    private String PerLgnTkn;
    
    @Column(name = "PerCntIntLgn")
    private Integer PerCntIntLgn;
    
    @Column(name = "PerBeca", precision=10, scale=2)
    private Double PerBeca;
    
    @Column(name = "PerTpoBeca", length = 255)
    private String PerTpoBeca;
    
    @Column(name = "PerDir", length = 255)
    private String PerDir;
    
    @Column(name = "PerCiudad", length = 255)
    private String PerCiudad;
    
    @Column(name = "PerDto", length = 255)
    private String PerDto;
    
    @Column(name = "PerPais", length = 255)
    private String PerPais;
    
    @Column(name = "PerTel", length = 500)
    private String PerTel;
    
    @Column(name = "PerSecApr", length = 255)
    private String PerSecApr;
    
    @Column(name = "PerProf", length = 255)
    private String PerProf;
    
    @Column(name = "PerObs", length = 4000)
    private String PerObs;
    
    @Column(name = "PerGen")
    private Genero PerGen;
    
    @Column(name = "PerFchNac", columnDefinition="DATE")
    @Temporal(TemporalType.DATE)
    private Date PerFchNac;
    
    @Column(name = "PerFchLog", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date PerFchLog;
    
    @Column(name = "ObjFchMod", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ObjFchMod;

    @Transient
    private ArrayList<SDT_PersonaEstudio> lstEstudios;
    
    @Transient
    private Date FechaInicio;
    
    @OneToOne(targetEntity = Archivo.class)
    @JoinColumn(name="ArcCod")
    private Archivo archivo;

    public Persona() {
        PerCntIntLgn    = 0;
        PerFil          = Filial.COLONIA;
        PerEsAdm        = Boolean.FALSE;
        PerEsAlu        = Boolean.FALSE;
        PerEsDoc        = Boolean.FALSE;
        PerNotApp       = Boolean.TRUE;
        PerNotEml       = Boolean.TRUE;
        PerPass         = "";
    }

    public Persona(Long PerCod, String PerNom, String PerApe, String PerUsrMod, Boolean PerEsDoc, Boolean PerEsAdm, Boolean PerEsAlu, Integer PerNroLib, Integer PerNroEstOrt, Filial PerFil, String PerEml, Boolean PerNotEml, Boolean PerNotApp, Date ObjFchMod) {
        this.PerCod = PerCod;
        this.PerNom = PerNom;
        this.PerApe = PerApe;
        this.PerUsrMod = PerUsrMod;
        this.PerEsDoc = PerEsDoc;
        this.PerEsAdm = PerEsAdm;
        this.PerEsAlu = PerEsAlu;
        this.PerNroLib = PerNroLib;
        this.PerNroEstOrt = PerNroEstOrt;
        this.PerFil = PerFil;
        this.PerEml = PerEml;
        this.PerNotEml = PerNotEml;
        this.PerNotApp = PerNotApp;
        this.ObjFchMod = ObjFchMod;
    }
 
    
    /** 
    * @return Retorna el código de la persona
    */
    public Long getPerCod() {
        return PerCod;
    }

    /**
    *@param PerCod Código de una persona
    */
    public void setPerCod(Long PerCod) {
        this.PerCod = PerCod;
    }

    /**
     *
     * @return Nombre de persona
     */
    public String getPerNom() {
        if(PerNom == null) PerNom = "";
        return PerNom;
    }

    /**
     *
     * @param PerNom Nombre de persona
     */
    public void setPerNom(String PerNom) {
        this.PerNom = PerNom;
    }

    /**
     *
     * @return Apellido
     */
    public String getPerApe() {
        if(PerApe == null) PerApe = "";
        return PerApe;
    }

    /**
     *
     * @param PerApe Apellido
     */
    public void setPerApe(String PerApe) {
        this.PerApe = PerApe;
    }

    /**
     *
     * @return Segundo apellido
     */
    public String getPerApe2() {
        if(PerApe2 == null) PerApe2 = "";
        return PerApe2;
    }

    /**
     *
     * @param PerApe2 Segundo apellido
     */
    public void setPerApe2(String PerApe2) {
        this.PerApe2 = PerApe2;
    }
    
    /**
     *
     * @return Usuario en moodle
     */
    public String getPerUsrMod() {
        return PerUsrMod;
    }

    /**
     *
     * @param PerUsrMod Usuario en moodle
     */
    public void setPerUsrMod(String PerUsrMod) {
        this.PerUsrMod = PerUsrMod.toLowerCase();
    }

    /**
     *
     * @return Es docente
     */
    public Boolean getPerEsDoc() {
        if(PerEsDoc == null) PerEsDoc = Boolean.FALSE;
        return PerEsDoc;
    }

    /**
     *
     * @param PerEsDoc Es docente
     */
    public void setPerEsDoc(Boolean PerEsDoc) {
        this.PerEsDoc = PerEsDoc;
    }

    /**
     *
     * @return Es administrador
     */
    public Boolean getPerEsAdm() {
        if(PerEsAdm == null) PerEsAdm = Boolean.FALSE;
        return PerEsAdm;
    }

    /**
     *
     * @param PerEsAdm Es administrador
     */
    public void setPerEsAdm(Boolean PerEsAdm) {
        this.PerEsAdm = PerEsAdm;
    }

    /**
     *
     * @return Es alumno
     */
    public Boolean getPerEsAlu() {
        if(PerEsAlu == null) PerEsAlu = Boolean.FALSE;
        return PerEsAlu;
    }

    /**
     *
     * @param PerEsAlu Es alumno
     */
    public void setPerEsAlu(Boolean PerEsAlu) {
        this.PerEsAlu = PerEsAlu;
    }

    /**
     *
     * @return Número en libra
     */
    public Integer getPerNroLib() {
        return PerNroLib;
    }

    /**
     *
     * @param PerNroLib Número en libra
     */
    public void setPerNroLib(Integer PerNroLib) {
        this.PerNroLib = PerNroLib;
    }

    /**
     *
     * @return Número estudiante ORT
     */
    public Integer getPerNroEstOrt() {
        return PerNroEstOrt;
    }

    /**
     *
     * @param PerNroEstOrt Número estudiante ORT
     */
    public void setPerNroEstOrt(Integer PerNroEstOrt) {
        this.PerNroEstOrt = PerNroEstOrt;
    }

    /**
     *
     * @return Filial
     */
    public Filial getPerFil() {
        return PerFil;
    }

    /**
     *
     * @param PerFil Filial
     */
    public void setPerFil(Filial PerFil) {
        this.PerFil = PerFil;
    }

    /**
     *
     * @return Email
     */
    public String getPerEml() {
        return PerEml;
    }

    /**
     *
     * @param PerEml Email
     */
    public void setPerEml(String PerEml) {
        this.PerEml = PerEml;
    }

    /**
     *
     * @return Notificar por Email
     */
    public Boolean getPerNotEml() {
        return PerNotEml;
    }

    /**
     *
     * @param PerNotEml Notificar por EMail
     */
    public void setPerNotEml(Boolean PerNotEml) {
        this.PerNotEml = PerNotEml;
    }

    /**
     *
     * @return Notificar por app
     */
    public Boolean getPerNotApp() {
        return PerNotApp;
    }

    /**
     *
     * @param PerNotApp Notificar por APP
     */
    public void setPerNotApp(Boolean PerNotApp) {
        this.PerNotApp = PerNotApp;
    }

    /**
     *
     * @return Fecha de modificado
     */
    public Date getObjFchMod() {
        return ObjFchMod;
    }

    /**
     *
     * @param ObjFchMod Fecha de modificado
     */
    public void setObjFchMod(Date ObjFchMod) {
        this.ObjFchMod = ObjFchMod;
    }

    /**
     *
     * @return Password
     */
    public String getPerPass() {
        return PerPass;
    }

    /**
     *
     * @param PerPass Password
     */
    public void setPerPass(String PerPass) {
        this.PerPass = PerPass;
    }

    /**
     *
     * @return ID de usuario en moodle
     */
    public Long getPerUsrModID() {
        return PerUsrModID;
    }

    /**
     *
     * @param PerUsrModID ID de usuario en moodle
     */
    public void setPerUsrModID(Long PerUsrModID) {
        this.PerUsrModID = PerUsrModID;
    }

    /**
     *
     * @return Fecha de login
     */
    public Date getPerFchLog() { 
        return PerFchLog;
    }

    /**
     *
     * @param PerFchLog Fecha de login
     */
    public void setPerFchLog(Date PerFchLog) {
        this.PerFchLog = PerFchLog;
    }

    /**
     *
     * @return Intentos de login
     */
    public Integer getPerCntIntLgn() {
        return PerCntIntLgn;
    }

    /**
     *
     * @param PerCntIntLgn Intentos de login
     */
    public void setPerCntIntLgn(Integer PerCntIntLgn) {
        this.PerCntIntLgn = PerCntIntLgn;
    }
 
    /**
     *
     * @return Documento
     */
    public String getPerDoc() {
        return PerDoc;
    }

    /**
     *
     * @param PerDoc Documento
     */
    public void setPerDoc(String PerDoc) {
        this.PerDoc = PerDoc;
    }

    /**
     *
     * @return Obtener token de app
     */
    public String getPerAppTkn() {
        return PerAppTkn;
    }

    /**
     *
     * @param PerAppTkn Guardar token de app
     */
    public void setPerAppTkn(String PerAppTkn) {
        this.PerAppTkn = PerAppTkn;
    }

    /**
     *
     * @return Login token
     */
    public String getPerLgnTkn() {
        return PerLgnTkn;
    }

    /**
     *
     * @param PerLgnTkn Login token
     */
    public void setPerLgnTkn(String PerLgnTkn) {
        this.PerLgnTkn = PerLgnTkn;
    }

    /**
     *
     * @return Porcentaje de beca
     */
    public Double getPerBeca() {
        return PerBeca;
    }

    /**
     *
     * @param PerBeca Porcentaje de beca
     */
    public void setPerBeca(Double PerBeca) {
        this.PerBeca = PerBeca;
    }

    /**
     *
     * @return Tipo de beca
     */
    public String getPerTpoBeca() {
        return PerTpoBeca;
    }

    /**
     *
     * @param PerTpoBeca  Tipo de beca
     */
    public void setPerTpoBeca(String PerTpoBeca) {
        this.PerTpoBeca = PerTpoBeca;
    }

    /**
     *
     * @return Direccion
     */
    public String getPerDir() {
        return PerDir;
    }

    /**
     *
     * @param PerDir Direccion
     */
    public void setPerDir(String PerDir) {
        this.PerDir = PerDir;
    }

    /**
     *
     * @return Ciudad
     */
    public String getPerCiudad() {
        return PerCiudad;
    }

    /**
     *
     * @param PerCiudad Ciudad
     */
    public void setPerCiudad(String PerCiudad) {
        this.PerCiudad = PerCiudad;
    }

    /**
     *
     * @return Departamento
     */
    public String getPerDto() {
        if(PerDto == null) PerDto = "Colonia";
        return PerDto;
    }

    /**
     *
     * @param PerDto Departamento
     */
    public void setPerDto(String PerDto) {
        this.PerDto = PerDto;
    }

    /**
     *
     * @return Pais
     */
    public String getPerPais() {
        if(PerPais == null) PerPais = "Uruguay";
        return PerPais;
    }

    /**
     *
     * @param PerPais Pais
     */
    public void setPerPais(String PerPais) {
        this.PerPais = PerPais;
    }

    /**
     *
     * @return Telefono
     */
    public String getPerTel() {
        return PerTel;
    }

    /**
     *
     * @param PerTel Telefono
     */
    public void setPerTel(String PerTel) {
        this.PerTel = PerTel;
    }

    /**
     *
     * @return Fecha de nacimiento
     */
    public Date getPerFchNac() {
        return PerFchNac;
    }

    /**
     *
     * @param PerFchNac Fecha de nacimiento
     */
    public void setPerFchNac(Date PerFchNac) {
        this.PerFchNac = PerFchNac;
    }

    /**
     *
     * @return Secundaria aprobado
     */
    public String getPerSecApr() {
        return PerSecApr;
    }

    /**
     *
     * @param PerSecApr Secundaria aprobado
     */
    public void setPerSecApr(String PerSecApr) {
        this.PerSecApr = PerSecApr;
    }

    /**
     *
     * @return Genero
     */
    public Genero getPerGen() {
        return PerGen;
    }

    /**
     *
     * @param PerGen Genero
     */
    public void setPerGen(Genero PerGen) {
        this.PerGen = PerGen;
    }

    /**
     *
     * @return Profesion
     */
    public String getPerProf() {
        return PerProf;
    }

    /**
     *
     * @param PerProf Profesion
     */
    public void setPerProf(String PerProf) {
        this.PerProf = PerProf;
    }

    /**
     *
     * @return Observaciones
     */
    public String getPerObs() {
        return PerObs;
    }

    /**
     *
     * @param PerObs Observaciones
     */
    public void setPerObs(String PerObs) {
        this.PerObs = PerObs;
    }
    
    /**
     *
     * @return Foto de perfil
     */
    @JsonIgnore
    @XmlTransient
    public File getFoto(){
        if(this.archivo == null) return null;
        return this.archivo.getArchivo();
    }

    /**
     *
     * @param pArchivo Foto de perfil
     */
    public void setFoto(Archivo pArchivo) {
        this.archivo = pArchivo;
    }
    
    @JsonIgnore
    /**
     *
     * @return Foto en base64
     */
    public String getFotoBase64(){
        if(this.archivo == null) return null;
        return this.archivo.getFileBase64();
    }
    
    @JsonIgnore
    public String getFotoExtension(){
        return this.archivo.getArcExt();
    }

    public String getPerPassAux() {
        return PerPassAux;
    }

    public void setPerPassAux(String PerPassAux) {
        this.PerPassAux = PerPassAux;
    }

    public Archivo getArchivo() {
        return archivo;
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }

    
    
    
    /**
     *
     * @return Nombre completo
     */
    public String getNombreCompleto()
    {
        return getPerNom() + " " + getPerApe()+ " " + getPerApe2();
    }
    
    /**
     *
     * @return Tipo de persona
     */
    public String getTipoPersona()
    {
        String retorno = "";
        
        if(this.PerEsAdm) retorno += (retorno.isEmpty() ? "Administrador" : ", Administrador");
        if(this.PerEsAlu) retorno += (retorno.isEmpty() ? "Alumno" : ", Alumno");
        if(this.PerEsDoc) retorno += (retorno.isEmpty() ? "Docente" : ", Docente");

        return retorno;
    }

    /**
     *
     * @return lista de estudios
     */
    public ArrayList<SDT_PersonaEstudio> getLstEstudios() {
        return lstEstudios;
    }

    /**
     *
     * @param lstEstudios lista de estudios
     */
    public void setLstEstudios(ArrayList<SDT_PersonaEstudio> lstEstudios) {
        this.lstEstudios = lstEstudios;
    }

    /**
     * Fecha de inicio.
     * No se almacena en base de datos, solo para importacion
     * @return Fecha de inicio
     */
    public Date getFechaInicio() {
        if(FechaInicio == null) FechaInicio = new Date();
        return FechaInicio;
    }

    /**
     * Fecha de inicio.
     * No se almacena en base de datos, solo para importacion
     * @param FechaInicio Fecha de inicio
     */
    public void setFechaInicio(Date FechaInicio) {
        this.FechaInicio = FechaInicio;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (PerCod != null ? PerCod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.PerCod == null && other.PerCod != null) || (this.PerCod != null && !this.PerCod.equals(other.PerCod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidad.Persona[ id=" + PerCod + " ]";
    }
    
    @Override
    public Long GetPrimaryKey() {
        return this.PerCod;
    }
}
