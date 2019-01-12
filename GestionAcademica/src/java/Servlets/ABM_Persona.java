/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;


import Entidad.Archivo;
import Entidad.Parametro;
import Entidad.Persona;
import Enumerado.Filial;
import Enumerado.Genero;
import Enumerado.NombreSesiones;
import Enumerado.TipoMensaje;
import Logica.LoArchivo;
import Logica.LoParametro;
import Logica.LoPersona;
import Logica.Seguridad;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import Utiles.Utilidades;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Mantenimiento de Persona
 *
 * @author alvar
 */
public class ABM_Persona extends HttpServlet {
    private final Parametro parametro       = LoParametro.GetInstancia().obtener();
    private final Seguridad seguridad       = Seguridad.GetInstancia();
    private final LoPersona loPersona       = LoPersona.GetInstancia();
    private final Utilidades utilidades     = Utilidades.GetInstancia();
    private Mensajes mensaje                = new Mensajes("Error", TipoMensaje.ERROR);
    private Boolean error                   = false;
    private final SimpleDateFormat yMd      = new SimpleDateFormat("yyyy-MM-dd");
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            
            String action   = request.getParameter("pAction");
            String retorno  = "";

            //----------------------------------------------------------------------------------------------------
            //CONTROL DE ACCESO
            //----------------------------------------------------------------------------------------------------
            String referer = request.getHeader("referer");
                
