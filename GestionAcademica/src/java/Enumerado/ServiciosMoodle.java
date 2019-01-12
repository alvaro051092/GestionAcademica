/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumerado;

/**
 * Enumerado ServiciosMoodle
 *
 * @author Alvaro
 */
public enum ServiciosMoodle {
    USUARIO_CREAR("core_user_create_users"), 
    USUARIO_OBTENER_LISTA("core_user_get_users"),
    USUARIO_OBTENER_LISTA_BY_CAMPO("core_user_get_users_by_field");
    
    ServiciosMoodle(){
        
    }
    
    private String vValor;

    ServiciosMoodle(String pValor) {
        this.vValor = pValor;
    }

    /**
     *
     * @return Retorna el valor de ServiciosMoodle
     */
    public String getValor() {
        return vValor;
    }
    
}
