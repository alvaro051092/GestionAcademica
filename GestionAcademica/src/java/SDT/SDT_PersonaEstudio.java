/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDT;

import Entidad.Escolaridad;
import Entidad.Inscripcion;
import java.io.Serializable;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * SDT SDT_PersonaEstudio
 *
 * @author alvar
 */
@XmlRootElement
public class SDT_PersonaEstudio implements Serializable{
    private Inscripcion inscripcion;
    private ArrayList<Escolaridad> escolaridad;

    public SDT_PersonaEstudio() {
        this.escolaridad = new ArrayList<>();
    }

    /**
     *
     * @return Retorna la instancia de SDT_PersonaEstudio
     */
    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    /**
     *
     * @param inscripcion Recibe la inscripción
     */
    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

    /**
     *
     * @return Retorna la escolaridad
     */
    public ArrayList<Escolaridad> getEscolaridad() {
        return escolaridad;
    }

    /**
     *
     * @param escolaridad Recibe la escolaridad
     */
    public void setEscolaridad(ArrayList<Escolaridad> escolaridad) {
        this.escolaridad = escolaridad;
    }

    /**
     *
     * @return Retorna el Titulo del Estudio
     */
    public String getTituloEstudio()
    {
        String retorno = "";
        if(this.getInscripcion().getInsCod().equals(Long.valueOf("0")))
        {
            retorno = "Sin inscripción";
        }
        else
        {
            if(this.getInscripcion().getCurso() != null){
                retorno = this.getInscripcion().getCurso().getCurNom();
            }

            if(this.getInscripcion().getPlanEstudio() != null){
                retorno = this.getInscripcion().getPlanEstudio().getPlaEstNom();
            }                                   

        }
        
        return retorno;
    }
    
}
