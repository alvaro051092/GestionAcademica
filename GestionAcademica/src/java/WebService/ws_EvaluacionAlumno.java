/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import Entidad.Calendario;
import Entidad.CalendarioAlumno;
import Entidad.Persona;
import Enumerado.EstadoServicio;
import Enumerado.ServicioWeb;
import Enumerado.TipoMensaje;
import Logica.LoCalendario;
import Logica.LoPersona;
import Logica.LoWS;
import Utiles.Mensajes;
import Utiles.Retorno_MsgObj;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

/**
 * Servicio Evaluaciones
 *
 * @author aa
 */
@WebService(serviceName = "ws_EvaluacionAlumno")
public class ws_EvaluacionAlumno {
    
    @Resource WebServiceContext context;

    /**
     * 
     * @param AluPerCod Recibo el código de una persona
     * @param AlIns parametro (SI/NO, para devolver evaluaciones a las que esté inscripto el alumno)
     * @return : Devuelve una lista para determinado alumno con evaluaciones que se encuentran pendientes para inscribirse o para borrarse
     */
    @WebMethod(operationName = "EvaluacionesParaInscripcion")
    public Retorno_MsgObj EvaluacionesParaInscripcion(@WebParam(name = "AluPerCod") Long AluPerCod, @WebParam(name = "AlIns") String AlIns)
    {
        Retorno_MsgObj retorno = this.isAuthenticated();
        
        LoCalendario loCalendario = LoCalendario.GetInstancia();
        Retorno_MsgObj retCal = new Retorno_MsgObj();
        Calendario cal = new Calendario();
        List<Object> lstObjeto = new ArrayList<>();
        List<Object> lstCalendario = new ArrayList<>();

        if(!retorno.SurgioError())
        {
            if(AluPerCod == null)
            {    
                retorno.setMensaje(new Mensajes("No se recibió un parámetro Alumno", TipoMensaje.ERROR));
            }
            else
            {
                if(AlIns == null)
                {
                    retorno.setMensaje(new Mensajes("No se recibió un parámetro AlIns", TipoMensaje.ERROR));
                }
                else
                {
                    //Lista de Calendarios donde el alumno NO esta inscripto
                    if(AlIns.equals("SI"))
                    {
                        retCal  = (Retorno_MsgObj) loCalendario.ObtenerListaParaInscripcion(AluPerCod); 
                        
                        if (!retCal.SurgioError()) 
                        {
                            retorno.setMensaje(new Mensajes("OK", TipoMensaje.MENSAJE));
                            lstObjeto = retCal.getLstObjetos();
                            for(Object obj : lstObjeto)
                            {
                                cal = (Calendario) obj;
                                if(cal.existeAlumno(AluPerCod) == false)
                                {
                                    lstCalendario.add(cal);
                                }
                            }
                            retorno.setLstObjetos(lstCalendario);
                        }
                        else
                        {
                            retorno.setMensaje(new Mensajes("Surgió error en lista Requerida", TipoMensaje.ERROR));
                        }
                    }
                    //lista de Calendarios donde el alumno SI esta inscripto
                    else if(AlIns.equals("NO"))
                    {
                        retCal  = (Retorno_MsgObj) loCalendario.ObtenerListaParaInscripcion(AluPerCod); 
                        
                        if (!retCal.SurgioError())
                        {
                            retorno.setMensaje(new Mensajes("OK", TipoMensaje.MENSAJE));
                            lstObjeto = retCal.getLstObjetos();
                            for(Object obj : lstObjeto)
                            {
                                cal = (Calendario) obj;
                                if(cal.existeAlumno(AluPerCod) == true)
                                {
                                    lstCalendario.add(cal);
                                }
                            }
                            retorno.setLstObjetos(lstCalendario);
                        }
                        else
                        {
                            retorno.setMensaje(new Mensajes("Surgió error en lista Requerida", TipoMensaje.ERROR));
                        }
                    }
                    else
                    {
                        retorno.setMensaje(new Mensajes("ERROR, el valor de AlIns es incorrecto", TipoMensaje.ERROR));
                    }
                }
            }
        }
        return retorno;
    }
    
