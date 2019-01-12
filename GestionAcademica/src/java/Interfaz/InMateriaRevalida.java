/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Entidad.MateriaRevalida;
import java.util.List;

/**
 * Interfaz InMateriaRevalida
 *
 * @author alvar
 */
public interface InMateriaRevalida {
    Object guardar(MateriaRevalida pObjeto);
    void actualizar(MateriaRevalida pObjeto);
    void eliminar(MateriaRevalida pObjeto);
    MateriaRevalida obtener(Object pCodigo);
    List<MateriaRevalida> obtenerLista();
}
