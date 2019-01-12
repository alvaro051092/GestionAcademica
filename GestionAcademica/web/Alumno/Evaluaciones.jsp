<%-- 
    Document   : Evaluaciones
    Created on : 26-jul-2017, 16:38:15
    Author     : alvar
--%>

<%@page import="Enumerado.IconClass"%>
<%@page import="Enumerado.Modo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="Entidad.Calendario"%>
<%@page import="Entidad.Persona"%>
<%@page import="Logica.LoPersona"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.LoCalendario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    Date fechaActual = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    Persona persona = (Persona) LoPersona.GetInstancia().obtenerByMdlUsr(usuario).getObjeto();

    List<Object> lstObjeto = new ArrayList<>();

    Retorno_MsgObj retorno = (Retorno_MsgObj) loCalendario.ObtenerListaParaInscripcion(persona.getPerCod());
    if (!retorno.SurgioError()) {
        lstObjeto = retorno.getLstObjetos();
    } else {
        out.print(retorno.getMensaje().toString());
    }

    String tblVisible = (lstObjeto.size() > 0 ? "" : "display: none;");

    List<Object> lstEvl = new ArrayList<>();

    Retorno_MsgObj retornoEvl = (Retorno_MsgObj) loCalendario.ObtenerListaPorAlumno(persona.getPerCod());
    if (!retornoEvl.SurgioError()) {
        lstEvl = retornoEvl.getLstObjetos();
    } else {
        out.print(retornoEvl.getMensaje().toString());
    }

    String tblEvlVisible = (lstEvl.size() > 0 ? "" : "display: none;");

    List<Object> lstEvlPend = new ArrayList<>();

    Retorno_MsgObj retornoEvlPend = (Retorno_MsgObj) loCalendario.ObtenerListaPendiente(persona.getPerCod());
    if (!retornoEvlPend.SurgioError()) {
        lstEvlPend = retornoEvlPend.getLstObjetos();
    } else {
        out.print(retornoEvlPend.getMensaje().toString());
    }

    String tblEvlPendVisible = (lstEvlPend.size() > 0 ? "" : "display: none;");

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Evaluaciones</title>
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
                            EVALUACIONES
                        </header>
                        <div class="panel-body">
                            <div class=" form">
                                <input type="hidden" name="PerCod" id="PerCod" value="<% out.print(persona.getPerCod()); %>">
                                
                                <div name="InscripcionDisponible">
                                    <h2 style=' <% out.print(tblVisible); %>'>Inscripciones</h2>
                                    <table style=' <% out.print(tblVisible); %>' class='table table-hover tabla_responsive'>
                                        <thead>
                                            <tr>
                                                <th>Carrera / Curso</th>
                                                <th>Estudio</th>
                                                <th>Evaluación</th>
                                                <th>Fecha</th>
                                                <th>Inscripción desde</th>
                                                <th>Inscripcion hasta</th>
                                                <th></th>
                                            </tr>
                                        </thead>

                                        <% for (Object objeto : lstObjeto) {
                                                Calendario calendario = (Calendario) objeto;
                                        %>
                                        <tr>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getEvaluacion().getCarreraCursoNombre())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getEvaluacion().getEstudioNombre())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getEvaluacion().getEvlNom())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getCalFch())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getEvlInsFchDsd())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getEvlInsFchHst())); %> </td>
                                            <td><%
                                                if ((calendario.getEvlInsFchDsd().before(fechaActual) && calendario.getEvlInsFchHst().after(fechaActual)) || sdf.format(calendario.getEvlInsFchDsd()).equals(sdf.format(fechaActual))) {
                                                    if (calendario.existeAlumno(persona.getPerCod())) {
                                                        out.print("<a href='#' data-codigo='" + calendario.getCalCod() + "' data-persona='" + calendario.getAlumnoByPersona(persona.getPerCod()).getCalAlCod() + "' data-nombre='" + calendario.getEvaluacion().getEstudioNombre() + "' data-toggle='modal' data-target='#PopUpDesInsc' name='btn_desInscribirAlumno' id='btn_desInscribirAlumno' title='Salir' class='glyphicon glyphicon-remove-circle btn_desInscribirAlumno'/>");
                                                    } else {
                                                        out.print("<a href='#' data-codigo='" + calendario.getCalCod() + "' data-persona='" + calendario.getAlumnoByPersona(persona.getPerCod()).getCalAlCod() + "' data-nombre='" + calendario.getEvaluacion().getEstudioNombre() + "' data-toggle='modal' data-target='#PopUpInsc' name='btn_inscribirAlumno' id='btn_inscribirAlumno' title='Inscribir' class='glyphicon glyphicon-ok-circle btn_inscribirAlumno'/>");
                                                    }
                                                }
                                                %>
                                            </td>

                                        </tr>
                                        <%
                                            }
                                        %>
                                    </table>
                                </div>

                                <div name="EvaluacionPendiente">
                                    <h2 style=' <% out.print(tblEvlPendVisible); %>'>Pendientes</h2>
                                    <table style=' <% out.print(tblEvlPendVisible); %>' class='table table-hover tabla_responsive'>
                                        <thead>
                                            <tr>
                                                <th>Carrera / Curso</th>
                                                <th>Estudio</th>
                                                <th>Evaluación</th>
                                                <th>Fecha</th>
                                                <th>Inscripción desde</th>
                                                <th>Inscripcion hasta</th>
                                            </tr>
                                        </thead>

                                        <% for (Object objeto : lstEvlPend) {
                                                Calendario calendario = (Calendario) objeto;
                                        %>
                                        <tr>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getEvaluacion().getCarreraCursoNombre())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getEvaluacion().getEstudioNombre())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getEvaluacion().getEvlNom())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getCalFch())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getEvlInsFchDsd())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getEvlInsFchHst())); %> </td>

                                        </tr>
                                        <%
                                            }
                                        %>
                                    </table>
                                </div>    

                                <div name="EvaluacionesRealizadas"> 
                                    <h2 style=' <% out.print(tblEvlVisible); %>'>Evaluaciones realizadas</h2>
                                    <table style=' <% out.print(tblEvlVisible); %>' class='table table-hover tabla_responsive'>
                                        <thead>
                                            <tr>
                                                <th>Carrera / Curso</th>
                                                <th>Estudio</th>
                                                <th>Evaluación</th>
                                                <th>Fecha</th>
                                                <th>Calificación</th>
                                            </tr>
                                        </thead>

                                        <% for (Object objeto : lstEvl) {
                                                Calendario calendario = (Calendario) objeto;
                                        %>
                                        <tr>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getEvaluacion().getCarreraCursoNombre())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getEvaluacion().getEstudioNombre())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getEvaluacion().getEvlNom())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getCalFch())); %> </td>
                                            <td><% out.print(utilidad.NuloToVacio(calendario.getAlumnoCalificacion(persona.getPerCod()))); %> </td>
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
        
        

        <!-- PopUp para Inscribir -->

        <div id="PopUpInsc" class="modal fade" role="dialog">
            <!-- Modal -->
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Inscribir</h4>
                    </div>
                    <div class="modal-body">

                        <p>Desea inscribirse a <label name="ins_nombre" id="ins_nombre"></label> ?</p>

                    </div>
                    <div class="modal-footer">
                        <button name="ins_boton_confirmar" id="ins_boton_confirmar" type="button" class="btn btn-success" data-codigo="">Confirmar</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    </div>
                </div>
            </div>



            <script type="text/javascript">

                $(document).ready(function () {

                    $('.btn_inscribirAlumno').on('click', function (e) {

                        var codigo = $(this).data("codigo");
                        var nombre = $(this).data("nombre");

                        $('#ins_nombre').text(nombre);
                        $('#ins_boton_confirmar').data('codigo', codigo);


                    });

                    $(document).on('click', "#ins_boton_confirmar", function () {

                        var CalCod = $(this).data("codigo");
                        var PerCod = $('#PerCod').val();

                        $.post('<% out.print(urlSistema); %>ABM_CalendarioAlumno', {
                            pCalCod: CalCod,
                            pAluPerCod: PerCod,
                            pAction: "<% out.print(Modo.INSERT);%>"
                        }, function (responseText) {
                            var obj = JSON.parse(responseText);
                            MostrarCargando(false);

                            $(function () {
                                $('#PopUpInsc').modal('toggle');
                            });
                            MostrarMensaje(obj.tipoMensaje, obj.mensaje);

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


        <!-- PopUp para Salir -->

        <div id="PopUpDesInsc" class="modal fade" role="dialog">
            <!-- Modal -->
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Inscripción</h4>
                    </div>
                    <div class="modal-body">

                        <p>Desea borrarse de <label name="elim_nombre" id="elim_nombre"></label> ?</p>

                    </div>
                    <div class="modal-footer">
                        <button name="desIns_boton_confirmar" id="desIns_boton_confirmar" type="button" class="btn btn-danger" data-codigo="">Borrar</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    </div>
                </div>
            </div>



            <script type="text/javascript">

                $(document).ready(function () {

                    $('.btn_desInscribirAlumno').on('click', function (e) {
                        var codigo = $(this).data("codigo");
                        var nombre = $(this).data("nombre");
                        var alumno = $(this).data("persona");


                        $('#elim_nombre').text(nombre);
                        $('#desIns_boton_confirmar').data('codigo', codigo);
                        $('#desIns_boton_confirmar').data('persona', alumno);



                    });

                    $(document).on('click', "#desIns_boton_confirmar", function () {

                        var CalCod = $(this).data("codigo");
                        var CalAlCod = $(this).data("persona");


                        $.post('<% out.print(urlSistema); %>ABM_CalendarioAlumno', {
                            pCalCod: CalCod,
                            pCalAlCod: CalAlCod,
                            pAction: "<% out.print(Modo.DELETE);%>"
                        }, function (responseText) {
                            var obj = JSON.parse(responseText);
                            MostrarCargando(false);

                            $(function () {
                                $('#PopUpDesInsc').modal('toggle');
                            });

                            MostrarMensaje(obj.tipoMensaje, obj.mensaje);

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

    </body>
</html>