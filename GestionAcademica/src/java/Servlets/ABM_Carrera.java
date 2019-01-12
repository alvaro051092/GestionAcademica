/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Entidad.Carrera;
import Enumerado.NombreSesiones;
import Enumerado.TipoMensaje;
import Logica.LoCarrera;
import Logica.Seguridad;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import Utiles.Utilidades;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Mantenimiento de Carrera
 *
 * @author aa
 */
public class ABM_Carrera extends HttpServlet {

    Mensajes mensaje    = new Mensajes("Error", TipoMensaje.ERROR);
    boolean error       = false; 
    String retorno;
    LoCarrera loCarrera = LoCarrera.GetInstancia();
    private final Utilidades utiles = Utilidades.GetInstancia();
    
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
            Retorno_MsgObj acceso = Seguridad.GetInstancia().ControlarAcceso(usuario, esAdm, esDoc, esAlu, utiles.GetPaginaActual(referer));

            if (acceso.SurgioError() && !utiles.GetPaginaActual(referer).isEmpty()) {
                mensaje = new Mensajes("Acceso no autorizado - " + this.getServletName(), TipoMensaje.ERROR);
                System.err.println("Acceso no autorizado - " + this.getServletName() + " - Desde: " + utiles.GetPaginaActual(referer));
                out.println(utiles.ObjetoToJson(mensaje));
            }
            else
            {
            
                System.out.println("ACTION: " + action);
                switch(action)
                {
                    case "INSERT":
                        retorno = this.IngresarCarrera(request);
                    break;

                    case "UPDATE":
                        retorno = this.ModificarCarrera(request);
                    break;

                    case "DELETE":
                        retorno = this.EliminarCarrera(request);
                    break;

                    case "POPUP_OBTENER":
                        retorno = this.PopUp_ObtenerDatos();
                    break;

                    case "POPUP_OBTENER_PLANES":
                        retorno = this.PopUp_ObtenerPlanesDatos(request);
                    break;

                    default:
                        break;
                }
                out.println(retorno);
            }
        }
    }
    
    /**
     * 
     * @param request
     * @return Método agregar Carrera
     */
    private String IngresarCarrera(HttpServletRequest request)
    {
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

        try
        {

            error           = false;

            Carrera car = this.ValidarCarrera(request, null);

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                Retorno_MsgObj retornoObj = (Retorno_MsgObj) loCarrera.guardar(car);
                mensaje    = retornoObj.getMensaje();
            }
        }
        catch(Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        String retorno = utiles.ObjetoToJson(mensaje);
        
        return retorno;
    } 
    
    /**
     * 
     * @param request
     * @return Método modificar Carrera
     */
    private String ModificarCarrera(HttpServletRequest request)
    {
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);
        try
        {

            error           = false;
            
            Carrera car = this.ValidarCarrera(request, null);

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                Retorno_MsgObj ret     = (Retorno_MsgObj) loCarrera.actualizar(car);
                mensaje = ret.getMensaje();
            }

             
            
        }
        catch(Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        String retorno = utiles.ObjetoToJson(mensaje);
       
        return retorno;
       
    } 
    
    /**
     * 
     * @param request
     * @return Método eliminar Carrera
     */
    private String EliminarCarrera(HttpServletRequest request)
    {   
        error       = false;
        mensaje    = new Mensajes("Error al eliminar", TipoMensaje.ERROR);
        
        try
        {
            Carrera car = this.ValidarCarrera(request, null);

            if(!error)
            {
                Retorno_MsgObj ret  = (Retorno_MsgObj) loCarrera.eliminar(car);
                mensaje             = ret.getMensaje();
            }
        }
        catch(Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        return utiles.ObjetoToJson(mensaje);
    }
    
    /**
     * 
     * @return Método Obtener datos PopUp Carrera
     */
    private String PopUp_ObtenerDatos()
    {
        String retorno = "";
        
        List<Object> lstCarrera   = loCarrera.obtenerLista().getLstObjetos();
        
        try
        {
            retorno = utiles.ObjetoToJson(lstCarrera);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return retorno;
    }
    
    /**
     * 
     * @param request
     * @return Método Obtener Planes Popup Carrera
     */
    private String PopUp_ObtenerPlanesDatos(HttpServletRequest request)
    {
        Carrera car = this.ValidarCarrera(request, null);
        return utiles.ObjetoToJson(car.getPlan());
    }
    
    /**
     * 
     * @param request
     * @param car
     * @return Método Validar Carrera
     */
    private Carrera ValidarCarrera(HttpServletRequest request, Carrera car)
    {
        if(car == null)
        {
            car   = new Carrera();
        }

        try
        {
            String cod  = request.getParameter("pCod");
            String nom  = request.getParameter("pNom");
            String Dsc  = request.getParameter("pDsc");
            String Fac  = request.getParameter("pfac");
            String Crt  = request.getParameter("pCrt");

            //Sin validacion
            if(cod != null) if(!cod.isEmpty()) car = (Carrera) loCarrera.obtener(Long.valueOf(cod)).getObjeto();

            if(nom != null) if(!nom.isEmpty()) car.setCarNom(nom);
            if(Dsc != null) if(!Dsc.isEmpty()) car.setCarDsc(Dsc);
            if(Fac != null) if(!Fac.isEmpty()) car.setCarFac(Fac);
            if(Crt != null) if(!Crt.isEmpty()) car.setCarCrt(Crt);

        }
        catch(NumberFormatException | UnsupportedOperationException  ex)
        {
            String texto = ex.getMessage().replace("For input string:", "Tipo de dato incorrecto: ");
            texto = texto.replace("Unparseable date:", "Tipo de dato incorrecto: ");
            
            mensaje = new Mensajes("Error: " + texto, TipoMensaje.ERROR);
            error   = true;
        }
        return car;
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
