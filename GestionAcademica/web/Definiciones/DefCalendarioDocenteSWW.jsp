<%-- 
    Document   : DefCalendarioWW
    Created on : 03-jul-2017, 18:28:52
    Author     : alvar
--%>
<%@page import="Enumerado.Modo"%>
<%@page import="Entidad.CalendarioDocente"%>
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
    String titulo = "";

    List<CalendarioDocente> lstObjeto = new ArrayList<>();

    Retorno_MsgObj retorno = (Retorno_MsgObj) loCalendario.obtener(Long.valueOf(CalCod));
    if (!retorno.SurgioErrorObjetoRequerido()) {
        titulo = ((Calendario) retorno.getObjeto()).getEvaluacion().getEstudioNombre() 
                + " - " 
                + ((Calendario) retorno.getObjeto()).getEvaluacion().getEvlNom()
                + " - "
                + ((Calendario) retorno.getObjeto()).getCalFch();
        lstObjeto = ((Calendario) retorno.getObjeto()).getLstDocentes();
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
        <title>Sistema de Gestión Académica - Calendario <%=titulo%> | Docentes</title>
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
                        <jsp:include page="/Definiciones/DefCalendarioTabs.jsp"/>
                        <div class="contenedor_agregar">
                            <a href="#" title="Inscribir periodo" name='btn_inscribirPeriodo' id='btn_inscribirPeriodo' class="fa fa-group" data-toggle="modal" data-target="#PopUpInscPeriodo"> </a>
                            <a href="#" title="Ingresar" class="glyphicon glyphicon-plus" data-toggle="modal" data-target="#PopUpAgregar"> </a>
                            <input type="hidden" name="CalCod" id="CalCod" value="<% out.print(CalCod); %>">
                            <input type="hidden" name="popFiltro" id="popFiltro" value="<% out.print(((Calendario) retorno.getObjeto()).getEvaluacion().getEstudioNombre()); %>">
                        </div>
                        <div class="panel-body">
                            <div class=" form">
                                   <table id='tbl_ww' style=' <% out.print(tblVisible); %>' class='table table-hover'>
                                        <thead><tr>
                                                <th></th>
                                                <th>Código</th>
                                                <th>Docente</th>
                                                <th>Documento</th>
                                            </tr>
                                        </thead>

                                        <tbody>
                                            <% for (CalendarioDocente calDocente : lstObjeto) {

                                            %>
                                            <tr>
                                                <td><% out.print("<a href='#' data-codigo='" + calDocente.getCalDocCod() + "' data-nombre='" + calDocente.getDocente().getNombreCompleto() + "' data-toggle='modal' data-target='#PopUpEliminar' name='btn_eliminar' id='btn_eliminar' title='Eliminar' class='glyphicon glyphicon-trash btn_eliminar'/>"); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio(calDocente.getCalDocCod())); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio((calDocente.getDocente() != null ? calDocente.getDocente().getNombreCompleto() : ""))); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio((calDocente.getDocente() != null ? calDocente.getDocente().getPerDoc() : ""))); %> </td>
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
        

        <!-- PopUp para Agregar docentes al calendario -->

        <div id="PopUpAgregar" class="modal fade" role="dialog">
            <!-- Modal -->
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content modal-lg">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Personas</h4>
                    </div>
                    <div class="modal-body">



                        <div>



                            <table id="PopUpTblPersona" name="PopUpTblPersona" class="table table-striped" cellspacing="0"  class="table" width="100%">
                                <thead>
                                    <tr>
                                        <th>Codigo</th>
                                        <th>Nombre</th>
                                        <th>Tipo</th>
                                        <th>Documento</th>
                                    </tr>
                                </thead>

                            </table>
                        </div>



                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    </div>
                </div>
            </div>



            <script type="text/javascript">

                $(document).ready(function () {

                    Buscar();


                    $(document).on('click', ".PopPer_Seleccionar", function () {

                        var PerCod = $(this).data("codigo");
                        var CalCod = $('#CalCod').val();
                        $.post('<% out.print(urlSistema); %>ABM_CalendarioDocente', {
                            pCalCod: CalCod,
                            pDocPerCod: PerCod,
                            pAction: "<% out.print(Modo.INSERT);%>"
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

                        $(function () {
                            $('#PopUpPersona').modal('toggle');
                        });
                    });


                    function Buscar()
                    {

                        $.post('<% out.print(urlSistema); %>ABM_Persona', {
                            pAction: "POPUP_OBTENER"
                        }, function (responseText) {


                            var personas = JSON.parse(responseText);

                            $.each(personas, function (f, persona) {

                                persona.perCod = "<td> <a href='#' data-codigo='" + persona.perCod + "' data-nombre='" + persona.perNom + "' class='PopPer_Seleccionar'>" + persona.perCod + " </a> </td>";
                            });

                            $('#PopUpTblPersona').DataTable({
                                data: personas,
                                responsive: true,
                                processing: true,
                                deferRender: true,
                                bLengthChange: false, //thought this line could hide the LengthMenu
                                pageLength: 10,
                                language: {
                                    "url": "<%=request.getContextPath()%>/JavaScript/DataTable/lang/spanish.json"
                                }
                                , search: {
                                    "search": "Docente"
                                }
                                , columns: [
                                    {"data": "perCod"},
                                    {"data": "nombreCompleto"},
                                    {"data": "tipoPersona"},
                                    {"data": "perDoc"}
                                ]

                            });

                        });
                    }


                });
            </script>
        </div>

        <!------------------------------------------------->

        <!-- PopUp para Eliminar personas del calendario -->

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

                        <p>Eliminar docente: <label name="elim_nombre" id="elim_nombre"></label></p>
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
                        var CalCod = $('#CalCod').val();
                        $.post('<% out.print(urlSistema); %>ABM_CalendarioDocente', {
                            pCalCod: CalCod,
                            pCalDocCod: codigo,
                            pAction: "<% out.print(Modo.DELETE);%>"
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

                        $(function () {
                            $('#PopUpEliminar').modal('toggle');
                        });

                    });

                });
            </script>
        </div>

        <!------------------------------------------------->

        <!-- Inscribir periodo ---------------------------------------------------------------------------------------------------------------------------------------->

        <div id="PopUpInscPeriodo" class="modal fade" role="dialog">
            <!-- Modal -->
            <div class="modal-dialog modal-lg" >
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Periodos</h4>
                    </div>

                    <div class="modal-body">

                        <div>
                            <input type="hidden" name="insTpo" id="insTpo" value="">
                            <table name="PopUpTblPeriodos" id="PopUpTblPeriodos" class="table table-striped" cellspacing="0" width="100%">
                                <thead>
                                    <tr>
                                        <th>Código</th>
                                        <th>Carrera / Curso</th>
                                        <th>Estudio</th>
                                        <th>Periodo</th>
                                        <th>Tipo</th>
                                        <th>Fecha de inicio</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <input type="button" class="btn btn-default" value="Cancelar" data-dismiss="modal" />
                    </div>
                </div>
            </div>

            <script type="text/javascript">
                $(document).ready(function () {
                    var filtro = $('#popFiltro').val();

                    $(document).on('click', ".Pop_Seleccionar", function () {
                        var CalCod = $('#CalCod').val();
                        var insTpo = "DOCENTE";
                        var PeriEstCod = $(this).data("codigo");

                        $.post('<% out.print(urlSistema); %>ABM_Calendario', {
                            pCalCod: CalCod,
                            pPeriEstCod: PeriEstCod,
                            pInsTpo: insTpo,
                            pAction: "INSCRIBIR_PERIODO"
                        }, function (responseText) {

                            var obj = JSON.parse(responseText);

                            $(function () {
                                $('#PopUpInscPeriodo').modal('toggle');
                            });

                            MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                            location.reload();

                        });
                    });

                    $.post('<% out.print(urlSistema);%>ABM_PeriodoEstudio', {
                        pAction: "POPUP_LISTAR"
                    }, function (responseText) {

                        var periodos = JSON.parse(responseText);

                        $.each(periodos, function (f, periodo) {
                            periodo.periEstCod = "<td> <a href='#' data-codigo='" + periodo.periEstCod + "' class='Pop_Seleccionar'>" + periodo.periEstCod + " </a> </td>";
                        });

                        $('#PopUpTblPeriodos').DataTable({
                            data: periodos,
                            deferRender: true,
                            bLengthChange: false, //thought this line could hide the LengthMenu
                            pageLength: 10,
                            select: {
                                style: 'multi',
                                selector: 'td:last-child'
                            },
                            search: {
                                "search": filtro
                            },
                            language: {
                                "lengthMenu": "Mostrando _MENU_ registros por página",
                                "zeroRecords": "No se encontraron registros",
                                "info": "Página _PAGE_ de _PAGES_",
                                "infoEmpty": "No hay registros",
                                "search": "Buscar:",
                                select: {
                                    rows: {
                                        _: "%d filas seleccionadas",
                                        0: "",
                                        1: "1 fila seleccionada"
                                    }
                                },
                                "paginate": {
                                    "first": "Primera",
                                    "last": "Ultima",
                                    "next": "Siguiente",
                                    "previous": "Anterior"
                                },
                                "infoFiltered": "(Filtrado de _MAX_ registros)"
                            },
                            columns: [
                                {"data": "periEstCod"},
                                {"data": "carreraCursoNombre"},
                                {"data": "estudioNombre"},
                                {"data": "periodo.perVal"},
                                {"data": "periodo.perTpoNombre"},
                                {"data": "periodo.perFchIni"}
                            ]

                        });

                    });


                });
            </script>
        </div>

    </body>
</html>

