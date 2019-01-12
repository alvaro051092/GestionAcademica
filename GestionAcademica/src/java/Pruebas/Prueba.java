/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruebas;

import Entidad.TipoEvaluacion;
import Logica.LoEstudio;
import Logica.LoSincronizacion;
import Moodle.MoodleCourse;
import Moodle.MoodleCourseContent;
import Moodle.MoodleModule;
import Moodle.MoodleModuleContent;
import SDT.SDT_Notificacion;
import SDT.SDT_NotificacionNotification;
import Utiles.Retorno_MsgObj;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
public class Prueba extends HttpServlet {
    
    

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


             Retorno_MsgObj lstCursos = LoEstudio.GetInstancia().Mdl_ListaCursos();

            
             for(Object objeto : lstCursos.getLstObjetos())
             {
                 MoodleCourse cru = (MoodleCourse) objeto;
                        
                MoodleCourseContent[] lalo = LoEstudio.GetInstancia().Mdl_ObtenerEstudioContent(cru.getId());

                out.println("Cursos content: " + lalo.length);

                if(lalo.length > 0)
                {
                    for(int i=0; i<lalo.length; i++)
                    {
                        MoodleCourseContent one = lalo[i];
                        out.println("<br>");
                        out.println(one.getName());
                        out.println("<br>");


                        if(one.getMoodleModules() != null)
                        {
                            out.println("Modulos: " + one.getMoodleModules().length);

                            for(int f=0; f<one.getMoodleModules().length; f++)
                            {
                                MoodleModule modulo = one.getMoodleModules()[f];

                                out.println("<br>");
                                out.println(modulo.getModName());


                                if(modulo.getContent() != null)
                                {
                                    out.println("<br>");
                                    out.println("Contenidos: " + modulo.getContent().length);

                                    for(int j=0; j<modulo.getContent().length; j++)
                                    {
                                        out.println("<br>");
                                        MoodleModuleContent con = modulo.getContent()[j];
                                        out.println("<br>");
                                        out.println(con.getFilename());

                                        out.println("<br>");
                                        out.println(con.getFileURL());

                                        
                                        Timestamp stamp = new Timestamp(con.getTimeModified());
                                        Date fechaMod = new Date(stamp.getTime());
                                        
                                        //fechaMod.setTime(con.getTimeModified());
                                        
                                        out.println("<br>" + con.getTimeModified());
                                        out.println("<br>" + fechaMod);
                                        
                                        stamp = new Timestamp(con.getTimeCreated());
                                        fechaMod = new Date(stamp.getTime());
                                        
                                        out.println("<br>" + con.getTimeCreated());
                                        out.println("<br>" + fechaMod);
                                        
                                        //PeriodoEstudioDocumento pe = LoEstudio.GetInstancia().Mdl_ObtenerEstudioArchivo(con);

                                        //---------------------------------------------------------------------------------
                                        //POST REST WEBSERVICE
                                        //---------------------------------------------------------------------------------
                                       /*
                                        Parametro param = LoParametro.GetInstancia().obtener();
                                        String url = param.getParUrlMdl() + Constantes.URL_FOLDER_SERVICIO_MDL.getValor();

                                        out.println("<br>Url servicio: " + url);

                                        Client client = ClientBuilder.newClient();

                                        WebTarget target = client.target(url);

                                        Form form = new Form();
                                        form.param("wstoken", param.getParMdlTkn());
                                        form.param("wsfunction", "core_files_upload");
                                        form.param("component", "user");
                                        form.param("filearea", "draft");
                                        form.param("itemid", "0");
                                        form.param("filepath", "/");
                                        form.param("filename", pe.getDocNom() + (new Date().getTime()) + "." + pe.getDocExt());
                                        form.param("filecontent", pe.getFileBase64());
                                        form.param("contextlevel", "course");
                                        form.param("instanceid", "13");

                                        String requestResult =
                                        target.request(MediaType.APPLICATION_XML_TYPE)
                                            .post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE),
                                                String.class);

                                        out.println("<br>Resultado: " + requestResult);
    */
                                        //---------------------------------------------------------------------------------
                                        /*    
                                        pe.setDocNom("subidoPorWeb");

                                        LoEstudio.GetInstancia().Mdl_SubirArchivoEstudio(pe);
                                        */
                                    }
                                }
                            }
                        }
                    }
                }
             }
 
