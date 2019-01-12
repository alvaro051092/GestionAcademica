<%-- 
    Document   : DefSolicitudWW
    Created on : 26-jul-2017, 16:59:06
    Author     : alvar
--%>

<%@page import="Enumerado.Modo"%>
<%@page import="Enumerado.EstadoSolicitud"%>
<%@page import="Entidad.Solicitud"%>
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
    List<Object> lstObjeto = new ArrayList<>();

    Retorno_MsgObj retorno = (Retorno_MsgObj) loSolicitud.obtenerLista();
    if (!retorno.SurgioErrorListaRequerida()) {
        lstObjeto = retorno.getLstObjetos();
    } else {
        out.print(retorno.getMensaje().toString());
    }

    List<Solicitud> lstTomadas = new ArrayList<>();
    List<Solicitud> lstSinTomar = new ArrayList<>();
    List<Solicitud> lstFinalizadas = new ArrayList<>();

    for (Object objeto : lstObjeto) {
        Solicitud solicitud = (Solicitud) objeto;

        if (solicitud.getSolEst().equals(EstadoSolicitud.FINALIZADA)) {
            lstFinalizadas.add(solicitud);
        }
        if (solicitud.getSolEst().equals(EstadoSolicitud.SIN_TOMAR)) {
            lstSinTomar.add(solicitud);
        }
        if (solicitud.getSolEst().equals(EstadoSolicitud.TOMADA)) {
            lstTomadas.add(solicitud);
        }
    }

    String tblFinVisible = (lstFinalizadas.size() > 0 ? "" : "display: none;");
    String tblSinTomVisible = (lstSinTomar.size() > 0 ? "" : "display: none;");
    String tblTomVisible = (lstTomadas.size() > 0 ? "" : "display: none;");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Solicitudes</title>
        <jsp:include page="/masterPage/head.jsp"/>
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
                        </header>
                        <div class="panel-body">
                            <div class=" form">
                                    <!-- CONTENIDO -->
                                    <div name="sin_tomar">

                                        <h2 style=' <% out.print(tblSinTomVisible); %>'>Nuevas solicitudes</h2>

                                        <table  style=' <% out.print(tblSinTomVisible); %>' class='table table-hover'>
                                            <thead>
                                                <tr>
                                                    <th></th>
                                                    <th>Código</th>
                                                    <th>Alumno</th>
                                                    <th>Tipo</th>
                                                    <th>Ingresada</th>
                                                    <th></th>
                                                </tr>
                                            </thead>

                                            <% for (Solicitud solicitud : lstSinTomar) {
                                            %>
                                            <tr>
                                                <td><% out.print("<a href='#' data-codigo='" + solicitud.getSolCod() + "' data-nombre='" + solicitud.getSolTpo().getNombre() + "' data-alumno='" + solicitud.getAlumno().getNombreCompleto() + "' data-toggle='modal' data-target='#PopUpEliminar' name='btn_eliminar' id='btn_eliminar' title='Eliminar' class='glyphicon glyphicon-trash btn_eliminar'/>"); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio(solicitud.getSolCod())); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio(solicitud.getAlumno().getNombreCompleto())); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio(solicitud.getSolTpo().getNombre())); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio(solicitud.getSolFchIng())); %> </td>
                                                <td><% out.print("<a href='#' data-codigo='" + solicitud.getSolCod() + "' data-nombre='" + solicitud.getSolTpo().getNombre() + "' data-alumno='" + solicitud.getAlumno().getNombreCompleto() + "' data-toggle='modal' data-target='#PopUpProceso' name='btn_Tomar' id='btn_Tomar' title='Tomar' class='glyphicon glyphicon-plus-sign btn_Tomar'/>"); %> </td>

                                            </tr>
                                            <%
                                                }
                                            %>
                                        </table>
                                    </div>

                                    <div name="tomadas">
                                        <h2 style=' <% out.print(tblTomVisible); %>'>Solicitudes en proceso</h2>

                                        <table style=' <% out.print(tblTomVisible); %>' class='table table-hover'>
                                            <thead>
                                                <tr>
                                                    <th></th>
                                                    <th>Código</th>
                                                    <th>Alumno</th>
                                                    <th>Tipo</th>
                                                    <th>Funcionario responsable</th>
                                                    <th>Ingresada</th>
                                                    <th>Procesada</th>
                                                    <th></th>
                                                    <th></th>
                                                </tr>
                                            </thead>

                                            <% for (Solicitud solicitud : lstTomadas) {
                                            %>
                                            <tr>
                                                <td><% out.print("<a href='#' data-codigo='" + solicitud.getSolCod() + "' data-nombre='" + solicitud.getSolTpo().getNombre() + "' data-alumno='" + solicitud.getAlumno().getNombreCompleto() + "' data-toggle='modal' data-target='#PopUpEliminar' name='btn_eliminar' id='btn_eliminar' title='Eliminar' class='glyphicon glyphicon-trash btn_eliminar'/>"); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio(solicitud.getSolCod())); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio(solicitud.getAlumno().getNombreCompleto())); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio(solicitud.getSolTpo().getNombre())); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio((solicitud.getFuncionario() != null ? solicitud.getFuncionario().getNombreCompleto() : ""))); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio(solicitud.getSolFchIng())); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio(solicitud.getSolFchPrc())); %> </td>

                                                <td><% out.print("<a href='#' data-codigo='" + solicitud.getSolCod() + "' data-nombre='" + solicitud.getSolTpo().getNombre() + "' data-alumno='" + solicitud.getAlumno().getNombreCompleto() + "' data-toggle='modal' data-target='#PopUpProceso' name='btn_Liberar' id='btn_Liberar' title='Liberar' class='glyphicon glyphicon-minus-sign btn_Liberar'/>"); %> </td>
                                                <td><% out.print("<a href='#' data-codigo='" + solicitud.getSolCod() + "' data-nombre='" + solicitud.getSolTpo().getNombre() + "' data-alumno='" + solicitud.getAlumno().getNombreCompleto() + "' data-toggle='modal' data-target='#PopUpProceso' name='btn_Finalizar' id='btn_Finalizar' title='Finalizar' class='glyphicon glyphicon-ok-sign btn_Finalizar'/>"); %> </td>

                                            </tr>
                                            <%
                                                }
                                            %>
                                        </table>
                                    </div>

                                    <div name="finalizadas">
                                        <h2 style=' <% out.print(tblFinVisible); %>'>Solicitudes finalizadas</h2>

                                        <table style=' <% out.print(tblFinVisible); %>' class='table table-hover'>
                                            <thead>
                                                <tr>
                                                    <th></th>
                                                    <th>Código</th>
                                                    <th>Alumno</th>
                                                    <th>Tipo</th>
                                                    <th>Funcionario responsable</th>
                                                    <th>Ingresada</th>
                                                    <th>Procesada</th>
                                                    <th>Finalizada</th>
                                                </tr>
                                            </thead>

                                            <% for (Solicitud solicitud : lstFinalizadas) {
                                            %>
                                            <tr>
                                                <td><% out.print("<a href='#' data-codigo='" + solicitud.getSolCod() + "' data-nombre='" + solicitud.getSolTpo().getNombre() + "' data-alumno='" + solicitud.getAlumno().getNombreCompleto() + "' data-toggle='modal' data-target='#PopUpEliminar' name='btn_eliminar' id='btn_eliminar' title='Eliminar' class='glyphicon glyphicon-trash btn_eliminar'/>"); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio(solicitud.getSolCod())); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio(solicitud.getAlumno().getNombreCompleto())); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio(solicitud.getSolTpo().getNombre())); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio((solicitud.getFuncionario() != null ? solicitud.getFuncionario().getNombreCompleto() : ""))); %> </td>
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

                        <p>Eliminar la solicitud de <label name="elim_nombre" id="elim_nombre"></label> del alumno: <label name="elim_alumno" id="elim_alumno"></label></p>
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
                        var alumno = $(this).data("nombre");

                        $('#elim_nombre').text(nombre);
                        $('#elim_alumno').text(alumno);
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

        <!-- PopUp para Procesar -->

        <div id="PopUpProceso"  class="modal fade" role="dialog">

            <!-- Modal -->
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title"><span id="proc_titulo" name="proc_titulo"></span></h4>
                    </div>
                    <div class="modal-body">
                        <p><span id="proc_contenido" name="proc_contenido"></span> <label name="proc_nombre" id="proc_nombre"></label> del alumno: <label name="proc_alumno" id="proc_alumno"></label></p>
                    </div>
                    <div class="modal-footer">
                        <button name="proc_boton_confirmar" id="proc_boton_confirmar" type="button" class="btn btn-success" data-codigo="">Confirmar</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    </div>
                </div>
            </div>
            <script type="text/javascript">
                $(document).ready(function () {

                    $('.btn_Tomar').on('click', function (e) {

                        var codigo = $(this).data("codigo");
                        var nombre = $(this).data("nombre");
                        var alumno = $(this).data("alumno");

                        $('#proc_nombre').text(nombre);
                        $('#proc_alumno').text(alumno);
                        $('#proc_boton_confirmar').data('codigo', codigo);
                        $('#proc_boton_confirmar').data('tipoAccion', "TOMAR");

                        $('#proc_titulo').text("Tomar solicitud");
                        $('#proc_contenido').text("Desea tomar la solicitud de ");


                    });

                    $('.btn_Liberar').on('click', function (e) {

                        var codigo = $(this).data("codigo");
                        var nombre = $(this).data("nombre");
                        var alumno = $(this).data("alumno");

                        $('#proc_nombre').text(nombre);
                        $('#proc_alumno').text(alumno);
                        $('#proc_boton_confirmar').data('codigo', codigo);
                        $('#proc_boton_confirmar').data('tipoAccion', "LIBERAR");

                        $('#proc_titulo').text("Liberar solicitud");
                        $('#proc_contenido').text("Desea liberar la solicitud de ");

                    });

                    $('.btn_Finalizar').on('click', function (e) {

                        var codigo = $(this).data("codigo");
                        var nombre = $(this).data("nombre");
                        var alumno = $(this).data("alumno");

                        $('#proc_nombre').text(nombre);
                        $('#proc_alumno').text(alumno);
                        $('#proc_boton_confirmar').data('codigo', codigo);
                        $('#proc_boton_confirmar').data('tipoAccion', "FINALIZAR");

                        $('#proc_titulo').text("Finalizar solicitud");
                        $('#proc_contenido').text("Desea finalizar la solicitud de ");



                    });

                    $('#proc_boton_confirmar').on('click', function (e) {
                        var codigo = $('#proc_boton_confirmar').data('codigo');
                        var accion = $('#proc_boton_confirmar').data('tipoAccion');

                        $.post('<% out.print(urlSistema);%>ABM_Solicitud', {
                            pSolCod: codigo,
                            pAction: accion
                        }, function (responseText) {
                            var obj = JSON.parse(responseText);

                            $(function () {
                                $('#PopUpProceso').modal('toggle');
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

