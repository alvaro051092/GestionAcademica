/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica.Notificacion;

import Enumerado.TipoMensaje;
import SDT.SDT_Notificacion;
import SDT.SDT_NotificacionApp;
import SDT.SDT_NotificacionEnvio;
import SDT.SDT_NotificacionNotification;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import Utiles.Utilidades;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alvar
 */
public class NotificacionApp {

    /**
     * Notificar a aplicacion android
     */
    public NotificacionApp() {
    }
    
    /**
     * Enviar notificacion a android
     * @param notificacion  Notificacion
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj Notificar(SDT_NotificacionEnvio notificacion)
    {
        
        //System.err.println("Mensaje: " + );
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Notificar App - Notificar", TipoMensaje.MENSAJE));
        
        try {
           // final String apiKey = "AIzaSyA8YSrq0BeiiJzNS24VlBKvAR1c03rBi0c";
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=AAAAciD0DaY:APA91bEvsL7m7l18MHBMXli8HQQUf-Hje7li6xa8SSwO-5XVxK5HAXKe5QBhiTCI0qArn-WsNG0-xqyjZjbR6xsHz-dzXuKLKcHrR629fT5iQegnGBm0Jqy08TKOsfdC7VDa1JIRFUgF");
            
            conn.setDoOutput(true);
            
            SDT_Notificacion notMobile = new SDT_Notificacion();
            notMobile.setTo(notificacion.getDestinatario().getPersona().getPerAppTkn());
            
            
            String contenido    = Utilidades.GetInstancia().QuitarTagHTML(notificacion.getContenido());
            String asunto       = Utilidades.GetInstancia().QuitarTagHTML(notificacion.getAsunto());
            
            //notMobile.setData(new SDT_NotificacionDato(contenido, asunto));
            notMobile.setNotification(new SDT_NotificacionNotification(contenido, asunto, "ic_launcher", "default"));
            
                
            String input = Utiles.Utilidades.GetInstancia().ObjetoToJson(notMobile);
            
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
            
            System.out.println(response.toString());
            
            if(responseCode == 200)
            {
                //retorno.setMensaje(new Mensajes("Envio correcto", TipoMensaje.MENSAJE));
               
                SDT_NotificacionApp resultado = new SDT_NotificacionApp();
                resultado = (SDT_NotificacionApp) Utiles.Utilidades.GetInstancia().JsonToObject(response.toString(), resultado);
                
                if(resultado.getSuccess() > 0)
                {
                    retorno.setMensaje(new Mensajes("Envio correcto", TipoMensaje.MENSAJE));
                }
                else
                {
                    retorno.setMensaje(new Mensajes("Error", TipoMensaje.ERROR));
                    
                    if(resultado.getResults() != null)
                    {
                        if(resultado.getResults().size() > 0)
                        {
                            retorno.setMensaje(new Mensajes("Error: " + resultado.getResults().get(0).getError(), TipoMensaje.ERROR));
                        }
                    }
                    
                }
                
            }
            else
            {
                retorno.setMensaje(new Mensajes(response.toString(), TipoMensaje.ERROR));
            }
            
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(NotificacionApp.class.getName()).log(Level.SEVERE, null, ex);
            retorno.setMensaje(new Mensajes(ex.getMessage(), TipoMensaje.ERROR));
            
        } catch (ProtocolException ex) {
            Logger.getLogger(NotificacionApp.class.getName()).log(Level.SEVERE, null, ex);
            retorno.setMensaje(new Mensajes(ex.getMessage(), TipoMensaje.ERROR));

        } catch (IOException ex) {
            Logger.getLogger(NotificacionApp.class.getName()).log(Level.SEVERE, null, ex);
            retorno.setMensaje(new Mensajes(ex.getMessage(), TipoMensaje.ERROR));

        }
        
        return retorno;
    }
    
}
