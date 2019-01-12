/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDT;

import Utiles.JSonDateyyyyMMddSerializer;
import java.io.Serializable;
import java.util.Date;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * SDT SDT_Evento
 *
 * @author alvar
 */

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class SDT_Evento implements Serializable{
    /*
    @Documentacion https://fullcalendar.io/docs/event_data/Event_Object/
    */
    private Long id;
    private String title;
    private Boolean allDay;
    private Date start;
    private Date end;
    private Boolean editable;
    private Boolean startEditable;
    private Boolean durationEditable;
    private Boolean resourceEditable;
    private String description;
    
    public SDT_Evento() {
    }

    /**
     *
     * @return Retorna id Evento
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id Recibe id Evento
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return Retorna titulo de Evento
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title Recibe titulo de Evento
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return Retorna Booleano (Si, No)
     */
    public Boolean getAllDay() {
        return allDay;
    }

    /**
     *
     * @param allDay Recibe (Si, No)
     */
    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    /**
     *
     * @return Retorno comienso
     */
    public Date getStart() {
        return start;
    }

    /**
     *
     * @param start Recibe comienso
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     *
     * @return Retorna final
     */
    @JsonSerialize(using=JSonDateyyyyMMddSerializer.class)
    public Date getEnd() {
        return end;
    }

    /**
     *
     * @param end Recibe final
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     *
     * @return Retorna Editable (Si, No)
     */
    public Boolean getEditable() {
        return editable;
    }

    /**
     *
     * @param editable Recibe Editable (Si, No)
     */
    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    /**
     *
     * @return Retorna comienso editable (Si, No)
     */
    public Boolean getStartEditable() {
        return startEditable;
    }

    /**
     *
     * @param startEditable Recibe comienzo editable (Si, No)
     */
    public void setStartEditable(Boolean startEditable) {
        this.startEditable = startEditable;
    }

    /**
     *
     * @return Retorno Duracion Editable (Si, No)
     */
    public Boolean getDurationEditable() {
        return durationEditable;
    }

    /**
     *
     * @param durationEditable Recibe duracion Editable (Si, No)
     */
    public void setDurationEditable(Boolean durationEditable) {
        this.durationEditable = durationEditable;
    }

    /**
     *
     * @return retorna Resource Editable (Si, No)
     */
    public Boolean getResourceEditable() {
        return resourceEditable;
    }

    /**
     *
     * @param resourceEditable Recibe Resource Editable (Si, No)
     */
    public void setResourceEditable(Boolean resourceEditable) {
        this.resourceEditable = resourceEditable;
    }

    /**
     *
     * @return Retorna Descripción
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description Recibe Descripción
     */
    public void setDescription(String description) {
        this.description = description;
    }

    
    
}
