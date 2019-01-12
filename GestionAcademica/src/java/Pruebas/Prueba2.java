/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruebas;

import Entidad.Persona;
import Logica.LoImportacion;
import Logica.LoPeriodo;
import Persistencia.PerManejador;
import SDT.SDT_Notificacion;
import SDT.SDT_NotificacionNotification;
import Utiles.Retorno_MsgObj;
import Utiles.Utilidades;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alvar
 */
public class Prueba2 extends HttpServlet {
    
    

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
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Prueba</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Prueba at " + request.getContextPath() + "</h1>");
            
            
            
            PerManejador perManager = new PerManejador();
            
           Retorno_MsgObj retorno = perManager.obtener(150L, Persona.class);
           
           
           out.println(retorno.toString());
          
           out.println("<br>");
           out.println(!perManager.obtener(1505L, Persona.class).SurgioErrorObjetoRequerido());
            
            
           /* 
            out.println(Utilidades.GetInstancia().ConexionValida("http://192.168.0.106"));
            */
        
           
            /*
            LoImportacion imp = LoImportacion.GetInstancia();
            
            Retorno_MsgObj ret = imp.ImportarPersonasPlan(1L, "C:/tmp/imp.xlsx");
            
            
            out.println(Utiles.Utilidades.GetInstancia().ObjetoToJson(ret));
            */

/*
           SimpleDateFormat dMy = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           String mensaje ="";
           
           String valor1 ="05/10/1992";
           
           try
           {
               String texto = valor1;
              boolean valor = Boolean.valueOf(valor1);
               
               out.println(valor);
               
           }
           catch(Exception ex)
           {
               mensaje = ex.getMessage().replace("For input string:", "Tipo de dato incorrecto: ");
               mensaje = mensaje.replace("Unparseable date:", "Tipo de dato incorrecto: ");
               Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
           }
           
           
           out.println(mensaje);
  */         
           
           
            
            
         //   ManejoNotificacion notManager = new ManejoNotificacion();
            
           // notManager.EjecutarNotificacionAutomaticamente();
            
            
        //    PerCarrera percarrera = new PerCarrera();
        //    PerPersona perPersona = new PerPersona();
            
         //   Retorno_MsgObj retorno = percarrera.obtenerLista();
         //   out.println("Tipo de evaluacion: " + retorno.getMensaje().toString());
            
         //   retorno = perPersona.obtenerLista();
         //   out.println("Persona: " + retorno.getMensaje().toString());
         
         
        // LoTipoEvaluacion loTpoEvl = LoTipoEvaluacion.GetInstancia();
         
        // TipoEvaluacion tpoEvl = new TipoEvaluacion();
         
        // tpoEvl.setTpoEvlNom("A");
        // tpoEvl.setTpoEvlExm(false);
        // tpoEvl.setTpoEvlInsAut(false);
       //  tpoEvl.setObjFchMod(new Date());
          
         //PerManejador perManejador = new PerManejador();
         
         
         /*
         Retorno_MsgObj retorno = (Retorno_MsgObj) loTpoEvl.guardar(tpoEvl);
         
         out.println("Alta:  " + retorno.getObjeto().toString());

         
         tpoEvl = (TipoEvaluacion) retorno.getObjeto();
         
         
         retorno = loTpoEvl.obtener(tpoEvl.getTpoEvlCod());
         
         out.println("Obtener Msg:  " + retorno.getMensaje().toString());
         
         out.println("Obtener:  " + retorno.getObjeto().toString());
        */
         /*
         tpoEvl = (TipoEvaluacion) retorno.getObjeto();
         
         tpoEvl.setTpoEvlNom("B");
         
         retorno = (Retorno_MsgObj) loTpoEvl.actualizar(tpoEvl);
         
         out.println("Actualizar:  " + retorno.getObjeto().toString());
         
         
         retorno = (Retorno_MsgObj) loTpoEvl.eliminar(tpoEvl);
         
         out.println(retorno.getMensaje().toString());
           

         retorno = loTpoEvl.obtenerLista();
         
         out.println(retorno.getMensaje().toString());
         out.println(retorno.getLstObjetos().size());
         
         */

         
            out.println("</body>");
            out.println("</html>");
            
            
            //this.Probar();
            
