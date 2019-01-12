/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Entidad.Curso;
import Utiles.Retorno_MsgObj;
import java.util.List;

/**
 * Interfaz InCurso
 *
 * @author alvar
 */
public interface InCurso {
    Object guardar(Curso pCurso);
    Object actualizar(Curso pCurso);
    Object eliminar(Curso pCurso);
    Retorno_MsgObj obtener(Long pCurCod);
    Retorno_MsgObj obtenerLista();
}
