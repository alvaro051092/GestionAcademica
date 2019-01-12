<%-- 
    Document   : DefCalendarioWW
    Created on : 03-jul-2017, 18:28:52
    Author     : alvar
--%>
<%@page import="Enumerado.IconClass"%>
<%@page import="Entidad.Periodo"%>
<%@page import="Enumerado.EstadoCalendarioEvaluacion"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Entidad.Persona"%>
<%@page import="Entidad.PeriodoEstudio"%>
<%@page import="Enumerado.Modo"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Entidad.Calendario"%>
<%@page import="java.util.List"%>
<%@page import="Logica.LoPeriodo"%>
<%@page import="Utiles.Utilidades"%>
<%

    LoPeriodo loPeriodo = LoPeriodo.GetInstancia();
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
    String PeriCod = request.getParameter("pPeriCod");
    String titulo = "";

    List<PeriodoEstudio> lstObjeto = new ArrayList<>();

    Retorno_MsgObj retorno = (Retorno_MsgObj) loPeriodo.obtener(Long.valueOf(PeriCod));
    if (!retorno.SurgioErrorObjetoRequerido()) {
        lstObjeto = ((Periodo) retorno.getObjeto()).getLstEstudio();
        titulo = ((Periodo) retorno.getObjeto()).TextoPeriodo();
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
        <title>Sistema de Gestión Académica - Periodo <%=titulo%> | Estudios</title>
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
                        <!-- TABS -->
                        <jsp:include page="/Definiciones/DefPeriodoTabs.jsp"/>
                        <span class="contenedor_agregar">
                            <a href="#" title="Ingresar" class="glyphicon glyphicon-plus" data-toggle="modal" data-target="#PopUpAgregar"> </a>
                        </span>
                        <div class="panel-body">
                            <div class="tab-content">
                                <div id="inicio" class="tab-pane active">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <section class="panel">

                                                <div class="panel-body">
                                                    <div class=" form">
                                                        <div name="datos_ocultos">
                                                            <input type="hidden" name="PeriCod" id="PeriCod" value="<% out.print(PeriCod); %>">
                                                        </div>
                                                        
                                                        <table id='tbl_ww' style=' <% out.print(tblVisible); %>' class='table table-hover'>
                                                            <thead>
                                                                <tr>
                                                                    <th></th>
                                                                    <th>Código</th>
                                                                    <th>Carrera / Curso</th>
                                                                    <th>Estudio</th>
                                                                    <th>Alumnos</th>
                                                                    <th>Docentes</th>
                                                                    <th></th>
                                                                </tr>
                                                            </thead>

                                                            <tbody>
                                                                <% for (PeriodoEstudio periEst : lstObjeto) {
                                                                %>
                                                                <tr>
                                                                    <td><% out.print("<a href='#' data-codigo='" + periEst.getPeriEstCod() + "' data-nombre='" + periEst.getEstudioNombre() + "' data-toggle='modal' data-target='#PopUpEliminar' name='btn_eliminar' id='btn_eliminar' title='Eliminar' class='glyphicon glyphicon-trash btn_eliminar'/>"); %></td>
                                                                    <td><% out.print(utilidad.NuloToVacio(periEst.getPeriEstCod())); %> </td>
                                                                    <td><% out.print(utilidad.NuloToVacio(periEst.getCarreraCursoNombre())); %> </td>
                                                                    <td><% out.print(utilidad.NuloToVacio(periEst.getEstudioNombre())); %> </td>
                                                                    <td>
                                                                        <% out.print(utilidad.NuloToVacio(periEst.getCantidadAlumnos())); %> 
                                                                        <a href="<% out.print(urlSistema); %>Definiciones/DefPeriodoAlumnoSWW.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pPeriEstCod=<% out.print(periEst.getPeriEstCod()); %>" name="btn_edit_alm" id="btn_edit_alm" title="Alumnos" class="<%=IconClass.ALUMNO.getValor()%>" style="margin-left: 5PX;"/>
                                                                    </td>
                                                                    <td>
                                                                        <% out.print(utilidad.NuloToVacio(periEst.getCantidadDocente())); %> 
                                                                        <a href="<% out.print(urlSistema); %>Definiciones/DefPeriodoDocenteSWW.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pPeriEstCod=<% out.print(periEst.getPeriEstCod()); %>" name="btn_edit_dct" id="btn_edit_dct" title="Docentes" class="<%=IconClass.DOCENTE.getValor()%>  style="margin-left: 5PX;""/>
                                                                    </td>    
                                                                    <td><a href="<% out.print(urlSistema); %>Definiciones/DefPeriodoDocumentoSWW.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pPeriEstCod=<% out.print(periEst.getPeriEstCod()); %>" name="btn_edit_doc" id="btn_edit_doc" title="Documentos" class="glyphicon glyphicon-paperclip"/></td>

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
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>

        <jsp:include page="/masterPage/footer.jsp"/>
        
        
        
        
        

                        

        <!-- PopUp para Agregar -->

        <div id="PopUpAgregar" class="modal fade" role="dialog">
            <!-- Modal -->
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Estudio</h4>
                    </div>
                    <div class="modal-body">
                        <div class="padding-popup">
                            <div class="alert alert-warning"><span class="glyphicon glyphicon-warning-sign" style="margin-right:10px;"></span>Se ingresaran los estudios que correspondan con este periodo</div>
                            <div class="row">
                                <label id="lbl_carrera" name="lbl_carrera" class="unchecked"><input type="radio" name="pop_TpoEst" id="pop_TpoEst" class="hide" value="carrera">Carrera</label>
                                <label id="lbl_curso" name="lbl_curso" class="unchecked"><input type="radio" name="pop_TpoEst" id="pop_TpoEst" class="hide" value="curso">Curso</label>
                            </div>

                            <div class="row">
                                <div id="pop_FltrCarrera" name="pop_FltrCarrera" class="contenedorSelect">
                                    <div class="triangulo"></div>
                                    <select class="form-control" id="pop_FltrCarCod" name="pop_FltrCarCod"></select>
                                </div>
                            </div>

                            <div class="row">
                                <table id="PopUpTblEstudio" name="PopUpTblEstudio" class="table table-striped" cellspacing="0"  class="table" width="100%">
                                    <thead>
                                        <tr>
                                            <th>Codigo</th>
                                            <th>Nombre</th>
                                        </tr>
                                    </thead>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    </div>
                </div>
            </div>

            <script type="text/javascript">
                $(document).ready(function () {

                    $('input:radio[name="pop_TpoEst"][value="carrera"]').prop("checked", true);
                    $('#pop_FltrCarrera').show();
                    $('#lbl_carrera').addClass("checked");
                    $('#lbl_curso').removeClass("checked");

                    CargarCarreras();

                    $('input:radio[name="pop_TpoEst"]').change(
                            function () {
                                $('#pop_FltrCarrera').show();

                                if (this.checked) {
                                    if (this.value == "carrera")
                                    {
                                        CargarCarreras();
                                        $('#lbl_carrera').addClass("checked");
                                        $('#lbl_curso').removeClass("checked");
                                    }

                                    if (this.value == "curso")
                                    {
                                        $('#lbl_curso').addClass("checked");
                                        $('#lbl_carrera').removeClass("checked");
                                        CargarCurso();
                                        $('#pop_FltrCarrera').hide();
                                    }
                                }
                            });


                    function CargarCurso()
                    {
                        $.post('<% out.print(urlSistema); %>ABM_Curso', {
                            pAction: "POPUP_OBTENER"
                        }, function (responseText) {
                            var cursos = JSON.parse(responseText);

                            $.each(cursos, function (f, curso) {
                                curso.curCod = "<td> <a href='#' data-codigo='" + curso.curCod + "' data-nombre='" + curso.curNom + "' class='Pop_Seleccionar'>" + curso.curCod + " </a> </td>";
                            });


                            $('#PopUpTblEstudio').DataTable({
                                data: cursos,
                                deferRender: true,
                                bLengthChange: false, //thought this line could hide the LengthMenu
                                destroy: true,
                                pageLength: 10,
                                language: {
                                    "lengthMenu": "Mostrando _MENU_ registros por página",
                                    "zeroRecords": "No se encontraron registros",
                                    "info": "Página _PAGE_ de _PAGES_",
                                    "infoEmpty": "No hay registros",
                                    "search": "Buscar:",
                                    "paginate": {
                                        "first": "Primera",
                                        "last": "Ultima",
                                        "next": "Siguiente",
                                        "previous": "Anterior"
                                    },
                                    "infoFiltered": "(Filtrado de _MAX_ total de registros)"
                                }
                                , columns: [
                                    {"data": "curCod"},
                                    {"data": "curNom"}
                                ]

                            });

                        });
                    }

                    function CargarCarreras()
                    {
                        $('#pop_FltrCarCod').empty();

                        $.post('<% out.print(urlSistema); %>ABM_Carrera', {
                            pAction: "POPUP_OBTENER"
                        }, function (responseText) {
                            var carreras = JSON.parse(responseText);

                            $.each(carreras, function (i, objeto) {
                                $('#pop_FltrCarCod').append($('<option>', {
                                    value: objeto.carCod,
                                    text: objeto.carNom
                                }));

                                if (i == 0)
                                {
                                    CargarPlanes(objeto);
                                }

                            });
                        });
                    }

                    function CargarPlanes(carrera)
                    {


                        $.post('<% out.print(urlSistema); %>ABM_Carrera', {
                            pCod: carrera.carCod,
                            pAction: "POPUP_OBTENER_PLANES"
                        }, function (responseText) {
                            var planes = JSON.parse(responseText);

                            $.each(planes, function (f, plan) {
                                plan.plaEstCod = "<td> <a href='#' data-codigo='" + plan.plaEstCod + "' data-nombre='" + plan.plaEstNom + "' class='Pop_Seleccionar'>" + plan.plaEstCod + " </a> </td>";
                            });

                            $('#PopUpTblEstudio').DataTable({
                                data: planes,
                                deferRender: true,
                                destroy: true,
                                bLengthChange: false, //thought this line could hide the LengthMenu
                                pageLength: 10,
                                language: {
                                    "lengthMenu": "Mostrando _MENU_ registros por página",
                                    "zeroRecords": "No se encontraron registros",
                                    "info": "Página _PAGE_ de _PAGES_",
                                    "infoEmpty": "No hay registros",
                                    "search": "Buscar:",
                                    "paginate": {
                                        "first": "Primera",
                                        "last": "Ultima",
                                        "next": "Siguiente",
                                        "previous": "Anterior"
                                    },
                                    "infoFiltered": "(Filtrado de _MAX_ total de registros)"
                                }
                                , columns: [
                                    {"data": "plaEstCod"},
                                    {"data": "plaEstNom"}
                                ]

                            });
                        });

                    $('#pop_FltrCarCod').on('change', function () {

                        //$('#PopUpTblEstudio').dataTable().fnClearTable();

                        var CarCod = $('select[name=pop_FltrCarCod]').val();
                        $.post('<% out.print(urlSistema); %>ABM_Carrera', {
                            pAction: "POPUP_OBTENER"
                        }, function (responseText) {
                            var carreras = JSON.parse(responseText);


                            $.each(carreras, function (i, objeto) {
                                if (objeto.carCod == CarCod)
                                {

                                    CargarPlanes(objeto);
                                }

                            });
                        });
                    });

                    $(document).on('click', ".Pop_Seleccionar", function () {

                        var CarCod = $('select[name=pop_FltrCarCod]').val();
                        var PeriCod = $('#PeriCod').val();
                        var codigo = $(this).data("codigo");

                        var tipo = "CARRERA";

                        if ($('input:radio[name="pop_TpoEst"][value="carrera"]').prop("checked"))
                        {
                            tipo = "CARRERA";
                        }

                        if ($('input:radio[name="pop_TpoEst"][value="curso"]').prop("checked"))
                        {
                            tipo = "CURSO";
                        }


                        $.post('<% out.print(urlSistema); %>ABM_PeriodoEstudio', {
                            pCarCod: CarCod,
                            pPeriCod: PeriCod,
                            pCodigoEstudio: codigo,
                            pTipoEstudio: tipo,
                            pAction: "<% out.print(Modo.INSERT);%>"
                        }, function (responseText) {
                            var obj = JSON.parse(responseText);

                            if (obj.tipoMensaje != 'ERROR')
                            {
                                $(function () {
                                    $('#PopUpAgregar').modal('toggle');
                                });
                                MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                                location.reload();
                            } else
                            {
                                MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                            }

                        });

                    });
                }
            });
            </script>

        </div>


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

                        <p>Eliminar : <label name="elim_nombre" id="elim_nombre"></label></p>
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
                        var PeriCod = $('#PeriCod').val();
                        var codigo = $('#elim_boton_confirmar').data('codigo');
                        $.post('<% out.print(urlSistema); %>ABM_PeriodoEstudio', {
                            pPeriCod: PeriCod,
                            pPeriEstCod: codigo,
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
    </body>
</html>
