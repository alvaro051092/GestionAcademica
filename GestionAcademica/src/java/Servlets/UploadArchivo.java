/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Entidad.Archivo;
import Enumerado.Extensiones;
import Enumerado.NombreSesiones;
import Enumerado.TipoArchivo;
import Enumerado.TipoMensaje;
import Logica.LoArchivo;
import Logica.Seguridad;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import Utiles.Utilidades;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

/**
 * UploadArchivo
 *
 * @author alvar
 */
public class UploadArchivo extends HttpServlet {
    
    private final Utilidades utilidad = Utiles.Utilidades.GetInstancia();

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
            Retorno_MsgObj acceso = Seguridad.GetInstancia().ControlarAcceso(usuario, esAdm, esDoc, esAlu, utilidad.GetPaginaActual(referer));

            if (acceso.SurgioError() && !utilidad.GetPaginaActual(referer).isEmpty()) {
                Mensajes mensaje = new Mensajes("Acceso no autorizado - " + this.getServletName(), TipoMensaje.ERROR);
                System.err.println("Acceso no autorizado - " + this.getServletName() + " - Desde: " + utilidad.GetPaginaActual(referer));
                out.println(utilidad.ObjetoToJson(mensaje));
            }
            else
            {
            
                System.err.println("Subiendo archivo");
                Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Upload file", TipoMensaje.MENSAJE));

                String tipoArch = request.getParameter("pTpoArc");
                File fichero    = null;

                if(tipoArch == null)
                {
                    //Verifico si recibo archivo
                    try {
                        List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
                        for (FileItem item : items) {
                            if (item.isFormField()) {
                                // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                                String fieldName = item.getFieldName();
                                String fieldValue = item.getString();

                                //--------------------------------
                                //CARGAR VARIABLES
                                //--------------------------------
                                switch (fieldName) {
                                    case "pTpoArc":
                                        tipoArch = fieldValue;
                                        break;
                                }
                                //--------------------------------

                            } else {
                                fichero = new File(utilidad.getPrivateTempStorage(), item.getName());
                                item.write(fichero);
                                
                                if(!utilidad.ArchivoExtValida(item.getName()))
                                {
                                    retorno.setMensaje(new Mensajes("Extensión invalida", TipoMensaje.ERROR));
                                }
                                else
                                {
                                    if(!utilidad.ArchivoSizeValida(fichero.length()))
                                    {
                                        retorno.setMensaje(new Mensajes("El tamaño del archivo es superior a lo permitido", TipoMensaje.ERROR));
                                    }
                                }
                            }
                        }
                    } 
                    catch (FileUploadException ex) {
                        retorno.setMensaje(new Mensajes("Error: " + ex, TipoMensaje.ERROR));
                        Logger.getLogger(UploadArchivo.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        retorno.setMensaje(new Mensajes("Error: " + ex, TipoMensaje.ERROR));
                        Logger.getLogger(UploadArchivo.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if(!retorno.SurgioError())
                    {
                        if(fichero!= null )
                        {
                            TipoArchivo tpoArch = TipoArchivo.valueOf(tipoArch);
                            Archivo archivo = new Archivo();
                            archivo.setArchivo(fichero, tpoArch);
                            archivo.setArcFch(new Date());
                            retorno = (Retorno_MsgObj) LoArchivo.GetInstancia().guardar(archivo);

                            if(!retorno.SurgioError())
                            {
                                archivo = (Archivo) retorno.getObjeto();
                                retorno.setObjeto(archivo.getArcCod());

                                utilidad.eliminarArchivo(fichero.getAbsolutePath());
                            }
                        }
                        else
                        {
                            retorno.setMensaje(new Mensajes("Error: No se recibió archivo", TipoMensaje.ERROR));
                        }
                    }

                }
                else
                {
                    retorno.setMensaje(new Mensajes("Error: No se recibió archivo " + tipoArch, TipoMensaje.ERROR));
                }

                System.err.println("Resultado " +retorno.toString());
                out.println(utilidad.ObjetoToJson(retorno));
                
            }
        }
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
