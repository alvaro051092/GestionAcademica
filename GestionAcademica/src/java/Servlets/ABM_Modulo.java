/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;


import Entidad.Curso;
import Entidad.Modulo;
import Enumerado.NombreSesiones;
import Enumerado.TipoMensaje;
import Enumerado.TipoPeriodo;
import Logica.LoCurso;
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
 * Mantenimiento de Módulo
 *
 * @author alvar
 */
public class ABM_Modulo extends HttpServlet {
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
     * @return Método agregar Módulo
     */
    private String AgregarDatos(HttpServletRequest request)
    {
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

        try
        {

            error         = false;
            Modulo modulo = this.ValidarModulo(request, null);

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                Retorno_MsgObj retornoObj = (Retorno_MsgObj) LoCurso.GetInstancia().ModuloAgregar(modulo);
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
     * @return Método actualizar Módulo
     */
    private String ActualizarDatos(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

        try
        {

            error           = false;
            String ModCod   = request.getParameter("pModCod");
            String CurCod   = request.getParameter("pCurCod");

            Modulo modulo = new Modulo();

            Retorno_MsgObj retorno = LoCurso.GetInstancia().obtener(Long.valueOf(CurCod));
            error = retorno.getMensaje().getTipoMensaje() == TipoMensaje.ERROR || retorno.getObjeto() == null;
            
            if(!error)
            {
                modulo.setModCod(Long.valueOf(ModCod));
                modulo.setCurso((Curso) retorno.getObjeto());

                modulo = modulo.getCurso().getModuloById(modulo.getModCod());

                if(modulo != null)
                {
                    modulo = this.ValidarModulo(request, modulo);
                    modulo.setModCod(Long.valueOf(ModCod));
                }
               else
                {
                    mensaje = new Mensajes("El modulo que quiere actualizar, no existe", TipoMensaje.ERROR); 
                    error   = true;
                }
            }
            

            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                Retorno_MsgObj retornoObj = (Retorno_MsgObj) LoCurso.GetInstancia().ModuloActualizar(modulo);
                    
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
     * @return Método eliminar Módulo
     */
    private String EliminarDatos(HttpServletRequest request)
    {
        error      = false;
        mensaje    = new Mensajes("Error al eliminar", TipoMensaje.ERROR);

        try
        {
            String ModCod    = request.getParameter("pModCod");
            String CurCod    = request.getParameter("pCurCod");

            Retorno_MsgObj retorno = LoCurso.GetInstancia().obtener(Long.valueOf(CurCod));
            error = retorno.getMensaje().getTipoMensaje() == TipoMensaje.ERROR || retorno.getObjeto() == null;
            
            if(!error)
            {
            
                Modulo modulo = ((Curso) retorno.getObjeto()).getModuloById(Long.valueOf(ModCod));

                if(modulo == null)
                {
                    retorno.setMensaje( new Mensajes("La modulo que quiere eliminar, no existe", TipoMensaje.ERROR)); 
                    error   = true;
                }

                if(!error)
                {
                    retorno = (Retorno_MsgObj) LoCurso.GetInstancia().ModuloEliminar(modulo);
                }
            }
            
            mensaje    = retorno.getMensaje();
            

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
     * @param modulo
     * @return Método validar Módulo
     */
    private Modulo ValidarModulo(HttpServletRequest request, Modulo modulo)
    {
        if(modulo == null)
        {
            modulo   = new Modulo();
        }

        try
        {
            String CurCod= request.getParameter("pCurCod");
            String ModNom= request.getParameter("pModNom");
            String ModDsc= request.getParameter("pModDsc");
            String ModTpoPer= request.getParameter("pModTpoPer");
            String ModPerVal= request.getParameter("pModPerVal");
            String ModCntHor= request.getParameter("pModCntHor");

            //Sin validacion
            if(CurCod != null) if(!CurCod.isEmpty()) modulo.setCurso((Curso) LoCurso.GetInstancia().obtener(Long.valueOf(CurCod)).getObjeto());
            
            if(ModNom!= null) modulo.setModNom(ModNom);
            if(ModDsc!= null) modulo.setModDsc(ModDsc);
            if(ModTpoPer!= null) modulo.setModTpoPer(TipoPeriodo.fromCode(Integer.valueOf(ModTpoPer)));
            if(ModPerVal!= null) modulo.setModPerVal(Double.valueOf(ModPerVal));
            if(ModCntHor!= null) modulo.setModCntHor(Double.valueOf(ModCntHor));
        }
        catch(NumberFormatException | UnsupportedOperationException  ex)
        {
            String texto = ex.getMessage().replace("For input string:", "Tipo de dato incorrecto: ");
            texto = texto.replace("Unparseable date:", "Tipo de dato incorrecto: ");
            
            mensaje = new Mensajes("Error: " + texto, TipoMensaje.ERROR);
            error   = true;
        }
        return modulo;
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
