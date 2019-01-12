/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Entidad.TipoEvaluacion;
import Utiles.Retorno_MsgObj;
import java.util.List;

/**
 * Entidad InTipoEvaluacion
 *
 * @author alvar
 */
public interface InTipoEvaluacion {
    Object guardar(TipoEvaluacion pTipoEvaluacion);
    Object actualizar(TipoEvaluacion pTipoEvaluacion);
    Object eliminar(TipoEvaluacion pTipoEvaluacion);
    Retorno_MsgObj obtener(Long pTpoEvlCod);
    Retorno_MsgObj obtenerLista();
}
