/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dominio;

import java.util.ArrayList;

/**
 * Carga una lista de sitios a los que puede acceder el usuario logueado
 * dependiendo del tipo que sea. (Administrador, Docente o Alumno)
 * 
 * @author alvar
 */
public class Sitios {
    private static Sitios instancia;
    
    private final ArrayList<String> lstSinSeguridad;
    private final ArrayList<String> lstDocente;
    private final ArrayList<String> lstAlumno;
   
    private Sitios() {
        lstSinSeguridad = new ArrayList();
        lstAlumno = new ArrayList();
        lstDocente = new ArrayList();
        
        CargarSinSeguridad();
        CargarAlumno();
        CargarDocente();
    }
    
    /**
     * Crear la instancia de Sitios
     * 
     * @return Retorna la Instancia
     */
    public static Sitios GetInstancia(){
        if (instancia==null)
        {
            instancia   = new Sitios();
        }

        return instancia;
    }
    
    /**
     *
     *  Cargar la lista lstSinSeguridad con las pantallas que pueden ser visualizadas por todos los usuarios
     */
    private void CargarSinSeguridad(){
        lstSinSeguridad.add("index.jsp");
        lstSinSeguridad.add("Error.jsp");
        lstSinSeguridad.add("pswSolRecovery.jsp");
        lstSinSeguridad.add("pswChangeREST.jsp");
        lstSinSeguridad.add("pswRecovery.jsp");
    }
    
    /**
     *
     *  Cargar la lista lstAlumno con las pantallas que puede visualizar el alumno
     */
    private void CargarAlumno(){
        lstAlumno.add("Estudios.jsp");
        lstAlumno.add("Evaluaciones.jsp");
        lstAlumno.add("Solicitudes.jsp");
        lstAlumno.add("Escolaridad.jsp");
        lstAlumno.add("pswChange.jsp");
        lstAlumno.add("uploadFoto.jsp");
        lstAlumno.add("Perfil.jsp");
        lstAlumno.add("archivos.jsp");
    }

    /**
     *
     * Cargar la lista lstDocente con las pantallas que puede visualizar el docente
     */
    private void CargarDocente(){
        lstDocente.add("EstudiosDictados.jsp");
        lstDocente.add("EstudioDocumentos.jsp");
        lstDocente.add("EvalPendientes.jsp");
        lstDocente.add("CalificarAlumnos.jsp");
        lstDocente.add("pswChange.jsp");
        lstDocente.add("uploadFoto.jsp");
        lstDocente.add("Perfil.jsp");
        lstDocente.add("archivos.jsp");
    }

    /**
     * Obtener la lista de sitios sin Seguridad
     * 
     * @return retorna la lista lstSinSeguridad
     */
    public ArrayList<String> getLstSinSeguridad() {
        return lstSinSeguridad;
    }

    /**
     * Obtener la lista de sitios que puede visualizar el docente
     * 
     * @return Retorna la lista lstDocente
     */
    public ArrayList<String> getLstDocente() {
        return lstDocente;
    }

    /**
     * Obtener la lista de sitios que puede visualizar el alumno
     * 
     * @return Retorna la lista lstAlumno
     */
    public ArrayList<String> getLstAlumno() {
        return lstAlumno;
    }
    
    
}
