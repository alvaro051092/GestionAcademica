<%-- 
    Document   : DefModulo
    Created on : 04-jul-2017, 19:31:27
    Author     : alvar
--%>

<%@page import="Entidad.NotificacionBitacora"%>
<%@page import="Enumerado.TipoRepeticion"%>
<%@page import="Enumerado.TipoEnvio"%>
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
    Modo Mode = Modo.valueOf(request.getParameter("MODO"));
    String NotCod = request.getParameter("pNotCod");
    String NotBitCod = request.getParameter("pNotBitCod");

    String js_redirect = "window.location.replace('" + urlSistema + "Definiciones/DefNotificacionBitacoraSWW.jsp?MODO=UPDATE&pNotCod=" + NotCod + "');";

    NotificacionBitacora bitacora = new NotificacionBitacora();

    Notificacion notificacion = new Notificacion();
    Retorno_MsgObj retorno = (Retorno_MsgObj) LoNotificacion.GetInstancia().obtener(Long.valueOf(NotCod));

    if (Mode.equals(Modo.UPDATE) || Mode.equals(Modo.DISPLAY) || Mode.equals(Modo.DELETE)) {

        if (!retorno.SurgioError()) {
            notificacion = (Notificacion) retorno.getObjeto();

            bitacora = notificacion.ObtenerBitacoraByCod(Long.valueOf(NotBitCod));

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

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Bitacora</title>
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
            
            
            function procesarDatos(){

                    var NotBitCod = $('#NotBitCod').val();
                    var NotCod = $('#NotCod').val();

                    var Modo = $('#MODO').val();

                    if (NotBitCod == '')
                    {
                        MostrarMensaje("ERROR", "Completa los datos papa");
                    } else
                    {



                        // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                        $.post('<% out.print(urlSistema); %>ABM_NotificacionBitacora', {
                            pNotBitCod: NotBitCod,
                            pNotCod: NotCod,
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
                                BITACORA
                            <!-- BOTONES -->
                            <span class="tools pull-right">
                                <a href="<% out.print(urlSistema); %>Definiciones/DefNotificacionBitacoraSWW.jsp?MODO=UPDATE&pNotCod=<% out.print(NotCod); %>">Regresar</a>
                            </span>
                        </header>
        
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
                                                            <input type="hidden" name="NotCod" id="NotCod" value="<% out.print(NotCod); %>">
                                                        </div>
                                                        <form name="frm_general" id="frm_general" class="cmxform form-horizontal " >
                                                            
                                                            <div class="form-group "><label for="NotBitCod" class="control-label col-lg-3">Código</label><div class="col-lg-6"><input type="number" class=" form-control inputs_generales" id="NotBitCod" name="NotBitCod" disabled value="<%=utilidad.NuloToVacio(bitacora.getNotBitCod())%>" ></div></div>
                                                            <div class="form-group "><label for="NotBitAsu" class="control-label col-lg-3">Asunto</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="NotBitAsu" name="NotBitAsu" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(bitacora.getNotBitAsu())%>" ></div></div>
                                                            <div class="form-group "><label for="NotBitCon" class="control-label col-lg-3">Contenido</label>
                                                                <div class="col-lg-6">
                                                                    <div class="formulario_borde"></div>
                                                                    <div class="" id="NotBitCon" name="NotBitCon">
                                                                        <%=utilidad.NuloToVacio(bitacora.getNotBitCon())%>
                                                                    </div>
                                                                    <div class="formulario_borde"></div>
                                                                </div>
                                                                    
                                                            </div>
                                                            <div class="form-group "><label for="NotBitDet" class="control-label col-lg-3">Detalle</label><div class="col-lg-6"><textarea rows="10" type="text" class=" form-control inputs_generales" id="NotBitDet" name="NotBitDet" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(bitacora.getNotBitDet())%>" ><% out.print(utilidad.NuloToVacio(bitacora.getNotBitDet())); %></textarea></div></div>
                                                            <div class="form-group "><label for="NotBitDst" class="control-label col-lg-3">Destinatarios</label><div class="col-lg-6"><textarea rows="10" type="text" class=" form-control inputs_generales" id="NotBitDst" name="NotBitDst" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(bitacora.getNotBitDst())%>" ><% out.print(utilidad.NuloToVacio(bitacora.getNotBitDst())); %></textarea></div></div>
                                                            <div class="form-group "><label for="NotBitEst" class="control-label col-lg-3">Estado</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="NotBitEst" name="NotBitEst" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(bitacora.getNotBitEst())%>" ></div></div>
                                                            <div class="form-group "><label for="NotBitFch" class="control-label col-lg-3">Fecha</label><div class="col-lg-6"><input type="text" class=" form-control inputs_generales" id="NotBitFch" name="NotBitFch" <%=CamposActivos%> value="<%=utilidad.NuloToVacio(bitacora.getNotBitFch())%>" ></div></div>
                                                            
                                                            <div class="form-group">
                                                                <div class="col-lg-offset-3 col-lg-6">
                                                                    <input type="submit" style="display:none;">
                                                                    <input type="button" class="btn <%=nameClass%>" data-accion="<%=Mode%>" name="btn_guardar" id="btn_guardar" value="<%=nameButton%>">
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