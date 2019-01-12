/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Enumerado.Proceso;
import Enumerado.TipoMensaje;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

/**
 *  Entidad Proceso Bitácora
 * 
 * @author alvar
 */
@Entity
@Table(name = "BITACORA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BitacoraProceso.findAll",       query = "SELECT t FROM BitacoraProceso t order by t.BitFch desc"),
    @NamedQuery(name = "BitacoraProceso.findByProceso",       query = "SELECT t FROM BitacoraProceso t WHERE t.BitProceso =:BitProceso ORDER by t.BitFch desc"),
    @NamedQuery(name = "BitacoraProceso.findByBeforeDate",    query = "SELECT t FROM BitacoraProceso t WHERE t.BitFch <= :BitFch")})

public class BitacoraProceso implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "BitCod", nullable = false)
    private Long BitCod;
    
    @Column(name = "BitProceso")
    private Proceso BitProceso;
    
    @Column(name = "BitFch", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date BitFch;
    
    @Column(name = "BitMsg", length = 4000)
    private String BitMsg;
    
    @Column(name = "BitEst")
    private TipoMensaje BitEst;
    
    @OneToMany(targetEntity = BitacoraDetalle.class, cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="BitCod")
    @Fetch(FetchMode.SUBSELECT)
    private List<BitacoraDetalle> lstDetalle;
    
    
    //-CONSTRUCTOR    
    public BitacoraProceso() {    
    }
    
    public BitacoraProceso(Proceso BitProceso, Date BitFch, String BitMsg, TipoMensaje BitEst) {
        this.BitProceso = BitProceso;
        this.BitFch = BitFch;
        this.BitMsg = BitMsg;
        this.BitEst = BitEst;
    }

    
    
    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorna el código de Bitácora
     */

    public Long getBitCod() {
        return BitCod;
    }

    /**
     *
     * @param BitCod Recibe el código de Bitácora
     */
    public void setBitCod(Long BitCod) {
        this.BitCod = BitCod;
    }

    /**
     *
     * @return Retorna el proceso de Bitácora
     */
    public Proceso getBitProceso() {
        return BitProceso;
    }

    /**
     *
     * @param BitProceso Recibe el proceso de Bitácora
     */
    public void setBitProceso(Proceso BitProceso) {
        this.BitProceso = BitProceso;
    }

    /**
     *
     * @return Retorna la fecha de Bitácora
     */
    public Date getBitFch() {
        return BitFch;
    }

    /**
     *
     * @param BitFch Recibe la fehca de Bitácora
     */
    public void setBitFch(Date BitFch) {
        this.BitFch = BitFch;
    }

    /**
     *
     * @return Retorana el mensaje de Bitácora
     */
    public String getBitMsg() {
        return BitMsg;
    }

    /**
     *
     * @param BitMsg Recibe el mensaje de Bitácora
     */
    public void setBitMsg(String BitMsg) {
        this.BitMsg = BitMsg;
    }

    /**
     *
     * @return Retorna el estado de Bitácora
     */
    public TipoMensaje getBitEst() {
        return BitEst;
    }

    /**
     *
     * @param BitEst Recibe el estado de Bitácora
     */
    public void setBitEst(TipoMensaje BitEst) {
        this.BitEst = BitEst;
    }

    /**
     *
     * @return Retorna la lista de Detalles de Bitácora
     */
    public List<BitacoraDetalle> getLstDetalle() {
        if(lstDetalle == null) lstDetalle = new ArrayList<>();
        return lstDetalle;
    }

    /**
     *
     * @param lstDetalle Recibe la lista de Detalles de Bitácora
     */
    public void setLstDetalle(List<BitacoraDetalle> lstDetalle) {
        this.lstDetalle = lstDetalle;
    }
    
    //------------------------------------------------------------------------
    //CUSTOM METHODS
    //------------------------------------------------------------------------

    /**
     *
     * @return Retorna 
     */
    
    public Retorno_MsgObj toRetorno(){
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes(this.getBitMsg(), this.getBitEst()));
        
        for(BitacoraDetalle bitDet : this.getLstDetalle())
        {
            retorno.getLstMensajes().add(new Mensajes(bitDet.getBitDetMsg(), bitDet.getBitDetEst()));
        }
        
        return retorno;
    }
    
    /**
     *
     * @param retorno
     * @param proceso
     */
    public void fromRetorno(Retorno_MsgObj retorno, Proceso proceso){
        this.setBitEst(retorno.getMensaje().getTipoMensaje());
        this.setBitMsg(retorno.getMensaje().getMensaje());
        this.setBitProceso(proceso);
        this.setBitFch(new Date());
        
        for(Mensajes msg : retorno.getLstMensajes())
        {
            this.getLstDetalle().add(new BitacoraDetalle(this, msg.getMensaje(), msg.getTipoMensaje()));
        }
    }
    
    //------------------------------------------------------------------------

    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.BitCod);
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
        final BitacoraProceso other = (BitacoraProceso) obj;
        if (!Objects.equals(this.BitCod, other.BitCod)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BitacoraProceso{" + "BitCod=" + BitCod + ", BitProceso=" + BitProceso + ", BitFch=" + BitFch + ", BitMsg=" + BitMsg + ", BitEst=" + BitEst + ", lstDetalle=" + lstDetalle + '}';
    }
}
