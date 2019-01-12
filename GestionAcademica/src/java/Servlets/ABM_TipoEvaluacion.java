/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;


import Entidad.Parametro;
import Entidad.TipoEvaluacion;
import Enumerado.NombreSesiones;
import Enumerado.TipoMensaje;
import Logica.LoParametro;
import Logica.LoTipoEvaluacion;
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
 * Mantenimiento TipoEvaluacion
 *
 * @author alvar
 */
public class ABM_TipoEvaluacion extends HttpServlet {
    private final Parametro parametro       = LoParametro.GetInstancia().obtener();
    private final Seguridad seguridad       = Seguridad.GetInstancia();
    private final LoTipoEvaluacion loTipoEvaluacion           = LoTipoEvaluacion.GetInstancia();
    private final Utilidades utilidades     = Utilidades.GetInstancia();
    private Mensajes mensaje                = new Mensajes("Error", TipoMensaje.ERROR);
    private Boolean error                   = false;
    
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


                switch(action)
                {

                    case "INSERTAR":
                        retorno = this.AgregarDatos(request);
                    break;

                    case "ACTUALIZAR":
                        retorno = this.ActualizarDatos(request);
                    break;

                    case "ELIMINAR":
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
     * @return Método agregar TipoEvaluacion
     */
    private String AgregarDatos(HttpServletRequest request)
        {
            mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

            try
            {

                error           = false;
                
                TipoEvaluacion tpoEvaluacion = this.ValidarTipoEvaluacion(request, null);

                //------------------------------------------------------------------------------------------
                //Guardar cambios
                //------------------------------------------------------------------------------------------

                if(!error)
                {
                    Retorno_MsgObj retornoObj = (Retorno_MsgObj) loTipoEvaluacion.guardar(tpoEvaluacion);
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
     * @return Método actualizar TipoEvaluacion
     */
    private String ActualizarDatos(HttpServletRequest request)
    {
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);
        try
        {

            error           = false;
            String TpoEvlCod= request.getParameter("pTpoEvlCod");

            Retorno_MsgObj retorno = loTipoEvaluacion.obtener(Long.valueOf(TpoEvlCod));
            error = retorno.getMensaje().getTipoMensaje() == TipoMensaje.ERROR || retorno.getObjeto() == null;

            if(!error)
            {
                TipoEvaluacion tpoEvaluacion = (TipoEvaluacion) retorno.getObjeto();
                tpoEvaluacion = this.ValidarTipoEvaluacion(request, tpoEvaluacion);

                //------------------------------------------------------------------------------------------
                //Guardar cambios
                //------------------------------------------------------------------------------------------

                if(!error)
                {
                    retorno     = (Retorno_MsgObj) loTipoEvaluacion.actualizar(tpoEvaluacion);
                }
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
     * @return Método eliminar TipoEvaluacion
     */
    private String EliminarDatos(HttpServletRequest request)
    {
        error       = false;
        mensaje    = new Mensajes("Error al eliminar", TipoMensaje.ERROR);
        try
        {
            String TpoEvlCod= request.getParameter("pTpoEvlCod");
            Retorno_MsgObj retorno  =  loTipoEvaluacion.obtener(Long.valueOf(TpoEvlCod));
            
            error = retorno.getMensaje().getTipoMensaje() == TipoMensaje.ERROR || retorno.getObjeto() == null;

            if(!error)
            {
                retorno = (Retorno_MsgObj) loTipoEvaluacion.eliminar((TipoEvaluacion) retorno.getObjeto());
            }
            
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
     * @param tpoEvaluacion
     * @return Método Validar TipoEvaluacion
     */
    private TipoEvaluacion ValidarTipoEvaluacion(HttpServletRequest request, TipoEvaluacion tpoEvaluacion)
    {
            if(tpoEvaluacion == null)
            {
                tpoEvaluacion   = new TipoEvaluacion();
            }

            try
            {
                String TpoEvlNom= request.getParameter("pTpoEvlNom");
                String TpoEvlExm= request.getParameter("pTpoEvlExm");
                String TpoEvlInsAut= request.getParameter("pTpoEvlInsAut");
                
                //------------------------------------------------------------------------------------------
                //Validaciones
                //------------------------------------------------------------------------------------------

                //TIPO DE DATO

                


                //Sin validacion
                tpoEvaluacion.setTpoEvlNom(TpoEvlNom);
                tpoEvaluacion.setTpoEvlExm(Boolean.valueOf(TpoEvlExm));
                tpoEvaluacion.setTpoEvlInsAut(Boolean.valueOf(TpoEvlInsAut));
              }
        catch(NumberFormatException | UnsupportedOperationException  ex)
        {
            String texto = ex.getMessage().replace("For input string:", "Tipo de dato incorrecto: ");
            texto = texto.replace("Unparseable date:", "Tipo de dato incorrecto: ");
            
            mensaje = new Mensajes("Error: " + texto, TipoMensaje.ERROR);
            error   = true;
            
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }  
                
            return tpoEvaluacion;
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
