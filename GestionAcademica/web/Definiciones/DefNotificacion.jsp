<%-- 
    Document   : DefModulo
    Created on : 04-jul-2017, 19:31:27
    Author     : alvar
--%>

<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Enumerado.TipoRepeticion"%>
<%@page import="Enumerado.TipoEnvio"%>
<%@page import="Enumerado.ObtenerDestinatario"%>
<%@page import="Enumerado.TipoNotificacion"%>
<%@page import="Logica.LoNotificacion"%>
<%@page import="Entidad.Notificacion"%>
<%@page import="Enumerado.TipoMensaje"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.TipoPeriodo"%>
<%@page import="Entidad.Modulo"%>
<%@page import="Entidad.Curso"%>
<%@page import="Enumerado.Modo"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.LoCurso"%>
<%@page import="Entidad.Parametro"%>
<%@page import="Logica.LoParametro"%>
<%
    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema = utilidad.GetUrlSistema();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    //----------------------------------------------------------------------------------------------------
    //CONTROL DE ACCESO
    //----------------------------------------------------------------------------------------------------
    String usuario = (String) session.getAttribute(Enumerado.NombreSesiones.USUARIO.getValor());
    Boolean esAdm = (Boolean) session.getAttribute(Enumerado.NombreSesiones.USUARIO_ADM.getValor());
    Boolean esAlu = (Boolean) session.getAttribute(Enumerado.NombreSesiones.USUARIO_ALU.getValor());
    Boolean esDoc = (Boolean) session.getAttribute(Enumerado.NombreSesiones.USUARIO_DOC.getValor());
    Retorno_MsgObj acceso = Logica.Seguridad.GetInstancia().ControlarAcceso(usuario, esAdm, esDoc, esAlu, utilidad.GetPaginaActual(request));

    if (acceso.SurgioError()) {
        response.sendRedirect((String) acceso.getObjeto());
    }

    //----------------------------------------------------------------------------------------------------
    
    Date fechaActual = new Date();
    Modo Mode = Modo.valueOf(request.getParameter("MODO"));
    String NotCod = request.getParameter("pNotCod");
    String js_redirect = "window.location.replace('" + urlSistema + "Definiciones/DefNotificacionWW.jsp');";

    Notificacion notificacion = new Notificacion();

    if (Mode.equals(Modo.UPDATE) || Mode.equals(Modo.DISPLAY) || Mode.equals(Modo.DELETE)) {
        Retorno_MsgObj retorno = (Retorno_MsgObj) LoNotificacion.GetInstancia().obtener(Long.valueOf(NotCod));
        if (!retorno.SurgioError()) {
            notificacion = (Notificacion) retorno.getObjeto();
        } else {
            out.print(retorno.getMensaje().toString());
        }

    }

    String notInterna       = "disabled";
    String CamposActivos    = "disabled";
    String nameButton       = "CONFIRMAR";
    String nameClass        = "btn-primary";

    switch (Mode) {
        case INSERT:
            CamposActivos = "enabled";
            notInterna    = (notificacion.getNotInt() == true ? "disabled" : "");
            break;
        case DELETE:
            nameButton    = "ELIMINAR";
            nameClass     = "btn-danger";
            break;
        case UPDATE:
            CamposActivos = "enabled";
            nameButton    = "MODIFICAR";
            notInterna    = (notificacion.getNotInt() == true ? "disabled" : "");
            break;
    }

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Notificacion</title>
        <jsp:include page="/masterPage/head.jsp"/>

        <link href="<% out.print(urlSistema); %>JavaScript/summernote/summernote.css" rel="stylesheet">
        <script src="<% out.print(urlSistema); %>JavaScript/summernote/summernote.js"></script>
        <script src="<% out.print(urlSistema); %>JavaScript/summernote/lang/summernote-es-ES.js"></script>

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
                
                


                $('#NotCon').summernote({
                    lang: 'es-ES',
                    height: 300
                });

            });
            
            function procesarDatos() {

                    var NotCod = $('#NotCod').val();
                    var NotAsu = $('#NotAsu').val();
                    var NotCon = $('#NotCon').summernote('code'); //$('#NotCon').val();
                    var NotDsc = $('#NotDsc').val();
                    var NotNom = $('#NotNom').val();
                    var NotRepHst = $('#NotRepHst').val();
                    var NotRepVal = $('#NotRepVal').val();
                    var NotFchDsd = $('#NotFchDsd').val();

                    var NotWeb = document.getElementById('NotWeb').checked;
                    var NotEmail = document.getElementById('NotEmail').checked;
                    var NotApp = document.getElementById('NotApp').checked;
                    var NotAct = document.getElementById('NotAct').checked;

                    var NotTpo = $('select[name=NotTpo]').val();
                    var NotTpoEnv = $('select[name=NotTpoEnv]').val();
                    var NotObtDest = $('select[name=NotObtDest]').val();
                    var NotRepTpo = $('select[name=NotRepTpo]').val();


                    var Modo = $('#MODO').val();

                    if (NotNom == '')
                    {
                        MostrarMensaje("ERROR", "Completa los datos papa");
                    } else
                    {



                        // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                        $.post('<% out.print(urlSistema); %>ABM_Notificacion', {
                            pNotCod: NotCod,
                            pNotAct: NotAct,
                            pNotApp: NotApp,
                            pNotAsu: NotAsu,
                            pNotCon: NotCon,
                            pNotDsc: NotDsc,
                            pNotEmail: NotEmail,
                            pNotNom: NotNom,
                            pNotObtDest: NotObtDest,
                            pNotRepHst: NotRepHst,
                            pNotRepTpo: NotRepTpo,
                            pNotRepVal: NotRepVal,
                            pNotTpo: NotTpo,
                            pNotTpoEnv: NotTpoEnv,
                            pNotWeb: NotWeb,
                            pNotFchDsd: NotFchDsd,
                            pAction: Modo
                        }, function (responseText) {
                            var obj = JSON.parse(responseText);

                            MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                            if (obj.tipoMensaje != 'ERROR')
                            {
            <%
                                    out.print(js_redirect);
            %>
                            }

                        });


                    }
                }

        </script>
        
        <style>
            .btn-default{
                color: #333 !important;
                background-color: #fff !important;
                border-color: #ccc !important;
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
                        <jsp:include page="/Definiciones/DefNotificacionTabs.jsp"/>
			<div class="panel-body">
                            <div class="tab-content">
                                <div id="inicio" class="tab-pane active">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <section class="panel">
                                                
                                                <div class="panel-body">
                                                    <div class=" form">
                                                        <div name="datos_ocultos">
                                                          <input type="hidden" name="MODO" id="MODO" value="<%=Mode%>">
                                                        </div>
                              
                                                        
                                                        <form name="frm_general" id="frm_general" class="cmxform form-horizontal " >
                                                            <!-- CONTENIDO -->
                                                        
                                                            <div class="form-group "><label for="NotCod" class="control-label col-lg-3">Código</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="NotCod" name="NotCod" disabled value="<%=utilidad.NuloToVacio(notificacion.getNotCod())%>" ></div></div>
                                                            <div class="form-group "><label for="NotNom" class="control-label col-lg-3">Nombre</label><div class="col-lg-6"><input required type="text" class=" form-control inputs_generales" id="NotNom" name="NotNom" <%=notInterna%> value="<%=utilidad.NuloToVacio(notificacion.getNotNom())%>" ></div></div>
                                                            <div class="form-group "><label for="NotDsc" class="control-label col-lg-3">Descripción</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="NotDsc" name="NotDsc" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(notificacion.getNotDsc())%>" ></div></div>

                                                            <div class="form-group "><label for="NotInt" class="control-label col-lg-3">Notificación de sistema</label><div class="col-lg-6"><input type="checkbox"  id="NotInt" name="NotInt" disabled <%=utilidad.BooleanToChecked(notificacion.getNotInt())%> ></div></div>
                                                            <div class="form-group "><label for="NotAct" class="control-label col-lg-3">Activa</label><div class="col-lg-6"><input type="checkbox"  id="NotAct" name="NotAct" <%=CamposActivos%> <%=utilidad.BooleanToChecked(notificacion.getNotAct())%> ></div></div>
                                                            
                                                            <div class="formulario_borde"></div>
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2>Detalle de notificacion</h2>
                                                            </div>

                                                            <div class="form-group "><label for="NotFchDsd" class="control-label col-lg-3">Ejecutar desde</label><div class="col-lg-6"><input type="datetime-local" class=" form-control inputs_generales" id="NotFchDsd" name="NotFchDsd" <%=CamposActivos%> value="<% out.print((notificacion.getNotFchDsd() != null ? dateFormat.format(notificacion.getNotFchDsd()) : dateFormat.format(fechaActual))); %>" ></div></div>
                                                            <div class="form-group ">
                                                                <label for="NotTpo" class="control-label col-lg-3">Tipo</label>
                                                                <div class="col-lg-6">
                                                                    <select class="form-control inputs_generales" id="NotTpo" name="NotTpo" <% out.print(notInterna); %> >
                                                                        <%
                                                                            for (TipoNotificacion tpoNot : TipoNotificacion.values()) {

                                                                                String seleccionado = "";
                                                                                if (notificacion.getNotTpo() != null) {
                                                                                    if (notificacion.getNotTpo().equals(tpoNot)) {
                                                                                        seleccionado = "selected";
                                                                                    }
                                                                                }

                                                                                out.println("<option " + seleccionado + " value='" + tpoNot.getValor() + "'>" + tpoNot.getNombre() + "</option>");

                                                                            }
                                                                        %>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="form-group ">
                                                                <label for="NotTpoEnv" class="control-label col-lg-3">Tipo de envío</label>
                                                                <div class="col-lg-6">
                                                                    <select class="form-control inputs_generales" id="NotTpoEnv" name="NotTpoEnv" <% out.print(notInterna); %>>
                                                                        <%
                                                                            for (TipoEnvio tpoEnv : TipoEnvio.values()) {

                                                                                String seleccionado = "";
                                                                                if (notificacion.getNotTpoEnv() != null) {
                                                                                    if (notificacion.getNotTpoEnv().equals(tpoEnv)) {
                                                                                        seleccionado = "selected";
                                                                                    }
                                                                                }

                                                                                out.println("<option " + seleccionado + " value='" + tpoEnv.getValor() + "'>" + tpoEnv.getNombre() + "</option>");

                                                                            }
                                                                        %>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="form-group ">
                                                                <label for="NotObtDest" class="control-label col-lg-3">Obtener destinatario</label>
                                                                <div class="col-lg-6">
                                                                    
                                                                        <select class="form-control inputs_generales" id="NotObtDest" name="NotObtDest"  <% out.print(notInterna); %>>
                                                                        <%
                                                                            for (ObtenerDestinatario obtDestinatario : ObtenerDestinatario.values()) {

                                                                                String seleccionado = "";
                                                                                if (notificacion.getNotObtDest() != null) {
                                                                                    if (notificacion.getNotObtDest().equals(obtDestinatario)) {
                                                                                        seleccionado = "selected";
                                                                                    }
                                                                                }

                                                                                out.println("<option " + seleccionado + " value='" + obtDestinatario.getValor() + "'>" + obtDestinatario.getNombre() + "</option>");

                                                                            }
                                                                        %>
                                                                    </select>
                                                                
                                                                </div>
                                                            </div>
                                                            
                                                            <div class="formulario_borde"></div>
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2>Repetición</h2>
                                                            </div>

                                                            <div class="form-group ">
                                                                <label for="NotRepVal" class="control-label col-lg-3">Cada</label>
                                                                <div class="col-lg-2">
                                                                    <input type="number" class=" form-control inputs_generales" id="NotRepVal" name="NotRepVal" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(notificacion.getNotRepVal())%>" >
                                                                </div>
                                                                
                                                                <div class="col-lg-4">
                                                                    <select class="form-control inputs_generales" id="NotRepTpo" name="NotRepTpo" <% out.print(CamposActivos); %> >
                                                                        <%
                                                                            for (TipoRepeticion tpoRep : TipoRepeticion.values()) {

                                                                                String seleccionado = "";
                                                                                if (notificacion.getNotRepTpo() != null) {
                                                                                    if (notificacion.getNotRepTpo().equals(tpoRep)) {
                                                                                        seleccionado = "selected";
                                                                                    }
                                                                                }

                                                                                out.println("<option " + seleccionado + " value='" + tpoRep.getValor() + "'>" + tpoRep.getNombre() + "</option>");

                                                                            }
                                                                        %>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            
                                                            
                                                            <div class="form-group "><label for="NotRepHst" class="control-label col-lg-3">Hasta</label><div class="col-lg-6"><input type="datetime-local" class=" form-control inputs_generales" id="NotRepHst" name="NotRepHst" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(notificacion.getNotRepHst())%>" ></div></div>
                                                            
                                                            <div class="formulario_borde"></div>
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2>Medios</h2>
                                                            </div>

                                                            <div class="form-group "><label for="NotEmail" class="control-label col-lg-3">Email</label><div class="col-lg-6"><input type="checkbox" id="NotEmail" name="NotEmail" <%=CamposActivos%> <%=utilidad.BooleanToChecked(notificacion.getNotEmail())%> ></div></div>
                                                            <div class="form-group "><label for="NotApp" class="control-label col-lg-3">Aplicación</label><div class="col-lg-6"><input type="checkbox"  id="NotApp" name="NotApp" <%=CamposActivos%> <%=utilidad.BooleanToChecked(notificacion.getNotApp())%> ></div></div>
                                                            <div class="form-group "><label for="NotWeb" class="control-label col-lg-3">Web</label><div class="col-lg-6"><input type="checkbox"  id="NotWeb" name="NotWeb" <%=CamposActivos%> <%=utilidad.BooleanToChecked(notificacion.getNotWeb())%> ></div></div>

                                                            <div class="formulario_borde"></div>
                                                            <div class="col-lg-offset-3 panel_contenedorTitulo">
                                                                <h2>Mensaje</h2>
                                                            </div>
                                                            
                                                            <div class="form-group "><label for="NotAsu" class="control-label col-lg-3">Asunto</label><div class="col-lg-6"><input required type="text" class=" form-control inputs_generales" id="NotAsu" name="NotAsu" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(notificacion.getNotAsu())%>" ></div></div>
                                                            <div class="form-group ">
                                                                <label for="NotCon" class="control-label col-lg-3">Contenido</label>
                                                                <div class="col-lg-6">
                                                                    <div class=" form-control inputs_generales" id="NotCon" name="NotCon"><% out.print(utilidad.NuloToVacio(notificacion.getNotCon())); %></div>
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