/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado IconClass
 *
 * @author Alvaro
 */
public enum IconClass {
    /* OPERACIONES BASICAS */
    ADD("glyphicon glyphicon-plus")
    ,EDIT("glyphicon glyphicon-edit")
    ,DELETE("glyphicon glyphicon-trash")
    ,SYNC("glyphicon glyphicon-refresh")
    
    /* REPORTES */
    ,COPY("fa fa-files-o")
    ,CSV("fa fa-floppy-o")
    ,EXCEL("fa fa-file-excel-o")
    ,PDF("fa fa-file-pdf-o")
    
    /* GENERALES */
    ,DOCENTE("fa fa-user-o")
    ,ALUMNO("fa fa-users")
    
    ;
    
    IconClass(){
        
    }
    
    private String valor;

    IconClass(String  pValor) {
        this.valor = pValor;
    }

    /**
     *
     * @return Retorna el valor IconClass
     */
    public String getValor() {
        return valor;
    }
    
    /**
     *
     * @param pCod Recibe el código de IconClass
     * @return Retorna el dato requerido definido al inicio de la clase
     */
    public static IconClass fromCode(String pCod) {
        for (IconClass objeto  : IconClass.values()){
            if (objeto.getValor().equals(pCod)){
                return objeto;
            }
        }
        throw new UnsupportedOperationException(
                "El icono " + pCod + " is not supported!");
    }
    
}
