<%-- 
    Document   : DefPeriodoEstudioWW
    Created on : 03-jul-2017, 18:28:52
    Author     : alvar
--%>
<%@page import="Logica.Seguridad"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Entidad.Persona"%>
<%@page import="Entidad.PeriodoEstudioAlumno"%>
<%@page import="Enumerado.Modo"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="java.util.ArrayList"%>
<%@page import="Entidad.PeriodoEstudio"%>
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
    String PeriEstCod = request.getParameter("pPeriEstCod");
    
    String titulo = "";

    List<PeriodoEstudioAlumno> lstObjeto = new ArrayList<>();

    Retorno_MsgObj retorno = (Retorno_MsgObj) loPeriodo.EstudioObtener(Long.valueOf(PeriEstCod));
    if (!retorno.SurgioErrorObjetoRequerido()) {
        lstObjeto = ((PeriodoEstudio) retorno.getObjeto()).getLstAlumno();
        titulo = ((PeriodoEstudio) retorno.getObjeto()).getPeriodo().TextoPeriodo()
                + " - "
                +((PeriodoEstudio) retorno.getObjeto()).getCarreraCursoNombre() 
                + " - "
                + ((PeriodoEstudio) retorno.getObjeto()).getEstudioNombre();
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
        <title>Sistema de Gestión Académica - Periodo Estudio <%=titulo%> | Alumnos</title>
        <jsp:include page="/masterPage/head.jsp"/>
        <jsp:include page="/masterPage/head_tables.jsp"/>
        <style>
            td.details-control{
                cursor: pointer;
            }

            th.fa-plus-square{
                display: none; 
            }


        </style>
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
                        <jsp:include page="/Definiciones/DefPeriodoEstudioTabs.jsp">
                            <jsp:param name="MostrarTabs" value="SI" />
                            <jsp:param name="Codigo" value="<%= PeriEstCod%>" />
                        </jsp:include>
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
                                                            <input type="hidden" name="PeriEstCod" id="PeriEstCod" value="<% out.print(PeriEstCod); %>">
                                                        </div>
                                                        
                                                        <table id='tbl_ww' style=' <% out.print(tblVisible); %>' class='table table-hover'>
                                                            <thead><tr>
                                                                    <th></th>
                                                                    <th>Código</th>
                                                                    <th>Alumno</th>
                                                                    <th>Fecha de inscripción</th>
                                                                    <th>Inscripcion forzada</th>
                                                                </tr>
                                                            </thead>

                                                            <tbody>
                                                                <% for (PeriodoEstudioAlumno periAlumno : lstObjeto) {

                                                                %>
                                                                <tr>
                                                                    <td><% out.print("<a href='#' data-codigo='" + periAlumno.getPeriEstAluCod() + "' data-nombre='" + periAlumno.getAlumno().getNombreCompleto() + "' data-toggle='modal' data-target='#PopUpEliminar' name='btn_eliminar' id='btn_eliminar' title='Eliminar' class='glyphicon glyphicon-trash btn_eliminar'/>"); %> </td>
                                                                    <td><% out.print(utilidad.NuloToVacio(periAlumno.getPeriEstAluCod())); %> </td>
                                                                    <td><% out.print(utilidad.NuloToVacio(periAlumno.getAlumno().getNombreCompleto())); %> </td>
                                                                    <td><% out.print(utilidad.NuloToVacio(periAlumno.getPerInsFchInsc())); %> </td>
                                                                    <td><% out.print(utilidad.BooleanToSiNo(periAlumno.getPerInsFrz())); %> </td>
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
        
        


        <!-- PopUp para Agregar personas del calendario -->

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

                        <p>Forzar inscripción de alumnos.</p>
                        <p>Seleccione a la persona que desea inscribir.</p>

                        <div>

                            <table id="PopUpTblPersona" name="PopUpTblPersona" class="table table-striped" cellspacing="0"  class="table" width="100%">
                                <thead>
                                    <tr>
                                        <th>Codigo</th>
                                        <th>Nombre</th>
                                        <th>Tipo</th>
                                        <th>Documento</th>
                                        <th></th>
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

                    var table;

                    Buscar();

                    $(document).on('click', ".PopPer_Seleccionar", function () {

                        var AluPerCod = $(this).data("codigo");
                        var PeriEstCod = $('#PeriEstCod').val();

                        $.post('<% out.print(urlSistema); %>ABM_PeriodoEstudioAlumno', {
                            pPeriEstCod: PeriEstCod,
                            pAluPerCod: AluPerCod,
                            pAction: "<% out.print(Modo.INSERT);%>"
                        }, function (responseText) {
                            var obj = JSON.parse(responseText);
                            MostrarCargando(false);

                            if (obj.tipoMensaje != 'ERROR')
                            {
                                location.reload();
                            } else
                            {
                                MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                            }

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

                            table = $('#PopUpTblPersona').DataTable({
                                data: personas,
                                responsive: true,
                                processing: true,
                                deferRender: true,
                                bLengthChange: false, //thought this line could hide the LengthMenu
                                pageLength: 10,
                                language: {
                                    "url": "<%=request.getContextPath()%>/JavaScript/DataTable/lang/spanish.json"
                                },
                                 search: {
                                    "search": "Alumno"
                                }
                                , columns: [
                                    {"data": "perCod"},
                                    {"data": "nombreCompleto"},
                                    {"data": "tipoPersona"},
                                    {"data": "perDoc"},
                                    {
                                        "className": 'details-control fa fa-plus-square',
                                        "orderable": false,
                                        "data": null,
                                        "defaultContent": ''
                                    }

                                ]
                            });




                        });
                    }


                    function format(d)
                    {

                        var retorno = "";

                        $.each(d.lstEstudios, function (f, estudio) {
                            retorno += "<div>";

                            retorno += "<div class='contenedor_titulo_escolaridad'><label>" + estudio.inscripcion.nombreEstudio + "</label></div>";

                            retorno += "<div class='contenedor_tabla_escolaridad'>";
                            retorno += "<table class='table table-hover eliminar_margen_tabla'>";
                            retorno += "<thead><tr>";
                            retorno += "<th>Materia</th>";
                            retorno += "<th class='texto_derecha'>Calificación</th>";
                            retorno += "</tr>";
                            retorno += "</thead>";
                            retorno += "<tbody>";

                            $.each(estudio.escolaridad, function (f, esc) {

                                retorno += "<tr>";
                                retorno += "<td>";

                                retorno += esc.nombreEstudio;

                                retorno += "</td>";

                                retorno += "<td class='texto_derecha'>";
                                retorno += "<label>" + esc.aprobacion + "</label>";
                                retorno += "</td>";

                                retorno += "</tr>";

                            });

                            retorno += "</tbody>";
                            retorno += "</table>";
                            retorno += "</div>";

                            retorno += "</div>";
                        });




                        return retorno;

                    }


                    // Add event listener for opening and closing details
                    $(document).on('click', ".details-control", function () {

                        var tr = $(this).closest('tr');
                        var td = $(this).closest('td');

                        var row = table.row(tr);

                        if (row.child.isShown()) {
                            // This row is already open - close it
                            row.child.hide();
                            tr.removeClass('shown');
                            td.addClass("fa-plus-square");
                            td.removeClass("fa-minus-square");
                        } else {
                            // Open this row
                            row.child(format(row.data())).show();
                            tr.addClass('shown');

                            td.removeClass("fa-plus-square");
                            td.addClass("fa-minus-square");
                        }


                    });


                });
            </script>
        </div>

        <!------------------------------------------------->

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

                        <p>Eliminar la inscripción de: <label name="elim_nombre" id="elim_nombre"></label></p>
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
                        var PeriEstCod = $('#PeriEstCod').val();
                        var codigo = $('#elim_boton_confirmar').data('codigo');

                        $.post('<% out.print(urlSistema); %>ABM_PeriodoEstudioAlumno', {
                            pPeriEstCod: PeriEstCod,
                            pPeriEstAluCod: codigo,
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

                    });

                });
            </script>
        </div>

        <!------------------------------------------------->

    </body>
</html>
