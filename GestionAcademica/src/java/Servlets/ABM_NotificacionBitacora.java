/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;


import Entidad.Notificacion;
import Entidad.NotificacionBitacora;
import Entidad.Persona;
import Enumerado.Modo;
import Enumerado.NombreSesiones;
import Enumerado.NotificacionEstado;
import Enumerado.TipoMensaje;
import Logica.LoNotificacion;
import Logica.LoPersona;
import Logica.Seguridad;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import Utiles.Utilidades;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *  Mantenimiento de NotificacionesBitacora
 * 
 * @author alvar
 */
public class ABM_NotificacionBitacora extends HttpServlet {
    private final LoNotificacion loNotificacion           = LoNotificacion.GetInstancia();
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


                Modo mode = Modo.valueOf(action);


                switch(mode)
                {

                    case INSERT:
                        retorno = this.AgregarDatos(request);
                    break;

                    case UPDATE:
                        retorno = this.ActualizarDatos(request);
                    break;

                    case DELETE:
                        retorno = this.EliminarDatos(request);
                    break;

                }

                out.println(retorno);
            }
        }
        
    }
    
    /**
     * 
     * @param request
     * @return Método agregar NotificacionesBitacora
     */
    private String AgregarDatos(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

        try
        {

            error           = false;

            NotificacionBitacora bitacora = this.ValidarNotificacionBitacora(request, null);

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                Retorno_MsgObj retornoObj = (Retorno_MsgObj) loNotificacion.BitacoraAgregar(bitacora);
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
     * @return Método actualizar NotificacionesBitacora
     */
    private String ActualizarDatos(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);
        try
        {

            error           = false;
            
            NotificacionBitacora bitacora = this.ValidarNotificacionBitacora(request, null);

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            Retorno_MsgObj retorno = new Retorno_MsgObj();
            
            if(!error)
            {
                retorno     = (Retorno_MsgObj) loNotificacion.BitacoraActualizar(bitacora);
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
     * @return Método eliminar NotificacionesBitacora
     */
    private String EliminarDatos(HttpServletRequest request){
        error       = false;
        mensaje    = new Mensajes("Error al eliminar", TipoMensaje.ERROR);
        try
        {

            NotificacionBitacora bitacora = this.ValidarNotificacionBitacora(request, null);

            if(!error)
            {
                mensaje = ((Retorno_MsgObj) loNotificacion.BitacoraEliminar(bitacora)).getMensaje();
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
     * @param bitacora
     * @return Método Validar NotificacionesBitacora
     */
    private NotificacionBitacora ValidarNotificacionBitacora(HttpServletRequest request, NotificacionBitacora bitacora){
        
        if(bitacora == null)
        {
            bitacora   = new NotificacionBitacora();
        }
        try{    
                String NotCod= request.getParameter("pNotCod");
                String NotBitCod= request.getParameter("pNotBitCod");
                String NotBitAsu= request.getParameter("pNotBitAsu");
                String NotBitCon= request.getParameter("pNotBitCon");
                String NotBitDet= request.getParameter("pNotBitDet");
                String NotBitDst= request.getParameter("pNotBitDst");
                String NotBitEst= request.getParameter("pNotBitEst");
                String NotBitFch= request.getParameter("pNotBitFch");
                String NotPerCod= request.getParameter("pNotPerCod");
                
                
                //------------------------------------------------------------------------------------------
                //Validaciones
                //------------------------------------------------------------------------------------------

                //TIPO DE DATO

                


                //Sin validacion
                
                if(NotCod != null) if(!NotCod.isEmpty()) bitacora.setNotificacion((Notificacion) loNotificacion.obtener(Long.valueOf(NotCod)).getObjeto());
                
                if(NotBitCod != null) if(!NotBitCod.isEmpty() && bitacora.getNotificacion() != null) bitacora = bitacora.getNotificacion().ObtenerBitacoraByCod(Long.valueOf(NotBitCod));
                
                if(NotBitAsu != null) if(!NotBitAsu.isEmpty()) bitacora.setNotBitAsu(NotBitAsu);
                if(NotBitCon != null) if(!NotBitCon.isEmpty()) bitacora.setNotBitCon(NotBitCon);
                if(NotBitDet != null) if(!NotBitDet.isEmpty()) bitacora.setNotBitDet(NotBitDet);
                if(NotBitDst != null) if(!NotBitDst.isEmpty()) bitacora.setNotBitDst(NotBitDst);
                if(NotBitEst != null) if(!NotBitEst.isEmpty()) bitacora.setNotBitEst(NotificacionEstado.fromCode(Integer.valueOf(NotBitEst)));
                if(NotBitFch != null) if(!NotBitFch.isEmpty()) bitacora.setNotBitFch(yMd.parse(NotBitFch));
                if(NotPerCod != null) if(!NotPerCod.isEmpty()) bitacora.setPersona((Persona) LoPersona.GetInstancia().obtener(Long.valueOf(NotPerCod)).getObjeto());
            
                }
        catch(NumberFormatException | ParseException | UnsupportedOperationException  ex)
        {
            String texto = ex.getMessage().replace("For input string:", "Tipo de dato incorrecto: ");
            texto = texto.replace("Unparseable date:", "Tipo de dato incorrecto: ");
            
            mensaje = new Mensajes("Error: " + texto, TipoMensaje.ERROR);
            error   = true;
            
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }  
                
                return bitacora;
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
