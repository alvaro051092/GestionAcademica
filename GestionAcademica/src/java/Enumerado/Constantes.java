/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado Constantes
 *
 * @author Alvaro
 */
public enum Constantes {
    URL_FOLDER_SERVICIO_MDL("/webservice/rest/server.php"), 
    ENCRYPT_VECTOR_INICIO("a#!?d./*@@^^''_a"),
    ENCRYPT_SEMILLA("-KeY!!AD#AM!!KeY"),
    VERSION("0.1.0"),
    SEPARADOR("#||#"),
    WS_USR("ws_generico"),
    WS_USR_MOODLE("ws_mdl"),
    WS_PSW_MOODLE("$.WSusMdl%"),
    WS_USR_APP("ws_app"),
    WS_PSW_APP("$.WSusApp%"),
    WS_USR_WEB("ws_web"),
    WS_PSW_WEB("$.WSusWeb%"),
    METODO_GETPK("GetPrimaryKey"),
    METODO_GETINSQ("getInsertQuery"),
    METODO_GETUPDQ("getUpdateQuery"),
    SIZE_FILE("20971520");

    Constantes(){
        
    }
    
    private String vValor;

    Constantes(String pValor) {
        this.vValor = pValor;
    }

    /**
     *
     * @return Retorna el valor de Constantes
     */
    public String getValor() {
        return vValor;
    }
    
}