            //this.ProbarManejoNotificacion();
            
//            SDT_NotificacionApp app = new SDT_NotificacionApp();
            /*SDT_NotificacionAppResultado resultado = new SDT_NotificacionAppResultado();
            
            //resultado.setError("asda");
            resultado.setMessage_id("0:1502071602468440%6593fe6ff9fd7ecd");

            ArrayList<SDT_NotificacionAppResultado> resuls = new ArrayList<>();
            resuls.add(resultado);
            
            app.setResults(resuls);
            
            app.setCanonical_ids(0);
            app.setFailure(0);
            app.setMulticast_id(Long.valueOf("8141774776764165558"));
            app.setSuccess(1);
            
            System.err.println("Resultado: " + Utiles.Utilidades.GetInstancia().ObjetoToJson(app));
            */
            
       //     SDT_NotificacionApp app = new SDT_NotificacionApp();
            
        //    String auxi = "{\"multicast_id\":8141774776764165558,\"success\":1,\"failure\":0,\"canonical_ids\":0}";
            
         //   app =  (SDT_NotificacionApp) Utiles.Utilidades.GetInstancia().JsonToObject(auxi, app);
            
          //  System.err.println(app.getMulticast_id());
        }
    }
  
   
    
    public void Probar(){

        try {
           // final String apiKey = "AIzaSyA8YSrq0BeiiJzNS24VlBKvAR1c03rBi0c";
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=AAAAciD0DaY:APA91bEvsL7m7l18MHBMXli8HQQUf-Hje7li6xa8SSwO-5XVxK5HAXKe5QBhiTCI0qArn-WsNG0-xqyjZjbR6xsHz-dzXuKLKcHrR629fT5iQegnGBm0Jqy08TKOsfdC7VDa1JIRFUgF");
            
            conn.setDoOutput(true);
            
            SDT_Notificacion notificacion = new SDT_Notificacion();
            notificacion.setTo("cluNmdH0708:APA91bHUZUrgE5ia18UIDxawwt_jnPwsP7bxbuyrAn7PT48x9eP3JmSUkavKe3q5yQq9PQOdqjePl0rcf47jxRtz2vLM50YUht5iEoz09V6idLX72oXIPhIxewQOwHSYvCvooOfILCTB");
            //notificacion.setData(new SDT_NotificacionDato("Esto es un mensaje"));

            notificacion.setNotification(new SDT_NotificacionNotification("adasd", "titulo", "icon", "default"));
            
          //  System.err.println(Utiles.Utilidades.GetInstancia().ObjetoToJson(notificacion));
                
            String input = Utiles.Utilidades.GetInstancia().ObjetoToJson(notificacion);
            
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();
            os.close();
            
            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + input);
            System.out.println("Response Code : " + responseCode);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            // print result
            System.out.println(response.toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(Prueba2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(Prueba2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Prueba2.class.getName()).log(Level.SEVERE, null, ex);
        }

     
    }
    
    public void ProbarManejoNotificacion()
    {
        String cadena = "Personas:\n" +
"[%=INICIO_REPETICION]\n" +
"[%=ALUMNO]  tiene el apellido [%=APELLIDO]\n" +
"[%=FIN_REPETICION] fsd sdf sd";
        
//        System.err.println("Cadena: " + cadena);
  //      System.err.println("Resultado: " + ProcesoTags(cadena));
        
        String repeticionTags   = cadena.substring(cadena.indexOf("[%=INICIO_REPETICION]"), cadena.indexOf("[%=FIN_REPETICION]"));
        
        
        repeticionTags = repeticionTags.replace("[%=INICIO_REPETICION]", "");
        
        System.err.println("Resultado: " + repeticionTags);
    }
    
    
    private String ProcesoTags(String mensaje){
        List<String> tags = this.ObtenerTags(mensaje);
        
        for(String unTag : tags)
        {
            
            System.err.println("Tag: " + unTag);
            
            String campo = unTag.replace("[%=", "");
            campo = campo.replace("]", "");
        
            switch(campo)
            {
                case "ALUMNO":
                    mensaje = mensaje.replace(unTag, "Alvaro devotto");
                    break;
                case "PASSWORD":
                    mensaje = mensaje.replace(unTag, "dJ$$ASD!jkj135");
                    break;
                    
            }
            
        }
            
        return mensaje;
    }
    
    private List<String> ObtenerTags(String mensaje){
        List<String> tags = new ArrayList<>();
        
        boolean continuar = true;
        int indice = 0;

        while(continuar){
            indice = mensaje.indexOf("[%=", indice);
            
            if(indice >= 0)
            {
                int fin = mensaje.indexOf("]", indice) + 1;
                String tag = mensaje.substring(indice, fin);
                tags.add(tag);
                
                indice  = fin;
            }
            else
            {
                continuar = false;
            }
        }
        
        return tags;
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
