/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utiles;

import Entidad.Parametro;
import Enumerado.Constantes;
import Enumerado.ExpresionesRegulares;
import Enumerado.Extensiones;
import Enumerado.Proceso;
import Enumerado.RutaArchivos;
import Enumerado.TipoDato;
import Enumerado.TipoMensaje;
import Logica.LoBitacora;
import Logica.LoParametro;
import Logica.LoSincronizacion;
import Logica.LoVersion;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Utilidades Utilidades
 *
 * @author alvar
 */
public class Utilidades {

    private static Utilidades instancia;
    private final LoParametro loParam;
    private final LoVersion loVersion;

    private Utilidades() {
        loParam = LoParametro.GetInstancia();
        loVersion = LoVersion.GetInstancia();

    }

    /**
     *
     * @return Retorna la instancia
     */
    public static Utilidades GetInstancia() {
        if (instancia == null) {
            instancia = new Utilidades();
        }

        return instancia;
    }

    /**
     * Muestra los mensajes en consola
     *
     * @param TAG
     * @param Msg
     */
    public void MostrarMensajeConsola(String TAG, String Msg) {
        System.err.println(TAG + " ---> " + Msg);
    }

    /**
     * Mostrar Mensajes de error en consola
     *
     * @param mensaje
     */
    public void MostrarMensaje(Mensajes mensaje) {
        System.err.println(mensaje.getTipoMensaje() + ": " + mensaje.getMensaje());
    }

    /**
     * 
     * 
     * @param objeto Recibe un objeto
     * @return Devuelve un objeto JSON
     */
    public String ObjetoToJson(Object objeto) {
        String retorno = "";
        ObjectMapper mapper = new ObjectMapper();

        try {
            // convert user object to json string and return it 
            retorno = mapper.writeValueAsString(objeto);
        } // catch various errors
        catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retorno;
    }

