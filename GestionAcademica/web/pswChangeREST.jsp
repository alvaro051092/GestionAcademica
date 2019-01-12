<%-- 
    Document   : pswChange
    Created on : 28-ago-2017, 19:29:24
    Author     : alvar
--%>

<%@page import="Enumerado.Constantes"%>
<%@page import="Logica.LoPersona"%>
<%@page import="Utiles.Utilidades"%>
<%@page import="Logica.Seguridad"%>
<%@page import="Utiles.Retorno_MsgObj"%>
<%@page import="Enumerado.NombreSesiones"%>
<%

    Utilidades utilidad = Utilidades.GetInstancia();
    String urlSistema = utilidad.GetUrlSistema();

    String tkn = request.getParameter("tkn");
    String usr = request.getParameter("usr");
    String usuario = Seguridad.GetInstancia().decrypt(usr, Constantes.ENCRYPT_VECTOR_INICIO.getValor(), Constantes.ENCRYPT_SEMILLA.getValor());
    String referrer = request.getHeader("referer");
    String js_redirect = "window.location.replace('" + referrer + "');";
    /*
    if(tkn != null && usr != null)
    {
        Retorno_MsgObj retorno = LoPersona.GetInstancia().ValCambiarPasswordExterno(usr, tkn);
        if(retorno.SurgioError())
        {
            response.sendRedirect(urlSistema);
        }
    }
    else
    {
        response.sendRedirect(urlSistema);
    }
    */
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sistema de Gestión Académica - Cambiar contraseña</title>
        <jsp:include page="/masterPage/head.jsp"/>
    </head>
    <body class="body_clase">
        <div class="login_fondo">		
            <div class="login_contenedor">
                
                <div class="login_contenedorImg"><img src="Imagenes/ctc.png" /></div>
                <h1 class="login_titulo">Cambiar contraseña</h1>
                <p class="login_texto">Bienvenido a Gestión, el servicio a estudiantes del Instituto CTC - Colonia. Indique su nueva contraseña</p>
                <form>
                    <div class="login_form">
                        <input type="hidden" name="usuario" id="usuario" value="<%=usuario%>">
                        <input type="password" class="form-control login_inputPass login_inputBorde" required="true" name="pswActual" id="pswActual" placeholder="Contraseña actual">
                        <input type="password" class="form-control login_inputPass login_inputBorde" required="true" name="pswNueva" id="pswNueva" placeholder="Contraseña nueva">
                        <input type="password" class="form-control login_inputPass" required="true" name="pswConfirmacion" id="pswConfirmacion" placeholder="Confirmar contraseña">

                    </div>
                    <div class="row login_contenedorBotones">
                        <div class="col-lg-6">
                            <button type="submit" name="btn_guardar" id="btn_guardar" class="login_cambiarContrasena">CONFIRMAR</button>
                        </div>
                        <div class="col-lg-6">
                            <button type="button" class="login_cambiarContrasena_cancelar" onclick="<%=js_redirect%>">CANCELAR</button>
                        </div>
                    </div>
                </form>


            </div>
        </div>

        <div>
            <div id="div_pop_bkgr" name="div_pop_bkgr"></div>

            <div id="div_cargando" name="div_cargando">
                <div class="loading"></div>
            </div>

        </div>

        <div id="msgError" name="msgError" class="alert alert-success div_msg" style="display: none;"> 
            <label id="txtError" name="txtError">Error</label>
        </div>

        <script>
            $(document).ready(function () {
                MostrarCargando(false);

                $('#btn_guardar').click(function (event) {
                    MostrarCargando(true);
                    
                    var usuario = $('#usuario').val();
                    var pswActual = $('#pswActual').val();
                    var pswNueva = $('#pswNueva').val();
                    var pswConfirmacion = $('#pswConfirmacion').val();
                    
                    
                    // Si en vez de por post lo queremos hacer por get, cambiamos el $.post por $.get
                    $.post('<% out.print(urlSistema); %>ABM_Persona', {
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
    </body>
</html>
