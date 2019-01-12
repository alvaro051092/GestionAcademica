/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Entidad.BitacoraProceso;
import Enumerado.NombreSesiones;
import Enumerado.TipoMensaje;
import Logica.LoBitacora;
import Logica.Seguridad;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import Utiles.Utilidades;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Mantenimiento de Bitacora
 *
 * @author alvar
 */
public class ABM_Bitacora extends HttpServlet {

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

                        case "DELETE":
                            retorno = this.EliminarDatos(request);
                        break;
                        
                        case "DEPURAR":
                            retorno = this.Depurar();
                        break;


                    
                    }

                out.println(retorno);
            }
        
        }
    }
    
    /**
     * 
     * @param request
     * @return Método eliminar datos Bitácora
     */
    private String EliminarDatos(HttpServletRequest request){
        BitacoraProceso bit = this.ValidarBitacora(request);
        Retorno_MsgObj retorno = (Retorno_MsgObj) LoBitacora.GetInstancia().eliminar(bit);
        return utilidades.ObjetoToJson(retorno.getMensaje());
    }
    
    /**
     * 
     * @return Método Depurar Bitácora
     */
    private String Depurar(){
       Retorno_MsgObj retorno = (Retorno_MsgObj) LoBitacora.GetInstancia().depurar();
       return utilidades.ObjetoToJson(retorno.getMensaje()); 
    }
    
    /**
     * 
     * @param request
     * @return Método Validar Bitácora
     */
    private BitacoraProceso ValidarBitacora(HttpServletRequest request){
        BitacoraProceso bit = new BitacoraProceso();
        
        String BitCod = request.getParameter("pBitCod");
        
        if(BitCod != null) bit = (BitacoraProceso) LoBitacora.GetInstancia().obtener(Long.valueOf(BitCod)).getObjeto();
        
        return bit;
        
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
