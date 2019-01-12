/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import Entidad.Archivo;
import Logica.LoArchivo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet DescargarArchivo
 *
 * @author alvar
 */
@WebServlet(name = "DescargarArchivo", urlPatterns = {"/DescargarArchivo"})
public class DescargarArchivo extends HttpServlet {

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
       try
       {
            Archivo  arch   = null;
            LoArchivo loArchivo = LoArchivo.GetInstancia();


            String 	ArcCod  = request.getParameter("pArcCod");

            if(ArcCod != null) arch = (Archivo) loArchivo.obtener(Long.valueOf(ArcCod)).getObjeto();

        
            
            //------------------------------------------------------------------------------------------
            //Guardar cambios
            //------------------------------------------------------------------------------------------

            if(arch != null)
            {
                        
                File fileToDownload = arch.getArchivo();
                FileInputStream fileInputStream = new FileInputStream(fileToDownload);

                ServletOutputStream out = response.getOutputStream();   
                String mimeType =  new MimetypesFileTypeMap().getContentType(fileToDownload.getAbsolutePath()); 

                response.setContentType(mimeType); 
                response.setContentLength(fileInputStream.available());
                response.setHeader( "Content-Disposition", "attachment; filename=\""+ arch.getArcNom() + "." + arch.getArcExt() + "\"" );

                int c;
                while((c=fileInputStream.read()) != -1){
                 out.write(c);
                }
                out.flush();
                out.close();
                fileInputStream.close();
                
            }
        }
        catch(NumberFormatException | IOException ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
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
