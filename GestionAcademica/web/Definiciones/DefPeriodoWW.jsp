<%-- 
    Document   : DefPeriodoWW
    Created on : 18-jul-2017, 20:39:04
    Author     : alvar
--%>

<%@page import="Enumerado.Modo"%>
<%@page import="Enumerado.TipoPeriodo"%>
<%@page import="Entidad.Periodo"%>
<%@page import="Logica.LoPeriodo"%>
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
    List<Object> lstObjeto = new ArrayList<>();

    Retorno_MsgObj retorno = (Retorno_MsgObj) loPeriodo.obtenerLista();
    if (!retorno.SurgioErrorListaRequerida()) {
        lstObjeto = retorno.getLstObjetos();
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
        <title>Sistema de Gestión Académica - Periodo</title>
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
                            PERIODOS
                            <!-- BOTONES -->
                            <span class="tools pull-right">
                                <a href="#" title="Ingresar" class="glyphicon glyphicon-plus" data-toggle="modal" data-target="#PopUpAgregar"> </a>
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
                                            <th>Tipo</th>
                                            <th>Valor</th>
                                            <th>Fecha de inicio</th>
                                            <th></th>
                                        </tr>
                                    </thead>

                                    <% for (Object objeto : lstObjeto) {
                                            Periodo periodo = (Periodo) objeto;
                                    %>
                                    <tr>
                                        <td><a href="<% out.print(urlSistema); %>Definiciones/DefPeriodo.jsp?MODO=<% out.print(Enumerado.Modo.DELETE); %>&pPeriCod=<% out.print(periodo.getPeriCod()); %>" name="btn_eliminar" id="btn_eliminar" title="Eliminar" class="glyphicon glyphicon-trash"/></td>
                                        <td><a href="<% out.print(urlSistema); %>Definiciones/DefPeriodo.jsp?MODO=<% out.print(Enumerado.Modo.UPDATE); %>&pPeriCod=<% out.print(periodo.getPeriCod()); %>" name="btn_editar" id="btn_editar" title="Editar" class='glyphicon glyphicon-edit'/></td>
                                        <td><% out.print(utilidad.NuloToVacio(periodo.getPeriCod())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(periodo.getPerTpo().getTipoPeriodoNombre())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(periodo.getPerVal())); %> </td>
                                        <td><% out.print(utilidad.NuloToVacio(periodo.getPerFchIni())); %> </td>
                                        <td>
                                            <a href="<% out.print(urlSistema); %>Definiciones/DefPeriodoEstudioSWW.jsp?MODO=<%=Modo.UPDATE%>&pPeriCod=<%=periodo.getPeriCod()%>" title="Estudios" class="fa fa-book" style="margin-right: 15px;"/>
                                            <% out.print("<a href='#' data-codigo='" + periodo.getPeriCod() + "' data-toggle='modal' data-target='#PopUpInscGeneracion' name='btn_generacion' id='btn_generacion' title='Agregar por generación' class='fa fa-group btn_generacion'/>"); %>
                                        </td>

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

        <div id="PopUpAgregar" class="modal fade" role="dialog">
            <!-- Modal -->
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Periodo</h4>
                    </div>

                    <div class="modal-body">

                        <div><label>Tipo</label>
                            <select class="form-control" id="PerTpo" name="PerTpo" placeholder="PerTpo">
                                <%
                                    for (TipoPeriodo tpoPer : TipoPeriodo.values()) {
                                        out.println("<option value='" + tpoPer.getTipoPeriodo() + "'>" + tpoPer.getTipoPeriodoNombre() + "</option>");
                                    }
                                %>

                            </select> 
                        </div>
                        <div><label>Valor</label><input type="number" class="form-control" id="PerVal" name="PerVal" value="" ></div>
                        <div><label>Fecha de inicio</label><input type="date" class="form-control" id="PerFchIni" name="PerFchIni" value="" ></div>
                    </div>
                    <div class="modal-footer">
                        <input name="btn_guardar" id="btn_guardar" value="Guardar" class="btn btn-success" type="button" />
                        <input type="button" class="btn btn-default" value="Cancelar" data-dismiss="modal" />
                    </div>
                </div>
            </div>

            <script type="text/javascript">

                $(document).ready(function () {

                    $(document).on('click', "#btn_guardar", function () {

                        var PerTpo = $('select[name=PerTpo]').val();
                        var PerVal = $('#PerVal').val();
                        var PerFchIni = $('#PerFchIni').val();

                        if (PerVal == '')
                        {
                            MostrarMensaje("ERROR", "Completa los datos papa");
                        } else
                        {


                            // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                            $.post('<% out.print(urlSistema); %>ABM_Periodo', {
                                pPerTpo: PerTpo,
                                pPerVal: PerVal,
                                pPerFchIni: PerFchIni,
                                pAction: "INSERT"
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

                    });


                });
            </script>
        </div>

        <div id="PopUpInscGeneracion" class="modal fade" role="dialog">
            <!-- Modal -->
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Inscripción</h4>
                    </div>

                    <div class="modal-body">
                        <p>Ingrese la generación que desea inscribir a este periodo</p>
                        <label>Generación:</label> <input type="number" name="InsGenAnio" id="InsGenAnio" min="1" step="1" class="" max="2049">
                    </div>
                    <div class="modal-footer">
                        <input name="btn_inscribir" id="btn_inscribir" value="Inscribir" class="btn btn-success" type="button" />
                        <input type="button" class="btn btn-default" value="Cancelar" data-dismiss="modal" />
                    </div>
                </div>
            </div>

            <script type="text/javascript">

                $(document).ready(function () {

                    $('#InsGenAnio').val(new Date().getFullYear());

                    $('.btn_generacion').on('click', function (e) {

                        var codigo = $(this).data("codigo");

                        $('#btn_inscribir').data('codigo', codigo);


                    });

                    $(document).on('click', "#btn_inscribir", function () {

                        var InsGenAnio = $('#InsGenAnio').val();
                        var codigo = $('#btn_inscribir').data('codigo');

                        if (InsGenAnio == '')
                        {
                            MostrarMensaje("ERROR", "Complete los datos");
                        } else
                        {


                            // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                            $.post('<% out.print(urlSistema);%>ABM_PeriodoEstudioAlumno', {
                                pPeriCod: codigo,
                                pInsGenAnio: InsGenAnio,
                                pAction: "INGRESAR_GENERACION"
                            }, function (responseText) {
                                var obj = JSON.parse(responseText);

                                MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                                if (obj.tipoMensaje != 'ERROR')
                                {
                                    location.reload();
                                } 

                            });

                        }

                    });

                });
            </script>
        </div>
    </body>
</html>
