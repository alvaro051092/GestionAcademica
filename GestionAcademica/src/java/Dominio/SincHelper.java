/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dominio;

import Enumerado.Objetos;
import Enumerado.TipoMensaje;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import Utiles.Utilidades;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.apache.tomcat.util.codec.binary.Base64;
import org.codehaus.jackson.annotate.JsonIgnore;

//@JsonIgnoreProperties({"insertQuery", "updateQuery"})

/**
 * Clase abstracta que se usa para ayudar en la sincronización
 * 
 * @author alvar
 */
public abstract class SincHelper{
    
    private final SimpleDateFormat dtFrmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private final SimpleDateFormat dMy = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat yMd_HMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     *
     * @return
     */
    public String GetNamePrimaryKey(){
        for (Field field : this.getClass().getDeclaredFields()) 
        {
        
            if(fieldIsPrimaryKey(field))
            {
                return field.getName();
            }
        }

        return null;
    }
    
    /**
     *
     * @return
     */
    public Long GetPrimaryKey(){
        for (Field field : this.getClass().getDeclaredFields()) 
        {
        
            if(fieldIsPrimaryKey(field))
            {
                if(field.getType().equals(Long.class))
                {
                    try {
                        field.setAccessible(true);
                        if(field.get(this) != null)
                        {
                            return (Long) field.get(this);
                        }
                    } 
                    catch (IllegalArgumentException | IllegalAccessException ex) {
                            Logger.getLogger(SincHelper.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return null;
    }
    
    /**
     *
     * @return
     */
    @JsonIgnore
    public String getInsertQuery(){
        String insert = "INSERT INTO " + Objetos.fromQueryName(this.getClass().getSimpleName().toUpperCase()).name() + "(";
        
        String columns  = "";
        String values   = "";
        for (Field field : this.getClass().getDeclaredFields()) 
        {
        
            if(fieldIsColumn(field) || fieldIsJoinColumn(field))
            {
                //-------------------------------------------------------------
                //Nombre del campo
                //-------------------------------------------------------------
                String name = (this.fieldIsJoinColumn(field) ? this.fieldJoinColumnName(field) : field.getName());
                
                columns += (columns.isEmpty() ? name :  ", " + name);
               
                //-------------------------------------------------------------
                //Valor del campo
                //-------------------------------------------------------------
                
                values += (values.isEmpty() ? getParsedValue(field) :  ", " + getParsedValue(field));
 
            }
       }
        
        
        insert += columns +") VALUES(" + values + ")";
        
        System.err.println("Query: " + insert);
        return insert;
    }
    
    /**
     *
     * @return
     */
    @JsonIgnore
    public String getUpdateQuery(){
        String update = "UPDATE " + Objetos.fromQueryName(this.getClass().getSimpleName().toUpperCase()).getNamedQuery() + " SET ";
        
        
        
        String sets   = "";

        for (Field field : this.getClass().getDeclaredFields()) 
        {
        
            if((fieldIsColumn(field) || fieldIsJoinColumn(field) ) && !fieldIsPrimaryKey(field))
            {
                String name = (this.fieldIsJoinColumn(field) ? this.fieldJoinColumnName(field) : field.getName());
                
                sets += (sets.isEmpty() ? name + " = " + this.getParsedValue(field) : ", " + name + " = " + this.getParsedValue(field));
                
            }
       }
        
        
        update += sets + " WHERE " + this.GetNamePrimaryKey() + " = " + this.GetPrimaryKey();
        
        System.err.println("Query: " + update);
        
        return update;
    }
    
    /**
     * Funcion para setear campos de manera dinamica. 
     * Recibe campo y valor, realiza la validacion del tipo de dato, y si 
     * existe un campo con el mismo nombre se lo setea al objeto.
     * 
     * @param fldName Nombre del campo
     * @param fldValue Valor del campo
     * @return Retorna el resultado y un mensaje
     */
    @JsonIgnore
    public Retorno_MsgObj setField(String fldName, String fldValue){
       Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Set field success", TipoMensaje.MENSAJE));
       
       for (Field field : this.getClass().getDeclaredFields()) 
        {
            if(field.getName().equals(fldName))
            {
                try {
                    
                    field.setAccessible(true);
                    //field.set(this, field.getType().cast(fldValue));
                    
                    if(field.getType().equals(String.class))
                    {
                        field.set(this, fldValue);
                    }

                    if(field.getType().equals(Date.class))
                    {
                        try
                        {
                            field.set(this, dMy.parse(fldValue));
                        }
                        catch(ParseException ex)
                        {
                            field.set(this, yMd_HMS.parse(fldValue));
                        }
                    }
                    
                    if(field.getType().equals(Boolean.class))
                    {
                        field.set(this, Boolean.valueOf(fldValue));
                    }
                    
                    if(field.getType().equals(Integer.class))
                    {
                        
                        field.set(this, Integer.parseInt(fldValue));
                    }
                    
                    if(field.getType().equals(Long.class))
                    {
                        field.set(this, Long.valueOf(fldValue));
                    }
                    
                    if(field.getType().equals(Double.class))
                    {
                        field.set(this, Double.valueOf(fldValue));
                    }
                    
                    if(field.getType() instanceof Class && ((Class<?>)field.getType()).isEnum())
                    {
                        //Enum en = (Enum).valueOf(field.getType(), fldValue);
                        
                        //Class<?> clase = field.getType().getClass();

                        //Object objeto = Enum.valueOf((Class<Enum>) clase, fldValue);
                        
                        Enum<?> enumerado = Enum.valueOf((Class<Enum>) field.getType(), fldValue);
                        
                        field.set(this, enumerado);
                        
                    }

                    
                    
                } catch (IllegalArgumentException | IllegalAccessException | ClassCastException ex) {
                    
                    retorno.setMensaje(new Mensajes("Set field failed: \n" + ex, TipoMensaje.ERROR));
                    
                    Logger.getLogger(SincHelper.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(SincHelper.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       }
       
       return retorno;
    }
    
    /**
     *
     *
     */
    private Boolean fieldIsColumn(Field campo){
        Annotation[] lstAnot = campo.getAnnotations();
        for(Annotation anot : lstAnot)
        {
           if(anot.annotationType().equals(Column.class)){
               return true;
           }
        }

        return false;
        
    }

    /**
     *
     *
     */
    private Boolean fieldIsJoinColumn(Field campo){
        Annotation[] lstAnot = campo.getAnnotations();
        
        for(Annotation anot : lstAnot)
        {
           if(anot.annotationType().equals(ManyToOne.class) || anot.annotationType().equals(OneToOne.class)){
               return true;
           }
        }

        return false;
    }
    
    /**
     *
     *
     */
    private String fieldJoinColumnName(Field campo){
        
        JoinColumn j = campo.getAnnotation(JoinColumn.class);
        
        return j.name();
        
    }
        
    /**
     *
     *
     */
    private Boolean fieldIsPrimaryKey(Field campo){
        Annotation[] lstAnot = campo.getAnnotations();
        for(Annotation anot : lstAnot)
        {
           if(anot.annotationType().equals(Id.class)){
               return true;
           }
        }

        return false;
        
    }
    
    /**
     *
     *
     */
    private String getParsedValue(Field campo){
        String value = null;

        campo.setAccessible(true);

        try{
            if(campo.get(this) != null)
            {
                
                if(fieldIsJoinColumn(campo))
                {
                    value = Utilidades.GetInstancia().ObtenerPrimaryKey(campo.get(this)).toString();
                }
                else
                {
                    
                    if(campo.getType().isArray())
                    {
                        if(campo.getType().getComponentType().equals(Byte.TYPE))
                        {
                            value = "FROM_BASE64('" + new String(Base64.encodeBase64((byte[]) campo.get(this)), StandardCharsets.UTF_8) + "')";
                        }
                    }

                    if(campo.getType().equals(String.class))
                    {
                        value = "'" + campo.get(this) + "'";
                    }

                    if(campo.getType().equals(Date.class))
                    {
                        value = "'" + dtFrmt.format((Date) campo.get(this)) + "'";
                    }
                    
                    if(campo.getType() instanceof Class && ((Class<?>)campo.getType()).isEnum())
                    {
                        Enum en = (Enum) campo.get(this);
                        
                        value = en.ordinal() + "";
                    }
                        
                    if(value == null) value = campo.get(this).toString();
                    
                }
            }
        }
        catch(IllegalAccessException ex)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        
        return value;
    }
    
    /**
     *
     * @param fromObjeto
     */
    public void CastFromObject(Object fromObjeto){
        Object toObject = this;
        
        try{
            
            for (Field field : this.getClass().getDeclaredFields()) 
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
        
        //return toObject;
        
    }
}
