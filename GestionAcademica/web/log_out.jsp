<%-- 
    Document   : log_out
    Created on : 24-jun-2017, 11:58:10
    Author     : alvar
--%>
<%@page import="Entidad.NotificacionBandeja"%>
<%@page import="Enumerado.BandejaEstado"%>
<%@page import="Enumerado.BandejaTipo"%>
<%@page import="Logica.LoBandeja"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Enumerado.NombreSesiones"%>

<%
    
    String usuarioNombre    = (String) session.getAttribute(NombreSesiones.USUARIO_NOMBRE.getValor());
    Long PerCod             = (Long) session.getAttribute(NombreSesiones.USUARIO_PER.getValor());    

    List<Object> lstBandeja = new ArrayList<>();
    List<Object> lstVistos = new ArrayList<>();
    
    if(PerCod != null)
    {
        Retorno_MsgObj retorno = (Retorno_MsgObj) LoBandeja.GetInstancia().obtenerListaByTipoEstado(PerCod, BandejaTipo.WEB, BandejaEstado.SIN_LEER);
        if(!retorno.SurgioError())
        {
            lstBandeja = retorno.getLstObjetos();
        }
        else
        {
            out.print(retorno.getMensaje().toString());
        }
        
        retorno = (Retorno_MsgObj) LoBandeja.GetInstancia().obtenerListaByTipoEstado(PerCod, BandejaTipo.WEB, BandejaEstado.LEIDA);
        if(!retorno.SurgioError())
        {
            lstVistos = retorno.getLstObjetos();
        }
        else
        {
            out.print(retorno.getMensaje().toString());
        }
    }
    Integer cantidad = lstBandeja.size() + lstVistos.size();

    String urlSistema = (String) session.getAttribute(NombreSesiones.URL_SISTEMA.getValor());

%>

<div  class="col-lg-12" style="text-align: right;">
    <div class="col-lg-10">
        <p>Notificaciones: <label><a href="#" id="btn_ver_bandeja"><%out.print(cantidad);%></a></label></p>
        
        <div name="div_notificaciones" id="div_notificaciones" class="col-lg-2 div_notificaciones" style="display: none;" >
            <div id="div_not_sinleer">
            <%
                for(Object objeto : lstBandeja)
                {
                  NotificacionBandeja bandeja = (NotificacionBandeja) objeto;  
                  out.println("<div class='row'><div data-toggle='modal' data-target='#PopUpMensaje' data-objeto='"+ Utilidades.GetInstancia().ObjetoToJson(bandeja)+"' class='btn_ver_msg not_sinver'><label>"+bandeja.getNotBanFch()+": </label>" + bandeja.getNotBanAsu() + "</div></div>");
                }
            %>
            </div>
            
            <div id="div_not_vistos">
            <%
                for(Object objeto : lstVistos)
                {
                  NotificacionBandeja bandeja = (NotificacionBandeja) objeto;  
                  out.println("<div class='row'><div data-toggle='modal' data-target='#PopUpMensaje' data-objeto='"+ Utilidades.GetInstancia().ObjetoToJson(bandeja)+"' class='btn_ver_msg not_vista'><label>"+bandeja.getNotBanFch()+": </label>" + bandeja.getNotBanAsu() + "</div></div>");
                }
            %>
            </div>
        </div>
        
    </div>
    <div class="col-lg-2">
    <p>
        <label>Bienvenido: <% out.print(usuarioNombre); %></label>
    </p>
    <p>
        <a href="#" id="cerrar_sesion" name="cerrar_sesion">Cerrar sesion</a>
        <a href="<%=urlSistema%>pswChange.jsp" >Cambiar contraseña</a>
    </p>
    </div>
</div>

<script>
                $(document).ready(function() {
                    
                    var urlAct = $('#sga_url').val();
                    
                    MostrarCargando(false);
                    
                        $('#cerrar_sesion').click(function(event) {
                                
                                MostrarCargando(true);
                                
                                // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                                $.post(urlAct + 'Login', {
                                        pAction : "FINALIZAR"
                                }, function(responseText) {
                                        var obj = JSON.parse(responseText);
                                
                                        if(obj.tipoMensaje == 'ERROR')
                                        {
                                            MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                                            MostrarCargando(false);
                                        }
                                        else
                                        {
                                            window.location.replace(urlAct);
                                        }
                                });
                            
                        });
                    
                });
        </script>
   