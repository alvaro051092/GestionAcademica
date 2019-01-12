<%-- 
    Document   : DefPeriodo
    Created on : 03-jul-2017, 18:29:13
    Author     : alvar
--%>

<%@page import="Enumerado.TipoPeriodo"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Moodle.MoodleCategory"%>
<%@page import="Entidad.Parametro"%>
<%@page import="Logica.LoParametro"%>
<%@page import="Logica.LoCategoria"%>
<%@page import="Entidad.Periodo"%>
<%@page import="Logica.LoPeriodo"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Enumerado.Modo"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

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
    Modo Mode = Modo.valueOf(request.getParameter("MODO"));
    String PeriCod = request.getParameter("pPeriCod");
    String js_redirect = "window.location.replace('" + urlSistema + "Definiciones/DefPeriodoWW.jsp');";

    Periodo periodo = new Periodo();

    if (Mode.equals(Modo.UPDATE) || Mode.equals(Modo.DISPLAY) || Mode.equals(Modo.DELETE)) {
        Retorno_MsgObj retorno = (Retorno_MsgObj) loPeriodo.obtener(Long.valueOf(PeriCod));

        if (!retorno.SurgioErrorObjetoRequerido()) {
            periodo = (Periodo) retorno.getObjeto();
        } else {
            out.print(retorno.getMensaje().toString());
        }
    }

    String CamposActivos    = "disabled";
    String nameButton       = "CONFIRMAR";
    String nameClass        = "btn-primary";

    switch (Mode) {
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

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Periodo</title>
        <jsp:include page="/masterPage/head.jsp"/>

        <script>
            $(document).ready(function () {
                $('#btn_guardar').click(function (event) {
                    if($(this).data("accion") == "<%=Mode.DELETE%>")
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
                
                function validarDatos(){
                    
                    if(!$('#frm_general')[0].checkValidity())
                    {
                        var $myForm = $('#frm_general');
                        $myForm.find(':submit').click();
                        return false;
                    }

                    return true;
                }
                
            });
            
            function procesarDatos() {


                    var PeriCod = $('#PeriCod').val();
                    var PerTpo = $('select[name=PerTpo]').val();
                    var PerVal = $('#PerVal').val();
                    var PerFchIni = $('#PerFchIni').val();

                    var modo = $('#MODO').val();

                   

                        // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                        $.post('<% out.print(urlSistema); %>ABM_Periodo', {
                            pPeriCod: PeriCod,
                            pPerTpo: PerTpo,
                            pPerVal: PerVal,
                            pPerFchIni: PerFchIni,
                            pAction: modo
                        }, function (responseText) {
                            var obj = JSON.parse(responseText);

                            if (obj.tipoMensaje != 'ERROR')
                            {
            <%
                                                    out.print(js_redirect);
            %>
                            } else
                            {
                                MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                            }

                        });

                    
                }

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
                        <!-- TABS -->
                        <jsp:include page="/Definiciones/DefPeriodoTabs.jsp"/>
			<div class="panel-body">
                            <div class="tab-content">
                                <div id="inicio" class="tab-pane active">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <section class="panel">
                                                
                                                <div class="panel-body">
                                                    <div class=" form">
                                                        
                                                        <div style="display:none" id="datos_ocultos" name="datos_ocultos">
                                                            <input type="hidden" name="MODO" id="MODO" value="<% out.print(Mode); %>">
                                                        </div>
                                                        <form name="frm_general" id="frm_general" class="cmxform form-horizontal " >
                                                            <!-- CONTENIDO -->
                                                            <div class="form-group "><label for="PeriCod" class="control-label col-lg-3">Código</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="PeriCod" name="PeriCod" disabled value="<%=utilidad.NuloToVacio(periodo.getPeriCod())%>" ></div></div>
                                                            <div class="form-group "><label for="PerFchIni" class="control-label col-lg-3">Inicio</label><div class="col-lg-6"><input type="date" required class=" form-control inputs_generales" id="PerFchIni" name="PerFchIni" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(periodo.getPerFchIni())%>" ></div></div>
                                                            <div class="form-group ">
                                                                <label for="PerVal" class="control-label col-lg-3">Período</label>
                                                                <div class="col-lg-2">
                                                                    <input type="number" required step="0.1" class=" form-control inputs_generales" id="PerVal" name="PerVal" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(periodo.getPerVal())%>" >
                                                                </div>
                                                                <div class="col-lg-4">
                                                                    <select class="form-control inputs_generales" <%=CamposActivos%> id="PerTpo" name="PerTpo" placeholder="PerTpo">
                                                                        <%
                                                                            for (TipoPeriodo tpoPer : TipoPeriodo.values()) {
                                                                                if (tpoPer.equals(periodo.getPerTpo())) {
                                                                                    out.println("<option value='" + tpoPer.getTipoPeriodo() + "' selected>" + tpoPer.getTipoPeriodoNombre() + "</option>");
                                                                                } else {
                                                                                    out.println("<option value='" + tpoPer.getTipoPeriodo() + "'>" + tpoPer.getTipoPeriodoNombre() + "</option>");
                                                                                }
                                                                            }
                                                                        %>

                                                                    </select> 
                                                                </div>
                                                            </div>
                                                            
                                                            <div class="form-group">
                                                                <div class="col-lg-offset-3 col-lg-6">
                                                                    <input type="submit" style="display:none;">
                                                                    <input name="btn_guardar" id="btn_guardar"  type="button"  class="btn <%=nameClass%>" data-accion="<%=Mode%>" value="<%=nameButton%>" />
                                                                    <input value="Cancelar" class="btn btn-default" type="button" onclick="<% out.print(js_redirect);%>"/>
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
