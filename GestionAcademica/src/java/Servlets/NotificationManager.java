/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Entidad.Notificacion;
import Entidad.NotificacionBandeja;
import Entidad.Persona;
import Enumerado.BandejaEstado;
import Enumerado.BandejaTipo;
import Enumerado.NombreSesiones;
import Enumerado.ObtenerDestinatario;
import Enumerado.TipoEnvio;
import Enumerado.TipoMensaje;
import Enumerado.TipoNotificacion;
import Enumerado.TipoRepeticion;
import Logica.LoBandeja;
import Logica.LoNotificacion;
import Logica.LoPersona;
import Logica.Notificacion.AsyncNotificar;
import Logica.Seguridad;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import Utiles.Utilidades;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * NotificationManager
 *
 * @author alvar
 */
public class NotificationManager extends HttpServlet {
    private final LoNotificacion loNotificacion     = LoNotificacion.GetInstancia();
    private final Utilidades utilidades             = Utilidades.GetInstancia();
    private final SimpleDateFormat yMd      = new SimpleDateFormat("yyyy-MM-dd");

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
                    case "NOTIFICAR":
                        retorno = this.Notificar(request);
                        break;

                    case "DEPURAR_BITACORA":
                        retorno = this.DepurarBitacora(request);
                        break;

                    case "ELIMINAR_MENSAJE":
                        retorno = this.EliminarBandeja(request);
                        break;

