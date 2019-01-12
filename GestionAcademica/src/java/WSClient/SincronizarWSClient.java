/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WSClient;

import Entidad.Parametro;
import Enumerado.ServicioWeb;
import Logica.LoParametro;
import Logica.Seguridad;
import Utiles.Retorno_MsgObj;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

/**
 * WSClient SincronizarWSClient
 *
 * @author alvar
 */
public class SincronizarWSClient {
    
    private final String token = Seguridad.GetInstancia().getTokenWS(ServicioWeb.SINCRONIZAR);

    public SincronizarWSClient(){
        /*
            System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
            System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
            System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
            System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
        */
    }
    
    /**
     *
     * @param cambios
     * @return
     */
    public Retorno_MsgObj Sincronizar(Retorno_MsgObj cambios){
        return sincronizar(token, cambios);
    }
    
    /**
     *
     * @param cambios
     * @return
     */
    public Retorno_MsgObj ImpactarInconsistencia(Retorno_MsgObj cambios){
        return impactar_inconsistencia(token, cambios);
    }
    
    /**
     *
     * @param fecha
     */
    public void ActualizarFecha(Date fecha){
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(fecha);
        XMLGregorianCalendar date2 = null;
        try {
            date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(SincronizarWSClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        updateFecha(token, date2);

    }

    //------------------------------------------------------------------------------------------
    //WS CLIENT
    //------------------------------------------------------------------------------------------
    
    private static Retorno_MsgObj updateFecha(String token, javax.xml.datatype.XMLGregorianCalendar fecha) {
        Retorno_MsgObj retorno = new Retorno_MsgObj();
        
        try {
            Parametro param = LoParametro.GetInstancia().obtener();
            
            URL wsUrl = new URL(param.getParUrlSrvSnc());

            WsSincronizar_Service service = new WsSincronizar_Service(wsUrl);
            WsSincronizar port = service.getWsSincronizarPort();

            //-----------------------------------------------------------------
            //WS SECURITY
            //-----------------------------------------------------------------
            Map<String, Object> reqMap = ((BindingProvider) port).getRequestContext();
            reqMap.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, param.getParUrlSrvSnc());
            Map<String, List<String>> header = new HashMap<>();
            header.put("token", Collections.singletonList(token));
            
            reqMap.put(MessageContext.HTTP_REQUEST_HEADERS, header);
            //-----------------------------------------------------------------
            
            retorno = port.updateFecha(fecha);

        } catch (MalformedURLException ex) {
            
            Logger.getLogger(SincronizarWSClient.class.getName()).log(Level.SEVERE, null, ex);
            
            retorno.getMensaje().setMensaje(ex.getMessage());
            retorno.getMensaje().setTipoMensaje(Enumerado.TipoMensaje.ERROR);

        }
        
        return retorno;
    }

    private static Retorno_MsgObj sincronizar(String token, Retorno_MsgObj cambios) {
        Retorno_MsgObj retorno = new Retorno_MsgObj();
        
        try {
            Parametro param = LoParametro.GetInstancia().obtener();
            
            URL wsUrl = new URL(param.getParUrlSrvSnc());
        
            WsSincronizar_Service service = new WsSincronizar_Service(wsUrl);
            WsSincronizar port = service.getWsSincronizarPort();
            
            //-----------------------------------------------------------------
            //WS SECURITY
            //-----------------------------------------------------------------
            Map<String, Object> reqMap = ((BindingProvider) port).getRequestContext();
            reqMap.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, param.getParUrlSrvSnc());
            Map<String, List<String>> header = new HashMap<>();
            header.put("token", Collections.singletonList(token));
            
            reqMap.put(MessageContext.HTTP_REQUEST_HEADERS, header);
            //-----------------------------------------------------------------
            
            retorno = port.sincronizar(cambios);
            
        } catch (MalformedURLException ex) {
            
            Logger.getLogger(SincronizarWSClient.class.getName()).log(Level.SEVERE, null, ex);
            
            retorno.getMensaje().setMensaje(ex.getMessage());
            retorno.getMensaje().setTipoMensaje(Enumerado.TipoMensaje.ERROR);

        }
        return retorno;
    }
    
    private static Retorno_MsgObj impactar_inconsistencia(String token, Retorno_MsgObj cambios) {
        Retorno_MsgObj retorno = new Retorno_MsgObj();
        
        try {
            Parametro param = LoParametro.GetInstancia().obtener();
            
            URL wsUrl = new URL(param.getParUrlSrvSnc());
        
            WsSincronizar_Service service = new WsSincronizar_Service(wsUrl);
            WsSincronizar port = service.getWsSincronizarPort();
            
            //-----------------------------------------------------------------
            //WS SECURITY
            //-----------------------------------------------------------------
            Map<String, Object> reqMap = ((BindingProvider) port).getRequestContext();
            reqMap.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, param.getParUrlSrvSnc());
            Map<String, List<String>> header = new HashMap<>();
            header.put("token", Collections.singletonList(token));
            
            reqMap.put(MessageContext.HTTP_REQUEST_HEADERS, header);
            //-----------------------------------------------------------------
            
            retorno = port.impactarInconsistencia(cambios);
            
        } catch (MalformedURLException ex) {
            
            Logger.getLogger(SincronizarWSClient.class.getName()).log(Level.SEVERE, null, ex);
            
            retorno.getMensaje().setMensaje(ex.getMessage());
            retorno.getMensaje().setTipoMensaje(Enumerado.TipoMensaje.ERROR);

        }
        return retorno;
    }
}
