/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import Enumerado.TipoConsulta;
import java.io.Serializable;
import java.util.Objects;
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
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 * Entidad Notificación Consulta
 *
 * @author alvar
 */
@Entity
@Table(name = "NOTIFICACION_CONSULTA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotificacionConsulta.findAll",       query = "SELECT t FROM NotificacionConsulta t")
})

public class NotificacionConsulta implements Serializable {

    private static final long serialVersionUID = 1L;
    
    //-ATRIBUTOS
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native" )
    @Column(name = "NotCnsCod", nullable = false)
    private Long NotCnsCod;

    @OneToOne(targetEntity = Notificacion.class)
    @JoinColumn(name="NotCod", referencedColumnName="NotCod")
    private Notificacion notificacion;

       
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "NotCnsTpo")
    private TipoConsulta NotCnsTpo;    
    
    @Column(name = "NotCnsSQL", length = 1000)
    private String NotCnsSQL;
    
    
    //-CONSTRUCTOR
    public NotificacionConsulta() {
    }
    
    
    //-GETTERS Y SETTERS

    /**
     *
     * @return Retorna el código
     */
    public Long getNotCnsCod() {
        return NotCnsCod;
    }

    /**
     *
     * @param NotCnsCod Recibe el código
     */
    public void setNotCnsCod(Long NotCnsCod) {
        this.NotCnsCod = NotCnsCod;
    }

    /**
     *
     * @return Retorna la nofiricación
     */
    public Notificacion getNotificacion() {
        return notificacion;
    }

    /**
     *
     * @param notificacion Recibe la notificación
     */
    public void setNotificacion(Notificacion notificacion) {
        this.notificacion = notificacion;
    }

    /**
     *
     * @return Retorna el tipo NotificacionConsulta
     */
    public TipoConsulta getNotCnsTpo() {
        return NotCnsTpo;
    }

    /**
     *
     * @param NotCnsTpo Recibe el tipo NotificacionConsulta
     */
    public void setNotCnsTpo(TipoConsulta NotCnsTpo) {
        this.NotCnsTpo = NotCnsTpo;
    }

    /**
     *
     * @return Retorna la sentencia SQL de NotificacionConsulta
     */
    public String getNotCnsSQL() {
        return NotCnsSQL;
    }

    /**
     *
     * @param NotCnsSQL Recibe la sentencia SQL de NotificacionConsulta
     */
    public void setNotCnsSQL(String NotCnsSQL) {
        this.NotCnsSQL = NotCnsSQL;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.NotCnsCod);
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
        final NotificacionConsulta other = (NotificacionConsulta) obj;
        if (!Objects.equals(this.NotCnsCod, other.NotCnsCod)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NotificacionConsulta{" + "NotCnsCod=" + NotCnsCod + ", notificacion=" + notificacion + ", NotCnsTpo=" + NotCnsTpo + ", NotCnsSQL=" + NotCnsSQL + '}';
    }
    
    
    
    
    
    
    
}

