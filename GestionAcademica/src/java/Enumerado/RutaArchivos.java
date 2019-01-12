/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado RutaArchivos
 *
 * @author Alvaro
 */
public enum RutaArchivos {
    CARPETA_PRIVADA("/WEB-INF/PrivateTempStorage"), CARPETA_PUBLICA("/PublicTempStorage");
    
    RutaArchivos(){
        
    }
    
    private String vRuta;

    RutaArchivos(String pRuta) {
        this.vRuta = pRuta;
    }

    /**
     *
     * @return Retorna la ruta de RutaArchivos
     */
    public String getRuta() {
        return vRuta;
    }
    
}