            /*
            Carrera car = new Carrera();

            car.setCarNom("asdasd");
            
            out.println("<br>" + car.getUpdateQuery());
            
            out.println("<br>" + Utiles.Utilidades.GetInstancia().ObjetoToJson(car));
            */

            /*
            TipoEvaluacion tpoEvl   = new TipoEvaluacion();
            
            tpoEvl.setTpoEvlNom("Agregar");
            Retorno_MsgObj retorno = (Retorno_MsgObj) LoTipoEvaluacion.GetInstancia().guardar(tpoEvl);

            out.println("<br>Guardado");
            out.println("<br>" + retorno.getMensaje().toString());
            out.println("<br>" + retorno.getObjeto().toString());
            
            //--------------------------------------------------------
            
            tpoEvl = (TipoEvaluacion) retorno.getObjeto();
            
            retorno =  LoTipoEvaluacion.GetInstancia().obtener(tpoEvl.getTpoEvlCod());
            
            out.println("<br>Obtenido");
            out.println("<br>" + retorno.getMensaje().toString());
            out.println("<br>" + retorno.getObjeto().toString());
            
            //--------------------------------------------------------
                    
            tpoEvl.setTpoEvlNom("Modificado");
            retorno = (Retorno_MsgObj) LoTipoEvaluacion.GetInstancia().actualizar(tpoEvl);
            
            out.println("<br>Modificado");
            out.println("<br>" + retorno.getMensaje().toString());
            out.println("<br>" + retorno.getObjeto().toString());
            
            //--------------------------------------------------------
            
            tpoEvl = (TipoEvaluacion) retorno.getObjeto();
            
            retorno = (Retorno_MsgObj) LoTipoEvaluacion.GetInstancia().eliminar(tpoEvl);
            
            out.println("<br>Eliminado");
            out.println("<br>" + retorno.getMensaje().toString());
            */
            
            
            
    /*
            
            WSExternalObjects.Sinc.Carrera carOne = new WSExternalObjects.Sinc.Carrera();
            WSExternalObjects.Sinc.PlanEstudio planOne = new WSExternalObjects.Sinc.PlanEstudio();
            
            planOne.setPlaEstCod(44L);
            planOne.setPlaEstNom("PlanOne");
            carOne.getLstPlanEstudio().add(planOne);
            
            Entidad.Carrera carTwo = new Entidad.Carrera();
      
            carOne.setCarNom("asdasd");
            carOne.setCarCod(33L);
            
            out.println("<br>" + carOne.toString());
            
            carTwo.CastFromObject(carOne);
            
            out.println("<br>" + carTwo.toString());
            out.println("<br>" + carTwo.getPlan().toString());
            
            out.println("<br>" + carTwo.getPlan().getClass().getName());
            
            out.println("<p>------------------</p>");
            
            carOne = new WSExternalObjects.Sinc.Carrera();
            carTwo = new Entidad.Carrera();
            Entidad.PlanEstudio planTwo = new Entidad.PlanEstudio();
            
            
            
            planTwo.setPlaEstCod(44L);
            planTwo.setPlaEstNom("PlanTwo");
            carTwo.getPlan().add(planTwo);
            
            carTwo.setCarNom("asdasd");
            carTwo.setCarCod(33L);
            
            out.println("<br>" + carTwo.toString());
            
            carOne.CastFromObject(carTwo);
            
            out.println("<br>" + carOne.toString());
            out.println("<br>" + carOne.getLstPlanEstudio().toString());
            
            out.println("<br>" + carOne.getLstPlanEstudio().getClass().getName());
      */      
      
