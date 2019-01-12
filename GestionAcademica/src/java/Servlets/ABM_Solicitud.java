/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;


import Entidad.Parametro;
import Entidad.Solicitud;
import Entidad.Persona;
import Enumerado.NombreSesiones;
import Enumerado.TipoMensaje;
import Enumerado.TipoSolicitud;
import Logica.LoParametro;
import Logica.LoSolicitud;
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Mantenimiento Solicitudes
 *
 * @author alvar
 */
public class ABM_Solicitud extends HttpServlet {
    private final Parametro parametro       = LoParametro.GetInstancia().obtener();
    private final Seguridad seguridad       = Seguridad.GetInstancia();
    private final LoSolicitud loSolicitud           = LoSolicitud.GetInstancia();
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
            
                if(usuario != null)  perUsuario = (Persona) LoPersona.GetInstancia().obtenerByMdlUsr(usuario).getObjeto();

                String action   = request.getParameter("pAction");
                String retorno  = "";

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

                    case "TOMAR":
                        retorno = this.TomarSolicitud(request);
                    break;

                    case "LIBERAR":
                        retorno = this.LiberarSolicitud(request);
                    break;

                    case "FINALIZAR":
                        retorno = this.FinalizarSolicitud(request);
                    break;

                }

                out.println(retorno);
            } 
        }
        
    }
    
    /**
     * 
     * @param request
     * @return Método agregar Solicitudes
     */
    private String AgregarDatos(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

        try
        {
            error           = false;

            Solicitud solicitud = this.ValidarSolicitud(request, null);

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                
                if(perUsuario != null) solicitud.setAlumno(perUsuario);
                Retorno_MsgObj retornoObj = (Retorno_MsgObj) loSolicitud.guardar(solicitud);
                mensaje    = retornoObj.getMensaje();
            }
        }
        catch(Exception ex)
        {
            mensaje = new Mensajes("Error al guardar: " + ex.getMessage(), TipoMensaje.ERROR);
            throw ex;
        }

        String retorno = utilidades.ObjetoToJson(mensaje);

        return retorno;
    }

    /**
     * 
     * @param request
     * @return Método actualizar Solicitudes
     */
    private String ActualizarDatos(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);
        try
        {

            error           = false;
            
            Solicitud solicitud = this.ValidarSolicitud(request, null);

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            Retorno_MsgObj retorno = new Retorno_MsgObj();
            
            if(!error)
            {
                retorno     = (Retorno_MsgObj) loSolicitud.actualizar(solicitud);
            }

            mensaje = retorno.getMensaje(); 
            
        }
        catch(Exception ex)
        {
            mensaje = new Mensajes("Error al actualizar: " + ex.getMessage(), TipoMensaje.ERROR);
            throw ex;
        }

        String retorno = utilidades.ObjetoToJson(mensaje);

        return retorno;
    }

    /**
     * 
     * @param request
     * @return Método eliminar Solicitudes
     */
    private String EliminarDatos(HttpServletRequest request){
        error       = false;
        mensaje    = new Mensajes("Error al eliminar", TipoMensaje.ERROR);
        try
        {

            Solicitud solicitud = this.ValidarSolicitud(request, null);

            if(!error)
            {
                mensaje = ((Retorno_MsgObj) loSolicitud.eliminar(solicitud)).getMensaje();
            }
            
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
     * @return Método Tomar Solicitudes
     */
    private String TomarSolicitud(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);
        try
        {

            error           = false;
            
            Solicitud solicitud = this.ValidarSolicitud(request, null);

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            Retorno_MsgObj retorno = new Retorno_MsgObj();
            
            if(!error)
            {
                retorno     = (Retorno_MsgObj) loSolicitud.tomarSolicitud(solicitud, perUsuario);
            }

            mensaje = retorno.getMensaje(); 
            
        }
        catch(Exception ex)
        {
            mensaje = new Mensajes("Error al actualizar: " + ex.getMessage(), TipoMensaje.ERROR);
            throw ex;
        }

        String retorno = utilidades.ObjetoToJson(mensaje);

        return retorno;
    }
    
    /**
     * 
     * @param request
     * @return Método Liberar Solicitudes
     */
    private String LiberarSolicitud(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);
        try
        {

            error           = false;
            
            Solicitud solicitud = this.ValidarSolicitud(request, null);

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            Retorno_MsgObj retorno = new Retorno_MsgObj();
            
            if(!error)
            {
                retorno     = (Retorno_MsgObj) loSolicitud.liberarSolicitud(solicitud);
            }

            mensaje = retorno.getMensaje(); 
            
        }
        catch(Exception ex)
        {
            mensaje = new Mensajes("Error al actualizar: " + ex.getMessage(), TipoMensaje.ERROR);
            throw ex;
        }

        String retorno = utilidades.ObjetoToJson(mensaje);

        return retorno;
    }
    
    /**
     * 
     * @param request
     * @return Método finalizar Solicitudes
     */
    private String FinalizarSolicitud(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);
        try
        {

            error           = false;
            
            Solicitud solicitud = this.ValidarSolicitud(request, null);

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            Retorno_MsgObj retorno = new Retorno_MsgObj();
            
            if(!error)
            {
                retorno     = (Retorno_MsgObj) loSolicitud.finalizarSolicitud(solicitud);
            }

            mensaje = retorno.getMensaje(); 
            
        }
        catch(Exception ex)
        {
            mensaje = new Mensajes("Error al actualizar: " + ex.getMessage(), TipoMensaje.ERROR);
            throw ex;
        }

        String retorno = utilidades.ObjetoToJson(mensaje);

        return retorno;
    }
    
    /**
     * 
     * @param request
     * @param solicitud
     * @return Método validar Solicitudes
     */
    private Solicitud ValidarSolicitud(HttpServletRequest request, Solicitud solicitud){
        
        if(solicitud == null)
        {
            solicitud   = new Solicitud();
        }

        try
        {
            
            
                String SolCod= request.getParameter("pSolCod");
                String SolTpo= request.getParameter("pSolTpo");
                
                
                //------------------------------------------------------------------------------------------
                //Validaciones
                //------------------------------------------------------------------------------------------

                //TIPO DE DATO


                //Sin validacion
                
                if(SolCod != null) solicitud = ((Solicitud) loSolicitud.obtener(Long.valueOf(SolCod)).getObjeto());
                if(SolTpo != null) solicitud.setSolTpo(TipoSolicitud.fromCode(Integer.valueOf(SolTpo)));
         }
        catch(NumberFormatException | UnsupportedOperationException  ex)
        {
            String texto = ex.getMessage().replace("For input string:", "Tipo de dato incorrecto: ");
            texto = texto.replace("Unparseable date:", "Tipo de dato incorrecto: ");
            
            mensaje = new Mensajes("Error: " + texto, TipoMensaje.ERROR);
            error   = true;
            
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }       
                return solicitud;
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
