<%-- 
    Document   : DefCursoWW
    Created on : 03-jul-2017, 18:28:52
    Author     : alvar
--%>
<%@page import="Entidad.Calendario"%>
<%@page import="SDT.SDT_Evento"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Entidad.Curso"%>
<%@page import="java.util.List"%>
<%@page import="Logica.LoCurso"%>
<%@page import="Utiles.Utilidades"%>
<%

    LoCurso loCurso = LoCurso.GetInstancia();
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
    List<Object> lstCurso = new ArrayList<>();

    Retorno_MsgObj retorno = (Retorno_MsgObj) loCurso.obtenerLista();
    if (retorno.getMensaje().getTipoMensaje() != TipoMensaje.ERROR && retorno.getLstObjetos() != null) {
        lstCurso = retorno.getLstObjetos();
    } else {
        out.print(retorno.getMensaje().toString());
    }

    String tblCursoVisible = (lstCurso.size() > 0 ? "" : "display: none;");

    //Documentacion
    //https://fullcalendar.io/docs/

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica -  Calendario</title>
        <jsp:include page="/masterPage/head.jsp"/>

        <link rel='stylesheet' href='<% out.print(urlSistema); %>JavaScript/calendario/fullcalendar.css' />

        <style>

            
        </style>

        <script src='<% out.print(urlSistema); %>JavaScript/calendario/lib/moment.min.js'></script>
        <script src='<% out.print(urlSistema); %>JavaScript/calendario/fullcalendar.js'></script>
        <script src='<% out.print(urlSistema); %>JavaScript/calendario/locale/es.js'></script>

        <script src='<%=request.getContextPath()%>/JavaScript/calendario/lib/jquery-ui.min.js'></script>
        <link href="<%=request.getContextPath()%>/JavaScript/calendario/lib/cupertino/jquery-ui.min.css" rel="stylesheet" type="text/css"/>

        <script src="<%=request.getContextPath()%>/JavaScript/DataTable/extensions/Responsive/js/dataTables.responsive.min.js"></script>
        <link href="<%=request.getContextPath()%>/JavaScript/DataTable/extensions/Responsive/css/responsive.dataTables.min.css" rel="stylesheet" type="text/css"/>
        
        <style>
            @media (min-width: 1024px) {
                .modal-lg { width: 1000px; }
              }
        </style>
        
        <script>
            $(document).ready(function () {


                //---------------------------------------------------------------------------------------------------
                //MOSTRAR CALENDARIO
                //---------------------------------------------------------------------------------------------------
                function MostrarCalendario(codigo)
                {
                    $('#btn_guardar').data("codigo", codigo);
                    $('#btn_eliminar').data("codigo", codigo);
                    $('#btn_guardar').data("modo", "UPDATE");
                    $('#btn_eliminar').data("modo", "DELETE");

                    $.post('<% out.print(urlSistema); %>ABM_Calendario', {
                        pCalCod: codigo,
                        pAction: "POPUP_OBTENER"
                    }, function (responseText) {

                        var calendario = JSON.parse(responseText);

                        $('#CalCod').val(calendario.calCod);
                        $('#EvlCod').val(calendario.evaluacion.evlCod);
                        $('#EvlNom').val(calendario.evaluacion.evlNom);
                        $('#CalFch').val(calendario.calFch);
                        $('#EvlInsFchDsd').val(calendario.evlInsFchDsd);
                        $('#EvlInsFchHst').val(calendario.evlInsFchHst);

                        $("#enlace_alumno").attr("href", "<% out.print(urlSistema); %>Definiciones/DefCalendarioAlumnoSWW.jsp?RET=GR&MODO=UPDATE&pCalCod=" + calendario.calCod);
                        $("#enlace_docente").attr("href", "<% out.print(urlSistema); %>Definiciones/DefCalendarioDocenteSWW.jsp?RET=GR&MODO=UPDATE&pCalCod=" + calendario.calCod);

                        $(function () {
                            $('#PopUpVerCalendario').modal('show');
                        });
                    });
                }


                function MostrarAgregarEvaluacion(date)
                {
                    var fechaAgregar = date.format();
                    
                    var fechaHasta  = new Date(fechaAgregar);
                    
                    fechaHasta = new Date(fechaHasta.setDate(fechaHasta.getDate() - 1));
                    var fechaDesde = new Date(fechaHasta);
                    fechaDesde.setDate(fechaDesde.getDate() - 7);
                    

                    $(function () {
                        $('#PopUpAgregar').modal('show');
                    });

                    //---------------------------------------------------------------------------------------------------
                    //LISTAR EVALUACIONES
                    //---------------------------------------------------------------------------------------------------
                    $.post('<% out.print(urlSistema); %>ABM_Evaluacion', {
                        pAction: "POPUP_LISTAR"
                    }, function (responseText) {

                        var evaluaciones = JSON.parse(responseText);

                        $('#PopUpTblEvaluaciones').DataTable({
                            data: evaluaciones,
                            responsive: true,
                            processing: true,
                            deferRender: true,
                            bLengthChange: false, //thought this line could hide the LengthMenu
                            pageLength: 10,
                            destroy: true,
                            select: {
                                style: 'multi'
                            },
                            language: {
                                "url": "<%=request.getContextPath()%>/JavaScript/DataTable/lang/spanish.json"
                            },
                            columns: [
                                {"data": "carreraCursoNombre"},
                                {"data": "estudioNombre"},
                                {"data": "evlNom"},
                                
                                {
                                    "orderable": false,
                                    "data": null,
                                    "defaultContent": '<input type="date" name="cel_fecha" value="' + fechaAgregar + '">'
                                },
                                {
                                    "orderable": false,
                                    "data": null,
                                    "defaultContent": '<input type="date" name="cel_fecha_desde" value="' + fechaDesde.toISOString().substring(0,10) + '">'
                                },
                                {
                                    "orderable": false,
                                    "data": null,
                                    "defaultContent": '<input type="date" name="cel_fecha_hasta" value="' + fechaHasta.toISOString().substring(0,10) + '">'
                                },
                                {
                                    "className": 'select-checkbox',
                                    "orderable": false,
                                    "data": null,
                                    "defaultContent": ''
                                }
                            ]

                        });

                    });
                }

                //---------------------------------------------------------------------------------------------------
                //GUARDAR CALENDARIO
                //---------------------------------------------------------------------------------------------------
                $('#btn_guardar').on('click', function (e) {

                    var CalCod = $(this).data("codigo");
                    var Modo = $(this).data("modo");

                    var EvlCod = $('#EvlCod').val();
                    var CalFch = $('#CalFch').val();
                    var EvlInsFchDsd = $('#EvlInsFchDsd').val();
                    var EvlInsFchHst = $('#EvlInsFchHst').val();

                    $.post('<% out.print(urlSistema); %>ABM_Calendario', {
                        pCalCod: CalCod,
                        pEvlCod: EvlCod,
                        pCalFch: CalFch,
                        pEvlInsFchDsd: EvlInsFchDsd,
                        pEvlInsFchHst: EvlInsFchHst,
                        pAction: Modo
                    }, function (responseText) {

                        var obj = JSON.parse(responseText);

                        $(function () {
                            $('#PopUpVerCalendario').modal('toggle');
                        });

                        MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                        if (obj.tipoMensaje != 'ERROR')
                            location.reload();

                    });

                });

                //---------------------------------------------------------------------------------------------------
                //GUARDAR CALENDARIO
                //---------------------------------------------------------------------------------------------------
                $('#btn_eliminar').on('click', function (e) {

                    var CalCod = $(this).data("codigo");
                    var Modo = $(this).data("modo");

                    var EvlCod = $('#EvlCod').val();
                    var CalFch = $('#CalFch').val();
                    var EvlInsFchDsd = $('#EvlInsFchDsd').val();
                    var EvlInsFchHst = $('#EvlInsFchHst').val();

                    $.post('<% out.print(urlSistema); %>ABM_Calendario', {
                        pCalCod: CalCod,
                        pEvlCod: EvlCod,
                        pCalFch: CalFch,
                        pEvlInsFchDsd: EvlInsFchDsd,
                        pEvlInsFchHst: EvlInsFchHst,
                        pAction: Modo
                    }, function (responseText) {

                        var obj = JSON.parse(responseText);

                        $(function () {
                            $('#PopUpVerCalendario').modal('toggle');
                        });

                        MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                        if (obj.tipoMensaje != 'ERROR')
                            location.reload();

                    });

                });

                //---------------------------------------------------------------------------------------------------
                //GUARDAR CALENDARIO - EVALUACIONES
                //---------------------------------------------------------------------------------------------------
                $(document).on('click', "#btn_agregar", function () {

                    var table = $('#PopUpTblEvaluaciones').DataTable();
                    var count = table.rows({selected: true}).count();
                    var rows = table.rows({selected: true});

                    var error = false;

                    for (i = 0; i < count; i++)
                    {
                        var objeto = rows.data()[i];
                        var fila = rows.nodes()[i];

                        var fechaEvaluacion = fila.cells[3].lastChild.value;
                        var fechaDesde = fila.cells[4].lastChild.value;
                        var fechaHasta = fila.cells[5].lastChild.value;

                        if (fechaEvaluacion == "")
                        {
                            MostrarMensaje("ERROR", "Debe ingresar una fecha de evaluación para: " + objeto.evlNom);
                            error = true;
                        }

                        if (!objeto.tpoEvl.tpoEvlInsAut)
                        {
                            if (fechaDesde == "")
                            {
                                MostrarMensaje("ERROR", "Debe ingresar una fecha de inicio de inscripción para: " + objeto.evlNom);
                                error = true;
                            }

                            if (fechaHasta == "")
                            {
                                MostrarMensaje("ERROR", "Debe ingresar una fecha de fin de inscripción para: " + objeto.evlNom);
                                error = true;
                            }
                        }
                    }

                    if (count < 1)
                    {
                        MostrarMensaje("ERROR", "Debe seleccionar al menos una fila");
                    } else
                    {
                        if (!error)
                        {


                            var listaCalendario = new Array(count);

                            //Procesar
                            for (i = 0; i < count; i++)
                            {

                                var calendario = JSON.parse('{"evaluacion":{"evlCod":null},"calFch":null,"calCod":null,"evlInsFchHst":null,"evlInsFchDsd":null}');
                                var objeto = rows.data()[i];
                                var fila = rows.nodes()[i];

                                var fechaEvaluacion = fila.cells[3].lastChild.value;
                                var fechaDesde = fila.cells[4].lastChild.value;
                                var fechaHasta = fila.cells[5].lastChild.value;

                                calendario.evaluacion.evlCod = objeto.evlCod;
                                calendario.calFch = fechaEvaluacion;

                                if (!objeto.tpoEvl.tpoEvlInsAut)
                                {
                                    calendario.evlInsFchDsd = fechaDesde;
                                    calendario.evlInsFchHst = fechaHasta;
                                }

                                listaCalendario[i] = calendario;

                            }

                            $.post('<% out.print(urlSistema); %>ABM_Calendario', {
                                pLstCalendario: JSON.stringify(listaCalendario),
                                pAction: "INSERT_LIST"
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
                        }
                    }

                });



                //---------------------------------------------------------------------------------------------------
                //CARGAR CALENDARIO
                //---------------------------------------------------------------------------------------------------
                
                var defaultView = 'month';
                var header = {
				left: 'prev,next today',
				center: 'title',
				right: 'month,basicWeek,basicDay'
                            };
                if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) 
                {
                    defaultView = 'basicDay';
                    header = {
                                left: 'prev,next today',
				center: 'title',
				right: 'basicWeek,basicDay'
                            };
                }

                $('#calendar').fullCalendar({
                    // put your options and callbacks here
                    eventLimit: true,
                    theme: true,
                    defaultView: defaultView,
                    aspectRatio: 2.1,
                    themeSystem: 'jquery-ui',
                    header: header,
                    locale: 'es',
                    events: {
                        url: '<%=urlSistema%>ABM_Calendario',
                        type: 'POST',
                        data: {
                            pAction: 'OBTENER_EVENTO'
                        },
                        error: function () {
                            alert('Error al recuperar eventos!');
                        }
                    },
                    eventClick: function (calEvent, jsEvent, view) {

                        MostrarCalendario(calEvent.id);
                    },
                    dayClick: function (date, jsEvent, view) {

                        MostrarAgregarEvaluacion(date);

                    }
                });
                
                 
                
            });
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
                        <jsp:include page="/Definiciones/DefCalendarioWWTabs.jsp"/>
                        <div class="panel-body">
                            <div class="tab-content">
                                <div id="inicio" class="tab-pane active">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <section class="panel">
                                                
                                                <div class="panel-body">
                                                    <div class=" form">
                                                        <div id="calendar"></div>
                                                    </div>
                                                </div>
                                            </section>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>

        <jsp:include page="/masterPage/footer.jsp"/>
        
        
        <!-- Ver alumno -->

        <div id="PopUpVerCalendario" class="modal fade" role="dialog">
            <!-- Modal -->
            <div class="modal-dialog modal-lg" >
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Periodos</h4>
                    </div>
                    <form id="frm_objeto" name="frm_objeto">
                        <div class="modal-body">

                            <div style="margin-bottom: 20px;">
                                <a style="padding: 5px 15px; background-color: #3a80af; color: #FFF; border-radius: 4px; margin-right: 5px;" href="" id="enlace_alumno" name="enlace_alumno">Alumnos</a>
                                <a style="padding: 5px 15px; background-color: #3a80af; color: #FFF; border-radius: 4px;" href="" id="enlace_docente" name="enlace_docente">Docentes</a>
                            </div>
                            <input type="hidden" class="form-control" id="CalCod" name="CalCod" placeholder="CalCod" disabled value="" >

                            <label>Evaluación:</label>
                            <div class="row"> 
                                <div class="col-lg-1">
                                    <input type="text" class="form-control" id="EvlCod" name="EvlCod" placeholder="EvlCod" disabled value="" >
                                </div>
                                <div class="col-lg-3">
                                    <input type="text" class="form-control" id="EvlNom" name="EvlNom" placeholder="EvlNom" disabled value="" >
                                </div>
                                <div class="col-lg-3">
                                    <a href="#" id="btnEvlCod" name="btnEvlCod" class="glyphicon glyphicon-search" data-toggle="modal" data-target="#PopUpEvaluacion"></a>
                                </div>
                            </div>


                            <div class="row">
                                <div class="col-lg-3"><label>Fecha:</label><input type="date" class="form-control" id="CalFch" name="CalFch" placeholder="CalFch" value="" ></div>                            
                                <div class="col-lg-3"><label>Inscripción desde:</label><input type="date" class="form-control" id="EvlInsFchDsd" name="EvlInsFchDsd" placeholder="EvlInsFchDsd" value="" ></div>                       
                                <div class="col-lg-3"><label>Inscripción hasta:</label><input type="date" class="form-control" id="EvlInsFchHst" name="EvlInsFchHst" placeholder="EvlInsFchHst" value="" ></div>
                            </div>

                        </div>
                        <div class="modal-footer">
                            <input name="btn_guardar" id="btn_guardar" value="Guardar" type="button"  class="btn btn-success" />
                            <input name="btn_eliminar" id="btn_eliminar" value="Eliminar" type="button"  class="btn btn-danger" />
                            <input type="button" class="btn btn-default" value="Cancelar" data-dismiss="modal" />
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Evaluaciones -->
        <div id="PopUpEvaluacion"  class="modal fade" role="dialog">
            <jsp:include page="/PopUps/PopUpEvaluacion.jsp"/>
        </div>


        <!-- Agregar periodo -->

        <div id="PopUpAgregar" class="modal fade" role="dialog">
            <!-- Modal -->
            <div class="modal-dialog modal-lg" >
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Evaluaciones</h4>
                        <input type="hidden" name="obj_calendario" id="obj_calendario" value="<% out.print(utilidad.ObjetoToJson(new Calendario()));%>">
                    </div>

                    <div class="modal-body">

                        <div>
                            <table name="PopUpTblEvaluaciones" id="PopUpTblEvaluaciones" class="table table-striped" cellspacing="0" width="100%">
                                <thead>
                                    <tr>
                                        <th>Carrera / Curso</th>
                                        <th>Estudio</th>
                                        <th>Evaluación</th>
                                        <th>Fecha</th>
                                        <th>Inscripción desde</th>
                                        <th>Inscripción hasta</th>
                                        <th></th>
                                    </tr>
                                </thead>

                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <input name="btn_agregar" id="btn_agregar" value="Guardar" class="btn btn-success" type="button" />
                        <input type="button" class="btn btn-default" value="Cancelar" data-dismiss="modal" />
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
