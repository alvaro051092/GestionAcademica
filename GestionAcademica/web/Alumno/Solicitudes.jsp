<%-- 
    Document   : Solicitudes
    Created on : 26-jul-2017, 16:58:46
    Author     : alvar
--%>

<%@page import="Enumerado.TipoSolicitud"%>
<%@page import="Enumerado.Modo"%>
<%@page import="Enumerado.EstadoSolicitud"%>
<%@page import="Entidad.Solicitud"%>
<%@page import="Entidad.Persona"%>
<%@page import="Logica.LoPersona"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.LoSolicitud"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    LoSolicitud loSolicitud = LoSolicitud.GetInstancia();
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
    Persona persona = (Persona) LoPersona.GetInstancia().obtenerByMdlUsr(usuario).getObjeto();

    List<Object> lstObjeto = new ArrayList<>();

    Retorno_MsgObj retorno = (Retorno_MsgObj) loSolicitud.obtenerListaByAlumno(persona.getPerCod());

    if (!retorno.SurgioErrorListaRequerida()) {
        lstObjeto = retorno.getLstObjetos();
    } else {
        out.print(retorno.getMensaje().toString());
    }

    String tblVisible = (lstObjeto.size() > 0 ? "" : "display: none;");

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Solicitudes</title>
        <jsp:include page="/masterPage/head.jsp"/>
        
        <script src="<%=request.getContextPath()%>/JavaScript/DataTable/extensions/Responsive/js/dataTables.responsive.min.js"></script>
        <link href="<%=request.getContextPath()%>/JavaScript/DataTable/extensions/Responsive/css/responsive.dataTables.min.css" rel="stylesheet" type="text/css"/>
        
        <script>
            $('.tabla_responsive').hide();
            MostrarCargando(true);

            $(document).ready(function() {



                    //---------------------------------------------------------
                    //TABLA CON REPORTES
                    //---------------------------------------------------------
                    $('.tabla_responsive').DataTable({
                        "responsive": true,
                        "processing": true,
                        deferRender: true,
                        "lengthMenu": [ [10, 20, 50, -1], [10, 20, 50, "Todos"] ],
                        pageLength: 20,
                        "fnInitComplete": function(oSettings, json) {
                                MostrarCargando(false);
                                $('.tabla_responsive').show();
                              },
                        "ordering": false,
                        dom: '',
                        "language": {
                                "url": "<%=request.getContextPath()%>/JavaScript/DataTable/lang/spanish.json"
                            }
                    });

            } );
        </script>
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
                            SOLICITUDES
                            
                            <span class="tools pull-right">
                                <a href="#" title="Ingresar" class="glyphicon glyphicon-plus" data-toggle="modal" data-target="#PopUpAgregar"> </a>
                                <input type="hidden" name="PerCod" id="PerCod" value="<% out.print(persona.getPerCod()); %>">
                            </span>
                        </header>
                        <div class="panel-body">
                            <div class=" form">
                                <table style=' <% out.print(tblVisible); %>' class='table table-hover tabla_responsive'>
                                    <thead>
                                        <tr>
                                            <th></th>
                                            <th>Código</th>
                                            <th>Alumno</th>
                                            <th>Tipo</th>
                                            <th>Estado</th>
                                            <th>Ingresada</th>
                                            <th>Procesada</th>
                                            <th>Finalizada</th>
                                        </tr>
                                    </thead>

                                    <% for (Object objeto : lstObjeto) {

                                            Solicitud solicitud = (Solicitud) objeto;

                                    %>
                                    <tr>
                                        <td><% out.print("<a href='#' data-codigo='" + solicitud.getSolCod() + "' data-nombre='" + solicitud.getSolTpo().getNombre() + "' data-alumno='" + solicitud.getAlumno().getNombreCompleto() + "' data-toggle='modal' data-target='#PopUpEliminar' name='btn_eliminar' id='btn_eliminar' title='Eliminar' class='glyphicon glyphicon-trash btn_eliminar'/>"); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(solicitud.getSolCod())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(solicitud.getAlumno().getNombreCompleto())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(solicitud.getSolTpo().getNombre())); %> </td>
                                        <td class="<%  out.print(RetornaClase(solicitud.getSolEst())); %>"><% out.print(utilidad.NuloToVacio(solicitud.getSolEst().getNombre())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(solicitud.getSolFchIng())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(solicitud.getSolFchPrc())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(solicitud.getSolFchFin())); %> </td>

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
        
        
       


    <!-- PopUp para Eliminar -->

    <div id="PopUpEliminar"  class="modal fade" role="dialog">

        <!-- Modal -->
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Eliminar</h4>
                </div>
                <div class="modal-body">

                    <p>Eliminar la solicitud de <label name="elim_nombre" id="elim_nombre"></label></p>
                    <p>Quiere proceder?</p>

                </div>
                <div class="modal-footer">
                    <button name="elim_boton_confirmar" id="elim_boton_confirmar" type="button" class="btn btn-danger" data-codigo="">Eliminar</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            $(document).ready(function () {

                $('.btn_eliminar').on('click', function (e) {

                    var codigo = $(this).data("codigo");
                    var nombre = $(this).data("nombre");

                    $('#elim_nombre').text(nombre);
                    $('#elim_boton_confirmar').data('codigo', codigo);


                });

                $('#elim_boton_confirmar').on('click', function (e) {
                    var codigo = $('#elim_boton_confirmar').data('codigo');
                    $.post('<% out.print(urlSistema); %>ABM_Solicitud', {
                        pSolCod: codigo,
                        pAction: "<% out.print(Modo.DELETE);%>"
                    }, function (responseText) {
                        var obj = JSON.parse(responseText);

                        $(function () {
                            $('#PopUpEliminar').modal('toggle');
                        });

                        MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                        if (obj.tipoMensaje != 'ERROR')
                        {
                            location.reload();
                        }

                    });



                });

            });
        </script>
    </div>

    <!-- PopUp para Agregar -->

    <div id="PopUpAgregar"  class="modal fade" role="dialog">

        <!-- Modal -->
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Agregar</h4>
                </div>
                <div class="modal-body">

                    <p>Que desea solicitar?</p>

                    <select class="form-control" id="SolTpo" name="SolTpo">
                        <%
                            for (TipoSolicitud tpoSolicitud : TipoSolicitud.values()) {
                                out.println("<option value='" + tpoSolicitud.getTipoSolicitud() + "'>" + tpoSolicitud.getNombre() + "</option>");
                            }
                        %>
                    </select>

                </div>
                <div class="modal-footer">
                    <button name="agregar_boton_confirmar" id="agregar_boton_confirmar" type="button" class="btn btn-success" data-codigo="">Solicitar</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            $(document).ready(function () {

                $('#agregar_boton_confirmar').on('click', function (e) {

                    var codigo = $('select[name=SolTpo]').val();

                    $.post('<% out.print(urlSistema); %>ABM_Solicitud', {
                        pSolTpo: codigo,
                        pAction: "<% out.print(Modo.INSERT);%>"
                    }, function (responseText) {
                        var obj = JSON.parse(responseText);

                        $(function () {
                            $('#PopUpAgregar').modal('toggle');
                        });

                        MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                        if (obj.tipoMensaje != 'ERROR')
                        {
                            location.reload();
                        }

                    });



                });

            });
        </script>
    </div>
</body>
</html>

<%!
    private String RetornaClase(EstadoSolicitud estado) {
        switch (estado) {
            case FINALIZADA:
                return "alert alert-success";
            case SIN_TOMAR:
                return "alert alert-danger";
            case TOMADA:
                return "alert alert-warning";

        }
        return "";
    }

%>