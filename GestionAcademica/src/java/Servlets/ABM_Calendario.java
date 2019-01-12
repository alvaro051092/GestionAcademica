/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;


import Entidad.Calendario;
import Entidad.Evaluacion;
import Enumerado.Modo;
import Enumerado.NombreSesiones;
import Enumerado.TipoMensaje;
import Logica.LoCalendario;
import Logica.LoEvaluacion;
import Logica.Seguridad;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import Utiles.Utilidades;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 * Mantenimiento de Calendario
 *
 * @author alvar
 */
public class ABM_Calendario extends HttpServlet {
    private final LoCalendario loCalendario = LoCalendario.GetInstancia();
    private final Utilidades utilidades     = Utilidades.GetInstancia();
    private Mensajes mensaje                = new Mensajes("Error", TipoMensaje.ERROR);
    private Boolean error                   = false;
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
                mensaje = new Mensajes("Acceso no autorizado - " + this.getServletName(), TipoMensaje.ERROR);
                System.err.println("Acceso no autorizado - " + this.getServletName() + " - Desde: " + utilidades.GetPaginaActual(referer));
                out.println(utilidades.ObjetoToJson(mensaje));
            }
            else
            {
            
            
                String retorno  = "";
                String action   = request.getParameter("pAction");



                    switch(action)
                    {
                        case "INSERT_LIST":
                            retorno = this.GuardarLista(request);
                        break;

                        case "INSCRIBIR_PERIODO":
                            retorno = this.InscribirPeriodo(request);
                        break;

                        case "OBTENER_EVENTO":
                            retorno = this.ObtenerEvento();
                        break;

                        case "POPUP_OBTENER":
                            retorno = this.ObtenerDatos(request);
                        break;

                        default:
                            Modo mode = Modo.valueOf(action);
                            switch(mode)
                            {

                                case INSERT:
                                    retorno = this.AgregarDatos(request);
                                break;

                                case UPDATE:
                                    retorno = this.ActualizarDatos(request);
                                break;

                                case DELETE:
                                    retorno = this.EliminarDatos(request);
                                break;
                            }
                        break;

                    }


                out.println(retorno);
            }
            
        }
        
    }
    
    /**
     * 
     * @param request
     * @return Método guardar lista Calendario
     */
    private String GuardarLista(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

        List<Object> lstCalendario = new ArrayList<>();
        String listaCala    = request.getParameter("pLstCalendario");
        
        if(listaCala != null)
        {
            if(!listaCala.isEmpty())
            {
                lstCalendario       = utilidades.JsonToListObject(listaCala, Calendario.class);
                Retorno_MsgObj retorno = loCalendario.guardarLista(lstCalendario);
                
                mensaje = retorno.getMensaje();
                
            }
        }
        
        return  utilidades.ObjetoToJson(mensaje);
    }
    
    /**
     * 
     * @param request
     * @return Método agregar Calendario
     */
    private String AgregarDatos(HttpServletRequest request){
            mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

            try
            {

                error           = false;
                
                Calendario calendario = this.ValidarCalendario(request, null);
                
               
                //------------------------------------------------------------------------------------------
                //Guardar cambios
                //------------------------------------------------------------------------------------------

                if(!error)
                {
                    Retorno_MsgObj retornoObj = (Retorno_MsgObj) loCalendario.guardar(calendario);
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
     * @return Método actualizar Calendario
     */
    private String ActualizarDatos(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);
        try
        {

            error           = false;
            String CalCod= request.getParameter("pCalCod");


            Retorno_MsgObj retorno = loCalendario.obtener(Long.valueOf(CalCod));
            error = retorno.getMensaje().getTipoMensaje() == TipoMensaje.ERROR || retorno.getObjeto() == null;

            if(!error)
            {
                Calendario calendario = (Calendario) retorno.getObjeto();
                calendario = this.ValidarCalendario(request, calendario);

                //------------------------------------------------------------------------------------------
                //Guardar cambios
                //------------------------------------------------------------------------------------------

                if(!error)
                {
                    retorno     = (Retorno_MsgObj) loCalendario.actualizar(calendario);
                }
            }

            mensaje = retorno.getMensaje(); 
            
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
     * @return Método eliminar Calendario
     */
    private String EliminarDatos(HttpServletRequest request){
        error       = false;
        mensaje    = new Mensajes("Error al eliminar", TipoMensaje.ERROR);
        try
        {
            String CalCod= request.getParameter("pCalCod");

            Retorno_MsgObj retorno  =  loCalendario.obtener(Long.valueOf(CalCod));
            
            error = retorno.getMensaje().getTipoMensaje() == TipoMensaje.ERROR || retorno.getObjeto() == null;

            if(!error)
            {
                retorno = (Retorno_MsgObj) loCalendario.eliminar((Calendario) retorno.getObjeto());
            }
            
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
     * @return Método Inscribir Período Calendario
     */
    private String InscribirPeriodo(HttpServletRequest request){
        mensaje    = new Mensajes("Error al guardar datos", TipoMensaje.ERROR);

            try
            {
                String CalCod       = request.getParameter("pCalCod");
                String PeriEstCod   = request.getParameter("pPeriEstCod");
                String InsTpo       = request.getParameter("pInsTpo");
                
                
                
                Retorno_MsgObj retornoObj = new Retorno_MsgObj();
                
                if(InsTpo.equals("ALUMNO"))
                {
                    retornoObj = (Retorno_MsgObj) loCalendario.AlumnoAgregarPorPeriodo(Long.valueOf(CalCod), Long.valueOf(PeriEstCod));
                }
                
                if(InsTpo.equals("DOCENTE"))
                {
                    retornoObj = (Retorno_MsgObj) loCalendario.DocenteAgregarPorPeriodo(Long.valueOf(CalCod), Long.valueOf(PeriEstCod));
                }
 
                mensaje    = retornoObj.getMensaje();
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
     * @return Método Obtener Evento Calendario
     */
    private String ObtenerEvento(){
        return utilidades.ObjetoToJson(loCalendario.ObtenerEventoTodosCalendario());
    }
    
    /**
     * 
     * @param request
     * @return Método Obtener datos Calendario
     */
    private String ObtenerDatos(HttpServletRequest request)
    {
        Calendario calendario = this.ValidarCalendario(request, null);
        return utilidades.ObjetoToJson(calendario);
    }
        
    /**
     * 
     * @param request
     * @param calendario
     * @return Método validar Calendario
     */
    private Calendario ValidarCalendario(HttpServletRequest request, Calendario calendario){
            
        
        if(calendario == null)
            {
                calendario   = new Calendario();
            }
        
        try
        {

                SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
            
                String CalCod= request.getParameter("pCalCod");
                String EvlCod= request.getParameter("pEvlCod");
                String CalFch= request.getParameter("pCalFch");
                String EvlInsFchDsd= request.getParameter("pEvlInsFchDsd");
                String EvlInsFchHst= request.getParameter("pEvlInsFchHst");
                
                //------------------------------------------------------------------------------------------
                //Validaciones
                //------------------------------------------------------------------------------------------

                //TIPO DE DATO

                
                
                //Sin validacion
                if(CalCod != null) if(!CalCod.isEmpty()) calendario = (Calendario) loCalendario.obtener(Long.valueOf(CalCod)).getObjeto();
    
                Evaluacion evaluacion = new Evaluacion();
                if(EvlCod !=null) evaluacion  = (Evaluacion) LoEvaluacion.GetInstancia().obtener(Long.valueOf(EvlCod)).getObjeto();
                
                if(EvlCod !=null) calendario.setEvaluacion(evaluacion);

               
                    if(CalFch !=null) if(!CalFch.isEmpty()) calendario.setCalFch(sdf.parse(CalFch));

                    if(EvlInsFchDsd !=null)
                    {
                        if(!EvlInsFchDsd.isEmpty()) calendario.setEvlInsFchDsd(yMd.parse(EvlInsFchDsd));
                        if(EvlInsFchDsd.isEmpty()) calendario.setEvlInsFchDsd(null);
                    }

                    if(EvlInsFchHst !=null) 
                    {
                        if(!EvlInsFchHst.isEmpty()) calendario.setEvlInsFchHst(yMd.parse(EvlInsFchHst));
                        if(EvlInsFchHst.isEmpty()) calendario.setEvlInsFchHst(null);
                    }
                
        }        
        catch(NumberFormatException | ParseException | UnsupportedOperationException  ex)
        {
            String texto = ex.getMessage().replace("For input string:", "Tipo de dato incorrecto: ");
            texto = texto.replace("Unparseable date:", "Tipo de dato incorrecto: ");
            
            mensaje = new Mensajes("Error: " + texto, TipoMensaje.ERROR);
            error   = true;
            
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
                
            return calendario;
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
