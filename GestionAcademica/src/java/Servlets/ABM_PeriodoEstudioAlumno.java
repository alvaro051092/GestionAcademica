/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Entidad.PeriodoEstudioAlumno;
import Entidad.PeriodoEstudio;
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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Mantenimiento PeriodoEstudioAlumno
 *
 * @author alvar
 */
public class ABM_PeriodoEstudioAlumno extends HttpServlet {
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

                    case "INSERT":
                        retorno = this.AgregarDatos(request);
                    break;

                    case "UPDATE":
                        retorno = this.ActualizarDatos(request);
                    break;

                    case "DELETE":
                        retorno = this.EliminarDatos(request);
                    break;

                    case "OBTENER":
                        retorno = this.ObtenerDatos(request);
                    break;

                    case "INGRESAR_GENERACION":
                        retorno = this.IngresarGeneracion(request);
                    break;
                }

                out.println(retorno);
            }
        }
    }
    
    
    /**
     * 
     * @param request
     * @return Método agregar PeriodoEstudioAlumno
     */
    private String AgregarDatos(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

        try
        {
            error           = false;

            PeriodoEstudioAlumno periAlumno = this.ValidarPeriodoEstudioAlumno(request, null);

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                periAlumno.setPerInsFchInsc(new Date());
                if(perUsuario != null) periAlumno.setInscritoPor(perUsuario);
                periAlumno.setPerInsFrz(true);
                
                Retorno_MsgObj retornoObj = (Retorno_MsgObj) loPeriodo.AlumnoAgregar(periAlumno);
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
     * @return Método eliminar PeriodoEstudioAlumno
     */
    private String EliminarDatos(HttpServletRequest request){
        error       = false;
        mensaje    = new Mensajes("Error al eliminar", TipoMensaje.ERROR);
        try
        {
            PeriodoEstudioAlumno periAlumno = this.ValidarPeriodoEstudioAlumno(request, null);
            
            Retorno_MsgObj retorno = (Retorno_MsgObj) loPeriodo.AlumnoEliminar(periAlumno);
            
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
     * @return Método actualizar PeriodoEstudioAlumno
     */
    private String ActualizarDatos(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

        try
        {

            error           = false;
            
            PeriodoEstudioAlumno periAlumno = ValidarPeriodoEstudioAlumno(request, null);
            
            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                Retorno_MsgObj retornoObj = (Retorno_MsgObj) loPeriodo.AlumnoActualizar(periAlumno);
                    
                mensaje    = retornoObj.getMensaje();
            }
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
     * @return Método obtener datos PeriodoEstudioAlumno
     */
    private String ObtenerDatos(HttpServletRequest request){
        error       = false;
        PeriodoEstudioAlumno periAlumno = new PeriodoEstudioAlumno();
        
        try
        {
            periAlumno = this.ValidarPeriodoEstudioAlumno(request, null);
        }
        catch(Exception ex)
        {
            mensaje = new Mensajes("Error al obtener: " + ex.getMessage(), TipoMensaje.ERROR);
            throw ex;
        }

       return utilidades.ObjetoToJson(periAlumno);
    }
    
    /**
     * 
     * @param request
     * @return Método ingresar generación PeriodoEstudioAlumno
     */
    private String IngresarGeneracion(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

        try
        {
            error           = false;

            String 	PeriCod     = request.getParameter("pPeriCod");
            String 	InsGenAnio  = request.getParameter("pInsGenAnio");

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                Retorno_MsgObj retornoObj = loPeriodo.GeneracionAgregar(Long.valueOf(PeriCod) , Integer.valueOf(InsGenAnio));
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
     * @param periAlumno
     * @return Método validar PeriodoEstudioAlumno
     */
    private PeriodoEstudioAlumno ValidarPeriodoEstudioAlumno(HttpServletRequest request, PeriodoEstudioAlumno periAlumno){
        if(periAlumno == null)
        {
            periAlumno   = new PeriodoEstudioAlumno();
        }
        
        try
        {

            String 	PeriEstCod	= request.getParameter("pPeriEstCod");
            String 	PeriEstAluCod   = request.getParameter("pPeriEstAluCod");
            String 	AluPerCod	= request.getParameter("pAluPerCod");

            //------------------------------------------------------------------------------------------
            //Validaciones
            //------------------------------------------------------------------------------------------

            //TIPO DE DATO

            if(PeriEstCod != null) periAlumno.setPeriodoEstudio(((PeriodoEstudio) loPeriodo.EstudioObtener(Long.valueOf(PeriEstCod)).getObjeto()));

            if(PeriEstAluCod != null) periAlumno = periAlumno.getPeriodoEstudio().getAlumnoById(Long.valueOf(PeriEstAluCod));

            if(AluPerCod != null) periAlumno.setAlumno((Persona) LoPersona.GetInstancia().obtener(Long.valueOf(AluPerCod)).getObjeto());
         }
        catch(NumberFormatException | UnsupportedOperationException  ex)
        {
            String texto = ex.getMessage().replace("For input string:", "Tipo de dato incorrecto: ");
            texto = texto.replace("Unparseable date:", "Tipo de dato incorrecto: ");
            
            mensaje = new Mensajes("Error: " + texto, TipoMensaje.ERROR);
            error   = true;
            
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }   
        return periAlumno;
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
