/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado TipoArchivo
 *
 * @author Alvaro
 */
public enum TipoArchivo {
    PERIODO_DOCUMENTO("Periodo estudio - Documento"),
    FOTO_PERFIL("Foto de perfil"),
    IMP_ALUMNO_CARRERA("Importar alumno a carrera"),
    IMP_ALUMNO_CURSO("Importar alumno a curso"),
    IMP_ALUMNO_ESCOLARIDAD("Importar alumno a escolaridad");
    
    TipoArchivo(){
        
    }
    
    private String valor;

    TipoArchivo(String  pValor) {
        this.valor = pValor;
    }

    /**
     *
     * @return Retorna el valor TipoArchivo
     */
    public String getValor() {
        return valor;
    }
    
    /**
     *
     * @param pCod Recibe el código de TipoArchivo
     * @return Retorna TipoArchivo dado el código recibido
     */
    public static TipoArchivo fromCode(String pCod) {
        for (TipoArchivo objeto  : TipoArchivo.values()){
            if (objeto.getValor().equals(pCod)){
                return objeto;
            }
        }
        throw new UnsupportedOperationException(
                "El tipo archivo " + pCod + " is not supported!");
    }
    
}
