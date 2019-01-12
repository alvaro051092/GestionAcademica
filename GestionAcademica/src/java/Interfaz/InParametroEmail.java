/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Entidad.ParametroEmail;
import Entidad.Version;
import java.util.List;

/**
 * Interfaz InParametroEmail
 *
 * @author alvar
 */
public interface InParametroEmail {
    Object guardar(ParametroEmail pObjeto);
    void actualizar(ParametroEmail pObjeto);
    ParametroEmail obtener(Object pCodigo);
    void eliminar(ParametroEmail pObjeto);
    List<ParametroEmail> obtenerLista();
}