                    case "MENSAJE_OBTENER":
                        retorno = this.VerBandeja(request);
                        break;
                }

                out.print(retorno);
            }
        }
    }
    
    /**
     * 
     * @param request
     * @return Método Notificar
     */
    private String Notificar(HttpServletRequest request){
        Notificacion notificacion = this.ValidarNotificacion(request, null);
        
        //ManejoNotificacion notManager = new ManejoNotificacion();
        
        //notManager.EjecutarNotificacion(notificacion);
        
        AsyncNotificar xthread = null;
        //Long milliseconds = 10000; // 10 seconds
          try {
            xthread = new AsyncNotificar(notificacion, TipoNotificacion.A_DEMANDA);
            xthread.start();
          //  xthread.join(milliseconds);
          } catch (Exception ex) {
              
              Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, "[InterfacesAgent] Error" + ex);
          } finally {
            if (xthread != null && xthread.isAlive()) {
              Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, "[InterfacesAgent] Interrupting" );
              xthread.interrupt();
            }
          }
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Notificando", TipoMensaje.MENSAJE));
        
        return utilidades.ObjetoToJson(retorno.getMensaje());
    }
    
    /**
     * 
     * @param request
     * @return Método Depurar Bitacora
     */
    private String DepurarBitacora(HttpServletRequest request){
        Notificacion notificacion = this.ValidarNotificacion(request, null);
        
        Retorno_MsgObj retorno = loNotificacion.BitacoraDepurar(notificacion);
        
        return utilidades.ObjetoToJson(retorno.getMensaje());
    }
    
    /**
     * 
     * @param request
     * @return Método Eliminar Bandeja
     */
    private String EliminarBandeja(HttpServletRequest request){
        NotificacionBandeja bandeja = this.ValidarBandeja(request);
        
        Retorno_MsgObj retorno = (Retorno_MsgObj) LoBandeja.GetInstancia().eliminar(bandeja);
        
        return utilidades.ObjetoToJson(retorno.getMensaje());
    }
    
    /**
     * 
     * @param request
     * @return Método ver Bandeja
     */
    private String VerBandeja(HttpServletRequest request){
        NotificacionBandeja bandeja = this.ValidarBandeja(request);
        
        bandeja.setNotBanEst(BandejaEstado.LEIDA);
        
        Retorno_MsgObj retorno = (Retorno_MsgObj) LoBandeja.GetInstancia().actualizar(bandeja);
        
        return utilidades.ObjetoToJson(retorno);
    }    
    
    /**
     * 
     * @param request
     * @param notificacion
     * @return Método validar NotificationManager
     */
    private Notificacion ValidarNotificacion(HttpServletRequest request, Notificacion notificacion){
        
        if(notificacion == null)
        {
            notificacion   = new Notificacion();
        }

        try
        {
                String NotCod= request.getParameter("pNotCod");
                String NotAct= request.getParameter("pNotAct");
                String NotApp= request.getParameter("pNotApp");
                String NotAsu= request.getParameter("pNotAsu");
                String NotCon= request.getParameter("pNotCon");
                String NotDsc= request.getParameter("pNotDsc");
                String NotEmail= request.getParameter("pNotEmail");
                String NotNom= request.getParameter("pNotNom");
                String NotObtDest= request.getParameter("pNotObtDest");
                String NotRepHst= request.getParameter("pNotRepHst");
                String NotRepTpo= request.getParameter("pNotRepTpo");
                String NotRepVal= request.getParameter("pNotRepVal");
                String NotTpo= request.getParameter("pNotTpo");
                String NotTpoEnv= request.getParameter("pNotTpoEnv");
                String NotWeb= request.getParameter("pNotWeb");
                
                
                //------------------------------------------------------------------------------------------
                //Validaciones
                //------------------------------------------------------------------------------------------

                //TIPO DE DATO

                


                //Sin validacion
                if(NotCod != null) if(!NotCod.isEmpty()) notificacion = (Notificacion) loNotificacion.obtener(Long.valueOf(NotCod)).getObjeto();
                
                if(NotAct != null) if(!NotAct.isEmpty()) notificacion.setNotAct(Boolean.valueOf(NotAct));
                if(NotApp != null) if(!NotApp.isEmpty()) notificacion.setNotApp(Boolean.valueOf(NotApp));
                if(NotAsu != null) if(!NotAsu.isEmpty()) notificacion.setNotAsu(NotAsu);
                if(NotCon != null) if(!NotCon.isEmpty()) notificacion.setNotCon(NotCon);
                if(NotDsc != null) if(!NotDsc.isEmpty()) notificacion.setNotDsc(NotDsc);
                if(NotEmail != null) if(!NotEmail.isEmpty()) notificacion.setNotEmail(Boolean.valueOf(NotEmail));
                if(NotNom != null) if(!NotNom.isEmpty()) notificacion.setNotNom(NotNom);
                if(NotObtDest != null) if(!NotObtDest.isEmpty()) notificacion.setNotObtDest(ObtenerDestinatario.fromCode(Integer.valueOf(NotObtDest)));
                if(NotRepHst != null) if(!NotRepHst.isEmpty()) notificacion.setNotRepHst(yMd.parse(NotRepHst));
                if(NotRepTpo != null) if(!NotRepTpo.isEmpty()) notificacion.setNotRepTpo(TipoRepeticion.fromCode(Integer.valueOf(NotRepTpo)));
                if(NotRepVal != null) if(!NotRepVal.isEmpty()) notificacion.setNotRepVal(Integer.valueOf(NotRepVal));
                if(NotTpo != null) if(!NotTpo.isEmpty()) notificacion.setNotTpo(TipoNotificacion.fromCode(Integer.valueOf(NotTpo)));
                if(NotTpoEnv != null) if(!NotTpoEnv.isEmpty()) notificacion.setNotTpoEnv(TipoEnvio.fromCode(Integer.valueOf(NotTpoEnv)));
                if(NotWeb != null) if(!NotWeb.isEmpty()) notificacion.setNotWeb(Boolean.valueOf(NotWeb));
}
        catch(NumberFormatException | ParseException | UnsupportedOperationException  ex)
        {
            
            
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
                return notificacion;
        }

    /**
     * 
     * @param request
     * @return Método validar bandeja
     */
    private NotificacionBandeja ValidarBandeja(HttpServletRequest request){
        
        NotificacionBandeja bandeja   = new NotificacionBandeja();
        
        try
        {
                String NotBanCod= request.getParameter("pNotBanCod");
                String NotBanAsu= request.getParameter("pNotBanAsu");
                String NotBanEst= request.getParameter("pNotBanEst");
                String NotBanMen= request.getParameter("pNotBanMen");
                String NotBanTpo= request.getParameter("pNotBanTpo");
                String NotBanPerCod= request.getParameter("pNotBanPerCod");
                
                
                //------------------------------------------------------------------------------------------
                //Validaciones
                //------------------------------------------------------------------------------------------

                //TIPO DE DATO

                


                //Sin validacion
                
                if(NotBanCod != null) if(!NotBanCod.isEmpty()) bandeja = (NotificacionBandeja) LoBandeja.GetInstancia().obtener(Long.valueOf(NotBanCod)).getObjeto();
                if(NotBanAsu != null) if(!NotBanAsu.isEmpty()) bandeja.setNotBanAsu(NotBanAsu);
                if(NotBanEst != null) if(!NotBanEst.isEmpty()) bandeja.setNotBanEst(BandejaEstado.fromCode(Integer.valueOf(NotBanEst)));
                if(NotBanMen != null) if(!NotBanMen.isEmpty()) bandeja.setNotBanMen(NotBanMen);
                if(NotBanTpo != null) if(!NotBanTpo.isEmpty()) bandeja.setNotBanTpo(BandejaTipo.fromCode(Integer.valueOf(NotBanTpo)));
                if(NotBanPerCod != null) if(!NotBanPerCod.isEmpty()) bandeja.setDestinatario((Persona) LoPersona.GetInstancia().obtener(Long.valueOf(NotBanPerCod)).getObjeto());
            }
        catch(NumberFormatException | UnsupportedOperationException  ex)
        {
            
            
            
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }    
                
                return bandeja;
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
