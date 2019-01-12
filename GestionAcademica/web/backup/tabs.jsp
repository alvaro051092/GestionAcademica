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
    String urlSistema = (String) session.getAttribute(NombreSesiones.URL_SISTEMA.getValor());

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
                        <header class="panel-heading tab-bg-dark-navy-blue ">
                            <ul class="nav nav-tabs">
                                <li class="active">
                                    <a class="tabs" data-toggle="tab" href="#inicio" aria-expanded="true">Inicio</a>
                                </li>
                                <li class="">
                                    <a class="tabs" data-toggle="tab" href="#tabs" aria-expanded="false">TABS</a>
                                </li>
                                <li class="">
                                    <a class="tabs" data-toggle="tab" href="#perfil" aria-expanded="false">PERFIL</a>
                                </li>
                                <li class="">
                                    <a class="tabs" data-toggle="tab" href="#contacto" aria-expanded="false">CONTACTO</a>
                                </li>
                            </ul>
                        </header>
                        <div class="panel-body">
                            <div class="tab-content">
                                <div id="inicio" class="tab-pane active">
                                    <div class="row">
                                        <div class="col-lg-12">
                                            <section class="panel">
                                                <header class="panel-heading">
                                                    CAMBIAR CONTRASEÑA
                                                    <span class="tools pull-right">
                                                        <a class="fa fa-chevron-down" href="javascript:;"></a>
                                                     </span>
                                                </header>
                                                <div class="panel-body">
                                                    <div class=" form">
                                                        <form class="cmxform form-horizontal " id="commentForm" method="get" action="" novalidate="novalidate">
                                                            <div class="form-group ">
                                                                <label for="cname" class="control-label col-lg-3">Contraseña Actual</label>
                                                                <div class="col-lg-6">
                                                                    <input class=" form-control inputs_generales" id="cname" name="name" minlength="2" type="text" required="">
                                                                </div>
                                                            </div>
                                                            <div class="form-group ">
                                                                <label for="cemail" class="control-label col-lg-3">Contraseña Nueva</label>
                                                                <div class="col-lg-6">
                                                                    <input class="form-control inputs_generales" id="cemail" type="email" name="email" required="">
                                                                </div>
                                                            </div>
                                                            <div class="form-group ">
                                                                <label for="curl" class="control-label col-lg-3">Confirmar Contraseña</label>
                                                                <div class="col-lg-6">
                                                                    <input class="form-control inputs_generales " id="curl" type="url" name="url">
                                                                </div>
                                                            </div>
                                                            <div class="form-group">
                                                                <div class="col-lg-offset-3 col-lg-6">
                                                                    <button class="btn btn-primary" type="submit">CONFIRMAR</button>
                                                                    <button class="btn btn-default" type="button">CANCELAR</button>
                                                                </div>
                                                            </div>
                                                        </form>
                                                    </div>

                                                </div>
                                            </section>
                                        </div>
                                     </div>
                                </div>
                                <div id="tabs" class="tab-pane">TABS</div>
                                <div id="perfil" class="tab-pane">PERFIL</div>
                                <div id="contacto" class="tab-pane">CONTACTO</div>
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
