/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Entidad.Persona;
import Utiles.Retorno_MsgObj;
import java.util.List;

/**
 * Interfaz InPersona
 *
 * @author alvar
 */
public interface InPersona {
    Object guardar(Persona pObjeto);
    Object actualizar(Persona pObjeto);
    Object eliminar(Persona pObjeto);
    Retorno_MsgObj obtener(Object pCodigo);
    Retorno_MsgObj obtenerByMdlUsr(String pMdlUsr);
    Retorno_MsgObj obtenerLista();
}
