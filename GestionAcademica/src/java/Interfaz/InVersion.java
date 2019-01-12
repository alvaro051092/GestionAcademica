/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Entidad.Version;

/**
 * Interfaz InVersion
 *
 * @author alvar
 */
public interface InVersion {
    Object guardar(Version pObjeto);
    void actualizar(Version pObjeto);
    Version obtener(Object pCodigo);
}
