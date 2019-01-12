<%-- 
    Document   : DefMateria
    Created on : jul 17, 2017, 4:21:56 p.m.
    Author     : aa
--%>

<%@page import="Entidad.Carrera"%>
<%@page import="Enumerado.TipoAprobacion"%>
<%@page import="Enumerado.TipoPeriodo"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Entidad.Materia"%>
<%@page import="Logica.LoCarrera"%>
<%@page import="Entidad.PlanEstudio"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Enumerado.Modo"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Utilidades utilidad = Utilidades.GetInstancia();
    PlanEstudio plan = new PlanEstudio();
    Carrera car = new Carrera();
    Materia mat = new Materia();
    LoCarrera loCar = LoCarrera.GetInstancia();

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
    String PlaEstCod = request.getParameter("pPlaEstCod");
    String CarCod = request.getParameter("pCarCod");
    String MatCod = request.getParameter("pMatCod");
    Modo mode = Modo.valueOf(request.getParameter("MODO"));
    String js_redirect = "window.location.replace('" + urlSistema + "Definiciones/DefMateriaSWW.jsp?MODO=" + mode.DISPLAY + "&pPlaEstCod=" + PlaEstCod + "&pCarCod=" + CarCod + "');";
 
    if (mode.equals(Modo.UPDATE) || mode.equals(Modo.DISPLAY) || mode.equals(Modo.DELETE)) {
        Retorno_MsgObj retorno = (Retorno_MsgObj) loCar.obtener(Long.valueOf(CarCod));
        if (!retorno.SurgioErrorObjetoRequerido()) {
            car = (Carrera) retorno.getObjeto();
            plan = car.getPlanEstudioById(Long.valueOf(PlaEstCod));
            mat = plan.getMateriaById(Long.valueOf(MatCod));
        } else {
            out.print(retorno.getMensaje().toString());
        }
    }
    
    String CamposActivos    = "disabled";
    String nameButton       = "CONFIRMAR";
    String nameClass        = "btn-primary";

    switch (mode) {
        case INSERT:
            CamposActivos = "enabled";
            break;
        case DELETE:
            nameButton    = "ELIMINAR";
            nameClass     = "btn-danger";
            break;
        case UPDATE:
            CamposActivos = "enabled";
            nameButton    = "MODIFICAR";
            break;
    }
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Materia</title>
        <jsp:include page="/masterPage/head.jsp"/>

        <script>
            $(document).ready(function ()
            {
                $('#btn_guardar').click(function ()
                {
                    if($(this).data("accion") == "<%=mode.DELETE%>")
                    {
                        $(function () {
                            $('#PopUpConfEliminar').modal('show');
                        });
                    }
                    else
                    {
                        if(validarDatos())
                        {
                            procesarDatos();
                        }
                    }
                });
                
                $('#btn_conf_eliminar').click(function()
                {
                    procesarDatos();
                });
                    
                function validarDatos(){
                    
                    if(!$('#frm_general')[0].checkValidity())
                    {
                        var $myForm = $('#frm_general');
                        $myForm.find(':submit').click();
                        return false;
                    }

                    return true;
                }
                
                function procesarDatos()
                {
                    var vCarCod = $('#CarCod').val();
                    var PlaEstCod = $('#PlaEstCod').val();
                    var MatCod = $('#MatCod').val();
                    var MatNom = $('#MatNom').val();
                    var MatCntHor = $('#MatCntHor').val();
                    var MatTpoApr = $('select[name=MatTpoApr]').val();
                    var MatTpoPer = $('select[name=MatTpoPer]').val();
                    var MatPerVal = $('#MatPerVal').val();
                    var PreMatCod = $('#PreMatCod').val();
                    
                    var modo = $('#MODO').val();

                    // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                    $.post('<% out.print(urlSistema); %>ABM_Materia',
                    {
                        pCarCod: vCarCod,
                        pPlaEstCod: PlaEstCod,
                        pMatCod: MatCod,
                        pMatNom: MatNom,
                        pMatCntHor: MatCntHor,
                        pMatTpoApr: MatTpoApr,
                        pMatTpoPer: MatTpoPer,
                        pMatPerVal: MatPerVal,
                        pPreMatCod: PreMatCod,
                        pAction: modo
                    }, function (responseText) {
                        var obj = JSON.parse(responseText);

                        if (obj.tipoMensaje != 'ERROR')
                        {
                            <%=js_redirect%>
                        } 
                    });
                }
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
                            <jsp:include page="/Definiciones/DefMateriaTabs.jsp"/>
                        <div class="panel-body">
                            <div class="tab-content">
                                <div id="inicio" class="tab-pane active">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <section class="panel">
                                                <div class="panel-body">
                                                    <div class=" form">
                                                        <form name="frm_general" id="frm_general" class="cmxform form-horizontal " >

                                                            <!-- DATOS PERSONALES -->
                                                            <div style="display:none" id="datos_ocultos" name="datos_ocultos">
                                                                <input type="hidden" name="MODO" id="MODO" value="<% out.print(mode); %>">
                                                                <input type="hidden" name="PlaEstCod" id="PlaEstCod" value="<% out.print(PlaEstCod); %>">
                                                                <input type="hidden" name="CarCod" id="CarCod" value="<% out.print(CarCod); %>">
                                                            </div>

                                                            <div class="form-group "><label for="cname" class="control-label col-lg-3">Código</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="MatCod" name="MatCod" required disabled value="<% out.print( utilidad.NuloToVacio(mat.getMatCod())); %>"></div></div>
                                                            <div class="form-group "><label for="cname" class="control-label col-lg-3">Nombre</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="MatNom" name="MatName" required <% out.print(CamposActivos); %> value="<% out.print( utilidad.NuloToVacio(mat.getMatNom())); %>"></div></div>
                                                            <div class="form-group "><label for="cname" class="control-label col-lg-3">Cantidad de horas</label><div class="col-lg-6"><input type="number" step="0.5" class=" form-control inputs_generales" id="MatCntHor" name="MatCntHor" required <% out.print(CamposActivos); %> value="<% out.print( utilidad.NuloToVacio(mat.getMatCntHor())); %>"></div></div>

                                                            <div class="form-group ">
                                                                <label for="cname" class="control-label col-lg-3">Tipo de aprobación</label>
                                                                <div class="col-lg-6">
                                                                    <select class="form-control inputs_generales" id="MatTpoApr" name="MatTpoApr" <%out.print(CamposActivos);%>>
                                                                        <%
                                                                            for(TipoAprobacion tpoApr : TipoAprobacion.values())
                                                                            {
                                                                                if(mat.getMatTpoApr() == tpoApr)
                                                                                {
                                                                                    out.println("<option selected value='" + tpoApr.getTipoAprobacionC() + "'>" + tpoApr.getTipoAprobacionN() + "</option>");
                                                                                }
                                                                                else
                                                                                {
                                                                                    out.println("<option value='" + tpoApr.getTipoAprobacionC() + "'>" + tpoApr.getTipoAprobacionN() + "</option>");
                                                                                }
                                                                            }
                                                                        %>
                                                                    </select>
                                                                </div>
                                                            </div>

                                                            <div class="form-group ">
                                                                <label for="cname" class="control-label col-lg-3">Tipo de Período</label>
                                                                <div class="col-lg-6">
                                                                    <select class="form-control inputs_generales" id="MatTpoPer" name="MatTpoPer" <%out.print(CamposActivos);%>>
                                                                        <%
                                                                            for(TipoPeriodo tpoPer : TipoPeriodo.values())
                                                                            {
                                                                                if(mat.getMatTpoPer() == tpoPer)
                                                                                {
                                                                                    out.println("<option selected value='" + tpoPer.getTipoPeriodo() + "'>" + tpoPer.getTipoPeriodoNombre() + "</option>");
                                                                                }
                                                                                else
                                                                                {
                                                                                    out.println("<option value='" + tpoPer.getTipoPeriodo() + "'>" + tpoPer.getTipoPeriodoNombre() + "</option>");
                                                                                }
                                                                            }
                                                                        %>
                                                                    </select>
                                                                </div>
                                                            </div>

                                                            <div class="form-group "><label for="cname" class="control-label col-lg-3">Valor del Período</label><div class="col-lg-6"><input type="number" required step="0.5" class="form-control inputs_generales" id="MatPerVal" name="MatPerVal" <% out.print(CamposActivos); %> value="<% out.print( utilidad.NuloToVacio(mat.getMatPerVal())); %>"></div></div>

                                                            <div class="form-group">
                                                                <div class="col-lg-offset-3 col-lg-6">
                                                                    <input type="submit" style="display:none;">
                                                                    <input type="button" class="btn <%=nameClass%>" data-accion="<%=mode%>" name="btn_guardar" id="btn_guardar" value="<%=nameButton%>">
                                                                    <input type="button" class="btn btn-default" onclick="<%=js_redirect%>" value="CANCELAR">
                                                                </div>
                                                            </div>              
                                                        </form>
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
                                
        <!--Popup Confirmar Eliminación-->
        <div id="PopUpConfEliminar" class="modal fade" role="dialog">
            <!-- Modal -->
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Eliminar</h4>
                    </div>
                    <div class="modal-body">
                        <div>
                            <h4>Confirma eliminación?</h4>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button name="btn_conf_eliminar" id="btn_conf_eliminar" class="btn btn-danger" data-dismiss="modal" onclick="procesarDatos()">Eliminar</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>