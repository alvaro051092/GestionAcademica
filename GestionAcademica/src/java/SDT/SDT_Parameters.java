/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDT;

/**
 *  SDT SDT_Parameters
 *
 * @author alvar
 */
public class SDT_Parameters {
    private Object objeto;
    private String nombre;

    /**
     *
     * @return Retorno el Objeto
     */
    public Object getObjeto() {
        return objeto;
    }

    /**
     *
     * @param objeto Recibe el Objeto
     */
    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }

    /**
     *
     * @return Retorno el nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @param nombre Recibe el Nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     * @param objeto Recibe el objeto SDT_Parameters
     * @param nombre Recibe el nombre SDT_Parameters
     */
    public SDT_Parameters(Object objeto, String nombre) {
        this.objeto = objeto;
        this.nombre = nombre;
    }
    
    
    
}
