/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Dominio.Sitios;
import Entidad.WS_User;
import Enumerado.Constantes;
import Enumerado.ServicioWeb;
import Enumerado.TipoMensaje;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import Utiles.Utilidades;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 *
 * @author alvar
 */
public class Seguridad {
    
    private static Seguridad instancia;

    private Seguridad() {
    }
    
    /**
     * Obtener instancia
     * @return Instancia
     */
    public static Seguridad GetInstancia(){
        if (instancia==null)
        {
            instancia   = new Seguridad();
            
        }

        return instancia;
    }
    
    /**
     * Encriptar con MD5
     * @param input Texto a encriptar
     * @return Texto encriptado
     * @throws NoSuchAlgorithmException
     */
    public String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger number = new BigInteger(1, messageDigest);
        return number.toString(16);
    }

    /**
     * Desencriptar
     * @param encryptedData Dato encriptado
     * @param initialVectorString Vector de encriptacion
     * @param secretKey Semilla de encriptacion
     * @return Texto desencritpado
     */
    public String decrypt(String encryptedData, String initialVectorString, String secretKey) {
        String decryptedData = null;
        try {
            SecretKeySpec skeySpec          = new SecretKeySpec(secretKey.getBytes(), "AES");
            IvParameterSpec initialVector   = new IvParameterSpec(initialVectorString.getBytes());
            Cipher cipher                   = Cipher.getInstance("AES/CFB8/NoPadding");

            cipher.init(Cipher.DECRYPT_MODE, skeySpec, initialVector);

            byte[] encryptedByteArray = (new Base64()).decode(encryptedData.getBytes());
            byte[] decryptedByteArray = cipher.doFinal(encryptedByteArray);

            decryptedData = new String(decryptedByteArray, "UTF8");

        } catch (Exception e) {

            e.printStackTrace();
            //System.err.println("Problem decrypting the data" + e.getMessage());
        }
        return decryptedData;
    }

    /**
     * Encriptar con MD5
     * @param pass Texto a encriptar
     * @return Texto encriptado
     */
    public String cryptWithMD5(String pass){
       MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] passBytes = pass.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Seguridad.class.getName()).log(Level.SEVERE, null, ex);
        }
            return null;


       }
   
    /**
     * Determina si el usuario tiene permiso para el sitio que esta visualizando
     * @param sitioActual Sitio actual
     * @param esAdm Es administrador
     * @param esDoc Es docente 
     * @param esAlu Es alumno
     * @return  Tiene permiso
     */
    private boolean PermisoSitio(String sitioActual, Boolean esAdm, Boolean esDoc, Boolean esAlu)
    {
        if(Sitios.GetInstancia().getLstSinSeguridad().contains(sitioActual)) return true;
            
        if(esAdm) return true;
        
        if(esDoc)
        {
            return Sitios.GetInstancia().getLstDocente().contains(sitioActual);
        }
        
        if(esAlu)
        {
            return Sitios.GetInstancia().getLstAlumno().contains(sitioActual);
        }
        
        return false;
    }
    
    /**
     * Controlar acceso al sitio
     * @param usuario Usuario
     * @param esAdm Es administrador
     * @param esDoc Es docente
     * @param esAlu Es alumno
     * @param sitioActual Sitio actual
     * @return Resultado: RETORNO_MSGOBJ
     */
    public Retorno_MsgObj ControlarAcceso(String usuario, Boolean esAdm, Boolean esDoc, Boolean esAlu, String sitioActual)
    {
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error", TipoMensaje.ERROR));
        
        esAdm = (esAdm == null ? false : esAdm);
        esAlu = (esAlu == null ? false : esAlu);
        esDoc = (esDoc == null ? false : esDoc);

        if(usuario == null )
        {
            if(!Sitios.GetInstancia().getLstSinSeguridad().contains(sitioActual))
            {
                retorno.setObjeto(Utilidades.GetInstancia().GetUrlSistema() + "login.jsp");
                return retorno;
            }
        }

        if (!Seguridad.GetInstancia().PermisoSitio(sitioActual, esAdm, esDoc, esAlu))
        {
            String texto = "No tiene acceso a está página";
            
            texto = Utilidades.GetInstancia().GetUrlSistema() + "Error.jsp?pMensaje=" + Utilidades.GetInstancia().UrlEncode(texto);
            retorno.setObjeto(texto);
            
        }
        else
        {
            retorno.setMensaje(new Mensajes("Ok", TipoMensaje.MENSAJE));
        }
        
        return retorno;

    }
   
    /**
     * Obtener token WebService
     * @param servicioWeb Servicio web
     * @return token
     */
    public String getTokenWS(ServicioWeb servicioWeb){

        String token = "INVALIDO";

        WS_User usr = (WS_User) LoWS.GetInstancia().obtenerByUsrNom(Constantes.WS_USR_WEB.getValor()).getObjeto();
        

        try {
            String wsUsr = this.crypt(Constantes.WS_USR_WEB.getValor());
            String wsPsw = this.crypt(Constantes.WS_PSW_WEB.getValor());

            token = wsUsr + Constantes.SEPARADOR.getValor() + wsPsw;

            token = this.crypt(token);

        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }

        return token;
    }

    /**
     * Encriptar
     * @param text Texto
     * @return Texto encriptado
     * @throws Exception
     */
    public String crypt(String text) throws Exception {

        SecretKeySpec skeySpec          = new SecretKeySpec(Constantes.ENCRYPT_SEMILLA.getValor().getBytes(), "AES");
        IvParameterSpec initialVector   = new IvParameterSpec(Constantes.ENCRYPT_VECTOR_INICIO.getValor().getBytes());

        if(text == null || text.length() == 0)
            throw new Exception("Empty string");

        byte[] encrypted = null;

        try {
            Cipher cipher                   = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, initialVector);

            encrypted = cipher.doFinal(text.getBytes());

            encrypted = (new Base64()).encode(encrypted); 
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e)
        {
            throw new Exception("[encrypt] " + e.getMessage());
        }

        return new String(encrypted, "UTF8");

    }

}
