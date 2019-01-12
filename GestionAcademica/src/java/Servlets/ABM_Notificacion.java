/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;


import Entidad.Notificacion;
import Enumerado.Modo;
import Enumerado.NombreSesiones;
import Enumerado.ObtenerDestinatario;
import Enumerado.TipoEnvio;
import Enumerado.TipoMensaje;
import Enumerado.TipoNotificacion;
import Enumerado.TipoRepeticion;
import Logica.LoNotificacion;
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
 * Mantenimiento de Notificación
 *
 * @author alvar
 */
public class ABM_Notificacion extends HttpServlet {
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
     * @return Método agregar Notificación
     */
    private String AgregarDatos(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

        try
        {

            error           = false;

            Notificacion notificacion = this.ValidarNotificacion(request, null);

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                Retorno_MsgObj retornoObj = (Retorno_MsgObj) loNotificacion.guardar(notificacion);
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
     * @return Método actualizar Notificación
     */
    private String ActualizarDatos(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);
        try
        {

            error           = false;
            
            Notificacion notificacion = this.ValidarNotificacion(request, null);

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            Retorno_MsgObj retorno = new Retorno_MsgObj();
            
            if(!error)
            {
                retorno     = (Retorno_MsgObj) loNotificacion.actualizar(notificacion);
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
     * @return Método eliminar Notificación
     */
    private String EliminarDatos(HttpServletRequest request){
        error       = false;
        mensaje    = new Mensajes("Error al eliminar", TipoMensaje.ERROR);
        try
        {

            Notificacion notificacion = this.ValidarNotificacion(request, null);

            if(!error)
            {
                mensaje = ((Retorno_MsgObj) loNotificacion.eliminar(notificacion)).getMensaje();
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
     * @param notificacion
     * @return Método validar Notificación
     */
    private Notificacion ValidarNotificacion(HttpServletRequest request, Notificacion notificacion){
        
        if(notificacion == null)
        {
            notificacion   = new Notificacion();
        }

        try
        {
                String NotCod= request.getParameter("pNotCod");
                String NotAct= request.getParameter("pNotAct");
                String NotApp= request.getParameter("pNotApp");
                String NotAsu= request.getParameter("pNotAsu");
                String NotCon= request.getParameter("pNotCon");
                String NotDsc= request.getParameter("pNotDsc");
                String NotEmail= request.getParameter("pNotEmail");
                String NotNom= request.getParameter("pNotNom");
                String NotObtDest= request.getParameter("pNotObtDest");
                String NotRepHst= request.getParameter("pNotRepHst");
                String NotRepTpo= request.getParameter("pNotRepTpo");
                String NotRepVal= request.getParameter("pNotRepVal");
                String NotTpo= request.getParameter("pNotTpo");
                String NotTpoEnv= request.getParameter("pNotTpoEnv");
                String NotWeb= request.getParameter("pNotWeb");
                String NotFchDsd= request.getParameter("pNotFchDsd");
                
                
                //------------------------------------------------------------------------------------------
                //Validaciones
                //------------------------------------------------------------------------------------------

                //TIPO DE DATO

                


                //Sin validacion
                if(NotCod != null) if(!NotCod.isEmpty()) notificacion = (Notificacion) loNotificacion.obtener(Long.valueOf(NotCod)).getObjeto();
                
                if(NotAct != null) if(!NotAct.isEmpty()) notificacion.setNotAct(Boolean.valueOf(NotAct));
                if(NotApp != null) if(!NotApp.isEmpty()) notificacion.setNotApp(Boolean.valueOf(NotApp));
                if(NotAsu != null)  notificacion.setNotAsu(NotAsu);
                if(NotCon != null)  notificacion.setNotCon(NotCon);
                if(NotDsc != null)  notificacion.setNotDsc(NotDsc);
                if(NotEmail != null)  notificacion.setNotEmail(Boolean.valueOf(NotEmail));
                if(NotNom != null)  notificacion.setNotNom(NotNom);
                if(NotObtDest != null) if(!NotObtDest.isEmpty()) notificacion.setNotObtDest(ObtenerDestinatario.fromCode(Integer.valueOf(NotObtDest)));
                if(NotRepTpo != null) if(!NotRepTpo.isEmpty()) notificacion.setNotRepTpo(TipoRepeticion.fromCode(Integer.valueOf(NotRepTpo)));
                if(NotRepVal != null) if(!NotRepVal.isEmpty()) notificacion.setNotRepVal(Integer.valueOf(NotRepVal));
                if(NotTpo != null) if(!NotTpo.isEmpty()) notificacion.setNotTpo(TipoNotificacion.fromCode(Integer.valueOf(NotTpo)));
                if(NotTpoEnv != null) if(!NotTpoEnv.isEmpty()) notificacion.setNotTpoEnv(TipoEnvio.fromCode(Integer.valueOf(NotTpoEnv)));
                if(NotWeb != null) if(!NotWeb.isEmpty()) notificacion.setNotWeb(Boolean.valueOf(NotWeb));

                if(NotRepHst != null)
                {
                    if(!NotRepHst.isEmpty())
                    {
                        notificacion.setNotRepHst(yMd.parse(NotRepHst));
                    }
                    else
                    {
                        notificacion.setNotRepHst(null);
                    }
                }
                
                if(NotFchDsd != null)
                {
                    if(!NotFchDsd.isEmpty())
                    {
                        notificacion.setNotFchDsd(utilidades.StringToDateTime(NotFchDsd));
                    }
                    else
                    {
                        notificacion.setNotFchDsd(null);
                    }
                }
            }
        catch(NumberFormatException | ParseException | UnsupportedOperationException  ex)
        {
            String texto = ex.getMessage().replace("For input string:", "Tipo de dato incorrecto: ");
            texto = texto.replace("Unparseable date:", "Tipo de dato incorrecto: ");
            
            mensaje = new Mensajes("Error: " + texto, TipoMensaje.ERROR);
            error   = true;
            
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }    
                
                return notificacion;
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
