/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

/**
 *  Interfaz InABMGenerico
 *  
 * @author alvar
 */
public interface InABMGenerico {
    Object guardar(Object pObjeto);
    Object actualizar(Object pObjeto);
    Object eliminar(Object pObjeto);
    Object obtener(Object pObjeto);
    Object obtenerLista();
}
