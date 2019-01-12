/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Enumerado.ObtenerDestinatario;
import Enumerado.TipoNotificacion;
import Enumerado.TipoEnvio;
import Enumerado.TipoRepeticion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad Notificacion
 *
 * @author alvar
 */
@Entity
@Table(name = "NOTIFICACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notificacion.findAll",       query = "SELECT t FROM Notificacion t"),
    @NamedQuery(name = "Notificacion.findByNom",       query = "SELECT t FROM Notificacion t WHERE t.NotNom =:NotNom"),
    @NamedQuery(name = "Notificacion.findAutoActiva",       query = "SELECT t FROM Notificacion t WHERE t.NotAct =:NotAct AND t.NotTpo =:NotTpo")

})

public class Notificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "NotCod", nullable = false)
    private Long NotCod;
    
    @Column(name = "NotNom", length = 100)
    private String NotNom;
    @Column(name = "NotDsc", length = 500)
    private String NotDsc;
    @Column(name = "NotCon", length = 4000)
    private String NotCon;
    @Column(name = "NotAsu", length = 100)
    private String NotAsu;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "NotTpo")
    private TipoNotificacion NotTpo;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "NotTpoEnv")
    private TipoEnvio NotTpoEnv;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "NotObtDest")
    private ObtenerDestinatario NotObtDest;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "NotRepTpo")
    private TipoRepeticion NotRepTpo;

    @Column(name = "NotRepVal")
    private Integer NotRepVal;

    @Column(name = "NotRepHst", columnDefinition="DATE")
    @Temporal(TemporalType.DATE)
    private Date NotRepHst;
    
    @Column(name = "NotFchDsd", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date NotFchDsd;

    @Column(name = "NotEmail")
    private Boolean NotEmail;
    @Column(name = "NotApp")
    private Boolean NotApp;
    @Column(name = "NotWeb")
    private Boolean NotWeb;
    @Column(name = "NotAct")
    private Boolean NotAct;
    
    @Column(name = "NotInt")
    private Boolean NotInt;
    
    
    @OneToMany(targetEntity = NotificacionBitacora.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="NotCod", referencedColumnName="NotCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<NotificacionBitacora> lstBitacora;
    
    @OneToMany(targetEntity = NotificacionConsulta.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="NotCod", referencedColumnName="NotCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<NotificacionConsulta> lstConsulta;
    
    @OneToMany(targetEntity = NotificacionDestinatario.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="NotCod", referencedColumnName="NotCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<NotificacionDestinatario> lstDestinatario;
    
        
    //-CONSTRUCTOR
    public Notificacion() {
        this.lstDestinatario = new ArrayList<>();
        this.lstBitacora = new ArrayList<>();
        this.lstConsulta = new ArrayList<>();
        
        this.NotAct     = false;
        this.NotInt     = false;
        
        this.NotWeb     = false;
        this.NotApp     = false;
        this.NotEmail   = false;
       
        
        this.NotAsu = "";
        this.NotCon = "";
        this.NotDsc = "";
        this.NotNom = "";
        
        this.NotObtDest = ObtenerDestinatario.UNICA_VEZ;
        this.NotRepTpo  = TipoRepeticion.SIN_REPETICION;
        this.NotTpo     = TipoNotificacion.A_DEMANDA;
        this.NotTpoEnv  = TipoEnvio.COMUN;
        
        
    }
    
    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorna el Código Notificacion
     */
    public Long getNotCod() {
        return NotCod;
    }

    /**
     *
     * @param NotCod Recibe el código Notificacion
     */
    public void setNotCod(Long NotCod) {
        this.NotCod = NotCod;
    }

    /**
     *
     * @return Retorna el nombre Notificacion
     */
    public String getNotNom() {
        return NotNom;
    }

    /**
     *
     * @param NotNom Recibe el nombre Notificacion
     */ 
    public void setNotNom(String NotNom) {
        this.NotNom = NotNom;
    }

    /**
     *
     * @return Retorna la descripción Notificacion
     */
    public String getNotDsc() {
        return NotDsc;
    }

    /**
     *
     * @param NotDsc Recibe la descripción Notificacion
     */
    public void setNotDsc(String NotDsc) {
        this.NotDsc = NotDsc;
    }

    /**
     *
     * @return Retorna el contenido de Notificacion
     */
    public String getNotCon() {
        return NotCon;
    }

    /**
     *
     * @param NotCon Recibe el contenido de Notificacion
     */
    public void setNotCon(String NotCon) {
        this.NotCon = NotCon;
    }

    /**
     *
     * @return Retorna el asunto de Notificacion
     */
    public String getNotAsu() {
        return NotAsu;
    }

    /**
     *
     * @param NotAsu Recibe el asunto de Notificacion
     */
    public void setNotAsu(String NotAsu) {
        this.NotAsu = NotAsu;
    }

    /**
     *
     * @return Retorna el tipo Notificacion
     */
    public TipoNotificacion getNotTpo() {
        return NotTpo;
    }

    /**
     *
     * @param NotTpo Recibe el tipo Notificacion
     */
    public void setNotTpo(TipoNotificacion NotTpo) {
        this.NotTpo = NotTpo;
    }

    /**
     *
     * @return Retorna el tipo envío Notificacion
     */
    public TipoEnvio getNotTpoEnv() {
        return NotTpoEnv;
    }

    /**
     *
     * @param NotTpoEnv Recibe el tipo envío Notificacion
     */
    public void setNotTpoEnv(TipoEnvio NotTpoEnv) {
        this.NotTpoEnv = NotTpoEnv;
    }

    /**
     *
     * @return Retorna ObtenerDestinatario (por unica ves o por cada registro)
     */
    public ObtenerDestinatario getNotObtDest() {
        return NotObtDest;
    }

    /**
     *
     * @param NotObtDest Recibe obtener destinatario (por unica ves o por cada registro)
     */
    public void setNotObtDest(ObtenerDestinatario NotObtDest) {
        this.NotObtDest = NotObtDest;
    }

    /**
     *
     * @return Retorna el Tipo de repetición: minutos, horas, dias, semanas, meses, años
     */
    public TipoRepeticion getNotRepTpo() {
        return NotRepTpo;
    }

    /**
     *
     * @param NotRepTpo Recibe el Tipo de repetición: minutos, horas, dias, semanas, meses, años
     */
    public void setNotRepTpo(TipoRepeticion NotRepTpo) {
        this.NotRepTpo = NotRepTpo;
    }

    /**
     *
     * @return Retorna la ejecución de alerta por tipo de repetición
     */
    public Integer getNotRepVal() {
        return NotRepVal;
    }

    /**
     *
     * @param NotRepVal Recibe el valor de la ejecución de alerta por tipo de repetición
     */
    public void setNotRepVal(Integer NotRepVal) {
        this.NotRepVal = NotRepVal;
    }

    /**
     *
     * @return Retorna la fecha hasta  que define hasta cuando se tiene que ejecutar la alerta
     */
    public Date getNotRepHst() {
        return NotRepHst;
    }

    /**
     *
     * @param NotRepHst Recibe la fecha hasta  que define hasta cuando se tiene que ejecutar la alerta
     */
    public void setNotRepHst(Date NotRepHst) {
        this.NotRepHst = NotRepHst;
    }

    /**
     *
     * @return Retorna el Email de la notificación
     */
    public Boolean getNotEmail() {
        return NotEmail;
    }

    /**
     *
     * @param NotEmail Recibe el email de la notificación
     */
    public void setNotEmail(Boolean NotEmail) {
        this.NotEmail = NotEmail;
    }

    /**
     *
     * @return Retorna si debe o no notificar a la aplicación android
     */
    public Boolean getNotApp() {
        return NotApp;
    }

    /**
     *
     * @param NotApp Recibe si debe o no notificar a la aplicación android
     */
    public void setNotApp(Boolean NotApp) {
        this.NotApp = NotApp;
    }

    /**
     *
     * @return Retorna si debe o no notificar a la web
     */
    public Boolean getNotWeb() {
        return NotWeb;
    }

    /**
     *
     * @param NotWeb Recibe si debe o no notificar a la web
     */
    public void setNotWeb(Boolean NotWeb) {
        this.NotWeb = NotWeb;
    }

    /**
     *
     * @return Retorno si la notificación esta activa o no
     */
    public Boolean getNotAct() {
        return NotAct;
    }

    /**
     *
     * @param NotAct Recibe si la notificación esta activa o no
     */
    public void setNotAct(Boolean NotAct) {
        this.NotAct = NotAct;
    }

    /**
     *
     * @return Retorna la lista de bitacora
     */
    public List<NotificacionBitacora> getLstBitacora() {
        return lstBitacora;
    }

    /**
     *
     * @param lstBitacora Recibe la lista de bitacora
     */
    public void setLstBitacora(List<NotificacionBitacora> lstBitacora) {
        this.lstBitacora = lstBitacora;
    }

    /**
     *
     * @return Retorna la lista de consulta
     */
    public List<NotificacionConsulta> getLstConsulta() {
        return lstConsulta;
    }

    /**
     *
     * @param lstConsulta Recibe la lista de consulta
     */
    public void setLstConsulta(List<NotificacionConsulta> lstConsulta) {
        this.lstConsulta = lstConsulta;
    }

    /**
     *
     * @return Retorna la lista de destinatarios
     */
    public List<NotificacionDestinatario> getLstDestinatario() {
        return lstDestinatario;
    }

    /**
     *
     * @param lstDestinatario Recibe la lista de destinatarios
     */
    public void setLstDestinatario(List<NotificacionDestinatario> lstDestinatario) {
        this.lstDestinatario = lstDestinatario;
    }

    /**
     *
     * @return 
     */
    public Boolean getNotInt() {
        return NotInt;
    }

    /**
     *
     * @param NotInt
     */
    public void setNotInt(Boolean NotInt) {
        this.NotInt = NotInt;
    }

    /**
     *
     * @return Retorna la fecha desde de notificación
     */
    public Date getNotFchDsd() {
        return NotFchDsd;
    }

    /**
     *
     * @param NotFchDsd Recibe la fecha desde de notificación
     */
    public void setNotFchDsd(Date NotFchDsd) {
        this.NotFchDsd = NotFchDsd;
    }
    
    /**
     *
     * @return Retorna el medio (Aplicacion, email o web)
     */
    public String getMedio(){
        String medio = "";
        
        if(this.NotApp) medio += (medio.equals("") ? "Aplicación" : ", Aplicación");
        if(this.NotEmail) medio += (medio.equals("") ? "Email" : ", Email");
        if(this.NotWeb) medio += (medio.equals("") ? "Web" : ", Web");
        
        return medio;
    }
    
    /**
     *
     * @return Retorna los destinatarios agrupados
     */
    public String ObtenerDestinatariosAgrupados(){
        String destinatarios = "";
        
        for(NotificacionDestinatario destino : this.lstDestinatario)
        {
            if(!destinatarios.equals("")) destinatarios += "\n";
            destinatarios += destino.getNotDstCod() + ": ";
            
            if(destino.getNotEmail() != null) destinatarios += destino.getNotEmail();
            if(destino.getPersona() != null) destinatarios += destino.getPersona().getNombreCompleto();
            
        }
        
        return destinatarios;
    }
    
    /**
     *
     * @param NotDstCod Recibe el código de la NotificaciónDestinatario
     * @return Retorna el destinatario dado el código recibido
     */
    public NotificacionDestinatario ObtenerDestinatarioByCod(Long NotDstCod){
        for(NotificacionDestinatario destinatario : this.lstDestinatario)
        {
            if(destinatario.getNotDstCod().equals(NotDstCod)) return destinatario;
        }
        
        return null;
    }
    
    /**
     *
     * @param NotBitCod Recibe el código de NotificacionBitacora
     * @return Retorna la bitacora dado el código recibido
     */
    public NotificacionBitacora ObtenerBitacoraByCod(Long NotBitCod){
        for(NotificacionBitacora bitacora : this.lstBitacora)
        {
            if(bitacora.getNotBitCod().equals(NotBitCod)) return bitacora;
        }
        
        return null;
    }
    
    /**
     *
     * @param NotCnsCod Recibe el código de NotificacionConsulta
     * @return Retorna la consulta dado el código recibido
     */
    public NotificacionConsulta ObtenerConsultaByCod(Long NotCnsCod){
        for(NotificacionConsulta consulta : this.lstConsulta)
        {
            if(consulta.getNotCnsCod().equals(NotCnsCod)) return consulta;
        }
        
        return null;
    }
    
    /**
     *
     * @return Retorna si la notificación es automatica o no
     */
    public Boolean NotificarAutomaticamente(){
        Boolean notificar   = false;
        
        Calendar fchAct = Calendar.getInstance();
        Calendar fchDsd = Calendar.getInstance();
        
        Boolean continuarFchDsd = false;

        if(!this.NotRepTpo.equals(TipoRepeticion.SIN_REPETICION))
        {
            if(this.NotFchDsd == null)
            {
                continuarFchDsd = true;
            }
            else
            {
                fchDsd.setTime(this.NotFchDsd);
                
                if(fchDsd.compareTo(fchAct) <= 0)
                {
                    continuarFchDsd = true;
                }
            }
            
            if(continuarFchDsd)
            {
                if(this.NotRepHst == null)
                {
                    notificar = this.NotificarAutomaticamenteRango();
                }
                else
                {
                    if(this.NotRepHst.compareTo(fchAct.getTime()) <= 0)
                    {
                        notificar = this.NotificarAutomaticamenteRango();                    
                    }
                }
            }
        }
        
        return notificar;
    }
    
    /**
     *
     * @return Retorna el rango de la notificación automatica
     */
    private Boolean NotificarAutomaticamenteRango()
    {
        Boolean notificar               = false;
        Calendar fechaUltimaNotificacion = Calendar.getInstance();
        
        Date fchPrevia = this.GetLastNotification();
        
        if(fchPrevia == null)
        {
            return true;
        }
        
        fechaUltimaNotificacion.setTime(fchPrevia);
        
        
        switch(this.NotRepTpo)
        {
            case ANIOS:
                fechaUltimaNotificacion.add(Calendar.YEAR, this.NotRepVal);
                break;
            case DIAS:
                fechaUltimaNotificacion.add(Calendar.DAY_OF_MONTH, this.NotRepVal);
                break;
            case HORAS:
                fechaUltimaNotificacion.add(Calendar.HOUR_OF_DAY, this.NotRepVal);
                break;
            case MESES:
                fechaUltimaNotificacion.add(Calendar.MONTH, this.NotRepVal);
                break;
            case MINUTOS:
                fechaUltimaNotificacion.add(Calendar.MINUTE, this.NotRepVal);
                break;
            case SEMANAS:
                fechaUltimaNotificacion.add(Calendar.WEEK_OF_MONTH, this.NotRepVal);
                break;
        }
        
        
        if(Calendar.getInstance().compareTo(fechaUltimaNotificacion) >= 0)
        {
            notificar = true;
        }
        
        return notificar;
    }
    
    /**
     *
     * @return Retorna la fecha de la ultima notificación
     */
    private Date GetLastNotification(){
        Date fecha = null;
        
        if(this.lstBitacora != null)
        {
            if(this.lstBitacora.size() > 0)
            {
                Collections.sort(this.lstBitacora, new Comparator<NotificacionBitacora>() {

                    public int compare(NotificacionBitacora o1, NotificacionBitacora o2) {

                        int sComp = o2.getNotBitFch().compareTo(o1.getNotBitFch());

                        if (sComp != 0) {
                            return sComp;
                        } else {
                            Long x1 = ((NotificacionBitacora) o2).getNotBitCod();
                            Long x2 = ((NotificacionBitacora) o1).getNotBitCod();
                            return x1.compareTo(x2);
                        }
                    }

                });
                
                fecha = this.lstBitacora.get(0).getNotBitFch();
            }
        }
        
        return fecha;        
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (NotCod != null ? NotCod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notificacion)) {
            return false;
        }
        Notificacion other = (Notificacion) object;
        if ((this.NotCod == null && other.NotCod != null) || (this.NotCod != null && !this.NotCod.equals(other.NotCod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidad.Notificacion[ id=" + NotCod + " ]";
    }
    
}
