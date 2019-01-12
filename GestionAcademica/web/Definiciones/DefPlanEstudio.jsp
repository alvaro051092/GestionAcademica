<%-- 
    Document   : DefPlanEstudio
    Created on : jul 7, 2017, 8:10:58 p.m.
    Author     : aa
--%>

<%@page import="Entidad.Carrera"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Logica.LoCarrera"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Enumerado.Modo"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="Entidad.PlanEstudio"%>
<%@page import="Utiles.Utilidades"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    Utilidades utilidad = Utilidades.GetInstancia();
    PlanEstudio plan = new PlanEstudio();
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
    Modo mode = Modo.valueOf(request.getParameter("MODO"));
    String js_redirect = "location.replace('" + urlSistema + "Definiciones/DefPlanEstudioSWW.jsp?MODO=" + Enumerado.Modo.DISPLAY + "&pCarCod=" + CarCod + "');";
    String boton = "";

    Carrera car = new Carrera();
    if (mode.equals(Modo.UPDATE) || mode.equals(Modo.DISPLAY) || mode.equals(Modo.DELETE)) {
        Retorno_MsgObj retorno = (Retorno_MsgObj) loCar.obtener(Long.valueOf(CarCod));
        if (!retorno.SurgioErrorObjetoRequerido()) {
            car = (Carrera) retorno.getObjeto();
        } else {
            out.print(retorno.getMensaje().toString());
        }
    }

    if (mode.equals(Modo.UPDATE) || mode.equals(Modo.DISPLAY) || mode.equals(Modo.DELETE)) {
        plan = car.getPlanEstudioById(Long.valueOf(PlaEstCod));
    }

    String CamposActivos    = "disabled";
    String nameButton       = "CONFIRMAR";
    String nameClass        = "btn-primary";
    
    switch (mode) {
        case INSERT:
            CamposActivos   = "enabled";
            break;
        case DELETE:
            nameButton      = "ELIMINAR";
            nameClass       = "btn-danger";
            break;
        case UPDATE:
            CamposActivos   = "enabled";
            nameButton      = "MODIFICAR";
            break;
    }
%>

<!DOCTYPE html>
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
            
                function validarDatos()
                {
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
                    var PlaEstCod = $('#PlaEstCod').val();
                    var PlaEstNom = $('#PlaEstNom').val();
                    var PlaEstDsc = $('#PlaEstDsc').val();
                    var PlaEstCre = $('#PlaEstCre').val();
                    var CarCod = $('#CarCod').val();

                    var modo = $('#MODO').val();

                    // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                    $.post('<% out.print(urlSistema); %>ABM_PlanEstudio', 
                    {
                        pPlaEstCod: PlaEstCod,
                        pPlaEstNom: PlaEstNom,
                        pPlaEstDsc: PlaEstDsc,
                        pPlaEstCreNec: PlaEstCre,
                        pCarCod: CarCod,
                        pAction: modo
                    }, function (responseText) {
                        var obj = JSON.parse(responseText);

                        MostrarMensaje(obj.tipoMensaje, obj.mensaje);

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
                        <jsp:include page="/Definiciones/DefPlanEstudioTabs.jsp"/>                        
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
                                                                <input type="hidden" name="CarCod" id="CarCod" value="<% out.print(CarCod); %>">
                                                            </div>

                                                            <div class="form-group "><label for="cname" class="control-label col-lg-3">Código</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="PlaEstCod" name="PlaEstCod" disabled value="<% out.print(utilidad.NuloToVacio(plan.getPlaEstCod())); %>"></div></div>
                                                            <div class="form-group "><label for="cname" class="control-label col-lg-3">Nombre</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PlaEstNom" name="PlaEstNom" required  <% out.print(CamposActivos); %> value="<% out.print(utilidad.NuloToVacio(plan.getPlaEstNom())); %>"></div></div>
                                                            <div class="form-group "><label for="cname" class="control-label col-lg-3">Descripción</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PlaEstDsc" name="PlaEstDsc" required  <% out.print(CamposActivos); %> value="<% out.print(utilidad.NuloToVacio(plan.getPlaEstDsc())); %>"></div></div>
                                                            <div class="form-group "><label for="cname" class="control-label col-lg-3">Creditos Necesarios</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="PlaEstCre" name="PlaEstCre" required <% out.print(CamposActivos); %> value="<% out.print(utilidad.NuloToCero(plan.getPlaEstCreNec())); %>"></div></div>                                    

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