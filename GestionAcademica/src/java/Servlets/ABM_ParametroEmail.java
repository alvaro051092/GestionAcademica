/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Entidad.ParametroEmail;
import Enumerado.NombreSesiones;
import Enumerado.ProtocoloEmail;
import Enumerado.TipoAutenticacion;
import Enumerado.TipoDato;
import Enumerado.TipoMensaje;
import Enumerado.TipoSSL;
import Logica.LoParametroEmail;
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
 * Mantenimiento ParametroEmail
 *
 * @author alvar
 */
public class ABM_ParametroEmail extends HttpServlet {

    private final LoParametroEmail loParamEmail = LoParametroEmail.GetInstancia();;
    private final Utilidades utilidades         = Utilidades.GetInstancia();
    private Mensajes mensaje                    = new Mensajes("Error", TipoMensaje.ERROR);
    private Boolean error                       = false;
    
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

                    case "OBTENER":
                        retorno = this.ObtnerDatos(request);
                    break;

                    case "INSERTAR":
                        retorno = this.AgregarDatos(request);
                    break;

                    case "ACTUALIZAR":
                        retorno = this.ActualizarDatos(request);
                    break;

                    case "ELIMINAR":
                        retorno = this.EliminarDatos(request);
                    break;

                    case "POPUP_OBTENER":
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
     * @return Método Obtner datos ParametroEmail
     */
    private String ObtnerDatos(HttpServletRequest request)
    {
        String retorno = "";
        
        String ParEmlCod                = request.getParameter("pParEmlCod");
        ParametroEmail parametroEmail   = loParamEmail.obtener(Integer.valueOf(ParEmlCod));
        
        try
        {
            retorno = utilidades.ObjetoToJson(parametroEmail);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return retorno;
    }
    
    /**
     * 
     * @return Método Obtener datos PopUp ParametroEmail
     */
    private String PopObtenerDatos()
    {
        String retorno = "";
        
        List<ParametroEmail> lstParam   = loParamEmail.obtenerLista();
        
        try
        {
            retorno = utilidades.ObjetoToJson(lstParam);
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
     * @return Método agregar ParametroEmail
     */
    private String AgregarDatos(HttpServletRequest request)
    {
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);
        
        try
        {

            error                           = false;
            ParametroEmail parametroEmail   = this.ValidarObjeto(request, null);

            
            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                loParamEmail.guardar(parametroEmail);
                mensaje    = new Mensajes("Cambios guardados correctamente", TipoMensaje.MENSAJE);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        String retorno = utilidades.ObjetoToJson(mensaje);
        
        return retorno;
    }
    
    /**
     * 
     * @param request
     * @return Método actualizar ParametroEmail
     */
    private String ActualizarDatos(HttpServletRequest request)
    {
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);
        
        try
        {

            error                   = false;
            String ParEmlCod   = request.getParameter("pParEmlCod");
                
            ParametroEmail parametroEmail = loParamEmail.obtener(Long.valueOf(ParEmlCod));

            if(parametroEmail != null)
            {
                parametroEmail = this.ValidarObjeto(request, parametroEmail);
                parametroEmail.setParEmlCod(Long.valueOf(ParEmlCod));
            }
           else
            {
                mensaje = new Mensajes("El parametro que quiere actualizar, no existe", TipoMensaje.ERROR); 
                error   = true;
            }
            
            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                loParamEmail.actualizar(parametroEmail);
                mensaje    = new Mensajes("Cambios guardados correctamente", TipoMensaje.MENSAJE);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        String retorno = utilidades.ObjetoToJson(mensaje);
        
        return retorno;
    }
    
    /**
     * 
     * @param request
     * @return Método eliminar ParametroEmail
     */
    private String EliminarDatos(HttpServletRequest request)
    {
        error      = false;
        mensaje    = new Mensajes("Error al eliminar", TipoMensaje.ERROR);
        try
        {
            String ParEmlCod    = request.getParameter("pParEmlCod");

            ParametroEmail parametroEmail = new ParametroEmail();

            if(utilidades.ValidarTipoDato(TipoDato.NUMERO_ENTERO, ParEmlCod))
                {
                    parametroEmail = loParamEmail.obtener(Long.valueOf(ParEmlCod)); 

                    if(parametroEmail == null)
                    {
                        mensaje = new Mensajes("El parametro que quiere eliminar, no existe", TipoMensaje.ERROR); 
                        error   = true;
                    }
                }
                else { mensaje = new Mensajes("Tipo de dato incorrecto: ParEmlCod", TipoMensaje.ERROR); error   = true; }


            if(!error)
            {
                loParamEmail.eliminar(parametroEmail);
                mensaje    = new Mensajes("Cambios guardados correctamente", TipoMensaje.MENSAJE);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        
                    
        return utilidades.ObjetoToJson(mensaje);
    }
     
    /**
     * 
     * @param request
     * @param parametroEmail
     * @return Método validar ParametroEmail
     */
    private ParametroEmail ValidarObjeto(HttpServletRequest request, ParametroEmail parametroEmail)
    {
        if(parametroEmail == null)
        {
            parametroEmail   = new ParametroEmail();
        }
        
        try
        {

        String ParEmlNom    = request.getParameter("pParEmlNom");
        String ParEmlPro    = request.getParameter("pParEmlPro");
        String ParEmlSrv    = request.getParameter("pParEmlSrv");
        String ParEmlPrt    = request.getParameter("pParEmlPrt");
        String ParEmlDeNom  = request.getParameter("pParEmlDeNom");
        String ParEmlDeEml  = request.getParameter("pParEmlDeEml");
        String ParEmlUtlAut = request.getParameter("pParEmlUtlAut");
        String ParEmlTpoAut = request.getParameter("pParEmlTpoAut");
        String ParEmlDom    = request.getParameter("pParEmlDom");
        String ParEmlUsr    = request.getParameter("pParEmlUsr");
        String ParEmlPsw    = request.getParameter("pParEmlPsw");
        String ParEmlSSL    = request.getParameter("pParEmlSSL");
        String ParEmlTmpEsp = request.getParameter("pParEmlTmpEsp");
        String ParEmlDebug = request.getParameter("pParEmlDebug");
        String ParEmlReqConf = request.getParameter("pParEmlReqConf");

        //------------------------------------------------------------------------------------------
        //Validaciones
        //------------------------------------------------------------------------------------------

        //TIPO DE DATO

        if(utilidades.ValidarTipoDato(TipoDato.NUMERO_ENTERO, ParEmlPro))
        {parametroEmail.setParEmlPro(ProtocoloEmail.fromCode(Integer.valueOf(ParEmlPro))); }
        else { mensaje = new Mensajes("Tipo de dato incorrecto: ParEmlPro", TipoMensaje.ERROR); error   = true; }

        if(utilidades.ValidarTipoDato(TipoDato.NUMERO_ENTERO, ParEmlPrt))
        {parametroEmail.setParEmlPrt(Integer.valueOf(ParEmlPrt)); }
        else { mensaje = new Mensajes("Tipo de dato incorrecto: ParEmlPrt", TipoMensaje.ERROR); error   = true; }

        if(utilidades.ValidarTipoDato(TipoDato.NUMERO_ENTERO, ParEmlTmpEsp))
        {parametroEmail.setParEmlTmpEsp(Integer.valueOf(ParEmlTmpEsp)); }
        else { mensaje = new Mensajes("Tipo de dato incorrecto: ParEmlTmpEsp", TipoMensaje.ERROR); error   = true; }


        if(utilidades.ValidarTipoDato(TipoDato.BOOLEAN, ParEmlUtlAut))
        {parametroEmail.setParEmlUtlAut(Boolean.valueOf(ParEmlUtlAut)); }
        else { mensaje = new Mensajes("Tipo de dato incorrecto: ParEmlUtlAut", TipoMensaje.ERROR); error   = true; }
        
        
        if(utilidades.ValidarTipoDato(TipoDato.NUMERO_ENTERO, ParEmlTpoAut))
        {parametroEmail.setParEmlTpoAut(TipoAutenticacion.fromCode(Integer.valueOf(ParEmlTpoAut))); }
        else { mensaje = new Mensajes("Tipo de dato incorrecto: ParEmlTpoAut", TipoMensaje.ERROR); error   = true; }
        
        if(utilidades.ValidarTipoDato(TipoDato.NUMERO_ENTERO, ParEmlSSL))
        {parametroEmail.setParEmlSSL(TipoSSL.fromCode(Integer.valueOf(ParEmlSSL))); }
        else { mensaje = new Mensajes("Tipo de dato incorrecto: ParEmlSSL", TipoMensaje.ERROR); error   = true; }
        
        
        //Sin validacion
        parametroEmail.setParEmlNom(ParEmlNom);
        parametroEmail.setParEmlSrv(ParEmlSrv);
        parametroEmail.setParEmlDeNom(ParEmlDeNom);
        parametroEmail.setParEmlDeEml(ParEmlDeEml);
        parametroEmail.setParEmlDom(ParEmlDom);
        parametroEmail.setParEmlUsr(ParEmlUsr);
        parametroEmail.setParEmlPsw(ParEmlPsw);
        
        if(ParEmlDebug != null) parametroEmail.setParEmlDebug(Boolean.valueOf(ParEmlDebug));
        if(ParEmlReqConf != null) parametroEmail.setParEmlReqConf(Boolean.valueOf(ParEmlReqConf));
        }
        catch(NumberFormatException | UnsupportedOperationException  ex)
        {
            String texto = ex.getMessage().replace("For input string:", "Tipo de dato incorrecto: ");
            texto = texto.replace("Unparseable date:", "Tipo de dato incorrecto: ");
            
            mensaje = new Mensajes("Error: " + texto, TipoMensaje.ERROR);
            error   = true;
            
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } 
        return parametroEmail;
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
