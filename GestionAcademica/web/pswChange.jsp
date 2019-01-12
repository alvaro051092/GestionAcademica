<%-- 
    Document   : pswChange
    Created on : 28-ago-2017, 19:29:24
    Author     : alvar
--%>

<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.NombreSesiones"%>
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
    String js_redirect = "window.location.replace('" + urlSistema + "');";

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Cambiar contraseña</title>
        <jsp:include page="/masterPage/head.jsp"/>
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
                                CAMBIAR CONTRASEÑA
                                <!--
                                <span class="tools pull-right">
                                    <a class="fa fa-chevron-down" href="javascript:;"></a>
                                 </span>
                                -->
                            </header>
                            <div class="panel-body">
                                <div class=" form">
                                    <form class="cmxform form-horizontal " >
                
                                        <input type="hidden" name="usuario" id="usuario" value="<%=usuario%>">
                
                                        <div class="form-group ">
                                            <label for="cname" class="control-label col-lg-3">Contraseña Actual</label>
                                            <div class="col-lg-6">
                                                <input class=" form-control inputs_generales" name="pswActual" id="pswActual" minlength="2" type="password" required="">
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <label for="cemail" class="control-label col-lg-3">Contraseña Nueva</label>
                                            <div class="col-lg-6">
                                                <input class="form-control inputs_generales"  type="password" name="pswNueva" id="pswNueva" required="">
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <label for="curl" class="control-label col-lg-3">Confirmar Contraseña</label>
                                            <div class="col-lg-6">
                                                <input class="form-control inputs_generales " name="pswConfirmacion" id="pswConfirmacion" type="password">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-lg-offset-3 col-lg-6">
                                                <button class="btn btn-primary" name="btn_guardar" id="btn_guardar" type="button">CONFIRMAR</button>
                                                <!--<button class="btn btn-default" type="button" >CANCELAR</button>-->
                                            </div>
                                        </div>
                                    </form>
                                </div>

                            </div>
                        </section>
                    </div>
                 </div>
        </div>

        <script>
            $(document).ready(function () {
                $('#btn_guardar').click(function (event) {
                    var usuario = $('#usuario').val();
                    var pswActual = $('#pswActual').val();
                    var pswNueva = $('#pswNueva').val();
                    var pswConfirmacion = $('#pswConfirmacion').val();


// Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                    $.post('<% out.print(urlSistema);%>ABM_Persona', {
                        usuario: usuario,
                        pswActual: pswActual,
                        pswNueva: pswNueva,
                        pswConfirmacion: pswConfirmacion,
                        pAction: "CAMBIAR_PSW"
                    }, function (responseText) {
                        var obj = JSON.parse(responseText);

                        MostrarMensaje(obj.tipoMensaje, obj.mensaje);

                        if (obj.tipoMensaje != 'ERROR')
                        {
            <%=js_redirect%>
                        }

                    });

                });
            });
        </script>
        
        <jsp:include page="/masterPage/footer.jsp"/>
    </body>
</html>