    /**
     *
     * @param UsuAlumno Recibo el código de una persona
     * @return : Metodo que devuelve la lista de evaluaciones de determinado alumno, que fueron finalizadas.
     */
    @WebMethod(operationName = "EvaluacionesFinalizadas")
    public Retorno_MsgObj EvaluacionesFinalizadas(@WebParam(name = "UsuAlumno") Long UsuAlumno)
    {
        Retorno_MsgObj retorno = this.isAuthenticated();
        
        LoCalendario loCalendario = LoCalendario.GetInstancia();

        if(!retorno.SurgioError())
        {
            if(UsuAlumno == null)
            {    
                retorno.setMensaje(new Mensajes("No se recibió un parámetro", TipoMensaje.ERROR));
            }
            else
            {
                retorno = (Retorno_MsgObj) loCalendario.ObtenerListaPorAlumno(UsuAlumno);
            }
        }
        return retorno;
    }
    
    /**
     *
     * @param UsuAlumno Recibo el código de una persona
     * @return : Metodo que devuelve una lista de las evaluaciones que el alumno se encuentra inscripto pero estan pendientes para rendír la preuba
     */
    @WebMethod(operationName = "ListaPendiente")
    public Retorno_MsgObj ListaPendiente(@WebParam(name = "UsuAlumno") Long UsuAlumno)
    {
        Retorno_MsgObj retorno = this.isAuthenticated();
        
        LoCalendario loCalendario = LoCalendario.GetInstancia();

        if(!retorno.SurgioError())
        {
            if(UsuAlumno == null)
            {    
                retorno.setMensaje(new Mensajes("No se recibió un parámetro", TipoMensaje.ERROR));
            }
            else
            {
                retorno = (Retorno_MsgObj) loCalendario.ObtenerListaPendiente(UsuAlumno);
                retorno.setMensaje(new Mensajes("OK", TipoMensaje.MENSAJE));
            }
        }
        return retorno;
    }

    /**
     *
     * @param AluPerCod Recibo el código de una persona
     * @param CalCod Recibe el código de calendario
     * @return : Metodo que Inscribe un alumno a la evaluacion
     */
    @WebMethod(operationName = "InscribirAlumno")
    public Retorno_MsgObj InscribirAlumno(@WebParam(name = "AluPerCod") Long AluPerCod, @WebParam(name = "CalCod") Long CalCod)
    {
        Retorno_MsgObj retorno      = this.isAuthenticated();
        
        LoCalendario loCalendario   = LoCalendario.GetInstancia();
        LoPersona loPersona         = LoPersona.GetInstancia();

        if(!retorno.SurgioError())
        {
            if(AluPerCod == null)
            {    
                retorno.setMensaje(new Mensajes("No se recibió el parámetro Alumno", TipoMensaje.ERROR));
            }
            else
            {
                if(CalCod == null)
                {
                    retorno.setMensaje(new Mensajes("No se recibió el parámetro Calendario", TipoMensaje.ERROR));
                }
                else
                {
                    CalendarioAlumno CalAlumno  = new CalendarioAlumno();
                    
                    CalAlumno.setCalendario((Calendario)loCalendario.obtener(CalCod).getObjeto());
                    CalAlumno.setAlumno((Persona)loPersona.obtener(AluPerCod).getObjeto());
                    
                    if(CalAlumno.getCalendario() != null)
                    {
                        if(CalAlumno.getAlumno() != null)
                        {
                            CalAlumno.setEvlCalFch(new java.util.Date());
                            
                            retorno = (Retorno_MsgObj) loCalendario.AlumnoAgregar(CalAlumno);
                            retorno.setMensaje(new Mensajes("OK", TipoMensaje.MENSAJE));
                        }
                        else
                        {
                            retorno.setMensaje(new Mensajes("No existe el Alumno", TipoMensaje.ERROR));
                        }
                    }
                    else
                    {
                        retorno.setMensaje(new Mensajes("No existe el Calendario", TipoMensaje.ERROR));
                    }
                }
            }
        }
        return retorno;
    }
    