            /*
            Carrera carrera = (Carrera) LoCarrera.GetInstancia().obtener(1L).getObjeto();
            
            out.println(carrera.getInsertQuery());
            out.println(carrera.getUpdateQuery());
            
            
            for(Objetos ob : Objetos.values())
            {
                Object objeto = Utiles.Utilidades.GetInstancia().GetObjectByName(ob.getClassName());

                out.println("<p>Objeto: " + objeto.getClass().getSimpleName() + "</p>");
                out.println("<p>" + Utiles.Utilidades.GetInstancia().ObtenerInsertQuery(objeto) + "</p>");
                out.println("<p>" + Utiles.Utilidades.GetInstancia().ObtenerUpdateQuery(objeto) + "</p>");
                out.println("<p>------------------------------------------------------------------------------</p>");
            }
            */
            
            /*
            
            for (Field field : carrera.getClass().getDeclaredFields()) {
                out.println("-------------------");
                out.println("<br>Campo: " + field.getName());
                out.println("<br>Tipo: " + field.getType());
                
                field.setAccessible(true); // You might want to set modifier to public first.
                Object value = field.get(carrera); 
                if (value != null) {
                    out.println("<br>Valor: " + value);
                }
                
                Annotation[] a = field.getAnnotations();
                Annotation[] b = field.getDeclaredAnnotations();
                
                out.println("<p>Anotations a</p>");
                if(a!= null)
                {
                 for(Annotation one : a)
                 {
                     out.println("<p>"+one.annotationType().toString()+ " " + (one.annotationType().equals(Column.class))  + "</p>");
                 }
                }
                
                out.println("<p>Anotations b</p>");
                if(b!=null)
                {
                 for(Annotation one : b)
                 {
                     out.println("<p>"+one.annotationType().toString()+"</p>");
                 }
                }
                
                if(field.getType().equals(String.class))
                {
                    out.println("<p>Es string</p>");
                }
            }
            */
            
