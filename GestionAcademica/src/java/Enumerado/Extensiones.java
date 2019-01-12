/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado Extensiones
 *
 * @author Alvaro
 */
public enum Extensiones {
    //ARCHIVOS DE IMPORTACION
    XLS("xls"), 
    XLSX("xlsx"),
    DOC("doc"),
    DOCX("docx"),
    //FOTOS
    JPG("jpg"),
    JPEG("jpeg"),
    GIF("gif"),
    PNG("png")
    ;
    
    Extensiones(){
        
    }
    
    private String valor;

    Extensiones(String  pValor) {
        this.valor = pValor;
    }

    /**
     *
     * @return Retorna el valor de Extenciones
     */
    public String getValor() {
        return valor;
    }
    
    /**
     *
     * @param pCod Recibe un codigo de Extenciones
     * @return Retorna la extención dado el código recibido
     */
    public static Extensiones fromCode(String pCod) {
        for (Extensiones objeto  : Extensiones.values()){
            if (objeto.getValor().equals(pCod)){
                return objeto;
            }
        }
        throw new UnsupportedOperationException(
                "El extension " + pCod + " is not supported!");
    }
    
}