    /**
     *
     * @param CalAlCod Recibo el código de una persona
     * @param CalCod Recibe el código de calendario
     * @return : Metodo que Borra un alumno de la evaluación a la que está inscripto
     */
    @WebMethod(operationName = "DesinscribirAlumno")
    public Retorno_MsgObj DesinscribirAlumno(@WebParam(name = "PerCod") Long PerCod, @WebParam(name = "CalCod") Long CalCod)
    {
        Retorno_MsgObj retorno      = this.isAuthenticated();
        
        LoCalendario loCalendario   = LoCalendario.GetInstancia();

        if(!retorno.SurgioError())
        {
            if(PerCod == null)
            {    
                retorno.setMensaje(new Mensajes("No se recibió el parámetro Alumno", TipoMensaje.ERROR));
            }
            else
            {
                if(CalCod == null)
                {
                    retorno.setMensaje(new Mensajes("No se recibió el parámetro Calendario", TipoMensaje.ERROR));
                }
                else
                {
                    Calendario cal = (Calendario)loCalendario.obtener(CalCod).getObjeto();

                    if(cal != null)
                    {
                        CalendarioAlumno CalAlumno = cal.getAlumnoByPersona(PerCod);
                        if(CalAlumno != null)
                        {
                            CalAlumno.setEvlCalFch(new java.util.Date());
                            
                            retorno = (Retorno_MsgObj) loCalendario.AlumnoEliminar(CalAlumno);
                            retorno.setMensaje(new Mensajes("Eliminado Correctamente", TipoMensaje.MENSAJE));
                        }
                        else
                        {
                            retorno.setMensaje(new Mensajes("No existe el Alumno", TipoMensaje.ERROR));
                        }
                    }
                    else
                    {
                        retorno.setMensaje(new Mensajes("No existe el Calendario", TipoMensaje.ERROR));
                    }
                }
            }
        }
        return retorno;
    }
    
    private Retorno_MsgObj isAuthenticated() {
        
        Retorno_MsgObj retorno  = new Retorno_MsgObj(new Mensajes("Autenticando", TipoMensaje.ERROR));

        MessageContext messageContext = context.getMessageContext();
        HttpServletRequest request = (HttpServletRequest) messageContext.get(MessageContext.SERVLET_REQUEST);
        Map httpHeaders = (Map) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
        
        String direccion           = "IP: "+request.getRemoteAddr()+", Port: "+request.getRemotePort()+", Host: "+request.getRemoteHost();

        List tknList = (List) httpHeaders.get("token");
        
        if (tknList != null)
        {
            if(tknList.size() > 0)
            {
                String token = (String) tknList.get(0);
                
                if(token == null)
                {
                    retorno.setMensaje(new Mensajes("No se recibió token", TipoMensaje.ERROR));
                    LoWS.GetInstancia().GuardarMensajeBitacora(null, direccion + "\n Token invalido", EstadoServicio.CON_ERRORES, ServicioWeb.EVALUACION_ALUMNO);
                }
                else
                {
                    if(!LoWS.GetInstancia().ValidarConsumo(token, ServicioWeb.EVALUACION_ALUMNO, direccion))
                    {
                        retorno.setMensaje(new Mensajes("Token invalido, no se puede consumir el servicio", TipoMensaje.ERROR));
                    }
                    else
                    {
                        retorno.setMensaje(new Mensajes("Token valido, puede consumir el servicio", TipoMensaje.MENSAJE));                        
                    }
                }
            }
        }
        else
        {
           retorno.setMensaje(new Mensajes("No se recibió token", TipoMensaje.ERROR));
           LoWS.GetInstancia().GuardarMensajeBitacora(null, direccion + "\n Token invalido", EstadoServicio.CON_ERRORES, ServicioWeb.EVALUACION_ALUMNO); 
        }


        return retorno;

    }
    
}