            /*
           Sincronizacion sin = new Sincronizacion();
           
           //-----INCONSISTENCIA UNO--------
           SincronizacionInconsistencia inc = new SincronizacionInconsistencia();
           
           //SincInconsistenciaDatos dat      = new SincInconsistenciaDatos(inc, LoSincronizacion.GetInstancia().ObjetoObtenerByNombre(Objetos.TIPO_EVALUACION.name()), "{\\\"objFchMod\\\":1503169998000,\\\"tpoEvlInsAut\\\":true,\\\"tpoEvlExm\\\":false,\\\"tpoEvlCod\\\":2,\\\"tpoEvlNom\\\":\\\"Obligatorio -\\\"}");
           //SincInconsistenciaDatos dat2     = new SincInconsistenciaDatos(inc, LoSincronizacion.GetInstancia().ObjetoObtenerByNombre(Objetos.TIPO_EVALUACION.name()), "{\\\"objFchMod\\\":1503169998000,\\\"tpoEvlInsAut\\\":true,\\\"tpoEvlExm\\\":false,\\\"tpoEvlCod\\\":2,\\\"tpoEvlNom\\\":\\\"Obligatorio -\\\"}");
           
           //inc.getLstDatos().add(dat);
           //inc.getLstDatos().add(dat2);

           inc.setIncEst(EstadoInconsistencia.CON_ERRORES);
           //inc.setObjetoSeleccionado(dat);

           inc.setSincronizacion(sin);

           sin.getLstInconsistencia().add(inc);
           
           //-----INCONSISTENCIA DOS--------
           inc = new SincronizacionInconsistencia();
           //dat = new SincInconsistenciaDatos(inc, LoSincronizacion.GetInstancia().ObjetoObtenerByNombre(Objetos.TIPO_EVALUACION.name()), "{\\\"objFchMod\\\":1503169998000,\\\"tpoEvlInsAut\\\":true,\\\"tpoEvlExm\\\":false,\\\"tpoEvlCod\\\":2,\\\"tpoEvlNom\\\":\\\"Obligatorio -\\\"}");
           
          // inc.getLstDatos().add(dat);
           
           inc.setIncEst(EstadoInconsistencia.CORRECTO);
         //  inc.setObjetoSeleccionado(dat);

           inc.setSincronizacion(sin);

           sin.getLstInconsistencia().add(inc);
           
           
           
           
           sin.setSncFch(new Date());
           sin.setSncDur("475 milisegundos");
           sin.setSncEst(EstadoSincronizacion.CON_ERRORES);
           sin.setSncObjDet("1503170016959 - Inicio el proceso de sincronización\nSat Aug 19 16:13:37 UYT 2017 - Surgio error al sincronizar con el sistema online - Genero inconsistencias que deberan ser corregidas\nSat Aug 19 16:13:37 UYT 2017 - Fin del proceso\n");
           
            System.err.println("Lst:_ " + sin.getLstInconsistencia().size());
           
            System.err.println("->" + Utiles.Utilidades.GetInstancia().ObjetoToJson(sin));
            
           LoSincronizacion.GetInstancia().guardar(sin);
            
            */
          /*
            tpoEvl.setTpoEvlNom("sasdas");
            
            String json = Utiles.Utilidades.GetInstancia().ObjetoToJson(tpoEvl);
            
            Class cls = this.GetClass(TipoEvaluacion.class.getName());
            
            
            
            out.println(cls.getName());
            
            
            try {
                //objeto = ;
                
                //Object objeto = Utiles.Utilidades.GetInstancia().JsonToObject(json, cls.getConstructor());
            
                
                Object objeto = cls.getConstructor().newInstance();
                
                out.println(objeto.toString());
                
              //  objeto = Utiles.Utilidades.GetInstancia().JsonToObject(json, objeto);
                
              //  out.println(objeto.toString());
              
              PerManejador perManager = new PerManejador();
              
              out.println(perManager.GetPrimaryKeyFromObject(objeto));
                
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(Prueba.class.getName()).log(Level.SEVERE, null, ex);
            }
*/
            

           
           //sinc.ActualizarFecha(new Date());
           
           
           //LoSincronizacion.GetInstancia().Sincronizar();
           
           //Retorno_MsgObj cambios = new SincRetorno(new Mensajes("aca te va", TipoMensaje.MENSAJE));
           
         //  SincronizarWSClient cli = new SincronizarWSClient();
         //  SincRetorno retorno = cli.Sincronizar(null);
           
         //  out.println(retorno.getMensaje().toString());
           
            out.println("</body>");
            out.println("</html>");
            
            
            
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Prueba.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
   public void lstAddUno(List<Object> lst){
       this.lstAddDos(lst);
   }
   
   public void lstAddDos(List<Object> lst){
       TipoEvaluacion tpoEvl   = new TipoEvaluacion();
       tpoEvl.setTpoEvlCod(Long.valueOf("2"));
       tpoEvl.setTpoEvlNom("Dos");
       
       lst.add(tpoEvl);
       
       tpoEvl   = new TipoEvaluacion();
       tpoEvl.setTpoEvlCod(Long.valueOf("1"));
       tpoEvl.setTpoEvlNom("Uno");
       
       if(lst.contains(tpoEvl))
       {
           System.err.println("Existe el tipo evaluacion uno");
       }
       else
       {
           System.err.println("Una cagada");
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

            notificacion.setNotification(new SDT_NotificacionNotification("adasd", "titulo", "ic_launcher", "default"));
            
                
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
            Logger.getLogger(Prueba.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(Prueba.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Prueba.class.getName()).log(Level.SEVERE, null, ex);
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

    
    private Class<?> GetClass(String nombre){
        Class<?> act = null;
        
        try {
            act = Class.forName(nombre);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoSincronizacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return act;
    }
    
}
