<%-- 
    Document   : DefCalendarioWW
    Created on : 03-jul-2017, 18:28:52
    Author     : alvar
--%>
<%@page import="Entidad.Notificacion"%>
<%@page import="Logica.LoNotificacion"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Entidad.Calendario"%>
<%@page import="java.util.List"%>
<%@page import="Logica.LoCalendario"%>
<%@page import="Utiles.Utilidades"%>
<%

    Utilidades utilidad         = Utilidades.GetInstancia();
    String urlSistema = utilidad.GetUrlSistema();
    
    //----------------------------------------------------------------------------------------------------
    //CONTROL DE ACCESO
    //----------------------------------------------------------------------------------------------------
    
    String  usuario = (String) session.getAttribute(NombreSesiones.USUARIO.getValor());
    Boolean esAdm   = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ADM.getValor());
    Boolean esAlu   = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ALU.getValor());
    Boolean esDoc   = (Boolean) session.getAttribute(NombreSesiones.USUARIO_DOC.getValor());
    Retorno_MsgObj acceso = Seguridad.GetInstancia().ControlarAcceso(usuario, esAdm, esDoc, esAlu, utilidad.GetPaginaActual(request));
    
    if(acceso.SurgioError()) response.sendRedirect((String) acceso.getObjeto());
            
    //----------------------------------------------------------------------------------------------------
    
    List<Object> lstObjeto = new ArrayList<>();
    
    Retorno_MsgObj retorno = (Retorno_MsgObj) LoNotificacion.GetInstancia().obtenerLista();
    if(!retorno.SurgioError())
    {
        lstObjeto = retorno.getLstObjetos();
    }
    else
    {
        out.print(retorno.getMensaje().toString());
    }
    
    String tblVisible = (lstObjeto.size() > 0 ? "" : "display: none;");

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Notificaciones</title>
        <jsp:include page="/masterPage/head.jsp"/>
        <jsp:include page="/masterPage/head_tables.jsp"/>
    </head>
    <body>
        <jsp:include page="/masterPage/NotificacionError.jsp"/>
        <jsp:include page="/masterPage/cabezal_menu.jsp"/>

        <!-- CONTENIDO -->
        <div class="contenido" id="contenedor">

            <div class="row">
                <div class="col-lg-12">
                    <section class="panel">
                        
                        <header class="panel-heading">
                            <!-- TITULO -->
                            NOTIFICACIONES
                            <!-- BOTONES -->
                            <span class="tools pull-right">
                                <a href="<% out.print(urlSistema); %>Definiciones/DefNotificacion.jsp?MODO=<% out.print(Enumerado.Modo.INSERT); %>" title="Ingresar" class="glyphicon glyphicon-plus"> </a>
                            </span>
                        </header>
                        <div class="panel-body">
                            <div class=" form">
                                <!-- CONTENIDO -->
                                <table id='tbl_ww' style=' <% out.print(tblVisible); %>' class='table table-hover'>
                                <thead>
                                    <tr>
                                        <th></th>
                                        <th></th>
                                        <th>Código</th>
                                        <th>Nombre</th>
                                        <th>Descripción</th>
                                        <th>Tipo</th>
                                        <th>Medio</th>
                                        <th>Activa</th>
                                        <th></th>
                                        <th></th>
                                        <th></th>
                                        <th></th>
                                    </tr>
                                </thead>

                                <% for(Object objeto : lstObjeto)
                                {
                                    Notificacion notificacion = (Notificacion) objeto;
                                %>
                                <tr>
                                    <td><% if(!notificacion.getNotInt()){ %><a href="<% out.print(urlSistema); %>Definiciones/DefNotificacion.jsp?MODO=<% out.print(Enumerado.Modo.DELETE); %>&pNotCod=<% out.print(notificacion.getNotCod()); %>" name="btn_eliminar" id="btn_eliminar" title="Eliminar" class="glyphicon glyphicon-trash"/> <% } %> </td>
                                    <td><a href="<% out.print(urlSistema); %>Definiciones/DefNotificacion.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pNotCod=<% out.print(notificacion.getNotCod()); %>" name="btn_editar" id="btn_editar" title="Editar" class='glyphicon glyphicon-edit'/></td>
                                    <td><% out.print( utilidad.NuloToVacio(notificacion.getNotCod())); %> </td>
                                    <td><% out.print( utilidad.NuloToVacio(notificacion.getNotNom())); %> </td>
                                    <td><% out.print( utilidad.NuloToVacio(notificacion.getNotDsc())); %> </td>
                                    <td><% out.print( utilidad.NuloToVacio(notificacion.getNotTpo().getNombre())); %> </td>
                                    <td><% out.print( utilidad.NuloToVacio(notificacion.getMedio())); %> </td>
                                    <td><% out.print( utilidad.BooleanToSiNo(notificacion.getNotAct())); %> </td>
                                    <td><a href="#" title="Ejecutar" class="glyphicon glyphicon-play btn_ejecutar" data-toggle="modal" data-target="#PopUpEjecutar" data-codigo="<% out.print(notificacion.getNotCod()); %>"> </a></td>
                                    <td><a href="<% out.print(urlSistema); %>Definiciones/DefNotificacionDestinatarioSWW.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pNotCod=<% out.print(notificacion.getNotCod()); %>" title="Destinatarios" class='fa fa-address-book'/></td>
                                    <td><a href="<% out.print(urlSistema); %>Definiciones/DefNotificacionConsultaSWW.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pNotCod=<% out.print(notificacion.getNotCod()); %>" title="Consultas" class='fa fa-database'/></td>
                                    <td><a href="<% out.print(urlSistema); %>Definiciones/DefNotificacionBitacoraSWW.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pNotCod=<% out.print(notificacion.getNotCod()); %>" title="Bitacora" class='fa fa-folder-open'/></td>

                                </tr>
                                <%
                                }
                                %>
                            </table>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>

        <jsp:include page="/masterPage/footer.jsp"/>
        
        
        <!-- PopUp para ejecutar tarea -->
                                
        <div id="PopUpEjecutar" class="modal fade" role="dialog">
           <!-- Modal -->
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Ejecutar</h4>
                  </div>
                  <div class="modal-body">
                        <div>
                            <p>Confirma la ejecución?</p>
                        </div>
                  </div>
                  <div class="modal-footer">
                      <button name="boton_confirmar" id="boton_confirmar" type="button" class="btn btn-success" data-codigo="">Confirmar</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                  </div>
                </div>
            </div>
    
    
    
        <script type="text/javascript">

            $(document).ready(function() {

                $('.btn_ejecutar').on('click', function(e) {
                        
                        var codigo = $(this).data("codigo");
                        
                        $('#boton_confirmar').data('codigo', codigo);
                        
                        
                      });
                      
                      $('#boton_confirmar').on('click', function(e) {
                            var codigo = $('#boton_confirmar').data('codigo');
                            
                            $.post('<% out.print(urlSistema); %>NotificationManager', {
                                         pNotCod: codigo,
                                         pAction: "NOTIFICAR"
                                     }, function (responseText) {
                                         var obj = JSON.parse(responseText);
                                         
                                         $(function () {
                                                $('#PopUpEjecutar').modal('toggle');
                                             });
                                         
                                         MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                                         

                                     });

                             
                     
                      });


            });
            </script>
        </div>
                        
    </body>
</html>
