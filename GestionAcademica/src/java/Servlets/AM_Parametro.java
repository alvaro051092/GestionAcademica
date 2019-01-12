/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Entidad.Parametro;
import Entidad.ParametroEmail;
import Enumerado.NombreSesiones;
import Enumerado.TipoDato;
import Enumerado.TipoMensaje;
import Logica.LoParametro;
import Logica.LoParametroEmail;
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
 * Mantenimiento Parámetro
 *
 * @author alvar
 */
public class AM_Parametro extends HttpServlet {
    
    private final Utilidades utilidades = Utilidades.GetInstancia();
    

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
                Mensajes mensaje = new Mensajes("Acceso no autorizado - " + this.getServletName(), TipoMensaje.ERROR);
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

                    case "ACTUALIZAR":
                        retorno = this.ActualizarDatos(request);
                    break;

                }

                out.println(retorno);
            }
        }
    }
    
    /**
     * 
     * @param request
     * @return Método obtener datos Parámetro
     */
    private String ObtnerDatos(HttpServletRequest request)
    {
        String retorno = "";
        
        Parametro parametro = LoParametro.GetInstancia().obtener();
        
        try
        {
            retorno = utilidades.ObjetoToJson(parametro);
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
     * @return Método actualizar datos Parámetro
     */
    private String ActualizarDatos(HttpServletRequest request){
        Mensajes mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);
        
        String retorno = "";
        
        try
        {
            Boolean error         = false;
            String ParCod         = request.getParameter("pParCod");
            
            Parametro parametro   = LoParametro.GetInstancia().obtener();
            
            String ParEmlCod      = request.getParameter("pParEmlCod");
            String ParSisLocal    = request.getParameter("pParSisLocal");
            String ParUtlMdl      = request.getParameter("pParUtlMdl");
            String ParUrlMdl      = request.getParameter("pParUrlMdl");
            String ParMdlTkn      = request.getParameter("pParMdlTkn");
            String ParUrlSrvSnc   = request.getParameter("pParUrlSrvSnc");
            String ParPswValMsg   = request.getParameter("pParPswValMsg");
            String ParDiaEvlPrv   = request.getParameter("pParDiaEvlPrv");
            String ParTieIna      = request.getParameter("pParTieIna");
            String ParSncAct      = request.getParameter("pParSncAct");
            
            //------------------------------------------------------------------------------------------
            //Validaciones
            //------------------------------------------------------------------------------------------
            
            //Existe Parametro Email
            if(ParEmlCod != null)
            {
                if(!ParEmlCod.isEmpty())
                {
                    if(utilidades.ValidarTipoDato(TipoDato.NUMERO_ENTERO, ParEmlCod))
                    { 
                        
                        LoParametroEmail loParamEml = LoParametroEmail.GetInstancia();
                        ParametroEmail paramEmail   = loParamEml.obtener(Long.valueOf(ParEmlCod));
                        
                        if(paramEmail == null)
                        {
                            mensaje = new Mensajes("El parámetro de email indicado no existe.", TipoMensaje.ERROR); 
                            error   = true;
                        }
                        else
                        {
                            parametro.setParametroEmail(paramEmail);
                        }
                    }
                    else 
                    { 
                    
                        mensaje = new Mensajes("Tipo de dato incorrecto: ParEmlCod", TipoMensaje.ERROR); 
                        error   = true; 
                    }
                }
            }
            
            
            //Tipo de dato
            if(utilidades.ValidarTipoDato(TipoDato.BOOLEAN, ParSisLocal))
            {parametro.setParSisLocal(Boolean.valueOf(ParSisLocal)); }
            else { mensaje = new Mensajes("Tipo de dato incorrecto: ParSisLocal", TipoMensaje.ERROR); error   = true; }
            
            if(utilidades.ValidarTipoDato(TipoDato.BOOLEAN, ParUtlMdl))
            { parametro.setParUtlMdl(Boolean.valueOf(ParUtlMdl)); }
            else { mensaje = new Mensajes("Tipo de dato incorrecto: ParUtlMdl", TipoMensaje.ERROR); error   = true; }
            
            if(utilidades.ValidarTipoDato(TipoDato.NUMERO_ENTERO, ParDiaEvlPrv))
            { parametro.setParDiaEvlPrv(Integer.valueOf(ParDiaEvlPrv)); }
            else { mensaje = new Mensajes("Tipo de dato incorrecto: ParDiaEvlPrv", TipoMensaje.ERROR); error   = true; }
            
            if(utilidades.ValidarTipoDato(TipoDato.NUMERO_ENTERO, ParTieIna))
            { parametro.setParTieIna(Integer.valueOf(ParTieIna)); }
            else { mensaje = new Mensajes("Tipo de dato incorrecto: ParTieIna", TipoMensaje.ERROR); error   = true; }
            
            
            if(utilidades.ValidarTipoDato(TipoDato.BOOLEAN, ParSncAct))
            { parametro.setParSncAct(Boolean.valueOf(ParSncAct)); }
            else { mensaje = new Mensajes("Tipo de dato incorrecto: ParSncAct", TipoMensaje.ERROR); error   = true; }
            
            //Sin validacion
            parametro.setParMdlTkn(ParMdlTkn);
            parametro.setParPswValMsg(ParPswValMsg);
            parametro.setParUrlMdl(ParUrlMdl);
            parametro.setParUrlSrvSnc(ParUrlSrvSnc);


            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(!error)
            {
                LoParametro.GetInstancia().actualizar(parametro);
                mensaje    = new Mensajes("Cambios guardados correctamente", TipoMensaje.MENSAJE);
            }
            
        }
        catch(NumberFormatException | UnsupportedOperationException  ex)
        {
            String texto = ex.getMessage().replace("For input string:", "Tipo de dato incorrecto: ");
            texto = texto.replace("Unparseable date:", "Tipo de dato incorrecto: ");
            
            mensaje = new Mensajes("Error: " + texto, TipoMensaje.ERROR);
            
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        retorno = utilidades.ObjetoToJson(mensaje);

        return retorno;
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
