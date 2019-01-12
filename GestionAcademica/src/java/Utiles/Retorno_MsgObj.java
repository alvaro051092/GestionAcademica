/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utiles;

import Entidad.*;
import Enumerado.TipoMensaje;
import SDT.SDT_PersonaEstudio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Utilidad Retorno_MsgObj
 *
 * @author alvar
 */
@XmlRootElement
@XmlSeeAlso({Persona.class, Calendario.class, CalendarioAlumno.class, CalendarioDocente.class, Carrera.class, Curso.class, Escolaridad.class, Evaluacion.class, Inscripcion.class, 
    Materia.class, MateriaPrevia.class, MateriaRevalida.class, Modulo.class, Periodo.class, PeriodoEstudio.class, PeriodoEstudioAlumno.class, PeriodoEstudioDocente.class, 
    PlanEstudio.class, PeriodoEstudioDocumento.class, Solicitud.class, TipoEvaluacion.class, SDT_PersonaEstudio.class, Objeto.class, ObjetoCampo.class, SincRegistroEliminado.class, Sincronizacion.class})
public class Retorno_MsgObj implements Serializable{

    private Mensajes mensaje;
    private ArrayList<Mensajes> lstMensajes;
    private Object   objeto;
    private List<Object> lstObjetos;
    
    public Retorno_MsgObj() {
    }

    /**
     *
     * @param mensaje Recibe un mensaje
     * @param objeto Recibe un Objeto
     */
    public Retorno_MsgObj(Mensajes mensaje, Object objeto) {
        this.mensaje = mensaje;
        this.objeto = objeto;
    }

    /**
     *
     * @param lstMensajes Recibe una lista de mensajes
     * @param objeto Recibe un objeto
     */
    public Retorno_MsgObj(ArrayList<Mensajes> lstMensajes, Object objeto) {
        this.lstMensajes = lstMensajes;
        this.objeto = objeto;
    }
    
    /**
     *
     * @param mensaje Recibe un mensaje
     * @param lstObjetos Recibe una lista de objetos
     */
    public Retorno_MsgObj(Mensajes mensaje, ArrayList<Object> lstObjetos) {
        this.mensaje = mensaje;
        this.lstObjetos = lstObjetos;
    }

    /**
     *
     * @param mensaje Recibe un mensaje
     */
    public Retorno_MsgObj(Mensajes mensaje) {
        this.mensaje = mensaje;
    }
    
    /**
     *
     * @return Retorna una lista de mensajes
     */
    public ArrayList<Mensajes> getLstMensajes() {
        if (lstMensajes == null) {
            lstMensajes = new ArrayList<>();
        }
        return lstMensajes;
    }

    /**
     *
     * @param lstMensajes Recibe una lista de mensajes 
     */
    public void setLstMensajes(ArrayList<Mensajes> lstMensajes) {
        this.lstMensajes = lstMensajes;
    }
    
    /**
     *
     * @param mensaje Recibe un mensaje y lo agrega a la lista
     */
    public void addMensaje(Mensajes mensaje)
    {
        this.lstMensajes.add(mensaje);
    }
    
    /**
     *
     * @return Retorna un mensaje
     */
    public Mensajes getMensaje() {
        return mensaje;
    }

    /**
     *
     * @param mensaje Recibe un mensaje
     */
    public void setMensaje(Mensajes mensaje) {
        this.mensaje = mensaje;
    }

    /**
     *
     * @return Retorna un objeto
     */
    public Object getObjeto() {
        return objeto;
    }

    /**
     *
     * @param objeto Recibe un objeto
     */
    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }

    /**
     *
     * @return Retorna una lista de objetos
     */
    public List<Object> getLstObjetos() {
        if (lstObjetos == null) {
            lstObjetos = new ArrayList<>();
        }
        return lstObjetos;
    }

    /**
     *
     * @param lstObjetos Recibe una lista de Objetos
     */
    public void setLstObjetos(List<Object> lstObjetos) {
        this.lstObjetos = lstObjetos;
    }

    /**
     *
     * @return Retorna si el mensaje es de tipo error
     */
    public boolean SurgioError()
    {
        if(this.getMensaje() == null) return true;
        return this.getMensaje().getTipoMensaje() == TipoMensaje.ERROR;
    }
    
    /**
     *
     * @return Retorna si el objeto requerido tiene un mensaje de tipo error o si es nulo
     */
    public boolean SurgioErrorObjetoRequerido()
    {
        if(this.getMensaje() == null) return true;
        return this.getMensaje().getTipoMensaje() == TipoMensaje.ERROR || this.getObjeto() == null;
    }
 
    /**
     *
     * @return Retorna si la lista es nula o el tipo de mensaje que contiene es de tipo error
     */
    public boolean SurgioErrorListaRequerida()
    {
        if(this.getMensaje() == null) return true;
        
        return this.getMensaje().getTipoMensaje() == TipoMensaje.ERROR || this.getLstObjetos() == null;
    }
    
    /**
     *
     * @return Retorna si la lista contiene un tipo de mensaje Error o no
     */
    public boolean SurgioErrorListaMensajes(){
        for(Mensajes msg : getLstMensajes())
        {
            if(msg.getTipoMensaje().equals(TipoMensaje.ERROR)) return true;
        }
        
        return false;
    }

    @Override
    public String toString() {
        return "Retorno_MsgObj{" + "mensaje=" + mensaje + ", lstMensajes=" + lstMensajes + ", objeto=" + objeto + ", lstObjetos=" + lstObjetos + '}';
    }
    
    
    
    
}
