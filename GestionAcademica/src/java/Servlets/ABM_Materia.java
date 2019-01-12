/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Entidad.Carrera;
import Entidad.Materia;
import Entidad.MateriaPrevia;
import Entidad.PlanEstudio;
import Enumerado.NombreSesiones;
import Enumerado.TipoAprobacion;
import Enumerado.TipoMensaje;
import Enumerado.TipoPeriodo;
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
 * Mantenimiento de Materia
 *
 * @author aa
 */
public class ABM_Materia extends HttpServlet {

    private final Utilidades utilidades = Utilidades.GetInstancia();
    private final LoCarrera loCarrera   = LoCarrera.GetInstancia();
    private Mensajes mensaje            = new Mensajes("Error", TipoMensaje.ERROR);
    private Boolean error               = false;             
    
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
                    case "INSERT":
                        retorno = this.AgregarDatos(request);
                    break;   
                    case "UPDATE":
                        retorno = this.ActualizarDatos(request);
                    break;   
                    case "DELETE":
                        retorno = this.EliminarDatos(request);
                    break;
                    case "AGREGAR_PREVIA":
                        retorno = this.AgregarPreviaDatos(request);
                    break;
                    case "ELIMINAR_PREVIA":
                        retorno = this.EliminarPreviaDatos(request);
                    break;
                    case "POPUP_OBTENER":
                        retorno = this.POPUP_ObtenerDatos(request);
                    break;
                }
                out.println(retorno);
                
            }
        }
    }
    
    /**
     * 
     * @param request
     * @return Método agregar Materia
     */
    public String AgregarDatos(HttpServletRequest request)
    {
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

        try
        {
            error         = false;
            Materia materia = this.ValidarMateria(request, null);

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                Retorno_MsgObj retornoObj = (Retorno_MsgObj) LoCarrera.GetInstancia().MateriaAgregar(materia);
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
     * @return Método actualizar Materia
     */
    public String ActualizarDatos(HttpServletRequest request)
    {
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);
        try
        {
            error           = false;
            String CarCod       = request.getParameter("pCarCod");
            String PlaEstCod    = request.getParameter("pPlaEstCod");
            String MatCod       = request.getParameter("pMatCod");

            Materia mat = new Materia();
            PlanEstudio plan = new PlanEstudio();

            Retorno_MsgObj retorno = LoCarrera.GetInstancia().obtener(Long.valueOf(CarCod));
            error = retorno.getMensaje().getTipoMensaje() == TipoMensaje.ERROR || retorno.getObjeto() == null;
            
            if(!error)
            {
                plan.setPlaEstCod(Long.valueOf(PlaEstCod));
                plan.setCarrera((Carrera) retorno.getObjeto());
                
                plan = plan.getCarrera().getPlanEstudioById(plan.getPlaEstCod());
                
                mat.setMatCod(Long.valueOf(MatCod));
                mat.setPlan((PlanEstudio) plan);
                
                mat = mat.getPlan().getMateriaById(mat.getMatCod());
                
                if(mat != null)
                {
                    mat = this.ValidarMateria(request, mat);
                    mat.setMatCod(Long.valueOf(MatCod));
                }
               else
                {
                    mensaje = new Mensajes("La materia que quiere actualizar, no existe", TipoMensaje.ERROR); 
                    error   = true;
                }
            }
            
            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                Retorno_MsgObj retornoObj = (Retorno_MsgObj) LoCarrera.GetInstancia().MateriaActualizar(mat);
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
     * @return Método eliminar Materia
     */
    public String EliminarDatos(HttpServletRequest request)
    {
        error      = false;
        mensaje    = new Mensajes("Error al eliminar", TipoMensaje.ERROR);

        try
        {
            String MatCod       = request.getParameter("pMatCod");
            String CarCod       = request.getParameter("pCarCod");
            String PlaEstCod    = request.getParameter("pPlaEstCod");

            Materia mat = new Materia();
            PlanEstudio plan = new PlanEstudio();
            
            Retorno_MsgObj retorno = LoCarrera.GetInstancia().obtener(Long.valueOf(CarCod));
            error = retorno.getMensaje().getTipoMensaje() == TipoMensaje.ERROR || retorno.getObjeto() == null;

            if(!error)
            {
                plan = ((Carrera) retorno.getObjeto()).getPlanEstudioById(Long.valueOf(PlaEstCod));
                mat = (Materia) plan.getMateriaById(Long.valueOf(MatCod));

                if(mat == null)
                {
                    retorno.setMensaje( new Mensajes("La materia que quiere eliminar, no existe", TipoMensaje.ERROR)); 
                    error   = true;
                }

                if(!error)
                {
                    retorno = (Retorno_MsgObj) LoCarrera.GetInstancia().MateriaEliminar(mat);
                }
            }
            mensaje    = retorno.getMensaje();
        }
        catch(Exception ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return utilidades.ObjetoToJson(mensaje);
    }
    
    /**
     * 
     * @param request
     * @return Método Obtener Datos PopUp
     */
    private String POPUP_ObtenerDatos(HttpServletRequest request)
    {
        List<Object> lstMateria;
        
        String MatCod       = request.getParameter("popMatCod");
        String PlaEstCod    = request.getParameter("popPlaEstCod");
        
        lstMateria  = loCarrera.obtenerPopUp(Long.valueOf(PlaEstCod)).getLstObjetos();
        
        return utilidades.ObjetoToJson(lstMateria);
    }
    
    /**
     * 
     * @param request
     * @return Método agregar previas de Materia
     */
    private String AgregarPreviaDatos(HttpServletRequest request)
    {
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

        try
        {
            error         = false;
            Materia materia = this.ValidarMateria(request, null);
            
            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                String MatCod       = request.getParameter("pPreMatCod");
                
                
                System.err.println("Plan: " + materia.getPlan().getPlaEstCod());
                System.err.println("Materia: " + materia.getMatCod());
                System.err.println("Previa: " + MatCod);
                
                MateriaPrevia matPrevia = new MateriaPrevia();
                matPrevia.setMateria(materia);
                matPrevia.setMateriaPrevia(materia.getPlan().getMateriaById(Long.valueOf(MatCod)));
                
                materia.getLstPrevias().add(matPrevia);
                
                Retorno_MsgObj retornoObj = (Retorno_MsgObj) loCarrera.MateriaActualizar(materia);
                mensaje    = retornoObj.getMensaje();
            }
        }
        catch(Exception ex)
        {
            mensaje = new Mensajes("Error al Eliminar: " + ex.getMessage(), TipoMensaje.ERROR);
            throw ex;
        }
        String retorno = utilidades.ObjetoToJson(mensaje);
        return retorno;
    }
    
    /**
     * 
     * @param request
     * @return Método eliminar previas de Materia
     */
    private String EliminarPreviaDatos(HttpServletRequest request)
    {
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

        try
        {
            error         = false;
            Materia materia = this.ValidarMateria(request, null);
            
            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                String MatCod       = request.getParameter("pMatPreCod");
                
                MateriaPrevia matPrevia = materia.getPreviaById(Long.valueOf(MatCod));
                int indice = materia.getLstPrevias().indexOf(matPrevia);
                
                if(indice >= 0)
                {
                    materia.getLstPrevias().remove(indice);
                    Retorno_MsgObj retornoObj = (Retorno_MsgObj) loCarrera.MateriaActualizar(materia);
                    mensaje    = retornoObj.getMensaje();
                }
            }
        }
        catch(Exception ex)
        {
            mensaje = new Mensajes("Error al Eliminar: " + ex.getMessage(), TipoMensaje.ERROR);
            throw ex;
        }
        String retorno = utilidades.ObjetoToJson(mensaje);
        return retorno;
    }
    
    /**
     * 
     * @param request
     * @param materia
     * @return Método validar Materia
     */
    private Materia ValidarMateria(HttpServletRequest request, Materia materia)
    {
        if(materia == null)
        {
            materia   = new Materia();
        }
        
        try{
            String CarCod       = request.getParameter("pCarCod");
            String PlaEstCod    = request.getParameter("pPlaEstCod");
            String MatCod       = request.getParameter("pMatCod");
            String MatNom       = request.getParameter("pMatNom");
            String MatCntHor    = request.getParameter("pMatCntHor");
            String MatTpoApr    = request.getParameter("pMatTpoApr");
            String MatTpoPer    = request.getParameter("pMatTpoPer");
            String MatPerVal    = request.getParameter("pMatPerVal");
           
            //------------------------------------------------------------------------------------------
            //Validaciones
            //------------------------------------------------------------------------------------------

            //TIPO DE DATO

            //Sin validacion
            
            Carrera carrera     = new Carrera();
            
            System.err.println("CarCod_ " + CarCod);
            System.err.println("PlaEstCod_ " + PlaEstCod);
            System.err.println("MatCod_ " + MatCod);

            if(CarCod != null) carrera = (Carrera) loCarrera.obtener(Long.valueOf(CarCod)).getObjeto();
            if(PlaEstCod != null && CarCod != null) materia.setPlan(carrera.getPlanEstudioById(Long.valueOf(PlaEstCod)));
            if(MatCod != null) if(!MatCod.isEmpty() && PlaEstCod != null) materia = materia.getPlan().getMateriaById(Long.valueOf(MatCod));

            if(MatNom != null) materia.setMatNom(MatNom);
            if(MatCntHor != null) materia.setMatCntHor(Double.valueOf(MatCntHor));
            if(MatTpoApr != null) materia.setMatTpoApr(TipoAprobacion.fromCode(Integer.valueOf(MatTpoApr)));
            if(MatTpoPer != null) materia.setMatTpoPer(TipoPeriodo.fromCode(Integer.valueOf(MatTpoPer)));
            if(MatPerVal != null) materia.setMatPerVal(Double.valueOf(MatPerVal));
            //Objetos
            
           }
        catch(NumberFormatException | UnsupportedOperationException  ex)
        {
            String texto = ex.getMessage().replace("For input string:", "Tipo de dato incorrecto: ");
            texto = texto.replace("Unparseable date:", "Tipo de dato incorrecto: ");
            
            mensaje = new Mensajes("Error: " + texto, TipoMensaje.ERROR);
            error   = true;
            
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
            
        return materia;
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
