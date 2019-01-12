/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Entidad.Archivo;
import Enumerado.NombreSesiones;
import Enumerado.TipoMensaje;
import Logica.LoArchivo;
import Logica.LoImportacion;
import Logica.Seguridad;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import Utiles.Utilidades;

/**
 * Mantenimiento Importacion
 *
 * @author alvar
 */
public class ABM_Importacion extends HttpServlet {

    private Mensajes mensaje                = new Mensajes("Error", TipoMensaje.ERROR);
    private final Utilidades utilidades     = Utilidades.GetInstancia();
    
    
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

                        case "IMP_AL_PLAN":
                            retorno = this.ImpAlPlan(request);
                        break;

                        case "IMP_AL_CURSO":
                            retorno = this.ImpAlCurso(request);
                        break;

                        case "IMP_AL_ESCOLARIDAD":
                            retorno = this.ImpAlEscolaridad(request);
                        break;

                    
                    }

                out.println(retorno);
            }
        }
        
    }
    
    /**
     * 
     * @param request
     * @return Método Importar al Plan
     */
    private String ImpAlPlan(HttpServletRequest request){
        Archivo archivo = this.ValidarArchivo(request);
        
        String PlaEstCod = request.getParameter("pPlaEstCod");
        
        Retorno_MsgObj retorno = LoImportacion.GetInstancia().ImportarPersonasPlan(Long.valueOf(PlaEstCod), archivo.getArchivo().getAbsolutePath());
        
        LoArchivo.GetInstancia().eliminar(archivo);
        
        return utilidades.ObjetoToJson(retorno.getMensaje());
    }
    
    /**
     * 
     * @param request
     * @return Método Importar al Curso
     */
    private String ImpAlCurso(HttpServletRequest request){
        Archivo archivo = this.ValidarArchivo(request);
        
        String CurCod = request.getParameter("pCurCod");
        
        Retorno_MsgObj retorno = LoImportacion.GetInstancia().ImportarPersonasCurso(Long.valueOf(CurCod), archivo.getArchivo().getAbsolutePath());
        
        LoArchivo.GetInstancia().eliminar(archivo);
        
        return utilidades.ObjetoToJson(retorno.getMensaje());
    }
    
    /**
     * 
     * @param request
     * @return Método Importar Escolaridad
     */
    private String ImpAlEscolaridad(HttpServletRequest request){
        Archivo archivo = this.ValidarArchivo(request);
        
        Retorno_MsgObj retorno = LoImportacion.GetInstancia().ImportarPersonasEscolaridad(archivo.getArchivo().getAbsolutePath());
        
        LoArchivo.GetInstancia().eliminar(archivo);
        
        return utilidades.ObjetoToJson(retorno.getMensaje());
    }
    
    /**
     * 
     * @param request
     * @return Método validar archivo
     */
    private Archivo ValidarArchivo(HttpServletRequest request){
        Archivo archivo = new Archivo();
        
        String ArcCod = request.getParameter("pArcCod");
        
        if(ArcCod != null) archivo = (Archivo) LoArchivo.GetInstancia().obtener(Long.valueOf(ArcCod)).getObjeto();
        
        return archivo;
        
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
