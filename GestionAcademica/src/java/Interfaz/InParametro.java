/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Entidad.Parametro;

/**
 * Interfaz InParametro
 *
 * @author alvar
 */
public interface InParametro {
    Object guardar(Parametro pObjeto);
    void actualizar(Parametro pObjeto);
    Parametro obtener(Object pCodigo);
}