    /**
     * 
     * @param jsonValue Recibe un Objeto JSON
     * @param unObj Recibe un Objeto
     * @return Retorna un Objeto
     */
    public Object JsonToObject(String jsonValue, Object unObj) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            
            // convert user object to json string and return it 
            return mapper.readValue(jsonValue, unObj.getClass());
        } // catch various errors
        catch (JsonGenerationException e) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     *
     * @param jsonValue Recibe un Objeto JSON
     * @param clase Recibe una clase
     * @return Retorna una lista de Objeto
     */
    public List<Object> JsonToListObject(String jsonValue, Class clase) {
        List<Object> lstObjeto = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            // convert user object to json string and return it 
            //return mapper.readValue(listaCala, Object.class);
            lstObjeto = mapper.readValue(jsonValue, mapper.getTypeFactory().constructCollectionType(List.class, clase));
        } // catch various errors
        catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lstObjeto;
    }
    
    /**
     *
     * @return Retprna la URL del Sistema
     */
    public String GetUrlSistema() {
        Parametro param = loParam.obtener();
        if (param != null) {
            return param.getParUrlSis();
        }

        return "";
    }

    /**
     *
     * @param tipoDato Recibe un tupo de dato
     * @param valor Recibe un valor
     * @return Valida el tipo de Dato (Booleano o Entero)
     */
    public Boolean ValidarTipoDato(TipoDato tipoDato, String valor) {

        boolean resultado = false;

        switch (tipoDato) {
            case BOOLEAN:
                resultado = valor.matches(ExpresionesRegulares.BOOLEAN.getValor());
                break;

            case NUMERO_ENTERO:
                resultado = valor.matches(ExpresionesRegulares.NUMERO_ENTERO.getValor());
                break;
        }

        return resultado;
    }

    /**
     *
     * @param objeto Recibe un objeto
     * @return Retorna si es null o vacío (Textos)
     */
    public Object NuloToVacio(Object objeto) {
        return (objeto == null ? "" : objeto);
    }

    /**
     *
     * @param objeto Recibe un objeto
     * @return Retorna si es null o cero (Numericos)
     */
    public Object NuloToCero(Object objeto) {
        return (objeto == null ? "0" : objeto);
    }

    /**
     *
     * @param objeto Recibe un objeto booleano
     * @return Retorna un valor booleano
     */
    public Object BooleanToChecked(Boolean objeto) {
        return (objeto == null ? "" : (objeto == true ? "checked" : ""));
    }

    /**
     *
     * @param objeto Recibe un vlor (Si o No)
     * @return Retorna valor (SI o NO)
     */
    public Object BooleanToSiNo(Boolean objeto) {
        return (objeto == null ? "No" : (objeto == true ? "Si" : "No"));
    }

    /**
     *
     * @param request Recibe un valor request
     * @return Retorna la página actual
     */
    public String GetPaginaActual(HttpServletRequest request) {
        return this.GetPaginaActual(request.getRequestURL().toString());
    }
    
    /**
     *
     * @param url Recibe la url
     * @return Retorna la página actual
     */
    public String GetPaginaActual(String url) {
 
        if (!url.isEmpty()) {
            url = url.substring(url.lastIndexOf("/") + 1, url.length());
        }
        
        if(url.indexOf(".jsp")> 0)
        {
            url = url.substring(0, url.indexOf(".jsp") + 4);
        }
        return url;
    }

    /**
     *
     * @param texto Recibe un texto
     * @return Retorna el encoding de la URL
     */
    public String UrlEncode(String texto) {
        String encodedUrl = null;

        try {
            encodedUrl = URLEncoder.encode(texto, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            // Can be safely ignored because UTF-8 is always supported
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        return encodedUrl;
    }

    /**
     *
     * @param contenido Recibe contenido HTML
     * @return Retorna el contenido sin el tag HTML
     */
    public String QuitarTagHTML(String contenido) {
        contenido = contenido.replaceAll("\\<.*?>", "");

        return contenido;
    }

    /**
     *
     * @param texto Recibe una fecha como texto
     * @return Retorna la fecha convertida en dato tipo DateTime
     */
    public Date StringToDateTime(String texto) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        Date fecha = null;
        try {
            fecha = dateFormat.parse(texto);
        } catch (ParseException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fecha;
    }

    /**
     *
     * @param texto Recibe una fecha como texto
     * @return Retorna la fecha convertida en dato tipo Date
     */
    public Date StringToDate(String texto) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date fecha = null;
        try {
            fecha = dateFormat.parse(texto);
        } catch (ParseException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fecha;

    }
    
    /**
     *
     * @param nombre Recibe un nombre
     * @return Retorna un objeto dado el nombre recibido
     */
    public Object GetObjectByName(String nombre){
        Object objeto = null;
        
        try {
            Class<?> cls = Class.forName(nombre);
            objeto = cls.getConstructor().newInstance();
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return objeto;
    }
    
    /**
     *
     * @param registro Recibe un Objeto que extiende de SyncHelp
     * @return Retorna Retorna el código que tiene en la base de datos
     */
    public Long ObtenerPrimaryKey(Object registro){
        
        Long pk = null;
        try {

            Method metodo   = registro.getClass().getMethod(Constantes.METODO_GETPK.getValor());
            pk = (Long) metodo.invoke(registro);

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(LoSincronizacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return pk;
        
    }
    
    /**
     *
     * @param registro Recibe un Objeto que extiende de SyncHelper
     * @return Retorna una sentencia insert con los objetos recibidos
     */
    public String ObtenerInsertQuery(Object registro){
        
        String query = null;
        try {

            Method metodo   = registro.getClass().getMethod(Constantes.METODO_GETINSQ.getValor());
            query = (String) metodo.invoke(registro);

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(LoSincronizacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return query;
        
    }
    
    /**
     *
     * @param registro Recibe un objeto que extiende de SyncHelper
     * @return Retorna una sentencia Update con los objetos recibidos
     */
    public String ObtenerUpdateQuery(Object registro){
        
        String query = null;
        try {

            Method metodo   = registro.getClass().getMethod(Constantes.METODO_GETUPDQ.getValor());
            query = (String) metodo.invoke(registro);

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(LoSincronizacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return query;
        
    }

    /**
     *
     * @param fromObjeto Recibe un objeto al que quiero castear
     * @param toObject Recibe el objeto que castea
     * @return Retorna fromObjeto casteado con toObject
     */
    public Object CastFromObject(Object fromObjeto, Object toObject){
        
        try{

            for (Field field : toObject.getClass().getDeclaredFields()) 
            {
                field.setAccessible(true);
                
                for (Field fld : fromObjeto.getClass().getDeclaredFields()) 
                {
                    fld.setAccessible(true);

                    if(field.getType().equals(fld.getType()) && field.getName().equals(fld.getName()))
                    {
                        field.set(toObject, fld.get(fromObjeto));
                    }
                }                
            }
        }
        catch(SecurityException | IllegalArgumentException | IllegalAccessException ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        return toObject;
        
    }
   
    /**
     *
     * @return Retorna la ruta de la App
     */
    public String getAppPath(){
        try {
            String path = this.getClass().getClassLoader().getResource("").getPath();
            String fullPath = URLDecoder.decode(path, "UTF-8");
            String pathArr[] = fullPath.split("/WEB-INF/classes/");
            fullPath = pathArr[0];
            
            return fullPath;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     *
     * @return Retorna la ruta de la carpeta Private Temp Storage
     */
    public String getPrivateTempStorage(){
        String path = this.getAppPath();
        path += RutaArchivos.CARPETA_PRIVADA.getRuta();
        
        return path;
    }
    
    /**
     *
     * @return Retorna la ruta de la carpeta Public Temp Storage
     */
    public String getPublicTempStorage(){
        String path = this.getAppPath();
        path += RutaArchivos.CARPETA_PUBLICA.getRuta();
        
        return path;
    }
    
    /**
     *
     * @param ruta Recibe una ruta de archivo
     * @return Elimina el archivo
     */
    public Retorno_MsgObj eliminarArchivo(String ruta){

        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Eliminando archivo: " + ruta, TipoMensaje.MENSAJE));
        
        try{
    		File file = new File(ruta);

                if(file.exists())
                {
                    if(file.delete()){
                            retorno.setMensaje(new Mensajes("Eliminación correcta: " + ruta, TipoMensaje.MENSAJE));
                    }else{
                            retorno.setMensaje(new Mensajes("Eliminación fallida: " + ruta, TipoMensaje.ERROR));
                    }
                }
                else
                {
                    retorno.setMensaje(new Mensajes("Archivo no existe: " + ruta, TipoMensaje.ERROR));
                }

    	}catch(Exception e){

    		retorno.setMensaje(new Mensajes("Error al eliminar el archivo: " + ruta + "\n" + e, TipoMensaje.ERROR));

    	}
        
        return retorno;
    }
    
    /**
     *
     * @param longitud Recibe una longitud
     * @return Retorna un token
     */
    public String GenerarToken(Integer longitud){
        String psw = "";
        
        long milis = new java.util.GregorianCalendar().getTimeInMillis();
        Random r = new Random(milis);
        int i = 0;
        while ( i < longitud){
            char c = (char)r.nextInt(255);
            if ( (c >= '0' && c <='9') || (c >='A' && c <='Z') ){
                psw += c;
                i ++;
            }
        }
        
        return psw;
    }
    
    /**
     *
     * @param nombre Recibe un nombre de archivo (Formatos (doc, xls, jpg))
     * @return Retorna si la extención del archivo es válida
     */
    public Boolean ArchivoExtValida(String nombre){
        Collection<String> lstExt = new ArrayList<>();
        
        for(Extensiones ex : Extensiones.values())
        {
            lstExt.add(ex.getValor());
        }

        return FilenameUtils.isExtension(nombre, lstExt);
    }
    
    /**
     *
     * @param size Recibe un tamaño
     * @return Retorna si el tamaño del archivo es válido
     */
    public Boolean ArchivoSizeValida(Long size){
        
        size = size / 1024;
        return size <= Long.valueOf(Constantes.SIZE_FILE.getValor());
    }
    
    /**
     *
     * @param url Recibe la URL
     * @return Retorna si la conexión es valida o no
     */
    public Boolean ConexionValida(String url){
        
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return true;
            }
        } catch (MalformedURLException | ConnectException  ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        
        System.err.println("Url no disponible: " + url);
        LoBitacora.GetInstancia().NuevoMensaje(new Mensajes("Url no disponible: " + url, TipoMensaje.ERROR), Proceso.SISTEMA);
        return false;
    }
}
