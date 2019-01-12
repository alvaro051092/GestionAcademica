/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica.Notificacion;

import Entidad.ParametroEmail;
import Enumerado.ProtocoloEmail;
import Enumerado.TipoMensaje;
import Enumerado.TipoSSL;
import Logica.LoParametro;
import SDT.SDT_NotificacionEnvio;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.Transport;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;


/**
 *
 * @author alvar
 */
public class NotificacionEmail {
    
    /**
     * Notificar por email
     */
    public NotificacionEmail() {
    }
    
    /**
     * Notificar
     * @param notificacion Notificacion
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj Notificar(SDT_NotificacionEnvio notificacion)
    {
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Notificar email", TipoMensaje.MENSAJE));
        
        ParametroEmail parEmail = LoParametro.GetInstancia().obtener().getParametroEmail();
        
        String destinatario     = notificacion.getDestinatario().getEmail();
        String destinatarioNom  = notificacion.getDestinatario().getNombre();
        
        if(parEmail == null)
        {
            retorno = new Retorno_MsgObj(new Mensajes("Notificar Email - No se a configurado un parámetro", TipoMensaje.ERROR));
        }
        else
        {
            
            //EMAIL Y PERSONA NULOS            
            if(destinatario == null)
            {
                retorno = new Retorno_MsgObj(new Mensajes("Notificar Email - No se recibio destinatario", TipoMensaje.ERROR));
            }
            else
            {
                if(destinatario.isEmpty())
                {
                    retorno = new Retorno_MsgObj(new Mensajes("Notificar Email - No se recibio destinatario", TipoMensaje.ERROR));
                }
            }
            
            if(!retorno.SurgioError())
            {
                
                if(parEmail.getParEmlPro().equals(ProtocoloEmail.SMTP)) 
                {
                    retorno = this.EnviarMailSmtp(retorno, parEmail, notificacion, destinatario, destinatarioNom);
                } 
                else if(parEmail.getParEmlPro().equals(ProtocoloEmail.EWS)) 
                {
                    retorno = this.EnviarMailEWS(retorno, parEmail, notificacion, destinatario, destinatarioNom);
                }
                
                /*
                int timeoutnilisegundos = parEmail.getParEmlTmpEsp() * 1000;
                
                final String username = parEmail.getParEmlUsr();
                final String password = parEmail.getParEmlPsw();

                Properties props = new Properties();

                props.put("mail.smtp.host", parEmail.getParEmlSrv());
                props.put("mail.smtp.port", parEmail.getParEmlPrt());

                props.put("mail.smtp.auth", parEmail.getParEmlUtlAut());

                props.put("mail.smtp.ssl.trust", parEmail.getParEmlSrv());
                props.put("mail.smtp.starttls.enable", "true");


                Session session = Session.getInstance(props,
                  new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                        }
                  });

                try {

                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(parEmail.getParEmlDeEml(), parEmail.getParEmlDeNom()));
                        message.setRecipients(Message.RecipientType.TO,
                                InternetAddress.parse(destinatario));
                        message.setSubject(notificacion.getAsunto());
                        message.setText(notificacion.getContenido());

                        Transport.send(message);

                } catch (MessagingException e) {
                    retorno = new Retorno_MsgObj(new Mensajes("Notificar Email - Error: " + e.getMessage(), TipoMensaje.ERROR));
                    
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
                } catch (UnsupportedEncodingException ex) {
                    retorno = new Retorno_MsgObj(new Mensajes("Notificar Email - Error: " + ex.getMessage(), TipoMensaje.ERROR));
                    
                    Logger.getLogger(NotificacionEmail.class.getName()).log(Level.SEVERE, null, ex);
                }
                */
            }
        }
        
        if(!retorno.SurgioError())
        {
            if(parEmail != null)
            {
                if(parEmail.getParEmlDebug())
                {
                    this.EscribirLog(retorno, "Email enviado");
                } 
                else 
                {
                    retorno.getMensaje().setMensaje("Email enviado");
                }
            }
        }
            

        return retorno;
    }

    /**
     * Enviar email por SMTP
     * @param retorno Retorno
     * @param parEml Parametro Email
     * @param notificacion Notificacion
     * @param destinatario Destinatario Email
     * @param destinatarioNombre Destinatario Nombre
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj EnviarMailSmtp(Retorno_MsgObj retorno, ParametroEmail parEml, SDT_NotificacionEnvio notificacion, String destinatario, String destinatarioNombre){
        final int timeout   = parEml.getParEmlTmpEsp() * 1000;
        final String user   = parEml.getParEmlUsr();
        final String pass   = parEml.getParEmlPsw();

        if(parEml.getParEmlDebug()) EscribirLog(retorno, "Enviando mail por SMTP");
        try
        {
          Properties properties = new Properties();

          properties.put("mail.smtp.host", parEml.getParEmlSrv());
          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Se cargo properties con host");
          properties.put("mail.smtp.port", parEml.getParEmlPrt());
          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Se cargo properties con port");
          if(parEml.getParEmlSSL().equals(TipoSSL.STARTTLS))
          {
            properties.put("mail.smtp.starttls.enable", "true");
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Se cargO properties con usa STARTTLS");
          }
          if (parEml.getParEmlSSL().equals(TipoSSL.SSL))
          {
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Se cargo properties con usa SSL");
          }
          
          properties.put("mail.smtp.timeout", timeout);
          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Se carg� properties timeout");
          if(parEml.getParEmlUtlAut())
          {
            properties.put("mail.smtp.auth", true);
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Se cargo properties requiere autenticacion");
          }
          
          properties.setProperty("mail.smtp.connectiontimeout", timeout + "");
          properties.setProperty("mail.smtp.timeout", timeout + "");
          
          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Se carg� properties timeout " + timeout + " milisegundos");
          
          if(!parEml.getParEmlDom().isEmpty())
          {
            properties.setProperty("mail.auth.ntlm.domain", parEml.getParEmlDom());
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Se cargo properties dominio autenticaci�n, dominio: " + parEml.getParEmlDom());
          }
          
          Session session = Session.getInstance(properties, new Authenticator()
          {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
              return new PasswordAuthentication(user, pass);
            }
          });
          
          if(parEml.getParEmlDebug()) {
            session.setDebug(true);
          } else {
            session.setDebug(false);
          }
          
          Transport transport = null;
          if(parEml.getParEmlSSL().equals(TipoSSL.SSL))
          {
            transport = session.getTransport("smtps");
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Se usa protocolo smtps");
          }
          else
          {
            transport = session.getTransport("smtp");
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Se usa protocolo smtp");
          }
          if(parEml.getParEmlUtlAut())
          {
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Conectando... host, port, username, password");
            transport.connect(parEml.getParEmlSrv(), parEml.getParEmlPrt(), user, pass);
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Conectado... host, port, username, password");
          }
          MimeMessage message = new MimeMessage(session);
          
          if(parEml.getParEmlReqConf())
          {
            message.addHeader("Disposition-Notification-To", parEml.getParEmlDeEml());
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Se setea que requiere confirmaci�n");
          }
          message.setFrom(new InternetAddress(parEml.getParEmlDeEml(), parEml.getParEmlDeNom()));
          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Cargando emisor from: " + parEml.getParEmlDeEml() + " fromName: " + parEml.getParEmlDeNom());
          
          
          message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario, destinatarioNombre));
          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Cargando destinatario To Mail: " + destinatario + " Nombre: " + destinatarioNombre);

          message.setSubject(notificacion.getAsunto(), "utf-8");

          MimeBodyPart messageBodyPart = new MimeBodyPart();
          if(notificacion.getEsHTML())
          {
            messageBodyPart.setHeader("Content-Type", "text/html");
            messageBodyPart.setText(notificacion.getContenido(), "utf-8", "html");
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Mensaje es html");
          }
          else
          {
            messageBodyPart.setText(notificacion.getContenido(), "utf-8");
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Mensaje no es html");
          }
          
          Multipart multipart = new MimeMultipart();
          multipart.addBodyPart(messageBodyPart);

          /*
          
          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Cargando adjuntos....");
          
          
          if ((!xmlAdjuntos.isEmpty()) && (xmlAdjuntos != null))
          {
            List<Adjunto> listaAdjuntos = cargarAdjuntos(xmlAdjuntos);
            for (Adjunto a : listaAdjuntos)
            {
              messageBodyPart = new MimeBodyPart();

              DataSource source = new FileDataSource(a.path);
              if(parEml.getParEmlDebug()) EscribirLog(retorno, "Adjuntando archivo: " + a.path);
              messageBodyPart.setDataHandler(new DataHandler(source));
              messageBodyPart.setFileName(a.nombre);
              multipart.addBodyPart(messageBodyPart);
            }
          }
          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Fin de cargando adjuntos....");

          */

          message.setContent(multipart);
          if(parEml.getParEmlUtlAut())
          {
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Enviando mensaje con requiere autenticacion");

            Transport.send(message);
          }
          else
          {
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Enviando mensaje sin requiere autenticacion");

            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Conectando sin requiere autenticacion");
            Transport.send(message);
          }

          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Mail enviado...");

        }
        catch (MessagingException | UnsupportedEncodingException e)
        {
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Error: " + e.getMessage());
            retorno.getMensaje().setTipoMensaje(TipoMensaje.ERROR);
            Logger.getLogger(NotificacionEmail.class.getName()).log(Level.SEVERE, null, e);
        }

        return retorno;
    }
    
    /**
     *  Enviar email por EWS
     * @param retorno Retorno
     * @param parEml Parametro Email
     * @param notificacion Notificacion
     * @param destinatario Email Destinatario
     * @param destinatarioNombre Destinatario nombre
     * @return  Resultado: RETORNO_MSGOBJ
     */
    private Retorno_MsgObj EnviarMailEWS(Retorno_MsgObj retorno, ParametroEmail parEml, SDT_NotificacionEnvio notificacion, String destinatario, String destinatarioNombre){
        final int timeout   = parEml.getParEmlTmpEsp() * 1000;
        final String user   = parEml.getParEmlUsr();
        final String pass   = parEml.getParEmlPsw();

        if(parEml.getParEmlDebug()) EscribirLog(retorno, "Enviando mail por EWS");
    
        try
        {
          Properties properties = new Properties();
          if (!parEml.getParEmlDom().isEmpty())
          {
            properties.setProperty("mail.auth.ntlm.domain", parEml.getParEmlDom());
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Se carg� properties dominio autenticaci�n, dominio: " + parEml.getParEmlDom());
          }
          
          Session session = Session.getDefaultInstance(properties);
          if(parEml.getParEmlDebug()) {
            session.setDebug(true);
          } else {
            session.setDebug(false);
          }
          
          Transport transport = session.getTransport("ewstransport");
          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Se usa protocolo ewstransport");

          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Conectando... host, port, username, password");
          transport.connect(parEml.getParEmlSrv(), parEml.getParEmlPrt(), user, pass);
          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Conectando... host, port, username, password");

          MimeMessage message = new MimeMessage(session);
          if(parEml.getParEmlReqConf())
          {
            message.addHeader("Disposition-Notification-To", parEml.getParEmlDeEml());
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Se setea que requiere confirmaci�n");
          }
          message.setFrom(new InternetAddress(parEml.getParEmlDeEml(), parEml.getParEmlDeNom()));

          message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Cargando destinatario To Mail: " + destinatario + " Nombre: " + destinatarioNombre);

          message.setSubject(notificacion.getAsunto(), "utf-8");

          MimeBodyPart messageBodyPart = new MimeBodyPart();
          if(notificacion.getEsHTML())
          {
            messageBodyPart.setHeader("Content-Type", "text/html");
            messageBodyPart.setText(notificacion.getContenido(), "utf-8", "html");
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Mensaje es html");
          }
          else
          {
            messageBodyPart.setText(notificacion.getContenido(), "utf-8");
            if(parEml.getParEmlDebug()) EscribirLog(retorno, "Mensaje no es html");
          }
          Multipart multipart = new MimeMultipart();
          multipart.addBodyPart(messageBodyPart);

          /*
          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Cargando adjuntos....");
          if ((!xmlAdjuntos.isEmpty()) && (xmlAdjuntos != null))
          {
            List<Adjunto> listaAdjuntos = cargarAdjuntos(xmlAdjuntos);
            for (Adjunto a : listaAdjuntos)
            {
              messageBodyPart = new MimeBodyPart();

              DataSource source = new FileDataSource(a.path);
              if(parEml.getParEmlDebug()) EscribirLog(retorno, "Adjuntando archivo: " + a.path);
              messageBodyPart.setDataHandler(new DataHandler(source));
              messageBodyPart.setFileName(a.nombre);
              multipart.addBodyPart(messageBodyPart);
            }
          }
          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Fin de cargando adjuntos....");

            */

          message.setContent(multipart);

          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Enviando mail...");
          transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));

          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Mail enviado...");
        }
        catch (MessagingException | UnsupportedEncodingException e)
        {
          if(parEml.getParEmlDebug()) EscribirLog(retorno, "Error: " + e.getMessage());
            retorno.getMensaje().setTipoMensaje(TipoMensaje.ERROR);
            Logger.getLogger(NotificacionEmail.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return retorno;
    }

    /**
     * Escribir log
     * @param retorno Retorno
     * @param mensaje Mensaje
     */
    private void EscribirLog(Retorno_MsgObj retorno, String mensaje){
        
        
        System.err.println("Envio email: " + mensaje);
        
        retorno.getMensaje().setMensaje(retorno.getMensaje().getMensaje()  + "\n" + mensaje);
        
    }
    
}
