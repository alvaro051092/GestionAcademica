<%-- 
    Document   : DefCalendario
    Created on : 03-jul-2017, 18:29:13
    Author     : alvar
--%>

<%@page import="Logica.Seguridad"%>
<%@page import="Enumerado.NombreSesiones"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Moodle.MoodleCategory"%>
<%@page import="Entidad.Parametro"%>
<%@page import="Logica.LoParametro"%>
<%@page import="Logica.LoCategoria"%>
<%@page import="Entidad.Calendario"%>
<%@page import="Logica.LoCalendario"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Enumerado.Modo"%>
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
    Modo Mode = Modo.valueOf(request.getParameter("MODO"));
    String CalCod = request.getParameter("pCalCod");

    Calendario calendario = new Calendario();

    if (Mode.equals(Modo.UPDATE) || Mode.equals(Modo.DISPLAY) || Mode.equals(Modo.DELETE)) {
        Retorno_MsgObj retorno = (Retorno_MsgObj) loCalendario.obtener(Long.valueOf(CalCod));

        if (!retorno.SurgioErrorObjetoRequerido()) {
            calendario = (Calendario) retorno.getObjeto();
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

    String ret = request.getParameter("RET");
    String urlRet = urlSistema + "Definiciones/DefCalendarioWW.jsp";
    if (ret != null) {
        if (!ret.isEmpty()) {
            urlRet = urlSistema + "Definiciones/DefCalendarioGrid.jsp";
        }
    }

    String js_redirect = "window.location.replace('" + urlRet + "');";

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Calendario</title>
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


                    var CalCod = $('#CalCod').val();
                    var EvlCod = $('#EvlCod').val();
                    var CalFch = $('#CalFch').val();
                    var EvlInsFchDsd = $('#EvlInsFchDsd').val();
                    var EvlInsFchHst = $('#EvlInsFchHst').val();

                    var modo = $('#MODO').val();

                    if (CalFch == '')
                    {
                        MostrarMensaje("ERROR", "Completa los datos papa");
                    } else
                    {


                        // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                        $.post('<% out.print(urlSistema); %>ABM_Calendario', {
                            pCalCod: CalCod,
                            pEvlCod: EvlCod,
                            pCalFch: CalFch,
                            pEvlInsFchDsd: EvlInsFchDsd,
                            pEvlInsFchHst: EvlInsFchHst,
                            pAction: modo
                        }, function (responseText) {
                            var obj = JSON.parse(responseText);

                            if (obj.tipoMensaje != 'ERROR')
                            {
                                <%=js_redirect%>
                            } else
                            {
                                MostrarMensaje(obj.tipoMensaje, obj.mensaje);
                            }

                        });

                    }
                }

        </script>
        
        <script src="<%=request.getContextPath()%>/JavaScript/DataTable/extensions/Responsive/js/dataTables.responsive.min.js"></script>
        <link href="<%=request.getContextPath()%>/JavaScript/DataTable/extensions/Responsive/css/responsive.dataTables.min.css" rel="stylesheet" type="text/css"/>


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
                        <jsp:include page="/Definiciones/DefCalendarioTabs.jsp"/>
            		<div class="panel-body">
                            <div class="tab-content">
                                <div id="inicio" class="tab-pane active">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <section class="panel">
                                                
                                                <div class="panel-body">
                                                    <div class=" form">
                                                        <form name="frm_general" id="frm_general" class="cmxform form-horizontal " >
                                                            <!-- CONTENIDO -->
                                                            <div style="display:none" id="datos_ocultos" name="datos_ocultos">
                                                                <input type="hidden" name="MODO" id="MODO" value="<% out.print(Mode); %>">
                                                            </div>
                                                            
                                                            <div class="form-group "><label for="CalCod" class="control-label col-lg-3">Código</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="CalCod" name="CalCod" disabled value="<%=utilidad.NuloToVacio(calendario.getCalCod())%>" ></div></div>
                                                            <div class="form-group ">
                                                                <label for="EvlCod" class="control-label col-lg-3">Evaluación</label>
                                                                 
                                                                    <div class="col-lg-1">
                                                                        <input type="number" required class="form-control inputs_generales" id="EvlCod" name="EvlCod" disabled value="<% out.print(utilidad.NuloToVacio((calendario.getEvaluacion() == null ? "" : calendario.getEvaluacion().getEvlCod()))); %>" >
                                                                    </div>
                                                                    <div class="col-lg-3">
                                                                        <input type="text" class="form-control inputs_generales" id="EvlNom" name="EvlNom"  disabled value="<% out.print(utilidad.NuloToVacio((calendario.getEvaluacion() == null ? "" : calendario.getEvaluacion().getEvlNom()))); %>" >
                                                                    </div>
                                                                    <div class="col-lg-2">
                                                                        <a href="#" id="btnEvlCod" name="btnEvlCod" class="glyphicon glyphicon-search" data-toggle="modal" data-target="#PopUpEvaluacion"></a>
                                                                    </div>
                                                              
                                                            </div>
                                                            
                                                            <div class="form-group "><label for="CalFch" class="control-label col-lg-3">Fecha</label><div class="col-lg-6"><input type="date" required class="form-control inputs_generales" id="CalFch" name="CalFch"<% out.print(CamposActivos); %> value="<% out.print(utilidad.NuloToVacio(calendario.getCalFch())); %>" ></div></div>
                                                            <div class="form-group "><label for="EvlInsFchDsd" class="control-label col-lg-3">Inscripcion desde</label><div class="col-lg-6"><input type="date" class="form-control inputs_generales" id="EvlInsFchDsd" name="EvlInsFchDsd"  <% out.print(CamposActivos); %> value="<% out.print(utilidad.NuloToVacio(calendario.getEvlInsFchDsd())); %>" ></div></div>
                                                            <div class="form-group "><label for="EvlInsFchHst" class="control-label col-lg-3">Inscripcion hasta</label><div class="col-lg-6"><input type="date" class="form-control inputs_generales" id="EvlInsFchHst" name="EvlInsFchHst"  <% out.print(CamposActivos); %> value="<% out.print(utilidad.NuloToVacio(calendario.getEvlInsFchHst())); %>" ></div></div>


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
        
        <div id="PopUpEvaluacion"  class="modal fade" role="dialog">
            <jsp:include page="/PopUps/PopUpEvaluacion.jsp"/>
        </div>
        
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
