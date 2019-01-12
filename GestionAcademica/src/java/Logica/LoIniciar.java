/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Entidad.Parametro;
import Entidad.Persona;
import Entidad.TipoEvaluacion;
import Entidad.Version;
import Enumerado.Constantes;
import Enumerado.Filial;
import Logica.Notificacion.NotificacionesInternas;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author alvar
 */
public class LoIniciar {

    private final LoVersion loVersion = LoVersion.GetInstancia();
    private Version version;
        
    /**
     *Inicia el sistema
     */
    public LoIniciar() {
        version     = loVersion.obtener(Long.valueOf("1"));
    }
    
    /**
     * Inicia el sistema
     * @param request HttpServletRequest
     */
    public void Iniciar(HttpServletRequest request){
        
        Parametro param = LoParametro.GetInstancia().obtener();
        
        if(version == null)
        {
            this.CargarVersion();
        }
        
        if(!version.getSisCrgDat())
        {
            this.CargarDatosIniciales(request);
            param = LoParametro.GetInstancia().obtener();
            
            if(param.getParUtlMdl())
            {
                this.SincronizarConMoodle();
            }
        }
        
        
        
    }
    
    /**
     * Cargar version
     */
    private void CargarVersion(){
        version = new Version();
        version.setSisCrgDat(Boolean.FALSE);
        version.setSisVer(Constantes.VERSION.getValor());
        
        loVersion.guardar(version);
    }
    
    /**
     * Cargar datos iniciales
     * @param request HttpServletRequest
     */
    private void CargarDatosIniciales(HttpServletRequest request){
        CargarParametros();
        CargarTipoEvaluacion();
        CargarUrlSistema(request);
        CargarUsuarioAdministrador();
        CargarNotificaciones();
        CargarUsuariosWS();
        CargarObjetosSincronizar();
        
        version.setSisCrgDat(Boolean.TRUE);
        loVersion.actualizar(version);

    }
    
    /**
     * Cargar tipos de evaluación
     */
    private void CargarTipoEvaluacion(){
        LoTipoEvaluacion lTpoEval = LoTipoEvaluacion.GetInstancia();
        if(lTpoEval.obtenerLista().getLstObjetos().size() < 4)
        {
            TipoEvaluacion tpoEval = new TipoEvaluacion();

            tpoEval.setTpoEvlNom("Parcial");
            tpoEval.setTpoEvlExm(Boolean.FALSE);
            tpoEval.setTpoEvlInsAut(Boolean.TRUE);
            lTpoEval.guardar(tpoEval);
            
            tpoEval = new TipoEvaluacion();
            tpoEval.setTpoEvlNom("Obligatorio");
            tpoEval.setTpoEvlExm(Boolean.FALSE);
            tpoEval.setTpoEvlInsAut(Boolean.TRUE);
            lTpoEval.guardar(tpoEval);

            tpoEval = new TipoEvaluacion();
            tpoEval.setTpoEvlNom("Examen");
            tpoEval.setTpoEvlExm(Boolean.TRUE);
            tpoEval.setTpoEvlInsAut(Boolean.FALSE);
            lTpoEval.guardar(tpoEval);
            
            tpoEval = new TipoEvaluacion();
            tpoEval.setTpoEvlNom("Trabajo");
            tpoEval.setTpoEvlExm(Boolean.FALSE);
            tpoEval.setTpoEvlInsAut(Boolean.TRUE);
            lTpoEval.guardar(tpoEval);

        }
        
   }
    
    /**
     * Cargar parametros
     */
    private void CargarParametros(){
        
        Parametro parametro = LoParametro.GetInstancia().obtener();
        
        if(parametro == null)
        {
            
            parametro = new Parametro();
            parametro.setParDiaEvlPrv(7);
            parametro.setParFchUltSinc(new Date());
            parametro.setParMdlTkn("ce19d614e5a749b22d89d010a5396249");
            parametro.setParSisLocal(Boolean.FALSE);
            parametro.setParSncAct(Boolean.FALSE);
            parametro.setParUtlMdl(Boolean.FALSE);
            parametro.setParTieIna(12);
            parametro.setParUrlMdl("http://192.168.0.106");
            parametro.setParUrlSrvSnc("");
        
            LoParametro.GetInstancia().guardar(parametro);
        }
        
    }
    
    /**
     * Cargar url de sistema
     * @param request  HttpServletRequest
     */
    
    private void CargarUrlSistema(HttpServletRequest request){
        
        //URI contextUrl = URI.create(request.getRequestURL().toString()).resolve(request.getContextPath());
        String urlSistema = request.getContextPath();
        
        if(urlSistema != null) if(urlSistema.length() > 1) urlSistema += "/";
        
        Parametro param = LoParametro.GetInstancia().obtener();
        param.setParUrlSis(urlSistema);
        LoParametro.GetInstancia().actualizar(param);
        
    }
    
    /**
     * Sincronizar personas con moodle
     */
    private void SincronizarConMoodle(){
        LoPersona persona = LoPersona.GetInstancia();
        persona.SincronizarUsuariosMoodleSistema();
    }    
    
    /**
     * Cargar usuario administrador
     */
    private void CargarUsuarioAdministrador(){
        LoPersona loPersona   = LoPersona.GetInstancia();
        Persona persona     = (Persona) loPersona.obtener(Long.valueOf("1")).getObjeto();
        
        if(persona == null)
        {
            persona     = new Persona();

            persona.setPerApe("Administrador");
            persona.setPerCntIntLgn(0);
            persona.setPerEml("administrador@administrador.com");
            persona.setPerEsAdm(Boolean.TRUE);
            persona.setPerEsAlu(Boolean.FALSE);
            persona.setPerEsDoc(Boolean.FALSE);
            persona.setPerFil(Filial.COLONIA);
            persona.setPerNom("Administrador");
            persona.setPerNotApp(Boolean.TRUE);
            persona.setPerNotEml(Boolean.TRUE);
            persona.setPerUsrMod("sga_admin");
            persona.setPerPass(Seguridad.GetInstancia().cryptWithMD5("admin"));
            
            loPersona.guardar(persona);
        }
    }
    
    /**
     * Cargar notificaciones
     */
    
    private void CargarNotificaciones(){
        NotificacionesInternas notInt = new NotificacionesInternas();
        notInt.CargarNotificaciones();
     }
    
    /**
     * Cargar usuarios de servicio web
     */
    
    private void CargarUsuariosWS(){
        LoWS.GetInstancia().CargarUsuariosWS();
    }
    /**
     * Cargar sincronizar
     */
    private void CargarObjetosSincronizar(){
        LoSincronizacion.GetInstancia().CargaInicialObjetos();
    }
}
