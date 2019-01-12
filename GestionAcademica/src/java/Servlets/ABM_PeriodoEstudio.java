/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Entidad.Carrera;
import Entidad.Curso;
import Entidad.Materia;
import Entidad.Modulo;
import Entidad.Periodo;
import Entidad.PeriodoEstudio;
import Entidad.Persona;
import Entidad.PlanEstudio;
import Enumerado.NombreSesiones;
import Enumerado.TipoMensaje;
import Logica.LoCarrera;
import Logica.LoCurso;
import Logica.LoPeriodo;
import Logica.LoPersona;
import Logica.Seguridad;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import Utiles.Utilidades;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Mantenimiento PeriodoEstudio
 *
 * @author alvar
 */
public class ABM_PeriodoEstudio extends HttpServlet {
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

                    case "POPUP_LISTAR":
                        retorno = this.PopObtenerDatos();
                    break;


                }

                out.println(retorno);
            } 
        }
    }
    
    
    /**
     * 
     * @param request
     * @return Método agregar PeriodoEstudio
     */
    private String AgregarDatos(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

        try
        {
            error           = false;

            PeriodoEstudio periEstudio = this.ValidarPeriodoEstudio(request, null);
            
            Curso curso         = null;
            PlanEstudio plan    = null;
                
            String 	CodigoEstudio   = request.getParameter("pCodigoEstudio");
            String 	tipoEstudio	= request.getParameter("pTipoEstudio");
            
            if(CodigoEstudio != null)
            {
                if(!CodigoEstudio.isEmpty())
                {
                    if(tipoEstudio != null)
                    {
                        if(tipoEstudio.equals("CARRERA"))
                        {
                            String 	CarCod          = request.getParameter("pCarCod");

                            if(CarCod != null) 
                            {
                                if(!CarCod.isEmpty())
                                {
                                   Carrera carrera = (Carrera) LoCarrera.GetInstancia().obtener(Long.valueOf(CarCod)).getObjeto();
                                   plan = carrera.getPlanEstudioById(Long.valueOf(CodigoEstudio));

                                }
                                else
                                {
                                    error = true;
                                    mensaje = new Mensajes("No se recibio carrera", TipoMensaje.ERROR);
                                }
                            }
                            else
                            {
                                error = true;
                                mensaje = new Mensajes("No se recibio carrera", TipoMensaje.ERROR);
                            }
                        }
                        
                        if(tipoEstudio.equals("CURSO"))
                        {
                            curso = (Curso) LoCurso.GetInstancia().obtener(Long.valueOf(CodigoEstudio)).getObjeto();
                        }
                    }
                    else
                    {
                        error = true;
                        mensaje = new Mensajes("No se recibio tipo de estudio", TipoMensaje.ERROR);
                    }
                }
                else
                {
                    error = true;
                    mensaje = new Mensajes("No se recibio estudio", TipoMensaje.ERROR);
                }
            }
            else
            {
                error = true;
                mensaje = new Mensajes("No se recibio estudio", TipoMensaje.ERROR);
            }
            

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                                
                Retorno_MsgObj retornoObj = loPeriodo.guardarPorEstudio(periEstudio.getPeriodo(), curso, plan);
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
     * @return Método eliminar PeriodoEstudio
     */
    private String EliminarDatos(HttpServletRequest request){
        error       = false;
        mensaje    = new Mensajes("Error al eliminar", TipoMensaje.ERROR);
        try
        {
            PeriodoEstudio periEstudio = this.ValidarPeriodoEstudio(request, null);
            
            Retorno_MsgObj retorno = (Retorno_MsgObj) loPeriodo.EstudioEliminar(periEstudio);
            
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
     * @return Método actualizar PeriodoEstudio
     */
    private String ActualizarDatos(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

        try
        {

            error           = false;
            
            PeriodoEstudio periEstudio = ValidarPeriodoEstudio(request, null);
            
            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                Retorno_MsgObj retornoObj = (Retorno_MsgObj) loPeriodo.EstudioActualizar(periEstudio);
                    
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
     * @return Método obtener datos PopUp PeriodoEstudio
     */
    private String PopObtenerDatos(){
        error           = false;
        
        List<Object> lstPeriodoEstudio = new ArrayList<>();
        
        try
        {
            Retorno_MsgObj retorno = loPeriodo.EstudioObtenerTodos();
            mensaje = retorno.getMensaje();
            if(!retorno.SurgioError())
            {
                lstPeriodoEstudio = retorno.getLstObjetos();
            }
        }
        catch(Exception ex)
        {
            mensaje = new Mensajes("Error al obtener: " + ex.getMessage(), TipoMensaje.ERROR);
            throw ex;
        }

       return utilidades.ObjetoToJson(lstPeriodoEstudio);
    }
    
    /**
     * 
     * @param request
     * @param periEstudio
     * @return Método validar PeriodoEstudio
     */
    private PeriodoEstudio ValidarPeriodoEstudio(HttpServletRequest request, PeriodoEstudio periEstudio){
        if(periEstudio == null)
        {
            periEstudio   = new PeriodoEstudio();
        }
        
        try
        {
        
            String PeriCod      = request.getParameter("pPeriCod");
            String PeriEstCod   = request.getParameter("pPeriEstCod");
            String MatEstMatCod = request.getParameter("pMatEstMatCod");
            String ModEstModCod = request.getParameter("pModEstModCod");



            //------------------------------------------------------------------------------------------
            //Validaciones
            //------------------------------------------------------------------------------------------

            //TIPO DE DATO
            if(PeriCod != null) if(!PeriCod.isEmpty()) periEstudio.setPeriodo((Periodo) loPeriodo.obtener(Long.valueOf(PeriCod)).getObjeto());

            if(PeriEstCod != null) if(!PeriEstCod.isEmpty()) periEstudio = (PeriodoEstudio) loPeriodo.EstudioObtener(Long.valueOf(PeriEstCod)).getObjeto();

            if(MatEstMatCod != null) if(!MatEstMatCod.isEmpty()) periEstudio.setMateria((Materia) LoCarrera.GetInstancia().MateriaObtener(Long.valueOf(MatEstMatCod)).getObjeto());

            if(ModEstModCod != null) if(!ModEstModCod.isEmpty()) periEstudio.setModulo( (Modulo) LoCurso.GetInstancia().ModuloObtener(Long.valueOf(ModEstModCod)).getObjeto());
        }
        catch(NumberFormatException | UnsupportedOperationException  ex)
        {
            String texto = ex.getMessage().replace("For input string:", "Tipo de dato incorrecto: ");
            texto = texto.replace("Unparseable date:", "Tipo de dato incorrecto: ");
            
            mensaje = new Mensajes("Error: " + texto, TipoMensaje.ERROR);
            error   = true;
            
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
            
        return periEstudio;
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
