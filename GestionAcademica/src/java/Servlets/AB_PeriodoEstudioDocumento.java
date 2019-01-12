/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Entidad.PeriodoEstudio;
import Entidad.PeriodoEstudioDocumento;
import Entidad.Persona;
import Enumerado.NombreSesiones;
import Enumerado.TipoMensaje;
import Logica.LoPeriodo;
import Logica.LoPersona;
import Logica.Seguridad;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import Utiles.Utilidades;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Mantenimiento PeriodoEstudioDocumento
 *
 * @author alvar
 */
@WebServlet(name = "AB_PeriodoEstudioDocumento", urlPatterns = {"/AB_PeriodoEstudioDocumento"})
public class AB_PeriodoEstudioDocumento extends HttpServlet {

    LoPeriodo loPeriodo                     = LoPeriodo.GetInstancia();
    private final Utilidades utilidades     = Utilidades.GetInstancia();
    private Mensajes mensaje                = new Mensajes("Error", TipoMensaje.ERROR);
    private Boolean error                   = false;
    private Persona perUsuario;
    
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
            
                String action   = request.getParameter("pAction");
                String retorno  = "";

                if(usuario != null)  perUsuario = (Persona) LoPersona.GetInstancia().obtenerByMdlUsr(usuario).getObjeto();


                switch(action)
                {


                    case "DELETE":
                        retorno = this.EliminarDatos(request);
                    break;

                    case "OBTENER":
                        retorno = this.ObtenerDatos(request);
                    break;
                    
                    case "SINCRONIZAR":
                        retorno = this.Sincronizar(request);
                    break;

               }

                out.println(retorno);
            }  
        }
    }
    
    
    /**
     * 
     * @param request
     * @return Método eliminar PeriodoEstudioDocumento
     */
    private String EliminarDatos(HttpServletRequest request){
        error       = false;
        mensaje    = new Mensajes("Error al eliminar", TipoMensaje.ERROR);
        try
        {
            PeriodoEstudioDocumento periDocumento = this.ValidarPeriodoEstudioDocumento(request, null);
            
            Retorno_MsgObj retorno = (Retorno_MsgObj) loPeriodo.DocumentoEliminar(periDocumento);
            
            mensaje = retorno.getMensaje();

        }
        catch(Exception ex)
        {
            mensaje = new Mensajes("Error al Eliminar: " + ex.getMessage(), TipoMensaje.ERROR);
            throw ex;
        }

       return utilidades.ObjetoToJson(mensaje);
    }
    
    /**
     * 
     * @param request
     * @return Método obtener datos PeriodoEstudioDocumento
     */
    private String ObtenerDatos(HttpServletRequest request){
        error       = false;
        PeriodoEstudioDocumento periDocumento = new PeriodoEstudioDocumento();
        
        try
        {
            periDocumento = this.ValidarPeriodoEstudioDocumento(request, null);
        }
        catch(Exception ex)
        {
            mensaje = new Mensajes("Error al obtener: " + ex.getMessage(), TipoMensaje.ERROR);
            throw ex;
        }

       return utilidades.ObjetoToJson(periDocumento);
    }
    
    /**
     * 
     * @param request
     * @return Método sincronizar
     */
    private String Sincronizar(HttpServletRequest request){
        error       = false;
        mensaje = new Mensajes("Sincronizando", TipoMensaje.MENSAJE);
        try
        {
            PeriodoEstudioDocumento periDocumento = this.ValidarPeriodoEstudioDocumento(request, null);
            
            if(periDocumento.getPeriodo() != null) loPeriodo.DocumentoImportarMoodle(periDocumento.getPeriodo());
            
        }
        catch(Exception ex)
        {
            mensaje = new Mensajes("Error al obtener: " + ex.getMessage(), TipoMensaje.ERROR);
            throw ex;
        }

       return utilidades.ObjetoToJson(mensaje);
    }
    
    private PeriodoEstudioDocumento ValidarPeriodoEstudioDocumento(HttpServletRequest request, PeriodoEstudioDocumento periDocumento){
        if(periDocumento == null)
        {
            periDocumento   = new PeriodoEstudioDocumento();
        }
        
        try
        {

        String 	PeriEstCod  = request.getParameter("pPeriEstCod");
        String 	DocCod      = request.getParameter("pDocCod");
        
        //------------------------------------------------------------------------------------------
        //Validaciones
        //------------------------------------------------------------------------------------------

        //TIPO DE DATO
        

        if(PeriEstCod != null) periDocumento.setPeriodo(((PeriodoEstudio) loPeriodo.EstudioObtener(Long.valueOf(PeriEstCod)).getObjeto()));
        
        if(DocCod != null) periDocumento = periDocumento.getPeriodo().getDocumentoById(Long.valueOf(DocCod));
        }
        catch(NumberFormatException | UnsupportedOperationException  ex)
        {
            String texto = ex.getMessage().replace("For input string:", "Tipo de dato incorrecto: ");
            texto = texto.replace("Unparseable date:", "Tipo de dato incorrecto: ");
            
            mensaje = new Mensajes("Error: " + texto, TipoMensaje.ERROR);
            error   = true;
            
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return periDocumento;
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
