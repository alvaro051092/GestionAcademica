/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidad.SincRegistroEliminado;
import Enumerado.Objetos;
import Enumerado.Proceso;
import Enumerado.TipoMensaje;
import Logica.LoBitacora;
import Logica.LoParametro;
import Logica.LoSincronizacion;
import SDT.SDT_Parameters;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import Utiles.Utilidades;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Table;
import org.hibernate.Criteria;
import org.hibernate.Query;

/**
 * Persistencia PerManejador
 *
 * @author alvar
 */
public class PerManejador{
    
    private Session sesion;
    private Transaction tx;
    
   private void iniciaOperacion() throws HibernateException {
        try {
            sesion = NewHibernateUtil.getSessionFactory().openSession();
            tx = sesion.beginTransaction();
        } catch (HibernateException ec) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ec);
        }
    }
   
    private Retorno_MsgObj manejaExcepcion(HibernateException he, Retorno_MsgObj retorno) throws HibernateException {
        
        String mensaje;
        
        Throwable cause = he.getCause();
        if (cause instanceof SQLException) {
            switch(((SQLException) cause).getErrorCode())
            {
                case 1451:
                    mensaje = "Existen datos en otros registros";
                    break;
                case 1062:
                    mensaje = "Ya se ingreso el registro";
                    break;
                default: 
                    mensaje = cause.getMessage();
                    break;
            }
        }
        else
        {
            mensaje = he.getMessage();
        }
        
        retorno.setMensaje(new Mensajes("Error: " + mensaje, TipoMensaje.ERROR));
        
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, he);
        
        LoBitacora.GetInstancia().NuevoMensaje(new Mensajes("Error: " + mensaje + "\n" + he.getLocalizedMessage(), TipoMensaje.ERROR), Proceso.SISTEMA);
        
        tx.rollback();
        
        return retorno;
    }

    /**
     *
     * @param pObjeto Recibe un objeto
     * @return Retorna el objeto Retorno_MsgObj con la información que haya retornado el método Guardar
     */
    public Retorno_MsgObj guardar(Object pObjeto) {
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al guardar", TipoMensaje.ERROR), pObjeto);
        
        try {
            iniciaOperacion();
            retorno.setObjeto((Long) sesion.save(pObjeto));
            
            tx.commit();
            
            retorno.setMensaje(new Mensajes("Guardado correctamente", TipoMensaje.MENSAJE));
            
        } catch (HibernateException he) {
            System.err.println("Objeto: " + pObjeto.toString());
            retorno = manejaExcepcion(he, retorno);
            
        } finally {
            sesion.close();
        }
        
        return retorno;
    }
    
    /**
     *
     * @param pObjeto Recibe un objeto
     * @return Retorna el objeto Retorno_MsgObj con la información que haya retornado el método guardarPKManual
     */
    public Retorno_MsgObj guardarPkManual(Object pObjeto) {
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al guardar", TipoMensaje.ERROR), pObjeto);
        
        try {
            iniciaOperacion();
            
            sesion.save(pObjeto);
            tx.commit();
            
            retorno.setMensaje(new Mensajes("Guardado correctamente", TipoMensaje.MENSAJE));
            
        } catch (HibernateException he) {
            
            retorno = manejaExcepcion(he, retorno);
            
        } finally {
            sesion.close();
        }
        
        return retorno;
    }
    
    /**
     *
     * @param pObjeto Recibe un objeto
     * @return Retorna el objeto Retorno_MsgObj con la información que haya retornado el método actualizar
     */
    public Retorno_MsgObj actualizar(Object pObjeto) {
        
        //Notificacion notificacion = (Notificacion) pObjeto;
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al modificar", TipoMensaje.ERROR), pObjeto);

        try {
            iniciaOperacion();
            sesion.update(pObjeto);
            tx.commit();
            
            retorno.setMensaje(new Mensajes("Modificado correctamente", TipoMensaje.MENSAJE));
            
        } catch (HibernateException he) {
            System.err.println("Objeto: " + pObjeto.toString());
            retorno = manejaExcepcion(he, retorno);
            
        } finally {
            sesion.close();
        }
        
        return retorno;
    }
    
    /**
     *
     * @param pObjeto Recibe un objeto
     * @return Retorna el objeto Retorno_MsgObj con la información que haya retornado el método merge
     */
    public Retorno_MsgObj merge(Object pObjeto) {
        
        //Notificacion notificacion = (Notificacion) pObjeto;
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al merge", TipoMensaje.ERROR), pObjeto);

        try {
            iniciaOperacion();
            sesion.merge(pObjeto);
            tx.commit();
            
            retorno.setMensaje(new Mensajes("Modificado merge", TipoMensaje.MENSAJE));
            
        } catch (HibernateException he) {
            System.err.println("Objeto: " + pObjeto.toString());
            retorno = manejaExcepcion(he, retorno);
            
        } finally {
            sesion.close();
        }
        
        return retorno;
    }
    
    /**
     *
     * @param pObjeto Recibe un Objeto
     * @return Retorna el objeto Retorno_MsgObj con la información que haya retornado el método eliminar
     */
    public Retorno_MsgObj eliminar(Object pObjeto) {
        
        //Notificacion notificacion = (Notificacion) pObjeto;

        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al eliminar", TipoMensaje.ERROR), null);

        try {
            iniciaOperacion();
            sesion.delete(pObjeto);
            tx.commit();
            retorno = new Retorno_MsgObj(new Mensajes("Eliminado correctamente", TipoMensaje.MENSAJE), null);
            
        } catch (HibernateException he) {
            
            retorno = manejaExcepcion(he, retorno);
        } finally {
            sesion.close();
        }
        
        if(!retorno.SurgioError())
        {
            this.SincronizarEliminado(pObjeto);
        }
        
        return retorno;
    }

    /**
     *
     * @param pCodigo Recibe un objeto
     * @param clase Recibe la clase
     * @return Retorna el objeto Retorno_MsgObj con la información que haya retornado el método obtener
     */
    public Retorno_MsgObj obtener(Long pCodigo, Class clase) {
        
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al obtener", TipoMensaje.ERROR), null);
        
        try {
            iniciaOperacion();
            
            if(tx.isActive()) tx.commit();
            
            Object objRetorno = sesion.get(clase, pCodigo);
            
            retorno = new Retorno_MsgObj(new Mensajes("Ok", TipoMensaje.MENSAJE), objRetorno);
                 
        } catch (HibernateException he) {
            
            retorno = manejaExcepcion(he, retorno);
            
        } finally {
            sesion.close();
        }

        return retorno;
    }
    
    /**
     *
     * @param namedQuery Recibe la query
     * @param parametros Recibe los parametros
     * @return Retorna el objeto Retorno_MsgObj con la información que haya retornado el método obtenerLista
     */
    public Retorno_MsgObj obtenerLista(String namedQuery, ArrayList<SDT_Parameters> parametros) {

        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al obtener lista", TipoMensaje.ERROR), null);

        try {
            iniciaOperacion();
            
            if(tx.isActive()) tx.commit();
            Query query = sesion.getNamedQuery(namedQuery);
            
            if(parametros != null)
            {
                for(SDT_Parameters parametro : parametros)
                {
                    query.setParameter(parametro.getNombre(), parametro.getObjeto());
                }
            }
            
            List<Object> listaRetorno = query.list();
            
            retorno.setMensaje(new Mensajes("Ok", TipoMensaje.MENSAJE));
            retorno.setLstObjetos(listaRetorno);
            
            
        } catch (HibernateException he) {
            
            retorno = manejaExcepcion(he, retorno);
            
        } finally {
            sesion.close();
        }

        return retorno;
    }
    
    /**
     *
     * @param sentencia Recibe la sentencia
     * @return Retorna el objeto Retorno_MsgObj con la información que haya retornado el método obtenerResultadosQuery
     */
    public Retorno_MsgObj obtenerResultadosQuery(String sentencia) {

        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al obtener lista", TipoMensaje.ERROR), null);

        try {
            iniciaOperacion();
            
            if(tx.isActive()) tx.commit();
            Query query = sesion.createSQLQuery(sentencia);
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            List list = query.list();

            retorno.setMensaje(new Mensajes("Ok", TipoMensaje.MENSAJE));
            retorno.setObjeto(list);
            
        } catch (HibernateException he) {
            
            retorno = manejaExcepcion(he, retorno);
            
        } finally {
            sesion.close();
        }

        return retorno;
    }
    
    /**
     *
     * @param sentencia Recibe la sentencia
     * @return Retorna el objeto Retorno_MsgObj con la información que haya retornado el método ejecutarUpdateQuery
     */
    public Retorno_MsgObj ejecutarUpdateQuery(String sentencia){
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al ejecutar custom query", TipoMensaje.ERROR), null);
        
        try {
            iniciaOperacion();
            
            Query query = sesion.createQuery(sentencia);
            int result = query.executeUpdate();
            
            tx.commit();
            
            retorno.setMensaje(new Mensajes("Objetos afectados: " + result, TipoMensaje.MENSAJE));
            
        } catch (HibernateException he) {
            
            retorno = manejaExcepcion(he, retorno);
            
        } finally {
            sesion.close();
        }

        return retorno;
        
    }
    
    /**
     *
     * @param sentencia Recibe la sentencia
     * @return Retorna el objeto Retorno_MsgObj con la información que haya retornado el método ejecutarQuery
     */
    public Retorno_MsgObj ejecutarQuery(String sentencia){
        Retorno_MsgObj retorno = new Retorno_MsgObj(new Mensajes("Error al ejecutar custom query", TipoMensaje.ERROR), null);
        
        try {
            iniciaOperacion();
            
            Query query = sesion.createSQLQuery(sentencia);
            int result = query.executeUpdate();
            
            tx.commit();
            
            retorno.setMensaje(new Mensajes("Objetos afectados: " + result, TipoMensaje.MENSAJE));
            
        } catch (HibernateException he) {
            retorno = manejaExcepcion(he, retorno);
            
        } finally {
            sesion.close();
        }

        return retorno;
        
    }
    
    /**
     *
     * @param objeto recibe el objeto
     * @return Retorna el nombre de la tabla del objeto
     */
    public String GetTableNameFromObject(Object objeto){
        Table table = objeto.getClass().getAnnotation(Table.class);
        return table.name();
    }
    
    
    private void SincronizarEliminado(Object objeto){
        String tabla = this.GetTableNameFromObject(objeto);
        
        if(LoParametro.GetInstancia().obtener().getParSncAct())
        {
            if(Objetos.contains(tabla))
            {
                //DEBE GUARDAR LA SINCRONIZACION.
                SincRegistroEliminado elim = new SincRegistroEliminado();
                
                elim.setObjElimCod(Utilidades.GetInstancia().ObtenerPrimaryKey(objeto));
                elim.setSncObjElimFch(new Date());
                elim.setObjeto(LoSincronizacion.GetInstancia().ObjetoObtenerByNombre(tabla));
                this.guardar(elim);
            }
        }
    }
    
}