            HttpSession session=request.getSession();
            String usuario = (String) session.getAttribute(NombreSesiones.USUARIO.getValor());
            Boolean esAdm = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ADM.getValor());
            Boolean esAlu = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ALU.getValor());
            Boolean esDoc = (Boolean) session.getAttribute(NombreSesiones.USUARIO_DOC.getValor());
            Retorno_MsgObj acceso = Seguridad.GetInstancia().ControlarAcceso(usuario, esAdm, esDoc, esAlu, utilidades.GetPaginaActual(referer));

            if (acceso.SurgioError() && !utilidades.GetPaginaActual(referer).isEmpty()) {
                mensaje = new Mensajes("Acceso no autorizado - " + this.getServletName(), TipoMensaje.ERROR);
                System.err.println("Acceso no autorizado - " + this.getServletName() + " - Desde: " + utilidades.GetPaginaActual(referer));
                out.println(utilidades.ObjetoToJson(mensaje));
            }
            else
            {
            
                switch(action)
                {

                    case "INSERT":
                        retorno = this.AgregarDatos(request);
                    break;

                    case "UPDATE":
                        retorno = this.ActualizarDatos(request);
                    break;

                    case "DELETE":
                        retorno = this.EliminarDatos(request);
                    break;

                    case "POPUP_OBTENER":
                        retorno = this.POPUP_ObtenerDatos();
                    break;

                    case "POPUP_OBTENER_ESTUDIOS":
                        retorno = this.POPUP_ObtenerEstudiosDatos(request);
                    break;

                    case "CAMBIAR_PSW":
                        retorno = this.CambiarPsw(request);
                    break;

                    case "SOL_PSW_RECOVERY":
                        retorno = this.SolRecoveryPsw(request);
                    break;

                    case "PSW_RECOVERY":
                        retorno = this.RecoveryPsw(request);
                    break;

                    case "SUBIR_FOTO":
                        retorno = this.SubirFoto(request);
                    break;
                    
                    case "SINCRONIZAR_MOODLE":
                        retorno = this.SincronizarMoodle();
                    break;

                }

                out.println(retorno);
            }
        }
        
    }
    
    /**
     * 
     * @param request
     * @return Método agregar Persona
     */
    private String AgregarDatos(HttpServletRequest request)
    {
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

        try
        {

            error           = false;
            Persona persona = this.ValidarPersona(request, null);

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                Retorno_MsgObj retornoObj = (Retorno_MsgObj) loPersona.guardar(persona);
                mensaje    = retornoObj.getMensaje();
            }
        }
        catch(Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        String retorno = utilidades.ObjetoToJson(mensaje);

        return retorno;
    }

    /**
     * 
     * @param request
     * @return Método actualizar Persona
     */
    private String ActualizarDatos(HttpServletRequest request)
    {
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

        try
        {

            error           = false;
            Persona persona = this.ValidarPersona(request, null);

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                Retorno_MsgObj retornoObj = (Retorno_MsgObj) loPersona.actualizar(persona);
                mensaje    = retornoObj.getMensaje();
            }
        }
        catch(Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        String retorno = utilidades.ObjetoToJson(mensaje);

        return retorno;
    }

    /**
     * 
     * @param request
     * @return Método eliminar Persona
     */
    private String EliminarDatos(HttpServletRequest request)
    {
        error      = false;
        mensaje    = new Mensajes("Error al eliminar", TipoMensaje.ERROR);
        try
        {
            Persona persona = this.ValidarPersona(request, null);

            if(!error)
            {
                Retorno_MsgObj retornoObj = (Retorno_MsgObj) loPersona.eliminar(persona);
                mensaje    = retornoObj.getMensaje();
            }


        }
        catch(Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }



        return utilidades.ObjetoToJson(mensaje);
    }
    
    /**
     * 
     * @return Método obtener datos PopUp Persona
     */
    private String POPUP_ObtenerDatos()
    {
        List<Object> lstPersona;
        
        
        lstPersona = loPersona.obtenerPopUp().getLstObjetos();

        return utilidades.ObjetoToJson(lstPersona);
    }
    
    /**
     * 
     * @return Método sincronizar con moodle
     */
    private String SincronizarMoodle()
    {
        
        loPersona.SincronizarUsuariosMoodleSistema();

        return utilidades.ObjetoToJson(new Mensajes("Sincronizando", TipoMensaje.MENSAJE));
    }
    
    /**
     * 
     * @param request
     * @return Método obtener estudios PopUp
     */
    private String POPUP_ObtenerEstudiosDatos(HttpServletRequest request)
    {
        Persona persona = this.ValidarPersona(request, null);

        return utilidades.ObjetoToJson(persona.getLstEstudios());
    }
    
    /**
     * 
     * @param request
     * @return Método cambiar pasword Persona
     */
    private String CambiarPsw(HttpServletRequest request){
        String usuario    = request.getParameter("usuario");
        String pswActual  = request.getParameter("pswActual");
        String pswNueva   = request.getParameter("pswNueva");
        String pswConf    = request.getParameter("pswConfirmacion");
        
        Retorno_MsgObj retorno = loPersona.CambiarPassword(usuario, pswActual, pswNueva, pswConf);
        
        return utilidades.ObjetoToJson(retorno.getMensaje());
    }
    
    /**
     * 
     * @param request
     * @return Método solicitud recuperar Pasword
     */
    private String SolRecoveryPsw(HttpServletRequest request){
        String usuario    = request.getParameter("usuario");
        
        Retorno_MsgObj retorno = loPersona.SolicitaRestablecerPassword(usuario);
        return utilidades.ObjetoToJson(retorno.getMensaje());
    }
    
    /**
     * 
     * @param request
     * @return Método Recuperar Pasword
     */
    private String RecoveryPsw(HttpServletRequest request){
        String PerCod       = request.getParameter("PerCod");
        String tkn          = request.getParameter("tkn");
        String pswNueva     = request.getParameter("pswNueva");
        String pswConf      = request.getParameter("pswConfirmacion");
        
        Retorno_MsgObj retorno = loPersona.RestablecerPassword(Long.valueOf(PerCod), tkn, pswNueva, pswConf);
        
        return utilidades.ObjetoToJson(retorno.getMensaje());
    }
    
    /**
     * 
     * @param request
     * @return Método Subir Foto
     */
    private String SubirFoto(HttpServletRequest request){
        Persona persona = this.ValidarPersona(request, null);
        
        if(!error)
        {
            String ArcCod          = request.getParameter("pArcCod");
            persona.setFoto((Archivo)LoArchivo.GetInstancia().obtener(Long.valueOf(ArcCod)).getObjeto());
            Retorno_MsgObj retorno = (Retorno_MsgObj) loPersona.actualizar(persona);
            mensaje = retorno.getMensaje();
        }
        
        return utilidades.ObjetoToJson(mensaje);
    }
    
    /**
     * 
     * @param request
     * @param persona
     * @return Método validar Persona
     */
    private Persona ValidarPersona(HttpServletRequest request, Persona persona)
    {
        if(persona == null)
        {
            persona   = new Persona();
        }
        
        try
        {

            String PerCod= request.getParameter("pPerCod");
            String PerApe= request.getParameter("pPerApe");
            String PerDoc= request.getParameter("pPerDoc");
            String PerEml= request.getParameter("pPerEml");
            String PerEsAdm= request.getParameter("pPerEsAdm");
            String PerEsAlu= request.getParameter("pPerEsAlu");
            String PerEsDoc= request.getParameter("pPerEsDoc");
            String PerFil= request.getParameter("pPerFil");
            String PerNom= request.getParameter("pPerNom");
            String PerNotApp= request.getParameter("pPerNotApp");
            String PerNotEml= request.getParameter("pPerNotEml");
            String PerNroEstOrt= request.getParameter("pPerNroEstOrt");
            String PerNroLib= request.getParameter("pPerNroLib");
            String PerPass= request.getParameter("pPerPass");
            String PerUsrMod= request.getParameter("pPerUsrMod");
            String PerApe2= request.getParameter("pPerApe2");
            String PerBeca= request.getParameter("pPerBeca");
            String PerCiudad= request.getParameter("pPerCiudad");
            String PerDir= request.getParameter("pPerDir");
            String PerDto= request.getParameter("pPerDto");
            String PerFchNac= request.getParameter("pPerFchNac");
            String PerGen= request.getParameter("pPerGen");
            String PerObs= request.getParameter("pPerObs");
            String PerPais= request.getParameter("pPerPais");
            String PerProf= request.getParameter("pPerProf");
            String PerSecApr= request.getParameter("pPerSecApr");
            String PerTel= request.getParameter("pPerTel");
            String PerTpoBeca= request.getParameter("pPerTpoBeca");
            //String ArcCod= request.getParameter("pArcCod");

            //------------------------------------------------------------------------------------------
            //Validaciones
            //------------------------------------------------------------------------------------------

            //TIPO DE DATO




            //Sin validacion
            
            if(PerCod!= null) if(!PerCod.isEmpty()) persona = (Persona) loPersona.obtener(Long.valueOf(PerCod)).getObjeto();
            
            if(PerApe!= null)persona.setPerApe(PerApe);
            if(PerDoc!= null)persona.setPerDoc(PerDoc);
            if(PerEml!= null)persona.setPerEml(PerEml);
            
            
            if(PerNom!= null)persona.setPerNom(PerNom);
            if(PerUsrMod!= null)persona.setPerUsrMod(PerUsrMod);
            if(PerApe2!= null)persona.setPerApe2(PerApe2);
            if(PerCiudad!= null)persona.setPerCiudad(PerCiudad);
            if(PerDir!= null)persona.setPerDir(PerDir);
            if(PerDto!= null)persona.setPerDto(PerDto);
            if(PerObs!= null)persona.setPerObs(PerObs);
            if(PerPais!= null)persona.setPerPais(PerPais);
            if(PerProf!= null)persona.setPerProf(PerProf);
            if(PerSecApr!= null)persona.setPerSecApr(PerSecApr);
            if(PerTel!= null)persona.setPerTel(PerTel);
            if(PerTpoBeca!= null)persona.setPerTpoBeca(PerTpoBeca);
            if(PerPass != null)
            {
                if(!PerPass.isEmpty())
                {
                    if(parametro.getParPswValExp() != null)
                    {
                        if(!PerPass.matches(parametro.getParPswValExp()))
                        {
                            error = true;
                            mensaje = new Mensajes(parametro.getParPswValMsg(), TipoMensaje.ERROR);
                        }
                    }    

                    if(!error)
                    {
                        persona.setPerPass(seguridad.cryptWithMD5(PerPass));
                    }
                }
            }
            
            
            
            if(PerEsAdm!= null) persona.setPerEsAdm(Boolean.valueOf(PerEsAdm));
            if(PerEsAlu!= null) persona.setPerEsAlu(Boolean.valueOf(PerEsAlu));
            if(PerEsDoc!= null) persona.setPerEsDoc(Boolean.valueOf(PerEsDoc));
            
            if(PerNotApp!= null)persona.setPerNotApp(Boolean.valueOf(PerNotApp));
            if(PerNotEml!= null)persona.setPerNotEml(Boolean.valueOf(PerNotEml));
            
            
            if(PerFil!= null)persona.setPerFil(Filial.fromCode(Integer.valueOf(PerFil)));
            if(PerGen!= null)persona.setPerGen(Genero.valueOf(PerGen));
            
            
            if(PerNroEstOrt!= null)persona.setPerNroEstOrt(Integer.valueOf(PerNroEstOrt));
            if(PerNroLib!= null)persona.setPerNroLib(Integer.valueOf(PerNroLib));
            if(PerBeca!= null)persona.setPerBeca(Double.valueOf(PerBeca));

            
            if(PerFchNac!= null) if(!PerFchNac.isEmpty()) persona.setPerFchNac(yMd.parse(PerFchNac));

        }
        catch(NumberFormatException | ParseException | UnsupportedOperationException  ex)
        {
            String texto = ex.getMessage().replace("For input string:", "Tipo de dato incorrecto: ");
            texto = texto.replace("Unparseable date:", "Tipo de dato incorrecto: ");
            
            mensaje = new Mensajes("Error: " + texto, TipoMensaje.ERROR);
            error   = true;
            
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

            
        return persona;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
