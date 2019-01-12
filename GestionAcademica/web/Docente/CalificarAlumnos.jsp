<%-- 
    Document   : CalificarAlumnos
    Created on : jul 27, 2017, 8:29:09 p.m.
    Author     : aa
--%>

<%@page import="Enumerado.Modo"%>
<%@page import="Entidad.Calendario"%>
<%@page import="Entidad.CalendarioAlumno"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.LoCalendario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%

    LoCalendario loCalendario = LoCalendario.GetInstancia();
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
    String CalCod = request.getParameter("pCalCod");

    List<CalendarioAlumno> lstObjeto = new ArrayList<>();

    Retorno_MsgObj retorno = (Retorno_MsgObj) loCalendario.obtener(Long.valueOf(CalCod));
    if (!retorno.SurgioErrorObjetoRequerido()) {
        lstObjeto = loCalendario.AlumnoObtenerListaPorUsuario((Calendario) retorno.getObjeto(), usuario);
    } else {
        out.print(retorno.getMensaje().toString());
    }

    String tblVisible = (lstObjeto.size() > 0 ? "" : "display: none;");


%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Calendario | Alumnos</title>
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
                            ALUMNOS
                            <span class="tools pull-right">
                                <div class="hidden-xs">
                                    <a href="<% out.print(urlSistema); %>Docente/EvalPendientes.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pCalCod=<% out.print(CalCod); %>">Regresar</a>
                                </div>
                            </span>
                        </header>

                        <div class=""> 
                            <input type="hidden" name="CalCod" id="CalCod" value="<% out.print(CalCod); %>">
                        </div>

                        <div class="panel-body">
                            <div class=" form">
                                <table class='table table-hover tabla_responsive' style=' <% out.print(tblVisible); %>'>
                                    <thead>
                                        <tr>
                                            <th>Código</th>
                                            <th>Alumno</th>
                                            <th>Calificación</th>
                                            <th>Fecha</th>
                                            <th>Estado</th>
                                            <th></th>
                                            <th></th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <% for (CalendarioAlumno calAlumno : lstObjeto) {

                                        %>
                                        <tr>
                                            <td><% out.print(utilidad.NuloToVacio(calAlumno.getCalAlCod())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio((calAlumno.getAlumno() != null ? calAlumno.getAlumno().getNombreCompleto() : ""))); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calAlumno.getEvlCalVal())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calAlumno.getEvlCalFch())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calAlumno.getEvlCalEst().getEstadoNombre())); %> </td>
                                            <td><% if (calAlumno.puedeCalificarse()) {
                                                    out.print("<a href='#' data-codigo='" + calAlumno.getCalAlCod() + "' data-toggle='modal' data-target='#PopUpCalificarAlumno' name='btn_calificar' id='btn_calificar' title='Calificar' class='glyphicon glyphicon-edit btn_calificar'/>");
                                                } %> </td>
                                            <td><% if (calAlumno.puedeEnviarToValidar()) {
                                                    out.print("<a href='#' data-codigo='" + calAlumno.getCalAlCod() + "' data-toggle='modal' data-target='#PopUpEnviarValidacion' name='btn_toVal' id='btn_toVal' title='Enviar a validación' class='glyphicon glyphicon-log-out btn_toVal'/>");
                                                } %> </td>

                                        </tr>
                                        <%
                                            }
                                        %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>
        <jsp:include page="/masterPage/footer.jsp"/>
        
        <!-- PopUp para Calificar Persona -->

        <div id="PopUpCalificarAlumno"  class="modal fade" role="dialog">

            <!-- Modal -->
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Alumno</h4>
                    </div>
                    <div class="modal-body">

                        <p>Nombre: <label name="cal_nombre" id="cal_nombre"></label></p>
                        <p>Documento: <label name="cal_documento" id="cal_documento"></label></p>
                        <p>Calificación: <input type="text" class="form-control" id="cal_EvlCalVal" name="cal_EvlCalVal" placeholder="Calificación"  value="" ></p>
                        <p>Observaciones: <textarea type="text" row="5" class="form-control" id="cal_EvlCalObs" name="cal_EvlCalObs" placeholder="Observaciones"  value="" ></textarea></p>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-success" name="cal_boton_confirmar" id="cal_boton_confirmar" data-codigo="">Confirmar</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    </div>
                </div>
            </div>
            <script type="text/javascript">
                $(document).ready(function ()
                {

                    $('.btn_calificar').on('click', function (e)
                    {
                        var codigo = $(this).data("codigo");
                        var CalCod = $('#CalCod').val();

                        $('#cal_boton_confirmar').data('codigo', codigo);

                        $.post('<% out.print(urlSistema); %>ABM_CalendarioAlumno',
                                {
                                    pCalCod: CalCod,
                                    pCalAlCod: codigo,
                                    pAction: "OBTENER"
                                }
                        , function (responseText)
                        {
                            var alumno = JSON.parse(responseText);

                            $('#cal_nombre').text(alumno.alumno.nombreCompleto);
                            $('#cal_documento').text(alumno.alumno.perDoc);
                            $('#cal_EvlCalVal').val(alumno.evlCalVal);
                            $('#cal_EvlCalObs').val(alumno.evlCalObs);
                        });

                    });

                    $('#cal_boton_confirmar').on('click', function (e)
                    {
                        var codigo = $(this).data("codigo");
                        var CalCod = $('#CalCod').val();
                        var calificacion = $('#cal_EvlCalVal').val();
                        var observaciones = $('#cal_EvlCalObs').val();

                        $.post('<% out.print(urlSistema); %>ABM_CalendarioAlumno',
                                {
                                    pCalCod: CalCod,
                                    pCalAlCod: codigo,
                                    pEvlCalVal: calificacion,
                                    pEvlCalObs: observaciones,
                                    pAction: "<% out.print(Modo.UPDATE); %>"
                                }
                        , function (responseText)
                        {
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

        <!------------------------------------------------->

        <!-- PopUp TO VALIDACION -->

        <div id="PopUpEnviarValidacion"  class="modal fade" role="dialog">

            <!-- Modal -->
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Alumno</h4>
                    </div>
                    <div class="modal-body">
                        <p>Desea enviar a validación el siguiente alumno?</p>
                        <p>Nombre: <label name="toVal_nombre" id="toVal_nombre"></label></p>
                        <p>Calificación: <label id="toVal_EvlCalVal" name="toVal_EvlCalVal" ></label></p>
                        <p>Observaciones: <label id="toVal_EvlCalObs" name="toVal_EvlCalObs" ></label></p>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-success" name="toVal_boton_confirmar" id="toVal_boton_confirmar" data-codigo="">Confirmar</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    </div>
                </div>
            </div>
            <script type="text/javascript">
                $(document).ready(function () {

                    $('.btn_toVal').on('click', function (e) {

                        var codigo = $(this).data("codigo");
                        var CalCod = $('#CalCod').val();

                        $('#toVal_boton_confirmar').data('codigo', codigo);

                        $.post('<% out.print(urlSistema); %>ABM_CalendarioAlumno', {
                            pCalCod: CalCod,
                            pCalAlCod: codigo,
                            pAction: "OBTENER"
                        }, function (responseText) {
                            var alumno = JSON.parse(responseText);

                            $('#toVal_nombre').text(alumno.alumno.nombreCompleto);
                            $('#toVal_EvlCalVal').text(alumno.evlCalVal);
                            $('#toVal_EvlCalObs').text(alumno.evlCalObs);

                        });

                    });

                    $('#toVal_boton_confirmar').on('click', function (e) {
                        var codigo = $(this).data("codigo");
                        var CalCod = $('#CalCod').val();

                        $.post('<% out.print(urlSistema);%>ABM_CalendarioAlumno', {
                            pCalCod: CalCod,
                            pCalAlCod: codigo,
                            pAction: "TO_VALIDAR"
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