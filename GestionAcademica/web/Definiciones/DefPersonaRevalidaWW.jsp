<%-- 
    Document   : Estudios
    Created on : 26-jul-2017, 16:37:59
    Author     : alvar
--%>

<%@page import="Enumerado.Modo"%>
<%@page import="Entidad.MateriaRevalida"%>
<%@page import="Entidad.Inscripcion"%>
<%@page import="Logica.LoInscripcion"%>
<%@page import="Logica.LoPeriodo"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Utiles.Utilidades"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    String InsCod = request.getParameter("pInsCod");
    Inscripcion inscripcion = (Inscripcion) LoInscripcion.GetInstancia().obtener(Long.valueOf(InsCod)).getObjeto();

    String tblVisible = (inscripcion.getLstRevalidas().size() > 0 ? "" : "display: none;");
    
    String lstMaterias = utilidad.ObjetoToJson(inscripcion.getPlanEstudio().getLstMateria());
    

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Revalidas</title>
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
                            REVALIDAS
                            <!-- BOTONES -->
                            <span class="tools pull-right">
                                <a href="<% out.print(urlSistema); %>Definiciones/DefPersonaInscripcionSWW.jsp?MODO=UPDATE&pPerCod=<% out.print(inscripcion.getAlumno().getPerCod()); %>">Regresar</a>
                            </span>
                        </header>
                        <span class="contenedor_agregar">
                            <a href="#" title="Ingresar" class="glyphicon glyphicon-plus" data-toggle="modal" data-target="#PopUpAgregar"> </a>
                        </span>
                            
                        <div class="panel-body">
                            <div class=" form">
                                    <!-- CONTENIDO -->
                                    
                                    <div name="datos_ocultos">
                                        <input type="hidden" name="InsCod" id="InsCod" value="<% out.print(InsCod); %>">
                                        <input type="hidden" name="CarCod" id="CarCod" value="<% out.print(inscripcion.getPlanEstudio().getCarrera().getCarCod()); %>">
                                        <input type="hidden" name="PlaEstCod" id="PlaEstCod" value="<% out.print(inscripcion.getPlanEstudio().getPlaEstCod()); %>">
                                    </div>
                                    
                                    
                                    <table id='tbl_ww' style=' <% out.print(tblVisible); %>' class='table table-hover'>
                                        <thead>
                                            <tr>
                                                <th></th>
                                                <th>Código</th>
                                                <th>Materia</th>
                                            </tr>
                                        </thead>

                                        <tbody>
                                            <% for (MateriaRevalida matRvl : inscripcion.getLstRevalidas()) {

                                            %>
                                            <tr>
                                                <td><% out.print("<a href='#' data-codigo='" + matRvl.getMatRvlCod() + "' data-nombre='" + matRvl.getMateria().getMatNom() + "' data-toggle='modal' data-target='#PopUpEliminar' name='btn_eliminar' id='btn_eliminar' title='Eliminar' class='glyphicon glyphicon-trash btn_eliminar'/>"); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio(matRvl.getMatRvlCod())); %> </td>
                                                <td><% out.print(utilidad.NuloToVacio(matRvl.getMateria().getMatNom())); %> </td>
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

                    var lstMateria = <%=lstMaterias%>;

                        $.each(lstMateria, function (f, materia) {
                            materia.matCod = "<td> <a href='#' data-codigo='" + materia.matCod + "' data-nombre='" + materia.matNom + "' class='Pop_Seleccionar'>" + materia.matCod + " </a> </td>";
                        });

                        $('#PopUpTblEstudio').DataTable({
                            data: lstMateria,
                            deferRender: true,
                            destroy: true,
                            bLengthChange: false, //thought this line could hide the LengthMenu
                            pageLength: 10,
                            language: {
                                url: "<%=request.getContextPath()%>/JavaScript/DataTable/lang/spanish.json"
                            }
                            , columns: [
                                {"data": "matCod"},
                                {"data": "matNom"}
                            ]

                        });

                    

                    $(document).on('click', ".Pop_Seleccionar", function () {

                        var InsCod = $('#InsCod').val();
                        var codigo = $(this).data("codigo");


                        $.post('<% out.print(urlSistema); %>ABM_Inscripcion', {
                            pInsCod: InsCod,
                            pMatCod: codigo,
                            pAction: "REVALIDA_INSERT"
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

                        <p>Eliminar: <label name="elim_nombre" id="elim_nombre"></label></p>
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
                        var InsCod = $('#InsCod').val();

                        $.post('<% out.print(urlSistema);%>ABM_Inscripcion', {
                            pInsCod: InsCod,
                            pMatRvlCod: codigo,
                            pAction: "REVALIDA_DELETE"
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