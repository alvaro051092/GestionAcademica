<%-- 
    Document   : DefSincronizacionWW
    Created on : 18-ago-2017, 10:05:16
    Author     : alvar
--%>
<%@page import="Entidad.Sincronizacion"%>
<%@page import="Logica.LoSincronizacion"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Utiles.Utilidades"%>
<%
    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema = utilidad.GetUrlSistema();

    //----------------------------------------------------------------------------------------------------
    //CONTROL DE ACCESO
    //----------------------------------------------------------------------------------------------------
   
    
    String usuario = (String) session.getAttribute(NombreSesiones.USUARIO.getValor());
    Boolean esAdm = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ADM.getValor());
    Boolean esAlu = (Boolean) session.getAttribute(NombreSesiones.USUARIO_ALU.getValor());
    Boolean esDoc = (Boolean) session.getAttribute(NombreSesiones.USUARIO_DOC.getValor());
    Retorno_MsgObj acceso = Seguridad.GetInstancia().ControlarAcceso(usuario, esAdm, esDoc, esAlu, utilidad.GetPaginaActual(request));

    if (acceso.SurgioError()) {
        response.sendRedirect((String) acceso.getObjeto());
    }

    //----------------------------------------------------------------------------------------------------
    
    List<Object> lstObjeto = new ArrayList<>();

    Retorno_MsgObj retorno = (Retorno_MsgObj) LoSincronizacion.GetInstancia().obtenerLista();
    if(!retorno.SurgioError()) {
        if(!retorno.SurgioErrorListaRequerida()) 
        {
            lstObjeto = retorno.getLstObjetos();
        }
    } else {
        out.print(retorno.getMensaje().toString());
    }

    String tblVisible = (lstObjeto.size() > 0 ? "" : "display: none;");

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Sincronización</title>
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
                            BITÁCORA DE SINCRONIZACION
                            <!-- BOTONES -->
                            <span class="tools pull-right">
                                <a href="#" title="Ejecutar" class="glyphicon glyphicon-play" id="ejecutar"> </a>
                                <a href="#" title="Depurar" class="glyphicon glyphicon-trash" data-toggle="modal" data-target="#PopUpDepurar"> </a>
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
                                            <th>Fecha</th>
                                            <th>Duración</th>
                                            <th>Estado</th>
                                            <th>Registros afectados</th>
                                            <th>Detalle</th>
                                        </tr>
                                    </thead>

                                    <% for (Object objeto : lstObjeto) {
                                            Sincronizacion sinc = (Sincronizacion) objeto;
                                    %>
                                    <tr>
                                        <td><a href="#" data-id="<%=sinc.getSncCod()%>" name="btn_eliminar" id="btn_eliminar" class="glyphicon glyphicon-trash btn_eliminar"></a></td>
                                        <td><a href="<% out.print(urlSistema); %>Administracion/DefSincIncSWW.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pSncCod=<% out.print(sinc.getSncCod()); %>" name="btn_editar" id="btn_editar" class="glyphicon glyphicon-edit"></a></td>
                                        <td><% out.print(utilidad.NuloToVacio(sinc.getSncCod())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(sinc.getSncFch())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(sinc.getSncDur())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(sinc.getSncEst())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(sinc.getSncObjCnt())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(sinc.getSncObjDet())); %> </td>

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
        
        
        <!-- PopUp para depurar -->

        <div id="PopUpDepurar" class="modal fade" role="dialog">
            <!-- Modal -->
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Depurar</h4>
                    </div>
                    <div class="modal-body">
                        <div>
                            <p>Confirma la eliminación de todos los registros?</p>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button name="boton_confirmar" id="boton_confirmar" class="btn btn-danger" data-codigo="">Confirmar</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    </div>
                </div>
            </div>
            
            <script type="text/javascript">

                $(document).ready(function () {
                    
                    $('#boton_confirmar').click(function (event) {

                            // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                            $.post('<% out.print(urlSistema); %>ABM_Sincronizacion', {
                                pAction: 'DEPURAR'
                            }, function (responseText) {
                                var obj = JSON.parse(responseText);

                                if (obj.tipoMensaje != 'ERROR')
                                {
                                    location.reload();
                                } else
                                {
                                    MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                                }

                            });

                        
                    });
                    
                    $('.btn_eliminar').click(function (event) {


                        var SncCod = $(this).data('id');
                        
                        $.post('<% out.print(urlSistema); %>ABM_Sincronizacion', {
                                pSncCod: SncCod,
                                pAction: 'DELETE'
                            }, function (responseText) {
                                var obj = JSON.parse(responseText);

                                if (obj.tipoMensaje != 'ERROR')
                                {
                                    location.reload();
                                } else
                                {
                                    MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                                }

                            });

                        
                    });
                    
                    $('#ejecutar').click(function (event) {


                    
                            // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                            $.post('<% out.print(urlSistema); %>ABM_Sincronizacion', {
                                pAction: 'EJECUTAR'
                            }, function (responseText) {
                                var obj = JSON.parse(responseText);

                                if (obj.tipoMensaje != 'ERROR')
                                {
                                    location.reload();
                                } else
                                {
                                    MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                                }

                            });

                        
                    });

                });
            </script>
        </div>
        
    </body>
</html>
